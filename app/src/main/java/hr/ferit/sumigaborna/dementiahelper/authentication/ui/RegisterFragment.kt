package hr.ferit.sumigaborna.dementiahelper.authentication.ui

import android.os.Bundle
import android.view.View
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessage
import hr.ferit.sumigaborna.dementiahelper.authentication.view_model.AuthVM
import androidx.lifecycle.Observer
import hr.ferit.sumigaborna.dementiahelper.app.common.checkRegisterInput
import hr.ferit.sumigaborna.dementiahelper.app.common.showFragment
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RegisterFragment : BaseFragment(){

    private val viewModel by sharedViewModel<AuthVM>()

    override fun getLayout(): Int = R.layout.fragment_register

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.authData.observe(viewLifecycleOwner, Observer {
            if(it.registerSuccess) {
                if(it.registerSuccessMessage.isNotBlank()) {
                    toastMessage(it.registerSuccessMessage)
                    activity!!.showFragment(R.id.authFragmentContainer,LoginFragment(),false)
                }
            }
            else{
                if(it.registerErrorMessage.isNotBlank()) toastMessage(it.registerErrorMessage)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        buttonRegister.setOnClickListener {
            if(checkRegisterInput(activity!!,etEmail.text.toString(),etPassword.text.toString(),etConfirmPassword.text.toString(),etCarerPassword.text.toString(),etCarerConfirmPassword.text.toString()))
                viewModel.registerUser(etEmail.text.toString(),etPassword.text.toString(),etCarerPassword.text.toString())
        }

        linkLogin.setOnClickListener {
            activity!!.showFragment(R.id.authFragmentContainer,LoginFragment(),false)
        }
    }
}