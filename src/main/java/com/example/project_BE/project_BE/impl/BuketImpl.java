package com.example.project_BE.project_BE.impl;

import com.example.project_BE.project_BE.DTO.BuketDTO;
import com.example.project_BE.project_BE.exception.NotFoundException;
import com.example.project_BE.project_BE.model.Buket;
import com.example.project_BE.project_BE.model.Admin;
import com.example.project_BE.project_BE.repository.BuketRepository;
import com.example.project_BE.project_BE.repository.AdminRepository;
import com.example.project_BE.project_BE.servise.BuketService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BuketImpl implements BuketService {

    private final BuketRepository buketRepository;
    private final AdminRepository adminRepository;

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

        Buket savedBuket = buketRepository.save(buket);

        BuketDTO result = new BuketDTO();
        result.setId(savedBuket.getId());
        result.setIdAdmin(admin.getId());
        result.setNamaBuket(savedBuket.getNamaBuket());
        result.setHargaBuket(savedBuket.getHargaBuket());

        return result;
    }

    @Override
    public BuketDTO editBuketDTO(Long id, Long idAdmin, BuketDTO buketDTO) throws IOException {
        Buket existingBuket = buketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buket tidak ditemukan"));

        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan"));

        existingBuket.setAdmin(admin);
        existingBuket.setNamaBuket(buketDTO.getNamaBuket());
        existingBuket.setHargaBuket(buketDTO.getHargaBuket());

        Buket updatedBuket = buketRepository.save(existingBuket);

        BuketDTO result = new BuketDTO();
        result.setId(updatedBuket.getId());
        result.setIdAdmin(admin.getId());
        result.setNamaBuket(updatedBuket.getNamaBuket());
        result.setHargaBuket(updatedBuket.getHargaBuket());

        return result;
    }

    @Override
    public void deleteBuket(Long id) throws IOException {
        buketRepository.deleteById(id);
    }


}
