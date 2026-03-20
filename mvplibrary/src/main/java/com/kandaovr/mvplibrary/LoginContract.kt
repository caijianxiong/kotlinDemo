package com.kandaovr.mvplibrary

import com.kandaovr.mvplibrary.base.IBaseModel
import com.kandaovr.mvplibrary.base.IBasePresenter
import com.kandaovr.mvplibrary.base.IBaseView

/**
 * 定义契约接口（Contract）
 * 统一管理 View 和 Presenter 的接口，方便维护：
 */
interface LoginContract {

    // View 层接口：只定义 UI 操作
    interface View : IBaseView{
        fun showLoading()
        fun hideLoading()
        fun showLoginSuccess()
        fun showLoginFailed(msg: String)
    }

    // Presenter 层接口：定义业务逻辑入口
    interface Presenter: IBasePresenter<View> {
        fun login(userName: String, password: String)
    }

    // Model 层接口：定义数据操作
    interface Model : IBaseModel {
        fun login(userName: String, password: String, callback: LoginCallback)
        fun destroy()
    }

    // Model 回调接口：通知 Presenter 结果
    interface LoginCallback {
        fun onLoginSuccess(token: String)
        fun onLoginFailed(msg: String)
    }
}