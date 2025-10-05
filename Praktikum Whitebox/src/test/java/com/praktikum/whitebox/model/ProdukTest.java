package com.praktikum.whitebox.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Class Produk - White Box Testing")
public class ProdukTest {
    private Produk produk;

    @BeforeEach
    void setUp() {
        produk = new Produk("PROD001", "Laptop Gaming", "Elektronik", 10000000, 10, 5);
    }

    @Test
    @DisplayName("Test status stok - stok aman")
    void testStokAman() {
        produk.setStok(10);
        produk.setStokMinimum(5);

        assertTrue(produk.isStokAman());
        assertFalse(produk.isStokMenipis());
        assertFalse(produk.isStokHabis());
    }

    @Test
    @DisplayName("Test status stok - stok menipis")
    void testStokMenipis() {
        produk.setStok(5);
        produk.setStokMinimum(5);

        assertFalse(produk.isStokAman());
        assertTrue(produk.isStokMenipis());
        assertFalse(produk.isStokHabis());
    }

    @Test
    @DisplayName("Test status stok - stok habis")
    void testStokHabis() {
        produk.setStok(0);
        produk.setStokMinimum(5);

        assertFalse(produk.isStokAman());
        assertFalse(produk.isStokMenipis());
        assertTrue(produk.isStokHabis());
    }

    // Annotasi untuk menjalankan tes dengan berbagai parameter
    @ParameterizedTest
    @DisplayName("Test kurangi stok dengan berbagai nilai")
    @CsvSource({
            "5, 5", // kurangi 5 dari 10, sisa 5
            "3, 7", // kurangi 3 dari 10, sisa 7
            "10, 0" // kurangi semua stok
    })
    void testKurangiStokValid(int jumlah, int expectedStok) {
        produk.kurangiStok(jumlah);
        assertEquals(expectedStok, produk.getStok());
    }

    @Test
    @DisplayName("Test kurangi stok - jumlah negatif")
    void testKurangiStokNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.kurangiStok(-5);
        });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test kurangi stok - stok tidak mencukupi")
    void testKurangiStokTidakMencukupi() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.kurangiStok(15);
        });
        assertEquals("Stok tidak mencukupi", exception.getMessage());
    }

    @Test
    @DisplayName("Test tambah stok valid")
    void testTambahStokValid() {
        produk.tambahStok(5);
        assertEquals(15, produk.getStok());
    }

    @Test
    @DisplayName("Test tambah stok - jumlah negatif")
    void testTambahStokNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.tambahStok(-5);
        });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    // Annotasi untuk menjalankan tes dengan berbagai parameter
    @ParameterizedTest
    @DisplayName("Test hitung total harga")
    @CsvSource({
            "1, 10000000",
            "2, 20000000"
    })
    void testHitungTotalHarga(int jumlah, double expectedTotal) {
        double total = produk.hitungTotalHarga(jumlah);
        assertEquals(expectedTotal, total, 0.001); // 0.001 adalah delta untuk perbandingan double
    }

    @Test
    @DisplayName("Test hitung total harga - jumlah negatif")
    void testHitungTotalHargaNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.hitungTotalHarga(-1);
        });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test equals dan hashCode")
    void testEqualsAndHashCode() {
        Produk produk1 = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 5, 2);
        Produk produk2 = new Produk("PROD001", "Laptop Baru", "Elektronik", 12000000, 3, 1);
        Produk produk3 = new Produk("PROD002", "Mouse", "Elektronik", 50000, 10, 5);

        assertEquals(produk1, produk2); // kode sama
        assertNotEquals(produk1, produk3); // kode berbeda
        assertEquals(produk1.hashCode(), produk2.hashCode());
    }
    @Test
    @DisplayName("Equals: Gagal - Objek adalah null (Menutup cabang o == null)")
    void testEquals_GagalObjekNull() {
        // Memaksa o == null menjadi TRUE. Ini menguji sisi KIRI dari '||'.
        assertNotEquals(produk, null);
    }

    @Test
    @DisplayName("Equals: Gagal - Objek bukan kelas Produk (Menutup cabang getClass() != o.getClass())")
    void testEquals_GagalKelasBerbeda() {
        // Memaksa getClass() != o.getClass() menjadi TRUE. Ini menguji sisi KANAN dari '||'.
        Object objekLain = new Object();
        assertNotEquals(produk, objekLain);
    }
    @Test
    @DisplayName("ToString: Memastikan format string telah dipanggil")
    void testToString() {
        // Arrange: Buat objek Produk yang valid
        Produk produk = new Produk("PROD001", "Laptop Gaming", "Elektronik", 1000000.0, 10, 5);

        // Act: Panggil toString()
        String result = produk.toString();

        // Assert: Pastikan output string mengandung elemen kunci
        assertTrue(result.contains("PROD001"));
        assertTrue(result.contains("Laptop Gaming"));
        assertTrue(result.contains("harga=1000000.0"));
        assertTrue(result.startsWith("Produk{"));
        // Dimas Angka Wijaya
    }
}