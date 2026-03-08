package ru.myitschool.work

import android.app.Application
import android.content.Context
import com.google.crypto.tink.config.TinkConfig

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        context = this

        initTink()
    }

    private fun initTink() {
        TinkConfig.register()
    }

    companion object {
        lateinit var context: Context
    }
}