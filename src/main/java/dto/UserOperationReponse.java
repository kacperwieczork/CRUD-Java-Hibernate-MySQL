package dto;

import java.io.Serializable;

public class UserOperationReponse implements Serializable {
    private String response;

    public UserOperationReponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
