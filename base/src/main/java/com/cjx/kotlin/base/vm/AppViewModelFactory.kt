package com.cjx.kotlin.base.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.InvocationTargetException

class AppViewModelFactory (private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (BaseAndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(Application::class.java).newInstance(application)
            } catch (e: NoSuchMethodException) {
                throw IllegalStateException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw IllegalStateException("Cannot create an instance of $modelClass", e)
            } catch (e: InstantiationException) {
                throw IllegalStateException("Cannot create an instance of $modelClass", e)
            } catch (e: InvocationTargetException) {
                throw IllegalStateException("Cannot create an instance of $modelClass", e)
            }
        } else super.create(modelClass)
    }
}