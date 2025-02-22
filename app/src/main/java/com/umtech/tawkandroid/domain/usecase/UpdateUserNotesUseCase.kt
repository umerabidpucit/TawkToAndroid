package com.umtech.tawkandroid.domain.usecase

import com.umtech.tawkandroid.data.repository.UserRepository

//class UpdateUserNotesUseCase(private val repository: UserRepository) {
//    suspend fun updateUserNotes(userLogin: String) {
//        val user = repository.getUserByLogin(userLogin)
//        user?.let {
//            if (!it.hasNotes) {
//                repository.updateUserNotes(userLogin, true)
//            }
//        }
//    }
//}