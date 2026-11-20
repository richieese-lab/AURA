# AURA — Instruksi untuk Claude Code

Aplikasi Android **AURA** (inventori UMKM retail), Kotlin + Jetpack Compose, package `com.aura.app`.

**Sebelum mengerjakan apa pun, baca [agents/README.md](agents/README.md)** dan ikuti urutan baca di sana. Panduan lengkap (keputusan, batasan library, arsitektur, aturan Compose, spesifikasi, rencana per tahap) ada di folder `agents/`.

Aturan yang paling sering dilanggar:
1. **Jangan menebak atau membuat interpretasi sendiri.** Kalau spesifikasi ambigu atau tidak tercakup, tanyakan ke user, lalu catat jawabannya di [agents/00-keputusan-proyek.md](agents/00-keputusan-proyek.md).
2. **Tetap dalam batasan modul praktikum PAPB.** Hanya library di daftar putih ([agents/01-stack-dan-batasan-modul.md](agents/01-stack-dan-batasan-modul.md)). Library baru harus diizinkan user dulu. Tanpa Hilt, tanpa Firebase.
3. **Kerjakan hanya tahap yang aktif** (lihat `agents/plan/`). Perubahan besar → buat rencana dan tunggu persetujuan.
4. Pakai istilah baku: Barang, Supplier, Stok, Pemasukan, Pengeluaran, Restock, Owner, Kasir. Hanya ada dua peran: **Owner & Kasir** (Manajer dihapus).
5. Laporkan hasil `.\gradlew.bat :app:assembleDebug` dan `.\gradlew.bat :app:testDebugUnitTest` apa adanya.
