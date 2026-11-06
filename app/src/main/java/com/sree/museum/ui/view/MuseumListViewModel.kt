package com.sree.museum.ui.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sree.museum.domain.MuseumRepo
import com.sree.museum.domain.model.MuseumItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MuseumListViewModel @Inject constructor(private val museumRepo: MuseumRepo) : ViewModel() {
    sealed class Event {
        object LoadMuseumItems : Event()
    }
    val events = Channel<Event>(Channel.UNLIMITED)
    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Success(val museumItems: List<MuseumItem>) : State()
        data class Error(val message: String) : State()
    }
    var state = mutableStateOf<State>(State.Idle)
        private set

    init {
        setup()
    }
    private fun setup() {
        events.receiveAsFlow().onEach { event ->
            handleEvent(event)
        }.launchIn(viewModelScope)
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.LoadMuseumItems -> loadMuseumItems()
        }
    }
    fun loadMuseumItems() {
        state.value = State.Loading
        viewModelScope.launch {
            try {
                val museumItems = museumRepo.fetchMuseumItems(query = "temples", page = 1)
                state.value = State.Success(museumItems)
            } catch (e: Exception) {
                state.value = State.Error(e.message ?: "Unknown error")
            }
        }
    }
}