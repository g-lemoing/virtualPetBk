package cat.itacademy.S05T02.virtualPetBk.dto;

import cat.itacademy.S05T02.virtualPetBk.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseDto {
    private String token;
    private long expiresIn;
    private String userName;
    private String userRole;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String token, long expiresIn, String userName, String userRole) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userName = userName;
        this.userRole = userRole;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("expiresIn")
    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }
}
