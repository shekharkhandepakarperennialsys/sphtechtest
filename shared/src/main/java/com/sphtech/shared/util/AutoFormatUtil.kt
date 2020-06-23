

package com.sphtech.shared.util


import java.text.DecimalFormat

object AutoFormatUtil {

    private const val FORMAT_NO_DECIMAL = "###,###"

    private const val FORMAT_WITH_DECIMAL = "###,###.###"

    fun getCharOccurance(input: String, c: Char): Int {
        var occurance = 0
        val chars = input.toCharArray()
        for (thisChar in chars) {
            if (thisChar == c) {
                occurance++
            }
        }
        return occurance
    }

    fun extractDigits(input: String): String {
        return input.replace("\\D+".toRegex(), "")
    }

    fun formatToStringWithoutDecimal(value: Double): String {
        val formatter = DecimalFormat(FORMAT_NO_DECIMAL)
        return formatter.format(value)
    }

    fun formatToStringWithoutDecimal(value: String): String {
        return formatToStringWithoutDecimal(java.lang.Double.parseDouble(value))
    }

    fun formatWithDecimal(price: String): String {
        return formatWithDecimal(java.lang.Double.parseDouble(price))
    }

    fun formatWithDecimal(price: Double): String {
        val formatter = DecimalFormat(FORMAT_WITH_DECIMAL)
        return formatter.format(price)
    }
}