package com.aura.app.core.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class StokCalculatorTest {

    @Test
    fun masuk_menambah_stok() {
        assertEquals(15L, StokCalculator.stokSesudah(stokSaatIni = 10, jumlah = 5))
    }

    @Test
    fun keluar_mengurangi_stok() {
        assertEquals(7L, StokCalculator.stokSesudah(stokSaatIni = 10, jumlah = -3))
    }

    @Test
    fun keluar_tepat_habis_menjadi_nol() {
        assertEquals(0L, StokCalculator.stokSesudah(stokSaatIni = 4, jumlah = -4))
    }

    @Test
    fun keluar_melebihi_stok_ditolak() {
        val e = assertThrows(StokTidakCukupException::class.java) {
            StokCalculator.stokSesudah(stokSaatIni = 2, jumlah = -3)
        }
        assertEquals(2L, e.stokTersedia)
        assertEquals(3L, e.jumlahDiminta)
    }

    @Test
    fun stok_awal_negatif_ditolak() {
        assertThrows(IllegalArgumentException::class.java) {
            StokCalculator.stokSesudah(stokSaatIni = -1, jumlah = 5)
        }
    }

    @Test
    fun selisih_penyesuaian_positif_nol_negatif() {
        assertEquals(3L, StokCalculator.selisihPenyesuaian(stokSistem = 10, stokFisik = 13))
        assertEquals(0L, StokCalculator.selisihPenyesuaian(stokSistem = 10, stokFisik = 10))
        assertEquals(-4L, StokCalculator.selisihPenyesuaian(stokSistem = 10, stokFisik = 6))
    }

    @Test
    fun stok_fisik_negatif_ditolak() {
        assertThrows(IllegalArgumentException::class.java) {
            StokCalculator.selisihPenyesuaian(stokSistem = 10, stokFisik = -1)
        }
    }
}
