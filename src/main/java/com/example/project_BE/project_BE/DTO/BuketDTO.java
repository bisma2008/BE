package com.example.project_BE.project_BE.DTO;

public class BuketDTO {
    private Long id;
    private Long idAdmin;
    private String namaBuket;
    private Double hargaBuket;
    private String fotoUrl; // Menambahkan atribut fotoUrl

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNamaBuket() {
        return namaBuket;
    }

    public void setNamaBuket(String namaBuket) {
        this.namaBuket = namaBuket;
    }

    public Double getHargaBuket() {
        return hargaBuket;
    }

    public void setHargaBuket(Double hargaBuket) {
        this.hargaBuket = hargaBuket;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
