package com.aura.app.core.access

import com.aura.app.core.model.Peran

/**
 * Alasan pembatasan. Teks yang tampil di snackbar ada di strings.xml (K-12),
 * dipetakan di lapisan UI, karena paket core tidak boleh bergantung pada Android.
 */
enum class AlasanPembatasan {
    /** "Hanya Owner yang dapat mengubah data ini" */
    HANYA_OWNER,

    /** "Kasir hanya dapat keluar melalui profil avatar" */
    KASIR_KELUAR_LEWAT_AVATAR,

    /** Tombol yang memang khusus Kasir (mis. "Ajukan pembatalan"). Tidak ditampilkan untuk Owner. */
    KHUSUS_KASIR,
}

/**
 * Elemen/aksi yang dibatasi peran (spec/00-perubahan-dari-stitch.md §1, konvensi UI #7–#10).
 */
enum class Fitur(
    val peranDiizinkan: Set<Peran>,
    val alasan: AlasanPembatasan,
) {
    DASHBOARD(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    MANAJEMEN(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    ANALISIS(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    PROFIL_TAB(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    PENGATURAN(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    KELOLA_BARANG(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    KELOLA_KATEGORI(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    KELOLA_SUPPLIER(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    RESTOCK(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    PENYESUAIAN_STOK(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    PERGERAKAN_STOK(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    RIWAYAT_AKTIVITAS(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    CATAT_KEUANGAN(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    BATALKAN_TRANSAKSI(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),
    LIHAT_SEMUA_TRANSAKSI(setOf(Peran.OWNER), AlasanPembatasan.HANYA_OWNER),

    /** Tombol kembali di K1 Beranda Kasir: abu-abu untuk Kasir. */
    KEMBALI_DARI_KASIR(setOf(Peran.OWNER), AlasanPembatasan.KASIR_KELUAR_LEWAT_AVATAR),

    KASIR(setOf(Peran.OWNER, Peran.KASIR), AlasanPembatasan.HANYA_OWNER),
    CATAT_PENJUALAN(setOf(Peran.OWNER, Peran.KASIR), AlasanPembatasan.HANYA_OWNER),
    AJUKAN_PEMBATALAN(setOf(Peran.KASIR), AlasanPembatasan.KHUSUS_KASIR),
}

/** Dilempar repository bila peran tidak berhak (aturan akses juga ditegakkan di sisi data). */
class AksesDitolakException(
    val fitur: Fitur,
    val peran: Peran,
) : IllegalStateException("Peran $peran tidak berhak atas $fitur")

object AksesPeran {

    fun diizinkan(peran: Peran, fitur: Fitur): Boolean = peran in fitur.peranDiizinkan

    fun pastikan(peran: Peran, fitur: Fitur) {
        if (!diizinkan(peran, fitur)) throw AksesDitolakException(fitur, peran)
    }
}
