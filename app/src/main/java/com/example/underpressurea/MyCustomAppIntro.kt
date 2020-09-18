package com.example.underpressurea

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.underpressurea.R
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.AppIntroFragment.*

class MyCustomAppIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(AppIntroFragment.newInstance("View pressure history",
            "Pressure history is visible from the home",
            R.drawable.screenshot_1,
            Color.BLUE
        ))
        addSlide(AppIntroFragment.newInstance("Save pressure",
            "Tap on ADD VALUE button",
            R.drawable.screenshot_2,
            Color.BLUE
        ))
        addSlide(AppIntroFragment.newInstance(
            "Insert pressure values",
            "Enter yor blood pressure values in the fields provided",
            R.drawable.screenshot_3,
            Color.BLUE
        ))
        addSlide(AppIntroFragment.newInstance(
            "Insert pressure values",
            "Tap on SAVE button to save pressure values and receive feedback",
            R.drawable.screenshot_4,
            Color.BLUE
        ))
        addSlide(AppIntroFragment.newInstance(
            "Positive feedback",
            "If the saved values are good you will see this screen",
            R.drawable.screenshot_5,
            Color.BLUE
        ))
        addSlide(AppIntroFragment.newInstance(
            "Negative feedback",
            "Instead, if the saved values are too high you will see this screen with a map reporting the pharmacies closest to you",
            R.drawable.screenshot_6,
            Color.BLUE
        ))
        addSlide(AppIntroFragment.newInstance(
            "Delete values",
            "Tap on trash icon of an item to delete it",
            R.drawable.screenshot_7,
            Color.BLUE
        ))
        addSlide(AppIntroFragment.newInstance(
            "Delete pressure history",
            "Tap on DELETE ALL button to cancel all values saved so far",
            R.drawable.screenshot_8,
            Color.BLUE
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}