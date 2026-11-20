# 00 — Keputusan Proyek & Pertanyaan Terbuka

> File ini adalah **sumber kebenaran** untuk keputusan yang diambil user. Jika bertentangan dengan file lain di `agents/`, file ini yang berlaku. Agent **tidak boleh** memutuskan sendiri item berstatus **MENUNGGU**; tanyakan ke user.

## A. Keputusan final

| ID | Tanggal | Keputusan |
|---|---|---|
| D-01 | 2026-11-20 | Aplikasi: Android, **Kotlin + Jetpack Compose** sebagai arsitektur utama. |
| D-02 | 2026-11-20 | Implementasi **wajib dalam batasan modul praktikum PAPB**. Tingkat: **"Ketat + izin per kasus"**: pola arsitektur ikut modul; library tambahan hanya bila fitur wajib dokumen mustahil tanpanya, dan **harus ditanyakan dulu**. Saran repo penunjang yang bertentangan → modul menang. |
| D-03 | 2026-11-20 | **Firebase TIDAK dipakai sama sekali** (termasuk Firebase Storage/Auth/Firestore). |
| D-04 | 2026-11-20 | Backend = **server sendiri**, cadangan **Railway**; diakses lewat **Retrofit**. File (foto barang, QRIS, nota, foto profil) juga disimpan di server tersebut. Detail API **belum ada**. |
| D-05 | 2026-11-20 | **Room** dipakai sebagai database lokal **cache untuk membaca** (K-20: wajib online, tanpa antrean offline). Selama server belum ada (pengembangan), Room sementara menjadi tempat simpan utama. |
| D-06 | 2026-11-20 | Package / applicationId: **`com.aura.app`**. |
| D-07 | 2026-11-20 | Struktur: **root repo = root proyek Gradle** dengan satu modul **`app/`** (mengikuti struktur modul). |
| D-08 | 2026-11-20 | Tahap awal = **"Fondasi saja"** (Tahap 0): setup proyek, tema, struktur paket, navigasi kerangka (bottom nav 5 posisi + aturan peran), skema Room lengkap, repository, logika bisnis inti (stok dari pergerakan, status stok, HPP, format Rp/tanggal) + unit test; UI hanya placeholder karena Figma belum selesai. |
| D-09 | 2026-11-20 | Urutan kerja: dokumen `agents/` dulu → **user review** → kode Tahap 0. User setuju **tidak menghapus folder modul** sampai gradle wrapper disalin (langkah 1 [plan/tahap-0-fondasi.md](plan/tahap-0-fondasi.md)). |
| D-10 | 2026-11-20 | Repo penunjang (`new-silvermoon/awesome-android-agent-skills`, `RhymezxCode/android-agent-project-kit`, `aldefy/compose-skill`) sudah dibaca; aturan yang relevan & tidak bertentangan dengan modul dimasukkan ke `02` dan `03`. Repo tidak disalin ke proyek. |
| D-11 | 2026-11-20 | **Semua folder praktikum** adalah referensi (Modul3, Materi_PAPB_sham, modul6–9, ProfilApp, test2, TugasPraktikumBab2, TugasTambahan1; TugasPraktikum1 & TugasPraktikum_Modul5 kosong). Jika dua modul mengajarkan cara berbeda untuk hal yang sama, **pakai cara modul yang lebih baru** (lihat [01-stack-dan-batasan-modul.md](01-stack-dan-batasan-modul.md) §0). |
| D-12 | 2026-11-20 | Acuan spesifikasi: **`AURA_Prompt_Stitch.md` adalah keputusan terbaru dan MENANG** atas `AURA_Dokumen_Flow_UI_Revisi_2` bila bertentangan; hal yang tidak disebut Stitch tetap ikut Revisi 2. Ringkasan perubahan: [spec/00-perubahan-dari-stitch.md](spec/00-perubahan-dari-stitch.md); salinan utuh: [spec/12-prompt-stitch-desain.md](spec/12-prompt-stitch-desain.md). Dokumen "Prompt untuk saran flow aplikasi dan UI" = draf awal, bukan acuan. **Dikonfirmasi ulang oleh user: Prompt Stitch adalah dokumen yang paling benar.** |
| D-14 | 2026-11-20 | (dari Stitch) **Hanya dua peran: Owner & Kasir. Manajer dihapus**, beserta Persetujuan, tingkat Hijau/Kuning/Merah, ambang, PIN Owner, Mode percaya. |
| D-15 | 2026-11-20 | (dari Stitch) **Batas 5.000 entri per jenis catatan** (penjualan, restock, penyesuaian stok, pengeluaran kustom, pemasukan kustom), entri terlama dihapus otomatis; peringatan 90% + notifikasi. Dampak teknis: K-22, K-23. |
| D-16 | 2026-11-20 | (dari Stitch) **Palet warna & gaya visual resmi**: lihat [03-aturan-compose-ui.md](03-aturan-compose-ui.md) §7. |
| D-13 | 2026-11-20 | Tidak ada masalah `.env` (isinya bukan kredensial asli). |

