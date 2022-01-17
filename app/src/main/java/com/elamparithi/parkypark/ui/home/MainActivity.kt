package com.elamparithi.parkypark.ui.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.database.entity.Booking
import com.elamparithi.parkypark.databinding.ActivityMainBinding
import com.elamparithi.parkypark.ui.allottedParkingDetails.AllottedParkingDetailFragment
import com.elamparithi.parkypark.ui.base.BaseActivity
import com.elamparithi.parkypark.ui.findParking.FindParkingDialogFragment
import com.elamparithi.parkypark.ui.getParkingDetails.GetParkingDetailsFragment
import com.elamparithi.parkypark.utils.AuthenticationState
import com.elamparithi.parkypark.utils.Constants
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val LOCATION_REQUEST_CODE: Int = 101

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>(), HomeCallback {

    private var menu: Menu? = null
    private val loginActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val data = result.data
        val response = IdpResponse.fromResultIntent(data)
        if (result.resultCode == Activity.RESULT_OK) {
            //User signed in successfully
            viewModel.insertUserDetails()
            Timber.i(
                "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
            )
        } else {
            //sign in failed
            Timber.i("Sign in unsuccessful ${response?.error?.errorCode}")
        }
    }

    private lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>

    override fun getViewModelClass() = MainActivityViewModel::class.java

    override fun setupView() {

        val mBottomSheetLayout = findViewById<FrameLayout>(R.id.bottomSheetLayout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);

        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        sheetBehavior.isDraggable = false
        sheetBehavior.isHideable = true

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvMainScreen) as NavHostFragment?
        navHostFragment?.let {
            setSupportActionBar(binding.mtbHome)
            val navController = navHostFragment.navController
            NavigationUI.setupWithNavController(binding.nvNavigationDrawerView, navController)
            NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.fcvMainScreen)
        return (NavigationUI.navigateUp(navController, binding.drawerLayout)
                || super.onSupportNavigateUp())
    }


    override fun observeData() {
        viewModel.authenticationState.observe(this, { authenticationState ->
            changeLoginButtonText(authenticationState)
        })
        viewModel.getCurrentlyActiveOrInProgressBookingDetails()
            .observe(this) { bookingStatusDetails ->

                bookingStatusDetails?.let {
                    if (bookingStatusDetails.bookingId != -1L) {
                        val allottedParkingDetailsFragment =
                            AllottedParkingDetailFragment.newInstance()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fcvContent, allottedParkingDetailsFragment)
                            .commit()
                        if (bookingStatusDetails.status == Constants.BOOKING_STATUS_CONFIRMED) {
                            sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                        } else {
                            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                } ?: run {
                    val parkingSelectionFragment = GetParkingDetailsFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fcvContent, parkingSelectionFragment)
                        .commit()
                }
            }
        super.observeData()
    }

    override fun handleParkingDetailsBottomSheet(bottomSheetState: Int) {
        sheetBehavior.state = bottomSheetState
    }

    override fun callUserLogin() {
        launchLogInFlow()
    }

    override fun getParkingDetailsBottomSheetCurrentState(): Int {
        return sheetBehavior.state
    }

    private fun launchFindParking() {
        viewModel.bookingDetails?.let { booking ->
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            FindParkingDialogFragment.newInstance(
                getString(R.string.finding_the_parking_space),
                false,
                booking
            )
                .show(supportFragmentManager, "loading_dialog")
        }

    }

    override fun checkLocationAccess(booking: Booking) {
        viewModel.bookingDetails = booking
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // launch find parking
                launchFindParking()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                //show why we need this permission.
                showReasonDialog()
            }
            else -> {
                //ask user location
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun getParkingDetailsBackPressed() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            ActivityCompat.finishAffinity(this)
        }
    }

    /**
     * this dialog will explain why we need the location access
     */
    private fun showReasonDialog() {

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.location_permission))
            .setMessage(resources.getString(R.string.location_permission_explanation))
            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->

            }
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
            .show()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // user granted location permission launch find parking
                    launchFindParking()
                } else {
                    Toast.makeText(
                        this,
                        "Can't find parking lot without the location.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun getMenuLayoutId() = R.menu.main_menu


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.login_or_logout) {
            return when (item.title) {
                getString(R.string.login) -> {
                    //login
                    launchLogInFlow()
                    true
                }
                getString(R.string.logout) -> {
                    launchLogOutFlow()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(item)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchLogOutFlow() {
        AuthUI.getInstance().signOut(this)
    }

    private fun launchLogInFlow() {
        //List of log in providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        //get login activity
        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers)
            .build()
        loginActivityResult.launch(loginIntent)
    }

    override fun setupMenu(menu: Menu) {
        this.menu = menu
        val loginState = viewModel.authenticationState.value
        loginState?.let { changeLoginButtonText(it) }
    }

    private fun changeLoginButtonText(authenticationState: AuthenticationState) {
        when (authenticationState) {
            AuthenticationState.AUTHENTICATED -> {
                menu?.findItem(R.id.login_or_logout)?.title = getString(R.string.logout)
            }
            else -> {
                viewModel.clearUserDetails()
                menu?.findItem(R.id.login_or_logout)?.title = getString(R.string.login)
            }
        }
    }

    override fun inflateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

}