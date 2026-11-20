package com.aura.app.core.model

/**
 * Kode kesalahan validasi dari repository. Teks untuk user dipetakan di lapisan UI lewat
 * strings.xml (K-12), sehingga data layer tidak bergantung pada Context.
 */
enum class KesalahanValidasi {
    NAMA_KOSONG,
    SATUAN_KOSONG,
    HARGA_NEGATIF,
    STOK_MINIMUM_NEGATIF,
    STOK_AWAL_NEGATIF,
    JUMLAH_HARUS_POSITIF,
    TOTAL_NEGATIF,
    STOK_FISIK_NEGATIF,
    TIDAK_ADA_SELISIH,
    LINK_TIDAK_VALID,
    KATEGORI_SUDAH_ADA,
    KATEGORI_TIDAK_DITEMUKAN,
    BARANG_TIDAK_DITEMUKAN,
    BARANG_DIARSIPKAN,
    SUPPLIER_TIDAK_DITEMUKAN,
    SUPPLIER_NONAKTIF,
    SUPPLIER_PERNAH_DIPAKAI,
    TOKO_BELUM_ADA,
}

class ValidasiException(val kesalahan: KesalahanValidasi) :
    IllegalArgumentException("Validasi gagal: $kesalahan")
