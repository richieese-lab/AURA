# 00 — Perubahan Spesifikasi dari Prompt Stitch (BACA SEBELUM FILE spec/ LAIN)

> **Keputusan D-12 (2026-11-20):** `AURA_Prompt_Stitch.md` ([salinan utuh](12-prompt-stitch-desain.md)) adalah keputusan **terbaru** dan **menang** atas Revisi 2 bila bertentangan. Bagian yang tidak disebut Stitch tetap mengikuti Revisi 2 (file `spec/01`–`11`).
> File `spec/01`–`11` **sengaja tidak dihapus isinya** agar jejak keputusan terlihat. Bagian yang batal ditandai dengan catatan di file masing-masing. **Kalau ragu, file ini yang berlaku.**

## 1. Peran: hanya Owner dan Kasir
- **Peran Manajer dihapus.** Hanya ada **Owner** (akses penuh) dan **Kasir** (menu Kasir saja).
- Semua aturan khusus Manajer **batal**: tingkat Hijau/Kuning/Merah, ambang kuning/merah, "Mode percaya", PIN Owner, notifikasi "aksi sensitif Manajer".
- "Pegawai" sekarang berarti **Kasir**. Layar "Manajemen Akun Pegawai" menjadi **"Manajemen Akun Kasir"**: daftar (nama akun, status Aktif/Nonaktif, terakhir login), "+ Tambah Kasir" (nama akun, kata sandi awal + buat otomatis, lalu bagikan info login), detail (Reset kata sandi, Nonaktifkan/Aktifkan, Hapus lunak, "Lihat aktivitas akun ini"). Tidak ada pilihan peran.
- Login mode "Pegawai" tetap ada (ID Toko, Nama Akun, Kata Sandi), dipakai Kasir.
- Teks snackbar elemen abu-abu: **"Hanya Owner yang dapat mengubah data ini"**. Tombol kembali Kasir di K1: **"Kasir hanya dapat keluar melalui profil avatar"**.

## 2. Persetujuan: dihapus
- **Menu Persetujuan di tab Manajemen dihapus.** Tab Manajemen hanya punya: **Operasional** (Kasir), **Persediaan** (Barang, Pergerakan Stok, Supplier), **Kontrol** (Riwayat Aktivitas).
- Pengaturan **"Keamanan dan Persetujuan" dihapus.**
- Entitas `PermintaanPersetujuan` **tidak dibuat**.
- **"Ajukan pembatalan"** oleh Kasir = mengirim **notifikasi ke Owner** (bukan masuk menu Persetujuan). Owner membatalkan lewat **"Batalkan transaksi"** dengan alasan wajib.
- Jenis aktivitas di Riwayat Aktivitas: **Barang, Stok, Keuangan, Penjualan, Akun, Login** (Persetujuan dihapus).

## 3. Batas data 5.000 entri per jenis (retensi bergulir)
- Lima jenis catatan, **masing-masing punya batas 5.000 entri sendiri per toko**: **transaksi penjualan, restock, penyesuaian stok, pengeluaran kustom, pemasukan kustom.**
- Saat entri ke-5.001 suatu jenis disimpan, **entri terlama dari jenis yang sama dihapus otomatis.**
- Ini satu-satunya penghapusan data keuangan/transaksi yang diizinkan, dan dilakukan **oleh sistem**. User tetap tidak bisa mengedit/menghapus (hanya membatalkan).
- **Peringatan 90% (4.500/5.000):** banner oranye tipis di bawah app bar Dashboard untuk jenis yang paling dekat batas: *"Riwayat penjualan hampir penuh (4.500 / 5.000). Data terlama akan terhapus otomatis saat batas terlampaui. Ketuk untuk info."*
- **Notifikasi saat penuh:** *"Riwayat penjualan penuh. Transaksi pertama (INV-240101-0001) telah dihapus otomatis untuk memberi ruang bagi catatan baru."*
- Riwayat Hari Ini menampilkan banner seperti *"Riwayat penjualan: 4.750 / 5.000. Data terlama dihapus otomatis saat batas terlampaui."* (status peringatan pada 90%+).
- Semua informasi ini **hanya informatif**, tidak memblokir aksi apa pun.
- Analisis menampilkan banner: *"Data analisis mencakup catatan dari [tanggal] hingga sekarang (maks. 5.000 entri per jenis catatan)."*
- Tentang Aplikasi mencantumkan: *"Setiap jenis catatan (penjualan, restock, penyesuaian stok, pengeluaran kustom, pemasukan kustom) dibatasi 5.000 entri terakhir per toko secara terpisah. Data terlama dihapus otomatis."*
- **Dampak teknis belum diputuskan:** lihat **K-22** dan **K-23** di [../00-keputusan-proyek.md](../00-keputusan-proyek.md) (stok, saldo, dan HPP harus tetap benar setelah entri lama dihapus).

## 4. Tanpa diskon
- Aplikasi **tidak punya fitur diskon** (Stitch: "no discounts"; user: "di UMKM kemungkinan gk ada"). Omzet = total transaksi Selesai.
- Baris "discount" pada detail penjualan di prompt Stitch layar 9 **tidak dipakai** (K-21).

