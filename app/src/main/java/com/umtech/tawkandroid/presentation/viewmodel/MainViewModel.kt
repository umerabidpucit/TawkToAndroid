package com.umtech.tawkandroid.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.domain.usecase.FetchUserUseCase
import com.umtech.tawkandroid.presentation.state.MainState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class MainViewModel(private val fetchUserUseCase: FetchUserUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private var currentSince = 0
    private var isLoading = false
    private var isLastPage = false

    val users = MutableLiveData<List<UserEntity>>()

    init {
        loadUsersWithPostCount()
    }

    fun loadUsersWithPostCount() {
        if (isLoading || isLastPage) return  // Prevent duplicate calls
        println("currentSince is: $currentSince")
        isLoading = true

        viewModelScope.launch {
            fetchUserUseCase(currentSince)
                .onStart { _state.value = _state.value.copy(isLoading = true) }
                .catch { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                    isLoading = false
                }
                .collect { users ->
                    isLoading = false
                    if (users.isEmpty()) {
                        isLastPage = true // ✅ Stop pagination when no data
                    } else {
                        currentSince += users.size
                        _state.value = _state.value.copy(
                            data = _state.value.data + users,
                            isLoading = false
                        )
                    }
                }
        }
    }
}