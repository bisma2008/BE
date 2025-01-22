package com.example.project_BE.project_BE.Controller;

import com.example.project_BE.project_BE.DTO.BuketDTO;
import com.example.project_BE.project_BE.model.Buket;
import com.example.project_BE.project_BE.servise.BuketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class BuketController {

    private final BuketService buketService;

    public BuketController(BuketService buketService) {
        this.buketService = buketService;
    }

    @GetMapping("/buket/all")
    public ResponseEntity<List<Buket>> getAllBuket() {
        List<Buket> buketList = buketService.getAllBuket();
        return ResponseEntity.ok(buketList);
    }

    @GetMapping("/buket/getAllByAdmin/{idAdmin}")
    public ResponseEntity<List<Buket>> getAllByAdmin(@PathVariable Long idAdmin) {
        List<Buket> buketList = buketService.getAllByAdmin(idAdmin);
        return ResponseEntity.ok(buketList);
    }

    @GetMapping("/buket/getById/{id}")
    public ResponseEntity<Buket> getBuketById(@PathVariable Long id) {
        Optional<Buket> buket = buketService.getBuketById(id);
        return buket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/buket/tambah/{idAdmin}")
    public ResponseEntity<BuketDTO> tambahBuket(
            @PathVariable Long idAdmin,
            @RequestParam("buket") String buketJson,
            @RequestParam("file") MultipartFile file) throws IOException {

        // Convert JSON string to DTO
        ObjectMapper objectMapper = new ObjectMapper();
        BuketDTO buketDTO = objectMapper.readValue(buketJson, BuketDTO.class);

        // Upload the photo and set the URL in DTO
        String fotoUrl = buketService.uploadFoto(file);
        buketDTO.setFotoUrl(fotoUrl);

        // Save the Buket
        BuketDTO savedBuket = buketService.tambahBuketDTO(idAdmin, buketDTO);
        return ResponseEntity.ok(savedBuket);
    }

    @PutMapping("/buket/edit/{id}")
    public ResponseEntity<BuketDTO> editBuketById(
            @PathVariable Long id,
            @RequestParam("buket") String buketJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        // Convert JSON string to DTO
        ObjectMapper objectMapper = new ObjectMapper();
        BuketDTO buketDTO = objectMapper.readValue(buketJson, BuketDTO.class);

        // Pass the MultipartFile to the service method
        BuketDTO updatedBuket = buketService.editBuketDTO(id, buketDTO, file);

        return ResponseEntity.ok(updatedBuket);
    }


    @DeleteMapping("/buket/delete/{id}")
    public ResponseEntity<Void> deleteBuket(@PathVariable Long id) throws IOException {
        buketService.deleteBuket(id);
        return ResponseEntity.noContent().build();
    }
}
