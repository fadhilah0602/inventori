package inventori.example.demo.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "barang")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Barang  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nama_barang", length = 100)
    private String namaBarang;

    @Column(name = "jumlah_stok_barang")
    private int jumlahStokBarang;

    @Column(name = "nomor_seri_barang")
    private String nomorSeriBarang;

    @Column(name = "additional_info", columnDefinition = "json")
    private String additionalInfo;
//    @Column(columnDefinition = "json")
//    private JsonNode additionalInfo;

//    public JsonNode getAdditionalInfoAsJsonNode() {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.readTree(this.additionalInfo);
//        } catch (Exception e) {
//            return null; // atau throw exception sesuai kebutuhan
//        }
//    }
//
//    public void setAdditionalInfoFromJsonNode(JsonNode jsonNode) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            this.additionalInfo = objectMapper.writeValueAsString(jsonNode);
//        } catch (Exception e) {
//            // Handle exception
//        }
//    }

    @Column(name = "gambar_barang")
    private String gambarBarang;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

}
