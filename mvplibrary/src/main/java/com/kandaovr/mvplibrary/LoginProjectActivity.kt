package com.kandaovr.mvplibrary

import com.android.internal.infra.PerUser
import com.kandaovr.mvplibrary.base.BaseMvpActivity

class LoginProjectActivity :
    BaseMvpActivity<LoginContract.View, LoginContract.Presenter>(),
    LoginContract.View {

    override fun createPresenter(): LoginContract.Presenter {
        return LoginPresenter()
    }

    override fun getLayoutResId(): Int {
        TODO("Not yet implemented")
    }

    override fun initView() {
        TODO("Not yet implemented")
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

    fun clickLogin(userName: String, password: String) {
        presenter.login(userName, password)

    }
}