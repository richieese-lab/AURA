# 05 — Tab 2: Profile (dan Pengaturan)

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** **Manajemen Akun Pegawai → Manajemen Akun Kasir** (tanpa ubah peran); **Keamanan dan Persetujuan dihapus**; tambah **Pengaturan Toko**; Pengaturan **hanya Owner**; Kasir mengakses Edit Profil & Ubah Kata Sandi lewat sheet avatar. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 7, "Tab 2 - Profile". Tanda **+** = navigasi.

Tab ini sebenarnya berfungsi sebagai settings pada akun user, tidak hanya profile; pengaturan akun, kebijakan dan privasi ada di sini juga. Aplikasi memakai konsep **RBAC**. Siapa pun yang membuat akun untuk toko disebut **Owner**. Ada 2 jenis akun pegawai: **Manajer** dan **Kasir**, keduanya dibuat lewat akun Owner. Pada Login, diberikan opsi login sebagai akun sekunder (Manajer/Kasir) dengan 3 textfield: ID Toko (ID tampil di profil namun tidak dapat diubah), nama akun, dan password. Owner bisa mengedit password dan bahkan menghapus akun Pegawai (Owner memiliki kendali penuh atas kedua akun ini).

## Layer 1 — Komponen

1. **ID Toko** — tidak dapat diubah, unik. Memiliki tombol salin agar Owner mudah membagikannya ke pegawai.
2. **Tombol keluar +** — user dibawa kembali ke halaman login awal, setelah dialog konfirmasi.
3. **Ganti akun +** — bottom sheet daftar akun yang pernah login di perangkat; user memilih salah satu untuk login setelah verifikasi.
4. **Foto** — foto profil yang diinput user; jika belum ada, diganti icon SVG profil.
5. **Username dan peran** — nama user yang diinput sendiri, beserta chip peran Owner/Manajer/Kasir.
6. **No HP** — nomor handphone user.
7. **Email** — email (termasuk email Google yang dapat dipakai sebagai autentikasi login). Untuk pegawai, No HP dan Email bersifat **opsional**.
8. **Edit Profil +** — tombol menuju halaman edit profil.
9. **Pengaturan +** — ikon pengaturan di pojok kanan atas.

## Layer 2

1. **Edit Profil +** (dari tombol Edit Profil) — satu halaman mode-edit untuk foto, username, nomor HP, dan email. **Pegawai hanya dapat mengubah nama, foto, dan nomor HP.**
2. **Pengaturan +** (dari ikon pengaturan) — satu halaman dengan tiga grup: **Akun**, **Toko**, serta **Bantuan dan Informasi**.

## Layer 3

1. **Ubah kata sandi +** [grup Akun] — 3 password bar dengan tombol lihat/sembunyikan, berlabel: a. Kata sandi saat ini, b. Kata sandi baru, c. Konfirmasi kata sandi baru. Berlaku untuk semua peran.
2. **Hapus Akun +** [grup Akun, **hanya Owner**, di zona berbahaya paling bawah] — halaman peringatan konsekuensi: seluruh data toko dan akun pegawai ikut terhapus. User wajib memasukkan kata sandi dan mengetik kata **"HAPUS"**, lalu ada **masa tenggang pemulihan 14 hari**; login kembali membatalkan penghapusan.
3. **Manajemen Akun Pegawai +** [grup Toko, **hanya Owner**; abu-abu untuk Manajer] — daftar akun Manajer dan Kasir. Rincian pada Layer 4.
4. **Metode Pembayaran +** [grup Toko, **hanya Owner**; abu-abu untuk Manajer] — halaman menyimpan gambar QRIS toko yang diunggah dari galeri atau kamera: tombol Unggah/Ganti/Hapus gambar, pratinjau gambar, nama penerima, serta info rekening transfer berupa teks bebas (opsional). Gambar QRIS dan info rekening **hanya ditampilkan pada layar Pembayaran Kasir**, tidak diproses aplikasi.
5. **Keamanan dan Persetujuan +** [grup Toko, **hanya Owner**] — ambang kuning/merah aksi Manajer yang diinput Owner, PIN Owner untuk persetujuan di tempat, dan "Mode percaya" per kategori aksi.
6. **Pengaturan Analisis +** [grup Toko, **hanya Owner**] — periode dan ambang Fast/Slow Moving.
7. **Preferensi +** [grup Toko] — aktif/nonaktif notifikasi per jenis, sembunyikan nominal.
8. **Pusat Bantuan / FAQ +** [grup Bantuan dan Informasi] — daftar pertanyaan umum.
9. **Hubungi Kami +** [grup Bantuan dan Informasi] — daftar nomor, email, serta pesan customer service.
10. **Syarat dan Ketentuan +** [grup Bantuan dan Informasi] — syarat dan ketentuan serta aturan main yang harus dipahami dan disetujui user.
11. **Kebijakan Privasi +** [grup Bantuan dan Informasi] — jenis data pribadi yang dikumpulkan, tujuan penggunaan, cara menjaga keamanan, hak penghapusan data, dan kontak.
12. **Tentang Aplikasi +** [grup Bantuan dan Informasi] — versi aplikasi dan catatan rilis/patch.

## Layer 4 — Manajemen Akun Pegawai

1. **Daftar pegawai** — nama akun, peran, status Aktif/Nonaktif, terakhir login; tombol **"+ Tambah Pegawai"**.
2. **Tambah Pegawai +** — nama akun unik per toko, peran Manajer/Kasir, kata sandi awal minimal 8 karakter dengan tombol buat otomatis. Setelah dibuat ada tombol **Bagikan info login** (ID Toko + nama akun + kata sandi) lewat WhatsApp atau salin. **Pegawai wajib mengganti kata sandi saat login pertama.**
3. **Detail pegawai +** — Reset kata sandi, ubah peran, Nonaktifkan/Aktifkan, Hapus, dan pintasan "Lihat aktivitas akun ini" yang membuka Riwayat Aktivitas yang sudah terfilter. **Hapus bersifat penghapusan lunak:** akun tidak bisa login dan hilang dari daftar, tetapi namanya tetap tampil di riwayat lama. Sesi pegawai yang sedang aktif langsung berakhir.
