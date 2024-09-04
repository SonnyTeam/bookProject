package com.ohgiraffers.book.login.dao;

import com.ohgiraffers.book.login.dto.UserDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.*;


public class LoginDAO {

    private Properties prop = new Properties();

    public LoginDAO(String url) {
        try {
            prop.loadFromXML(new FileInputStream(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int login(Connection con, UserDTO userDTO) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        int result=0;

        try {
            pstmt = con.prepareStatement(prop.getProperty("loginCheck"));
            pstmt.setString(1, userDTO.getUser_id());
            pstmt.setString(2, userDTO.getUser_pwd());

            rset = pstmt.executeQuery();
            while (rset.next()) {
                result = rset.getInt("user_code");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }
        return result;
    }


}
