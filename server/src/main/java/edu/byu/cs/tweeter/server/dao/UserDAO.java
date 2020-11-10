package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class UserDAO {

    public LoginResponse login(LoginRequest request) {
        User user = new User("Ben", "Dover",
                "https://i.pinimg.com/originals/50/cb/08/50cb085f28faa563a5e286ecadd3d1bf.jpg");
        AuthToken authToken = new AuthToken();
        return new LoginResponse(user, authToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        User user = new User(request.getFirstname(), request.getLastname(),
                "https://i.pinimg.com/originals/50/cb/08/50cb085f28faa563a5e286ecadd3d1bf.jpg");
        AuthToken authToken = new AuthToken();
        return new RegisterResponse(user, authToken);
    }

    public boolean isValidUser(User user) {
        return true;
//        try{
//            return true;
//        } catch (Exception ex){
//            throw new RuntimeException("Server Error", ex);
//        }
    }

}
