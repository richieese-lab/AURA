# 10 — Rantai Efek Otomatis dan Kondisi Khusus

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** Efek "kuning/merah memicu notifikasi atau persetujuan" dan "di atas limit memerlukan persetujuan" **batal** (tanpa Manajer). **Wajib online:** Kondisi Khusus #2 (Kasir offline) dan bagian "perlu ditinjau" di #1 **batal**. Ditambah efek batas 5.000 entri. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 10 dan 12. Setiap aksi tulis di data layer WAJIB menjalankan seluruh efeknya **dalam satu transaksi database** (lihat [../02-arsitektur-dan-konvensi-kode.md](../02-arsitektur-dan-konvensi-kode.md)).

## Rantai Efek Otomatis

Apa yang terjadi di balik setiap aksi pengguna, agar stok, keuangan, notifikasi, dan riwayat selalu konsisten:

| # | Aksi | Efek otomatis |
|---|---|---|
| 1 | **Penjualan selesai (Kasir)** | Pergerakan **Keluar - Penjualan**; Pemasukan Penjualan (turunan); simpan **snapshot harga dan HPP**; hitung ulang status (stok menipis atau habis); masuk Riwayat |
| 2 | **Pembatalan penjualan** | **Masuk - Pembatalan**; transaksi berstatus Dibatalkan dan keluar dari Omzet; hitung ulang status; masuk Riwayat beserta alasan |
| 3 | **Restock** | **Masuk - Restock**; Pengeluaran Pembelian otomatis; update harga beli; hitung ulang HPP; barang keluar dari Stok Menipis bila stok melebihi minimum; masuk Riwayat |
| 4 | **Penyesuaian stok** | Pergerakan **Penyesuaian** (selisih); **tidak ada arus kas**, nilai selisih dilaporkan di Analisis; kuning/merah memicu notifikasi atau persetujuan |
| 5 | **Tambah barang (stok awal)** | **Masuk - Stok awal**; **tidak ada pengeluaran** |
| 6 | **Pengeluaran/pemasukan kustom** | Masuk ke Saldo; pengeluaran kustom masuk Biaya operasional; di atas limit memerlukan persetujuan |
| 7 | **Ubah harga jual/beli** | Hanya berlaku untuk transaksi berikutnya karena snapshot menjaga masa lalu; kuning memberi notifikasi ke Owner |
| 8 | **Arsipkan barang** | Hilang dari Kasir dan kartu Dashboard; riwayat tetap |

## Kondisi Khusus

1. **Dua kasir menjual barang terakhir bersamaan:** validasi stok di sisi data saat transaksi disimpan; yang kedua mendapat dialog "stok tersisa 0". Bila terjadi karena sinkronisasi offline, transaksi ditandai **"perlu ditinjau"**.
2. **Tidak ada internet saat berjualan:** Kasir tetap berfungsi dari cache dengan banner **"Offline"**; transaksi masuk antrean sinkron. Aksi yang butuh persetujuan Owner ditunda.
3. **Pegawai dinonaktifkan saat sedang login:** sesi berakhir pada sinkronisasi berikutnya dengan pesan jelas.
4. **Harga jual diubah saat barang sudah di keranjang:** harga keranjang dikunci, harga baru berlaku untuk keranjang berikutnya.
5. **Barang diarsipkan saat ada di keranjang:** tetap bisa diselesaikan untuk transaksi itu, tidak muncul lagi di daftar Kasir.
6. **Gambar QRIS belum diunggah atau Owner menghapusnya:** opsi QRIS tetap tercatat sebagai metode, layar menampilkan pesan bahwa gambar belum diatur.
7. **Pembatalan transaksi lama setelah restock banyak:** pembatalan mengembalikan stok ke saat ini; **HPP tidak dihitung ulang mundur** (batasan versi ini).
8. **Ganti perangkat atau kehilangan ponsel:** data aman di cloud; Owner dapat mengeluarkan semua sesi; login ulang dengan akun yang sama.
9. **Pengguna baru dengan data nol:** layar kosong yang ramah dengan daftar periksa mulai cepat; semua fitur ML menampilkan "Data belum cukup".

> **Catatan keputusan (2026-11-20):** user menyatakan "app harus tersambung internet". Apakah ini menghapus mode offline Kasir (Kondisi Khusus #2) dan penanda "perlu ditinjau" (#1) masih **menunggu klarifikasi K-20** di [../00-keputusan-proyek.md](../00-keputusan-proyek.md). Sampai dijawab, isi di atas tetap dianggap spesifikasi.
