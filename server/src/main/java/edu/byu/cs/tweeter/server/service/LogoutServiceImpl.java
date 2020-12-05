package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;

public class LogoutServiceImpl extends RequestChecker implements LogoutService {

    @Override
    public LogoutResponse logout(LogoutRequest request) {
        try{
            getAuthDAO().delete(request.getAuthToken().getToken());
            return new LogoutResponse();
        } catch (Exception ex){
            throw new RuntimeException("Server Error : ", ex);
        }
    }

    AuthTokenDAO getAuthDAO() { return new AuthTokenDAO(); }
}
