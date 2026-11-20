package com.aura.app.core.model

/** Akun yang sedang melakukan aksi; dipakai untuk cek akses & jejak audit. */
data class Pelaku(
    val akunId: String,
    val namaAkun: String,
    val peran: Peran,
)

data class Barang(
    val id: String,
    val sku: String?,
    val fotoPath: String?,
    val nama: String,
    val satuan: String,
    val hargaJual: Long,
    val stokSaatIni: Long,
    val hppRataRata: Long,
    val stokMinimum: Long,
    val status: StatusBarang,
    val statusStok: StatusStok,
    val dibuatOleh: String,
    val createdAt: Long,
)

data class Kategori(
    val id: String,
    val nama: String,
    val jumlahBarang: Int,
)

data class Supplier(
    val id: String,
    val nama: String,
    val link: String?,
    val catatan: String?,
    val status: StatusSupplier,
    val jumlahBarang: Int,
)

data class PergerakanStok(
    val id: String,
    val barangId: String,
    val jenis: JenisPergerakan,
    val sumber: SumberPergerakan,
    val jumlah: Long,
    val stokSebelum: Long,
    val stokSesudah: Long,
    val alasan: AlasanPenyesuaian?,
    val catatan: String?,
    val akunId: String,
    val waktu: Long,
)

data class LogAktivitas(
    val id: String,
    val akunId: String,
    val namaAkun: String,
    val peran: Peran,
    val kategori: KategoriAktivitas,
    val ringkasan: String,
    val nilaiSebelum: String?,
    val nilaiSesudah: String?,
    val waktu: Long,
)

/** Input Tambah Barang. Wajib: nama, harga jual, stok minimum (spec/06). */
data class TambahBarangInput(
    val nama: String,
    val satuan: String,
    val hargaJual: Long,
    val stokMinimum: Long,
    val stokAwal: Long = 0,
    val hargaBeliAwal: Long? = null,
    val sku: String? = null,
    val fotoPath: String? = null,
    val kategoriIds: Set<String> = emptySet(),
)

/** Input Edit Barang. Stok tidak dapat diedit di sini (spec/06 Edit Barang). */
data class EditBarangInput(
    val id: String,
    val nama: String,
    val satuan: String,
    val hargaJual: Long,
    val stokMinimum: Long,
    val sku: String? = null,
    val fotoPath: String? = null,
    val kategoriIds: Set<String> = emptySet(),
)

/** Satu barang per restock (batas versi). */
data class RestockInput(
    val barangId: String,
    val supplierId: String,
    val jumlahMasuk: Long,
    val totalDibayar: Long,
    val tanggal: Long? = null,
    val buktiPath: String? = null,
    val catatan: String? = null,
)

data class PenyesuaianStokInput(
    val barangId: String,
    val stokFisik: Long,
    val alasan: AlasanPenyesuaian,
    val catatan: String? = null,
    val buktiPath: String? = null,
)
