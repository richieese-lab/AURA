# 01 — Ringkasan Produk AURA

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** Sistem peran sekarang **Owner dan Kasir** (Manajer dihapus); "aksi kritis oleh Manajer menunggu persetujuan Owner" di Fungsi 5 batal. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: `AURA_Dokumen_Flow_UI_Revisi_2` bagian 1–5. Isi dipindahkan apa adanya (hanya dirapikan formatnya).

**AURA (Automated Updates & Retail Analytics)** — aplikasi mobile Android (Kotlin + Jetpack Compose) untuk pencatatan inventori UMKM retail.

## 1. Latar Belakang [ADK, RSH]

Banyak pelaku UMKM retail masih mengandalkan pencatatan stok manual menggunakan buku besar atau lembar kerja terpisah, yang belum mampu menyelesaikan masalah dasar seperti tingginya risiko kesalahan manusia (human error), ketidaksesuaian data fisik dengan catatan gudang, serta kejadian stok habis tak terduga (out of stock) pada produk terlaris yang berujung pada hilangnya potensi penjualan.

Untuk mengatasi berbagai kelemahan metode konvensional tersebut, dibutuhkan sistem manajemen inventori yang terdigitalisasi dan terstruktur guna meminimalkan ketidaksesuaian data stok serta mempercepat pemantauan pergerakan barang secara praktis. Selain itu, pelaku usaha memerlukan sebuah sistem terpadu yang memiliki fitur peringatan otomatis serta analitik bisnis yang dapat merangkum tren penjualan secara akurat guna mendukung pengambilan keputusan operasional yang lebih tepat.

Menjawab kebutuhan akan sistem cerdas tersebut, proyek pengembangan aplikasi AURA hadir sebagai solusi nyata yang sangat relevan dengan tantangan operasional UMKM saat ini. Aplikasi berbasis Android ini dirancang untuk mendigitalisasi pengelolaan inventori secara menyeluruh, mencakup fitur pengelolaan barang dan kategori, pengelolaan supplier, pencatatan pergerakan barang (inventory movement), hingga peringatan stok rendah otomatis yang secara langsung memitigasi risiko kehabisan stok. Lebih dari sekadar alat pencatatan, AURA juga dilengkapi dengan halaman analitik khusus yang menyajikan rekapitulasi penjualan, tren pasar, serta klasifikasi barang fast-moving dan slow-moving, sehingga dapat membantu pelaku UMKM memahami kondisi bisnis mereka secara mendalam untuk merancang strategi yang tepat sasaran.

## 2. Tujuan Proyek [ADK, RSH]

Tujuan utama dari proyek ini adalah menciptakan sebuah sistem manajemen inventori digital yang terstruktur untuk meminimalkan risiko kesalahan pencatatan dan ketidaksesuaian data stok pada operasional UMKM retail. Proyek ini secara khusus bertujuan untuk mencegah terjadinya insiden kehabisan barang (out of stock) melalui penyediaan fitur peringatan otomatis, sekaligus membantu pelaku usaha mempercepat pemantauan pergerakan inventori. Lebih jauh, proyek ini ditujukan untuk membekali pemilik toko dengan analitik bisnis yang akurat terkait performa penjualan, sehingga mereka mampu merancang strategi bisnis dan pengambilan keputusan operasional yang lebih efektif dan tepat sasaran.

## 3. Deskripsi Aplikasi [ADK, RSH]

AURA (Automated Updates & Retail Analytics) merupakan aplikasi mobile berbasis Android yang dirancang sebagai solusi terpadu bagi pelaku UMKM retail untuk mendigitalisasi seluruh pencatatan inventori harian mereka. Aplikasi ini memfasilitasi pengguna dengan fungsi operasional menyeluruh, yang mencakup pengelolaan data barang dan kategori, pencatatan informasi supplier, serta rekam jejak arus pergerakan barang (inventory movement) secara praktis. Sebagai sebuah sistem inventori yang cerdas, AURA mengintegrasikan fitur peringatan stok rendah otomatis guna mencegah kekosongan barang, serta sebuah halaman analitik khusus yang menyajikan rekapitulasi penjualan, tren pasar harian, hingga klasifikasi barang fast-moving maupun slow-moving. Kombinasi fitur tersebut dirancang agar pemilik usaha tidak hanya rapi secara administratif, tetapi juga benar-benar memahami kondisi bisnis mereka secara mendalam.

AURA juga dilengkapi sistem peran (Owner, Manajer, Kasir) dan jejak audit sehingga setiap perubahan data dapat ditelusuri dan aksi sensitif dapat dikendalikan pemilik toko.

## 4. Batasan Aplikasi (Disengaja)

