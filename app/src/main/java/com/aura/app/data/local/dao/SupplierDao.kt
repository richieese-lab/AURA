package com.aura.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.aura.app.core.model.StatusSupplier
import com.aura.app.data.local.entity.HargaBeliSupplierEntity
import com.aura.app.data.local.entity.SupplierEntity
import kotlinx.coroutines.flow.Flow

data class SupplierDenganJumlah(
    val id: String,
    val nama: String,
    val link: String?,
    val catatan: String?,
    val status: StatusSupplier,
    val jumlahBarang: Int,
)

@Dao
interface SupplierDao {
    @Insert
    suspend fun insert(supplier: SupplierEntity)

    @Query("SELECT * FROM supplier WHERE id = :id")
    suspend fun getById(id: String): SupplierEntity?

    @Query(
        """
        SELECT s.id, s.nama, s.link, s.catatan, s.status, COUNT(h.barangId) AS jumlahBarang
        FROM supplier s LEFT JOIN harga_beli_supplier h ON h.supplierId = s.id
        GROUP BY s.id ORDER BY s.nama COLLATE NOCASE
        """
    )
    fun observeDenganJumlah(): Flow<List<SupplierDenganJumlah>>

    @Query("UPDATE supplier SET nama = :nama, link = :link, catatan = :catatan WHERE id = :id")
    suspend fun updateInfo(id: String, nama: String, link: String?, catatan: String?): Int

    @Query("UPDATE supplier SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: StatusSupplier): Int

    /** Supplier dianggap pernah dipakai bila tercatat di pengeluaran atau punya harga beli barang. */
    @Query(
        """
        SELECT EXISTS(SELECT 1 FROM pengeluaran WHERE supplierId = :id)
            OR EXISTS(SELECT 1 FROM harga_beli_supplier WHERE supplierId = :id)
        """
    )
    suspend fun pernahDipakai(id: String): Boolean

    @Query("DELETE FROM supplier WHERE id = :id")
    suspend fun hapus(id: String): Int

    @Upsert
    suspend fun upsertHargaBeli(harga: HargaBeliSupplierEntity)

    @Query("SELECT * FROM harga_beli_supplier WHERE barangId = :barangId ORDER BY tanggal DESC")
    fun observeHargaBeliBarang(barangId: String): Flow<List<HargaBeliSupplierEntity>>
}
