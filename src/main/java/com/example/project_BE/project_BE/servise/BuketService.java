package com.example.project_BE.project_BE.servise;

import com.example.project_BE.project_BE.DTO.BuketDTO;
import com.example.project_BE.project_BE.model.Buket;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BuketService {
    // Mendapatkan semua buket
    List<Buket> getAllBuket();

    // Mendapatkan semua buket berdasarkan admin
    default List<Buket> getAllByAdmin() {
        return getAllByAdmin(null);
    }

    List<Buket> getAllByAdmin(Long idAdmin);

    // Mendapatkan buket berdasarkan ID
    Optional<Buket> getBuketById(Long id);
    // Menambah buket
    BuketDTO tambahBuketDTO(Long idAdmin, BuketDTO buketDTO);
    BuketDTO editBuketDTO(Long id, Long idAdmin, BuketDTO buketDTO) throws IOException;
    // Menghapus buket berdasarkan ID
    void deleteBuket(Long id) throws IOException;

    // Upload foto baru
    String uploadFoto(MultipartFile file) throws IOException;
}
