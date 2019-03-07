package com.tinklabs.iot.devicescanner.business

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.honeywell.barcode.HSMDecoder
import com.honeywell.license.ActivationManager
import com.tinklabs.iot.devicescanner.BuildConfig
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.app.base.BaseActivity
import com.tinklabs.iot.devicescanner.ext.toast
import com.tinklabs.iot.devicescanner.widget.ConfirmDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val mainViewModel by viewModel<MainViewModel>()
    private var granted: Boolean = false

    companion object {
        const val REQUEST_CODE = 37
        const val MY_PERMISSIONS_REQUEST_CAMERA = 0x2001
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivationManager.activate(this, BuildConfig.SWIFT_DECODER_LICENSE)

        navController = Navigation.findNavController(this, R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            initIfPermissionGranted()
        }
    }

    private fun initIfPermissionGranted() {
        granted = true
    }

    private fun exitIfPermissionDenied() {
        granted = false
        this.finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initIfPermissionGranted()
                } else {
                    exitIfPermissionDenied()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (granted) {
            HSMDecoder.disposeInstance()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE == requestCode) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) handleSignInResult(result.signInAccount!!)
            else {
                /**
                 * sign in progress will call here. and result is null,
                 * so return then sign in success will call {@link #onActivityResult()} again.
                 */
                if (result.status.statusCode == GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS) return
                toast("sign in failed")
                this.finish()
            }
        }
    }

    private fun handleSignInResult(account: GoogleSignInAccount) {
        // Signed in successfully, show authenticated UI.
        mainViewModel.setSignInStatus(true)
        checkPermission()
        Timber.d(account.email)
    }

    override fun onStart() {
        super.onStart()
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
        Timber.d(account?.email)
        // if account not null, skip sign in check
        if (null != account && account.email?.isNotEmpty() == true) {
            checkPermission()
            mainViewModel.setSignInStatus(true)
        } else {
            mainViewModel.setSignInStatus(false)
            doSignIn()
        }
    }

    private val googleSignInOptions: GoogleSignInOptions by inject()

    private fun doSignIn() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (status != ConnectionResult.SUCCESS) {
            ConfirmDialog(this)
                .cancelable(false)
                .title(R.string.tips)
                .content("Need to update Google Service to use Devices Scanner!!!")
                .confirmText(R.string.confirm)
                .cancelText(R.string.exit)
                .onConfirm(View.OnClickListener {
                    finish()
                })
                .onCancel(View.OnClickListener {
                    finish()
                })
                .show()
        } else {
            val mGoogleSignInClient = GoogleSignIn.getClient(this@MainActivity, googleSignInOptions)
            startActivityForResult(mGoogleSignInClient.signInIntent, REQUEST_CODE)
        }
    }
}