### Jawaban pertanyaan terbuka (2026-11-20)

| ID | Keputusan |
|---|---|
| K-01 | **(a)** API di dalam library daftar putih **boleh** dipakai walau belum muncul di modul (`NavigationBar`, `ModalBottomSheet`, `Snackbar`, `AlertDialog`, `ExposedDropdownMenuBox`, `LazyVerticalGrid`, `Canvas`, `SharedFlow`, `db.withTransaction`, dll.). |
| K-02 | **(a)** Dependency dirakit **seperti modul**: `remember { ... }` di root composable `AuraApp()` (pola `NavigationLabApp`). Tanpa `Application` class / `AppContainer`. |
| K-03 | **(b)** **minSdk 26** + `java.time`. |
| K-04 | **(a)** Struktur paket UI **seperti modul**: semua Screen di `ui/screens/`, ViewModel + UiState di `ui/<fitur>/`. |
| K-06 | **(a)** Jumlah stok **selalu bilangan bulat** (`Int`/`Long`). Barang curah memakai satuan kecil (gram, ml), mis. "250 gram". Tidak ada stok desimal. |
| K-05 | **(a)** Uang `Long` rupiah; harga per item & HPP **dibulatkan ke rupiah terdekat**; total pengeluaran tetap persis sesuai input. |
| K-07 | **(b)** Selama pengembangan (sebelum server siap), login diuji dengan **autentikasi lokal sementara di Room** (sandi di-hash). Berlaku mulai Tahap 1. |
| K-08 | **(a)** ID entitas `String` UUID. |
| K-09 | **Setuju:** `fallbackToDestructiveMigration` hanya selama pengembangan; sebelum demo dengan data asli diganti migrasi + `exportSchema = true`. |
| K-10 | **(a)** Dynamic color **mati**; warna dari tema AURA. |
| K-11 | **(a)** Selalu **WIB** (`Asia/Jakarta`). |
| K-12 | **(b)** Teks UI di **`res/values/strings.xml`**. |
| K-13 | **(a)** Layar sementara **"Pilih peran (dev)"** (Owner / Kasir) khusus build debug di Tahap 0, dihapus di Tahap 1. |
| K-14 | ~~Diskon ditentukan oleh akun Owner~~ → digantikan K-21 (tanpa diskon). |
| K-15 | **(a)** Rata-rata tertimbang: `HPP baru = (stokLama × HPPlama + jumlahMasuk × hargaPerItem) / (stokLama + jumlahMasuk)`. Jika stok lama ≤ 0 → HPP = harga per item restock tersebut. Stok awal tanpa harga beli awal → HPP awal = 0. |
| K-17 | **(a)** Penyesuaian Stok **selalu** pergerakan jenis **Penyesuaian**; alasan (Rusak/Hilang/Kadaluarsa/Salah catat/Lainnya) disimpan sebagai alasan. |
| K-20 | **Wajib online.** Semua aksi tulis (termasuk transaksi Kasir) butuh internet; tanpa internet pencatatan dinonaktifkan + pesan. Room hanya cache baca. **Dihapus:** mode offline Kasir, antrean sinkron, status "Perlu Ditinjau", tombol "Konfirmasi Selisih". Nomor transaksi diberikan server. |
| K-21 | **Tanpa diskon** (Stitch "no discounts" + user: UMKM kemungkinan tidak memakai). Baris "discount" di prompt Stitch layar 9 tidak dipakai. Tidak ada kolom diskon di skema. |
| K-22 | **Setuju.** **Stok saat ini, HPP rata-rata, dan saldo disimpan sebagai nilai berjalan** (`stokSaatIni` & `hppRataRata` di Barang, `saldoKumulatif` di Toko) yang diperbarui di setiap pencatatan dalam satu transaksi database, **bukan** dihitung ulang dari seluruh riwayat (riwayat lama bisa terhapus, D-15). Pergerakan tetap menyimpan `stokSebelum → stokSesudah`. Aturan Revisi 2 "stok = jumlah seluruh pergerakan" diganti dengan ini. Stok tetap **tidak bisa diedit langsung** oleh user. |
| K-18 | **Setuju:** "Masuk dengan Google" & "Lupa kata sandi" ditangani server sendiri; library login Google boleh ditambahkan saat fiturnya dikerjakan (versi dicatat di tabel di bawah ketika ditambahkan). |

