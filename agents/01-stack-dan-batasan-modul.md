# 01 — Stack Teknologi & Batasan Modul Praktikum

> **Aturan inti:** implementasi AURA **tidak boleh keluar** dari teknologi yang sudah dipelajari di modul praktikum PAPB (Modul 3–9, Tugas Praktikum, Tugas Tambahan, test2, Materi asisten). Dokumen ini adalah **pengganti** folder modul tersebut setelah dihapus, jadi semua pola penting dari modul dicatat di sini dan di [02-arsitektur-dan-konvensi-kode.md](02-arsitektur-dan-konvensi-kode.md).

## 0. Peta folder referensi (D-11)

Semua folder adalah referensi. Isinya berjenjang: modul yang lebih baru memperbaiki cara di modul lama. **Jika dua modul mengajarkan cara berbeda untuk hal yang sama, pakai cara yang lebih baru.**

| Folder | Yang dipelajari | Dipakai di AURA untuk |
|---|---|---|
| `Modul3` | Dasar Kotlin: `data class`, null-safety (`?:`), `when`, fungsi murni | Gaya logika bisnis murni di `core/` (mis. `StatusStokEvaluator` dengan `when`) |
| `TugasPraktikumBab2` | `Scaffold`, `Column`/`Row`/`Box`/`Spacer`, `remember { mutableStateOf }`, `clickable`, `Image` + `painterResource` + `clip(CircleShape)`, `@Preview` | Layout & state UI lokal (counter → stepper Kasir, toggle → ikon mata Saldo, foto profil bulat) |
| `ProfilApp_...` | Composable dengan parameter `modifier`, `Image`, `Button` | Komponen kecil yang bisa dipakai ulang |
| `TugasTambahan1` | `Intent(ACTION_VIEW, Uri)` membuka WhatsApp, cek `resolveActivity`, `Toast`, `RoundedCornerShape`, `ButtonDefaults.buttonColors` | **Tombol Hubungi / ikon tautan Supplier**, bagikan info login pegawai via WhatsApp |
| `test2`, `modul6-PAPB` | Navigation Compose: `NavHost`, `composable`, `Routes` string, `navArgument`, `popBackStack`; ViewModel + factory manual; Repository + LocalDataSource | Navigasi & pola ViewModel/Repository |
| `modul7-PAPB`, `Materi_PAPB_sham` | Retrofit + Gson, DTO + mapper; versi lama: `Call`/`enqueue`/`Callback` dan `var uiState by mutableStateOf` di ViewModel | Struktur remote (`api/`, `dto/`, `datasource/`). Cara lama **diganti** oleh modul 8 |
| `modul8-PAPB` | Retrofit `suspend`, `StateFlow` + `asStateFlow`, `viewModelScope`, `CancellationException`, pencarian `debounce` | **Pola utama ViewModel & remote** |
| `modul9-PAPB` | Room (Entity, Dao, `Flow`, `DatabaseProvider`), DataStore Preferences, `combine` + `stateIn`, izin runtime (`rememberLauncherForActivityResult`) | **Pola utama data lokal**, minta izin kamera/galeri nanti |
| `TugasPraktikum1`, `TugasPraktikum_Modul5_PAPB` | Kosong (hanya `.idea`/`.gradle`) | — |

**Cara lama yang tidak dipakai lagi** (sudah diganti modul yang lebih baru):
- `var uiState by mutableStateOf(...)` di ViewModel (modul6–7), diganti `MutableStateFlow` + `asStateFlow()` (modul8–9).
- Retrofit `Call<T>` + `enqueue(Callback)` (modul7, Materi), diganti fungsi `suspend` (modul8–9).
- Data dummy di `LocalDataSource` (modul6), diganti Room.

## 1. Tingkat keketatan (keputusan 2026-11-20: "Ketat + izin per kasus")

1. **Pola arsitektur wajib mengikuti modul** (ViewModel + `UiState` + `StateFlow`, factory manual, Repository + DataSource, route string Navigation Compose, `DatabaseProvider`, DataStore Repository).
2. **Library:** hanya yang ada di **daftar putih** (bagian 3).
3. **Library tambahan hanya boleh** bila fitur wajib dokumen AURA **mustahil** dibuat tanpanya, **dan setiap penambahan harus ditanyakan ke user dulu** (lihat bagian 5). Tanpa persetujuan → tandai `// TODO(izin-library): ...` dan lanjutkan bagian lain.
4. Bila panduan dari repo skill penunjang bertentangan dengan pola modul → **pola modul menang** (daftar konflik ada di [02-arsitektur-dan-konvensi-kode.md](02-arsitektur-dan-konvensi-kode.md) §9).
5. **API di dalam library daftar putih** (mis. `NavigationBar`, `ModalBottomSheet`, `Snackbar`, `AlertDialog`, `Canvas`, `SharedFlow`, `db.withTransaction`) **boleh dipakai** walau belum muncul di modul (K-01).

