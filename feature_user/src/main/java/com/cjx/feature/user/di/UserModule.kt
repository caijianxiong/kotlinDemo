package com.cjx.feature.user.di

import android.content.Context
import androidx.room.Room
import com.cjx.feature.user.data.local.UserDao
import com.cjx.feature.user.data.local.UserDatabase
import com.cjx.feature.user.data.remote.UserApi
import com.cjx.feature.user.data.repository.UserRepositoryImpl
import com.cjx.feature.user.domain.repository.UserRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt 依赖注入模块 (DI Module)
 *
 * 用于告诉 Hilt 如何创建和提供各种依赖对象。
 * 使用 @InstallIn(SingletonComponent::class) 表示这些依赖的生命周期与 Application 一致（单例）。
 */
@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    /**
     * 提供 Room 数据库实例
     */
    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_db"
        ).build()
    }

    /**
     * 提供 UserDao 实例
     *
     * 依赖于 [UserDatabase]
     */
    @Provides
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.userDao()
    }

    /**
     * 提供 Retrofit API 接口实例
     *
     * 配置了 Kotlin Serialization Converter 用于 JSON 解析。
     */
    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        val contentType = "application/json".toMediaType()
        // 配置 Json 解析器，忽略未知字段以提高兼容性
        val json = Json { ignoreUnknownKeys = true }
        
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // TODO: 替换为实际的 Base URL
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(UserApi::class.java)
    }

    /**
     * 提供 UserRepository 实例
     *
     * 将具体的实现类 [UserRepositoryImpl] 绑定到接口 [UserRepository]。
     * 这样，其他模块只需要注入接口，而不需要关心具体实现，方便测试和替换。
     */
    @Provides
    @Singleton
    fun provideUserRepository(api: UserApi, dao: UserDao): UserRepository {
        return UserRepositoryImpl(api, dao)
    }
}