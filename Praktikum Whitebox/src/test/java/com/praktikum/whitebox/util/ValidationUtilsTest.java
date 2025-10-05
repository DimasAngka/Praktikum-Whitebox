package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Produk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    // --- SETUP UNTUK TEST isValidProduk ---
    private Produk createValidProduk() {
        // Pastikan Kode ("PROD123") valid, Nama/Kategori ("Mouse"/"Aksesoris") valid, Harga (>0), Stok (>=0), StokMin (>=0)
        return new Produk("PROD123", "Mouse", "Aksesoris", 1000000.0, 10, 5);
    }

    // ----------------------------------------------------------------------
    // 1. Tes untuk isValidKodeProduk (Menutup Branch || di baris 8-9)
    // ----------------------------------------------------------------------
    @Test
    @DisplayName("Kode Produk: Berhasil")
    void testIsValidKodeProduk_Berhasil() {
        assertTrue(ValidationUtils.isValidKodeProduk("PROD001"));
    }

    @Test
    @DisplayName("Kode Produk: Gagal - Null")
        // Menutup cabang kode == null
    void testIsValidKodeProduk_GagalNull() {
        assertFalse(ValidationUtils.isValidKodeProduk(null));
    }

    @Test
    @DisplayName("Kode Produk: Gagal - Kosong/Blank")
        // Menutup cabang kode.trim().isEmpty()
    void testIsValidKodeProduk_GagalEmptyOrBlank() {
        assertFalse(ValidationUtils.isValidKodeProduk(""));
        assertFalse(ValidationUtils.isValidKodeProduk("   "));
    }

    // ----------------------------------------------------------------------
    // 2. Tes untuk isValidNama (Menutup Branch || di baris 19-20)
    // ----------------------------------------------------------------------
    @Test
    @DisplayName("Nama: Berhasil")
    void testIsValidNama_Berhasil() {
        assertTrue(ValidationUtils.isValidNama("Laptop Gaming"));
    }

    @Test
    @DisplayName("Nama: Gagal - Null")
        // Menutup cabang nama == null
    void testIsValidNama_GagalNull() {
        assertFalse(ValidationUtils.isValidNama(null));
    }

    @Test
    @DisplayName("Nama: Gagal - Kosong/Blank")
        // Menutup cabang nama.trim().isEmpty()
    void testIsValidNama_GagalEmptyOrBlank() {
        assertFalse(ValidationUtils.isValidNama(""));
        assertFalse(ValidationUtils.isValidNama("   "));
    }

    @Test
    @DisplayName("Nama: Gagal - Terlalu Pendek")
    void testIsValidNama_GagalPendek() {
        assertFalse(ValidationUtils.isValidNama("A"));
    }

    // ----------------------------------------------------------------------
    // 3. Tes untuk isValidProduk (Menutup Merah/Kuning di baris 45-54)
    // ----------------------------------------------------------------------
    @Test
    @DisplayName("Produk Lengkap: Berhasil - Semua Valid")
    void testIsValidProduk_Berhasil() {
        assertTrue(ValidationUtils.isValidProduk(createValidProduk()));
    }

    @Test
    @DisplayName("Produk Lengkap: Gagal - Produk adalah null")
        // Menutup baris 45-46
    void testIsValidProduk_GagalNull() {
        assertFalse(ValidationUtils.isValidProduk(null));
    }


    @Test
    @DisplayName("Produk Lengkap: Gagal - Harga Invalid")
    void testIsValidProduk_GagalHarga() {
        Produk produk = createValidProduk();
        produk.setHarga(-1.0); // Gagal di isValidHarga
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

    @Test
    @DisplayName("Produk Lengkap: Gagal - Stok Invalid")
    void testIsValidProduk_GagalStok() {
        Produk produk = createValidProduk();
        produk.setStok(-1); // Gagal di isValidStok
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

    @Test
    @DisplayName("Produk Lengkap: Gagal - Stok Minimum Invalid")
    void testIsValidProduk_GagalStokMinimum() {
        Produk produk = createValidProduk();
        produk.setStokMinimum(-5); // Gagal di isValidStokMinimum
        assertFalse(ValidationUtils.isValidProduk(produk));
        // Dimas Angka Wijaya
    }
}