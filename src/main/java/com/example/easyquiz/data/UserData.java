package com.example.easyquiz.data;

import com.example.easyquiz.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    private static final String USER_FILE = "users.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<User> loadUsers() {
        File file = new File(USER_FILE);

        if (!file.exists() || file.length() == 0) {
            // nếu file chưa tồn tại hoặc trống → trả về list rỗng
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveUsers(List<User> users) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(USER_FILE), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
