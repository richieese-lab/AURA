# 02 — Arsitektur & Konvensi Kode

> Pola di sini **diambil dari modul praktikum** (terutama Modul 8–9) lalu dirapikan dengan panduan dari repo penunjang (`awesome-android-agent-skills`, `android-agent-project-kit`, `compose-skill`) **sejauh tidak bertentangan dengan modul**. Hal yang masih menunggu keputusan user ditandai **K-xx** (lihat [00-keputusan-proyek.md](00-keputusan-proyek.md)).

## 1. Lapisan & alur data

```
Screen (Composable stateless)  ←state—  ViewModel (StateFlow<UiState>)  ←Flow/suspend—  Repository (interface)
        │ event (lambda)                     │                                         │
        └──────────────→ ViewModel.fungsi() ─┘                              Default*Repository
                                                                              ├─ Room DAO (lokal, sumber kebenaran)
                                                                              ├─ DataStore (preferensi/sesi)
                                                                              └─ RemoteDataSource (Retrofit → server sendiri) [nanti]
```

- Alur satu arah (UDF): state turun, event naik.
- **Room = sumber kebenaran di perangkat.** UI selalu membaca dari Room (Flow). Remote hanya menyinkronkan ke/dari Room (nanti, setelah server siap).
- **Logika bisnis murni** (hitung stok, status, HPP, format, ID, nomor transaksi, aturan peran) ditulis sebagai **Kotlin murni tanpa `android.*`** di paket `core/` agar bisa dites dengan JUnit4 biasa.
- Composable **tidak boleh** berisi logika bisnis, akses database, atau kerja berat.

## 2. Struktur paket (satu modul `:app`, package `com.aura.app`)

> Mengikuti modul (K-04 = a): **semua Screen di `ui/screens/`**, ViewModel + UiState di `ui/<fitur>/`. Karena layarnya banyak, `ui/screens/` boleh dibagi subfolder per tab (`ui/screens/dashboard/`, `ui/screens/manajemen/`, ...) agar tetap rapi.

```
com.aura.app
├── MainActivity.kt                 // enableEdgeToEdge + setContent { AuraTheme { AuraApp() } }
├── AuraApp.kt                      // root composable: rakit dependency dengan remember { } + AuraNavHost (pola NavigationLabApp, K-02)
├── navigation/
│   ├── Routes.kt                   // object Routes { const val ...; fun builder(id) } (pola modul)
│   ├── AuraNavHost.kt
│   └── AuraBottomBar.kt            // 5 posisi: Dashboard, Manajemen, (+), Analisis, Profile
├── core/                           // Kotlin murni, TANPA android.*
│   ├── model/                      // enum & model domain: Peran, StatusStok, JenisPergerakan, SumberPergerakan, ...
│   ├── logic/                      // StokCalculator, StatusStokEvaluator, HppCalculator, IdTokoGenerator, NomorTransaksiGenerator
│   ├── format/                     // RupiahFormatter, TanggalFormatter
│   └── access/                     // AksesPeran: elemen → peran yang diizinkan + alasan snackbar
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt          // @Database + object DatabaseProvider (pola Modul 9)
│   │   ├── entity/                 // *Entity + mapper toDomain()/toEntity()
│   │   ├── dao/                    // *Dao: Flow untuk observe, suspend untuk tulis
│   │   └── converter/              // @TypeConverter enum ↔ String
│   ├── preferences/                // DataStore: sesi, preferensi tampilan (pola UserPreferencesRepository)
│   ├── remote/                     // RetrofitClient, api/, dto/, datasource/ — DIISI setelah server siap
│   └── repository/                 // interface XxxRepository + class DefaultXxxRepository
└── ui/
    ├── theme/                      // Color.kt, Theme.kt (AuraTheme), Type.kt, StatusColors
    ├── components/                 // komponen bersama: StatusStokChip, ElemenBerperan, LoadingState, EmptyState, ErrorState, NoAccessState
    ├── screens/                    // SEMUA XxxScreen.kt (boleh subfolder per tab: dashboard/, manajemen/, kasir/, analisis/, profile/, auth/)
    └── <fitur>/                    // dashboard/, kasir/, barang/, ... berisi XxxViewModel.kt + XxxUiState.kt (pola ui/home/ di modul)
```

