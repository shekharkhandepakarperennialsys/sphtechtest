
package com.sphtech.shared.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.format.DateUtils
import android.util.Patterns
import android.widget.EditText
import com.sphtech.shared.core.exception.empty
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore
import android.text.*
import android.text.style.ForegroundColorSpan
import java.text.NumberFormat


object StringHelpers {

    @SuppressLint("SimpleDateFormat")
    fun convertStringToDate(date: String): Date {
        return SimpleDateFormat("EEE, dd MMM yyyy").parse(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToSting(date: Date): String {
        return SimpleDateFormat("EEE, dd MMM yyyy").format(date).toString()
    }

    fun getCurrentDateInFormat(): String {
        val formatter = SimpleDateFormat("EEE, dd MMM")
        val c = Calendar.getInstance()
        return formatter.format(c.time)
    }

    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("EEE, dd MMM yyyy")
        val c = Calendar.getInstance()
        return formatter.format(c.time)
    }

    fun convertTravelDate(originalDate: String): String {
        return try {
            var format = SimpleDateFormat("EEE, dd MMM yyyy")
            val newDate = format.parse(originalDate)
            format = SimpleDateFormat("EEE, dd MMM", Locale.ENGLISH)
            format.format(newDate)
        } catch (e: Exception) {
            originalDate
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun getDatePrefixFirst2Digit(): String {
        val formatter = SimpleDateFormat("yyyy")
        val now = formatter.format(Date())
        return now.substring(0, 2)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun getDeviceUniqueID(): String {

        val pseudoID: String

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            pseudoID = "35" + //we make this look like a valid IMEI

                    Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                    Build.SUPPORTED_ABIS.size % 10 + Build.DEVICE.length % 10 +
                    Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                    Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                    Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                    Build.USER.length % 10
        } else {
            pseudoID = "35" + //we make this look like a valid IMEI

                    Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                    Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 +
                    Build.HOST.length % 10 + Build.ID.length % 10 +
                    Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 +
                    Build.PRODUCT.length % 10 + Build.TAGS.length % 10 +
                    Build.TYPE.length % 10 + Build.USER.length % 10
        }

        var serial: String
        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
            return UUID(pseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (e: Exception) {
            serial = "serial"
        }

        return UUID(pseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !target.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun displayMobileNumber(number: String): String {
        if (number.length < 10) {
            return number
        }
        return number.substring(0, 3) + " " + number.substring(3, 6) + " " + number.substring(6, 10)
    }

    fun getFormattedMobileNumber(number: String): String {
        if (number.length < 10) {
            return number
        }
        return number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6, 10)
    }

    fun maskMobileNumber(number: String): String {
        if (number.length < 10) {
            return number
        }
        return number.substring(0, 3) + "-XXX-X" + number.substring(7, 10)
    }

    fun numberInstance(number: String): String {
        try {
            val _number = clearNumberFormat(number)
            if (_number.isEmpty()) {
                return "0.00"
            }
            val nf = NumberFormat.getNumberInstance(Locale.US)
            val formatter = nf as DecimalFormat
            formatter.applyPattern("###,###,##0.00")
            return formatter.format(_number.toBigDecimal()).toString()
        } catch (e: Exception) {
            return number
        }
    }

    fun numberInstanceNoDecimal(number: String): String {
        try {
            val _number = clearNumberFormat(number)
            if (_number.isEmpty()) {
                return "0.00"
            }

            val df = DecimalFormat("###,###,##0")
            return df.format(_number.toBigDecimal()).toString()
        } catch (e: Exception) {
            return number
        }
    }

    fun clearNumberFormat(numberFormat: String): String {
        return numberFormat.replace(",", "")
    }

    fun calculateMaxTopup(balance: String, maxTopup: String): String {
        if (maxTopup == "" || maxTopup == "" || maxTopup.isEmpty()) {
            return "0.00"
        }

        if (balance == "" || balance == "" || balance.isEmpty()) {
            return maxTopup
        }

        val cal = (maxTopup.toBigDecimal() - balance.toBigDecimal()).toString()

        if (cal.toBigDecimal() <= BigDecimal(0.00)) {
            return "0.00"
        }
        return cal
    }

    fun calculateAmountLessThanMaxTopup(_amount: String, _maxTopup: String): Boolean {
        val amount = clearNumberFormat(_amount)
        val maxTopup = clearNumberFormat(_maxTopup)
        if (amount == "" || amount == "" || amount.isEmpty()) {
            return false
        }
        if (maxTopup == "" || maxTopup == "" || maxTopup.isEmpty()) {
            return false
        }
        return amount.toBigDecimal() > maxTopup.toBigDecimal()
    }

    @SuppressLint("SimpleDateFormat")
    fun timeStampToDisplayDateTime(sdkDatetime: String): String {
        var result = ""
        if (sdkDatetime.length > 16) {
            val displayDate = sdkDatetime.subSequence(0, 10).toString()
            val displayTime = sdkDatetime.subSequence(11, 16).toString()
            result += historyTimeToDisplayDate("$displayDate $displayTime")
            result += ", " + historyTimeToDisplayTime(displayTime)
        }
        return result
    }



    @SuppressLint("SimpleDateFormat")
    fun profileDateTimeToDisplayDateTime(sdkDatetime: String): String {
        return try {
            var format = SimpleDateFormat("dd/MM/yyyy")
            val newDate = format.parse(sdkDatetime)
            format = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            format.format(newDate)
        } catch (e: Exception) {
            sdkDatetime
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun easybillTimestampToDisplayDate(sdkDatetime: String): String {
        return try {

            var format = SimpleDateFormat("yyyyMMddHHmmss")
            val newDate = format.parse(sdkDatetime)
            format = SimpleDateFormat("dd MMM yyyy", Locale.UK)
            format.format(newDate)

        } catch (e: Exception) {
            sdkDatetime
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun easybillTimestampToDisplayTime(sdkDatetime: String): String {
        return try {

            val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.UK)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            val value = formatter.parse(sdkDatetime)

            val newFormat = SimpleDateFormat("HH:mm", Locale.UK)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            sdkDatetime
        }
    }

    @SuppressLint("SimpleDateFormat", "BinaryOperationInTimber")
    fun sdkDateToDisplayDateTime(sdkDatetime: String): String {
        return try {

            val formatter = SimpleDateFormat("ddMMyyHHmmss", Locale.UK)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            val value = formatter.parse(sdkDatetime)

            val newFormat = SimpleDateFormat("dd MMM yyyy", Locale.UK)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            Timber.e("Exception " + e.localizedMessage)
            sdkDatetime
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun sdkTimeToDisplayDateTime(sdkDatetime: String): String {
        return try {

            val formatter = SimpleDateFormat("ddMMyyHHmmss", Locale.UK)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            val value = formatter.parse(sdkDatetime)

            val newFormat = SimpleDateFormat("HH:mm", Locale.UK)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            sdkDatetime
        }
    }

    //format "2018-10-30 15:30:46"
    fun getCurrentDateTimeForEasyBill(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date())
    }




    fun getCurrentDateInSpecificFormat(): String {
        val calender = Calendar.getInstance()
        val dayNumberSuffix = getDayNumberSuffix(calender.get(Calendar.DAY_OF_MONTH));
        val dateFormat = SimpleDateFormat(" d'" + dayNumberSuffix + "', MMM", Locale.ENGLISH);
        return dateFormat.format(calender.getTime());
    }

    private fun getDayNumberSuffix(day: Int): String {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        when (day % 10) {
            1 -> return "st"
            2 -> return "nd"
            3 -> return "rd"
            else -> return "th";
        }
    }

    fun getCurrentYear(): String {
        return SimpleDateFormat("yyyy", Locale.ENGLISH).format(Date())
    }

    fun getCurrentMonth(): String {
        return SimpleDateFormat("MM", Locale.ENGLISH).format(Date())
    }

    fun getMonth(month: Int): String {
        return DateFormatSymbols(Locale.ENGLISH).months[month - 1]
    }

    fun getStringFromIntList(list: MutableList<Int>): String {
        return TextUtils.join(", ", list).replace(",", "").replace(" ", "")
    }

    @SuppressLint("SimpleDateFormat")
    fun historyTimeToDisplayDate(time: String): String {
        return try {

            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            val value = formatter.parse(time)

            val newFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            time
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun dateToDisplayTime(time: String): String {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.UK)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            val value = formatter.parse(time)
            val newFormat = SimpleDateFormat("hh:mm a", Locale.UK)

            //val newFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.UK)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            time
        }
    }

    fun displayTwoDecimalPlaces(num: Double): String {
        if (num == null) {
            return "0.00"
        }
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(num)
    }


    fun transferTimeToDisplayDate(time: String): String {
        return try {

            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.UK)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            val value = formatter.parse(time)

            val newFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.UK)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            time
        }
    }

    fun convertDateFormat(time: String, inputFromat: String, outputFormat: String): String {
        return try {

            val formatter = SimpleDateFormat(inputFromat, Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Bangkok")
            val value = formatter.parse(time)

            val newFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            time
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun historyTimeToDisplayTime(time: String): String {
        return try {
            val formatter = SimpleDateFormat("HH:mm", Locale.UK)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Bangkok")
            val value = formatter.parse(time)

            val newFormat = SimpleDateFormat("HH:mm", Locale.UK)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            time
        }
    }

    fun allowDecimalAndLimit2Digit(et: EditText) {
        et.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(edt: Editable) {

                val temp = edt.toString()
                val posDot = temp.indexOf(".")

                if (posDot <= 0) {
                    return
                }
                if (temp.length - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4)
                }
            }
        })
    }

    fun displayCreditCard(_creditCard: String, divider: String): String {
        if (_creditCard.isEmpty()) return String.empty()
        val pre = _creditCard.replace(" ", "")
        if (pre.length != 16) {
            return _creditCard
        }
        val p1 = pre.substring(0, 4)
        val p2 = pre.substring(4, 8)
        val p3 = pre.substring(8, 12)
        val p4 = pre.substring(12, 16)
        return (p1 + divider + p2 + divider + p3 + divider + p4).replace("x", "X")
    }

    fun displayWalletId(_creditCard: String, divider: String): String {
        if (_creditCard.isEmpty()) return String.empty()
        val pre = _creditCard.replace(" ", "")
        if (pre.length != 15) {
            return _creditCard
        }
        val p1 = pre.substring(0, 4)
        val p2 = pre.substring(4, 8)
        val p3 = pre.substring(8, 12)
        val p4 = pre.substring(12, 15)
        return (p1 + divider + p2 + divider + p3 + divider + p4).replace("x", "X")
    }

    fun maskedCreditCard(creditCard: String): String {
        return creditCard.replace(" ", "").replace("\\w(?=\\w{4})".toRegex(), "X")
    }

    fun maskedBigCardMember(cardNumber: String): String {
        var index: Int = 0
        val mask = "###-- *** ---### #"
        val maskedNumber = StringBuilder()

        for (i in 0.until(mask.length)) {
            val c = mask[i]

            if (c == '#') {
                maskedNumber.append(cardNumber.get(index));
                index++;
            } else if (c == '*') {
                maskedNumber.append(c);
                index++;
            } else if (c == '-') {
                maskedNumber.append("");
                index++;
            } else {
                maskedNumber.append(c);
            }
        }

        return maskedNumber.toString()
    }

    fun maskedBigWalletCardNumber(bigWalletCardNumber: String): String {

        if (bigWalletCardNumber.length < 15) {
            return bigWalletCardNumber
        }
        var index: Int = 0
        val mask = "*****##########"
        val maskedNumber = StringBuilder()

        for (i in 0..mask.length - 1) {
            var c = mask.get(i)

            if (c == '#') {
                maskedNumber.append(bigWalletCardNumber.get(index));
                index++;
            } else if (c == 'x') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }

        return maskedNumber.toString()
    }


    fun maskedBigWalletId(bigWalletId: String): String {
        var index = 0
        val mask = "XXXXXXXXXXX###X"
        val maskedNumber = StringBuilder()

        for (i in 0 until mask.length) {
            var c = mask.get(i)

            if (c == '#') {
                maskedNumber.append(bigWalletId[index])
                index++
            } else if (c == 'X') {
                maskedNumber.append(c)
                index++
            } else {
                maskedNumber.append(c)
            }
        }

        return maskedNumber.toString()
    }

    fun encodeBitmapToBase64(
        image: Bitmap,
        compressFormat: Bitmap.CompressFormat,
        quality: Int
    ): String {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(compressFormat, quality, byteArrayOS)
        return android.util.Base64.encodeToString(
            byteArrayOS.toByteArray(),
            android.util.Base64.DEFAULT
        )
    }

    fun decodeBitmapFromBase64(input: String): Bitmap {
        val decodedBytes = android.util.Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun getDateOfFormat(date: String, format: String, inputFormat: String, locale: Locale): String {

        return try {
            val formatter = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(date)

            val newFormat = SimpleDateFormat(format, Locale.ENGLISH)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            date
        }

    }

    fun isToday(date: String, format: String, locale: Locale?): Boolean {
        val parser = SimpleDateFormat(format, locale)
        return DateUtils.isToday(parser.parse(date).time)
    }

    fun transactionHistoryDate(time: String): String {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Bangkok")
            val value = formatter.parse(time)

            val newFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Rangoon")

            newFormat.format(value)
        } catch (e: Exception) {
            time
        }
    }

    fun notificationDate(time: String): String {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            val value = formatter.parse(time)

            val newFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            newFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")
            newFormat.format(value)
        } catch (e: Exception) {
            time
        }
    }

    fun getCurrentDateInFormat(format: String): String {

        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH);
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Rangoon")

        return dateFormat.format(calender.getTime());
    }

    fun getBase64FromUri(selectedfile: Uri, contentResolver: ContentResolver): String {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedfile)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()

        //Use your Base64 String as you wish
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    }

    fun getMobileNumber(receiverMobile: String?): String {
        if (receiverMobile != null) {
            if (receiverMobile[0] == '0') {
                return receiverMobile.substring(1)
            }
        }
        return receiverMobile.toString()
    }

    fun amountWithDecimal(amount: Double?): String {
        val nf = NumberFormat.getNumberInstance(Locale.US)
        val formatter = nf as DecimalFormat
        formatter.applyPattern("###,###,##0.00")

        return formatter.format(amount)
    }

    fun formatMobileNo(string: String?): String {
        var index = 0
        val mask = "XXX XXX XXX XXX"
        val maskedNumber = StringBuilder()
        for (i in 0 until mask.length) {
            val c = mask[i]

            if (c == 'X') {
                maskedNumber.append(string?.get(i)?:"")
                index++
            } else {
                maskedNumber.append(c)
            }
        }

        return maskedNumber.toString()
    }

    fun SpanColorChange(start: Int, end: Int, text: String): SpannableString {
        val wordtoSpan = SpannableString(text)
        wordtoSpan.setSpan(ForegroundColorSpan(Color.RED), start, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordtoSpan.setSpan(ForegroundColorSpan(Color.RED), end, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return wordtoSpan
    }

    fun amountWithDecimal1(amount: Double?): String {
        return String.format(Locale.US, "%.2f", amount)
    }

    fun getRefNo(): String {

        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyMMddHHmmsss", Locale.ENGLISH)

        val timeStamp: String = dateFormat.format(calender.getTime()).replace("-", "")

        val timeStamp1: String = timeStamp.replace("T", "")

        val timeStamp2: String = timeStamp1.replace(":", "")

        return dateFormat.format(calender.getTime())

    }

    fun removeLastChar(str:String):String {
        var str1 = str
        if (str1.isNotEmpty() && str1[str1.length - 1] == '.')
        {
            str1 = str1.substring(0, str.length - 1)
        }
        return str1
    }

}
