
package com.sphtech.shared.util

object FormatUtility {
    val SEPARATE = "-,"
    val DEFAULT_TEXT_FORMAT = "###-####-####"
    val CREDIT_CARD_TEXT_FORMAT = "####-####-####"
    val AMOUNT_TEXT_FORMAT = "###,###.00"

    fun applyStringPattern(text: String, format: String): String {
        val pattern = StringBuilder() // ex. pattern "(\\d{3})(\\d{3})(\\d+)"
        val replacement = StringBuilder() // ex. replacement "$1-$2-$3"
        val formats = format.split(SEPARATE.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in formats.indices) {
            pattern.append("(\\d{").append(formats[i].length).append("})")
            if (i == 0) {
                replacement.append("$").append(i + 1)
            } else {
                replacement.append("$SEPARATE$").append(i + 1)
            }
        }
        return text.replaceFirst(pattern.toString().toRegex(), replacement.toString())
    }
}