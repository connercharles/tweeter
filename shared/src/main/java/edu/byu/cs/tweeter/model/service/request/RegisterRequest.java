package edu.byu.cs.tweeter.model.service.request;

public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private byte [] imageBytes;

    private RegisterRequest() {}

    public RegisterRequest(String firstname, String lastname, String username, String password, byte [] imageBytes) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.imageBytes = imageBytes;
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

    public byte[] getImageBytes() {
        return imageBytes;
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

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
