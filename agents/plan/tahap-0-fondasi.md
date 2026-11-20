# Rencana Tahap 0 — Fondasi

> Status (2026-11-21): **Langkah 1–3 SELESAI** (build OK, 43 unit test lulus, skema Room terkompilasi; test DAO `AppDatabaseTest` sudah ditulis tapi **belum dijalankan** karena tidak ada emulator). **Langkah 4–7 belum dimulai, menunggu perintah user.** K-23 (retensi data turunan) belum diputuskan, tidak menghambat.

## Tujuan
Menyiapkan kerangka aplikasi yang **pasti tidak berubah** walaupun mockup Figma belum selesai: build, struktur paket, tema dasar, navigasi + aturan peran, skema data lengkap, repository, dan logika bisnis inti yang teruji. **Tidak** ada login, sinkron server, atau UI final.

## Di luar lingkup Tahap 0
Login/registrasi/sesi nyata, Retrofit/server, upload gambar, scan barcode, grafik, ML, persetujuan (Tahap 2), notifikasi push, desain visual final.

---

## Langkah 1 — Setup proyek Gradle di root repo
1. **Salin wrapper dari `modul9-PAPB/`** (sebelum folder modul dihapus): `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`, `gradle/wrapper/gradle-wrapper.properties` (Gradle 9.6.0), `gradle/gradle-daemon-jvm.properties`.
2. Buat `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`, `gradle/libs.versions.toml`, `app/build.gradle.kts` sesuai template [../01-stack-dan-batasan-modul.md](../01-stack-dan-batasan-modul.md) §6 (minSdk 26).
   - Tambahkan `CLAUDE.md` di root yang mengarah ke `agents/README.md` (sudah dibuat saat dokumen agents).
3. `.gitignore` root: `.gradle/`, `build/`, `app/build/`, `local.properties`, `.idea/`, `*.iml`, `.env`, `hs_err_pid*.log`, `replay_pid*.log`, `.kotlin/`.
4. `AndroidManifest.xml` (INTERNET, theme `Theme.Aura`, `MainActivity` `adjustResize`), `res/values/themes.xml`, ikon launcher bawaan.
5. **Verifikasi:** `.\gradlew.bat :app:assembleDebug` berhasil dengan `MainActivity` kosong.

## Langkah 2 — Logika bisnis murni (`core/`) + unit test
Kotlin murni tanpa `android.*`, dites dengan JUnit4 di `app/src/test/java/com/aura/app/core/...`.

| Kelas | Isi | Test minimal |
|---|---|---|
| `core/model/*` | `enum Peran { OWNER, KASIR }` (D-14), `StatusStok { AMAN, MENIPIS, HABIS, DIARSIPKAN }`, `JenisPergerakan { MASUK, KELUAR, PENYESUAIAN }`, `SumberPergerakan { RESTOCK, STOK_AWAL, RETUR_PELANGGAN, PEMBATALAN_PENJUALAN, PENJUALAN, RUSAK, HILANG, KADALUARSA, STOCK_OPNAME }` (sesuai daftar dokumen; Penyesuaian Stok selalu `PENYESUAIAN` + `STOCK_OPNAME`, alasan di `AlasanPenyesuaian { RUSAK, HILANG, KADALUARSA, SALAH_CATAT, LAINNYA }`, K-17), `MetodePembayaran { TUNAI, TRANSFER, QRIS }`, `StatusTransaksi { SELESAI, DIBATALKAN, DIRETUR }`, `StatusAkun`, `StatusBarang`, `JenisPengeluaran { PEMBELIAN, KUSTOM }`, `StatusCatatanKeuangan { AKTIF, DIBATALKAN }`, `JenisCatatanTerbatas { PENJUALAN, RESTOCK, PENYESUAIAN_STOK, PENGELUARAN_KUSTOM, PEMASUKAN_KUSTOM }` (D-15) | — |
| `StokCalculator` | stok baru = stok saat ini + jumlah pergerakan (bertanda) (K-22); selisih penyesuaian = stok fisik − stok sistem; menolak hasil negatif & jumlah keluar > stok | masuk/keluar/penyesuaian; tepat 0; menolak negatif; selisih positif/negatif/nol |
| `StatusStokEvaluator` | Diarsipkan → DIARSIPKAN; stok = 0 → HABIS; stok ≤ minimum → MENIPIS; selain itu AMAN (stok minimum 0 ⇒ peringatan hanya saat stok 0) | batas tepat = minimum; minimum 0; stok 0; arsip menang |
| `HppCalculator` | harga per item = total / jumlah, dibulatkan ke rupiah terdekat (K-05); HPP baru = `(stokLama × HPPlama + jumlahMasuk × hargaPerItem) / (stokLama + jumlahMasuk)` dibulatkan; stok lama ≤ 0 → HPP = harga per item; stok awal tanpa harga beli → 0 (K-15) | jumlah 0 ditolak; pembagian tak bulat (100.000 / 3 → 33.333); stok lama 0 dan negatif; urutan beberapa restock |
| `IdTokoGenerator` | 8 karakter `[A-Z0-9]` | panjang 8, hanya karakter valid (generator acak disuntikkan agar deterministik di test) |
| `NomorTransaksiGenerator` | `INV-YYMMDD-NNNN`, urut per toko per hari (tanggal WIB). Nomor final diberikan **server** (K-20); generator ini dipakai selama pengembangan tanpa server | format; reset hari baru; padding 4 digit |
| `RupiahFormatter` | `Rp 1.250.000` (titik ribuan) | 0, ribuan, jutaan, negatif |
| `TanggalFormatter` | `17 Nov 2026, 19.48 WIB` (nama bulan Indonesia singkat; selalu WIB `Asia/Jakarta`, `java.time`) | format, tengah malam, pergantian tahun |
| `JumlahStokFormatter` | angka bulat + satuan, titik ribuan (`12 pcs`, `25.000 gram`) (K-06) | ribuan, 0 |
| `core/access/AksesPeran` | peta elemen/aksi → peran yang diizinkan + alasan (Owner & Kasir, [../spec/00-perubahan-dari-stitch.md](../spec/00-perubahan-dari-stitch.md) §1) | Owner boleh semua; Kasir hanya graph Kasir (tanpa bottom nav), tombol kembali K1 abu-abu dengan alasan "Kasir hanya dapat keluar melalui profil avatar", tidak bisa membuka Dashboard/Manajemen lain/Analisis/Pengaturan/Riwayat Aktivitas, hanya "Ajukan pembatalan" |

