package com.cjx.feature.user.data.repository

import android.util.Log
import com.cjx.feature.user.data.local.UserDao
import com.cjx.feature.user.data.model.User
import com.cjx.feature.user.data.remote.UserApi
import com.cjx.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 仓库实现类
 *
 * 实现了 Domain 层的 [UserRepository] 接口。
 * 这是数据层的核心，负责协调不同的数据源（API 和 Database）。
 *
 * 采用了 "Single Source of Truth" (SSOT) 模式：
 * 1. [getUser] 始终返回数据库中的数据 ([UserDao.getUser])。
 * 2. [refreshUser] 从网络获取最新数据并写入数据库 ([UserDao.insertUser])。
 * 3. 数据库的更新会自动触发 Flow 发射新数据，UI 随之更新。
 */
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val dao: UserDao
) : UserRepository {

    /**
     * 直接观察本地数据库。
     * 当 [refreshUser] 更新数据库时，这个 Flow 会自动发射新值。
     */
    override fun getUser(id: Long): Flow<User?> = dao.getUser(id)

    /**
     * 从网络拉取数据并保存到本地。
     */
    override suspend fun refreshUser(id: Long) {
        // 网络请求
//        val user = api.getUser(id)
        // 使用传入的 id，而不是写死 1122
        val user = User(id, "caicai", "1153448695@qq.com", null)
        // 写入本地数据库，触发 Flow 更新
        dao.insertUser(user)
        Log.d("UserRepository", "refreshUser: inserted user with id $id")
    }
}