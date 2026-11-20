# 06 — Tab 3: Manajemen

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** **Menu Persetujuan dihapus** (bagian "Layer 2 — Persetujuan" di bawah BATAL); "Ajukan pembatalan" = notifikasi ke Owner; Riwayat Aktivitas **hanya Owner**, tanpa jenis Persetujuan; K2 berjudul "Catat Transaksi"; **wajib online** (tanpa "Perlu Ditinjau"); **tanpa diskon**; Daftar Barang punya tombol edit & hapus per baris (K-25). Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 7, "Tab 3 - Manajemen". Tanda **+** = navigasi. Struktur object Barang ada di [08-object-barang-dan-model-data.md](08-object-barang-dan-model-data.md).

Tab ini berfungsi untuk manajemen toko secara keseluruhan. Di sini terdapat menu **Kasir** yang jika dimasuki akan masuk ke sistem kasir (tetap bisa melihat stok dan barang [hanya akses read]). Ada menu manajemen **Supplier** (daftar supplier toko; jika dibuka salah satunya, muncul link dan list barang yang disupply). Ada manajemen **Barang** (semua list barang; masing-masing adalah object "Barang").

## Layer 1 — Komponen

| # | Menu | Grup | Akses |
|---|---|---|---|
| 1 | **Kasir +** | Operasional | Owner, Manajer, Kasir |
| 2 | **Barang +** — daftar, tambah, profil, edit, restock, penyesuaian, kartu stok, kategori | Persediaan | Owner, Manajer |
| 3 | **Pergerakan Stok +** — buku besar masuk/keluar/penyesuaian dan Penyesuaian Stok | Persediaan | Owner, Manajer |
| 4 | **Supplier +** — daftar supplier, link kontak, barang yang disupply | Persediaan | Owner, Manajer |
| ~~5~~ | ~~**Persetujuan +**~~ (BATAL) — permintaan persetujuan Manajer, lencana angka bila ada yang menunggu | Kontrol | Owner memutuskan, Manajer mengajukan |
| 6 | **Riwayat Aktivitas +** — jejak audit seluruh akun | Kontrol | Owner dan Manajer (baca saja) |

---

## Layer 2 — Kasir

Kasir hanya mencatat penjualan. **Harga jual berasal dari data Barang dan tidak diubah di kasir.**

1. **K1 Beranda Kasir** — App bar: tombol kembali (abu-abu dan tidak bisa ditekan untuk Kasir), judul "Kasir", ikon Riwayat Hari Ini, avatar akun. Pencarian dan ikon scan barcode, chip kategori. Grid/daftar barang: foto, nama, harga jual, badge stok; stok menipis berbadge kuning; stok 0 abu-abu, tidak dapat dipilih, berlabel "Habis". **Tekan lama** pada barang membuka sheet info baca saja (nama, harga, stok, stok minimum, kategori) tanpa tombol edit atau restock. Bilah keranjang melayang di bawah: "N item - Rp total" dan "Lihat Keranjang".
2. **K2 Keranjang +** — daftar item dengan stepper jumlah; jumlah maksimum = stok tersedia; geser untuk hapus. Catatan transaksi opsional. Total otomatis. Tombol "Bayar".
3. **K3 Pembayaran +** — total tagihan; pilih metode pembayaran: **Tunai, Transfer, atau QRIS** (hanya pencatatan metode, tidak ada payment gateway).
   - **Tunai:** input uang diterima, chip cepat (Uang pas dan kelipatan umum), kembalian dihitung otomatis.
   - **Transfer:** menampilkan info rekening teks bila Owner mengisinya.
   - **QRIS:** menampilkan gambar QRIS yang diunggah Owner dalam ukuran besar agar mudah dipindai pelanggan.
   - Untuk Transfer dan QRIS, Kasir memastikan sendiri pembayaran masuk lalu menekan tombol **"Pembayaran Diterima"** sebagai konfirmasi manual.
   - Jika gambar QRIS belum diunggah, tampil pesan **"Gambar QRIS belum diatur. Hubungi Owner."** dan metode lain tetap dapat dipakai.
   - Tombol **"Selesaikan Transaksi"** aktif hanya setelah pembayaran dicatat cukup.
4. **K4 Transaksi Berhasil +** — nomor transaksi, mis. **`INV-261117-0001`** (urut per toko per hari), ringkasan, metode pembayaran, kembalian. Tombol "Bagikan Struk" (gambar/PDF lewat WhatsApp dll.) dan "Transaksi Baru".
5. **Riwayat Hari Ini +** — Kasir hanya melihat transaksi miliknya hari ini; Owner/Manajer melihat semua. Detail transaksi = struk. Kasir mendapat tombol **"Ajukan pembatalan"** yang diteruskan ke Manajer/Owner; Owner dan Manajer (dengan persetujuan) mendapat **"Batalkan transaksi"** dengan alasan wajib.

