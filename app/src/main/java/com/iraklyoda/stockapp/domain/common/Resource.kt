package com.iraklyoda.stockapp.domain.common

sealed class Resource<out D> {
    data class Success<out D>(val data: D) : Resource<D>()
    data class Error(val errorMessage: String) : Resource<Nothing>()
    data class Loading(val loading: Boolean) : Resource<Nothing>()
}
