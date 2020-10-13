package hr.ferit.sumigaborna.dementiahelper.app.common

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.format.DateTimeFormat
import java.text.DateFormatSymbols
import java.util.*


fun FragmentActivity.showFragment(containerId: Int, fragment:Fragment, addToBackStack : Boolean,bundle:Bundle = Bundle.EMPTY){
    this.supportFragmentManager.beginTransaction().apply {
        if(addToBackStack) addToBackStack(fragment.tag)
    }.replace(containerId,fragment).commitAllowingStateLoss()
    fragment.arguments = bundle
}

fun Fragment.showFragment(containerId: Int, fragment:Fragment, addToBackStack : Boolean,bundle:Bundle = Bundle.EMPTY){
    this.childFragmentManager.beginTransaction().apply {
        if(addToBackStack) addToBackStack(fragment.tag)
    }.replace(containerId,fragment).commitAllowingStateLoss()
    fragment.arguments = bundle
}

fun localizeDate(serverDate : String):String{
    val dtf = DateTimeFormat.shortDate().withLocale(Locale.getDefault())
    return dtf.print(org.joda.time.LocalDate(serverDate.substring(0,10))).replace(" ","")
}

fun checkRegisterInput(context: Context, emailInput:String, passwordInput:String, confirmPasswordInput:String, carerPassword:String, carerConfirmPassword:String):Boolean{
    return when{
        emailInput.isNotBlank() && passwordInput.isNotBlank() && confirmPasswordInput.isNotBlank() && carerPassword.isNotBlank() && carerConfirmPassword.isNotBlank() && passwordInput==confirmPasswordInput && carerPassword==carerConfirmPassword -> true
        emailInput.isBlank() || passwordInput.isBlank() || confirmPasswordInput.isBlank() || carerPassword.isBlank() || carerConfirmPassword.isBlank() -> {
            Toast.makeText(context, context.getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
            false
        }
        passwordInput!=confirmPasswordInput && carerPassword!=carerConfirmPassword -> {
            Toast.makeText(context, context.getString(R.string.passwords_dont_match), Toast.LENGTH_SHORT).show()
            false
        }
        else -> {
            Toast.makeText(context, context.getString(R.string.there_was_error), Toast.LENGTH_SHORT).show()
            false
        }
    }
}

fun checkLoginInput(context: Context,emailInput: String,passwordInput: String):Boolean{
    return when{
        emailInput.isNotBlank() && passwordInput.isNotBlank() -> true
        emailInput.isBlank() || passwordInput.isBlank() -> {
            Toast.makeText(context, context.getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
            false
        }
        else -> {
            Toast.makeText(context, context.getString(R.string.there_was_error), Toast.LENGTH_SHORT).show()
            false
        }
    }
}

fun Activity.toastMessage(message:String) = Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
fun Activity.toastMessageLong(message:String) = Toast.makeText(this,message,Toast.LENGTH_LONG).show()
fun Fragment.toastMessageLong(message:String) = Toast.makeText(this.context,message,Toast.LENGTH_LONG).show()
fun Fragment.toastMessage(message:String) = Toast.makeText(this.context,message,Toast.LENGTH_SHORT).show()

fun hasNetwork(context: Context):Boolean{
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

fun hideProgressBar(progressBar : ProgressBar){
    progressBar.visibility = View.GONE
}
fun showProgressBar(progressBar: ProgressBar){
    progressBar.visibility = View.VISIBLE
}

fun getTodaysName(): String {
    val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    return DateFormatSymbols().weekdays[dayOfWeek]
}
// Storage Permissions
private val REQUEST_EXTERNAL_STORAGE = 1
private val PERMISSIONS_STORAGE = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

fun verifyStoragePermissions(activity: Activity?) {
    // Check if we have write permission
    val permission = ActivityCompat.checkSelfPermission(
        activity!!,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    if (permission != PackageManager.PERMISSION_GRANTED) {
        // We don't have permission so prompt the user
        ActivityCompat.requestPermissions(
            activity,
            PERMISSIONS_STORAGE,
            REQUEST_EXTERNAL_STORAGE
        )
    }
}