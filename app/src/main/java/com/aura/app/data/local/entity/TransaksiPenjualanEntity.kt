package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.app.core.model.MetodePembayaran
import com.aura.app.core.model.StatusTransaksi

/**
 * Transaksi penjualan. Tidak dihapus user, hanya dibatalkan (status + alasan).
 * Tanpa diskon (K-21) dan tanpa "Perlu Ditinjau" (K-20). Alur penuh dibuat di Tahap 1.
 */
@Entity(
    tableName = "transaksi_penjualan",
    indices = [Index(value = ["nomor"], unique = true), Index("waktu"), Index("kasirId")],
)
data class TransaksiPenjualanEntity(
    @PrimaryKey val id: String,
    val nomor: String,
    val kasirId: String,
    val metodeBayar: MetodePembayaran,
    val total: Long,
    val uangDiterima: Long? = null,
    val kembalian: Long? = null,
    val catatan: String? = null,
    val status: StatusTransaksi = StatusTransaksi.SELESAI,
    val alasanBatal: String? = null,
    val waktu: Long,
)

/** Item transaksi dengan snapshot nama, harga jual, dan HPP saat dijual. Append-only. */
@Entity(
    tableName = "item_transaksi",
    foreignKeys = [
        ForeignKey(
            entity = TransaksiPenjualanEntity::class,
            parentColumns = ["id"],
            childColumns = ["transaksiId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("transaksiId"), Index("barangId")],
)
data class ItemTransaksiEntity(
    @PrimaryKey val id: String,
    val transaksiId: String,
    val barangId: String,
    val namaBarangSnapshot: String,
    val jumlah: Long,
    val hargaJualSnapshot: Long,
    val hppSnapshot: Long,
)
