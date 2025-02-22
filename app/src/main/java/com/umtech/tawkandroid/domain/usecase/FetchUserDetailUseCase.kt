package com.umtech.tawkandroid.domain.usecase

import com.umtech.tawkandroid.data.model.UserDetails
import com.umtech.tawkandroid.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class FetchUserDetailUseCase(private val repository: UserRepository) {
    operator fun invoke(username: String): Flow<UserDetails> {
        return flow {
            emitAll(repository.getUserDetailData(username)) // ✅ Flattens the Flow
        }
    }
}