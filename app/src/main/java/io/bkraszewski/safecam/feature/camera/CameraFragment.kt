package io.bkraszewski.safecam.feature.camera

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.bkraszewski.safecam.R
import io.bkraszewski.safecam.databinding.FragmentCameraBinding
import io.bkraszewski.safecam.extensions.toast
import kotlinx.android.synthetic.main.fragment_camera.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {

    private lateinit var outputDirectory: File
    private val vm: CameraViewModel by viewModel()

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.removeAllViews()
        val util = DataBindingUtil.inflate<FragmentCameraBinding>(inflater, R.layout.fragment_camera, container, false)
        util.lifecycleOwner = this
        util.viewModel = vm
        return util.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraAllowPermission.setOnClickListener {
            checkPermissions()
        }


        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = getOutputDirectory()
        cameraCaptureButton.setOnClickListener { takePhoto() }
        observeNavigationEvents()
        checkPermissions()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    private fun observeNavigationEvents() {
        vm.navigateToGallery.observe(viewLifecycleOwner, Observer {
            NavHostFragment.findNavController(this).navigate(R.id.browseFragment)
        })
    }

    private fun checkPermissions() {
        Dexter.withActivity(requireActivity())
            .withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (!report.areAllPermissionsGranted()) {
                        toast(R.string.camera_permission_denied)
                    } else {
                        startCamera()
                    }

                    vm.onPermissionsChecked(report.areAllPermissionsGranted())
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }

            })
            .check()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val container = view as ConstraintLayout
        val viewFinder1 = container.findViewById<PreviewView>(R.id.viewFinder)


        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder()
                .build()

            imageCapture = ImageCapture.Builder()
                //.setTargetRotation(requireView().display.rotation)
                .build()

            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                cameraProvider.unbindAll()

                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

                preview?.setSurfaceProvider(viewFinder1.createSurfaceProvider())
            } catch (exc: Exception) {
                Timber.e("Use case binding failed")
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                toast("Error saving image")
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                vm.onImageCaptured(savedUri)
                toast(msg)
            }
        })
    }

    private fun getOutputDirectory(): File {
        return File(requireActivity().cacheDir, resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        cameraExecutor.shutdown()

    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
