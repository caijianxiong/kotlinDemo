package com.kandaovr.meeting.kotlinDemo.mvvm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cjx.kotlin.base.BaseActivity
import com.kandaovr.meeting.kotlinDemo.databinding.ActivityMvvmBinding
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

class MvvmActivity : BaseActivity<LoginViewModel, ActivityMvvmBinding>(ActivityMvvmBinding::inflate) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
    }

    override fun setupListener() {
        binding.btnChangeData.setOnClickListener {
            Log.d(this.javaClass.simpleName, "setupListener: click")
            binding.loginData = binding.loginData?.apply {
                this.password = Random.nextInt(6000, 9000).toString()
            }
            Log.d(this.javaClass.simpleName, "setupListener:${binding.loginData?.password}")
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

        // 收集状态流（StateFlow）：处理 UI 状态更新
        lifecycleScope.launch {
            // 配合 repeatOnLifecycle 实现生命周期感知（仅在前台时收集）
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collectLatest { state ->
                    when (state) {
                        is LoginUiState.Idle -> Unit // 初始状态，不处理
                        is LoginUiState.Loading -> showLoading()
                        is LoginUiState.Success -> Log.d("TAG", "initObservable: ${state.user}")
                        is LoginUiState.Error -> Log.d("TAG", "initObservable: ${state.message}")
                    }
                }
            }
        }

    }

    fun btnSend(view: View) {
        viewModel.loginTest("caicai", Random.nextInt(123, 456).toString())


        // 模拟网络请求，页面销毁请请求取消
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