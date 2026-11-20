package com.aura.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.app.core.model.AlasanPenyesuaian
import com.aura.app.core.model.JenisCatatanTerbatas
import com.aura.app.core.model.JenisPergerakan
import com.aura.app.core.model.SumberPergerakan

/**
 * Buku besar pergerakan stok. **Append-only**: hanya insert & select.
 * `jumlah` bertanda (masuk positif, keluar negatif). `referensiId` + `jenisReferensi` menunjuk
 * catatan induk (mis. pengeluaran Pembelian dari Restock) agar keputusan retensi K-23 tetap bisa diterapkan.
 *
 * Sengaja tanpa foreign key ke `barang`: riwayat harus tetap ada walau barang diarsipkan.
 */
@Entity(
    tableName = "pergerakan_stok",
    indices = [Index("barangId"), Index("waktu"), Index("referensiId")],
)
data class PergerakanStokEntity(
    @PrimaryKey val id: String,
    val barangId: String,
    val jenis: JenisPergerakan,
    val sumber: SumberPergerakan,
    val jumlah: Long,
    val stokSebelum: Long,
    val stokSesudah: Long,
    val referensiId: String? = null,
    val jenisReferensi: JenisCatatanTerbatas? = null,
    val alasan: AlasanPenyesuaian? = null,
    val catatan: String? = null,
    val buktiPath: String? = null,
    val akunId: String,
    val waktu: Long,
)
