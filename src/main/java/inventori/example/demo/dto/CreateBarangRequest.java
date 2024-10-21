package inventori.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBarangRequest implements Serializable {

    private static final long serialVersionUID = 2791781078675756253L;

    private String namaBarang;
    private int jumlahStokBarang;
    private String nomorSeriBarang;
//    private Map<String, Object> additionalInfo;
    private AdditionalInfoDto additionalInfo;
    private byte[] gambarBarang;
}
