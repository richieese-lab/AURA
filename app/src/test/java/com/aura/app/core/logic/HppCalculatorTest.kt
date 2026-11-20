package com.aura.app.core.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class HppCalculatorTest {

    @Test
    fun harga_per_item_pembagian_bulat() {
        assertEquals(3_500L, HppCalculator.hargaPerItem(totalDibayar = 70_000, jumlahMasuk = 20))
    }

    @Test
    fun harga_per_item_dibulatkan_ke_rupiah_terdekat() {
        assertEquals(33_333L, HppCalculator.hargaPerItem(totalDibayar = 100_000, jumlahMasuk = 3))
        assertEquals(66_667L, HppCalculator.hargaPerItem(totalDibayar = 200_000, jumlahMasuk = 3))
        // tepat setengah dibulatkan ke atas
        assertEquals(3L, HppCalculator.hargaPerItem(totalDibayar = 5, jumlahMasuk = 2))
    }

    @Test
    fun harga_per_item_jumlah_nol_ditolak() {
        assertThrows(IllegalArgumentException::class.java) {
            HppCalculator.hargaPerItem(totalDibayar = 10_000, jumlahMasuk = 0)
        }
    }

    @Test
    fun hpp_rata_rata_tertimbang() {
        // (10 × 3.000 + 10 × 4.000) / 20 = 3.500
        assertEquals(3_500L, HppCalculator.hppBaru(stokLama = 10, hppLama = 3_000, jumlahMasuk = 10, hargaPerItem = 4_000))
    }

    @Test
    fun hpp_rata_rata_dibulatkan() {
        // (1 × 1.000 + 2 × 2.000) / 3 = 1.666,67 → 1.667
        assertEquals(1_667L, HppCalculator.hppBaru(stokLama = 1, hppLama = 1_000, jumlahMasuk = 2, hargaPerItem = 2_000))
    }

    @Test
    fun stok_lama_nol_hpp_sama_dengan_harga_restock() {
        assertEquals(4_000L, HppCalculator.hppBaru(stokLama = 0, hppLama = 3_000, jumlahMasuk = 5, hargaPerItem = 4_000))
    }

    @Test
    fun stok_lama_negatif_diperlakukan_seperti_nol() {
        assertEquals(4_000L, HppCalculator.hppBaru(stokLama = -2, hppLama = 3_000, jumlahMasuk = 5, hargaPerItem = 4_000))
    }

    @Test
    fun beberapa_restock_berurutan() {
        var stok = 0L
        var hpp = HppCalculator.hppAwal(null)
        listOf(10L to 30_000L, 10L to 50_000L, 20L to 60_000L).forEach { (jumlah, total) ->
            val harga = HppCalculator.hargaPerItem(total, jumlah)
            hpp = HppCalculator.hppBaru(stok, hpp, jumlah, harga)
            stok += jumlah
        }
        // restock 1: 3.000; restock 2: (10×3.000 + 10×5.000)/20 = 4.000; restock 3: (20×4.000 + 20×3.000)/40 = 3.500
        assertEquals(40L, stok)
        assertEquals(3_500L, hpp)
    }

    @Test
    fun hpp_awal_tanpa_harga_beli_nol() {
        assertEquals(0L, HppCalculator.hppAwal(null))
        assertEquals(2_500L, HppCalculator.hppAwal(2_500))
    }
}
