# 03 — Aturan Jetpack Compose & UI

> Gabungan pola modul + aturan dari `compose-skill` (aldefy) dan `android-agent-project-kit` yang **tidak** butuh library baru. Spesifikasi perilaku UI ada di [spec/02-konvensi-ui.md](spec/02-konvensi-ui.md).

## 1. Status desain

- **Mockup Figma belum selesai** (dikerjakan anggota tim lain lewat Google Stitch). Yang **sudah resmi**: palet warna & gaya global (§7, dari [spec/12-prompt-stitch-desain.md](spec/12-prompt-stitch-desain.md)). Sampai Figma final:
  - Layar dibuat **fungsional + placeholder sederhana** dengan Material3 + palet resmi.
  - **Jangan** menebak font, ukuran detail, atau tata letak yang belum ada di Figma. Deskripsi layar di prompt Stitch boleh dipakai sebagai acuan isi & urutan elemen, bukan ukuran piksel.
  - Semua warna/typography lewat `MaterialTheme` / token di `ui/theme/` supaya restyle nanti cukup di satu tempat.
- Saat Figma sudah ada: lakukan **pass pengukuran** sebelum coding (ukuran, padding, jarak, ukuran ikon/teks dari frame referensi), buat `@Preview` dengan ukuran frame yang sama, lalu bandingkan visual sebelum dianggap selesai.

## 2. Struktur composable

- **Screen stateless:** `XxxScreen(uiState, onEvent..., modifier = Modifier)`. ViewModel hanya disentuh di `composable {}` NavHost (pola modul).
- Setiap composable publik punya `modifier: Modifier = Modifier` sebagai **parameter opsional pertama**, dipasang di elemen root.
- Urutan modifier itu bermakna (`padding().clickable()` ≠ `clickable().padding()`).
- Pakai `Scaffold` untuk layar dengan app bar / bottom bar / snackbar (sudah dipakai di modul).
- `safeDrawingPadding()` / `paddingValues` dari Scaffold untuk edge-to-edge (MainActivity memanggil `enableEdgeToEdge()` seperti modul).
- Tambahkan `@Preview(showBackground = true)` dengan data dummy statis untuk komponen penting.

## 3. State

- Kumpulkan Flow dengan **`collectAsStateWithLifecycle()`**, bukan `collectAsState()`.
- Input form yang harus bertahan saat rotasi → `rememberSaveable`. State UI sementara (dropdown terbuka, dsb.) → `remember { mutableStateOf(...) }`.
- State diletakkan **serendah mungkin**, di-hoist hanya setinggi yang perlu. Tidak semua hal harus masuk ViewModel.
- `derivedStateOf` hanya untuk nilai turunan yang sering berubah (mis. posisi scroll → tombol "ke atas"); **jangan** untuk ukuran koleksi yang dipakai `LazyColumn`.
- Efek samping: `LaunchedEffect(key)` untuk memuat data berdasarkan argumen (pola modul), event sekali jalan (snackbar/navigasi) dikumpulkan di satu `LaunchedEffect`.

## 4. Daftar (lists)

- Selalu `LazyColumn`/`LazyRow`/`LazyVerticalGrid` (grid barang di K1 Kasir).
- `items(list, key = { it.id })` dengan **key stabil & unik** (ID entitas). Jika daftar mencampur beberapa jenis baris (mis. header "Hari ini / Sebelumnya" di notifikasi), beri prefiks key per jenis + `contentType`.
- Jangan `indexOf()` di dalam `items {}`; gunakan `itemsIndexed` bila butuh indeks.

## 5. Empat kondisi layar daftar (WAJIB)

Setiap layar daftar menampilkan salah satu: **memuat**, **kosong** (ilustrasi + ajakan aksi), **error/offline** (banner + "Coba lagi"), **tanpa izin** (pesan peran). Buat komponen bersama di `ui/components/` (`LoadingState`, `EmptyState`, `ErrorState`, `NoAccessState`) dan pakai di semua daftar.

## 6. Elemen berbasis peran (abu-abu)

Implementasi konvensi UI #7–#10:
- Ada satu sumber aturan di `core/access/` yang memetakan **elemen/aksi → daftar peran yang diizinkan + teks alasan**.
- Komponen pembungkus di `ui/components/` menerima `diizinkan: Boolean` + `alasan: String`:
  - diizinkan → perilaku normal;
  - tidak diizinkan → **tetap tampil**, warna abu-abu/redup, tanpa efek tekan (ripple), ketukan **tidak** navigasi dan memunculkan **snackbar berisi alasan**; untuk field input → baca saja.
