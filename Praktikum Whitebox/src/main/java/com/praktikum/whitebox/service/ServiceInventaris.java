package com.praktikum.whitebox.service;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import com.praktikum.whitebox.util.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class ServiceInventaris {
    private final RepositoryProduk repositoryProduk;

    public ServiceInventaris(RepositoryProduk repositoryProduk) {
        this.repositoryProduk = repositoryProduk;
    }
    public boolean updateStok(String kode, int stokBaru) {
        if (!ValidationUtils.isValidKodeProduk(kode) || stokBaru < 0) { // <--- Branch 1
            return false;
        }

        Optional<Produk> produk = repositoryProduk.cariByKode(kode);
        if (!produk.isPresent()) { // <--- Branch 2
            return false;
        }

        return repositoryProduk.updateStok(kode, stokBaru); // <--- Jalur Sukses
    }

    public boolean tambahProduk(Produk produk) {
        if (!ValidationUtils.isValidProduk(produk)) {
            return false;
        }

        // Cek apakah produk dengan kode yang sama sudah ada
        Optional<Produk> produkExist = repositoryProduk.cariByKode(produk.getKode());
        if (produkExist.isPresent()) {
            return false;
        }

        return repositoryProduk.simpan(produk);
    }

    public boolean keluarStok(String kode, int jumlah) {
        if (!ValidationUtils.isValidKodeProduk(kode) || jumlah <= 0) {
            return false;
        }

        Optional<Produk> produkOpt = repositoryProduk.cariByKode(kode);
        if (produkOpt.isEmpty() || !produkOpt.get().isAktif()) {
            return false;
        }

        Produk produk = produkOpt.get();
        if (produk.getStok() < jumlah) {
            return false;
        }

        int stokBaru = produk.getStok() - jumlah;
        return repositoryProduk.updateStok(kode, stokBaru);
    }

    public List<Produk> getProdukStokMenipis() {
        return repositoryProduk.cariProdukStokMenipis();
    }

    public List<Produk> getProdukStokHabis() {
        return repositoryProduk.cariProdukStokHabis(); // Tidak ada logika sama sekali
    }

    public double hitungTotalNilaiInventaris() {
        List<Produk> semuaProduk = repositoryProduk.cariSemua();
        return semuaProduk.stream()
                .filter(Produk::isAktif)
                .mapToDouble(p -> p.getHarga() * p.getStok())
                .sum();
    }

    public int hitungTotalStok() {
        List<Produk> semuaProduk = repositoryProduk.cariSemua();
        return semuaProduk.stream()
                .filter(Produk::isAktif) // Wajib, sesuai logic hitungTotalNilaiInventaris
                .mapToInt(Produk::getStok) // Mengambil stok dan menjumlahkan
                .sum();
    }

}