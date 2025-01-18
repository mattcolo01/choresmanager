package com.colombo.choresmanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChoresOverviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChoresOverviewViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}