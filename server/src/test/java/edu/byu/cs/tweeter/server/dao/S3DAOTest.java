package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class S3DAOTest {

    @Test
    public void put() throws IOException {
        S3DAO dao = new S3DAO();
        File file = new File("../alias.png");
        Path p = file.toPath();
        byte[] pic = Files.readAllBytes(file.toPath());
        ByteArrayInputStream stream = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
        String url = dao.put("alias.png", stream);

        Assertions.assertNotNull(url);
    }
}
