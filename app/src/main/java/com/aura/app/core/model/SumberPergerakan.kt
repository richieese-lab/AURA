package com.aura.app.core.model

/**
 * Sumber pergerakan stok sesuai daftar dokumen:
 * - Masuk: Restock, Stok awal, Retur dari pelanggan, Pembatalan penjualan.
 * - Keluar: Penjualan, Rusak, Hilang, Kadaluarsa.
 * - Penyesuaian: koreksi selisih hitung fisik (stock opname). Alasan Penyesuaian Stok
 *   disimpan terpisah di [AlasanPenyesuaian] (K-17).
 */
enum class SumberPergerakan(val jenis: JenisPergerakan) {
    RESTOCK(JenisPergerakan.MASUK),
    STOK_AWAL(JenisPergerakan.MASUK),
    RETUR_PELANGGAN(JenisPergerakan.MASUK),
    PEMBATALAN_PENJUALAN(JenisPergerakan.MASUK),
    PENJUALAN(JenisPergerakan.KELUAR),
    RUSAK(JenisPergerakan.KELUAR),
    HILANG(JenisPergerakan.KELUAR),
    KADALUARSA(JenisPergerakan.KELUAR),
    STOCK_OPNAME(JenisPergerakan.PENYESUAIAN),
}
