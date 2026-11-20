package com.aura.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aura.app.data.local.entity.KategoriEntity
import kotlinx.coroutines.flow.Flow

data class KategoriDenganJumlah(
    val id: String,
    val nama: String,
    val jumlahBarang: Int,
)

@Dao
interface KategoriDao {
    @Insert
    suspend fun insert(kategori: KategoriEntity)

    @Query("SELECT * FROM kategori WHERE id = :id")
    suspend fun getById(id: String): KategoriEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM kategori WHERE nama = :nama AND id != :kecualiId)")
    suspend fun namaSudahAda(nama: String, kecualiId: String = ""): Boolean

    @Query(
        """
        SELECT k.id, k.nama, COUNT(bk.barangId) AS jumlahBarang
        FROM kategori k LEFT JOIN barang_kategori bk ON bk.kategoriId = k.id
        GROUP BY k.id ORDER BY k.nama
        """
    )
    fun observeDenganJumlah(): Flow<List<KategoriDenganJumlah>>

    @Query("SELECT COUNT(*) FROM barang_kategori WHERE kategoriId = :kategoriId")
    suspend fun jumlahBarang(kategoriId: String): Int

    @Query("UPDATE kategori SET nama = :nama WHERE id = :id")
    suspend fun ubahNama(id: String, nama: String): Int

    /** Relasi barang_kategori ikut terhapus (CASCADE); barang tetap ada. */
    @Query("DELETE FROM kategori WHERE id = :id")
    suspend fun hapus(id: String): Int
}
