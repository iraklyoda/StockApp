package com.iraklyoda.stockapp.domain.repository

import com.iraklyoda.stockapp.domain.common.Resource
import com.iraklyoda.stockapp.domain.model.CompanyListing
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}