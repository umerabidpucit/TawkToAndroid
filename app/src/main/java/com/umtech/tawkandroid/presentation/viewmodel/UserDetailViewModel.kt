package com.umtech.tawkandroid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umtech.tawkandroid.data.model.UserDetails
import com.umtech.tawkandroid.data.model.toDetailEntity
import com.umtech.tawkandroid.data.model.toUserDetails
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao
import com.umtech.tawkandroid.domain.usecase.FetchUserDetailUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val fetchUserDetailUseCase: FetchUserDetailUseCase,
    private val userDetailsDao: UserDetailDao
) : ViewModel() {

    private val _userDetail = MutableStateFlow<UserDetails?>(null)
    val userDetail: StateFlow<UserDetails?> = _userDetail

    private val _notesUpdated = MutableSharedFlow<String>() // ✅ Notify when notes update
    val notesUpdated: SharedFlow<String> = _notesUpdated

    fun fetchUserDetails(username: String) {
        viewModelScope.launch {
            val userFromDb = userDetailsDao.getUserDetail(username)

            if (userFromDb != null) {
                _userDetail.value = userFromDb.toUserDetails()
            } else {
                fetchUserDetailUseCase(username).collect { userDetails ->
                    _userDetail.value = userDetails
                    userDetailsDao.insertUserDetail(userDetails.toDetailEntity())
                }
            }
        }
    }

    fun updateUserNotes(username: String, notes: String) {
        viewModelScope.launch {
            userDetailsDao.updateNotes(username, notes)
            _notesUpdated.emit(username) // 🔥 Notify UserDetailsScreen
        }
    }
}