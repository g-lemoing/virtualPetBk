package cat.itacademy.S05T02.virtualPetBk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseDto {
    private String token;
    private long expiresIn;

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
}