## Langkah 3 — Skema Room (`data/local/`)
Satu `AppDatabase` versi 1 + `DatabaseProvider` (pola Modul 9). ID `String` UUID (K-08), uang `Long` (K-05), jumlah stok bilangan bulat (K-06).

| Entity (tabel) | Field utama | Catatan |
|---|---|---|
| `TokoEntity` (`toko`) | idToko (8 char), nama (Nama Bisnis), bidangBisnis?, alamat?, logoPath?, saldoAwal, **saldoKumulatif** (K-22), qrisPath?, namaPenerima?, infoRekening?, createdAt | satu Owner per toko |
| `AkunEntity` (`akun`) | id, idToko, peran, namaAkun, namaTampil, email?, noHp?, fotoPath?, status (AKTIF/NONAKTIF), isDihapus, wajibGantiSandi, lastLoginAt?, createdAt | unik (idToko, namaAkun); hapus lunak |
| `BarangEntity` (`barang`) | id, sku?, fotoPath?, nama, satuan, hargaJual, **stokSaatIni**, **hppRataRata**, stokMinimum, status (AKTIF/DIARSIPKAN), dibuatOleh, createdAt | `stokSaatIni` & `hppRataRata` = nilai berjalan (K-22), hanya diubah oleh repository bersama pergerakannya |
| `KategoriEntity` + `BarangKategoriCrossRef` | id, nama / (barangId, kategoriId) | banyak-ke-banyak; hapus kategori melepas relasi saja |
| `SupplierEntity` (`supplier`) | id, nama, link?, catatan?, status (AKTIF/NONAKTIF), createdAt | hapus hanya bila belum dipakai |
| `HargaBeliSupplierEntity` | barangId, supplierId, hargaPerItem, tanggal | diperbarui tiap Restock |
| `PergerakanStokEntity` (`pergerakan_stok`) | id, barangId, jenis, sumber, jumlah (bertanda), stokSebelum, stokSesudah, referensiId?, alasan?, catatan?, buktiPath?, akunId, waktu | **append-only** |
| `TransaksiPenjualanEntity` | id, nomor, kasirId, metodeBayar, total, uangDiterima?, kembalian?, catatan?, status (SELESAI/DIBATALKAN/DIRETUR), alasanBatal?, waktu | tanpa diskon (K-21), tanpa perluDitinjau (K-20); dihapus hanya oleh retensi 5.000 |
| `ItemTransaksiEntity` | id, transaksiId, barangId, namaBarangSnapshot, jumlah, hargaJualSnapshot, hppSnapshot | **append-only**, snapshot |
| `PengeluaranEntity` | id, jenis (PEMBELIAN/KUSTOM), judul?, nominal, catatan?, buktiPath?, supplierId?, barangId?, jumlahBarang?, hargaPerItem?, pergerakanId?, tanggal, dicatatOleh, status, alasanBatal? | tidak dihapus |
| `PemasukanKustomEntity` | id, judul, nominal, catatan?, buktiPath?, tanggal, dicatatOleh, status, alasanBatal? | Pemasukan Penjualan = turunan dari transaksi (bukan tabel) |
| `NotifikasiEntity` | id, jenis, judul, isi, referensiId?, untukAkunId?, dibaca, waktu | |
| `LogAktivitasEntity` (`log_aktivitas`) | id, akunId, namaAkunSnapshot, peranSnapshot, kategori (Barang/Stok/Keuangan/Penjualan/Akun/Persetujuan/Login), ringkasan, nilaiSebelum?, nilaiSesudah?, namaPerangkat?, statusPersetujuan?, waktu | **append-only** |

