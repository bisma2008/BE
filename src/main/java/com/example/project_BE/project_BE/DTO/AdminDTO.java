package com.example.project_BE.project_BE.DTO;

public class AdminDTO {
    private String username;
    private String password;
    private String role = "admin";  // Menambahkan role dengan nilai default "admin"

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
