package io.bkraszewski.safecam.feature.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import io.bkraszewski.safecam.R
import io.bkraszewski.safecam.databinding.FragmentBrowseBinding
import kotlinx.android.synthetic.main.fragment_browse.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowserFragment : Fragment() {

    private val vm: BrowserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateToCamera()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val util = DataBindingUtil.inflate<FragmentBrowseBinding>(inflater, R.layout.fragment_browse, container, false)
        util.lifecycleOwner = this
        util.vm = vm
        return util.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setActionBar(browseToolbar)

        browseBack.setOnClickListener {
            navigateToCamera()
        }
    }

    private fun navigateToCamera() {
        NavHostFragment.findNavController(this).navigate(R.id.cameraFragment)
    }


}
