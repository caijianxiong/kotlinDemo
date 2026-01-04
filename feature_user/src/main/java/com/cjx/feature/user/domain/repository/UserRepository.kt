package com.cjx.feature.user.domain.repository

import com.cjx.feature.user.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 领域层仓库接口 (Repository Interface)
 *
 * 遵循依赖倒置原则 (DIP)：上层模块（UseCase/ViewModel）不应依赖于下层模块（Data Layer）的具体实现细节。
 * 因此，我们在 Domain 层定义接口，而由 Data 层去实现它。
 */
interface UserRepository {
    /**
     * 获取用户数据流
     *
     * @param id 用户 ID
     * @return 返回 User 的 Flow，支持响应式更新
     */
    fun getUser(id: Long): Flow<User?>

    /**
     * 强制刷新用户数据
     *
     * 从网络拉取最新数据并更新到本地缓存。
     * 这是一个 suspend 函数，因为网络操作是耗时的。
     */
    suspend fun refreshUser(id: Long)
}