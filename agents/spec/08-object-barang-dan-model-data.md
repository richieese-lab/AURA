# 08 — Object Barang dan Model Data Utama

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** Model **PermintaanPersetujuan dihapus**; **batas 5.000 entri** per jenis catatan dengan penghapusan otomatis (K-22, K-23); Room hanya cache baca karena **wajib online** (K-20); Pengaturan Toko menambah alamat & logo. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> **Catatan keputusan K-22 (2026-11-20):** "Stok saat ini = jumlah seluruh pergerakan" diganti menjadi **stok, HPP rata-rata, dan saldo disimpan sebagai nilai berjalan** yang diperbarui setiap pencatatan (karena riwayat lama bisa terhapus otomatis). Stok tetap tidak bisa diedit langsung.

> Sumber: dokumen AURA bagian 8 (Object) dan 11 (Model Data Utama). Keputusan teknis penyimpanan ada di [../00-keputusan-proyek.md](../00-keputusan-proyek.md).

## Object Barang

**Komponen per layer:**
- **Layer 1** = Restock, nama, dan gambar Barang.
- **Layer 2** (kecuali tombol restock) = Tampilan Profil Barang (id, foto, nama, harga beli [bisa banyak berdasarkan supplier], harga jual, kategori [bisa lebih dari 1, dibuat dan ditambahkan oleh user], stok, stok minimum), Edit Barang, Restock.
- **Layer 3 (Edit Barang)** = edit foto, nama, harga beli [ketika ada restock dari supplier tersebut, harganya diupdate berdasarkan harga per item dari total yang dibayarkan ke supplier], harga jual, kategori.
- **Layer 3 (Restock)** = formulir restock dengan pilihan supplier dan tombol tambah supplier.

### Field lengkap Object Barang

| # | Field | Keterangan |
|---|---|---|
| 1 | **ID** | Otomatis, unik, tampil di Profil Barang |
| 2 | **SKU / Barcode** | Teks, opsional; untuk scan di Kasir dan Penyesuaian Stok |
| 3 | **Foto** | Gambar; thumbnail kecil di daftar dashboard, dikompres saat unggah |
| 4 | **Nama** | Teks, **wajib** |
| 5 | **Satuan** | pcs, kg, liter, dst.; tampil di samping angka stok |
| 6 | **Kategori** | Banyak-ke-banyak; dibuat dan dikelola pengguna; boleh lebih dari satu |
| 7 | **Harga jual** | Rupiah, **wajib**; ditentukan user; perubahan tercatat di Riwayat |
| 8 | **Harga beli per supplier** | Daftar: supplier, harga per item, tanggal; diperbarui otomatis tiap Restock = total bayar / jumlah masuk yang diinput user |
| 9 | **HPP rata-rata** | Otomatis, baca saja; dasar laba dan valuasi gudang |
| 10 | **Stok saat ini** | Otomatis = jumlah seluruh pergerakan stok, baca saja; **tidak dapat diedit langsung** |
| 11 | **Stok minimum** | Angka, **wajib**, diinput user; dasar peringatan stok. Jika diisi 0, peringatan hanya muncul saat stok = 0 |
| 12 | **Status** | Aktif / Diarsipkan |
| 13 | **Dibuat oleh dan tanggal dibuat** | Otomatis, untuk jejak audit |

## Model Data Utama

1. **Toko:** ID Toko, nama, saldo awal, gambar QRIS, info rekening. Satu Owner per toko.
2. **Akun:** peran, nama akun, status; nama akun unik per toko; penghapusan lunak.
3. **Barang, Kategori, Supplier, HargaBeliSupplier:** Barang beranggota banyak kategori dan banyak supplier.
4. **TransaksiPenjualan + ItemTransaksi:** nomor, kasir, metode bayar (label), total, status; per item: jumlah, harga jual, HPP saat itu. Transaksi tidak dihapus, hanya dibatalkan.
5. **PergerakanStok:** barang, jenis, sumber, jumlah, stok sebelum/sesudah, akun, waktu. **Append-only.**
6. **Pengeluaran (Pembelian, Kustom) dan Pemasukan Kustom:** nominal, jenis, supplier, bukti, tanggal, status. **Pemasukan Penjualan adalah tampilan turunan dari transaksi** (bukan tabel terpisah).
7. **Notifikasi, PermintaanPersetujuan, LogAktivitas** (append-only).

## Penyimpanan Data (dokumen asli)

Database lokal (**Room**) sebagai cache dan antrean offline, disinkronkan ke backend agar Owner dan pegawai di ponsel berbeda melihat data yang sama dan persetujuan Owner sampai ke ponselnya. Sinkronisasi hanya untuk data catatan; tidak ada pemrosesan pembayaran.

> **Pembaruan keputusan (2026-11-20):** dokumen asli menyebut backend "Firebase Auth + Firestore atau Supabase". Keputusan tim: **Firebase tidak dipakai**; backend = **server sendiri, cadangan Railway**, diakses lewat **Retrofit**. Lihat [../00-keputusan-proyek.md](../00-keputusan-proyek.md).
