package com.kandaovr.meeting.kotlinDemo.test.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.cjx.kotlin.base.BaseActivity
import com.kandaovr.meeting.kotlinDemo.R
import com.kandaovr.meeting.kotlinDemo.databinding.ActivityMvvmBinding

class MvvmActivity : BaseActivity<LoginViewModel, ActivityMvvmBinding>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
    }

    override fun initParam() {
        viewModel.login("caicai", "123456")
    }

    override fun initViewObservable() {
        viewModel.loginLiveData.observe(this, Observer {
            binding.tvText.text = "hahah:${it.dataState.toString()}"
        })

        viewModel.loadingDataState.observe(this, Observer {


        })
    }
}