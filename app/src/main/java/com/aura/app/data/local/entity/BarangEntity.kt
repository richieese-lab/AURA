package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.app.core.model.StatusBarang

/**
 * Object Barang (spec/08). `stokSaatIni` dan `hppRataRata` adalah nilai berjalan (K-22):
 * hanya boleh diubah repository bersamaan dengan pergerakan stoknya, tidak pernah diedit user.
 */
@Entity(
    tableName = "barang",
    indices = [Index("sku"), Index("status")],
)
data class BarangEntity(
    @PrimaryKey val id: String,
    val sku: String? = null,
    val fotoPath: String? = null,
    val nama: String,
    val satuan: String,
    val hargaJual: Long,
    val stokSaatIni: Long,
    val hppRataRata: Long,
    val stokMinimum: Long,
    val status: StatusBarang = StatusBarang.AKTIF,
    val dibuatOleh: String,
    val createdAt: Long,
)