## 2. Versi & konfigurasi (diambil dari modul9-PAPB / Materi asisten)

| Item | Nilai |
|---|---|
| Gradle wrapper | **9.6.0** (`distributionUrl=https\://services.gradle.org/distributions/gradle-9.6.0-bin.zip`) |
| Daemon JVM toolchain | 25 (`gradle/gradle-daemon-jvm.properties`: `toolchainVersion=25`) |
| AGP | **9.3.3** |
| Kotlin (plugin compose) | **2.2.10** |
| KSP | **2.3.6** |
| Compose BOM | **2026.02.01** |
| compileSdk / targetSdk | **37** (`compileSdk { version = release(37) }`) |
| minSdk | **26** (K-03: agar `java.time` bisa dipakai tanpa library tambahan; modul memakai 24) |
| Java | `JavaVersion.VERSION_11` |
| gradle.properties | `org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8`, `org.gradle.workers.max=2`, `org.gradle.configuration-cache=true`, `kotlin.code.style=official` |
| settings plugin | `org.gradle.toolchains.foojay-resolver-convention` 1.0.0 |
| Struktur | **Satu modul `:app`** (seperti semua modul praktikum), root repo = root proyek Gradle |

## 3. Daftar putih library

| Library | Versi | Asal modul |
|---|---|---|
| `androidx.core:core-ktx` | 1.10.1 | semua |
| `androidx.activity:activity-compose` | 1.8.0 | semua |
| Compose BOM: `ui`, `ui-graphics`, `ui-tooling-preview`, `material3` | BOM 2026.02.01 | semua |
| `androidx.lifecycle:lifecycle-runtime-ktx` | 2.6.1 (catalog) | semua |
| `androidx.lifecycle:lifecycle-viewmodel-ktx` | 2.9.4 | Modul 8–9 |
| `androidx.lifecycle:lifecycle-viewmodel-compose` | 2.9.4 | Modul 8–9 |
| `androidx.lifecycle:lifecycle-runtime-compose` | 2.9.4 | Modul 8–9 (`collectAsStateWithLifecycle`) |
| `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.11.0 | Modul 8–9 |
| `androidx.navigation:navigation-compose` | 2.9.8 | Modul 6–9 |
| `androidx.room:room-runtime`, `room-ktx`, `ksp(room-compiler)` | 2.8.5 | Modul 9 |
| `androidx.datastore:datastore-preferences` | 1.3.0 | Modul 9 |
| `com.squareup.retrofit2:retrofit`, `converter-gson` | 3.0.0 | Modul 7–9 |
| Test: `junit:junit` 4.13.2, `androidx.test.ext:junit` 1.1.5, `espresso-core` 3.5.1, `ui-test-junit4`, `ui-test-manifest`, `ui-tooling` (debug) | — | semua |

**Sengaja TIDAK dibawa dari modul9:**
- `androidx.databinding:compiler` — tidak dipakai, salah tempel di modul9.
- `play-services-location` + `kotlinx-coroutines-play-services` — fitur lokasi tidak ada di AURA.

## 4. Library di luar modul yang **diketahui** akan dibutuhkan (BELUM diizinkan)

Jangan ditambahkan sebelum user menyetujui. Saat fiturnya dikerjakan, tanyakan dulu dengan menyebut: fitur dokumen yang butuh, kenapa mustahil tanpa library, alternatif tanpa library.

| Kebutuhan fitur (dokumen) | Kandidat library | Alternatif tanpa library |
|---|---|---|
| Foto barang, logo, QRIS, nota, foto profil (tampilkan dari Uri/URL) | Coil (`coil-compose`) | Decode `Bitmap` manual + `Image(bitmap=...)` (lebih rawan memori) |
| Ambil foto dari kamera / galeri | — (bawaan `activity-compose`: `rememberLauncherForActivityResult`, sudah dipakai Modul 9) | — |
| Scan barcode (Kasir, Penyesuaian Stok) | CameraX + ML Kit Barcode | Input SKU manual |
| Grafik tren (Analisis) | Library chart | `Canvas` Compose (lihat K-01) |
| Masuk dengan Google | Credential Manager / Google Identity | — (bergantung server sendiri) |
| Ikon (bottom nav, status stok, dll.) | `material-icons-extended` | **Vector drawable XML** di `res/drawable` + `painterResource` (pola Modul 3 / Tugas) |
| Sinkronisasi latar belakang / proses terjadwal ML | WorkManager | Sinkron saat app dibuka (coroutine di `viewModelScope`/scope aplikasi) |
| Push notification (Tahap 2) | Biasanya FCM (= Firebase, **sudah ditolak**) | Notifikasi lokal `NotificationManager` saat sinkron / WebSocket OkHttp, lihat K-19 |
| Unit test coroutine/Flow | `kotlinx-coroutines-test`, Turbine, MockK | JUnit4 + fake repository + logika murni tanpa coroutine |
| Penyimpanan sesi login | (security-crypto sudah deprecated) | DataStore Preferences (pola Modul 9) |
| Hash kata sandi untuk autentikasi lokal sementara (K-07) | — | `java.security.MessageDigest`/`javax.crypto` bawaan JDK (PBKDF2 + salt) |
| Login Google | Credential Manager / Google Identity | **Sudah disetujui prinsip (K-18)**, versi dicatat saat Tahap 1 |
| Bagikan struk gambar/PDF | — (`android.graphics.pdf.PdfDocument` + `Intent.ACTION_SEND`, bawaan Android) | — |

## 5. Prosedur minta izin library

Sebelum menambah dependency apa pun yang tidak ada di §3:
1. Berhenti, jangan ubah `build.gradle.kts` / `libs.versions.toml`.
2. Tanyakan ke user: *nama library + versi*, *fitur dokumen yang membutuhkan*, *kenapa tidak bisa dengan daftar putih*, *alternatif tanpa library + kekurangannya*.
3. Jika disetujui: tambahkan lewat version catalog, lalu catat di tabel "Library tambahan disetujui" pada [00-keputusan-proyek.md](00-keputusan-proyek.md).

## 6. Template build (pengganti file modul)

### `gradle/libs.versions.toml`
```toml
[versions]
agp = "9.3.3"
kotlin = "2.2.10"
ksp = "2.3.6"
coreKtx = "1.10.1"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"
lifecycleRuntimeKtx = "2.6.1"
lifecycle = "2.9.4"
activityCompose = "1.8.0"
composeBom = "2026.02.01"
coroutines = "1.11.0"
navigation = "2.9.8"
room = "2.8.5"
datastore = "1.3.0"
retrofit = "3.0.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-ktx = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-converter-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```
> Modul memakai string literal di `dependencies {}` untuk library tambahan; di AURA semuanya **dipindah ke version catalog** agar rapi (bukan library baru, hanya cara deklarasi).

