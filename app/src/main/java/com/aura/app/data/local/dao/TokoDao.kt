package com.aura.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aura.app.data.local.entity.TokoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TokoDao {
    @Insert
    suspend fun insert(toko: TokoEntity)

    @Query("SELECT * FROM toko LIMIT 1")
    fun observe(): Flow<TokoEntity?>

    @Query("SELECT * FROM toko LIMIT 1")
    suspend fun get(): TokoEntity?

    /** Mengubah saldo kumulatif (K-22). Mengembalikan jumlah baris yang berubah (0 = toko belum ada). */
    @Query("UPDATE toko SET saldoKumulatif = saldoKumulatif + :selisih")
    suspend fun ubahSaldo(selisih: Long): Int
}
