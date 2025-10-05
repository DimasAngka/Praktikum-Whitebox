package com.praktikum.whitebox.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Class Kategori")
public class KategoriTest {

    private Kategori kategori;

    @BeforeEach
    void setUp() {
        // Inisialisasi objek sebelum setiap tes
        kategori = new Kategori("ELE001", "Elektronik", "Semua produk elektronik rumah tangga dan gadget");
    }

    @Test
    @DisplayName("Test Constructor dan Status Default Aktif")
    void testConstructorAndAktifStatus() {
        assertEquals("ELE001", kategori.getKode());
        assertEquals("Elektronik", kategori.getNama());
        assertEquals("Semua produk elektronik rumah tangga dan gadget", kategori.getDeskripsi());
        assertTrue(kategori.isAktif(), "Status default kategori harus aktif (true)");
    }

    @Test
    @DisplayName("Test Getters dan Setters")
    void testGettersAndSetters() {
        // Test Setters
        kategori.setKode("FOOD002");
        kategori.setNama("Makanan");
        kategori.setDeskripsi("Semua jenis makanan dan minuman");
        kategori.setAktif(false);

        // Test Getters
        assertEquals("FOOD002", kategori.getKode());
        assertEquals("Makanan", kategori.getNama());
        assertEquals("Semua jenis makanan dan minuman", kategori.getDeskripsi());
        assertFalse(kategori.isAktif());
    }

    @Test
    @DisplayName("Test equals - Kode Sama (Harus True)")
    void testEqualsSameCode() {
        Kategori kategoriLain = new Kategori("ELE001", "Nama Berbeda", "Deskripsi Berbeda");

        // Objek dengan kode yang sama dianggap sama
        assertTrue(kategori.equals(kategoriLain));
        assertTrue(kategori.equals(kategori)); // Test dengan objek itu sendiri
    }

    @Test
    @DisplayName("Test equals - Kode Berbeda (Harus False)")
    void testEqualsDifferentCode() {
        Kategori kategoriBerbeda = new Kategori("FOOD002", "Makanan", "Deskripsi");

        // Kode berbeda dianggap tidak sama
        assertFalse(kategori.equals(kategoriBerbeda));
    }

    @Test
    @DisplayName("Test equals - Objek Null atau Tipe Berbeda (Harus False)")
    void testEqualsNullOrDifferentType() {
        // Test dengan null (Branch 1 di equals)
        assertFalse(kategori.equals(null));

        // Test dengan tipe objek berbeda (Branch 2 di equals)
        assertFalse(kategori.equals(new Object()));
    }

    @Test
    @DisplayName("Test hashCode - Kode Sama (Harus Sama)")
    void testHashCodeSameCode() {
        Kategori kategoriLain = new Kategori("ELE001", "Nama B", "Desk B");

        assertEquals(kategori.hashCode(), kategoriLain.hashCode(),
                "Hash code harus sama jika kode sama");
    }

    @Test
    @DisplayName("Test hashCode - Kode Berbeda (Biasanya Berbeda)")
    void testHashCodeDifferentCode() {
        Kategori kategoriBerbeda = new Kategori("FOOD002", "Makanan", "Deskripsi");

        assertNotEquals(kategori.hashCode(), kategoriBerbeda.hashCode(),
                "Hash code harus berbeda jika kode berbeda");
    }

    @Test
    @DisplayName("Test toString")
    void testToString() {
        String expected = "Kategori{kode='ELE001', nama='Elektronik', deskripsi='Semua produk elektronik rumah tangga dan gadget', aktif=true}";
        assertEquals(expected, kategori.toString());
    }
}