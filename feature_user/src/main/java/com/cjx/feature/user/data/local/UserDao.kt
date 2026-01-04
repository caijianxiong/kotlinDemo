package com.cjx.feature.user.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cjx.feature.user.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) 接口
 *
 * 定义了访问数据库的方法。Room 会在编译时自动生成此接口的实现类。
 */
@Dao
interface UserDao {
    /**
     * 观察指定 ID 的用户数据
     *
     * 返回 [Flow]，这意味着当数据库表中的数据发生变化时，Flow 会自动发射最新的数据。
     * 这是实现 "Reactive Data Layer" 的关键。
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: Long): Flow<User?>

    /**
     * 插入或更新用户信息
     *
     * [OnConflictStrategy.REPLACE] 策略表示如果 ID 已存在，则覆盖旧数据。
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}