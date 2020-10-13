package hr.ferit.sumigaborna.dementiahelper.profile.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.hideProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.showProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessage
import hr.ferit.sumigaborna.dementiahelper.profile.view_model.ProfileVM
import kotlinx.android.synthetic.main.fragment_add_edit_profile_info.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddEditProfileInfoFragment : BaseFragment(){

    private val profileViewModel by sharedViewModel<ProfileVM>()

    override fun getLayout(): Int = R.layout.fragment_add_edit_profile_info

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel.profileData.observe(viewLifecycleOwner, Observer {
            hideProgressBar(pbAddEditProfile)
            it.patientInfo.apply {
                etPatientName.setText(patientName)
                etPatientSurname.setText(patientSurname)
                etPatientAddress.setText(patientAddress)
                etPatientAge.setText(patientAge)
                etPatientCity.setText(patientCity)
            }
            it.carerInfo.apply {
                etCarerName.setText(carerName)
                etCarerSurname.setText(carerSurname)
                etCarerAddress.setText(carerAddress)
                etCarerContactPhone.setText(carerPhone)
                etCarerContactEmail.setText(carerEmail)
            }
        })
        profileViewModel.addEditProfileInfoResponse.observe(viewLifecycleOwner, Observer {
            if(it.message.isNotBlank()) {
                toastMessage(it.message)
                profileViewModel.resetAddEditProfileResponse()
                activity!!.onBackPressed()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        showProgressBar(pbAddEditProfile)
        profileViewModel.getAllProfileInfo()
        buttonSaveInfo.setOnClickListener {
            profileViewModel.saveProfileInfo(
                etPatientName.text.toString(),
                etPatientSurname.text.toString(),
                etPatientAddress.text.toString(),
                etPatientAge.text.toString(),
                etPatientCity.text.toString(),
                etCarerName.text.toString(),
                etCarerSurname.text.toString(),
                etCarerAddress.text.toString(),
                etCarerContactPhone.text.toString(),
                etCarerContactEmail.text.toString())
        }
        buttonCancel.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
    }
}