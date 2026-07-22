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
import kotlinx.coroutines.flow.collect
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
        binding.tvFlowState.text = "StateFlow：Idle"
        binding.tvCountDown.text = "倒计时：0"
        binding.tvFlowLog.text = "点击下方按钮查看 Flow 教程日志"
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // StateFlow：收集页面状态。它总有当前值，重新进入页面会马上收到最新状态。
                launch {
                    viewModel.loginState.collect { state ->
                        when (state) {
                            is LoginUiState.Idle -> binding.tvFlowState.text = "StateFlow：Idle"
                            is LoginUiState.Loading -> {
                                binding.tvFlowState.text = "StateFlow：Loading"
                                showLoading()
                            }
                            is LoginUiState.Success -> {
                                dismissLoading()
                                binding.loginData = state.result
                                binding.tvFlowState.text = "StateFlow：Success ${state.result.username}"
                            }
                            is LoginUiState.Error -> {
                                dismissLoading()
                                binding.tvFlowState.text = "StateFlow：Error ${state.message}"
                            }
                        }
                    }
                }

                // SharedFlow：收集一次性事件。Toast/导航这类动作不应该被 StateFlow 重放。
                launch {
                    viewModel.loginEvent.collect { event ->
                        when (event) {
                            is LoginUiEvent.Toast -> showToast(event.message)
                            is LoginUiEvent.NavigateHome -> showToast("SharedFlow：导航事件")
                        }
                    }
                }

                // StateFlow：日志列表保存在 ViewModel 中，屏幕旋转后仍能展示最后几条。
                launch {
                    viewModel.flowLogs.collect { logs ->
                        binding.tvFlowLog.text = if (logs.isEmpty()) {
                            "点击下方按钮查看 Flow 教程日志"
                        } else {
                            logs.joinToString(separator = "\n")
                        }
                    }
                }

                launch {
                    viewModel.countDownSecond.collect { second ->
                        binding.tvCountDown.text = "倒计时：$second"
                    }
                }
            }
        }
    }

    fun btnSend(view: View) {
        viewModel.loginTest("caicai", Random.nextInt(123, 456).toString())

        // 模拟网络请求，页面销毁时请求取消
        viewModel.sendNetWorkRequest()
    }

    fun btnFlowLogin(view: View) {
        viewModel.loginByFlow("flow_user", Random.nextInt(1000, 9999).toString())
    }

    fun btnColdFlow(view: View) {
        viewModel.startColdFlowDemo()
    }

    fun btnSharedFlow(view: View) {
        viewModel.sendSharedFlowEvent()
    }

    fun btnCountDown(view: View) {
        viewModel.startCountDown()
    }

    fun btnClearFlow(view: View) {
        viewModel.clearFlowLog()
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
