package com.cjx.kotlin.base.vm

import android.annotation.SuppressLint
import android.app.Application
import com.cjx.kotlin.base.model.BaseRepository

abstract class BaseAndroidViewModel <T : BaseRepository>(@field:SuppressLint("StaticFieldLeak") var application: Application) : BaseViewModel<T>() {
}