package com.cjx.feature.user.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 用户实体类
 *
 * 此类身兼数职，展示了在小型到中型项目中常见的模式：
 * 1. [Entity] - Room 数据库表定义 ("users" 表)。
 * 2. [Serializable] - Kotlin Serialization 网络传输对象 (DTO)。
 * 3. Domain Model - 业务逻辑层使用的数据结构。
 *
 * 在大型或严格分层的项目中，通常会将其分离为 `UserEntity`, `UserDto` 和 `User`，并使用 Mapper 进行转换。
 */
@Serializable
@Entity(tableName = "users")
data class User(
    /** 用户唯一标识符 */
    @PrimaryKey val id: Long,
    /** 用户名 */
    val username: String,
    /** 电子邮箱地址 */
    val email: String,
    /** 头像 URL 地址，可能为空 */
    val avatarUrl: String? = null
)