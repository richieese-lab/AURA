package com.aura.app.data.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aura.app.core.model.JenisPergerakan
import com.aura.app.core.model.StatusBarang
import com.aura.app.core.model.SumberPergerakan
import com.aura.app.data.local.entity.BarangEntity
import com.aura.app.data.local.entity.BarangKategoriCrossRef
import com.aura.app.data.local.entity.HargaBeliSupplierEntity
import com.aura.app.data.local.entity.KategoriEntity
import com.aura.app.data.local.entity.PergerakanStokEntity
import com.aura.app.data.local.entity.SupplierEntity
import com.aura.app.data.local.entity.TokoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** Test DAO dengan database in-memory. Butuh emulator/perangkat: `connectedDebugAndroidTest`. */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun barang(
        id: String,
        nama: String,
        stok: Long,
        minimum: Long,
        status: StatusBarang = StatusBarang.AKTIF,
    ) = BarangEntity(
        id = id, nama = nama, satuan = "pcs", hargaJual = 5_000,
        stokSaatIni = stok, hppRataRata = 3_000, stokMinimum = minimum,
        status = status, dibuatOleh = "owner-1", createdAt = 0,
    )

    @Test
    fun stok_menipis_hanya_aktif_dan_urut_paling_mendesak() = runBlocking {
        val dao = db.barangDao()
        dao.insert(barang("a", "Aman", stok = 50, minimum = 10))
        dao.insert(barang("b", "Menipis Separuh", stok = 5, minimum = 10))
        dao.insert(barang("c", "Habis", stok = 0, minimum = 10))
        dao.insert(barang("d", "Menipis Parah", stok = 1, minimum = 10))
        dao.insert(barang("e", "Arsip Habis", stok = 0, minimum = 10, status = StatusBarang.DIARSIPKAN))
        dao.insert(barang("f", "Minimum Nol", stok = 3, minimum = 0))

        val hasil = dao.observeStokMenipis().first().map { it.id }

        assertEquals(listOf("c", "d", "b"), hasil)
    }

    @Test
    fun daftar_barang_menyembunyikan_arsip_kecuali_diminta() = runBlocking {
        val dao = db.barangDao()
        dao.insert(barang("a", "Kopi", stok = 5, minimum = 1))
        dao.insert(barang("b", "Gula", stok = 5, minimum = 1, status = StatusBarang.DIARSIPKAN))

        assertEquals(listOf("a"), dao.observeSemua(termasukArsip = false).first().map { it.id })
        assertEquals(2, dao.observeSemua(termasukArsip = true).first().size)
    }

    @Test
    fun edit_info_tidak_mengubah_stok_dan_hpp() = runBlocking {
        val dao = db.barangDao()
        dao.insert(barang("a", "Kopi", stok = 7, minimum = 2))

        dao.updateInfo("a", "Kopi ABC", "pcs", 6_000, 3, sku = "123", fotoPath = null)

        val hasil = dao.getById("a")!!
        assertEquals("Kopi ABC", hasil.nama)
        assertEquals(7L, hasil.stokSaatIni)
        assertEquals(3_000L, hasil.hppRataRata)
    }

    @Test
    fun hapus_kategori_melepas_relasi_tanpa_menghapus_barang() = runBlocking {
        db.barangDao().insert(barang("a", "Kopi", stok = 5, minimum = 1))
        db.kategoriDao().insert(KategoriEntity("k1", "Minuman"))
        db.barangDao().insertKategori(listOf(BarangKategoriCrossRef("a", "k1")))
        assertEquals(1, db.kategoriDao().jumlahBarang("k1"))

        db.kategoriDao().hapus("k1")

        assertNotNull(db.barangDao().getById("a"))
        assertTrue(db.barangDao().getKategoriIds("a").isEmpty())
    }

    @Test(expected = SQLiteConstraintException::class)
    fun nama_kategori_unik_tanpa_beda_huruf_besar_kecil() = runBlocking {
        db.kategoriDao().insert(KategoriEntity("k1", "Makanan"))
        db.kategoriDao().insert(KategoriEntity("k2", "makanan"))
    }

    @Test
    fun supplier_pernah_dipakai_bila_punya_harga_beli() = runBlocking {
        db.barangDao().insert(barang("a", "Kopi", stok = 5, minimum = 1))
        db.supplierDao().insert(SupplierEntity("s1", "CV Kopi", link = "https://wa.me/62811", createdAt = 0))
        db.supplierDao().insert(SupplierEntity("s2", "Toko Gula", createdAt = 0))

        db.supplierDao().upsertHargaBeli(HargaBeliSupplierEntity("a", "s1", 3_000, tanggal = 1))
        db.supplierDao().upsertHargaBeli(HargaBeliSupplierEntity("a", "s1", 3_200, tanggal = 2))

        assertTrue(db.supplierDao().pernahDipakai("s1"))
        assertFalse(db.supplierDao().pernahDipakai("s2"))
        val harga = db.supplierDao().observeHargaBeliBarang("a").first()
        assertEquals(1, harga.size)
        assertEquals(3_200L, harga.single().hargaPerItem)
        val jumlah = db.supplierDao().observeDenganJumlah().first().associate { it.id to it.jumlahBarang }
        assertEquals(mapOf("s1" to 1, "s2" to 0), jumlah)
    }

    @Test
    fun saldo_kumulatif_berubah_dan_nol_baris_bila_toko_belum_ada() = runBlocking {
        assertEquals(0, db.tokoDao().ubahSaldo(-10_000))

        db.tokoDao().insert(
            TokoEntity(idToko = "AB12CD34", nama = "Toko Ani", saldoAwal = 100_000, saldoKumulatif = 100_000, createdAt = 0)
        )
        assertEquals(1, db.tokoDao().ubahSaldo(-30_000))

        assertEquals(70_000L, db.tokoDao().get()!!.saldoKumulatif)
    }

    @Test
    fun kartu_stok_dan_filter_jenis_pergerakan() = runBlocking {
        val dao = db.pergerakanStokDao()
        fun gerak(id: String, barang: String, sumber: SumberPergerakan, jumlah: Long, waktu: Long) =
            PergerakanStokEntity(
                id = id, barangId = barang, jenis = sumber.jenis, sumber = sumber, jumlah = jumlah,
                stokSebelum = 0, stokSesudah = 0, akunId = "owner-1", waktu = waktu,
            )
        dao.insert(gerak("p1", "a", SumberPergerakan.STOK_AWAL, 10, waktu = 100))
        dao.insert(gerak("p2", "a", SumberPergerakan.PENJUALAN, -2, waktu = 200))
        dao.insert(gerak("p3", "b", SumberPergerakan.RESTOCK, 5, waktu = 300))

        assertEquals(listOf("p2", "p1"), dao.observeKartuStok("a").first().map { it.id })
        assertEquals("p2", dao.terakhir("a")!!.id)
        assertEquals(
            listOf("p3", "p1"),
            dao.observeRentang(0, 1_000, JenisPergerakan.MASUK).first().map { it.id },
        )
        assertEquals(3, dao.observeRentang(0, 1_000, null).first().size)
        assertEquals(listOf("p2"), dao.observeRentang(150, 250, null).first().map { it.id })
    }
}
