package hr.ferit.sumigaborna.dementiahelper.profile.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.hideProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.showFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.showProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessage
import hr.ferit.sumigaborna.dementiahelper.profile.data.ProfileUI
import hr.ferit.sumigaborna.dementiahelper.profile.view_model.ProfileVM
import kotlinx.android.synthetic.main.fragment_carer.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.buttonConfirmButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class  ProfileFragment: BaseFragment(){

    override fun getLayout(): Int = R.layout.fragment_profile

    private val profileViewModel by viewModel<ProfileVM>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel.profileData.observe(viewLifecycleOwner, Observer {
            fillScreenWithData(it)
        })
        profileViewModel.resetLinkResponse.observe(viewLifecycleOwner, Observer {
            if(it.message.isNotBlank()) toastMessage(it.message)
        })
        profileViewModel.getProfileInfoResponse.observe(viewLifecycleOwner, Observer {
            if(it.message.isNotBlank() && !it.success) toastMessage(it.message)
            else if(it.message.isNotBlank()) hideProgressBar(pbProfile)
        })
        profileViewModel.carerPasswordFailure.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
        profileViewModel.carerFailureData.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun fillScreenWithData(data: ProfileUI) {
        tvEmail.text = data.userEmail

        data.patientInfo.apply {
            tvPatientName.text = patientName
            tvPatientSurname.text = patientSurname
            tvPatientAddress.text = patientAddress
            tvPatientAge.text = patientAge
            tvPatientCity.text = patientCity
        }
        data.carerInfo.apply {
            tvCarerName.text = carerName
            tvCarerSurname.text = carerSurname
            tvCarerAddress.text = carerAddress
            tvCarerContactPhone.text = carerPhone
            tvCarerContactEmail.text = carerEmail
        }
    }

    private fun initUI() {
        showProgressBar(pbProfile)
        profileViewModel.getAllProfileInfo()
        profileViewModel.getCarerPassword()
        buttonAddInfo.setOnClickListener {
            activity!!.showFragment(R.id.fragmentContainer, AddEditProfileInfoFragment(),true)
        }
        buttonChangeUserPassword.setOnClickListener {
            profileViewModel.requestChangeOfUserPassword()
        }
        buttonChangeCarerPassword.setOnClickListener {
            svProfile.visibility = View.GONE
            clPasswordProfile.visibility = View.VISIBLE
        }
        buttonConfirmButton.setOnClickListener {
            if(etPasswordProfile.text!!.isNotBlank() && profileViewModel.carerPassword.isNotBlank() && etPasswordProfile.text.toString() == profileViewModel.carerPassword) {
                svProfile.visibility = View.GONE
                clPasswordProfile.visibility = View.GONE
                profileViewModel.carerPassword = etPasswordProfile.text.toString()
                val edittext = EditText(activity!!)
                AlertDialog.Builder(activity!!)
                    .setTitle("Enter a new carer password")
                    .setView(edittext)
                    .setPositiveButton("Change"){ dialog, whichButton ->
                        svProfile.visibility = View.VISIBLE
                        profileViewModel.changeCarerPassword(edittext.text.toString())
                    }
                    .setNegativeButton("Cancel") { dialog, whichButton ->
                        dialog.dismiss()
                    }
                    .show()
            }
            else toastMessage("Wrong carer password")
        }
        buttonLogOut.setOnClickListener {
            profileViewModel.logOutUser()
            activity?.finish()
        }
    }
}