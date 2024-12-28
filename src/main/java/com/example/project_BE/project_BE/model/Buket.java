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

    @ManyToOne
    @JoinColumn(name = "id_admin", nullable = false)
    private Admin admin;

    public Buket(Long id, Admin admin, String namaBuket, Double hargaBuket) {
        this.id = id;
        this.admin = admin;
        this.namaBuket = namaBuket;
        this.hargaBuket = hargaBuket;
    }

    public Buket() {
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
