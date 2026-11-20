# 09 — RBAC (Owner, Manajer, Kasir)

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** **Seluruh bagian Manajer dan tingkat Hijau/Kuning/Merah BATAL.** Peran yang berlaku: **Owner** (akses penuh) dan **Kasir** (menu Kasir saja). Isi di bawah disimpan hanya sebagai arsip. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 9. Aturan tampilan abu-abu ada di [02-konvensi-ui.md](02-konvensi-ui.md).

## 1. Owner
Mendaftar sendiri (email/Google), **satu Owner per toko**. Memiliki kendali penuh termasuk akun pegawai, ambang keamanan, persetujuan, dan gambar QRIS toko.

## 2. Manajer
Akun sekunder dengan akses tertinggi. Mampu melakukan apa pun yang bisa Owner lakukan, **kecuali**: pengaturan terkait edit akun Owner maupun akun pegawai, ambang keamanan, metode pembayaran, dan hapus akun toko. Jadi Manajer bisa melakukan edit stok dan lain sebagainya.

Dari sisi keamanan, aksi Manajer dibagi tiga tingkat:

| Tingkat | Perilaku | Contoh |
|---|---|---|
| **Hijau (rutin)** | Langsung berlaku dan tercatat di Riwayat | Tambah barang, edit nama/foto/kategori, tambah supplier, restock normal, penyesuaian stok kecil, catat pengeluaran kecil |
| **Kuning (sensitif)** | Langsung berlaku, tercatat, dan **Owner mendapat notifikasi** | Ubah harga jual atau harga beli, penyesuaian stok sedang, edit stok minimum |
| **Merah (kritis)** | **Belum berlaku sampai Owner menyetujui** | Arsip/hapus barang, batalkan atau ubah transaksi, edit/batalkan catatan keuangan, penyesuaian stok besar, pengeluaran di atas limit, restock bernilai besar |

### Ambang bawaan
(Diinput dan dapat diubah Owner di Pengaturan > Keamanan dan Persetujuan.)

| Aksi | Kuning | Merah |
|---|---|---|
| Penyesuaian stok | Selisih 1–10 unit atau nilai di bawah Rp 100.000 | Di atas itu, atau lebih dari 20% stok sistem |
| Pengeluaran kustom | Rp 50.000 – Rp 500.000 | Di atas Rp 500.000 |
| Restock | — | Total bayar di atas Rp 2.000.000 |
| Ubah harga jual | — | Perubahan lebih dari 30% dari harga lama |

### Prinsip pendukung
1. Riwayat bersifat **append-only**, tidak bisa diedit atau dihapus.
2. Data keuangan **tidak dihapus**, hanya berstatus "Dibatalkan" beserta alasan.
3. Pegawai yang dihapus tetap tampil namanya di riwayat.

Seperti rencana awal, **UI Manajer sama persis dengan Owner** dan fitur yang tidak boleh dipakai berubah menjadi abu-abu dan tidak bisa ditekan.

## 3. Kasir
Akun yang **hanya bisa masuk di menu Kasir**. (Di akun Owner dan Manajer pun ada Menu Kasir, namun mereka bisa keluar dari menu tersebut dan mengakses yang lain. Menu Kasir sebenarnya bagian dari tab Manajemen sehingga dapat dibuka juga oleh Owner dan Manajer. Bedanya, untuk Kasir tombol keluarnya berwarna abu-abu dan tidak bisa ditekan.)

Kasir dapat:
- melihat barang dan stok (baca saja),
- membuat transaksi penjualan,
- melihat riwayat transaksi miliknya hari ini.

Kasir **tidak dapat** mengubah harga atau memberi diskon, dan **hanya dapat mengajukan** pembatalan transaksi.
