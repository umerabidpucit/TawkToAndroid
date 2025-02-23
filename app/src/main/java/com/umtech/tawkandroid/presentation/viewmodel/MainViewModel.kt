package com.umtech.tawkandroid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.umtech.tawkandroid.common.NetworkMonitor
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.domain.usecase.FetchUserUseCase
import com.umtech.tawkandroid.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class MainViewModel(
    fetchUserUseCase: FetchUserUseCase,
    private val searchUsersUseCase: SearchUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val users: Flow<PagingData<User>> = fetchUserUseCase(0).cachedIn(viewModelScope)

    val searchResults = MutableStateFlow<List<UserEntity>>(emptyList())

    private val _notesUpdated = MutableSharedFlow<String>(replay = 1) // Replay last emitted value
    val notesUpdated: SharedFlow<String> = _notesUpdated

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun notifyNoteUpdated(username: String) {
        viewModelScope.launch {
            println("🔥 Emitting note update for $username") // Debugging log
            _notesUpdated.emit(username)
        }
    }

    init {
        observeNetwork()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.observeNetworkState().collectLatest { isConnected ->
                _isConnected.value = isConnected
            }
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            searchUsersUseCase(query).collect { results ->
                searchResults.value = results
            }
        }
    }
}