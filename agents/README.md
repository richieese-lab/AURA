# agents/ — Panduan untuk AI agent (Claude Code) di proyek AURA

**AURA (Automated Updates & Retail Analytics)**: aplikasi Android (Kotlin + Jetpack Compose) untuk pencatatan inventori UMKM retail. Package `com.aura.app`.

Folder ini menggantikan dokumen `AURA_Dokumen_Flow_UI_Revisi_2.docx.md`, `AURA_Prompt_Stitch.md`, dan folder modul praktikum sebagai referensi. Dokumen asli **dipecah** per topik supaya agent cukup membaca bagian yang relevan dengan tugasnya, bukan seluruh 48 KB setiap kali.

## Urutan baca wajib

1. **[00-keputusan-proyek.md](00-keputusan-proyek.md)**: keputusan final dan pertanyaan yang **belum boleh ditebak**. Kalau ada yang bertentangan, file ini yang berlaku.
2. **[04-alur-kerja-agent.md](04-alur-kerja-agent.md)**: cara kerja, definisi selesai, perintah verifikasi.
3. **[01-stack-dan-batasan-modul.md](01-stack-dan-batasan-modul.md)**: library yang boleh dipakai, versi, template Gradle, prosedur minta izin library.
4. **[02-arsitektur-dan-konvensi-kode.md](02-arsitektur-dan-konvensi-kode.md)**: lapisan, struktur paket, pola kode dari modul, aturan data, konflik dengan repo penunjang.
5. **[03-aturan-compose-ui.md](03-aturan-compose-ui.md)**: aturan Compose, elemen berbasis peran, checklist.
6. File `spec/` yang sesuai dengan fitur yang sedang dikerjakan.
7. File `plan/` untuk tahap yang sedang aktif.

## Spesifikasi produk (`spec/`)

| File | Isi |
|---|---|
| **[spec/00-perubahan-dari-stitch.md](spec/00-perubahan-dari-stitch.md)** | **BACA DULU.** Perubahan terbaru dari Prompt Stitch yang menang atas Revisi 2 (Owner & Kasir saja, tanpa Persetujuan, batas 5.000 entri, tanpa diskon, wajib online, perubahan layar) |
| [spec/01-ringkasan-produk.md](spec/01-ringkasan-produk.md) | Latar belakang, tujuan, deskripsi, **batasan disengaja**, 5 fungsi utama |
| [spec/02-konvensi-ui.md](spec/02-konvensi-ui.md) | Bottom nav, istilah baku, format, status stok, tombol abu-abu, pola interaksi |
| [spec/03-autentikasi-onboarding.md](spec/03-autentikasi-onboarding.md) | Login Owner/Pegawai, registrasi toko, sesi, ganti akun |
| [spec/04-tab-dashboard.md](spec/04-tab-dashboard.md) | Dashboard dan layer turunannya |
| [spec/05-tab-profile-pengaturan.md](spec/05-tab-profile-pengaturan.md) | Profile, Pengaturan, Manajemen Akun Pegawai |
| [spec/06-tab-manajemen.md](spec/06-tab-manajemen.md) | Kasir, Barang, Pergerakan Stok, Supplier, Persetujuan, Riwayat Aktivitas |
| [spec/07-tab-analisis.md](spec/07-tab-analisis.md) | Sub-tab analisis, definisi metrik, Fast/Slow Moving, ML |
| [spec/08-object-barang-dan-model-data.md](spec/08-object-barang-dan-model-data.md) | Field Barang, model data utama |
| [spec/09-rbac.md](spec/09-rbac.md) | Arsip RBAC Revisi 2 (Manajer & tingkat persetujuan **batal**) |
| [spec/10-rantai-efek-dan-kondisi-khusus.md](spec/10-rantai-efek-dan-kondisi-khusus.md) | Efek otomatis tiap aksi, kondisi khusus |
| [spec/11-prioritas-pengembangan.md](spec/11-prioritas-pengembangan.md) | Tahap 0–3 |
| [spec/12-prompt-stitch-desain.md](spec/12-prompt-stitch-desain.md) | Salinan utuh prompt Google Stitch: gaya global, palet warna, deskripsi 36 layar |

## Rencana (`plan/`)

| File | Status |
|---|---|
| [plan/tahap-0-fondasi.md](plan/tahap-0-fondasi.md) | Langkah 1–3 selesai; 4–7 menunggu perintah |
