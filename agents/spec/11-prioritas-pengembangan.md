# 11 — Prioritas Pengembangan

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** Tahap 2 "Persetujuan Manajer [tiga tingkat], PIN Owner" **dihapus**; RBAC dasar = Owner & Kasir. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 13. Ditambah "Tahap 0" yang disepakati 2026-11-20 (lihat [../plan/tahap-0-fondasi.md](../plan/tahap-0-fondasi.md)).

| Tahap | Isi |
|---|---|
| **Tahap 0 — Fondasi** *(disepakati, belum ada UI Figma)* | Setup proyek, tema, struktur paket, navigasi kerangka (bottom nav 5 posisi + aturan peran), skema Room lengkap, repository, logika bisnis inti (stok dari pergerakan, status stok, HPP, format Rp/tanggal) + unit test. UI hanya placeholder. |
| **Tahap 1 — MVP inti** | Registrasi/login Owner dan pegawai, RBAC dasar; Barang lengkap dengan stok dan stok minimum, Kategori, Supplier; Kasir (Tunai, Transfer, QRIS berupa gambar unggahan), Restock, Pergerakan Stok, Penyesuaian Stok; Dashboard; Notifikasi stok; Analisis Ringkasan, Penjualan, Stok (aturan sederhana); Riwayat Aktivitas. |
| **Tahap 2** | Persetujuan Manajer (tiga tingkat), PIN Owner; ML prediksi stok habis, tren, anomali; retur dan pembatalan transaksi lengkap; ekspor riwayat, struk Bluetooth, push notification. |
| **Tahap 3** | Musiman, analisis keranjang, saran stok minimum; restock banyak barang per nota; sesi opname massal, rekap shift Kasir; multi-toko. |

**Batas versi saat ini** (dari Batasan Aplikasi poin 6): satu barang per restock, satu toko per Owner, tanpa hutang/piutang.

*Dokumen asli: "Dokumen ini adalah rancangan yang masih dapat dikembangkan."*
