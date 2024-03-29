package com.example.androidmvvm.app

import AuthRepository
import RequestGenerator
import android.app.Application
import com.example.androidmvvm.BuildConfig

class Application: Application() {

    lateinit var authRepository: AuthRepository

    override fun onCreate() {
        super.onCreate()
        initializeRepos()
    }

    private fun initializeRepos() {
        val service = RequestGenerator.create(applicationContext, BuildConfig.BASE_URL)
        authRepository = AuthRepository(service)
    }
}