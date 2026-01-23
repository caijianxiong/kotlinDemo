package com.cjx.mvi.test

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cjx.mvi.databinding.ActivityMviTestBinding
import kotlinx.coroutines.launch

class MviTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMviTestBinding
    private val viewModel: MviTestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMviTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeUiState()
        observeUiEvent()
    }

    private fun setupClickListeners() {
        binding.incrementButton.setOnClickListener {
            viewModel.dispatch(MviTestAction.IncrementCounter)
        }
        binding.decrementButton.setOnClickListener {
            viewModel.dispatch(MviTestAction.DecrementCounter)
        }
        binding.fetchDataButton.setOnClickListener {
            viewModel.dispatch(MviTestAction.FetchData)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.counterText.text = state.counter.toString()
                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun observeUiEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is MviTestEvent.ShowToast -> {
                            Toast.makeText(this@MviTestActivity, event.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
