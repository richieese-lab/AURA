package com.aura.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aura.app.core.model.JenisPergerakan
import com.aura.app.data.local.entity.PergerakanStokEntity
import kotlinx.coroutines.flow.Flow

/** Append-only: hanya insert & select (tidak ada update/delete). */
@Dao
interface PergerakanStokDao {
    @Insert
    suspend fun insert(pergerakan: PergerakanStokEntity)

    /** Kartu Stok: riwayat pergerakan satu barang, terbaru di atas. */
    @Query("SELECT * FROM pergerakan_stok WHERE barangId = :barangId ORDER BY waktu DESC")
    fun observeKartuStok(barangId: String): Flow<List<PergerakanStokEntity>>

    /** Daftar global dengan FilterPeriode dan filter jenis (null = semua jenis). */
    @Query(
        """
        SELECT * FROM pergerakan_stok
        WHERE waktu BETWEEN :dari AND :sampai AND (:jenis IS NULL OR jenis = :jenis)
        ORDER BY waktu DESC
        """
    )
    fun observeRentang(dari: Long, sampai: Long, jenis: JenisPergerakan?): Flow<List<PergerakanStokEntity>>

    @Query("SELECT * FROM pergerakan_stok WHERE barangId = :barangId ORDER BY waktu DESC LIMIT 1")
    suspend fun terakhir(barangId: String): PergerakanStokEntity?
}
