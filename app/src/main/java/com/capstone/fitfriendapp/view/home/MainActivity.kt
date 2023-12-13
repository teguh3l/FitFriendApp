package com.capstone.fitfriendapp.view.home

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.capstone.fitfriendapp.data.pref.UserModel
import com.capstone.fitfriendapp.databinding.ActivityMainBinding
import com.capstone.fitfriendapp.view.ViewModelFactory
import com.capstone.fitfriendapp.view.getstarted.GetStartedActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeSession()
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { user ->
            if (user != null) {
                showLoading(false)
                displayLoggedIn(user)
            } else {
                showLoading(true)
                navigateToGetStarted()
            }

        }
    }

    private fun navigateToGetStarted() {
        val intent = Intent(this, GetStartedActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayLoggedIn(user: UserModel) {
        binding.cardViewProfile.visibility = View.VISIBLE
        binding.cardViewWorkout.visibility = View.VISIBLE
        binding.tvUsername.text = user.token
        displayCurrentDate()
    }
    private fun displayCurrentDate() {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val currentDate = Date()
        binding.tvCurrentDate.text = sdf.format(currentDate)
    }

    private fun showLoading(isLoading : Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

}