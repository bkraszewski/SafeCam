package io.bkraszewski.safecam.feature.camera

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import io.bkraszewski.safecam.R
import io.bkraszewski.safecam.databinding.FragmentCameraBinding
import io.bkraszewski.safecam.extensions.toast
import kotlinx.android.synthetic.main.fragment_camera.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraFragment : Fragment(), MultiplePermissionsListener {

    private val vm : CameraViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val util =  DataBindingUtil.inflate<FragmentCameraBinding>(inflater, R.layout.fragment_camera, container, false)
        util.lifecycleOwner = this
        util.viewModel = vm
        return util.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraAllowPermission.setOnClickListener {
            checkPermissions()
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        Dexter.withActivity(requireActivity())
            .withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(this)
            .check()
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        if(!report.areAllPermissionsGranted()){
            toast(R.string.camera_permission_denied)
        }

        vm.onPermissionsChecked(report.areAllPermissionsGranted())
    }

    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
        token?.continuePermissionRequest()
    }


}
