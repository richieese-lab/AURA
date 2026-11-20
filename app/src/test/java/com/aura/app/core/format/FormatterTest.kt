package com.aura.app.core.format

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZonedDateTime

class FormatterTest {

    @Test
    fun format_rupiah() {
        assertEquals("Rp 0", RupiahFormatter.format(0))
        assertEquals("Rp 500", RupiahFormatter.format(500))
        assertEquals("Rp 3.500", RupiahFormatter.format(3_500))
        assertEquals("Rp 1.250.000", RupiahFormatter.format(1_250_000))
        assertEquals("Rp 1.000.000.000", RupiahFormatter.format(1_000_000_000))
        assertEquals("-Rp 45.000", RupiahFormatter.format(-45_000))
    }

    @Test
    fun rupiah_tersembunyi() {
        assertEquals("Rp ••••••", RupiahFormatter.formatAtauSembunyi(1_250_000, sembunyikan = true))
        assertEquals("Rp 1.250.000", RupiahFormatter.formatAtauSembunyi(1_250_000, sembunyikan = false))
    }

    @Test
    fun jumlah_stok_dengan_satuan() {
        assertEquals("12 pcs", JumlahStokFormatter.format(12, "pcs"))
        assertEquals("25.000 gram", JumlahStokFormatter.format(25_000, "gram"))
        assertEquals("0 pcs", JumlahStokFormatter.format(0, "pcs"))
        assertEquals("7", JumlahStokFormatter.format(7, " "))
    }

    private fun wib(tahun: Int, bulan: Int, hari: Int, jam: Int, menit: Int): Long =
        ZonedDateTime.of(tahun, bulan, hari, jam, menit, 0, 0, TanggalFormatter.ZONA_WIB)
            .toInstant().toEpochMilli()

    @Test
    fun tanggal_jam_baku() {
        assertEquals("17 Nov 2026, 19.48 WIB", TanggalFormatter.tanggalJam(wib(2026, 11, 17, 19, 48)))
    }

    @Test
    fun tanggal_jam_tengah_malam_dan_nama_bulan_indonesia() {
        assertEquals("1 Mei 2026, 00.05 WIB", TanggalFormatter.tanggalJam(wib(2026, 5, 1, 0, 5)))
        assertEquals("31 Agu 2026, 23.59 WIB", TanggalFormatter.tanggalJam(wib(2026, 8, 31, 23, 59)))
        assertEquals("9 Okt 2026", TanggalFormatter.tanggal(wib(2026, 10, 9, 8, 0)))
        assertEquals("25 Des 2026", TanggalFormatter.tanggal(wib(2026, 12, 25, 8, 0)))
    }

    @Test
    fun selalu_zona_wib_walau_input_dari_utc() {
        // 31 Des 2026 18.30 UTC = 1 Jan 2027 01.30 WIB (pergantian tahun)
        val utc = ZonedDateTime.of(2026, 12, 31, 18, 30, 0, 0, java.time.ZoneOffset.UTC).toInstant().toEpochMilli()
        assertEquals("1 Jan 2027, 01.30 WIB", TanggalFormatter.tanggalJam(utc))
    }
}
