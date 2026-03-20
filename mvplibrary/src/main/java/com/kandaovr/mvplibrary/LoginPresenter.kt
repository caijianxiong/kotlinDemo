package com.kandaovr.mvplibrary

import com.kandaovr.mvplibrary.base.BasePresenter
import com.kandaovr.mvplibrary.base.IBaseView
import java.lang.ref.WeakReference

/**
 * 持有model和view
 */
class LoginPresenter(
) : BasePresenter<LoginContract.View, LoginContract.Model>(LoginModel()), LoginContract.Presenter {

    override fun login(userName: String, password: String) {
        view?.showLoading()
        model.login(userName, password, object : LoginContract.LoginCallback {
            override fun onLoginSuccess(token: String) {
                view?.hideLoading()
                view?.showLoginSuccess()
            }

            override fun onLoginFailed(msg: String) {
                view?.hideLoading()
                view?.showLoginFailed(msg)
            }

        })
    }

    override fun attachView(view: LoginContract.View) {
        super.attachView(view)
    }


    // 解绑 View，避免内存泄漏
    override fun detachView() {
        super.detachView()
        model.destroy()
    }
}