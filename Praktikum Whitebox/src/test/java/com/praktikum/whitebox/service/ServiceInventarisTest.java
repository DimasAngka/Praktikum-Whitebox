package com.praktikum.whitebox.service;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when; // Ganti ini dengan baris yang Anda butuhkan
import static org.mockito.Mockito.verify; // Ganti ini dengan baris yang Anda butuhkan
import static org.mockito.Mockito.never; // Ganti ini dengan baris yang Anda butuhkan
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class) // HANYA gunakan ini untuk setup Mockito
public class ServiceInventarisTest {

    @Mock
    private RepositoryProduk mockRepositoryProduk;

    // Gunakan HANYA SATU deklarasi serviceInventaris, biarkan Mockito yang mengisinya
    @InjectMocks
    private ServiceInventaris serviceInventaris;

    // Deklarasi variabel Produk
    private Produk produkTest;
    private Produk validProduk;
    private final String VALID_KODE = "PROD001";

    // HANYA SATU metode setUp
    @BeforeEach
    void setUp() {
        // Produk untuk test case awal
        produkTest = new Produk("PROD001", "Laptop Gaming", "Elektronik", 15000000, 10, 5);
        produkTest.setAktif(true);

        // Produk untuk test case yang menggunakan validProduk
        validProduk = new Produk(VALID_KODE, "Laptop", "Elektronik", 5000000.0, 10, 5);
        validProduk.setAktif(true);
    }

    @Test
    @DisplayName("Tambah produk berhasil - semua kondisi valid")
    void testTambahProdukBerhasil() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        when(mockRepositoryProduk.simpan(produkTest)).thenReturn(true);