## 3. Penamaan

- Bahasa domain mengikuti **istilah baku** dokumen (Barang, Supplier, Stok, Pemasukan, Pengeluaran, Restock, Owner, Kasir; peran Manajer sudah dihapus, D-14). Contoh: `Barang`, `BarangEntity`, `BarangDao`, `BarangRepository`, `DefaultBarangRepository`, `DaftarBarangViewModel`. **Jangan** pakai Product/Item/Vendor.
- Kata teknis Android tetap Inggris (`Screen`, `ViewModel`, `UiState`, `Dao`, `Entity`, `Repository`) — sama seperti modul (`HomeViewModel`, `FavoriteStudentDao`).
- File = satu kelas/fungsi utama, nama file = nama kelas.

## 4. Pola kode wajib (dari modul)

### 4.1 UiState
Dua bentuk yang sama-sama ada di modul:
```kotlin
// Bentuk data class (HomeUiState / DetailUiState) — untuk layar dengan banyak field
data class DaftarBarangUiState(
    val barang: List<BarangRingkas> = emptyList(),
    val isLoading: Boolean = true,
    val errorMsg: String? = null,
    val query: String = "",
)

// Bentuk sealed interface (LocationUiState) — untuk alur dengan status yang saling eksklusif
sealed interface SimpanRestockUiState {
    data object Idle : SimpanRestockUiState
    data object Loading : SimpanRestockUiState
    data class Success(val pergerakanId: String) : SimpanRestockUiState
    data class Error(val message: String) : SimpanRestockUiState
}
```
Ingat aturan dokumen: **setiap layar daftar punya 4 kondisi**: memuat, kosong, error/offline, tanpa izin. UiState harus bisa merepresentasikan keempatnya.

### 4.2 ViewModel + factory manual (TANPA Hilt)
```kotlin
class DetailBarangViewModel(
    private val barangRepository: BarangRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailBarangUiState())
    val uiState: StateFlow<DetailBarangUiState> = _uiState.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMsg = null) }
            try {
                val barang = barangRepository.getBarang(id)
                _uiState.update { it.copy(barang = barang, isLoading = false) }
            } catch (e: CancellationException) {
                throw e                                  // WAJIB dilempar ulang
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMsg = e.message ?: "Gagal memuat data", isLoading = false) }
            }
        }
    }

    companion object {
        fun factory(barangRepository: BarangRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    DetailBarangViewModel(barangRepository) as T
            }
    }
}
```
- Pakai `_uiState.update { }` (atomik), bukan `_uiState.value = _uiState.value.copy(...)`.
- Jangan pernah mengekspos `MutableStateFlow`.
- **Tidak** memakai Explicit Backing Fields (butuh Kotlin 2.3+; proyek pakai 2.2.10).

### 4.3 Data dari Room → StateFlow (pola FavoritesViewModel)
```kotlin
val uiState: StateFlow<DashboardUiState> =
    combine(barangRepository.observeStokMenipis(), preferensiRepository.sembunyikanNominal) { menipis, sembunyi ->
        DashboardUiState(stokMenipis = menipis, sembunyikanNominal = sembunyi, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState(),
    )
```

### 4.4 Navigasi (Navigation Compose 2.9.8, route string — pola Routes.kt modul)
```kotlin
object Routes {
    const val DASHBOARD = "dashboard"
    const val PROFIL_BARANG = "barang/{barangId}"
    fun profilBarang(barangId: String) = "barang/$barangId"
}
// di NavHost
composable(
    route = Routes.PROFIL_BARANG,
    arguments = listOf(navArgument("barangId") { type = NavType.StringType }),
) { backStackEntry ->
    val barangId = backStackEntry.arguments?.getString("barangId").orEmpty()
    val vm: ProfilBarangViewModel = viewModel(factory = ProfilBarangViewModel.factory(barangRepository))
    val state by vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(barangId) { vm.load(barangId) }
    ProfilBarangScreen(uiState = state, onBack = { navController.popBackStack() })
}
```
- **Tidak** memakai type-safe `@Serializable` route (butuh plugin kotlinx-serialization → di luar modul).
- Bottom nav dengan back stack per tab: `navigate(route) { popUpTo(startDestination) { saveState = true }; launchSingleTop = true; restoreState = true }` + nested graph per tab — memenuhi aturan "Profil Barang dari Dashboard tetap di tumpukan Dashboard".
- ViewModel dibuat di dalam `composable {}` dengan `viewModel(factory = ...)`; Screen menerima **state + lambda**, bukan ViewModel (kecuali pola LocationScreen modul — hindari untuk layar baru).