## 5. Wajib online (K-20)
- Semua aksi tulis (termasuk transaksi Kasir) **butuh internet**. Tanpa internet: pencatatan dinonaktifkan dan muncul pesan. Room hanya menjadi **cache untuk membaca**.
- **Dihapus:** mode offline Kasir, antrean sinkron, status transaksi **"Perlu Ditinjau"**, dan tombol **"Konfirmasi Selisih"** (masih muncul di prompt Stitch layar 16, sehingga desain Figma perlu disesuaikan).
- Status transaksi: **Selesai / Dibatalkan / Diretur**.

## 6. Perubahan layar & fitur kecil
| Area | Perubahan |
|---|---|
| Bottom nav | Label tab kelima **"Profil"**. Ikon tab abu-abu outline (#525252), latar putih, garis/bayangan tipis di atas; tombol (+) merah #930000 dengan ikon putih. Tombol (+) hanya untuk Owner |
| Login | Judul "Masuk ke AURA", toggle Owner/Pegawai, banner error umum |
| Registrasi | Dua langkah, judul **"Isi Profil"**: avatar unggah foto, **"Nama Bisnis"**, dropdown **"Bidang Bisnis"**, **"Saldo Awal"** (helper "Nominal kas saat mulai, boleh 0"), tombol "Buat Akun"/"Selesai", tautan "Sudah punya akun? Masuk", tampil **ID Toko** baca saja + ikon salin. Alamat & logo pindah ke Pengaturan Toko |
| Dashboard | App bar: **nama toko** + lonceng. Kartu Saldo (ikon mata), 3 aksi cepat, kartu **Pemasukan & Pengeluaran berdampingan**, lalu Stok Menipis / Fast Moving / Slow Moving. **Saldo = satu angka kumulatif** (tanpa rincian per metode bayar) |
| Notifikasi | Jenis: stok habis, stok menipis, **anomali**, pembaruan versi, **batas data**, pengajuan pembatalan dari Kasir. Notifikasi stok & anomali **hanya muncul saat statusnya berubah**. Detail anomali: tombol **"Lihat Analisis"**; detail batas data: tombol **"Pelajari lebih lanjut"** |
| Pengeluaran | Detail punya tombol **"Batalkan"** (minta alasan). Form kustom: Judul/jenis biaya (Listrik, Sewa, Gaji, Lainnya), Nominal, Tanggal, Catatan, Foto bukti opsional |
| Kasir K2 | Judul **"Catat Transaksi"**; kartu item dengan foto, harga satuan, stepper, subtotal; tombol garis putus "+ Tambah Barang"; ringkasan bawah (Subtotal Harga, Jumlah Item, Total Transaksi); tombol **"Catat Transaksi"** |
| Kasir K3 | Transfer: kartu info rekening **dengan tombol salin**. QRIS: catatan "Pastikan pembayaran sudah masuk" |
| Kasir avatar | Sheet: nama, chip "Kasir", ID Toko, **Edit Profil** (nama, foto, no HP), **Ubah Kata Sandi**, Ganti Akun, Keluar Akun. Ini satu-satunya akses pengaturan akun Kasir |
| Ganti akun | Verifikasi **kata sandi saja** (bukan biometrik/PIN). Maks. 5 akun, "Tambah akun lain" |
| Daftar Barang | Judul **"Manajemen Barang"**, pencarian "Cari Barang", tombol "+ Tambah"; baris: foto, nama, label kategori amber "Makanan • Stok 50 pcs", harga "Rp 3.500 / pcs", tombol ikon **edit** dan **hapus** (arti tombol hapus: **K-25**) |
| Tambah/Edit Barang | Ada **dropdown satuan**; kategori multi-select + buat di tempat |
| Penyesuaian Stok | Dilakukan langsung oleh Owner (tanpa persetujuan) |
| Riwayat Aktivitas | **Hanya Owner** |
| Analisis | **Hanya Owner.** Ringkasan menambah kartu **Kerugian Penyesuaian** (rusak/hilang/kadaluarsa, ditampilkan terpisah). Pengaturan Fast/Slow diatur Owner di Pengaturan Analisis (**bukan filter sementara**). Sisa hari stok **"Tidak dapat dihitung"** bila rata-rata penjualan = 0. Banner rentang data (lihat §3) |
| Profil | Foto/ikon, username, chip peran, ID Toko + salin, HP, email, "Edit Profil", "Ganti Akun", "Keluar", ikon gear |
| Pengaturan | **Hanya Owner.** Akun: Ubah Kata Sandi, Hapus Akun. Toko: **Manajemen Akun Kasir**, Metode Pembayaran, **Pengaturan Toko** (nama, alamat, logo, **koreksi saldo awal**, tombol **"Keluarkan Semua Sesi Lain"**), Pengaturan Analisis, Preferensi. Bantuan dan Informasi: FAQ, Hubungi Kami, S&K, Kebijakan Privasi, Tentang Aplikasi |
| Metode Pembayaran | Catatan "Gambar hanya ditampilkan di layar pembayaran Kasir." |

## 7. Gaya visual resmi (dari Stitch)
Lihat [../03-aturan-compose-ui.md](../03-aturan-compose-ui.md) §7: palet warna, gradien, kartu 16dp, border #E5E7EB, warna teks, warna status.

## 8. Dampak pada prioritas pengembangan
- Tahap 1: "RBAC dasar" = Owner & Kasir.
- Tahap 2: **"Persetujuan Manajer [tiga tingkat], PIN Owner" dihapus.** Sisanya tetap: ML prediksi stok habis, tren, anomali; retur dan pembatalan transaksi lengkap; ekspor riwayat, struk Bluetooth, push notification.
