package com.cjx.feature.user.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cjx.feature.user.databinding.ActivityUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val userId = intent.getLongExtra(EXTRA_USER_ID, -1L)
        if (userId != -1L) {
            viewModel.loadUser(userId)
        }

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            // repeatOnLifecycle 是 Android 官方推荐的、用于在 UI 层安全地收集 Flow 数据的标准做法。它简化了生命周期管理，使你的代码更简洁、更安全、也更高效
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UserUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.userProfileView.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                        }
                        is UserUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.userProfileView.visibility = View.VISIBLE
                            binding.errorText.visibility = View.GONE
                            binding.usernameText.text = state.user.username
                            binding.emailText.text = state.user.email
                        }
                        is UserUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.userProfileView.visibility = View.GONE
                            binding.errorText.visibility = View.VISIBLE
                            binding.errorText.text = state.message
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_USER_ID = "extra_user_id"

        fun createIntent(context: Context, userId: Long): Intent {
            return Intent(context, UserActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            }
        }
    }
}
