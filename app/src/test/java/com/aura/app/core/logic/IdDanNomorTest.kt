package com.aura.app.core.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.util.Random

class IdDanNomorTest {

    @Test
    fun id_toko_8_karakter_huruf_besar_dan_angka() {
        val generator = IdTokoGenerator(Random(42))
        repeat(200) {
            val id = generator.buat()
            assertEquals(8, id.length)
            assertTrue(id, IdTokoGenerator.valid(id))
        }
    }

    @Test
    fun id_toko_deterministik_dengan_seed_sama() {
        assertEquals(IdTokoGenerator(Random(7)).buat(), IdTokoGenerator(Random(7)).buat())
    }

    @Test
    fun validasi_id_toko() {
        assertTrue(IdTokoGenerator.valid("AB12CD34"))
        assertFalse(IdTokoGenerator.valid("ab12cd34"))
        assertFalse(IdTokoGenerator.valid("AB12CD3"))
        assertFalse(IdTokoGenerator.valid("AB12-D34"))
    }

    private val tgl = LocalDate.of(2026, 11, 17)

    @Test
    fun format_nomor_transaksi() {
        assertEquals("INV-261117-0001", NomorTransaksiGenerator.buat(tgl, 1))
        assertEquals("INV-261117-0123", NomorTransaksiGenerator.buat(tgl, 123))
    }

    @Test
    fun nomor_pertama_hari_ini() {
        assertEquals("INV-261117-0001", NomorTransaksiGenerator.berikutnya(null, tgl))
    }

    @Test
    fun nomor_berurutan_di_hari_yang_sama() {
        assertEquals("INV-261117-0042", NomorTransaksiGenerator.berikutnya("INV-261117-0041", tgl))
    }

    @Test
    fun nomor_reset_di_hari_baru() {
        assertEquals("INV-261118-0001", NomorTransaksiGenerator.berikutnya("INV-261117-0099", tgl.plusDays(1)))
    }

    @Test
    fun urutan_di_luar_batas_ditolak() {
        assertThrows(IllegalArgumentException::class.java) { NomorTransaksiGenerator.buat(tgl, 0) }
        assertThrows(IllegalArgumentException::class.java) { NomorTransaksiGenerator.buat(tgl, 10_000) }
    }

    @Test
    fun link_supplier_valid() {
        assertTrue(LinkValidator.valid("https://wa.me/6281234567890"))
        assertTrue(LinkValidator.valid("https://t.me/tokosupplier"))
        assertTrue(LinkValidator.valid("http://instagram.com/supplier.id"))
        assertFalse(LinkValidator.valid("wa.me/6281234567890"))
        assertFalse(LinkValidator.valid("bukan link"))
        assertFalse(LinkValidator.valid(""))
        assertFalse(LinkValidator.valid("ftp://contoh.com"))
    }
}