- Peran hanya **Owner** dan **Kasir** (Manajer dihapus). Teks alasan baku: **"Hanya Owner yang dapat mengubah data ini"**; tombol kembali K1 untuk Kasir: **"Kasir hanya dapat keluar melalui profil avatar"**.
- Barang stok 0 di grid Kasir: abu-abu, badge "Habis", tidak bisa ditekan/dimasukkan keranjang.
- Pengecekan yang sama **juga dijalankan di repository** (bukan hanya tampilan).

## 7. Tema & warna status

- `AuraTheme` mengikuti struktur Theme.kt modul (`lightColorScheme`/`darkColorScheme`, `Typography`), tetapi **dynamic color dimatikan** (K-10).
- **Palet resmi (D-16, dari Prompt Stitch)**, didefinisikan sekali di `ui/theme/Color.kt`:

| Token | Hex | Dipakai untuk |
|---|---|---|
| Primary: Crimson Red | `#930000` | Tombol aksi utama (CTA), tombol (+) tengah, tombol ikon, badge aktif, status Habis |
| Secondary: Deep Aubergine | `#311A37` | Header, aksen brand |
| Gradien auth | `#311A37` (atas) → `#5C0E1E` → `#930000` (bawah), vertikal | Splash, Onboarding, Login, Registrasi ("Isi Profil") |
| Surface / background | `#FFFFFF` | Latar layar internal & kartu |
| Border kartu | `#E5E7EB` (1px) | Kartu bersudut **16dp** |
| Teks utama | `#1F1F1F` | |
| Teks sekunder / hint | `#757575` | |
| Tag kategori | `#C86225` (Warm Amber) | Label kategori barang |
| Ikon tab bottom nav | `#525252` (outline abu) | |
| Status **Aman** | `#16A34A` | |
| Status **Menipis** | `#D97706` | |
| Status **Habis** | `#930000` | |
| Status **Diarsipkan/Nonaktif** | `#9E9E9E` | |

- Gaya: bersih, ramah, profesional; kartu bersudut 16dp, spasi lega; tombol utama berbentuk pill. Di layar bergradien, tombol utama putih dengan teks merah `#930000`, field berbentuk pill dengan isi transparan dan border putih.
- Status stok **selalu** tampil sebagai warna + label + ikon (bukan warna saja), lewat token terpusat `StatusColors`.
- Dark theme: **belum ditentukan** oleh desain. Sementara hanya tema terang (tanyakan sebelum membuat tema gelap).
- Ikon: vector drawable XML di `res/drawable` + `painterResource` selama `material-icons-extended` belum diizinkan.

## 8. Format tampilan (pakai formatter di `core/format/`, jangan format manual di composable)

- Nominal: `Rp 1.250.000`.
- Tanggal: `17 Nov 2026, 19.48 WIB`, selalu zona **WIB / `Asia/Jakarta`** (K-11), memakai `java.time`.
- Stok selalu bilangan bulat dengan satuan (K-06): `12 pcs`, `250 gram`, `25.000 gram`.
- Nominal Saldo/Pemasukan/Pengeluaran di Dashboard default `Rp ••••••`; pilihan pengguna diingat (DataStore).

## 9. Pola interaksi baku

- Aksi destruktif (hapus, arsip, batalkan transaksi) → `AlertDialog` konfirmasi dengan kata kerja jelas ("Arsipkan", bukan "OK").
- Aksi ringan → `Snackbar` dengan tombol **Urungkan**.
- "Buat baru di tempat" (kategori, supplier) → textfield di bawah dropdown; saat fokus/mengetik, tombol **Buat** dan **Batal** muncul di bawahnya. **Bukan pop up/dialog.**
- `FilterPeriode` → **satu komponen bersama** dipakai di Pengeluaran, Pemasukan, Pergerakan Stok, Riwayat Aktivitas, Analisis.

## 10. Aksesibilitas

- `contentDescription` untuk ikon/gambar yang bermakna (null untuk dekorasi).
- Target sentuh minimal **48dp**.
- Teks UI dalam Bahasa Indonesia dan disimpan di **`res/values/strings.xml`** (K-12), dipanggil dengan `stringResource(R.string.xxx)`. Pesan dari ViewModel memakai ID resource (`@StringRes Int`), bukan teks mentah, agar ViewModel tidak bergantung pada `Context`.

## 11. Checklist sebelum menyerahkan kode Compose

Cari di file yang diubah:
- [ ] `collectAsState(` → ganti `collectAsStateWithLifecycle()`
- [ ] `LazyColumn`/`LazyRow` tanpa `key` untuk item dinamis
- [ ] composable publik tanpa parameter `modifier`
- [ ] warna/ukuran teks hardcode di luar `ui/theme/`
- [ ] teks UI hardcode (harus lewat `strings.xml`)
- [ ] logika bisnis / akses DB di dalam composable
- [ ] ikon/gambar bermakna tanpa `contentDescription`
- [ ] secret/kredensial hardcode
