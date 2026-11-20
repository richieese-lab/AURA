package com.aura.app.core.format

/** Pemisah ribuan gaya Indonesia (titik), tanpa bergantung pada Locale perangkat. */
object AngkaFormatter {

    fun ribuan(angka: Long): String {
        val tanda = if (angka < 0) "-" else ""
        val digit = kotlin.math.abs(angka).toString()
        val hasil = StringBuilder()
        digit.forEachIndexed { index, c ->
            if (index > 0 && (digit.length - index) % 3 == 0) hasil.append('.')
            hasil.append(c)
        }
        return tanda + hasil
    }
}
