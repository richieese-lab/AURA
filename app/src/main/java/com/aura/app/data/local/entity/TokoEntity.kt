package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Satu Owner per toko. `saldoKumulatif` adalah nilai berjalan (K-22). */
@Entity(tableName = "toko")
data class TokoEntity(
    @PrimaryKey val idToko: String,
    val nama: String,
    val bidangBisnis: String? = null,
    val alamat: String? = null,
    val logoPath: String? = null,
    val saldoAwal: Long,
    val saldoKumulatif: Long,
    val qrisPath: String? = null,
    val namaPenerima: String? = null,
    val infoRekening: String? = null,
    val createdAt: Long,
)
