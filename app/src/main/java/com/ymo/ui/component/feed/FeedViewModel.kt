package com.ymo.ui.component.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymo.data.DataRepositoryHelper
import com.ymo.data.Resource
import com.ymo.data.mapper.toModelList
import com.ymo.data.model.api.Feed
import com.ymo.di.NetworkModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val network: NetworkModule.Network,
    private val dataRepositoryHelper: DataRepositoryHelper
) : ViewModel() {
    private val _feedsLiveData = MutableLiveData<Resource<List<Feed>>>()
    val feedsLiveData: LiveData<Resource<List<Feed>>> get() = _feedsLiveData

    private var currentPage = 1
    private var isLoading = false
    private var hasMoreData = true

    init {
        loadFeeds()
    }

    private fun loadFeeds() {
        if (isLoading) return
        isLoading = true
        _feedsLiveData.postValue(Resource.loading(null))

        viewModelScope.launch {
            try {
                val feeds =
                    if (network.isConnected && !dataRepositoryHelper.isTryingToGetExistingData(currentPage)) {
                        dataRepositoryHelper.loadFeedsFromAPI(currentPage)
                    } else {
                        dataRepositoryHelper.getFeedsFromDB().toModelList()
                    }

                if (feeds.isNullOrEmpty()) {
                    hasMoreData = false
                }

                feeds?.let {
                    _feedsLiveData.postValue(Resource.success(it))
                    currentPage++
                }
            } catch (e: Exception) {
                _feedsLiveData.postValue(
                    Resource.error(
                        e.localizedMessage ?: e.message ?: "Unknown Error", null
                    )
                )
            } finally {
                isLoading = false
            }
        }
    }

    fun loadMoreFeeds() {
        if (hasMoreData) {
            loadFeeds()
        }
    }

    fun getCurrentPage() = currentPage
}