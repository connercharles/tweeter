package edu.byu.cs.tweeter.model.service.request;

public class RegisterRequest {

    private final String firstname;
    private final String lastname;
    private final String username;
    private final String password;
    private final byte [] imageBytes;

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
}
