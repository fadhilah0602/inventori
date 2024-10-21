package inventori.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Map;

public class UpdateBarangRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String namaBarang;
    private int jumlahStokBarang;
    private String nomorSeriBarang;
    private Map<String, Object> additionalInfo; // Dynamic structure
    private MultipartFile gambarBarang; // Gambar dapat di-upload

    // Getter dan Setter
    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getJumlahStokBarang() {
        return jumlahStokBarang;
    }

    public void setJumlahStokBarang(int jumlahStokBarang) {
        this.jumlahStokBarang = jumlahStokBarang;
    }

    public String getNomorSeriBarang() {
        return nomorSeriBarang;
    }

    public void setNomorSeriBarang(String nomorSeriBarang) {
        this.nomorSeriBarang = nomorSeriBarang;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public MultipartFile getGambarBarang() {
        return gambarBarang;
    }

    public void setGambarBarang(MultipartFile gambarBarang) {
        this.gambarBarang = gambarBarang;
    }
}
