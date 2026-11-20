package com.aura.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aura.app.core.model.KategoriAktivitas
import com.aura.app.data.local.entity.LogAktivitasEntity
import com.aura.app.data.local.entity.NotifikasiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotifikasiDao {
    @Insert
    suspend fun insert(notifikasi: NotifikasiEntity)

    @Query("SELECT * FROM notifikasi ORDER BY waktu DESC")
    fun observeSemua(): Flow<List<NotifikasiEntity>>

    @Query("SELECT COUNT(*) FROM notifikasi WHERE dibaca = 0")
    fun observeJumlahBelumDibaca(): Flow<Int>

    @Query("UPDATE notifikasi SET dibaca = 1 WHERE dibaca = 0")
    suspend fun tandaiSemuaDibaca(): Int
}

/** Riwayat Aktivitas: append-only, hanya insert & select. */
@Dao
interface LogAktivitasDao {
    @Insert
    suspend fun insert(log: LogAktivitasEntity)

    @Query(
        """
        SELECT * FROM log_aktivitas
        WHERE waktu BETWEEN :dari AND :sampai
          AND (:kategori IS NULL OR kategori = :kategori)
          AND (:akunId IS NULL OR akunId = :akunId)
        ORDER BY waktu DESC
        """
    )
    fun observe(
        dari: Long,
        sampai: Long,
        kategori: KategoriAktivitas?,
        akunId: String?,
    ): Flow<List<LogAktivitasEntity>>
}
