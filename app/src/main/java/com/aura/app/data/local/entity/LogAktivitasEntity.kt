package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.app.core.model.KategoriAktivitas
import com.aura.app.core.model.Peran

/**
 * Riwayat Aktivitas (jejak audit). **Append-only**, tidak bisa diedit/dihapus siapa pun.
 * Nama & peran disimpan sebagai snapshot agar nama akun yang dihapus tetap tampil.
 */
@Entity(
    tableName = "log_aktivitas",
    indices = [Index("waktu"), Index("akunId"), Index("kategori"), Index("referensiId")],
)
data class LogAktivitasEntity(
    @PrimaryKey val id: String,
    val akunId: String,
    val namaAkunSnapshot: String,
    val peranSnapshot: Peran,
    val kategori: KategoriAktivitas,
    val ringkasan: String,
    val nilaiSebelum: String? = null,
    val nilaiSesudah: String? = null,
    val namaPerangkat: String? = null,
    val referensiId: String? = null,
    val waktu: Long,
)
