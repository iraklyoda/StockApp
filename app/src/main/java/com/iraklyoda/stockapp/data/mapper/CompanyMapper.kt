package com.iraklyoda.stockapp.data.mapper

import com.iraklyoda.stockapp.data.local.CompanyListingEntity
import com.iraklyoda.stockapp.domain.model.CompanyListing

fun CompanyListingEntity.toDomain(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

