package com.tihcodes.finanzapp.co.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tihcodes.finanzapp.co.domain.model.CategoryItem
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.presentation.common.BaseViewModel
import com.tihcodes.finanzapp.co.utils.getDonutChartSampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import network.chaintech.cmpcharts.common.model.PlotType
import network.chaintech.cmpcharts.ui.piechart.models.PieChartData
import kotlin.math.absoluteValue


class TransactionChartViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : BaseViewModel() {

    private val _transactionType = MutableStateFlow(TransactionType.EXPENSE)
    val transactionType: StateFlow<TransactionType> = _transactionType.asStateFlow()

    private val _userId = MutableStateFlow("")
    fun setUserId(userId: String) {
        _userId.value = userId
        loadData(userId)
    }

    private val _transactions = MutableStateFlow(emptyList<TransactionItem>())
    private val _categories = MutableStateFlow(emptyList<CategoryItem>())

    fun toggleType(type: TransactionType) {
        _transactionType.value = type
    }

    val pieChartData: StateFlow<PieChartData> = combine(
        _transactions,
        _categories,
        _transactionType
    ) { transactions, categories, selectedType ->
        val filtered = transactions.filter { it.type == selectedType }

        val slices = filtered.groupBy { it.category.trim().lowercase() }
            .mapNotNull { (categoryName, txs) ->
                if (txs.isEmpty()) return@mapNotNull null
                val total = txs.sumOf { it.amount }
                if (total <= 0.0) return@mapNotNull null
                val category = categories.find { it.name.trim().lowercase() == categoryName }

                PieChartData.Slice(
                    value = total.toFloat().absoluteValue
                        .let { if (selectedType == TransactionType.EXPENSE) -it else it },
                    label = category?.name ?: categoryName,
                    color = category?.backgroundColor ?: Color.Gray
                )
            }

        if (slices.isEmpty()) getDonutChartSampleData()
        else PieChartData(slices = slices, plotType = PlotType.Donut)

    }.stateIn(viewModelScope, SharingStarted.Eagerly, getDonutChartSampleData())

    private fun loadData(userId: String) {
        viewModelScope.launch {
            val txs = transactionRepository.getAllTransactions(userId)
            _transactions.value = txs
        }

        viewModelScope.launch {
            categoryRepository.categories.collect {
                _categories.value = it
            }
        }
    }
}
