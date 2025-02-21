package com.umtech.tawkandroid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.domain.usecase.FetchUserUseCase
import com.umtech.tawkandroid.presentation.state.MainState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class MainViewModel(fetchUserUseCase: FetchUserUseCase) : ViewModel() {

    val users: Flow<PagingData<User>> = fetchUserUseCase(0).cachedIn(viewModelScope)
}