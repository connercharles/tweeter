package edu.byu.cs.tweeter.server.service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.S3DAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImpl implements RegisterService {

    @Override
    public RegisterResponse register(RegisterRequest request) {
        byte[] imageDecoded = Base64.getDecoder().decode(request.getImageEncoded());
        String imageUrl = "";

        String hashPassword = getHasher().hash(request.getPassword());

        try{
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageDecoded);
            imageUrl = gets3DAO().put(request.getUsername() + ".png", imageStream);

            getUserDAO().put(request.getFirstname(), request.getLastname(),
                    request.getUsername(), imageUrl, hashPassword);
            User user = getUserDAO().get(request.getUsername());

            String token = getAuthDAO().put();
            AuthToken auth = getAuthDAO().get(token);
            return new RegisterResponse(user, auth);
        } catch(Exception e){
            throw new RuntimeException("Server Error", e);
        }
    }

    UserDAO getUserDAO() { return new UserDAO(); }
    AuthTokenDAO getAuthDAO() { return new AuthTokenDAO(); }
    S3DAO gets3DAO() { return new S3DAO(); }
    Hasher getHasher() { return new Hasher(); }

}