### Library tambahan yang sudah disetujui
| Library | Versi | Untuk fitur | Tanggal |
|---|---|---|---|
| Library login Google (Credential Manager / Google Identity) | ditentukan saat Tahap 1 | Masuk dengan Google (K-18) | 2026-11-20 (disetujui prinsip) |

## B. Pertanyaan terbuka — perlu jawaban user

| ID | Pertanyaan | Tahap | Status |
|---|---|---|---|
| K-23 | Saat catatan terlama dihapus otomatis (batas 5.000): apa yang terjadi pada **pergerakan stok** terkait, item transaksi, pengeluaran Pembelian dari restock, dan **log Riwayat Aktivitas**? Opsi: a) ikut terhapus bersama induknya · b) tetap disimpan tanpa batas · c) punya batas 5.000 sendiri. Batas dijalankan di server, app, atau keduanya? **User (2026-11-20): belum diputuskan.** Tahap 0 tidak membuat logika retensi; skema dibuat agar ketiga opsi tetap mungkin (setiap pergerakan/log menyimpan `referensiId` ke catatan induknya) | 1 | MENUNGGU |
| K-24 | Setelah server ada, **di mana rantai efek & validasi** (stok tidak negatif, dua Kasir menjual barang terakhir, HPP, batas 5.000) dijalankan: di server (app hanya kirim permintaan) atau di app (server hanya menyimpan)? Bergantung pada K-16 | 1 | MENUNGGU |
| K-25 | Tombol ikon **hapus** per baris di Daftar Barang (Stitch layar 18): artinya **Arsipkan** (barang yang pernah bertransaksi) / **Hapus permanen** (yang belum pernah), sesuai aturan Revisi 2? Atau yang lain? | 1 | MENUNGGU |
| K-16 | Kontrak API server sendiri (endpoint, format, autentikasi, upload file) | 1 | MENUNGGU |
| K-19 | Push notification tanpa Firebase — pilihan di bawah | 2 | MENUNGGU |

### Penjelasan K-19 — push notification tanpa Firebase
- Di Android, **FCM memang standar de facto**. Aplikasi yang sedang tertutup praktis hanya bisa "dibangunkan" lewat FCM karena Android membatasi koneksi latar belakang (Doze).
- Alternatif tanpa Firebase:
  1. **Notifikasi lokal saat sinkron**: app mengambil data dari server saat dibuka / berjalan, lalu menampilkan notifikasi lewat `NotificationManager` (bawaan Android) + Pusat Notifikasi di app. Sederhana dan sesuai modul, tapi tidak real-time saat app tertutup. Sinkron periodik di latar belakang (min. 15 menit) butuh WorkManager (perlu izin library).
  2. **WebSocket dari server sendiri** selama app terbuka (OkHttp sudah ikut terpasang bersama Retrofit). Real-time hanya saat app terbuka.
  3. **UnifiedPush / ntfy self-hosted**: butuh aplikasi distributor terpisah di HP pengguna, kurang praktis untuk UMKM.
  4. **FCM khusus untuk push saja** (tanpa menyimpan data di Firebase), tapi bertentangan dengan D-03.
- Catatan: notifikasi **Tahap 1** (stok menipis/habis) dihitung di perangkat sendiri, jadi cukup Pusat Notifikasi + notifikasi lokal, tanpa push. Setelah Manajer dihapus, push hanya relevan untuk **"Ajukan pembatalan" dari Kasir ke Owner** dan notifikasi anomali/batas data.
