package com.aura.app.core.logic

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Perhitungan harga beli dan HPP. Semua uang dalam rupiah penuh (`Long`),
 * hasil bagi dibulatkan ke rupiah terdekat (K-05).
 */
object HppCalculator {

    /** Harga per item = total dibayar / jumlah masuk (Restock langkah 2). */
    fun hargaPerItem(totalDibayar: Long, jumlahMasuk: Long): Long {
        require(jumlahMasuk > 0) { "Jumlah masuk harus lebih dari 0" }
        require(totalDibayar >= 0) { "Total dibayar tidak boleh negatif" }
        return bagiBulat(BigDecimal.valueOf(totalDibayar), BigDecimal.valueOf(jumlahMasuk))
    }

    /**
     * HPP rata-rata tertimbang (K-15):
     * `(stokLama × hppLama + jumlahMasuk × hargaPerItem) / (stokLama + jumlahMasuk)`.
     * Jika stok lama ≤ 0, HPP baru = harga per item restock tersebut.
     */
    fun hppBaru(stokLama: Long, hppLama: Long, jumlahMasuk: Long, hargaPerItem: Long): Long {
        require(jumlahMasuk > 0) { "Jumlah masuk harus lebih dari 0" }
        require(hppLama >= 0) { "HPP lama tidak boleh negatif" }
        require(hargaPerItem >= 0) { "Harga per item tidak boleh negatif" }
        if (stokLama <= 0) return hargaPerItem

        val nilaiLama = BigDecimal.valueOf(stokLama).multiply(BigDecimal.valueOf(hppLama))
        val nilaiMasuk = BigDecimal.valueOf(jumlahMasuk).multiply(BigDecimal.valueOf(hargaPerItem))
        val totalStok = BigDecimal.valueOf(stokLama + jumlahMasuk)
        return bagiBulat(nilaiLama.add(nilaiMasuk), totalStok)
    }

    /** HPP awal saat Tambah Barang: harga beli awal opsional, tanpa harga beli → 0 (K-15). */
    fun hppAwal(hargaBeliAwal: Long?): Long {
        require(hargaBeliAwal == null || hargaBeliAwal >= 0) { "Harga beli awal tidak boleh negatif" }
        return hargaBeliAwal ?: 0L
    }

    private fun bagiBulat(pembilang: BigDecimal, penyebut: BigDecimal): Long =
        pembilang.divide(penyebut, 0, RoundingMode.HALF_UP).longValueExact()
}
