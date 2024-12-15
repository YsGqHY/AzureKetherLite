package kim.azure.kether.util

import java.util.*
import java.util.regex.Pattern
import kotlin.math.pow

class Calculator {

    private val p = Pattern.compile("(?<!\\d)-?\\d+(\\.\\d+)?|[+\\-*/%^()]")

    @Throws(Exception::class)
    private fun doubleCal(a1: Double, a2: Double, operator: Char): Double {
        return when (operator) {
            '+' -> a1 + a2
            '-' -> a1 - a2
            '*' -> a1 * a2
            '/' -> a1 / a2
            '%' -> a1 % a2
            '^' -> a1.pow(a2)
            else -> throw Exception("illegal operator!")
        }
    }


    @Throws(Exception::class)
    private fun getPriority(s: String?): Int {
        if (s == null) return 0
        return when (s) {
            "(" -> 1
            "+", "-" -> 2
            "%", "*", "/", "^" -> 3
            else -> throw Exception("illegal operator!")
        }

    }

    companion object {

        private val handle by lazy { Calculator() }

        @Throws(Exception::class)
        fun getResult(expr: String): Number {
            val number = Stack<Double>()
            val operator = Stack<String?>()
            operator.push(null)

            val m = handle.p.matcher(expr)
            while (m.find()) {
                val temp = m.group()
                if (temp.matches("[+\\-*/%^()]".toRegex())) {
                    when (temp) {
                        "(" -> {
                            operator.push(temp)
                        }

                        ")" -> {
                            var b: String
                            while (operator.pop().also { b = it!! } != "(") {
                                val a1 = number.pop()
                                val a2 = number.pop()
                                number.push(handle.doubleCal(a2, a1, b[0]))
                            }
                        }

                        else -> {
                            while (handle.getPriority(temp) <= handle.getPriority(operator.peek())) {
                                val a1 = number.pop()
                                val a2 = number.pop()
                                val b = operator.pop()
                                number.push(handle.doubleCal(a2, a1, b!![0]))
                            }
                            operator.push(temp)
                        }
                    }
                } else {
                    number.push(temp.toDouble())
                }
            }

            while (operator.peek() != null) {
                val a1 = number.pop()
                val a2 = number.pop()
                val b = operator.pop()
                number.push(handle.doubleCal(a2, a1, b!![0]))
            }
            return number.pop()
        }
    }

}