package com.sphtech.test.base

import com.sphtech.shared.network.repository.prefs.PreferenceStorage
import com.sphtech.test.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

/**
 * Initialization of libraries.
 */
class BaseApplication : DaggerApplication() {

    @Inject
    lateinit var preferenceStorage: PreferenceStorage

    override fun onCreate() {
        super.onCreate()

    }
    /**
     * Tell Dagger which [AndroidInjector] to use - in our case
     * [com.sphtech.test.di.AppComponent]. `DaggerAppComponent`
     * is a class generated by Dagger based on the `AppComponent` class.
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
