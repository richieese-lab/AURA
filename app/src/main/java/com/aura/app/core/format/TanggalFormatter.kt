package com.aura.app.core.format

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Format tanggal baku: `17 Nov 2026, 19.48 WIB` (konvensi UI #4).
 * Selalu zona WIB / Asia/Jakarta (K-11), memakai java.time (K-03).
 */
object TanggalFormatter {

    val ZONA_WIB: ZoneId = ZoneId.of("Asia/Jakarta")

    private val NAMA_BULAN = listOf(
        "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
        "Jul", "Agu", "Sep", "Okt", "Nov", "Des",
    )

    /** `17 Nov 2026, 19.48 WIB` */
    fun tanggalJam(epochMillis: Long): String {
        val waktu = keWib(epochMillis)
        return "${tanggal(waktu)}, ${dua(waktu.hour)}.${dua(waktu.minute)} WIB"
    }

    /** `17 Nov 2026` */
    fun tanggal(epochMillis: Long): String = tanggal(keWib(epochMillis))

    private fun tanggal(waktu: ZonedDateTime): String =
        "${waktu.dayOfMonth} ${NAMA_BULAN[waktu.monthValue - 1]} ${waktu.year}"

    private fun keWib(epochMillis: Long): ZonedDateTime =
        Instant.ofEpochMilli(epochMillis).atZone(ZONA_WIB)

    private fun dua(angka: Int): String = angka.toString().padStart(2, '0')
}
