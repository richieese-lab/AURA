package com.aura.app.core.logic

import com.aura.app.core.model.StatusStok
import org.junit.Assert.assertEquals
import org.junit.Test

class StatusStokEvaluatorTest {

    private fun status(stok: Long, minimum: Long, arsip: Boolean = false) =
        StatusStokEvaluator.evaluasi(stok, minimum, arsip)

    @Test
    fun di_atas_minimum_aman() {
        assertEquals(StatusStok.AMAN, status(stok = 11, minimum = 10))
    }

    @Test
    fun tepat_minimum_menipis() {
        assertEquals(StatusStok.MENIPIS, status(stok = 10, minimum = 10))
    }

    @Test
    fun di_bawah_minimum_menipis() {
        assertEquals(StatusStok.MENIPIS, status(stok = 1, minimum = 10))
    }

    @Test
    fun stok_nol_habis() {
        assertEquals(StatusStok.HABIS, status(stok = 0, minimum = 10))
    }

    @Test
    fun minimum_nol_hanya_peringatan_saat_habis() {
        assertEquals(StatusStok.AMAN, status(stok = 1, minimum = 0))
        assertEquals(StatusStok.HABIS, status(stok = 0, minimum = 0))
    }

    @Test
    fun arsip_mengalahkan_status_lain() {
        assertEquals(StatusStok.DIARSIPKAN, status(stok = 0, minimum = 10, arsip = true))
        assertEquals(StatusStok.DIARSIPKAN, status(stok = 50, minimum = 10, arsip = true))
    }
}
