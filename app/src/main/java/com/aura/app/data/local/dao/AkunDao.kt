package com.aura.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aura.app.data.local.entity.AkunEntity
import kotlinx.coroutines.flow.Flow

/** Alur akun lengkap (login, Manajemen Akun Kasir) dibuat di Tahap 1. */
@Dao
interface AkunDao {
    @Insert
    suspend fun insert(akun: AkunEntity)

    @Query("SELECT * FROM akun WHERE id = :id")
    suspend fun getById(id: String): AkunEntity?

    @Query("SELECT * FROM akun WHERE idToko = :idToko AND isDihapus = 0 ORDER BY namaAkun")
    fun observeByToko(idToko: String): Flow<List<AkunEntity>>
}
