# 03 — Alur Autentikasi dan Onboarding

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** Mode Pegawai = **Kasir** saja; registrasi jadi dua langkah "Isi Profil" (Nama Bisnis, Bidang Bisnis, Saldo Awal, foto); ganti akun **verifikasi kata sandi saja** (tanpa biometrik/PIN); **wajib online**. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 7 (awal). Lihat juga [../00-keputusan-proyek.md](../00-keputusan-proyek.md): Firebase **tidak** dipakai, sehingga login Google & reset sandi via email bergantung pada server sendiri (masalah terbuka).

## Layar Login

Layar Login memiliki dua mode melalui toggle di atas: **Owner** dan **Pegawai** (Manajer/Kasir).

1. **Mode Owner:** email + kata sandi, atau tombol "Masuk dengan Google". Tautan "Lupa kata sandi" (tautan reset dikirim ke email) dan "Daftar toko baru".
2. **Mode Pegawai:** tiga textfield, yaitu **ID Toko, Nama Akun, dan Kata Sandi**. Tidak ada reset mandiri; teks info "Lupa kata sandi? Hubungi Owner".
3. Pesan error bersifat umum agar tidak membocorkan data (mis. "ID toko, nama akun, atau kata sandi salah"). Setelah **5 kali gagal, login dikunci 5 menit**.
4. Aturan: kata sandi **minimal 8 karakter**; nama akun pegawai **unik per toko**.

## Registrasi Owner dan Setup Toko

1. Daftar dengan email + kata sandi atau Google, dengan persetujuan Syarat dan Ketentuan dan Kebijakan Privasi.
2. Data toko: nama toko (wajib), jenis usaha, alamat dan logo (opsional). Sistem membuat **ID Toko unik (8 karakter huruf besar dan angka)** yang tidak dapat diubah.
3. **Saldo awal:** nominal kas yang diinput Owner saat mulai memakai AURA (boleh 0). Menjadi dasar perhitungan Saldo.
4. **Daftar periksa mulai cepat di Dashboard:** Tambah barang pertama, Atur stok minimum, Unggah gambar QRIS (opsional), Tambah pegawai, Coba transaksi pertama di Kasir. Hilang otomatis saat selesai.

## Sesi, Ganti Akun, dan Keluar

1. **Saat aplikasi dibuka:** cek sesi tersimpan. Valid: Owner/Manajer ke Dashboard, Kasir ke menu Kasir. Tidak valid: ke Login. Jika akun pegawai sudah dinonaktifkan atau dihapus Owner, sesi dihentikan dengan pesan "Akun Anda tidak lagi aktif. Hubungi Owner."
2. **Ganti Akun** menampilkan bottom sheet daftar akun yang pernah login di perangkat ini (maks. 5). Memilih akun lain wajib verifikasi kata sandi (atau biometrik/PIN), sehingga Kasir tidak dapat berpindah ke akun Owner tanpa kredensial. Tersedia tombol "Tambah akun lain" yang membuka layar Login.
3. **Keluar:** Owner/Manajer keluar lewat Profile > Keluar > dialog konfirmasi. Kasir memiliki ikon avatar di app bar menu Kasir yang membuka sheet kecil: nama, peran, ID Toko, Ganti Akun, Keluar Akun.

> **Catatan keputusan (2026-11-20):**
> - Selama server belum siap, login diuji dengan **autentikasi lokal sementara di Room** (sandi di-hash) (K-07, mulai Tahap 1).
> - "Masuk dengan Google" dan "Lupa kata sandi" ditangani **server sendiri**; library login Google disetujui prinsip (K-18).
> - Tahap 0 belum punya login; peran dipilih lewat layar sementara "Pilih peran (dev)" (K-13).
