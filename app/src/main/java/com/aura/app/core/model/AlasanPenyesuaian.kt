package com.aura.app.core.model

/** Alasan wajib pada Penyesuaian Stok (spec/06). Pergerakannya selalu jenis PENYESUAIAN (K-17). */
enum class AlasanPenyesuaian {
    RUSAK,
    HILANG,
    KADALUARSA,
    SALAH_CATAT,
    LAINNYA,
}
