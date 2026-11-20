package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.app.core.model.JenisPengeluaran
import com.aura.app.core.model.StatusCatatanKeuangan

/**
 * Pengeluaran jenis Pembelian (otomatis dari Restock) atau Kustom.
 * Tidak dihapus user; pembatalan = status DIBATALKAN + alasan.
 */
@Entity(
    tableName = "pengeluaran",
    indices = [Index("tanggal"), Index("jenis"), Index("supplierId"), Index("barangId")],
)
data class PengeluaranEntity(
    @PrimaryKey val id: String,
    val jenis: JenisPengeluaran,
    val judul: String? = null,
    val nominal: Long,
    val catatan: String? = null,
    val buktiPath: String? = null,
    val supplierId: String? = null,
    val barangId: String? = null,
    val jumlahBarang: Long? = null,
    val hargaPerItem: Long? = null,
    val pergerakanId: String? = null,
    val tanggal: Long,
    val dicatatOleh: String,
    val status: StatusCatatanKeuangan = StatusCatatanKeuangan.AKTIF,
    val alasanBatal: String? = null,
)

/** Pemasukan Kustom. Pemasukan Penjualan adalah turunan dari transaksi, bukan tabel ini. */
@Entity(
    tableName = "pemasukan_kustom",
    indices = [Index("tanggal")],
)
data class PemasukanKustomEntity(
    @PrimaryKey val id: String,
    val judul: String,
    val nominal: Long,
    val catatan: String? = null,
    val buktiPath: String? = null,
    val tanggal: Long,
    val dicatatOleh: String,
    val status: StatusCatatanKeuangan = StatusCatatanKeuangan.AKTIF,
    val alasanBatal: String? = null,
)
