package hr.ferit.sumigaborna.dementiahelper.authentication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.checkLoginInput
import hr.ferit.sumigaborna.dementiahelper.app.common.showFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessage
import hr.ferit.sumigaborna.dementiahelper.app.ui.MainActivity
import hr.ferit.sumigaborna.dementiahelper.authentication.view_model.AuthVM
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment(){

    private val viewModel by sharedViewModel<AuthVM>()
    override fun getLayout(): Int = R.layout.fragment_login

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.authData.observe(viewLifecycleOwner, Observer {
            if(it.loginSuccess){
                if(it.loginSuccessMessage.isNotBlank()){
                    //toastMessage(it.loginSuccessMessage)
                    openMainActivity()
                }
            }
            else{
                if(it.loginErrorMessage.isNotBlank()) toastMessage(it.loginErrorMessage)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        viewModel.reLogUser()
        buttonLogin.setOnClickListener {
            if(checkLoginInput(
                    activity!!,
                    etEmailLogin.text.toString(),
                    etPasswordLogin.text.toString())
            ) {
                viewModel.logInUser(etEmailLogin.text.toString(),etPasswordLogin.text.toString())
                etEmailLogin.text.clear()
                etPasswordLogin.text.clear()
            }
        }
        linkRegister.setOnClickListener {
            activity!!.showFragment(R.id.authFragmentContainer,RegisterFragment(),false)
        }
    }

    private fun openMainActivity() = startActivity(Intent(activity!!,MainActivity::class.java))
}