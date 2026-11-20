package com.aura.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aura.app.data.local.dao.AkunDao
import com.aura.app.data.local.dao.BarangDao
import com.aura.app.data.local.dao.KategoriDao
import com.aura.app.data.local.dao.LogAktivitasDao
import com.aura.app.data.local.dao.NotifikasiDao
import com.aura.app.data.local.dao.PemasukanKustomDao
import com.aura.app.data.local.dao.PengeluaranDao
import com.aura.app.data.local.dao.PergerakanStokDao
import com.aura.app.data.local.dao.SupplierDao
import com.aura.app.data.local.dao.TokoDao
import com.aura.app.data.local.dao.TransaksiPenjualanDao
import com.aura.app.data.local.entity.AkunEntity
import com.aura.app.data.local.entity.BarangEntity
import com.aura.app.data.local.entity.BarangKategoriCrossRef
import com.aura.app.data.local.entity.HargaBeliSupplierEntity
import com.aura.app.data.local.entity.ItemTransaksiEntity
import com.aura.app.data.local.entity.KategoriEntity
import com.aura.app.data.local.entity.LogAktivitasEntity
import com.aura.app.data.local.entity.NotifikasiEntity
import com.aura.app.data.local.entity.PemasukanKustomEntity
import com.aura.app.data.local.entity.PengeluaranEntity
import com.aura.app.data.local.entity.PergerakanStokEntity
import com.aura.app.data.local.entity.SupplierEntity
import com.aura.app.data.local.entity.TokoEntity
import com.aura.app.data.local.entity.TransaksiPenjualanEntity

@Database(
    entities = [
        TokoEntity::class,
        AkunEntity::class,
        BarangEntity::class,
        KategoriEntity::class,
        BarangKategoriCrossRef::class,
        SupplierEntity::class,
        HargaBeliSupplierEntity::class,
        PergerakanStokEntity::class,
        TransaksiPenjualanEntity::class,
        ItemTransaksiEntity::class,
        PengeluaranEntity::class,
        PemasukanKustomEntity::class,
        NotifikasiEntity::class,
        LogAktivitasEntity::class,
    ],
    version = 1,
    exportSchema = false, // K-09: diganti true + migrasi sebelum demo dengan data asli
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tokoDao(): TokoDao
    abstract fun akunDao(): AkunDao
    abstract fun barangDao(): BarangDao
    abstract fun kategoriDao(): KategoriDao
    abstract fun supplierDao(): SupplierDao
    abstract fun pergerakanStokDao(): PergerakanStokDao
    abstract fun transaksiPenjualanDao(): TransaksiPenjualanDao
    abstract fun pengeluaranDao(): PengeluaranDao
    abstract fun pemasukanKustomDao(): PemasukanKustomDao
    abstract fun notifikasiDao(): NotifikasiDao
    abstract fun logAktivitasDao(): LogAktivitasDao
}

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "aura.db",
            )
                // K-09: HANYA selama pengembangan. Sebelum demo dengan data asli diganti migrasi.
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                .also { INSTANCE = it }
        }
}
