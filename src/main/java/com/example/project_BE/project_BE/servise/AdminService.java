package com.example.project_BE.project_BE.servise;

import com.example.project_BE.project_BE.DTO.PasswordDTO;
import com.example.project_BE.project_BE.model.Admin;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Admin registerAdmin(Admin admin);

    Admin getById(Long id);

    List<Admin> getAll();

    Admin edit(Long id, Admin admin);

    Admin putPasswordAdmin(PasswordDTO passwordDTO, Long id);

    Map<String, Boolean> delete(Long id);

    Object updatePassword(PasswordDTO password, Long id);
}