**Validasi kasir:**
1. Jumlah tidak melebihi stok.
2. Saat menekan Selesaikan, stok dicek ulang dari data terbaru; jika berubah, tampil dialog "Stok X tersisa N. Sesuaikan jumlah?".
3. Harga di keranjang **dikunci** saat barang dimasukkan.
4. Kasir tidak dapat mengubah harga maupun memberi diskon bebas.

Pembatalan mengembalikan stok lewat pergerakan **"Masuk - Pembatalan"** dan menandai transaksi "Dibatalkan" (tidak dihapus); **retur sebagian** mengembalikan barang tertentu dengan alasan.

---

## Layer 2 — Barang

1. **Daftar Barang +** — Restock, nama, dan gambar Barang. Ditambah chip stok dan status, pencarian, filter kategori, urut (Nama / Stok terendah / Terlaris), pilihan tampilkan arsip, tombol "+ Tambah Barang", dan menu "Kelola Kategori".
2. **Tambah Barang +** — formulir sama dengan Edit Barang, ditambah **stok awal** yang diinput user (dicatat sebagai pergerakan **"Masuk - Stok awal"**, **bukan pengeluaran**) dan **harga beli awal opsional**. Wajib: **nama, harga jual, stok minimum**.
3. **Kelola Kategori +** — daftar kategori dan jumlah barang, tambah, ubah nama, hapus. Menghapus kategori memunculkan dialog "N barang memakai kategori ini; kategori akan dilepas dari barang tersebut". Barang tidak ikut terhapus.

### Layer 3 — Profil Barang
(Dibuka dari Daftar Barang, **tanpa** tombol Restock pada daftar.)

1. **Tampilan Profil Barang:** id, foto, nama, satuan, SKU/barcode, harga beli (bisa banyak berdasarkan supplier), HPP rata-rata, harga jual, kategori (bisa lebih dari 1; dibuat dan ditambahkan oleh user), stok saat ini, stok minimum, ringkasan 30 hari (terjual, status Fast/Slow).
2. **Edit Barang +** (Layer 4)
3. **Restock +** (Layer 4)
4. **Penyesuaian Stok +** — membuka alur Penyesuaian Stok dengan barang terisi.
5. **Kartu Stok +** — riwayat pergerakan khusus barang ini.
6. **Menu titik tiga: Arsipkan.** Barang yang sudah punya riwayat tidak dihapus permanen, melainkan diarsipkan (hilang dari Kasir dan daftar aktif, riwayat tetap). **Hapus permanen hanya untuk barang yang belum pernah bertransaksi.**

### Layer 4 — Edit Barang dan Restock

1. **Edit Barang:** edit foto, nama, harga jual, harga beli, kategori (dropdown dari kategori yang dibuat user, dan di bawahnya ada textfield untuk membuat baru; ketika textfield ditekan sehingga muncul tanda typing, otomatis tombol Buat dan Batal muncul di bawah textfield — **tidak berupa pop up**).
   - **Stok tidak dapat diedit di sini**; ada teks petunjuk "Ubah stok lewat Restock atau Penyesuaian Stok".
   - Harga jual di bawah harga beli memunculkan **peringatan, bukan blokir**.
   - Seluruh harga ditentukan sendiri oleh user.
2. **Restock:** formulir pencatatan penerimaan barang yang seluruh isinya diinput user.
   1. Pilih supplier dari data master Supplier (dropdown; jika belum ada, buat di tempat dengan textfield nama dan link yang bekerja seperti tambah kategori). Setiap baris supplier punya dua aksi: **ketuk baris = pilih supplier**, **ikon tautan = buka link** yang dimasukkan user (link kontak WA, Telegram, atau profil IG).
   2. Isi **jumlah masuk** dan **total dibayar**. Sistem menampilkan `harga per item = total / jumlah`.
   3. Opsional: tanggal (default sekarang), foto nota, catatan.
   4. Simpan Restock. **Hasil otomatis:** pergerakan stok Masuk, pengeluaran jenis Pembelian, pembaruan harga beli barang pada supplier tersebut, hitung ulang HPP dan status stok, catatan di Riwayat.
   - **Satu barang per restock.**

---

## Layer 2 — Pergerakan Stok

