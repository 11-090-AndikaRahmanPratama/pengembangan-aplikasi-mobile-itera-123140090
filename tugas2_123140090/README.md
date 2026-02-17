# Digital News Ticker v2.0

**Tugas 2 — 123140090**

Aplikasi console news ticker berbasis **Kotlin Coroutines** dan **Flow**. Program ini mensimulasikan stream berita digital dengan berbagai kategori dan pemrosesan async.

## Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| **Live Stream** | Menggunakan `Flow` untuk memancarkan berita secara terus menerus (infinite stream). |
| **Channel Filter** | Filter berita berdasarkan kategori (Tech, Sports, Finance, dll) secara real-time. |
| **Async Processing** | Fetch detail berita menggunakan `async/await` tanpa memblokir stream utama. |
| **Non-blocking IO** | Input user ditangani di `Dispatchers.IO` agar tidak mengganggu animasi ticker. |
| **Reactive State** | Counter berita dipantau menggunakan `StateFlow`. |

## Struktur File

```
tugas2_123140090/
├── build.gradle.kts           # Config dependency (Coroutines 1.7+)
├── src/main/kotlin/
│   └── SimpleNewsTicker.kt    # Source code utama (Single-file implementation)
```

## Cara Menjalankan

1. **Build & Run via Gradle**:
   ```bash
   ./gradlew run --console=plain
   ```
   *(Gunakan `--console=plain` agar output tidak tertumpuk log Gradle)*

2. **Interaksi**:
   Ketik perintah berikut saat program berjalan:
   - `all` : Tampilkan semua kategori
   - `tech` : Filter berita Teknologi
   - `sport`: Filter berita Olahraga
   - `biz`  : Filter berita Bisnis/Ekonomi
   - `ent`  : Filter berita Hiburan
   - `read` : Simulasi baca detail (request parallel)
   - `exit` : Keluar aplikasi

## Teknologi

- Kotlin 1.9+
- Kotlinx Coroutines Core

---
*Dikembangkan oleh Andika Rahman Pratama NIM 123140090*
