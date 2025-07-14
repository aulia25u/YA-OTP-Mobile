package com.msfauthentication.di

import com.msfauthentication.data.repository.AuthRepositoryImpl
import com.msfauthentication.data.repository.UserRepositoryImpl
import com.msfauthentication.domain.repository.AuthRepository
import com.msfauthentication.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}