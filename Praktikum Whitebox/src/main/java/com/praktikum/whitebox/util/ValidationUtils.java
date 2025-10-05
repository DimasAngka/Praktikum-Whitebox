package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Produk;

public class ValidationUtils {

    public static boolean isValidKodeProduk(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            return false;
        }

        String kodeBersih = kode.trim();
        // Validasi kode: 3-10 karakter, alfanumerik (huruf besar/kecil dan angka)
        return kodeBersih.matches("^[A-Za-z0-9]{3,10}$");
    }

    // Validasi nama (3-100 karakter, boleh huruf, angka, spasi)
    public static boolean isValidNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            return false;
        }

        String namaBersih = nama.trim();
        // Hanya validasi panjang. Asumsi: regex untuk huruf/angka/spasi tidak disalin dari gambar.
        return namaBersih.length() >= 3 && namaBersih.length() <= 100;
    }

    // Validasi harga (harus positif)
    public static boolean isValidHarga(double harga) {
        return harga > 0;
    }

    // Validasi stok (non-negatif)
    public static boolean isValidStok(int stok) {
        return stok >= 0;
    }

    // Validasi stok minimum (non-negatif)
    public static boolean isValidStokMinimum(int stokMinimum) {
        return stokMinimum >= 0;
    }

    // Validasi produk lengkap
    public static boolean isValidProduk(Produk produk) {
        if (produk == null) {
            return false;
        }

        return isValidKodeProduk(produk.getKode())
                && isValidNama(produk.getNama())
                && isValidNama(produk.getKategori()) // asumsi kategori divalidasi dengan aturan nama
                && isValidHarga(produk.getHarga())
                && isValidStok(produk.getStok())
                && isValidStokMinimum(produk.getStokMinimum());
    }

}