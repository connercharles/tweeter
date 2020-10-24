package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;

public class UserDAO {

    public User login(LoginRequest request) {
        return new User(request.getUsername(), request.getPassword(),
                "https://i.pinimg.com/originals/50/cb/08/50cb085f28faa563a5e286ecadd3d1bf.jpg");
    }

    public User register(RegisterRequest request) {
        return new User(request.getFirstname(), request.getLastname(),
                "https://i.pinimg.com/originals/50/cb/08/50cb085f28faa563a5e286ecadd3d1bf.jpg");
    }

}
