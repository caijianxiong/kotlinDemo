package com.cjx.feature.user.domain.usecase

import com.cjx.feature.user.data.model.User
import com.cjx.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 业务用例 (Use Case) - 获取用户信息
 *
 * UseCase 的作用是封装单一的、可复用的业务逻辑。
 * 它使得 ViewModel 更加轻量，并且让业务逻辑更易于测试。
 *
 * 在简单场景下，它可能只是简单地调用 Repository，但在复杂场景下，它可以编排多个 Repository 或执行数据转换。
 */
class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    /**
     * 执行用例
     *
     * 利用 Kotlin 的 `operator fun invoke`，可以让类实例像函数一样被调用：
     * `val flow = getUserUseCase(userId)`
     */
    operator fun invoke(id: Long): Flow<User?> = repository.getUser(id)
}