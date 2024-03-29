package com.example.androidmvvm

import AuthViewModel
import AuthViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidmvvm.app.Application
import com.example.androidmvvm.databinding.ActivityMainBinding
import com.squareup.picasso.BuildConfig

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViewModels() {
        val repository = (application as Application).authRepository
        authViewModel =
            ViewModelProvider(this, AuthViewModelFactory(repository)).get(AuthViewModel::class.java)
    }

//    private fun observerCall() {
//        authViewModel.signupResponse.observe(this, Observer {
//            when (it) {
//                is Resource.Loading -> binding.progressBar.visible(true)
//                is Resource.Success -> {
//                    if (BuildConfig.DEBUG) {
//                        Log.e("Signup ->", "" + it)
//                    }
//                }
//                is Resource.Failure -> {
//                    binding.progressBar.visible(false)
//                    binding.rootLayout.handleApiError(it)
//                }
//            }
//        })
//
//    }

}