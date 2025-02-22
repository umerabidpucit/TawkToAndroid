package com.umtech.tawkandroid.domain.usecase

import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class SearchUseCase(private val userRepository: UserRepository) {
    operator fun invoke(query: String): Flow<List<UserEntity>> {
        return userRepository.searchUsers(query)
    }
}