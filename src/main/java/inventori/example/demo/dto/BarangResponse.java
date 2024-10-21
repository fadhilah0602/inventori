package inventori.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BarangResponse implements Serializable {

    private static final long serialVersionUID = 2791781078675756255L;

    private String namaBarang;
    private int jumlahStokBarang;
    private String nomorSeriBarang;
    private String additionalInfo;
    private String gambarBarang;


}
