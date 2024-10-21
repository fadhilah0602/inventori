package inventori.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import inventori.example.demo.constant.Constant;
import inventori.example.demo.dto.BarangResponse;
import inventori.example.demo.dto.CreateBarangRequest;
import inventori.example.demo.dto.UpdateBarangRequest;
import inventori.example.demo.model.Barang;
import inventori.example.demo.repository.BarangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BarangService {

    private static final String[] ALLOWED_MIME_TYPES = {"image/jpg", "image/png"};

    @Autowired
    BarangRepository barangRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public BarangService(BarangRepository repository) {
        this.barangRepository = repository;
    }


    public ResponseEntity<Object> getBarangById(Long id) {
        Optional<Barang> entity = barangRepository.findById(id);

        if (entity.isPresent()) {
            Barang barang = entity.get();
            BarangResponse dto = new BarangResponse(
                    barang.getNamaBarang(),
                    barang.getJumlahStokBarang(),
                    barang.getNomorSeriBarang(),
                    barang.getAdditionalInfo().toString(),
                    barang.getGambarBarang());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }

    public List<BarangResponse> getListBarang(int limit) {
        Page<Barang> page = barangRepository.findAll(PageRequest.of(0, limit));
        return page.getContent().stream()
                .map(barang -> new BarangResponse(
                        barang.getNamaBarang(),
                        barang.getJumlahStokBarang(),
                        barang.getNomorSeriBarang(),
                        barang.getAdditionalInfo().toString(), // Ubah jika perlu
                        barang.getGambarBarang()))
                .collect(Collectors.toList());
    }

    public void deleteBarang(Long id) {
        if (!barangRepository.existsById(id)) {
            throw new IllegalArgumentException("Barang dengan ID " + id + " tidak ditemukan.");
        }
        barangRepository.deleteById(id);
    }

    public Barang createBarang(CreateBarangRequest createBarangRequest, MultipartFile gambarFile) throws IOException {
        if (gambarFile == null || gambarFile.isEmpty()) {
            throw new IllegalArgumentException("File gambar tidak boleh kosong.");
        }

        // Validasi MIME Type
        validateMimeType(gambarFile);

        byte[] fileBytes = gambarFile.getBytes();
        String gambarPath = saveImage(fileBytes, gambarFile.getOriginalFilename());

         //jika type data json
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonAdditionalInfo = objectMapper.readTree(String.valueOf(createBarangRequest.getAdditionalInfo()));

        // jika type data string
        String jsonAdditionalInfo = objectMapper.writeValueAsString(createBarangRequest.getAdditionalInfo());


        Barang barang = new Barang();
        barang.setNamaBarang(createBarangRequest.getNamaBarang());
        barang.setJumlahStokBarang(createBarangRequest.getJumlahStokBarang());
        barang.setNomorSeriBarang(createBarangRequest.getNomorSeriBarang());
        barang.setGambarBarang(gambarPath);
        barang.setAdditionalInfo(jsonAdditionalInfo);
        barang.setCreatedAt(LocalDateTime.now());
        barang.setCreatedBy(Constant.CREATED_BY);

        return barangRepository.save(barang);
    }

    private void validateMimeType(MultipartFile gambarFile) {
        String mimeType = gambarFile.getContentType();
        boolean isValidMimeType = Arrays.stream(ALLOWED_MIME_TYPES).anyMatch(mimeType::equals);

        if (!isValidMimeType) {
            throw new IllegalArgumentException("Hanya file JPG dan PNG yang diizinkan.");
        }
    }




    private String saveImage(byte[] fileBytes, String originalFilename) throws IOException {
        String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
        Path path = Paths.get("uploads/" + uniqueFilename);
        Files.createDirectories(path.getParent());
        Files.write(path, fileBytes);
        return path.toString();
    }

    public Barang updateBarang(Long id, UpdateBarangRequest updateRequest) throws IOException {
        Barang existingBarang = barangRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Barang tidak ditemukan."));

        // Update fields
        existingBarang.setNamaBarang(updateRequest.getNamaBarang());
        existingBarang.setJumlahStokBarang(updateRequest.getJumlahStokBarang());
        existingBarang.setNomorSeriBarang(updateRequest.getNomorSeriBarang());

        // Validasi dan simpan gambar
        if (updateRequest.getGambarBarang() != null) {
            validateMimeType(updateRequest.getGambarBarang());
            String gambarPath = saveImage(updateRequest.getGambarBarang()); // Panggilan metode yang benar
            existingBarang.setGambarBarang(gambarPath);
        }

        // Convert additionalInfo Map to JSON String
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonAdditionalInfo;
        try {
            jsonAdditionalInfo = objectMapper.writeValueAsString(updateRequest.getAdditionalInfo());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Format additionalInfo tidak valid.");
        }
//        existingBarang.setAdditionalInfo(jsonAdditionalInfo);

        existingBarang.setUpdatedAt(LocalDateTime.now());
        existingBarang.setUpdatedBy("system"); // Sesuaikan dengan kebutuhan

        return barangRepository.save(existingBarang);
    }


    private void validateMimeType2(MultipartFile file) {
        String mimeType = file.getContentType();
        if (!Arrays.asList(ALLOWED_MIME_TYPES).contains(mimeType)) {
            throw new IllegalArgumentException("Hanya file JPG dan PNG yang diizinkan.");
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        // Menghasilkan nama file unik
        String uniqueFilename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/" + uniqueFilename);
        Files.createDirectories(path.getParent()); // Pastikan direktori ada
        Files.write(path, file.getBytes()); // Simpan file
        return path.toString();
    }

}
