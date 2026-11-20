# 04 — Tab 1: Dashboard

> ⚠️ **Diperbarui oleh Prompt Stitch (2026-11-20):** Dashboard **hanya Owner**; app bar berisi nama toko; kartu Pemasukan & Pengeluaran berdampingan; banner batas data 90%; jenis notifikasi berubah (tanpa persetujuan/aksi Manajer; tambah anomali & batas data); **tanpa diskon** di detail penjualan. Rincian: [00-perubahan-dari-stitch.md](00-perubahan-dari-stitch.md).

> Sumber: dokumen AURA bagian 7, "Tab 1 - Dashboard". Tanda **+** = navigasi.

Dashboard **hanya untuk Owner dan Manajer**. Tujuannya menjawab "apa yang perlu diperhatikan sekarang" dalam satu layar.

## Layer 1 — Komponen

1. **Notifikasi +** — ikon lonceng di app bar beserta lencana angka notifikasi belum dibaca. Jika ditekan masuk ke mini page khusus notifikasi. Jenis notifikasi: update patch, stok habis, stok menipis, permintaan dan hasil persetujuan, serta aksi sensitif Manajer untuk Owner.
2. **Saldo** — saldo total, dengan ikon mata dan default tersembunyi. `Saldo = saldo awal + seluruh pemasukan − seluruh pengeluaran`, sehingga berbasis arus kas dan bukan laba.
3. **Aksi Cepat +** — tiga tombol: **Mulai Kasir, Catat Restock, Catat Pengeluaran**. Masing-masing langsung membuka Kasir, formulir Restock (memilih barang terlebih dahulu), dan formulir pengeluaran kustom.
4. **Pengeluaran +** — tidak berisi nominal, hanya teks "Ketuk untuk lihat rincian". Nominalnya muncul saat ditekan, yang akan masuk ke bagian riwayat transaksi atau pengeluaran (pengeluaran bisa dikustom juga jika bukan pengeluaran berupa transaksi).
5. **Pemasukan +** — tidak berisi nominal. Jika ditekan, tampil data seperti pengeluaran namun untuk pemasukan.
6. **Stok Menipis +** — menampilkan **maks. 3 barang** beserta gambar barang (kecil), nama, `sisa stok / stok minimum`, dan chip status. Jika lebih dari 3, di bawahnya muncul baris ajakan "+N barang lainnya - Lihat semua". Jika ditekan, muncul list lengkap barang beserta stoknya yang bisa ditekan dan masuk ke bagian Barang. Ada tombol **Restock** sehingga user tidak perlu masuk ke bagian Barang untuk restock. Jika kosong tampil "Semua stok aman". Kartu ini mencakup barang berstatus **Menipis dan Habis**, diurutkan paling mendesak.
7. **Fast Moving +** — daftar barang yang sering laku atau sedang dalam tren baik sehingga perlu direstock atau diberi perhatian lebih. Tampilan awal sama persis seperti Stok Menipis (maks. 3 barang, gambar kecil, "+N barang lainnya - Lihat semua") dengan keterangan "terjual N unit / 30 hari". Memiliki tombol shortcut Restock. Jika kosong tampil "Belum ada data penjualan".
8. **Slow Moving +** — sama seperti Fast Moving namun untuk barang yang jarang/tidak laku, dengan keterangan "terakhir laku N hari lalu" dan "Modal tertahan: Rp ..." (stok × harga beli). **Tidak ada** tombol shortcut Restock.

## Layer 2

1. **(Dari Notifikasi) Baris notifikasi +** — tulisan lebih besar untuk highlight dan tulisan lebih kecil untuk preview, waktu, dan penanda belum dibaca. Dikelompokkan **Hari ini / Sebelumnya**, serta tombol "Tandai semua dibaca". Jika baris ditekan, muncul pesan lengkap notifikasi dengan tombol aksi sesuai jenis: Stok habis = "Restock sekarang", Permintaan persetujuan = "Tinjau", Update patch = tanpa tombol.
2. **(Dari Pengeluaran)** Komponen:
   1. **Total pengeluaran** — nominal pengeluaran total. Terdapat FilterPeriode (harian, mingguan, bulanan, tahunan) dan tombol advance di samping untuk memasukkan tanggal & jam mulai serta tanggal & jam akhir (rentang custom).
   2. **Filter jenis** — Semua / Pembelian / Kustom.
   3. **Baris Pengeluaran** — list pengeluaran. Jika ditekan, muncul detail untuk 2 jenis:
      - **Pembelian:** Nominal, Barang, jumlah dan harga per item, Supplier, dokumen/foto nota, tanggal, dicatat oleh; dibuat otomatis dari Restock.
      - **Kustom:** Judul/jenis biaya (listrik, sewa, gaji, dll.), Nominal, Catatan, dokumentasi, tanggal, dicatat oleh.
   4. Tombol **"+ Catat pengeluaran"** untuk membuat pengeluaran kustom.
   - Pengeluaran **tidak dihapus**, hanya diberi status **"Dibatalkan"** beserta alasan.
3. **(Dari Pemasukan)** Sama persis seperti Pengeluaran namun untuk pemasukan. Jenisnya:
   - **Penjualan** (otomatis dari Kasir; detail: nomor transaksi, daftar barang [jumlah dan harga], diskon, total, metode pembayaran, kasir, tanggal, status [Selesai / Dibatalkan / Diretur]).
   - **Kustom** (mis. modal tambahan; judul, nominal, catatan, dokumentasi, tanggal, dicatat oleh).
   - Karena Pemasukan jenis Penjualan sudah merupakan riwayat penjualan, **tidak ada layar "Riwayat Penjualan" terpisah**.
4. **(Dari Stok Menipis)** List Barang (object Barang; ketika ditekan masuk ke Profil Barang). Setiap baris memiliki tombol **Restock** yang membuka **bottom sheet** formulir Restock dengan barang sudah terisi.
5. **(Dari Fast Moving)** Sama persis seperti Stok Menipis namun untuk barang Fast Moving. Barang Fast Moving dengan stok masih tinggi diberi chip **"Stok aman"** agar tidak restock berlebihan, tombol tetap aktif.
6. **(Dari Slow Moving)** Sama persis seperti Stok Menipis namun untuk Slow Moving, **tanpa** tombol Restock.

**Catatan navigasi:** Profil Barang yang dibuka dari Dashboard tetap berada di tumpukan (back stack) Dashboard (tombol Kembali kembali ke daftar), **tidak** berpindah ke tab Manajemen.
