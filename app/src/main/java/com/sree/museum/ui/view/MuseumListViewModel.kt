package com.sree.museum.ui.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sree.museum.domain.MuseumRepo
import com.sree.museum.domain.model.MuseumItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MuseumListViewModel @Inject constructor(private val museumRepo: MuseumRepo) : ViewModel() {
    sealed class Event {
        data class LoadMuseumItems(val query: String = "temples") : Event()
        object SearchClicked : Event()
    }
    val events = Channel<Event>(Channel.UNLIMITED)
    sealed class State {
        class Idle(val museumItems: List<MuseumItem> = emptyList()) : State()
        object Loading : State()
        data class Success(val museumItems: List<MuseumItem>) : State()
        data class Error(val message: String) : State()
    }
    var state = mutableStateOf<State>(State.Idle())
        private set

    init {
        setup()
    }
    private fun setup() {
        events.receiveAsFlow().onEach { event ->
            handleEvent(event)
        }.launchIn(viewModelScope)
    }

    private val _query = MutableStateFlow("temples")
    val query: StateFlow<String> = _query.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val museumItemsFlow = _query
        .debounce( timeoutMillis = 300)
        .map { it.trim() }
        .distinctUntilChanged().flatMapLatest { museumRepo.getMuseumItemsFlow(query = it) }
        .cachedIn(viewModelScope)

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.LoadMuseumItems -> { _query.value = event.query }
            Event.SearchClicked -> {
                if (state.value is State.Success) {
                    state.value = State.Idle((state.value as State.Success).museumItems)
                }
            }
        }
    }
    fun loadMuseumItems(query: String) {
        state.value = State.Loading
        viewModelScope.launch {
            try {
                val museumItems = museumRepo.fetchMuseumItems(query = query, page = 1)
                state.value = State.Success(museumItems)
            } catch (e: Exception) {
                state.value = State.Error(e.message ?: "Unknown error")
            }
        }
    }
}