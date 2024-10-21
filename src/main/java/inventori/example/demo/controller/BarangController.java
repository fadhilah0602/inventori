package inventori.example.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import inventori.example.demo.dto.AdditionalInfoDto;
import inventori.example.demo.dto.BarangResponse;
import inventori.example.demo.dto.CreateBarangRequest;
import inventori.example.demo.dto.UpdateBarangRequest;
import inventori.example.demo.model.Barang;
import inventori.example.demo.service.BarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class BarangController {

    @Autowired
    private BarangService barangService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<BarangResponse> getListBarang(@RequestParam int limit) {
        return barangService.getListBarang(limit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEntityById(@PathVariable Long id) {
        return barangService.getBarangById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBarang(@PathVariable Long id) {
        try {
            barangService.deleteBarang(id);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // HTTP 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // HTTP 500 Internal Server Error
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Barang> updateBarang(
            @PathVariable Long id,
            @RequestParam(value = "gambarBarang", required = false) MultipartFile gambarBarang,
            @RequestParam("namaBarang") String namaBarang,
            @RequestParam("jumlahStokBarang") int jumlahStokBarang,
            @RequestParam("nomorSeriBarang") String nomorSeriBarang,
            @RequestParam("additionalInfo") String additionalInfo
    ) {
        UpdateBarangRequest updateRequest = new UpdateBarangRequest();
        updateRequest.setNamaBarang(namaBarang);
        updateRequest.setJumlahStokBarang(jumlahStokBarang);
        updateRequest.setNomorSeriBarang(nomorSeriBarang);

        // Mengonversi string JSON menjadi Map
        try {
            Map<String, Object> additionalInfoMap = new ObjectMapper().readValue(additionalInfo, Map.class);
            updateRequest.setAdditionalInfo(additionalInfoMap);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Barang updatedBarang = barangService.updateBarang(id, updateRequest);
            return ResponseEntity.ok(updatedBarang);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<Barang> createBarang(
            @RequestParam("gambarBarang") MultipartFile gambarBarang,
            @RequestParam("namaBarang") String namaBarang,
            @RequestParam("jumlahStokBarang") int jumlahStokBarang,
            @RequestParam("nomorSeriBarang") String nomorSeriBarang,
            @RequestParam("additionalInfo") String additionalInfoJson // Menggunakan nama yang lebih jelas
    ) {
        CreateBarangRequest createBarangRequest = new CreateBarangRequest();
        createBarangRequest.setNamaBarang(namaBarang);
        createBarangRequest.setJumlahStokBarang(jumlahStokBarang);
        createBarangRequest.setNomorSeriBarang(nomorSeriBarang);

        // Mengonversi string JSON menjadi AdditionalInfoDto
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AdditionalInfoDto additionalInfo = objectMapper.readValue(additionalInfoJson, AdditionalInfoDto.class);

            // Mengatur additionalInfo langsung ke CreateBarangRequest
            createBarangRequest.setAdditionalInfo(additionalInfo);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Barang createdBarang = barangService.createBarang(createBarangRequest, gambarBarang);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBarang);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }







}