### 4.5 Repository (pola StudentRepository / FavoriteRepository)
```kotlin
interface BarangRepository {
    fun observeBarangAktif(): Flow<List<BarangRingkas>>
    suspend fun getBarang(id: String): Barang
    suspend fun tambahBarang(input: TambahBarangInput, olehAkunId: String): String
}

class DefaultBarangRepository(
    private val db: AppDatabase,
    private val barangDao: BarangDao,
    private val pergerakanDao: PergerakanStokDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,   // injeksi dispatcher (repo penunjang), default IO
) : BarangRepository { /* ... */ }
```
- ViewModel hanya bergantung pada **interface** → nanti implementasi remote/sinkron bisa ditambah tanpa mengubah UI.
- Fungsi suspend repository harus **main-safe**. DAO Room `suspend` sudah main-safe; pakai `withContext(ioDispatcher)` hanya untuk kerja berat non-Room.

### 4.6 Room (pola Modul 9)
```kotlin
@Database(entities = [/* ... */], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() { /* abstract fun xxxDao(): XxxDao */ }

object DatabaseProvider {
    @Volatile private var INSTANCE: AppDatabase? = null
    fun getDatabase(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "aura.db")
                .fallbackToDestructiveMigration(dropAllTables = true)   // HANYA selama pengembangan (K-09)
                .build().also { INSTANCE = it }
        }
}
```
- DAO: `Flow<...>` untuk data yang diamati, `suspend` untuk tulis/one-shot.
- **Tabel append-only** (`pergerakan_stok`, `log_aktivitas`, `item_transaksi`): DAO **hanya** punya `@Insert` + `@Query SELECT`. Dilarang `@Update`/`@Delete`.
- **Data keuangan & transaksi tidak dihapus oleh user**: tidak ada aksi hapus/edit di UI; pembatalan = ubah `status` + `alasan`.
- **Satu-satunya pengecualian: retensi otomatis 5.000 entri per jenis** (D-15). Penghapusan ini dilakukan **sistem** lewat satu fungsi khusus (mis. `RetensiDao.hapusTerlama(jenis)`), bukan `@Delete` umum. Cakupan data turunan yang ikut terhapus menunggu **K-23**; logika retensi **belum dibuat** di Tahap 0.
- **Rantai efek** ([spec/10](spec/10-rantai-efek-dan-kondisi-khusus.md)) dijalankan dalam **satu `db.withTransaction { }`** (room-ktx) agar stok, keuangan, notifikasi, dan riwayat selalu konsisten.
- Validasi "stok tidak negatif" dan "jumlah ≤ stok" dilakukan **di repository di dalam transaksi**, bukan hanya di UI (aturan dokumen: "ditegakkan juga di sisi data").

### 4.7 DataStore (pola UserPreferencesRepository Modul 9)
```kotlin
internal val Context.auraPreferencesDataStore by preferencesDataStore(name = "aura_preferences")

class PreferensiRepository(private val context: Context) {
    private val sembunyikanNominalKey = booleanPreferencesKey("sembunyikan_nominal")
    val sembunyikanNominal: Flow<Boolean> = context.auraPreferencesDataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[sembunyikanNominalKey] ?: true }      // default tersembunyi (konvensi UI #6)
    suspend fun setSembunyikanNominal(value: Boolean) {
        context.auraPreferencesDataStore.edit { it[sembunyikanNominalKey] = value }
    }
}
```