### `build.gradle.kts` (root)
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
```

### `settings.gradle.kts`
Sama seperti modul (pluginManagement google/mavenCentral/gradlePluginPortal dengan filter `com.android.*`, `com.google.*`, `androidx.*`; foojay 1.0.0; `RepositoriesMode.FAIL_ON_PROJECT_REPOS`), dengan:
```kotlin
rootProject.name = "AURA"
include(":app")
```

### `app/build.gradle.kts`
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.aura.app"
    compileSdk { version = release(37) }

    defaultConfig {
        applicationId = "com.aura.app"
        minSdk = 26            // K-03: java.time
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release { optimization { enable = false } }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures { compose = true }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

### Gradle wrapper
`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`, `gradle/wrapper/gradle-wrapper.properties` **disalin dari `modul9-PAPB/`** sebelum folder modul dihapus. Jika sudah terlanjur terhapus: buat ulang lewat Android Studio (New Project, lalu salin wrapper) atau `gradle wrapper --gradle-version 9.6.0`.

### Manifest
- `android.permission.INTERNET` (Retrofit, nanti untuk server sendiri).
- Theme XML minimal seperti modul: `<style name="Theme.Aura" parent="android:Theme.Material.Light.NoActionBar" />`.
- `MainActivity` dengan `android:windowSoftInputMode="adjustResize"` (seperti modul9).
