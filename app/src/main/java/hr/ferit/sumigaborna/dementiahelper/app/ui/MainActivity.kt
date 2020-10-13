package hr.ferit.sumigaborna.dementiahelper.app.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseActivity
import hr.ferit.sumigaborna.dementiahelper.app.common.*
import hr.ferit.sumigaborna.dementiahelper.carer.ui.CarerFragment
import hr.ferit.sumigaborna.dementiahelper.home.ui.HomeFragment
import hr.ferit.sumigaborna.dementiahelper.patient.ui.PatientFragment
import hr.ferit.sumigaborna.dementiahelper.profile.ui.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.inject

class MainActivity : BaseActivity() {

    private val mediaPlayer by inject<MediaPlayer>()
    private val vibrator by inject<Vibrator>()

    override fun getLayout(): Int = R.layout.activity_main

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount>1){
            supportFragmentManager.popBackStack()
        }
        else {
            super.onBackPressed()
            selectHomeFragmentOnNavigationBar()
            displayFragment(HomeFragment(), false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    override fun onResume() {
        super.onResume()
        if(!hasNetwork(this)) toastMessage("No Internet connection, some features may not work!")
    }

    private fun initUI() {
        setBottomNav()
        when(intent.getStringExtra(KEY_NOTIFICATION_STOP_MUSIC_PLAYER)){
            KEY_ALARM -> {
                mediaPlayer.stop()
                vibrator.cancel()
                displayFragment(CarerFragment(),true)
                bottomNav.menu.getItem(2).isChecked = true
            }
            else -> displayFragment(HomeFragment(),false)
        }

    }

    private fun setBottomNav() {
        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.homeNavButton -> {
                    displayFragment(HomeFragment(),true)
                    true
                }
                R.id.patientNavButton -> {
                   displayFragment(PatientFragment(),true)
                    true
                }
                R.id.carerNavButton -> {
                    displayFragment(CarerFragment(),true)
                    true
                }
                R.id.profileNavButton -> {
                    displayFragment(ProfileFragment(),true)
                    true
                }
                else -> false
            }
        }
    }

    private fun displayFragment(fragment: Fragment,addToBackstack:Boolean) {
        showFragment(R.id.fragmentContainer,fragment,addToBackstack)
    }

    private fun selectHomeFragmentOnNavigationBar() {
        bottomNav.menu.getItem(0).isChecked = true
    }
}