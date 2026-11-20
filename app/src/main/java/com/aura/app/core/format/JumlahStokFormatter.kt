package com.aura.app.core.format

/** Angka stok selalu bilangan bulat (K-06) dan disertai satuan: `12 pcs`, `25.000 gram`. */
object JumlahStokFormatter {

    fun format(jumlah: Long, satuan: String): String {
        val angka = AngkaFormatter.ribuan(jumlah)
        val teksSatuan = satuan.trim()
        return if (teksSatuan.isEmpty()) angka else "$angka $teksSatuan"
    }
}
