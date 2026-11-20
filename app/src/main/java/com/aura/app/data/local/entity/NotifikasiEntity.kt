package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.app.core.model.JenisNotifikasi

@Entity(
    tableName = "notifikasi",
    indices = [Index("waktu"), Index("dibaca")],
)
data class NotifikasiEntity(
    @PrimaryKey val id: String,
    val jenis: JenisNotifikasi,
    val judul: String,
    val isi: String,
    val referensiId: String? = null,
    val untukAkunId: String? = null,
    val dibaca: Boolean = false,
    val waktu: Long,
)
