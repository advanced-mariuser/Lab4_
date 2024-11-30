package com.example.lab4_

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mariuszgromada.math.mxparser.Expression

data class CalculatorState(
    val expression: String = "",
    val result: String = "",
    val isError: Boolean = false
)

sealed class CalculatorEvent {
    data class EnterDigit(val digit: String) : CalculatorEvent()
    object Calculate : CalculatorEvent()
    object DeleteLast : CalculatorEvent()
}

class CalcViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.EnterDigit -> {
                _state.value = _state.value.copy(expression = _state.value.expression + event.digit)
            }
            CalculatorEvent.DeleteLast -> {
                if (_state.value.expression.isNotEmpty()) {
                    val newExpression = _state.value.expression.dropLast(1)
                    _state.value = _state.value.copy(expression = newExpression)
                }
            }
            CalculatorEvent.Calculate -> {
                val (result, isError) = evaluateExpression(_state.value.expression)
                _state.value = _state.value.copy(result = result, isError = isError)
            }
        }
    }

    private fun evaluateExpression(expression: String): Pair<String, Boolean> {
        val newExpression = expression.replace("×", "*").replace("÷", "/").replace(",", ".")
        val exp = Expression(newExpression)
        val result = exp.calculate()
        return if (result.isNaN()) {
            "Ошибка" to true
        } else {
            result.toString().replace(".", ",") to false
        }
    }
}
