package uadb.logement.gateway.dto.auth.registerUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
public class RegisterUserResponse {
    private Long id;
    private String nomUtilisateur;
    private String email;
    private String telephone;
    private String CNI;
    private String role;

    public RegisterUserResponse(Long id, String nomUtilisateur, String email, String telephone, String CNI, String role) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.email = email;
        this.telephone = telephone;
        this.CNI = CNI;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
