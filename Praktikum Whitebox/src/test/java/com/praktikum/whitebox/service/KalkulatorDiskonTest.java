package com.praktikum.whitebox.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Kalkulator Diskon - Path Coverage")
public class KalkulatorDiskonTest {
    private KalkulatorDiskon kalkulatorDiskon;

    @BeforeEach
    void setUp() {
        kalkulatorDiskon = new KalkulatorDiskon();
    }

    // Annotasi untuk menjalankan tes dengan berbagai parameter
    @ParameterizedTest
    @DisplayName("Test hitung diskon - berbagai kombinasi kuantitas dan tipe pelanggan")
    @CsvSource({
            // kuantitas, tipePelanggan, expectedDiskon
            // 1, "BARU", 20, // 2% dari 1000
            "1, 'BARU', 20", // 2% dari 1000*1
            "5, 'BARU', 350", // 5% + 2% = 7% dari 1000*5
            "10, 'REGULER', 1500", // 10% + 5% = 15% dari 1000*10
            "50, 'PREMIUM', 12500", // 15% + 10% = 25% dari 1000*50
            "100, 'PREMIUM', 30000", // 20% + 10% = 30% (maksimal) dari 1000*100
            "200, 'PREMIUM', 60000" // 20% + 10% = 30% (maksimal) dari 1000*200
    })
    void testHitungDiskonVariousCases(int kuantitas, String tipePelanggan, double expectedDiskon) {
        double harga = 1000;
        double diskon = kalkulatorDiskon.hitungDiskon(harga, kuantitas, tipePelanggan);
        assertEquals(expectedDiskon, diskon, 0.001);
    }

    @Test
    @DisplayName("Test hitung diskon - parameter invalid")
    void testHitungDiskonInvalidParameters() {
        // Harga negatif
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            kalkulatorDiskon.hitungDiskon(-1000, 5, "REGULER");
        });
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());

        // Kuantitas nol
        exception = assertThrows(IllegalArgumentException.class, () -> {
            kalkulatorDiskon.hitungDiskon(1000, 0, "REGULER");
        });
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test hitung harga setelah diskon")
    void testHitungHargaSetelahDiskon() {
        double harga = 1000;
        int kuantitas = 10;
        String tipePelanggan = "REGULER";

        double hargaSetelahDiskon = kalkulatorDiskon.hitungHargaSetelahDiskon(harga, kuantitas, tipePelanggan);

        // Perhitungan manual:
        // Total sebelum diskon = 1000 * 10 = 10000
        // Diskon = 10% (kuantitas 10) + 5% (REGULER) = 15%
        double totalSebelumDiskon = 1000 * 10; // 10000
        double expectedDiskon = totalSebelumDiskon * 0.15; // 1500
        double expectedHargaAkhir = totalSebelumDiskon - expectedDiskon; // 10000 - 1500 = 8500

        assertEquals(expectedHargaAkhir, hargaSetelahDiskon, 0.001);
    }
}