### 4.8 Retrofit (pola Modul 7–9) — untuk nanti
`object RetrofitClient { private const val BASE_URL = ...; val api by lazy { Retrofit.Builder()...create(...) } }`, `interface XxxApiService` dengan fungsi `suspend`, DTO + mapper `toDomain()`, `XxxRemoteDataSource(api)`. **BASE_URL dan kredensial tidak boleh di-hardcode** di kode yang di-commit (lihat §7).

### 4.9 Perakitan dependency (pola NavigationLabApp)
Diputuskan **mengikuti modul** (K-02 = a): repository, database, dan DataStore dirakit di root composable `AuraApp()` dengan `remember { ... }`, lalu dioper ke `viewModel(factory = XxxViewModel.factory(...))` di dalam `composable {}`. Tidak ada kelas `Application`, `AppContainer`, maupun library DI.
```kotlin
@Composable
fun AuraApp() {
    val context = LocalContext.current.applicationContext
    val db = remember { DatabaseProvider.getDatabase(context) }
    val barangRepository = remember { DefaultBarangRepository(db, db.barangDao(), db.pergerakanStokDao()) }
    val preferensiRepository = remember { PreferensiRepository(context) }
    // ... repository lain
    AuraNavHost(barangRepository = barangRepository, preferensiRepository = preferensiRepository /* ... */)
}
```

### 4.10 Membuka link eksternal (pola TugasTambahan1)
Untuk tombol Hubungi Supplier, ikon tautan di Restock, dan Bagikan info login pegawai:
```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
else /* snackbar: "Tidak ada aplikasi untuk membuka tautan ini" */
```
Validasi URL dilakukan saat menyimpan supplier ("Link harus berupa URL valid"), bukan saat diklik. Di Compose AURA, pesan gagal memakai `Snackbar` (konvensi UI), bukan `Toast`.

## 5. Aturan data domain

| Hal | Aturan | Status |
|---|---|---|
| Uang | `Long` dalam rupiah penuh. Harga per item & HPP dibulatkan ke rupiah terdekat (`RoundingMode.HALF_UP`); total pengeluaran tetap persis sesuai input | Final (K-05) |
| HPP | Rata-rata tertimbang; stok lama ≤ 0 → HPP = harga per item restock; stok awal tanpa harga beli → HPP 0 | Final (K-15) |
| Jumlah stok | Selalu bilangan bulat; barang curah pakai satuan kecil (gram, ml) | Final (K-06) |
| ID entitas | `String` UUID (`java.util.UUID`) agar aman disinkron ke server | Final (K-08) |
| Waktu | Simpan `Long` epoch millis; hitung & format dengan `java.time`, zona `Asia/Jakarta` (WIB) | Final (K-03, K-11) |
| Enum | Simpan sebagai `String` (nama enum) lewat `@TypeConverter` | Usulan |
| Penyesuaian stok | Selalu jenis `PENYESUAIAN`; alasan (Rusak/Hilang/Kadaluarsa/Salah catat/Lainnya) disimpan di kolom alasan | Final (K-17) |
| Stok, HPP, saldo | **Nilai berjalan** (`Barang.stokSaatIni`, `Barang.hppRataRata`, `Toko.saldoKumulatif`) yang diperbarui di setiap pencatatan **dalam `db.withTransaction` yang sama** dengan penyimpanan catatannya; bukan `SUM` seluruh riwayat. Tidak bisa diedit langsung user. Pembatalan membalik efeknya (stok kembali lewat pergerakan Masuk-Pembatalan, saldo dikurangi). Koreksi saldo awal di Pengaturan Toko menggeser `saldoKumulatif` sebesar selisihnya | Final (K-22) |
| Snapshot | `item_transaksi` menyimpan harga jual & HPP saat transaksi | Wajib (dokumen) |
| Penghapusan | Akun Kasir & Supplier: lunak; Barang: arsip (hapus permanen hanya bila belum pernah bertransaksi; tombol hapus di Daftar Barang → K-25); keuangan/transaksi: status Dibatalkan; retensi otomatis 5.000 entri (D-15) | Wajib |
| Konektivitas | **Wajib online** untuk semua aksi tulis (K-20). Repository memeriksa koneksi sebelum menulis; tanpa koneksi → error "Tidak ada koneksi internet" dan UI menonaktifkan tombol simpan. Room = cache baca | Final (K-20) |
| Aturan peran | Dicek di UI (abu-abu) **dan** di repository (lempar error bila peran tidak berhak) | Wajib (dokumen) |

