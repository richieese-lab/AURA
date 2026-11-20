package com.aura.app.data.local.entity

import com.aura.app.core.logic.StatusStokEvaluator
import com.aura.app.core.model.Barang
import com.aura.app.core.model.Kategori
import com.aura.app.core.model.LogAktivitas
import com.aura.app.core.model.PergerakanStok
import com.aura.app.core.model.StatusBarang
import com.aura.app.core.model.Supplier
import com.aura.app.data.local.dao.KategoriDenganJumlah
import com.aura.app.data.local.dao.SupplierDenganJumlah

/** Mapper Entity (Room) → model domain (pola `toStudent()` di Modul 9). */

fun BarangEntity.toDomain() = Barang(
    id = id,
    sku = sku,
    fotoPath = fotoPath,
    nama = nama,
    satuan = satuan,
    hargaJual = hargaJual,
    stokSaatIni = stokSaatIni,
    hppRataRata = hppRataRata,
    stokMinimum = stokMinimum,
    status = status,
    statusStok = StatusStokEvaluator.evaluasi(
        stok = stokSaatIni,
        stokMinimum = stokMinimum,
        diarsipkan = status == StatusBarang.DIARSIPKAN,
    ),
    dibuatOleh = dibuatOleh,
    createdAt = createdAt,
)

fun KategoriDenganJumlah.toDomain() = Kategori(
    id = id,
    nama = nama,
    jumlahBarang = jumlahBarang,
)

fun SupplierDenganJumlah.toDomain() = Supplier(
    id = id,
    nama = nama,
    link = link,
    catatan = catatan,
    status = status,
    jumlahBarang = jumlahBarang,
)

fun PergerakanStokEntity.toDomain() = PergerakanStok(
    id = id,
    barangId = barangId,
    jenis = jenis,
    sumber = sumber,
    jumlah = jumlah,
    stokSebelum = stokSebelum,
    stokSesudah = stokSesudah,
    alasan = alasan,
    catatan = catatan,
    akunId = akunId,
    waktu = waktu,
)

fun LogAktivitasEntity.toDomain() = LogAktivitas(
    id = id,
    akunId = akunId,
    namaAkun = namaAkunSnapshot,
    peran = peranSnapshot,
    kategori = kategori,
    ringkasan = ringkasan,
    nilaiSebelum = nilaiSebelum,
    nilaiSesudah = nilaiSesudah,
    waktu = waktu,
)
