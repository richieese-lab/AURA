# 04 — Alur Kerja Agent (Claude Code)

## 1. Prinsip utama dari user

1. **Jangan membuat interpretasi sendiri.** Jika spesifikasi ambigu, bertentangan, atau tidak tercakup → **tanyakan ke user**, jangan menebak. Catat jawabannya di [00-keputusan-proyek.md](00-keputusan-proyek.md).
2. **Tetap dalam batasan modul praktikum** ([01-stack-dan-batasan-modul.md](01-stack-dan-batasan-modul.md)). Library baru = minta izin dulu.
3. **Kerjakan hanya tahap yang sedang aktif** (lihat [spec/11-prioritas-pengembangan.md](spec/11-prioritas-pengembangan.md) dan `plan/`). Jangan mengerjakan fitur tahap berikutnya tanpa diminta.
4. **Istilah baku** dokumen dipakai di kode, UI, dan commit.

## 2. Urutan baca sebelum mengerjakan tugas

1. [README.md](README.md) → indeks.
2. [00-keputusan-proyek.md](00-keputusan-proyek.md) → keputusan final + yang masih terbuka.
3. [01](01-stack-dan-batasan-modul.md), [02](02-arsitektur-dan-konvensi-kode.md), [03](03-aturan-compose-ui.md) → aturan teknis.
4. File `spec/` yang relevan dengan fitur.
5. File `plan/` tahap yang aktif.

## 3. Sebelum perubahan besar → rencana dulu

Perubahan dianggap besar bila menyentuh: banyak paket, navigasi, skema Room, autentikasi/sesi, jaringan, build Gradle, atau menambah layar/alur/lapisan/izin baru. Buat rencana singkat berisi:
- file/paket yang akan berubah,
- pendekatan dan trade-off,
- alur data/state,
- edge case & skenario gagal,
- cara uji & perintah verifikasi,
- risiko / pertanyaan terbuka,

lalu **tunggu persetujuan user**. Setelah disetujui, tetap dalam lingkup rencana; jika muncul lingkup baru, berhenti dan jelaskan.

## 4. Definisi selesai (Definition of Done)

- [ ] Sesuai spesifikasi di `spec/` (atau keputusan di `00-keputusan-proyek.md`).
- [ ] Hanya memakai library daftar putih (atau yang sudah disetujui).
- [ ] Mengikuti pola di `02` dan checklist Compose di `03` §11.
- [ ] Logika bisnis baru punya unit test JUnit4 di `app/src/test/`, termasuk edge case (stok 0, stok minimum 0, jumlah > stok, nilai negatif, pembulatan).
- [ ] `./gradlew :app:assembleDebug` dan `./gradlew :app:testDebugUnitTest` dijalankan; hasilnya (lulus/gagal) dilaporkan apa adanya.
- [ ] Tidak ada secret yang ter-commit.

## 5. Perintah verifikasi (Windows, dari root repo)

```
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:connectedDebugAndroidTest   # butuh emulator/perangkat; untuk test DAO Room
```
Jalankan yang paling kecil dan relevan dulu, baru yang lebih luas bila kode bersama ikut berubah.

## 6. Git

- Commit/push **hanya bila user meminta**.
- Pesan commit dalam Bahasa Indonesia, singkat, menyebut fitur dengan istilah baku.
- Jangan commit `.env`, `local.properties`, folder `build/`, `.gradle/`, `.idea/` (pastikan ada di `.gitignore`).

## 7. Memperbarui dokumen agents

- Setiap keputusan baru dari user → tambahkan ke `00-keputusan-proyek.md` (tanggal + isi).
- Jika keputusan mengubah spesifikasi → perbarui file `spec/` terkait dan beri catatan "Pembaruan keputusan (tanggal)".
- Jangan menghapus isi spesifikasi asli; tandai yang berubah.
