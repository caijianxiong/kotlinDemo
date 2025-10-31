package com.kandaovr.meeting.kotlinDemo.mvvm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.cjx.kotlin.base.BaseActivity
import com.kandaovr.meeting.kotlinDemo.databinding.ActivityMvvmBinding
import com.orhanobut.logger.Logger
import kotlin.random.Random

class MvvmActivity : BaseActivity<LoginViewModel, ActivityMvvmBinding>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
    }

    override fun setupListener() {
        binding.btnChangeData.setOnClickListener {
            Log.d(this.javaClass.simpleName, "setupListener: click")
            binding.loginData = binding.loginData?.apply {
                this.password = Random.nextInt(6000, 9000).toString()
            }
            Log.d(this.javaClass.simpleName, "setupListener:${binding.loginData?.password}")
//            Toast.makeText(this, Thread.currentThread().name, Toast.LENGTH_SHORT)
//                .show()
        }
    }

    override fun initView() {
    }

    override fun initObservable() {
        viewModel.loginLiveData.observe(this) {
            Logger.i("loginData:${it.data}")
            binding.loginData = it.data
        }

        viewModel.loadingDataState.observe(this) {
            Toast.makeText(this, "${it.msg} ${Thread.currentThread().name}", Toast.LENGTH_SHORT)
                .show()
        }

    }

    fun btnSend(view: View) {
        viewModel.loginTest("caicai", Random.nextInt(123, 456).toString())


        viewModel.sendNetWorkRequest()
    }

    fun changeData(view: View) {
        Log.d(this.javaClass.simpleName, "changeData: click")

        Logger.d("changeData:${binding.loginData?.password}")
    }

    fun onFinishClick(view: View) {
        showToast("onFinishClick")
        finish()
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}