AURA adalah aplikasi pencatatan. Batasan berikut sengaja ditetapkan dan menjadi dasar seluruh rancangan:

1. **Tidak ada payment gateway.** AURA tidak memproses, memverifikasi, maupun menerima pembayaran. Metode pembayaran (Tunai, Transfer, QRIS) hanya label pencatatan pada transaksi.
2. **QRIS/QR berupa gambar yang diunggah sendiri oleh Owner** (hasil tangkapan layar atau foto QR toko). Aplikasi hanya menampilkannya kepada pelanggan di layar pembayaran Kasir. Kasir yang memastikan sendiri pembayaran sudah masuk, lalu menekan tombol konfirmasi manual.
3. **Semua data bersumber dari input pengguna.** Stok masuk (restock), jumlah, total bayar, harga beli, harga jual, dan stok awal seluruhnya diinput pengguna. Aplikasi tidak menentukan, menyarankan, atau mengubah harga secara otomatis.
4. **Perhitungan otomatis hanya turunan dari data yang sudah dicatat:** stok dari pergerakan, harga beli per item dari total bayar dan jumlah, serta laporan dan analisis.
5. **Tidak ada integrasi pihak ketiga untuk transaksi:** tanpa marketplace, tanpa e-wallet, tanpa mesin EDC, tanpa pemindaian pembayaran otomatis, tanpa pemesanan ke supplier. Link supplier hanya membuka aplikasi lain (WhatsApp, Telegram, Instagram, dll.).
6. Pembatasan fitur lain (mis. satu barang per restock, satu toko per Owner, tanpa hutang/piutang) tercantum pada [11-prioritas-pengembangan.md](11-prioritas-pengembangan.md) sebagai batas versi saat ini.

## 5. Definisi Fungsi Utama (Problem Solving) [ASN, MA]

### Fungsi 1: Peringatan Stok Rendah Otomatis (Reorder Point)
- **Sumber Data (CRUD asal):** CRUD Barang (stok minimum), Pergerakan Stok, dan Transaksi Penjualan.
- **Nilai Tambah:** Sistem tidak hanya menampilkan sisa stok, tetapi juga melakukan analisis kondisi stok secara real time setiap kali terjadi perubahan akibat transaksi penjualan, restock, maupun penyesuaian stok. Sistem menghitung ulang jumlah stok terbaru dan membandingkannya dengan batas minimum yang diinput pengguna. Apabila stok berada pada atau di bawah batas minimum, status barang otomatis menjadi **Menipis** (stok 0 menjadi **Habis**) dan notifikasi muncul pada kartu Stok Menipis di Dashboard serta di Pusat Notifikasi. Dengan demikian, pemilik toko dapat mengetahui kondisi persediaan terkini dan mengambil tindakan restock sebelum barang benar-benar habis.

### Fungsi 2: Pengelompokan Barang Fast-moving dan Slow-moving
- **Sumber Data:** Transaksi Penjualan dan data Barang.
- **Nilai Tambah:** Sistem menghitung total terjual tiap barang dalam periode tertentu, membandingkannya dengan ambang, lalu mengelompokkannya otomatis. Owner jadi tahu barang mana yang harus diprioritaskan restock dan mana yang menumpuk di gudang. Aturan ambang didefinisikan pada [07-tab-analisis.md](07-tab-analisis.md).

### Fungsi 3: Rekapitulasi dan Tren Penjualan
- **Sumber Data:** Transaksi Penjualan.
- **Nilai Tambah:** Data transaksi mentah diagregasi per hari, minggu, atau bulan, lalu disajikan sebagai grafik tren, daftar barang terlaris, dan perbandingan antarperiode. Pemilik toko tidak perlu merekap manual.

### Fungsi 4: Pembaruan Stok Otomatis dan Validasi Data
- **Sumber Data:** Transaksi Penjualan dan Pencatatan Pergerakan Stok (masuk, keluar, penyesuaian).
- **Nilai Tambah:** Stok dihitung otomatis dari riwayat pergerakan, bukan diedit manual. Sistem memvalidasi agar stok tidak negatif, dan fitur Penyesuaian Stok mencatat selisih antara data sistem dan stok fisik. Ini langsung menjawab masalah di latar belakang, yaitu human error dan ketidaksesuaian data.

### Fungsi 5: Kontrol Akses dan Jejak Audit
- **Sumber Data:** Akun, Riwayat Aktivitas, Permintaan Persetujuan, dan seluruh CRUD.
- **Nilai Tambah:** Setiap perubahan data tercatat (siapa, kapan, nilai sebelum dan sesudah). Aksi kritis oleh Manajer menunggu persetujuan Owner. Fungsi ini menjawab risiko kesalahan dan penyalahgunaan data pada tim toko.
