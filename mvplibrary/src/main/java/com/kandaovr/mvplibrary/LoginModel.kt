package com.kandaovr.mvplibrary

class LoginModel : LoginContract.Model {
    override fun login(
        userName: String,
        password: String,
        callback: LoginContract.LoginCallback
    ) {
        // 模拟网络请求（实际项目用 Retrofit/OkHttp）
        Thread {
            Thread.sleep(1000)
            if (userName == "admin" && password == "123456") {
                callback.onLoginSuccess("user_token_123")
            } else {
                callback.onLoginFailed("用户名或密码错误")
            }
        }.start()
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }
}