package com.example.underpressurea

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.underpressurea.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class MainActivity : AppCompatActivity() {

    private val MY_REQUEST_CODE: Int = 7117 //Any number
    lateinit var providers: List<AuthUI.IdpConfig>
    private lateinit var auth: FirebaseAuth
    //val STATE_USER = "user"
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var authStateListener: AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_main)

        @Suppress("UNUSED_VARIABLE")
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout


        //Init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),       //Google login
            AuthUI.IdpConfig.FacebookBuilder().build()      //Facebook login
        )

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        Log.d("OnCreate", "1111")
        var user = FirebaseAuth.getInstance().currentUser

        /**
        if(user == null){

        showSignInOptions()
        Log.d("OnCreate", "bbbbbbbbbb")
        }
         */
        val navController = this.findNavController(R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        Log.d("OnCreate", "aaaaaaaaaaa")


        if (user == null ) {
            showSignInOptions()
        }

        /*
        else {
            loop@ for (userInfo in user?.getProviderData()) {
                when (userInfo.providerId) {
                    EmailAuthProvider.PROVIDER_ID -> {
                        if (!user.isEmailVerified()) {
                            showSignInOptions()
                        }
                    }

                    GoogleAuthProvider.PROVIDER_ID -> {
                    }
                    FacebookAuthProvider.PROVIDER_ID -> {
                    }
                }
            }
        }
        */

    }

    /**public override fun onStart() {
    super.onStart()
    // Check if user is signed in (non-null) and update UI accordingly.
    val currentUser = auth.currentUser
    updateUI(currentUser)
    }
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = auth.currentUser //get current user
                Log.d("User registration", EmailAuthProvider.PROVIDER_ID)
                Log.d("User registration", GoogleAuthProvider.PROVIDER_ID)
                Log.d("User registration", FacebookAuthProvider.PROVIDER_ID)
                Log.d("User registration", user?.getProviderData().toString() )

                if (null != user) {
                    //if ("password".equals(user.getProviderData().get(0).getProviderId())) {


                    loop@ for (userInfo in user?.getProviderData()) {
                        when (userInfo.providerId) {
                            EmailAuthProvider.PROVIDER_ID -> {
                                //Email sign in
                                Log.d("User registration", "email sign up")
                                if (!user.isEmailVerified()) {
                                    /* Send Verification Email */
                                    user.sendEmailVerification()
                                        .addOnCompleteListener(this) { task ->
                                            /* Check Success */
                                            if (task.isSuccessful()) {
                                                //showSignInOptions()
                                                Toast.makeText(
                                                    getApplicationContext(),
                                                    "Verification Email Sent To: " + user.getEmail(),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                logout()
                                            } else {
                                                Log.e(
                                                    "User registration",
                                                    "sendEmailVerification",
                                                    task.getException()
                                                )
                                                Toast.makeText(
                                                    getApplicationContext(),
                                                    "Email not verified yet",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                logout()
                                            }

                                        }
                                    /* Handle Case When Email Not Verified */
                                }
                                else{
                                    Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish() // Call once you redirect to another activity
                                }
                            }
                            GoogleAuthProvider.PROVIDER_ID -> {
                                Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Call once you redirect to another activity
                            }
                            FacebookAuthProvider.PROVIDER_ID -> {
                                Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Call once you redirect to another activity
                            }
                            else -> continue@loop
                        }
                    }

                }
                /*
                Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()
                Log.d("onActivityResult", "2222")
                //val logoutButton : Button = findViewById(R.id.menu_logout_button)
                //logoutButton.isEnabled = true
                //logoutButton.isVisible = true
                Log.d("onActivityResult", "3333")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Call once you redirect to another activity
                */

            } else {
                //Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .setTheme(R.style.MyAuthTheme)
                .setLogo(R.drawable.ic_blood_pressure)
                .build(), MY_REQUEST_CODE
        )
        Log.d("OnCreate", "ccccccccccccc")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("onActivityResult", "7777")
        //super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        /**val menuItem = menu!!.findItem(R.id.menu_logout_button)
        val view = menuItem.actionView
        Log.d("onActivityResult", "8888")
        /view.setOnClickListener {
        onOptionsItemSelected(menuItem)
        }*/
        Log.d("onActivityResult", "9999")
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        // Handle item selection
        Log.d("onActivityResult", "4444")
        return when (item?.itemId) {
            R.id.menu_logout_button -> {
                Log.d("onActivityResult", "5555")
                logout()
                true
            }

            else -> {
                Log.d("onActivityResult", "6666")
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun logout() {
        /**
         * To access this from an outer scope (a class, or extension function, or labeled function literal with receiver)
         * we write this@label where @label is a label on the scope this is meant to be from:
         */
        AuthUI.getInstance().signOut(this@MainActivity)
            .addOnCompleteListener {
                //val logoutButton : Button = findViewById(R.id.menu_logout_button)
                //logoutButton.isEnabled = false
                //showSignInOptions()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    /**override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(STATE_USER, auth.currentUser)
    }*/


}

