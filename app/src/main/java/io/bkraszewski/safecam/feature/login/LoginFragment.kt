package io.bkraszewski.safecam.feature.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import io.bkraszewski.safecam.R
import io.bkraszewski.safecam.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val vm: LoginViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val util = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        util.vm = vm
        util.lifecycleOwner = this
        return util.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.navigateToCamera.observe(viewLifecycleOwner, Observer {
            NavHostFragment.findNavController(this).navigate(R.id.cameraFragment)
        })

        observePasswordSubmission()
    }

    private fun observePasswordSubmission() {
        loginPassword.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int,
                                        event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    vm.onPasswordSubmitted()
                    return true
                }
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()

        loginPassword.requestFocus()
    }
}
