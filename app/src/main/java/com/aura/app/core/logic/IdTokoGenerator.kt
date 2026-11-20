package com.aura.app.core.logic

import java.security.SecureRandom
import java.util.Random

/**
 * ID Toko unik: 8 karakter huruf besar dan angka, tidak dapat diubah (spec/03).
 * Generator acak disuntikkan agar bisa dibuat deterministik di unit test.
 */
class IdTokoGenerator(
    private val random: Random = SecureRandom(),
) {
    fun buat(): String = buildString(PANJANG) {
        repeat(PANJANG) { append(KARAKTER[random.nextInt(KARAKTER.length)]) }
    }

    companion object {
        const val PANJANG = 8
        const val KARAKTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

        fun valid(idToko: String): Boolean =
            idToko.length == PANJANG && idToko.all { it in KARAKTER }
    }
}
