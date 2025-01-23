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

@Service
public class BuketImpl implements BuketService {

    private static final String BASE_URL = "https://s3.lynk2.co/api/s3/test";
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
    public List<Buket> getAllByAdmin(Long idAdmin) {
        return buketRepository.findByAdminId(idAdmin);
    }

    @Override
    public BuketDTO getBuketByIdDTO(Long id) throws NotFoundException {
        Buket buket = buketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buket tidak ditemukan dengan ID: " + id));

        BuketDTO buketDTO = new BuketDTO();
        buketDTO.setId(buket.getId());
        buketDTO.setIdAdmin(buket.getAdmin().getId());
        buketDTO.setNamaBuket(buket.getNamaBuket());
        buketDTO.setHargaBuket(buket.getHargaBuket());
        buketDTO.setFotoUrl(buket.getFotoUrl());

        return buketDTO;
    }


    @Override
    public BuketDTO tambahBuketDTO(Long idAdmin, BuketDTO buketDTO) {
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin tidak ditemukan"));

        Buket buket = new Buket();
        buket.setAdmin(admin);
        buket.setNamaBuket(buketDTO.getNamaBuket());
        buket.setHargaBuket(buketDTO.getHargaBuket());
        buket.setFotoUrl(buketDTO.getFotoUrl());

        return mapToDTO(buketRepository.save(buket));
    }

    @Override
    public BuketDTO editBuketDTO(Long id, Long idAdmin, BuketDTO buketDTO) throws IOException {
        Buket existingBuket = buketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buket tidak ditemukan"));

        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin tidak ditemukan"));

        existingBuket.setNamaBuket(buketDTO.getNamaBuket());
        existingBuket.setHargaBuket(buketDTO.getHargaBuket());

        if (buketDTO.getFotoUrl() != null) {
            existingBuket.setFotoUrl(buketDTO.getFotoUrl());
        }

        existingBuket.setAdmin(admin);

        return mapToDTO(buketRepository.save(existingBuket));
    }

    @Override
    public void deleteBuket(Long id) throws IOException {
        buketRepository.deleteById(id);
    }

    @Override
    public String uploadFoto(MultipartFile file) throws IOException {
        String uploadUrl = BASE_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return extractFileUrlFromResponse(response.getBody());
        } else {
            throw new IOException("Gagal mengunggah file: " + response.getStatusCode());
        }
    }

    private String extractFileUrlFromResponse(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(responseBody);
        JsonNode dataNode = jsonResponse.path("data");
        return dataNode.path("url_file").asText();
    }

    private BuketDTO mapToDTO(Buket buket) {
        BuketDTO dto = new BuketDTO();
        dto.setId(buket.getId());
        dto.setIdAdmin(buket.getAdmin().getId());
        dto.setNamaBuket(buket.getNamaBuket());
        dto.setHargaBuket(buket.getHargaBuket());
        dto.setFotoUrl(buket.getFotoUrl());
        return dto;
    }
}
