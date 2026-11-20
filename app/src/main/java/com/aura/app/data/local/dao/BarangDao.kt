package com.aura.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aura.app.core.model.StatusBarang
import com.aura.app.data.local.entity.BarangEntity
import com.aura.app.data.local.entity.BarangKategoriCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface BarangDao {
    @Insert
    suspend fun insert(barang: BarangEntity)

    @Query("SELECT * FROM barang WHERE id = :id")
    suspend fun getById(id: String): BarangEntity?

    @Query("SELECT * FROM barang WHERE id = :id")
    fun observeById(id: String): Flow<BarangEntity?>

    @Query(
        """
        SELECT * FROM barang
        WHERE (:termasukArsip = 1 OR status = 'AKTIF')
        ORDER BY nama COLLATE NOCASE
        """
    )
    fun observeSemua(termasukArsip: Boolean): Flow<List<BarangEntity>>

    /**
     * Barang aktif berstatus Menipis atau Habis, diurutkan paling mendesak:
     * Habis dulu, lalu rasio stok / stok minimum terkecil.
     */
    @Query(
        """
        SELECT * FROM barang
        WHERE status = 'AKTIF' AND (stokSaatIni <= 0 OR stokSaatIni <= stokMinimum)
        ORDER BY (stokSaatIni > 0),
                 CAST(stokSaatIni AS REAL) / (CASE WHEN stokMinimum > 0 THEN stokMinimum ELSE 1 END),
                 nama COLLATE NOCASE
        """
    )
    fun observeStokMenipis(): Flow<List<BarangEntity>>

    /** Edit Barang: stok & HPP sengaja tidak ikut diubah di sini. */
    @Query(
        """
        UPDATE barang SET nama = :nama, satuan = :satuan, hargaJual = :hargaJual,
            stokMinimum = :stokMinimum, sku = :sku, fotoPath = :fotoPath
        WHERE id = :id
        """
    )
    suspend fun updateInfo(
        id: String,
        nama: String,
        satuan: String,
        hargaJual: Long,
        stokMinimum: Long,
        sku: String?,
        fotoPath: String?,
    ): Int

    /** Hanya dipanggil repository bersamaan dengan insert pergerakan stok (K-22). */
    @Query("UPDATE barang SET stokSaatIni = :stok, hppRataRata = :hpp WHERE id = :id")
    suspend fun updateStokDanHpp(id: String, stok: Long, hpp: Long): Int

    @Query("UPDATE barang SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: StatusBarang): Int

    @Insert
    suspend fun insertKategori(relasi: List<BarangKategoriCrossRef>)

    @Query("DELETE FROM barang_kategori WHERE barangId = :barangId")
    suspend fun hapusSemuaKategori(barangId: String)

    @Query("SELECT kategoriId FROM barang_kategori WHERE barangId = :barangId")
    suspend fun getKategoriIds(barangId: String): List<String>
}
