package com.umtech.tawkandroid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umtech.tawkandroid.data.model.UserDetails
import com.umtech.tawkandroid.data.model.toDetailEntity
import com.umtech.tawkandroid.data.model.toUserDetails
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao
import com.umtech.tawkandroid.domain.usecase.FetchUserDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val fetchUserDetailUseCase: FetchUserDetailUseCase,
    private val userDetailsDao: UserDetailDao
) : ViewModel() {

    private val _userDetail = MutableStateFlow<UserDetails?>(null)
    val userDetail: StateFlow<UserDetails?> = _userDetail

    fun fetchUserDetails(username: String) {
        viewModelScope.launch {
            val userFromDb = userDetailsDao.getUserDetail(username) // Fetch from Room

            if (userFromDb != null) {
                _userDetail.value = userFromDb.toUserDetails() // Use Room data, don't call API
            } else {
                fetchUserDetailUseCase(username).collect { userDetails ->
                    _userDetail.value = userDetails
                    // Save the fetched data in Room for future use
                    userDetailsDao.insertUserDetail(userDetails.toDetailEntity())
                }
            }
        }
    }

    fun updateUserNotes(username: String, notes: String) {
        viewModelScope.launch {
            userDetailsDao.updateNotes(username, notes)
        }
    }
}