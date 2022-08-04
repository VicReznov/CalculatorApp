package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var tvInput: TextView? = null
    var lastNumeric: Boolean = false // 마지막이 숫자인지
    var lastDot: Boolean = false // 마지막에 점이 있는지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.main_textview_input)
    }

    fun onDigit(view:View){
        tvInput?.append((view as Button).text)
        lastNumeric = true
        lastDot = false


    }

    // 화면을 초기화 하는 메소드
    fun onClear(view: View){
        tvInput?.text = ""
    }
    // 점 찍는 메소드
    fun onDecimalPoint(view: View){
        // 마지막이 숫자이고 마지막에 점이 없을 때
        if(lastNumeric && !lastDot){
            tvInput?.append(".")
            lastNumeric = false
            lastDot = true
        }
    }
    // 연산자를 넣는 메소드(나누기, 곱하기, 더하기, 빼기)
    fun onOperator(view: View){
        // 화면에 값이 있을 때만 수행
        tvInput?.text?.let {
            // 마지막이 숫자이고 연산자가 아직 없다면 연산자 추가가
           if(lastNumeric && !isOperatorAdded(it.toString())){
                tvInput?.append((view as Button).text)
                lastNumeric = false
                lastDot = false
            }
        }
    }

    private fun isOperatorAdded(value: String): Boolean{
        // 연산자가 이미 있다면 true, 연산자가 아직 없다면 false 반환
        return if(value.startsWith("-")){
            false
        } else {
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }
    }

    fun onEqual(view: View){
        if(lastNumeric){
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            // 산술 상의 에러는 예외로 만들어 줌 -> 0으로 나눈다거나 하는 상황
            try{
                // 값 앞에 "-"가 붙어있다면 그 "-" 기호를 날려버린다
                if(tvValue.startsWith("-")){
                    prefix = "-"
                    tvValue = tvValue.substring(1) // 맨 앞의 "-" 기호를 날려버린다
                }

                if(tvValue.contains("-")){ // 값에 "-"가 포함되어 있다면 빼는 연산을 한다
                    val splitValue  = tvValue.split("-")

                    var one = splitValue[0]
                    var two = splitValue[1]
                    // 만약 prefix가 비어있지 않다면? -> "-"가 들어 있다면?
                    // "-"를 one 앞에 붙여준다
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    var result = one.toDouble() - two.toDouble()
                    tvInput?.text = removeZeroAfterDot(result.toString())
                } else if(tvValue.contains("+")){
                    val splitValue  = tvValue.split("+")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    var result = one.toDouble() + two.toDouble()
                    tvInput?.text = removeZeroAfterDot(result.toString())
                } else if(tvValue.contains("X")){
                    val splitValue  = tvValue.split("X")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    var result = one.toDouble() * two.toDouble()
                    tvInput?.text = removeZeroAfterDot(result.toString())
                } else if(tvValue.contains("/")){
                    val splitValue  = tvValue.split("/")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    if(two.toDouble() != 0.0){
                        var result = one.toDouble() / two.toDouble()
                        tvInput?.text = removeZeroAfterDot(result.toString())
                    }

                }

            } catch (e: ArithmeticException){
                e.printStackTrace()
            }
        }
    }
    // 소수점 부분이 ".0" 이라면 삭제한다 -> 정수형처럼 보임
    private fun removeZeroAfterDot(result: String): String{
        var value = result
        if(result.contains(".0")){
            value = result.substring(0, result.length - 2)
        }

        return value
    }

}