package com.aura.app.core.logic

import com.aura.app.core.model.StatusStok

/**
 * Fungsi 1 (Reorder Point): stok ≤ stok minimum → Menipis, stok 0 → Habis.
 * Bila stok minimum 0, peringatan hanya muncul saat stok = 0 (field Barang #11).
 */
object StatusStokEvaluator {

    fun evaluasi(stok: Long, stokMinimum: Long, diarsipkan: Boolean): StatusStok = when {
        diarsipkan -> StatusStok.DIARSIPKAN
        stok <= 0 -> StatusStok.HABIS
        stok <= stokMinimum -> StatusStok.MENIPIS
        else -> StatusStok.AMAN
    }
}
