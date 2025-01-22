package com.example.project_BE.project_BE.impl;

import com.example.project_BE.project_BE.DTO.BuketDTO;
import com.example.project_BE.project_BE.exception.NotFoundException;
import com.example.project_BE.project_BE.model.Buket;
import com.example.project_BE.project_BE.model.Admin;
import com.example.project_BE.project_BE.repository.BuketRepository;
import com.example.project_BE.project_BE.repository.AdminRepository;
import com.example.project_BE.project_BE.servise.BuketService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BuketImpl implements BuketService {

    private static final String BASE_URL = "https://s3.lynk2.co/api/s3";
    private final BuketRepository buketRepository;
    private final AdminRepository adminRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public BuketImpl(BuketRepository buketRepository, AdminRepository adminRepository) {
        this.buketRepository = buketRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Buket> getAllBuket() {
        return buketRepository.findAll();
    }

    @Override
    public List<Buket> getAllByAdmin() {
        return BuketService.super.getAllByAdmin();
    }

    @Override
    public List<Buket> getAllByAdmin(Long idAdmin) {
        return buketRepository.findByAdminId(idAdmin);
    }

    @Override
    public Optional<Buket> getBuketById(Long id) {
        return buketRepository.findById(id);
    }

    @Override
    public BuketDTO tambahBuketDTO(Long idAdmin, BuketDTO buketDTO) {
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan"));

        Buket buket = new Buket();
        buket.setAdmin(admin);
        buket.setNamaBuket(buketDTO.getNamaBuket());
        buket.setHargaBuket(buketDTO.getHargaBuket());
        buket.setFotoUrl(buketDTO.getFotoUrl());

        try {
            Buket savedBuket = buketRepository.save(buket);
            System.out.println("Buket berhasil disimpan ke database dengan ID: " + savedBuket.getId());

            BuketDTO result = new BuketDTO();
            result.setId(savedBuket.getId());
            result.setIdAdmin(admin.getId());
            result.setNamaBuket(savedBuket.getNamaBuket());
            result.setHargaBuket(savedBuket.getHargaBuket());
            result.setFotoUrl(savedBuket.getFotoUrl());
            return result;

        } catch (Exception e) {
            System.err.println("Error saat menyimpan Buket: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Gagal menyimpan Buket");
        }
    }

    @Override
    public BuketDTO editBuketDTO(Long id, BuketDTO buketDTO, MultipartFile file) {
        Buket buket = buketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buket dengan ID " + id + " tidak ditemukan"));

        String oldFotoUrl = buket.getFotoUrl(); // Menyimpan URL foto lama

        // Update buket details (namaBuket and hargaBuket)
        buket.setNamaBuket(buketDTO.getNamaBuket());
        buket.setHargaBuket(buketDTO.getHargaBuket());

        // Check if a new file is provided
        if (file != null && !file.isEmpty()) {
            try {
                // Call replaceOldFoto with the MultipartFile instead of the fotoUrl String
                String newFotoUrl = replaceOldFoto(oldFotoUrl, file);
                buket.setFotoUrl(newFotoUrl); // Set foto URL baru
            } catch (IOException e) {
                throw new RuntimeException("Gagal mengganti foto: " + e.getMessage());
            }
        }

        // Simpan buket yang sudah diperbarui
        try {
            Buket savedBuket = buketRepository.save(buket);

            BuketDTO result = new BuketDTO();
            result.setId(savedBuket.getId());
            result.setIdAdmin(savedBuket.getAdmin().getId());
            result.setNamaBuket(savedBuket.getNamaBuket());
            result.setHargaBuket(savedBuket.getHargaBuket());
            result.setFotoUrl(savedBuket.getFotoUrl());
            return result;

        } catch (Exception e) {
            System.err.println("Error saat mengedit Buket: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Gagal mengedit Buket");
        }
    }

    @Override
    public void deleteBuket(Long id) throws IOException {
        buketRepository.deleteById(id);
    }

    public String replaceOldFoto(String oldFotoUrl, MultipartFile file) throws IOException {
        // Jika ada foto lama, kita ambil ID atau nama file dari URL lama (misalnya)
        String fileId = extractFileIdFromUrl(oldFotoUrl);

        // Kirim file baru dengan menggunakan API upload yang sama
        String uploadUrl = BASE_URL + "/uploadFoto";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        body.add("fileId", fileId);  // Mengirim fileId untuk menggantikan file lama

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return extractFileUrlFromResponse(response.getBody());
        } else {
            throw new IOException("Failed to upload and replace file: " + response.getStatusCode());
        }
    }

    private String extractFileIdFromUrl(String url) {
        // Logika untuk mengekstrak file ID dari URL (tergantung pada format URL yang diberikan)
        // Misalnya, jika URL-nya berbentuk: "https://s3.lynk2.co/somepath/fileId12345.jpg"
        String[] parts = url.split("/");
        return parts[parts.length - 1];  // Ambil bagian terakhir yang mengandung ID atau nama file
    }


    public String uploadFoto(MultipartFile file) throws IOException {
        String uploadUrl = BASE_URL + "/uploadFoto";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return extractFileUrlFromResponse(response.getBody());
        } else {
            throw new IOException("Failed to upload file: " + response.getStatusCode());
        }
    }

    private String extractFileUrlFromResponse(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(responseBody);
        JsonNode dataNode = jsonResponse.path("data");
        return dataNode.path("url_file").asText();
    }
}