## 6. Error handling & coroutine

- Selalu `catch (e: CancellationException) { throw e }` sebelum `catch (e: Exception)` (pola modul + repo penunjang).
- Tidak ada `GlobalScope`.
- Pesan error ke user dalam Bahasa Indonesia, umum (tidak membocorkan data — aturan login).
- Gagal menyimpan preferensi tidak boleh membuat app crash (pola modul9).

## 7. Keamanan

- **Jangan pernah** meng-hardcode API key, token, kata sandi, atau secret di kode yang di-commit. `.env` tidak boleh di-commit (masukkan ke `.gitignore`).
- Jangan log data sensitif (kata sandi, PIN Owner, token).
- Kata sandi tidak disimpan dalam bentuk teks biasa di perangkat.
- Validasi input di batas kepercayaan (form, sebelum tulis DB, sebelum kirim ke server). Link supplier harus URL valid.

## 8. Kesalahan di kode modul yang JANGAN ditiru

| Lokasi di modul | Masalah | Yang benar |
|---|---|---|
| modul9 `build.gradle.kts` | `androidx.databinding:compiler` tidak terpakai | Jangan ditambahkan |
| modul9 `LocationRepository.kt` | import `androidx.databinding.tool.Context` (salah) dan import lain hilang | `android.content.Context` + import lengkap |
| modul9 `LocationScreen.kt` | import `android.widget.Button` di Compose; ViewModel dioper ke Screen | `androidx.compose.material3.Button`; Screen terima state + lambda |
| modul9 `HomeScreen.kt` | `val onToggleLocation = null` (kode mati) | Hapus kode mati |
| modul8 `build.gradle.kts` | `lifecycle-viewmodel-ktx` dideklarasikan dua kali (2.6.1 & 2.9.4) | Satu versi lewat catalog |
| modul6–9 Screen | Tidak ada parameter `modifier` | Semua composable publik punya `modifier: Modifier = Modifier` |
| modul8 HomeViewModel | `_uiState.value = _uiState.value.copy(...)` | `_uiState.update { it.copy(...) }` |

## 9. Konflik repo penunjang vs modul — keputusan

| Saran repo penunjang | Keputusan untuk AURA |
|---|---|
| Hilt/Koin untuk DI | **Tidak.** Factory manual + `remember { }` di root composable (modul, K-02) |
| Multi-module (`:core:*`, `:feature:*`) | **Tidak.** Satu modul `:app`, dipisah per paket |
| Explicit Backing Fields (Kotlin 2.3+) | **Tidak.** Pola `_uiState` + `asStateFlow()` |
| Type-safe `@Serializable` routes / Navigation 3 | **Tidak.** Route string + `navArgument` |
| MockK, Turbine, coroutines-test, Roborazzi, Robolectric | **Belum.** Perlu izin; Tahap 0 cukup JUnit4 + fake |
| Coil untuk gambar | **Perlu izin** (lihat 01 §4) |
| WorkManager untuk sinkron | **Perlu izin** (lihat 01 §4) |
| Injeksi `CoroutineDispatcher` lewat konstruktor | **Ya** (tanpa library) |
| `SharedFlow` untuk event sekali jalan (snackbar, navigasi) | **Ya** (K-01) |
| UDF, state hoisting, `collectAsStateWithLifecycle`, `modifier` param, `@Preview` | **Ya** (sudah ada di modul) |
| `LazyColumn` dengan `key` stabil, `rememberSaveable` untuk input, touch target ≥ 48dp, `contentDescription` | **Ya** |
| Rencana singkat + persetujuan sebelum perubahan besar | **Ya** (lihat [04-alur-kerja-agent.md](04-alur-kerja-agent.md)) |
