# 02 — Konvensi UI dan Istilah Baku

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** Peran **Manajer dihapus** (contoh abu-abu untuk Manajer di poin 9 batal); teks snackbar baku "Hanya Owner yang dapat mengubah data ini"; tab kelima berlabel **Profil**; tombol (+) hanya Owner. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 6 + catatan cara membaca desain. Berlaku untuk SEMUA layar.

## Cara membaca spesifikasi layar

- Jika ada tanda **+**, berarti elemen tersebut berupa **navigasi** (berpindah ke page lain). Jika tidak ada tanda +, artinya hanya berupa teks biasa atau elemen yang tidak berfungsi sebagai navigator.
- **Layer** = tingkat kedalaman dari tab utama. Misal dari Dashboard masuk ke Pengeluaran (layer 1). Dari Pengeluaran masuk ke salah satu rincian pengeluaran (layer 2).
- Tab bernomor 1 sampai 4 di spesifikasi hanya penomoran pembahasan, **bukan urutan tampil**.

## Konvensi

1. **Navigasi utama:** bottom navigation dengan 5 posisi, berurutan **Dashboard, Manajemen, tombol tambah (+) di tengah, Analisis, Profile**. Kasir tidak memakai bottom navigation (langsung masuk ke menu Kasir).
2. **Tombol tambah (+) di tengah (shortcut Kasir):** berbentuk tombol bulat menonjol, bukan tab. Jika ditekan, langsung membuka menu Kasir (K1 Beranda Kasir) tanpa melalui tab Manajemen; bila keranjang masih berisi, langsung ke K2 Keranjang. Bottom navigation tetap terlihat selama Owner/Manajer berada di alur Kasir, dan tombol (+) tampil aktif. Hanya untuk Owner dan Manajer; Kasir tidak memiliki bottom navigation sehingga tombol ini tidak ada. Menu Kasir di tab Manajemen dan tombol "Mulai Kasir" di Dashboard tetap ada dan menuju tujuan yang sama.
3. **Istilah baku:** Barang (bukan Produk/Item), Supplier (bukan Vendor), Stok, Pemasukan, Pengeluaran, Restock, Owner, Manajer, Kasir. "Pegawai" berarti Manajer dan Kasir.
4. **Format:** nominal `Rp 1.250.000`; tanggal `17 Nov 2026, 19.48 WIB`; angka stok selalu disertai satuan (pcs, kg, dst.).
5. **Status stok** memakai warna sekaligus label dan ikon: **Aman** (hijau), **Menipis** (kuning/oranye), **Habis** (merah), **Diarsipkan/Nonaktif** (abu-abu).
6. **Nominal Pemasukan, Pengeluaran, dan Saldo di Dashboard disembunyikan secara bawaan** (`Rp ••••••`, ikon mata untuk Saldo). Nominal baru tampil setelah kartu ditekan. Pilihan pengguna diingat.
7. **Elemen yang dibatasi peran** berwarna abu-abu dan tidak bisa ditekan. Ketukan pada elemen tersebut menampilkan snackbar singkat berisi alasan (mis. "Hanya Owner yang dapat mengubah akun pegawai"). Aturan akses tetap ditegakkan juga di sisi data, bukan hanya tampilan.
8. **Mekanisme tombol abu-abu (aturan tampilan):**
   1. Saat layar dibuka, aplikasi membaca peran akun yang sedang login.
   2. Setiap elemen yang punya batasan peran memiliki daftar peran yang diizinkan. Bila peran tidak termasuk, elemen tetap tampil (tidak disembunyikan) tetapi berstatus disabled: warna abu-abu, ikon/teks redup, tanpa efek tekan. Alasannya: UI Manajer sama dengan Owner, dan pengguna tahu fitur itu ada tetapi bukan haknya.
   3. Mengetuk elemen abu-abu tidak membuka layar apa pun dan menampilkan snackbar singkat berisi alasan, mis. "Hanya Owner yang dapat mengubah akun pegawai".
   4. Berlaku sama untuk tombol, menu, kartu, dan field input (field jadi baca saja).
   5. Pembatasan berlaku per elemen, bukan per layar: layar boleh dibuka tetapi tombol aksi tertentu abu-abu.
9. **Contoh penerapan abu-abu:** Manajer melihat grup Toko di Pengaturan dengan Manajemen Akun Pegawai, Metode Pembayaran, dan Keamanan dan Persetujuan berwarna abu-abu. Kasir pada menu Kasir melihat tombol kembali ke Manajemen abu-abu. Kasir pada Riwayat Hari Ini hanya melihat tombol "Ajukan pembatalan" (tombol "Batalkan transaksi" tidak ada karena bukan haknya).
10. **Bedakan dengan tingkat persetujuan:** elemen abu-abu berarti tidak ada akses sama sekali. Aksi tingkat Merah milik Manajer **tidak** abu-abu; tombolnya aktif tetapi hasilnya berupa permintaan persetujuan Owner. Pembatasan tampilan hanya kosmetik, aturan akses tetap ditegakkan juga di sisi data.
11. **Aksi destruktif** (hapus, arsip, batalkan transaksi) memakai dialog konfirmasi dengan kata kerja jelas. **Aksi ringan** memakai snackbar dengan tombol Urungkan.
12. **Pola "buat baru di tempat"** (kategori, supplier): textfield di bawah dropdown. Saat textfield ditekan dan muncul tanda typing, tombol **Buat** dan **Batal** muncul di bawahnya (**bukan pop up**).
13. **FilterPeriode** adalah satu komponen yang sama di Pengeluaran, Pemasukan, Pergerakan Stok, Riwayat Aktivitas, dan Analisis: dropdown Hari ini, Minggu ini, Bulan ini, Tahun ini, Kustom. Tombol "Lanjutan" membuka input tanggal dan jam mulai serta tanggal dan jam akhir untuk rentang kustom.
14. **Setiap layar daftar memiliki empat kondisi tampilan:** memuat, kosong (ilustrasi dan ajakan aksi), error/offline (banner dan coba lagi), dan tanpa izin (pesan peran).
