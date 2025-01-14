package otus.homework.flowcats

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CatsViewModel(private val catsRepository: CatsRepository) : ViewModel() {

  private val _catsResultStateFlow = MutableStateFlow<Result>(Result.Loading)
  val catsResultStateFlow: StateFlow<Result> = _catsResultStateFlow.asStateFlow()

  init {
    viewModelScope.launch {
      catsRepository.listenForCatFacts().flowOn(Dispatchers.IO).collect { result ->
        _catsResultStateFlow.emit(result)
      }
    }
  }
}

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
  override fun <T : ViewModel> create(modelClass: Class<T>): T = CatsViewModel(catsRepository) as T
}
