package com.aura.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aura.app.data.local.entity.ItemTransaksiEntity
import com.aura.app.data.local.entity.PemasukanKustomEntity
import com.aura.app.data.local.entity.PengeluaranEntity
import com.aura.app.data.local.entity.TransaksiPenjualanEntity
import kotlinx.coroutines.flow.Flow

/** Tidak ada @Delete: data keuangan & transaksi hanya dibatalkan (alur penuh di Tahap 1). */
@Dao
interface PengeluaranDao {
    @Insert
    suspend fun insert(pengeluaran: PengeluaranEntity)

    @Query("SELECT * FROM pengeluaran WHERE id = :id")
    suspend fun getById(id: String): PengeluaranEntity?

    @Query("SELECT * FROM pengeluaran WHERE tanggal BETWEEN :dari AND :sampai ORDER BY tanggal DESC")
    fun observeRentang(dari: Long, sampai: Long): Flow<List<PengeluaranEntity>>
}

@Dao
interface PemasukanKustomDao {
    @Insert
    suspend fun insert(pemasukan: PemasukanKustomEntity)

    @Query("SELECT * FROM pemasukan_kustom WHERE tanggal BETWEEN :dari AND :sampai ORDER BY tanggal DESC")
    fun observeRentang(dari: Long, sampai: Long): Flow<List<PemasukanKustomEntity>>
}

@Dao
interface TransaksiPenjualanDao {
    @Insert
    suspend fun insert(transaksi: TransaksiPenjualanEntity)

    @Insert
    suspend fun insertItem(items: List<ItemTransaksiEntity>)

    @Query("SELECT * FROM transaksi_penjualan WHERE waktu BETWEEN :dari AND :sampai ORDER BY waktu DESC")
    fun observeRentang(dari: Long, sampai: Long): Flow<List<TransaksiPenjualanEntity>>

    @Query("SELECT * FROM item_transaksi WHERE transaksiId = :transaksiId")
    suspend fun getItem(transaksiId: String): List<ItemTransaksiEntity>
}
