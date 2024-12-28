package com.example.project_BE.project_BE.servise;

import com.example.project_BE.project_BE.DTO.BuketDTO;
import com.example.project_BE.project_BE.model.Buket;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BuketService {
    List<Buket> getAllBuket();

    default List<Buket> getAllByAdmin() {
        return getAllByAdmin(null);
    }

    List<Buket> getAllByAdmin(Long idAdmin);

    Optional<Buket> getBuketById(Long id);

    BuketDTO tambahBuketDTO(Long idAdmin, BuketDTO buketDTO);

    BuketDTO editBuketDTO(Long id, Long idAdmin, BuketDTO buketDTO) throws IOException;

    void deleteBuket(Long id) throws IOException;


}
