package com.cjx.feature.user.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * 用户详情路由组件
 *
 * 负责连接 ViewModel 和 UI 组件。
 * 遵循 State Hoisting（状态提升）模式：
 * 1. 拥有并管理 ViewModel。
 * 2. 从 ViewModel 收集状态。
 * 3. 将状态传递给无状态的 [UserScreen]。
 *
 * @param userId 从导航参数中获取的用户 ID
 * @param viewModel 通过 Hilt 注入的 ViewModel
 */
@Composable
fun UserRoute(
    userId: Long,
    viewModel: UserViewModel = hiltViewModel()
) {
    // 副作用：当 userId 变化时重新加载数据
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    // 以生命周期感知的方式收集 StateFlow
    // collectAsStateWithLifecycle 是目前推荐的方式，它会在应用切后台时自动停止收集流，节省资源
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserScreen(uiState = uiState)
}

/**
 * 无状态的用户详情屏幕组件
 *
 * 只负责渲染 UI，不包含任何业务逻辑或 ViewModel 引用。
 * 这种设计使得 UI 组件易于复用和测试（可以使用 Preview 进行预览）。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    uiState: UserUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("User Profile") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is UserUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UserUiState.Success -> {
                    UserProfile(user = uiState.user)
                }
                is UserUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * 用户信息展示组件
 */
@Composable
fun UserProfile(user: com.cjx.feature.user.data.model.User) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 这里可以使用 AsyncImage (如 Coil) 加载 user.avatarUrl
        Text(
            text = user.username,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}