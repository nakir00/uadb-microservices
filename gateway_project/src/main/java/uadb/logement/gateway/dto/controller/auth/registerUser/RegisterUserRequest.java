package uadb.logement.gateway.dto.auth.registerUser;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import uadb.logement.gateway.model.Utilisateur;

import java.time.LocalDateTime;

@Data
public class RegisterUserRequest {


    private String nomUtilisateur;
    private String email;
    private String password;
    private String telephone;
    private String CNI;
    private String role;

    public RegisterUserRequest(String nomUtilisateur, String email, String password, String telephone, String CNI, String role) {
        this.nomUtilisateur = nomUtilisateur;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.CNI = CNI;
        this.role = role;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCNI() {
        return CNI;
    }

    public void setCNI(String CNI) {
        this.CNI = CNI;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
