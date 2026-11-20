package com.aura.app.core.access

import com.aura.app.core.model.Peran
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class AksesPeranTest {

    @Test
    fun owner_boleh_semua_kecuali_tombol_khusus_kasir() {
        Fitur.entries
            .filter { it != Fitur.AJUKAN_PEMBATALAN }
            .forEach { assertTrue(it.name, AksesPeran.diizinkan(Peran.OWNER, it)) }
        assertFalse(AksesPeran.diizinkan(Peran.OWNER, Fitur.AJUKAN_PEMBATALAN))
    }

    @Test
    fun kasir_hanya_menu_kasir() {
        val bolehUntukKasir = Fitur.entries.filter { AksesPeran.diizinkan(Peran.KASIR, it) }.toSet()
        assertEquals(setOf(Fitur.KASIR, Fitur.CATAT_PENJUALAN, Fitur.AJUKAN_PEMBATALAN), bolehUntukKasir)
    }

    @Test
    fun kasir_tidak_bisa_kembali_dari_menu_kasir() {
        assertFalse(AksesPeran.diizinkan(Peran.KASIR, Fitur.KEMBALI_DARI_KASIR))
        assertEquals(AlasanPembatasan.KASIR_KELUAR_LEWAT_AVATAR, Fitur.KEMBALI_DARI_KASIR.alasan)
    }

    @Test
    fun kasir_tidak_bisa_membatalkan_langsung() {
        assertFalse(AksesPeran.diizinkan(Peran.KASIR, Fitur.BATALKAN_TRANSAKSI))
        assertTrue(AksesPeran.diizinkan(Peran.KASIR, Fitur.AJUKAN_PEMBATALAN))
    }

    @Test
    fun pastikan_melempar_saat_ditolak() {
        val e = assertThrows(AksesDitolakException::class.java) {
            AksesPeran.pastikan(Peran.KASIR, Fitur.KELOLA_BARANG)
        }
        assertEquals(Fitur.KELOLA_BARANG, e.fitur)
        AksesPeran.pastikan(Peran.OWNER, Fitur.KELOLA_BARANG) // tidak melempar
    }

    @Test
    fun hanya_owner_punya_bottom_nav() {
        assertTrue(Peran.OWNER.punyaBottomNav)
        assertFalse(Peran.KASIR.punyaBottomNav)
    }
}
