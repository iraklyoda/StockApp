package com.iraklyoda.stockapp.presentation.screen.company_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iraklyoda.stockapp.domain.model.CompanyListing
import com.iraklyoda.stockapp.presentation.screen.company_listings.component.CompanyItem
import com.iraklyoda.stockapp.presentation.screen.company_listings.event.CompanyListingsEvent
import com.iraklyoda.stockapp.presentation.ui.theme.StockAppTheme

@Composable
fun CompanyListingsScreen(
    viewModel: CompanyListingsViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CompanyListingsScreenContent(
            state = viewModel.state,
            onEvent = viewModel::onEvent
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyListingsScreenContent(
    state: CompanyListingsState,
    onEvent: (CompanyListingsEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {onEvent(CompanyListingsEvent.OnSearchQueryChange(query = it))},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search...")
            },
            singleLine = true
        )
    }
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {onEvent(CompanyListingsEvent.Refresh)},
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.companies.size) { i ->
                val company: CompanyListing = state.companies[i]
                CompanyItem(
                    company = company,
                    modifier = Modifier.fillMaxWidth().clickable {
                        // Navigate to detail
                    }.padding(16.dp)
                )
                if(i < state.companies.size) {
                    VerticalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun CompanyListingsScreenContentPreview() {
    StockAppTheme {
        CompanyListingsScreenContent(
            state = CompanyListingsState()
        ) {

        }
    }
}