DAO per entity: `Flow` untuk observe, `suspend` untuk tulis. Query turunan penting yang disiapkan: jumlah entri per jenis catatan terbatas (untuk banner 90% & retensi), daftar Stok Menipis/Habis diurutkan paling mendesak, kartu stok per barang, daftar pergerakan global terfilter periode/jenis.

**Test:** DAO diuji di `app/src/androidTest/` dengan `Room.inMemoryDatabaseBuilder` (butuh emulator; library sudah ada di daftar putih).

## Langkah 4 — Repository (`data/repository/`)
Interface + `Default*Repository` di atas Room, rantai efek dalam `db.withTransaction { }`:
- `BarangRepository`: tambah barang (+ pergerakan **Masuk - Stok awal**, tanpa pengeluaran), edit (tanpa ubah stok), arsip / hapus permanen bila belum pernah bertransaksi, observe daftar + stok + status.
- `KategoriRepository`, `SupplierRepository` (CRUD sesuai aturan hapus/nonaktif).
- `PergerakanStokRepository`: observe global & per barang (kartu stok).
- `RestockRepository`: simpan restock → pergerakan Masuk-Restock, pengeluaran Pembelian, harga beli supplier, hitung ulang HPP, log aktivitas.
- `PenyesuaianStokRepository`: simpan penyesuaian sebagai jenis `PENYESUAIAN` + alasan (K-17), log aktivitas. Tingkat kuning/merah = Tahap 2.
- `LogAktivitasRepository` (append-only), `PreferensiRepository` (DataStore: sembunyikan nominal, peran dev K-13).
- Repository untuk Kasir/transaksi, keuangan kustom, notifikasi: **interface + skema** disiapkan; implementasi alur penuh di Tahap 1.

## Langkah 5 — Tema & komponen dasar (`ui/theme`, `ui/components`)
- `AuraTheme` (struktur Theme.kt modul, dynamic color **mati** K-10) dengan **palet resmi Stitch** ([../03-aturan-compose-ui.md](../03-aturan-compose-ui.md) §7), `StatusColors`, teks di `strings.xml` (K-12).
- Komponen bersama: `StatusStokChip`, pembungkus elemen berperan (abu-abu + snackbar alasan), `LoadingState`, `EmptyState`, `ErrorState`, `NoAccessState`.

## Langkah 6 — Navigasi kerangka (`navigation/`)
- `Routes` (string), `AuraNavHost`, `AuraBottomBar` 5 posisi: **Dashboard, Manajemen, (+) Kasir, Analisis, Profil**; tombol (+) bulat merah menonjol, bukan tab, hanya untuk Owner.
- Nested graph per tab dengan `saveState/restoreState` (Profil Barang dari Dashboard tetap di stack Dashboard).
- Peran **Kasir** → langsung ke graph Kasir **tanpa** bottom nav; tombol kembali abu-abu.
- Semua layar = **placeholder** (judul + keterangan "menunggu desain") dengan 4 kondisi daftar bila relevan.
- Layar sementara **"Pilih peran (dev)"** (Owner / Kasir) khusus build debug (K-13), menyimpan peran di DataStore, dihapus di Tahap 1.

## Langkah 7 — Verifikasi & laporan
- `.\gradlew.bat :app:assembleDebug` dan `.\gradlew.bat :app:testDebugUnitTest` → laporkan hasil apa adanya.
- Test DAO (`connectedDebugAndroidTest`) bila emulator tersedia; jika tidak, laporkan bahwa belum dijalankan.
- Perbarui `00-keputusan-proyek.md` bila ada keputusan baru.

## Risiko
- K-23 (data turunan saat retensi) belum diputuskan → setiap pergerakan & log menyimpan `referensiId`/`jenisReferensi` ke catatan induk agar opsi a/b/c tetap bisa diterapkan di Tahap 1.
- Nilai berjalan (K-22) bisa tidak sinkron jika ada penulisan di luar repository → semua perubahan stok/saldo/HPP **wajib** lewat repository dalam satu `withTransaction`; tambahkan test yang memeriksa `stokSaatIni == stokSesudah` pergerakan terakhir.
- Karena wajib online (K-20), tidak ada kolom antrean sinkron (`syncStatus`).
- Sinkronisasi server (D-04) belum punya kontrak API; skema lokal mungkin perlu kolom tambahan (mis. `syncStatus`, `updatedAt`) saat Tahap 1. Pakai destructive migration selama pengembangan (K-09).
