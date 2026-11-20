package com.aura.app.core.format

/** Format nominal baku: `Rp 1.250.000` (konvensi UI #4). */
object RupiahFormatter {

    /** Teks pengganti saat nominal disembunyikan (konvensi UI #6). */
    const val TERSEMBUNYI = "Rp ••••••"

    fun format(nominal: Long): String {
        val angka = AngkaFormatter.ribuan(kotlin.math.abs(nominal))
        return if (nominal < 0) "-Rp $angka" else "Rp $angka"
    }

    fun formatAtauSembunyi(nominal: Long, sembunyikan: Boolean): String =
        if (sembunyikan) TERSEMBUNYI else format(nominal)
}
