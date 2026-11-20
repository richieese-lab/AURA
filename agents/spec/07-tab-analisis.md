# 07 — Tab 4: Analisis

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** Analisis **hanya Owner**; Ringkasan tambah kartu **Kerugian Penyesuaian**; sisa hari stok "Tidak dapat dihitung" bila rata-rata 0; banner rentang data (batas 5.000 entri); Omzet tanpa diskon. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 7, "Tab 4 - Analisis".

Tab ini memberikan insight berupa hasil analisis dengan machine learning. Di sini juga ada fitur slow moving dan fast moving yang ada juga di Dashboard. Aslinya kedua fitur ini bagian dari analisis, namun untuk memudahkan akses diletakkan juga di Dashboard **dengan perhitungan dan komponen yang sama**. Seluruh analisis dihitung dari data yang sudah dicatat user dan bersifat **informatif**; aplikasi tidak mengubah harga, stok, atau melakukan tindakan otomatis.

## Layer 1 — Komponen

1. **FilterPeriode** — komponen yang sama dengan Pengeluaran/Pemasukan, di atas empat sub-tab.
2. **Sub-tab Ringkasan** — kartu: Omzet, Laba kotor, Laba bersih, Jumlah transaksi, Rata-rata nilai transaksi, masing-masing dengan perubahan persen dibanding periode sebelumnya. Grafik garis tren penjualan.
3. **Sub-tab Penjualan** — tren per hari/minggu/bulan; barang terlaris Top 10 menurut unit dan omzet; Kontribusi Penjualan per kategori dan per barang beserta klasifikasi ABC; jam dan hari tersibuk; margin per barang.
4. **Sub-tab Stok** — Fast/Slow Moving rinci dengan periode dan ambang dapat diubah; valuasi gudang (total, per barang, persentase, kumulatif); volume pergerakan (jumlah barang masuk vs keluar) dengan tautan ke Pergerakan Stok; modal tertahan di Slow Moving; sisa hari stok.
5. **Sub-tab Prediksi** — Kartu Insight hasil machine learning, berbahasa biasa dengan tombol aksi, mis. *"Kopi ABC diperkirakan habis dalam 4 hari. Saran jumlah restock: 40 pcs. [Restock]"*. Saran bersifat perkiraan; jumlah dan harga yang dicatat tetap diputuskan user. Sebelum data cukup tampil **"Data belum cukup (butuh N minggu lagi)"** dan label **"Diperbarui: ..."**.

## Definisi Metrik

1. **Omzet (bruto)** = jumlah total transaksi penjualan berstatus **Selesai** setelah diskon.
2. **HPP** = Σ (terjual × HPP barang saat dijual). HPP dicatat sebagai **snapshot pada setiap item transaksi**, supaya laba masa lalu tidak berubah ketika harga beli berubah.
3. **HPP per barang** = rata-rata bergerak dari harga beli per item (total dibayar / jumlah) yang diinput user setiap Restock.
4. **Laba kotor** = Omzet − HPP. **Biaya operasional** = jumlah pengeluaran jenis **Kustom** (Pembelian tidak dihitung agar tidak terhitung dua kali). **Laba bersih (netto)** = Laba kotor − Biaya operasional.
5. **Saldo** = saldo awal + seluruh pemasukan − seluruh pengeluaran (**termasuk** Pembelian). Berbasis arus kas sehingga Saldo tidak sama dengan Laba; ada keterangan kecil di UI.
6. **Valuasi gudang** = Σ (stok × HPP). Persentase per barang = nilai barang / total valuasi.
7. **Kontribusi barang** = omzet barang / total omzet. **ABC:** A = barang yang bersama-sama menyumbang kira-kira 80% omzet, B = 15% berikutnya, C = sisanya.
8. **Sisa hari stok** = stok / rata-rata penjualan harian.

## Aturan Fast/Slow Moving
(Dapat diubah Owner di Pengaturan Analisis.)

- Periode bawaan **30 hari**.
- **Kecepatan jual** = unit terjual / jumlah hari barang tersedia (stok > 0).
- **Fast Moving** = 20% teratas menurut kecepatan jual **dengan minimal 5 unit terjual**.
- **Slow Moving** = 20% terbawah **atau** terjual kurang dari 3 unit.
- **Tidak laku** = 0 penjualan (tetap di Slow Moving dengan penanda khusus).
- **Normal** = di antaranya (tidak tampil di kartu Dashboard).
- **Baru** = barang berusia kurang dari 14 hari, dikecualikan.

## Instrumen Machine Learning yang Disarankan
(Dihitung dari data catatan penjualan dan pergerakan stok, dengan syarat data minimum.)

| # | Instrumen | Metode | Data minimum | Prioritas |
|---|---|---|---|---|
| 1 | Prediksi stok habis dan saran jumlah restock | Peramalan permintaan per barang (rata-rata bergerak / Holt-Winters) → sisa hari stok. Titik pesan ulang = rata-rata harian × lead time + stok pengaman | 8 minggu | Tinggi |
| 2 | Deteksi tren (naik daun) | Rasio rata-rata 7 hari terhadap 28 hari dan uji kemiringan tren | 4 minggu | Tinggi |
| 3 | Deteksi anomali | z-score/IQR terhadap penjualan, penyesuaian stok, dan pengeluaran yang janggal; memicu notifikasi ke Owner | 4 minggu | Tinggi |
| 4 | Deteksi musiman [Beta] | Indeks musiman per bulan dan minggu dengan fitur kalender hari besar (Ramadan, Lebaran, Natal, tahun ajaran baru, gajian). Satu tahun data hanya menyebut barang sebagai "kandidat musiman"; polanya dipastikan setelah dua tahun | 12 bulan | — |
| 5 | Analisis keranjang [tahap lanjut] | Barang yang sering dibeli bersamaan (Apriori/FP-Growth) | 500 transaksi | — |
| 6 | Saran stok minimum [tahap lanjut] | Dari rata-rata jual dan lead time supplier; hanya saran, user yang menetapkan angka stok minimum | — | — |
| 7 | Klasifikasi ABC, jam & hari tersibuk, modal tertahan | **Bukan ML**, melainkan agregasi/aturan; tersedia sejak hari pertama | — | — |

Seluruh model dijalankan **di perangkat** dengan metode sederhana (Kotlin) atau sebagai proses terjadwal, lalu hasilnya disimpan dan ditampilkan dengan keterangan waktu pembaruan dan label **"Perkiraan"**.