        // Act
        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        // Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).simpan(produkTest);
    }


    @Test
    @DisplayName("Tambah produk gagal - produk sudah ada")
    void testTambahProdukGagalSudahAda() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        // Act
        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        // Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).simpan(any());
    }

    @Test
    @DisplayName("Tambah Produk Gagal - Validasi Harga Tidak Valid (0 atau Negatif)")
    void testTambahProdukGagalHargaTidakValid() {
        // 1. Buat produk dengan harga 0.
        // Harga 0 akan menyebabkan ValidationUtils.isValidHarga(0) mengembalikan false,
        // yang berarti isValidProduk(produk) mengembalikan false.
        Produk produkHargaInvalid = new Produk("PROD005", "Buku", "Alat Tulis", 0, 10, 5); // Harga 0

        // 2. MOCK: Mock untuk kode spesifik

        // 3. ACT & ASSERT: Harusnya gagal (menutup branch if (!isValidProduk))
        assertFalse(serviceInventaris.tambahProduk(produkHargaInvalid));

        // 4. VERIFY: Pastikan simpan tidak dipanggil (menggunakan any() yang sudah dikoreksi)
        verify(mockRepositoryProduk, never()).simpan(any());

        verify(mockRepositoryProduk, never()).cariByKode(anyString());
    }



    @Test
    @DisplayName("Keluar stok berhasil - stok mencukupi")
    void testKeluarStokBerhasil() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 5)).thenReturn(true);

        // Act
        boolean hasil = serviceInventaris.keluarStok("PROD001", 5); // Kurangi 5 dari 10, sisa 5

        // Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 5);
    }

    @Test
    @DisplayName("Keluar stok gagal - stok tidak mencukupi")
    void testKeluarStokGagalStokTidakMencukupi() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        // Act
        boolean hasil = serviceInventaris.keluarStok("PROD001", 15); // Kurangi 15 dari 10 (gagal)

        // Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Hitung total nilai inventaris")
    void testHitungTotalNilaiInventaris() {
        // Arrange
        Produk produk1 = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 2, 1); // Aktif
        Produk produk2 = new Produk("PROD002", "Mouse", "Elektronik", 500000, 5, 2); // Aktif
        // Produk non-aktif harus diabaikan dalam perhitungan
        Produk produkNonAktif = new Produk("PROD003", "Keyboard", "Elektronik", 300000, 3, 1);
        produkNonAktif.setAktif(false);

        List<Produk> semuaProduk = Arrays.asList(produk1, produk2, produkNonAktif);
        when(mockRepositoryProduk.cariSemua()).thenReturn(semuaProduk);

        // Act
        double totalNilai = serviceInventaris.hitungTotalNilaiInventaris();

        // Assert
        // Perhitungan: (10000000 * 2) + (500000 * 5) = 20000000 + 2500000 = 22500000
        double expected = (10000000 * 2) + (500000 * 5); // hanya produk aktif
        assertEquals(expected, totalNilai, 0.001);
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Get produk stok menipis")
    void testGetProdukStokMenipis() {
        // Arrange
        Produk produkStokAman = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 10, 5); // Stok > Min
        Produk produkStokMenipis = new Produk("PROD002", "Mouse", "Elektronik", 500000, 3, 5); // Stok <= Min

        List<Produk> produkMenipis = Collections.singletonList(produkStokMenipis);
        when(mockRepositoryProduk.cariProdukStokMenipis()).thenReturn(produkMenipis);

        // Act
        List<Produk> hasil = serviceInventaris.getProdukStokMenipis();

        // Assert
        assertEquals(1, hasil.size());
        assertEquals("PROD002", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokMenipis();
    }

    @Test
    @DisplayName("Keluar Stok: Gagal - Produk Tidak Aktif")
    void testKeluarStok_GagalProdukTidakAktif() {
        // 1. Buat produk yang sama, tapi set aktif = false
        validProduk.setAktif(false);

        // 2. Mock: Produk ditemukan, tapi tidak aktif
        when(mockRepositoryProduk.cariByKode(VALID_KODE)).thenReturn(Optional.of(validProduk));

        // 3. Ekspektasi: Gagal keluar stok
        assertFalse(serviceInventaris.keluarStok(VALID_KODE, 5));
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }
    @Test
    @DisplayName("Update Stok: Gagal - Stok Baru Negatif")
    void testUpdateStok_GagalStokNegatif() {
        // Menutup jalur if (ValidationUtils.isValidKodeProduk(kode) || stokBaru < 0)
        assertFalse(serviceInventaris.updateStok(VALID_KODE, -10));
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
    }

    @Test
    @DisplayName("Update Stok: Gagal - Produk Tidak Ditemukan")
    void testUpdateStok_GagalTidakDitemukan() {
        // Mock: Produk tidak ditemukan di repository
        when(mockRepositoryProduk.cariByKode(VALID_KODE)).thenReturn(Optional.empty());

        // Coba update stok produk yang tidak ada
        assertFalse(serviceInventaris.updateStok(VALID_KODE, 20));
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Update Stok: Berhasil")
    void testUpdateStok_Berhasil() {
        final int STOK_BARU = 20; // Gunakan konstanta atau langsung nilai 20
        when(mockRepositoryProduk.cariByKode(VALID_KODE)).thenReturn(Optional.of(validProduk));
        // Mock updateStok di repository mengembalikan true
        when(mockRepositoryProduk.updateStok(VALID_KODE, STOK_BARU)).thenReturn(true);

        assertTrue(serviceInventaris.updateStok(VALID_KODE, STOK_BARU));
        verify(mockRepositoryProduk).updateStok(VALID_KODE, STOK_BARU);
    }
    @Test
    @DisplayName("Query: Hitung Total Stok (Berhasil)")
    void testHitungTotalStok_Berhasil() {
        // Siapkan dua produk: 1 aktif (stok 10), 1 tidak aktif (stok 20)
        Produk produkTidakAktif = new Produk("PROD002", "Monitor", "Elektronik", 1000000.0, 20, 5);
        produkTidakAktif.setAktif(false); // Penting: Hanya produk aktif yang dihitung!

        // Mock: cariSemua mengembalikan daftar kedua produk
        when(mockRepositoryProduk.cariSemua()).thenReturn(List.of(validProduk, produkTidakAktif));

        // Total stok AKTIF adalah 10. (Lihat ServiceInventaris.java: ada filter isAktif)
        int total = serviceInventaris.hitungTotalStok();
        assertEquals(10, total);
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Query: Hitung Total Stok (Hanya Produk Aktif)")
    void testHitungTotalStok_HanyaProdukAktif() {
        Produk produkTidakAktif = new Produk("PROD002", "Monitor", "Elektronik", 1000000.0, 20, 5);
        produkTidakAktif.setAktif(false);

        assertTrue(validProduk.isAktif(), "Error: validProduk harusnya aktif (true)");
        assertFalse(produkTidakAktif.isAktif(), "Error: produkTidakAktif harusnya non-aktif (false)");

        when(mockRepositoryProduk.cariSemua()).thenReturn(List.of(validProduk, produkTidakAktif));

        int total = serviceInventaris.hitungTotalStok();
        assertEquals(10, total);
    }

    @Test
    @DisplayName("Query: Hitung Total Stok (List Kosong)")
    void testHitungTotalStok_ListKosong() {
        // Mock: mengembalikan LIST KOSONG
        when(mockRepositoryProduk.cariSemua()).thenReturn(Collections.emptyList());

        int total = serviceInventaris.hitungTotalStok();

        // ASSERT: Ekspektasi harus 0
        assertEquals(0, total);
    }

    @Test
    @DisplayName("Query: Hitung Total Nilai Inventaris (Berhasil)")
    void testHitungTotalNilaiInventaris_Berhasil() {
        // validProduk: Harga 5.000.000, Stok 10. Nilai: 50.000.000
        // Mock: cariSemua mengembalikan validProduk
        when(mockRepositoryProduk.cariSemua()).thenReturn(List.of(validProduk));

        // Perhitungan nilai adalah harga * stok
        double totalNilai = serviceInventaris.hitungTotalNilaiInventaris();
        assertEquals(50000000.0, totalNilai, 0.001); // 0.001 adalah delta untuk perbandingan double
    }


    @Test
    @DisplayName("Query: Get Produk Stok Habis (List Kosong)")
    void testGetProdukStokHabis_ListKosong() {
        // MOCK: Repository harus mengembalikan LIST KOSONG
        when(mockRepositoryProduk.cariProdukStokHabis()).thenReturn(Collections.emptyList());

        List<Produk> result = serviceInventaris.getProdukStokHabis();

        // ASSERT: Berharap kosong
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(mockRepositoryProduk).cariProdukStokHabis();
    }


    @Test
    @DisplayName("Query: Get Produk Stok Habis (Data Ditemukan)")
    void testGetProdukStokHabis_DataDitemukan() {
        Produk produkHabis = new Produk("PROD003", "Mouse", "Aksesoris", 100000.0, 0, 5);

        // MOCK: Repository harus mengembalikan LIST DENGAN DATA
        when(mockRepositoryProduk.cariProdukStokHabis()).thenReturn(List.of(produkHabis));

        List<Produk> result = serviceInventaris.getProdukStokHabis();

        // ASSERT: Berharap ada isinya dan isinya benar
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("PROD003", result.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokHabis();
    }

    @Test
    @DisplayName("Query: Get Produk Stok Menipis")
    void testGetProdukStokMenipis_Berhasil() {
        // Asumsi: repositoryProduk.cariProdukStokMenipis() mengembalikan list yang benar
        when(mockRepositoryProduk.cariProdukStokMenipis()).thenReturn(List.of(validProduk));

        List<Produk> result = serviceInventaris.getProdukStokMenipis();
        assertFalse(result.isEmpty());
    }
}