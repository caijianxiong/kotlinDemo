package com.cjx.feature.user.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cjx.feature.user.data.model.User

/**
 * Room 数据库抽象类
 *
 * 必须添加 [Database] 注解，声明包含的 Entity 列表和版本号。
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    /**
     * 获取 UserDao 的抽象方法，Room 会自动生成实现。
     */
    abstract fun userDao(): UserDao
}