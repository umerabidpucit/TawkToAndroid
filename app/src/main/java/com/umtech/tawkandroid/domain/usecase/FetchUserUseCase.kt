package com.umtech.tawkandroid.domain.usecase

import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class FetchUserUseCase(private val repository: UserRepository) {
    operator fun invoke(since: Int): Flow<List<User>> {
        return repository.getUserData(since)
    }
}