# 12 — Prompt Google Stitch (salinan utuh)

> Salinan apa adanya dari `AURA_Prompt_Stitch.md` (2026-11-20). Dokumen ini **menang** atas Revisi 2 bila bertentangan (D-12). Ringkasan perubahannya ada di [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md). Pengecualian: status "Perlu Ditinjau" & "Konfirmasi Selisih" (layar 16) dan baris "discount" (layar 9) **tidak dipakai** (K-20, K-21).

---

# AURA \- Prompt untuk Google Stitch

Cara pakai:

1. Kirim **Prompt 0 (Gaya Global)** sekali di awal.  
2. Kirim prompt layar satu per satu. Setiap prompt berdiri sendiri, jadi salin seluruh isi blok kode.  
3. Lampirkan hanya gambar (logo, sketsa, screenshot referensi) bila ada. Teks prompt ditempel langsung ke kotak prompt.  
4. Setelah satu layar jadi, lampirkan screenshot-nya saat membuat layar berikutnya agar gaya tetap konsisten.

Palet warna resmi sudah terkunci di dalam prompt:

- Putih (\#FFFFFF): latar layar utama, kartu, dan teks kontras  
- Ungu (\#311A37): aksen sekunder & gradien atas layar pembuka/auth  
- Merah (\#930000): tombol aksi utama (CTA), tombol (+), tombol ikon, status Habis  
- Amber/Cokelat (\#C86225): label kategori barang  
- Gradien: vertikal \#311A37 (atas) ke \#930000 (bawah) untuk Splash, Onboarding, dan Form Profil

---

## 0\. Gaya Global

```
Design a mobile Android app (Material Design 3) called "AURA" - an inventory
recording app for small retail businesses (UMKM) in Indonesia.
All UI text must be in Bahasa Indonesia.

Style: clean, friendly, professional. Rounded cards (16dp), generous spacing.

Color Palette & Visual Design System:
- Primary Color: Crimson Red (#930000) - for primary CTA buttons, floating center "+" button, icon buttons, active badges.
- Secondary / Brand Accent: Deep Aubergine / Purple (#311A37) - for header sections, branding accents.
- Background Gradient (Welcome/Splash, Onboarding, Auth headers): Linear vertical gradient from Deep Purple (#311A37) at the top to Crimson Red (#930000) at the bottom (transitioning through deep wine #5C0E1E).
- Surface & Background (Internal app screens): Pure White (#FFFFFF) with rounded cards (16dp) and subtle 1px border (#E5E7EB).
- Text Colors: Dark Charcoal (#1F1F1F) for primary text, Muted Grey (#757575) for secondary text/hints, Warm Amber (#C86225) for category tags.
- Stock Status Colors (always color + label + icon):
  * Aman: Emerald Green (#16A34A)
  * Menipis: Amber Orange (#D97706)
  * Habis: Crimson Red (#930000)
  * Diarsipkan: Neutral Grey (#9E9E9E)

There are only TWO roles: Owner (full access) and Kasir/Cashier (menu Kasir only).
There is NO Manager role.

Main navigation: bottom navigation with 5 positions in this order:
Dashboard, Manajemen, a raised round center "+" button (shortcut to Kasir in solid #930000 with white icon),
Analisis, Profil. The center "+" is a floating action button, not a tab.
The Kasir role has NO bottom navigation at all; they go directly to the Kasir
menu and can only exit via the avatar sheet.

Currency format: Rp 1.250.000. Dates: 17 Nov 2026, 19.48 WIB.
Stock numbers always include a unit (pcs, kg).

Elements restricted by role stay visible but are greyed out (disabled, no press
effect). Tapping one shows a snackbar: "Hanya Owner yang dapat mengubah data ini".

The app only records data. No payment gateway, no discounts, no Manager role.
Financial records and transactions are append-only: no editing, only cancellation.
Balance is a single cumulative figure (no payment method breakdown).
Each record type has its own independent rolling window of 5,000 entries:
sales transactions, restock entries, stock adjustments, custom expenses, and
custom income each have a separate 5,000-entry cap. When entry 5,001 of a type
is saved, the oldest entry of that same type is deleted automatically.
```

---

## 1\. Login

```
Login screen for AURA with dark aesthetic.
Background: linear vertical gradient from Deep Purple (#311A37) at top to Crimson Red (#930000) at bottom.
Top: 3D red cube logo with white growth arrow, and title "Masuk ke AURA" in white text.
Segmented toggle with two modes: "Owner" and "Pegawai" in translucent pill container.
Owner mode: pill-shaped input fields (Email and Kata sandi) with subtle translucent fill and white border.
Primary button: white pill button with red text (#930000) "Masuk".
Secondary button: outlined white pill "Masuk dengan Google".
Links below: "Lupa kata sandi?" and "Daftar toko baru" in white text.
Pegawai mode: fields "ID Toko", "Nama Akun", "Kata Sandi" (with show/hide icon),
white pill button "Masuk", and info text "Lupa kata sandi? Hubungi Owner".
Show a generic error banner state: "ID toko, nama akun, atau kata sandi salah".
```

## 2\. Registrasi Owner dan Setup Toko

```
Two-step onboarding / setup profile screen for a new store owner.
Background: linear vertical gradient from Deep Purple (#311A37) at top to Crimson Red (#930000) at bottom.
Top: 3D red cube logo with white trend line, title "Isi Profil" in bold white text.
Center: circular camera upload avatar placeholder with thin white circular border.
Input fields: pill-shaped text fields with dark translucent background and thin white border:
"Nama Bisnis", dropdown "Bidang Bisnis", "Saldo Awal" (with helper text "Nominal kas saat mulai, boleh 0").
Primary button: white rounded pill button with red text (#930000) "Buat Akun" / "Selesai".
Bottom link: "Sudah punya akun? Masuk" in white text.
Show a generated read-only "ID Toko" like "AB12CD34" with a copy icon.
```

---

## 3\. Dashboard (Owner)

```
Dashboard screen. Top app bar: store name and a bell icon with a red unread badge.
Below: a "Saldo" card with an eye icon, hidden by default (Rp ••••••).
Then 3 quick action buttons: "Mulai Kasir", "Catat Restock", "Catat Pengeluaran".
Then two side-by-side cards "Pemasukan" and "Pengeluaran" without amounts, with
the hint "Ketuk untuk lihat rincian".
Then three sections: "Stok Menipis", "Fast Moving", "Slow Moving". Each shows
max 3 items (small thumbnail, name, stock/minimum, status chip) and a footer
link "+4 barang lainnya - Lihat semua".
Stok Menipis and Fast Moving rows have a "Restock" button; Slow Moving rows show
"Terakhir laku N hari lalu" and "Modal tertahan: Rp ..." and have no Restock button.
Bottom navigation visible with Dashboard active.
```

## 4\. Pusat Notifikasi

```
Notification list screen. Grouped by "Hari ini" and "Sebelumnya". Each row has a
bold title, a smaller preview line, time, and an unread dot. Top action:
"Tandai semua dibaca". Example items: "Stok Kopi ABC habis", "Stok Gula Pasir
menipis", "Anomali terdeteksi: penjualan Indomie turun 80% dari rata-rata",
"Pembaruan versi 1.1", "Riwayat penjualan hampir penuh (4.800 / 5.000)".
Note: stock and anomaly notifications fire only when the status changes.
```

## 5\. Detail Notifikasi

```
Notification detail screen. Show three variants:
1) "Stok Kopi ABC habis" with time and a button "Restock sekarang".
2) Anomaly: "Anomali terdeteksi: penjualan Indomie turun 80% dari rata-rata
   selama 4 minggu terakhir." with a button "Lihat Analisis".
3) Data limit: "Riwayat penjualan hampir penuh (4.800 / 5.000)" with an info
   button "Pelajari lebih lanjut".
```

## 6\. Daftar Pengeluaran

```
Expenses list screen. Top: large total expenses amount, a period dropdown
(Hari ini, Minggu ini, Bulan ini, Tahun ini, Kustom) and an "advanced" icon that
opens start/end date and time inputs. Filter chips: Semua, Pembelian, Kustom.
List rows: title, date, amount, a type chip (Pembelian/Kustom) and a status
"Dibatalkan" variant. Floating button "+ Catat pengeluaran".
```

## 7\. Detail Pengeluaran

```
Two variants of an expense detail screen.
Pembelian: amount, item name, quantity and price per item, supplier, receipt photo,
date, recorded by.
Kustom: title (e.g. Listrik), amount, note, documentation photo, date, recorded by.
Button "Batalkan" (asks for a reason). Expenses are never deleted.
```

## 8\. Form Catat Pengeluaran Kustom

```
Form screen "Catat Pengeluaran": Judul/jenis biaya (Listrik, Sewa, Gaji, Lainnya),
Nominal, Tanggal, Catatan, Foto bukti (optional), button "Simpan".
```

## 9\. Daftar Pemasukan dan Detail Penjualan

```
Income list screen with the same layout as the expenses list (total, period
dropdown, advanced custom range, filter chips Semua / Penjualan / Kustom).
Also a detail screen for a sale: transaction number INV-261117-0001, item list
with quantity and price, discount, total, payment method label, cashier, date,
status chip (Selesai / Dibatalkan / Diretur), and a button "Batalkan transaksi".
```

## 10\. Daftar Lengkap Stok Menipis / Fast Moving / Slow Moving

```
Full list screen "Stok Menipis". Rows: small thumbnail, name, stock / minimum with
unit, status chip (Menipis or Habis), and a "Restock" button. Tapping a row opens
the item profile. Ordered most urgent first. Empty state: "Semua stok aman".
Provide variants for "Fast Moving" (with chip "Terjual N unit / 30 hari") and
"Slow Moving" (no Restock button).
```

---

## 11\. Menu Manajemen (Layer 1\)

```
Manajemen tab screen with grouped menu cards.
Group "Operasional": Kasir.
Group "Persediaan": Barang, Pergerakan Stok, Supplier.
Group "Kontrol": Riwayat Aktivitas.
(There is no Persetujuan menu — the Manager role has been removed.)
Bottom navigation visible with Manajemen active.
```

## 12\. Kasir \- Beranda (K1)

```
Cashier home screen. App bar: back button (greyed out and disabled for the Kasir
role; active for Owner), title "Kasir", history icon, avatar icon.
Search field with a barcode scan icon, category chips (Semua, ...).
A grid of item cards: photo, name, price, stock badge. Low stock has a yellow
badge; stock 0 is greyed out with the label "Habis" and cannot be selected.
A floating bottom cart bar: "3 item - Rp 45.000" and the button "Lihat Keranjang".
Note: there are only two roles — Owner and Kasir. No Manager.
```

## 13\. Kasir \- Keranjang / Catat Transaksi (K2)

```
Cashier transaction recording screen ("Catat Transaksi"). Clean white background (#FFFFFF).
Top app bar: back arrow, centered title "Catat Transaksi".
Cart item list:
- Rounded white card (16dp) with subtle border: square item photo on the left, item name, unit price "Rp. 3.500", quantity stepper "(-) 3 (+)" with circular outline buttons, and subtotal line in red (#930000) "Subtotal: Rp. 10.500".
- Dashed outline button "+ Tambah Barang" with rounded corners.
Bottom pinned summary card (light pinkish/white tinted background):
- "Subtotal Harga: Rp 10.500" and "Jumlah Item: 3 pcs".
- Bold total row: "Total Transaksi" with large amount in red (#930000) "Rp 10.500".
- Large rounded pill CTA button in solid Crimson Red (#930000) with white text "Catat Transaksi".
Bottom navigation bar visible with center "+" button in Crimson Red (#930000).
```

## 14\. Kasir \- Pembayaran (K3)

```
Payment screen. Clean white background (#FFFFFF).
Top app bar with back arrow and title "Pembayaran".
Bill summary card with large total amount in Crimson Red (#930000).
Payment method segmented selector chips (Tunai, Transfer, QRIS) in Crimson Red (#930000) when active.
Three states:
1) Tunai: "Uang diterima" input, quick amount chips in rounded pills, automatic "Kembalian".
2) Transfer: store's bank account info card with a copy button.
3) QRIS: a large QR image centered on a white card, helper note "Pastikan pembayaran sudah masuk". Add empty state: "Gambar QRIS belum diatur. Hubungi Owner."
Large primary button in Crimson Red (#930000) with white text "Selesaikan Transaksi", disabled until payment confirmation.
This is only a recording label; there is no real payment integration.
```

## 15\. Kasir \- Transaksi Berhasil (K4)

```
Success screen with a check icon, transaction number INV-261117-0001, item summary,
payment method, change amount, and buttons "Bagikan Struk" and "Transaksi Baru".
```

## 16\. Kasir \- Riwayat Hari Ini

```
Today's transactions list. Owner sees all transactions; Kasir sees only their own
today. Each row: number, time, total, status chip (Selesai / Dibatalkan / Perlu Ditinjau [orange]).
Detail screen looks like a receipt. Kasir has a button "Ajukan pembatalan" (sends
a notification to Owner). Owner has a button "Batalkan transaksi" with a required
reason text field, plus "Konfirmasi Selisih" for transactions marked Perlu Ditinjau.
Show a banner: "Riwayat penjualan: 4.750 / 5.000. Data terlama dihapus otomatis
saat batas terlampaui." (warning state at 90%+). Each record type has its own
independent 5,000-entry limit.
```

## 17\. Sheet Avatar Kasir

```
Small bottom sheet opened from the avatar icon in the Kasir app bar:
name, role chip "Kasir", store ID, then four buttons:
"Edit Profil" (opens a form to edit name, photo, phone number),
"Ubah Kata Sandi" (three password fields: current, new, confirm),
"Ganti Akun" (opens account switcher with password verification),
"Keluar Akun".
This is the only way a Kasir can access their account settings since they have
no Profile tab.
```

---

## 18\. Daftar Barang

```
Item management list screen ("Manajemen Barang"). Clean white background (#FFFFFF).
Top app bar: back arrow, title "Manajemen Barang".
Search bar: rounded pill search input with magnifying glass icon and placeholder "Cari Barang",
and next to it a Crimson Red (#930000) pill button "+ Tambah" with white text.
Item list with rounded white cards (16dp) having subtle grey border:
- Square item photo on the left (e.g. Indomie Soto Kuah, Nabati Keju).
- Middle info: bold item name in black (#1F1F1F), category label in warm amber/orange (#C86225) like "Makanan * Stok 50 pcs",
  and price line "Rp. 3.500 / pcs".
- Right action buttons: rounded square Crimson Red (#930000) icon buttons with white icons:
  edit pencil icon button and trash delete icon button.
Bottom navigation bar visible with white background: Home, Manajemen (active), raised round center "+" button in Crimson Red (#930000), Analisis, Profil.
```

## 19\. Profil Barang

```
Item profile screen: photo, id, name, SKU/barcode, categories (chips), selling price,
purchase price per supplier (list), average cost, current stock with unit,
minimum stock, 30-day summary (sold, Fast/Slow status). Buttons: "Edit", "Restock",
"Penyesuaian Stok", "Kartu Stok", and a three-dot menu with "Arsipkan".
```

## 20\. Tambah / Edit Barang

```
Add/Edit item form: photo upload, name, unit dropdown, SKU/barcode, selling price,
minimum stock, initial stock and initial purchase price (add mode only). Category
multi-select dropdown with a textfield below for creating a new category: when
focused, the buttons "Buat" and "Batal" appear below it (not a popup).
Edit mode: stock is read-only with the hint "Ubah stok lewat Restock atau
Penyesuaian Stok". Warning if the selling price is below the purchase price.
```

## 21\. Kelola Kategori

```
Category management: list of categories with item count, add, rename, delete
(confirmation: "N barang memakai kategori ini; kategori akan dilepas dari barang
tersebut").
```

## 22\. Form Restock

```
Restock form for one item: supplier dropdown with a create-new-in-place textfield
(name and link, buttons Buat/Batal appear on focus). Each supplier row has a link
icon to open the contact link. Fields: "Jumlah masuk", "Total dibayar", automatic
"Harga per item", optional date, receipt photo, note. Button "Simpan Restock".
```

## 23\. Pergerakan Stok

```
Stock movement ledger: period filter, type filter (Masuk, Keluar, Penyesuaian),
item search. Rows: time, item, type and source, +/- quantity, stock before -> after,
actor. Read-only.
```

## 24\. Penyesuaian Stok

```
Stock adjustment flow: select/scan an item, show system stock, input physical
stock, automatic difference and value in rupiah, required reason chips (Rusak,
Hilang, Kadaluarsa, Salah catat, Lainnya), optional note and photo, button
"Simpan". Note: since there is no Manager role, all adjustments are done directly
by Owner.
```

## 25\. Daftar dan Detail Supplier

```
Supplier list: name, link type icon (WhatsApp/Telegram/Instagram), item count,
search, button "+ Tambah Supplier". Detail: name, button "Hubungi", supplied items
with last purchase price, short purchase history, note, buttons Edit / Hapus or
Nonaktifkan.
```

## 26\. Riwayat Aktivitas

```
Activity log (Owner only; Kasir cannot access this).
Period filter, account filter (Semua / per person), activity type filter
(Barang, Stok, Keuangan, Penjualan, Akun, Login).
Rows: time, account with role chip, action summary.
Detail: before -> after values, device name. Read-only, append-only.
```

---

## 27\. Analisis

```
Analisis tab (Owner only; Kasir cannot access). Period filter on top, four sub-tabs.
Ringkasan: KPI cards (Omzet, Laba kotor, Laba bersih, Kerugian Penyesuaian [rusak/
hilang/kadaluarsa, shown separately], Jumlah transaksi, Rata-rata transaksi) with %
change vs previous period, and a sales trend line chart.
Penjualan: trend chart, Top 10 best sellers by unit and revenue, ABC contribution
per category and item, busiest hours/days, margin per item.
Stok: Fast/Slow Moving (settings controlled by Owner in Pengaturan Analisis, not a
temporary filter), warehouse valuation, stock in vs out chart, tied-up capital
(stock x HPP), days of stock left (show "Tidak dapat dihitung" if average sales = 0).
Prediksi: ML insight cards in plain language with action buttons.
Add a small info banner: "Data analisis mencakup catatan dari [date] hingga
sekarang (maks. 5.000 entri per jenis catatan)."
```

---

## 28\. Profil (Layer 1\)

```
Profile tab: photo (or default icon), username, role chip, store ID with a copy
icon, phone, email, buttons "Edit Profil", "Ganti Akun", "Keluar", and a settings
gear icon at top right.
```

## 29\. Pengaturan

```
Single settings screen (Owner only). Three groups.
Akun: Ubah Kata Sandi, Hapus Akun (danger zone).
Toko: Manajemen Akun Kasir, Metode Pembayaran (upload QRIS image),
Pengaturan Toko (name, address, logo, correct initial balance, button "Keluarkan Semua Sesi Lain"),
Pengaturan Analisis (period and Fast/Slow thresholds), Preferensi.
Bantuan dan Informasi: Pusat Bantuan/FAQ, Hubungi Kami, Syarat dan Ketentuan,
Kebijakan Privasi, Tentang Aplikasi (include: "Setiap jenis catatan (penjualan,
restock, penyesuaian stok, pengeluaran kustom, pemasukan kustom) dibatasi 5.000
entri terakhir per toko secara terpisah. Data terlama dihapus otomatis.").
Note: There is no Manager role, so no approval threshold settings.
```

## 30\. Ubah Kata Sandi dan Hapus Akun

```
Change password screen with three labeled password fields (Kata sandi saat ini,
Kata sandi baru, Konfirmasi kata sandi baru). Also a delete account screen with a
warning about consequences, a password field, a field to type "HAPUS", and a red
button, with a note about a 14-day recovery period.
```

## 31\. Metode Pembayaran (unggah QRIS)

```
Payment method settings: preview of the uploaded QRIS image with buttons "Unggah",
"Ganti", "Hapus", a field for recipient name, and a free-text field for bank
account info (optional). Note: "Gambar hanya ditampilkan di layar pembayaran Kasir."
```

## 32\. Manajemen Akun Kasir

```
Kasir account list: account name, status (Aktif/Nonaktif), last login, button
"+ Tambah Kasir". Add screen: account name, initial password with auto-generate,
then a share-login-info button. Detail: Reset kata sandi, Nonaktifkan/Aktifkan,
Hapus (soft delete), "Lihat aktivitas akun ini".
Note: there is no Manager role; this screen only manages Kasir accounts.
```

## 33\. Notifikasi Batas Data

```
Two notification states shown on the Dashboard screen. Each record type
(penjualan, restock, penyesuaian stok, pengeluaran kustom, pemasukan kustom)
has its own independent 5,000-entry rolling window.
1) Warning banner (at 90%, 4,500/5,000): a thin orange banner below the app bar
   showing the type closest to its limit:
   "Riwayat penjualan hampir penuh (4.500 / 5.000). Data terlama akan terhapus
   otomatis saat batas terlampaui. Ketuk untuk info."
2) Notification card in the notification center:
   "Riwayat penjualan penuh. Transaksi pertama (INV-240101-0001) telah dihapus
   otomatis untuk memberi ruang bagi catatan baru."
Both are informational only — no action is blocked.
```

## 34\. Sheet Ganti Akun

```
Bottom sheet listing accounts previously logged in on this device (max 5).
Switching to an Owner account requires password verification (not biometric, since
biometrics are device-bound, not account-bound). Switching to a Kasir account also
requires that Kasir's password. Button "Tambah akun lain" opens the Login screen.
```

## 35\. Tombol (+) Shortcut Kasir pada Bottom Navigation

```
Bottom navigation bar with 5 positions: Dashboard, Manajemen, a raised round
center "+" button, Analisis, Profil.
Style: white background (#FFFFFF), thin top border/shadow, grey outline icons for tabs (#525252).
Center "+" button: a prominent raised circular button in solid Crimson Red (#930000) with a white plus icon (#FFFFFF) and subtle drop shadow.
Show two states side by side:
1) Default: on a Dashboard/Manajemen screen.
2) After tapping "+": the Kasir (K1 Beranda Kasir) screen opens, bottom navigation remains visible with the "+" button active.
The "+" button only exists for Owner. For the Kasir role there is no bottom navigation at all.
```

## 36\. Contoh Elemen Abu-abu Karena Pembatasan Peran

```
Screens showing role restriction as seen by a Kasir or state restrictions.
1) Kasir screen (K1): Top app bar back button is greyed out and disabled.
   Tapping it shows a snackbar: "Kasir hanya dapat keluar melalui profil avatar".
2) Kasir screen (K1) item grid: Out of stock item (stok = 0) is greyed out with
   a "Habis" badge and cannot be tapped or added to cart.
3) Kasir today's transactions: Cancel button is labeled "Ajukan pembatalan" instead
   of direct cancellation.
```

&nbsp;