package com.example.easyquiz.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    // Nên sử dụng thuật toán băm mạnh như SHA-256
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16; // Độ dài của salt (bytes)

    /**
     * Băm mật khẩu với salt ngẫu nhiên.
     *
     * @param password Mật khẩu dạng văn bản thuần.
     * @return Một chuỗi chứa cả salt và hash, được phân tách bởi dấu hai chấm.
     */
    public static String hashPassword(String password) {
        try {
            // Tạo salt ngẫu nhiên
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Tạo đối tượng MessageDigest
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

            // Thêm salt vào digest
            md.update(salt);

            // Băm mật khẩu
            byte[] hashedPassword = md.digest(password.getBytes());

            // Kết hợp salt và hash để lưu trữ
            // Sử dụng Base64 để đảm bảo an toàn khi lưu vào cơ sở dữ liệu dạng text
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String encodedHash = Base64.getEncoder().encodeToString(hashedPassword);

            return encodedSalt + ":" + encodedHash;
        } catch (NoSuchAlgorithmException e) {
            // Xử lý ngoại lệ nếu thuật toán không tồn tại (rất hiếm khi xảy ra)
            throw new RuntimeException("Không tìm thấy thuật toán băm mật khẩu", e);
        }
    }

    /**
     * Xác thực mật khẩu văn bản thuần với một chuỗi đã băm (bao gồm cả salt).
     *
     * @param plainPassword  Mật khẩu người dùng nhập vào.
     * @param storedPassword Chuỗi mật khẩu đã băm lưu trong DB (định dạng "salt:hash").
     * @return true nếu mật khẩu khớp, ngược lại trả về false.
     */
    public static boolean verifyPassword(String plainPassword, String storedPassword) {
        try {
            // Tách salt và hash từ chuỗi lưu trữ
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                // Định dạng lưu trữ không hợp lệ
                return false;
            }
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            // Tạo đối tượng MessageDigest
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

            // Thêm salt đã lưu vào digest
            md.update(salt);

            // Băm mật khẩu người dùng nhập vào với cùng salt
            byte[] actualHash = md.digest(plainPassword.getBytes());

            // So sánh hai mảng byte
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            // Ghi log lỗi hoặc xử lý phù hợp
            e.printStackTrace();
            return false;
        }
    }
}
