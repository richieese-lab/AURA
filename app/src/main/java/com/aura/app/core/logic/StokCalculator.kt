package com.aura.app.core.logic

/** Dilempar saat sebuah pergerakan akan membuat stok negatif (Fungsi 4: stok tidak boleh negatif). */
class StokTidakCukupException(
    val stokTersedia: Long,
    val jumlahDiminta: Long,
) : IllegalStateException("Stok tidak cukup: tersedia $stokTersedia, diminta $jumlahDiminta")

/**
 * Perhitungan stok. Stok disimpan sebagai nilai berjalan (K-22) dan selalu bilangan bulat (K-06).
 */
object StokCalculator {

    /**
     * @param stokSaatIni stok sebelum pergerakan.
     * @param jumlah jumlah bertanda: positif = masuk, negatif = keluar.
     * @return stok sesudah pergerakan.
     * @throws StokTidakCukupException bila hasilnya negatif.
     */
    fun stokSesudah(stokSaatIni: Long, jumlah: Long): Long {
        require(stokSaatIni >= 0) { "Stok saat ini tidak boleh negatif" }
        val hasil = stokSaatIni + jumlah
        if (hasil < 0) throw StokTidakCukupException(stokTersedia = stokSaatIni, jumlahDiminta = -jumlah)
        return hasil
    }

    /**
     * Selisih Penyesuaian Stok = stok fisik − stok sistem.
     * Positif berarti fisik lebih banyak, negatif berarti fisik lebih sedikit.
     */
    fun selisihPenyesuaian(stokSistem: Long, stokFisik: Long): Long {
        require(stokSistem >= 0) { "Stok sistem tidak boleh negatif" }
        require(stokFisik >= 0) { "Stok fisik tidak boleh negatif" }
        return stokFisik - stokSistem
    }
}
