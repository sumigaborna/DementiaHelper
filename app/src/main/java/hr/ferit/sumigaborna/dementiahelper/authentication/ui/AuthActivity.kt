package hr.ferit.sumigaborna.dementiahelper.authentication.ui

import android.os.Bundle
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseActivity
import hr.ferit.sumigaborna.dementiahelper.app.common.hasNetwork
import hr.ferit.sumigaborna.dementiahelper.app.common.showFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessage
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessageLong
import hr.ferit.sumigaborna.dementiahelper.authentication.view_model.AuthVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity(){

    override fun getLayout(): Int = R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hasNetwork(this)) toastMessageLong("No Internet connection, some features may not work!")
        showFragment(R.id.authFragmentContainer,LoginFragment(),false)
    }
}