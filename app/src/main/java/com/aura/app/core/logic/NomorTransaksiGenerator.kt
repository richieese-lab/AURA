package com.aura.app.core.logic

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Nomor transaksi `INV-YYMMDD-NNNN`, urut per toko per hari (contoh: INV-261117-0001).
 * Nomor final nantinya diberikan server (K-20); generator ini dipakai selama pengembangan.
 */
object NomorTransaksiGenerator {

    private const val PREFIX = "INV"
    private const val URUTAN_MAKS = 9999
    private val FORMAT_TANGGAL: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd")
    private val POLA = Regex("""^INV-(\d{6})-(\d{4})$""")

    fun buat(tanggal: LocalDate, urutan: Int): String {
        require(urutan in 1..URUTAN_MAKS) { "Urutan transaksi harus 1 sampai $URUTAN_MAKS" }
        return "$PREFIX-${tanggal.format(FORMAT_TANGGAL)}-${urutan.toString().padStart(4, '0')}"
    }

    /**
     * Nomor berikutnya untuk [tanggal]. Jika [nomorTerakhir] kosong atau berasal dari hari lain,
     * urutan dimulai lagi dari 0001.
     */
    fun berikutnya(nomorTerakhir: String?, tanggal: LocalDate): String {
        val cocok = nomorTerakhir?.let { POLA.matchEntire(it) }
        val urutanTerakhir = if (cocok != null && cocok.groupValues[1] == tanggal.format(FORMAT_TANGGAL)) {
            cocok.groupValues[2].toInt()
        } else {
            0
        }
        return buat(tanggal, urutanTerakhir + 1)
    }
}
