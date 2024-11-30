package com.example.lab4_

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lab4_.databinding.ActivityCalcBinding
import kotlinx.coroutines.launch

class CalcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalcBinding
    private val viewModel: CalcViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpButtons()
        observeState()
    }

    private fun setUpButtons() {
        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9,
            binding.btnAdd, binding.btnSubtract, binding.btnMultiply, binding.btnDivide,
            binding.btnDecimal, binding.btnEqual, binding.btnClear
        )

        buttons.forEach { button ->
            button?.setOnClickListener { handleButtonClick(button) }
        }
    }

    private fun handleButtonClick(button: Button?) {
        if (button != null)
        {
            when (button.id) {
                binding.btnEqual?.id -> viewModel.onEvent(CalculatorEvent.Calculate)
                binding.btnClear.id -> viewModel.onEvent(CalculatorEvent.DeleteLast)
                else -> viewModel.onEvent(CalculatorEvent.EnterDigit(button.text.toString()))
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.expressionTextView.text = state.expression
                binding.resultTextView.text = state.result

                val textColor = if (state.isError) getColor(R.color.red) else getColor(R.color.black)
                binding.resultTextView.setTextColor(textColor)
                binding.expressionTextView.setTextColor(textColor)
            }
        }
    }
}