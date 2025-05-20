package com.iraklyoda.stockapp.presentation.screen.company_listings.event

sealed interface CompanyListingsEvent {
    data object Refresh: CompanyListingsEvent
    data class OnSearchQueryChange(val query: String): CompanyListingsEvent
}