package com.example.project_BE.project_BE.Controller;

import com.example.project_BE.project_BE.DTO.BuketDTO;
import com.example.project_BE.project_BE.exception.NotFoundException;
import com.example.project_BE.project_BE.model.Buket;
import com.example.project_BE.project_BE.servise.BuketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<BuketDTO> getBuketById(@PathVariable Long id) {
        try {
            BuketDTO buketDTO = buketService.getBuketByIdDTO(id);
            return ResponseEntity.ok(buketDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // Handle adding Buket without uploadFoto
    @PostMapping("/buket/tambah/{idAdmin}")
    public ResponseEntity<BuketDTO> tambahBuket(
            @PathVariable Long idAdmin,
            @RequestBody BuketDTO buketDTO) {

        System.out.println("Data diterima di backend: " + buketDTO);

        BuketDTO savedBuket = buketService.tambahBuketDTO(idAdmin, buketDTO);
        return ResponseEntity.ok(savedBuket);
    }

    // Handle editing Buket and uploading a new photo if provided
    @PutMapping(value = "/buket/editById/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BuketDTO> editBuket(
            @PathVariable Long id,
            @RequestParam Long idAdmin,
            @RequestPart(required = false) MultipartFile file, // Untuk file opsional
            @RequestPart("data") String data // Untuk JSON dalam bentuk String
    ) throws IOException {
        // Parse JSON ke BuketDTO
        ObjectMapper objectMapper = new ObjectMapper();
        BuketDTO buketDTO = objectMapper.readValue(data, BuketDTO.class);

        // Jika ada file, upload dan set URL ke DTO
        if (file != null && !file.isEmpty()) {
            String fotoUrl = buketService.uploadFoto(file);
            buketDTO.setFotoUrl(fotoUrl);
        }

        // Proses edit Buket
        BuketDTO updatedBuket = buketService.editBuketDTO(id, idAdmin, buketDTO);
        return ResponseEntity.ok(updatedBuket);
    }


    @DeleteMapping("/buket/delete/{id}")
    public ResponseEntity<Void> deleteBuket(@PathVariable Long id) throws IOException {
        buketService.deleteBuket(id);
        return ResponseEntity.noContent().build();
    }
}
