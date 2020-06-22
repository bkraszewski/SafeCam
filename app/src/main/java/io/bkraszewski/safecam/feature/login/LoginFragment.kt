package io.bkraszewski.safecam.feature.login

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
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
            hideKeyboard()
            NavHostFragment.findNavController(this).navigate(R.id.cameraFragment)
        })

        observePasswordSubmission()
    }

    override fun onResume() {
        super.onResume()
        loginPassword.requestFocus()
        loginPassword.postDelayed({
            showKeyboard()
        }, 100)
    }


    private fun hideKeyboard() {
        val view: View? = requireActivity().currentFocus
        if (view != null) {
            val imm: InputMethodManager? = getInputManager()
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
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

    private fun showKeyboard() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        requireView().requestFocus()
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun getInputManager() = getSystemService(requireActivity(), InputMethodManager::class.java)
}
