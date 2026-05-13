# Tugas 10 — Testing & Dependency Injection

**Nama:** Andika Rahman Pratama
**NIM:** 123140090
**Mata Kuliah:** Pengembangan Aplikasi Mobile (IF25-22017)
**Pertemuan:** 10 (Testing dan Dependency Injection)

---

## Deskripsi

Tugas ini melanjutkan project dari Tugas 9, menambahkan:
- Refactor Dependency Injection ke **3 Koin modules**
- **Unit Test** untuk Repository dan ViewModel (MockK)
- **Flow Test** dengan Turbine
- **UI Test** dengan Compose Test + Test Tags


## Struktur DI (Koin Modules)

| Module | Isi |
|--------|-----|
| `dataModule` | `DatabaseProvider`, `NoteRepository`, `SettingsManager`, platform APIs |
| `networkModule` | `HttpClient` (Ktor), `GeminiService`, `AIRepository` |
| `viewModelModule` | `NotesViewModel`, `ProfileViewModel`, `ChatViewModel` |

Semua module di-start via `MyApplication.kt` menggunakan `allModules`.


## Daftar Test Cases

### Unit Test — NoteRepository (6 test cases)
| # | Test Case | File |
|---|-----------|------|
| 1 | `getAllNotes returns flow from database` | `NoteRepositoryTest.kt` |
| 2 | `searchNotes returns flow from database` | `NoteRepositoryTest.kt` |
| 3 | `insertNote calls database insert` | `NoteRepositoryTest.kt` |
| 4 | `updateNote calls database update` | `NoteRepositoryTest.kt` |
| 5 | `deleteNote calls database delete` | `NoteRepositoryTest.kt` |
| 6 | `getNoteById calls database selectById` | `NoteRepositoryTest.kt` |

### Unit Test — NotesViewModel (5 test cases)
| # | Test Case | File |
|---|-----------|------|
| 1 | `initial search query is empty string` | `NotesViewModelTest.kt` |
| 2 | `updateSearchQuery changes search state` | `NotesViewModelTest.kt` |
| 3 | `addNote calls repository insertNote` | `NotesViewModelTest.kt` |
| 4 | `deleteNote calls repository deleteNote` | `NotesViewModelTest.kt` |
| 5 | `updateNote calls repository updateNote` | `NotesViewModelTest.kt` |

### Flow Test — ChatViewModel + Turbine (3 test cases)
| # | Test Case | File |
|---|-----------|------|
| 1 | `initial uiState has empty messages and not loading` | `ChatViewModelFlowTest.kt` |
| 2 | `kirimPesan adds user message to state` | `ChatViewModelFlowTest.kt` |
| 3 | `resetChat clears all messages` | `ChatViewModelFlowTest.kt` |

### UI Test — NotesScreen (4 test cases)
| # | Test Case | File |
|---|-----------|------|
| 1 | `emptyState shows empty message` | `NotesScreenTest.kt` |
| 2 | `fab button is displayed` | `NotesScreenTest.kt` |
| 3 | `search field is displayed and accepts input` | `NotesScreenTest.kt` |
| 4 | `fab click opens add note dialog` | `NotesScreenTest.kt` |


## Test Tags

Didefinisikan di `TestTags.kt` untuk UI testing yang konsisten:

```
NOTES_LIST, NOTE_CARD, EMPTY_STATE, SEARCH_FIELD,
FAB_ADD, DELETE_BUTTON, DIALOG_TITLE, DIALOG_CONTENT,
DIALOG_SAVE, DIALOG_CANCEL
```


## Cara Menjalankan Test

```bash
# Unit test (NoteRepository, NotesViewModel, ChatViewModel)
./gradlew testDebugUnitTest

# UI test (butuh emulator/device)
./gradlew connectedDebugAndroidTest
```


## Tech Stack

| Library | Kegunaan |
|---------|----------|
| Koin 3.5.3 | Dependency Injection |
| MockK 1.13.9 | Mocking untuk unit test |
| Turbine 1.2.0 | Testing Kotlin Flow |
| Coroutines Test 1.7.3 | Testing coroutine |
| Compose UI Test | UI testing dengan test tags |
| JUnit 4 | Test runner |


## Perubahan dari Tugas 9

1. **DI Refactor** — `appModule` dipecah jadi `dataModule`, `networkModule`, `viewModelModule`
2. **Interface Extraction** — `NoteRepositoryInterface` supaya ViewModel bisa di-test tanpa database
3. **Test Tags** — Ditambahkan ke `NotesScreens.kt` untuk UI testing
4. **Test Dependencies** — MockK, Turbine, Coroutines Test, Koin Test
5. **18 Test Cases Total** — 6 repo + 5 viewmodel + 3 flow + 4 UI