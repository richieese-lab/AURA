package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.app.core.model.Peran
import com.aura.app.core.model.StatusAkun

/**
 * Akun Owner / Kasir. Nama akun unik per toko; penghapusan lunak (`isDihapus`).
 * Kolom kata sandi (hash) ditambahkan di Tahap 1 bersama autentikasi lokal sementara (K-07).
 */
@Entity(
    tableName = "akun",
    indices = [Index(value = ["idToko", "namaAkun"], unique = true)],
)
data class AkunEntity(
    @PrimaryKey val id: String,
    val idToko: String,
    val peran: Peran,
    val namaAkun: String,
    val namaTampil: String,
    val email: String? = null,
    val noHp: String? = null,
    val fotoPath: String? = null,
    val status: StatusAkun = StatusAkun.AKTIF,
    val isDihapus: Boolean = false,
    val wajibGantiSandi: Boolean = false,
    val lastLoginAt: Long? = null,
    val createdAt: Long,
)
