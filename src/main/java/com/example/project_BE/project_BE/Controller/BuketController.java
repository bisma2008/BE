package com.example.project_BE.project_BE.Controller;

import com.example.project_BE.project_BE.DTO.BuketDTO;
import com.example.project_BE.project_BE.model.Buket;
import com.example.project_BE.project_BE.servise.BuketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("Request received for bucket ID: " + id);  // Log request ID
        Optional<Buket> buket = buketService.getBuketById(id);
        return buket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/buket/tambah/{idAdmin}")
    public ResponseEntity<BuketDTO> tambahBuket(
            @PathVariable Long idAdmin,
            @RequestBody BuketDTO buketDTO) {
        BuketDTO savedBuket = buketService.tambahBuketDTO(idAdmin, buketDTO);
        return ResponseEntity.ok(savedBuket);
    }

    @PutMapping(value = "/buket/editById/{id}")
    public ResponseEntity<BuketDTO> editBuket(
            @PathVariable Long id,
            @RequestParam Long idAdmin,
            @RequestBody BuketDTO buketDTO) throws IOException { // Menggunakan @RequestBody untuk buketDTO

        // Edit buket tanpa foto
        BuketDTO updatedBuket = buketService.editBuketDTO(id, idAdmin, buketDTO);
        return ResponseEntity.ok(updatedBuket);
    }

    @DeleteMapping("/buket/delete/{id}")
    public ResponseEntity<Void> deleteBuket(@PathVariable Long id) throws IOException {
        buketService.deleteBuket(id);
        return ResponseEntity.noContent().build();
    }
}
