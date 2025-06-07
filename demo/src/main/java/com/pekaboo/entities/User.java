package com.pekaboo.entities;
import java.time.LocalDate; //represents a date, YY-MM-DD

public class User {
    private int idUser;
    private String username;
    private String password;
    private String email;
    private String noTelepon;
    private LocalDate tanggalLahir;
    private String jenisKelamin;
    private String alamat;
    private String userStatus;

    public User() {}

    public User(int idUser, String username, String password, String email, String noTelepon,
                LocalDate tanggalLahir, String jenisKelamin, String alamat, String userStatus) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.email = email;
        this.noTelepon = noTelepon;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.userStatus = userStatus;
    }

    // Getters and setters
    public int getIdUser() { 
        return idUser; 
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser; 
    }

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

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getNoTelepon() { 
        return noTelepon; 
    }
    public void setNoTelepon(String noTelepon) { 
        this.noTelepon = noTelepon; 
    }

    public LocalDate getTanggalLahir() { 
        return tanggalLahir; 
    }
    public void setTanggalLahir(LocalDate tanggalLahir) { 
        this.tanggalLahir = tanggalLahir; 
    }

    public String getJenisKelamin() { 
        return jenisKelamin; 
    }
    public void setJenisKelamin(String jenisKelamin) { 
        this.jenisKelamin = jenisKelamin; 
    }

    public String getAlamat() { 
        return alamat; 
    }
    public void setAlamat(String alamat) { 
        this.alamat = alamat; 
    }

    public String getUserStatus() { 
        return userStatus; 
    }
    public void setUserStatus(String userStatus) { 
        this.userStatus = userStatus; 
    }
}