1. **Daftar global pergerakan +** — FilterPeriode, filter jenis (Masuk/Keluar/Penyesuaian), pencarian barang. Baris: waktu, barang, jenis dan sumber, jumlah, `stok sebelum → sesudah`, akun pelaku. Sifatnya **hanya baca dan append-only**.
2. **Penyesuaian Stok +** — alur stock opname:
   1. Pilih barang (cari atau scan), layar menampilkan stok sistem.
   2. Masukkan stok fisik hasil hitung; sistem menghitung selisih dan nilainya dalam rupiah.
   3. Pilih alasan wajib: **Rusak, Hilang, Kadaluarsa, Salah catat, Lainnya**; catatan dan foto bukti opsional.
   4. Simpan; sistem membuat pergerakan Penyesuaian dan menghitung ulang status stok. Jika selisih melewati ambang dan pelakunya Manajer, permintaan menunggu persetujuan Owner.

**Jenis pergerakan:**
- **Masuk:** Restock, Stok awal, Retur dari pelanggan, Pembatalan penjualan.
- **Keluar:** Penjualan, Rusak, Hilang, Kadaluarsa.
- **Penyesuaian:** koreksi selisih hitung fisik.

**Stok barang = jumlah seluruh pergerakannya.**

---

## Layer 2 — Supplier

1. **Daftar Supplier +** — nama, ikon jenis link, jumlah barang yang disupply, pencarian, tombol "+ Tambah Supplier".
2. **Detail Supplier +** (Layer 3) — nama, tombol **Hubungi** yang membuka link (WhatsApp, Telegram, Instagram, dll. dengan ikon otomatis), daftar barang yang disupply beserta harga beli terakhir, riwayat pembelian ringkas (otomatis dari Restock), catatan. Tombol Edit, tambah barang ke supplier secara manual, **Hapus (hanya bila belum pernah dipakai, selain itu Nonaktifkan)**. Satu barang boleh punya beberapa supplier. **Link harus berupa URL valid.**

---

## ~~Layer 2 — Persetujuan~~ (BATAL — Manajer dihapus, lihat 00-perubahan-dari-stitch.md §2)

1. Dua tab: **Menunggu** dan **Riwayat**.
2. **Owner:** tiap kartu menampilkan siapa, aksi apa, nilai sebelum dan sesudah, alasan, waktu. Tombol **Setujui** dan **Tolak** (alasan wajib saat tolak). Persetujuan juga dapat dilakukan dari notifikasi, atau lewat **PIN Owner** yang dimasukkan langsung di ponsel Manajer bila Owner sedang di toko.
3. **Manajer:** melihat pengajuannya sendiri beserta status (**Menunggu / Disetujui / Ditolak / Kedaluwarsa**) dan boleh membatalkan pengajuan yang masih menunggu.

Pengajuan ditulis dalam bahasa biasa, contoh: *"Manajer Budi ingin mengubah harga jual Kopi ABC dari Rp 12.000 menjadi Rp 9.000. Alasan: promo akhir pekan"*, dilengkapi nilai sebelum dan sesudah. Pengajuan **kedaluwarsa setelah 48 jam** dan dapat diajukan ulang. Perubahan baru diterapkan sistem **setelah disetujui**. Setiap keputusan masuk ke Riwayat Aktivitas.

---

## Layer 2 — Riwayat Aktivitas

Fitur riwayat untuk memantau seluruh aktivitas, dengan filter periode yang sama persis dengan pengeluaran dan pemasukan (harian, mingguan, bulanan, tahunan, dan custom).

1. **Filter:** periode (FilterPeriode), akun (Semua / per orang), jenis aktivitas (**Barang, Stok, Keuangan, Penjualan, Akun, Persetujuan, Login**).
2. **Baris aktivitas +** — waktu, akun dan perannya, ringkasan aksi seperti "Budi mengubah harga jual Kopi ABC", status persetujuan jika ada. Detail: nilai sebelum → sesudah, nama perangkat.
3. **Sifat:** hanya baca untuk semua orang, **tidak bisa diedit atau dihapus siapa pun termasuk Owner**. Mencakup edit barang, transaksi, pengeluaran, pemasukan, login/logout, dan perubahan akun.

> **Catatan keputusan (2026-11-20):** diskon **ditentukan oleh akun Owner** (K-14). Kasir tetap tidak bisa memberi diskon bebas. Mekanisme detail (per barang/per transaksi, nominal/persen, cara Kasir memakainya) menunggu **K-21** di [../00-keputusan-proyek.md](../00-keputusan-proyek.md).
