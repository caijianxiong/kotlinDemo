package com.kandaovr.mvplibrary

import android.app.Activity
import android.os.Bundle
import java.lang.ref.WeakReference


class LoginSampleActivity : Activity(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LoginPresenter()
        presenter.attachView(this) // this使用弱应用
    }

    fun loginClicked(userName: String, password: String) {
        presenter.login(userName, password)
    }


    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun showLoginSuccess() {
        TODO("Not yet implemented")
    }

    override fun showLoginFailed(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}