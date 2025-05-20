package com.iraklyoda.stockapp.data.repository

import com.iraklyoda.stockapp.data.csv.CSVParser
import com.iraklyoda.stockapp.data.local.CompanyListingEntity
import com.iraklyoda.stockapp.data.local.StockDatabase
import com.iraklyoda.stockapp.data.mapper.toDomain
import com.iraklyoda.stockapp.data.mapper.toEntity
import com.iraklyoda.stockapp.data.remote.StockApi
import com.iraklyoda.stockapp.domain.common.Resource
import com.iraklyoda.stockapp.domain.model.CompanyListing
import com.iraklyoda.stockapp.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(loading = true))

            val localListings: List<CompanyListingEntity> = dao.searchCompanyListing(query = query)
            emit(Resource.Success(data = localListings.map { it.toDomain() }))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(loading = false))
                return@flow
            }

            val remoteListing: List<CompanyListing>? = try {
                val response = api.getListings()
                companyListingsParser.parse(stream = response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(errorMessage = "Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(errorMessage = "Couldn't load data"))
                null
            }

            remoteListing?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toEntity() }
                )
                emit(Resource.Success(data = dao
                    .searchCompanyListing("")
                    .map { it.toDomain() }))
                emit(Resource.Loading(loading = false))
            }
        }
    }
}