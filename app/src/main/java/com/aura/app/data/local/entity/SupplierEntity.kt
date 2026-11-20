package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.app.core.model.StatusSupplier

@Entity(tableName = "supplier")
data class SupplierEntity(
    @PrimaryKey val id: String,
    val nama: String,
    val link: String? = null,
    val catatan: String? = null,
    val status: StatusSupplier = StatusSupplier.AKTIF,
    val createdAt: Long,
)

/**
 * Harga beli terakhir sebuah Barang dari sebuah Supplier. Satu barang boleh punya beberapa supplier.
 * Diperbarui otomatis setiap Restock (harga per item = total bayar / jumlah masuk).
 */
@Entity(
    tableName = "harga_beli_supplier",
    primaryKeys = ["barangId", "supplierId"],
    foreignKeys = [
        ForeignKey(
            entity = BarangEntity::class,
            parentColumns = ["id"],
            childColumns = ["barangId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SupplierEntity::class,
            parentColumns = ["id"],
            childColumns = ["supplierId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("supplierId")],
)
data class HargaBeliSupplierEntity(
    val barangId: String,
    val supplierId: String,
    val hargaPerItem: Long,
    val tanggal: Long,
)
