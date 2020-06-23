
package com.sphtech.shared.util
//
//import org.spongycastle.jce.provider.BouncyCastleProvider
//import org.spongycastle.util.encoders.Base64
//import javax.crypto.Cipher
//import javax.crypto.spec.IvParameterSpec
//import javax.crypto.spec.SecretKeySpec
//
//class AESUtils {
//
//    private fun getSecretKeySpec(key: String): SecretKeySpec? {
//        try {
//            return SecretKeySpec(key.toByteArray(charset(AES_CHARSET)), AES_ALGORITHM)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    fun decrypt(strToDecrypt: String, key: String, iv: String): String {
//        if (strToDecrypt.isEmpty()) {
//            return ""
//        }
//        try {
//            val cipher = Cipher.getInstance(AES_CIPHER_ALG, BouncyCastleProvider())
//            cipher.init(
//                Cipher.DECRYPT_MODE,
//                getSecretKeySpec(key),
//                IvParameterSpec(iv.toByteArray())
//            )
//            return byteToString(cipher.doFinal(decodeBase64(strToDecrypt)))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return ""
//    }
//
//    companion object {
//
//        private val AES_ALGORITHM = "AES"
//        private val AES_CIPHER_ALG = "AES/CBC/PKCS7Padding"
//        private val AES_CHARSET = "UTF-8"
//
//        fun decodeBase64(input: String): ByteArray {
//            if (input.isEmpty()) {
//                return "".toByteArray()
//            }
//            return Base64.decode(input)
//        }
//
//        fun byteToString(input: ByteArray): String {
//            return String(input)
//        }
//    }
//}