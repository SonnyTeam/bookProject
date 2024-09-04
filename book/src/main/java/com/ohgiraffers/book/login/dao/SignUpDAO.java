package com.ohgiraffers.book.login.dao;

import com.ohgiraffers.book.login.dto.UserDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.close;

public class SignUpDAO {

    private Properties prop = new Properties();

    public SignUpDAO(String url) {
        try {
            prop.loadFromXML(new FileInputStream(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int signUp(Connection con, UserDTO userDTO) {
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            pstmt = con.prepareStatement(prop.getProperty("signUp"));
            pstmt.setString(1, userDTO.getName());
            pstmt.setString(2, userDTO.getPhone());
            pstmt.setString(3, userDTO.getUser_id());
            pstmt.setString(4, userDTO.getUser_pwd());
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
        }
        return result;
    }
}
