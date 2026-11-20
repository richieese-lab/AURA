package com.aura.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "kategori",
    indices = [Index(value = ["nama"], unique = true)],
)
data class KategoriEntity(
    @PrimaryKey val id: String,
    /** Unik tanpa membedakan huruf besar/kecil ("Makanan" = "makanan"). */
    @ColumnInfo(collate = ColumnInfo.NOCASE) val nama: String,
)

/**
 * Relasi banyak-ke-banyak Barang–Kategori. Menghapus kategori hanya melepas relasinya;
 * barang tidak ikut terhapus (spec/06 Kelola Kategori).
 */
@Entity(
    tableName = "barang_kategori",
    primaryKeys = ["barangId", "kategoriId"],
    foreignKeys = [
        ForeignKey(
            entity = BarangEntity::class,
            parentColumns = ["id"],
            childColumns = ["barangId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = KategoriEntity::class,
            parentColumns = ["id"],
            childColumns = ["kategoriId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("kategoriId")],
)
data class BarangKategoriCrossRef(
    val barangId: String,
    val kategoriId: String,
)
