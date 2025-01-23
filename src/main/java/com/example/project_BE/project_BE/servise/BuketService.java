package com.example.project_BE.project_BE.servise;

import com.example.project_BE.project_BE.DTO.BuketDTO;
import com.example.project_BE.project_BE.model.Buket;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BuketService {
    List<Buket> getAllBuket();
    List<Buket> getAllByAdmin(Long idAdmin);
    BuketDTO getBuketByIdDTO(Long id) throws IOException;
    BuketDTO tambahBuketDTO(Long idAdmin, BuketDTO buketDTO);
    BuketDTO editBuketDTO(Long id, Long idAdmin, BuketDTO buketDTO) throws IOException;
    void deleteBuket(Long id) throws IOException;

    // Upload foto baru
    String uploadFoto(MultipartFile file) throws IOException;
}
