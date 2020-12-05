package edu.byu.cs.tweeter.model.service.request;

public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String imageEncoded;

    private RegisterRequest() {}

    public RegisterRequest(String firstname, String lastname, String username, String password, String imageEncoded) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.imageEncoded = imageEncoded;
    }

    public String getImageEncoded() {
        return imageEncoded;
    }

    public void setImageEncoded(String imageEncoded) {
        this.imageEncoded = imageEncoded;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
