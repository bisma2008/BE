package com.example.project_BE.project_BE.model;

import javax.persistence.*;

@Entity
@Table(name = "buket")
public class Buket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_buket")
    private String namaBuket;

    @Column(name = "harga_buket")
    private Double hargaBuket;

    @Column(name = "foto_url") // Properti fotoUrl
    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "id_admin", nullable = false)
    private Admin admin;

    public Buket() {
    }

    public Buket(Long id, Admin admin, String namaBuket, Double hargaBuket, String fotoUrl) {
        this.id = id;
        this.admin = admin;
        this.namaBuket = namaBuket;
        this.hargaBuket = hargaBuket;
        this.fotoUrl = fotoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
