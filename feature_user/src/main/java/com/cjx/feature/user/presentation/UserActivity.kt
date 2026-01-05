package com.cjx.feature.user.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val userId = intent.getLongExtra(EXTRA_USER_ID, -1L)
        
        setContent {
            MaterialTheme {
                UserRoute(userId = userId)
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