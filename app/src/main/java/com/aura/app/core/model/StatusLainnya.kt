package com.aura.app.core.model

/** Metode pembayaran hanya label pencatatan; tidak ada payment gateway. */
enum class MetodePembayaran { TUNAI, TRANSFER, QRIS }

/** Tanpa "Perlu Ditinjau" karena aplikasi wajib online (K-20). */
enum class StatusTransaksi { SELESAI, DIBATALKAN, DIRETUR }

enum class StatusAkun { AKTIF, NONAKTIF }

enum class StatusBarang { AKTIF, DIARSIPKAN }

enum class StatusSupplier { AKTIF, NONAKTIF }

enum class JenisPengeluaran { PEMBELIAN, KUSTOM }

/** Catatan keuangan tidak dihapus user, hanya dibatalkan beserta alasan. */
enum class StatusCatatanKeuangan { AKTIF, DIBATALKAN }

/** Lima jenis catatan yang masing-masing dibatasi 5.000 entri (D-15). */
enum class JenisCatatanTerbatas {
    PENJUALAN,
    RESTOCK,
    PENYESUAIAN_STOK,
    PENGELUARAN_KUSTOM,
    PEMASUKAN_KUSTOM,
}

/** Jenis notifikasi setelah Manajer dihapus (spec/00-perubahan-dari-stitch.md §6). */
enum class JenisNotifikasi {
    STOK_HABIS,
    STOK_MENIPIS,
    ANOMALI,
    PEMBARUAN_VERSI,
    BATAS_DATA,
    PENGAJUAN_PEMBATALAN,
}

/** Jenis aktivitas pada Riwayat Aktivitas (tanpa Persetujuan). */
enum class KategoriAktivitas { BARANG, STOK, KEUANGAN, PENJUALAN, AKUN, LOGIN }
