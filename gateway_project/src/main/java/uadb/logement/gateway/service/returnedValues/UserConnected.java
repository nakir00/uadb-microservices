package uadb.logement.gateway.service.returnedValues;

import org.springframework.http.ResponseCookie;
import uadb.logement.gateway.model.Utilisateur;

public class UserConnected {
    private ResponseCookie token;
    private ResponseCookie refresh;
    private Utilisateur user;


    public UserConnected(ResponseCookie token, ResponseCookie refresh, Utilisateur user) {
        this.token = token;
        this.refresh = refresh;
        this.user = user;
    }

    public ResponseCookie getToken() {
        return token;
    }

    public void setToken(ResponseCookie token) {
        this.token = token;
    }

    public ResponseCookie getRefresh() {
        return refresh;
    }

    public void setRefresh(ResponseCookie refresh) {
        this.refresh = refresh;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }
}
