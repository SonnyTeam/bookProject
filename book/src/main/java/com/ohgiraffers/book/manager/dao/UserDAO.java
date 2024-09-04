package com.ohgiraffers.book.manager.dao;

import com.ohgiraffers.book.manager.dto.UserDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.close;

public class UserDAO {

    private Properties prop = new Properties();

    public UserDAO(String url) {
        try {
            prop.loadFromXML(new FileInputStream(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDTO selectUser(Connection con, String name){

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        UserDTO user = null;
        List<String> borrowedList;

        try {
            pstmt = con.prepareStatement(prop.getProperty("selectUser"));
            pstmt.setString(1, name);
            rset = pstmt.executeQuery();

            if (!rset.isBeforeFirst()) {
                throw new Exception("회원이 존재하지않습니다.");
            }

            String userName = "";
            String userPhone = "";
            borrowedList = new ArrayList<>();
            int userCode = 0;

            while(rset.next()){

                userName = rset.getString(1);
                userPhone = rset.getString(2);
                userCode = rset.getInt(3);

            }


            pstmt = con.prepareStatement(prop.getProperty("selectBorrowedUser"));
            pstmt.setInt(1, userCode);
            rset = pstmt.executeQuery();

            String subject = "";

            while(rset.next()){
                subject = rset.getString(1);
                borrowedList.add(subject);
            }

            user = new UserDTO(userName, userPhone, borrowedList);


        } catch (Exception e) {
            System.out.println("오류!! "+ e.getMessage()+"이전으로 돌아갑니다");
            // throw new RuntimeException(e);
        } finally {
            close(con);
            close(pstmt);
            close(rset);
        }


        return user;
    }


    public int updateUser(Connection con, String name, String phone) {
        PreparedStatement pstmt = null;
        int result = 0;

        // 연락처 바꾸기
        try {
            // 진짜 수정 시 주석처리
            // con.setAutoCommit(false);
            pstmt = con.prepareStatement(prop.getProperty("updatePhone"));
            pstmt.setString(1, phone);
            pstmt.setString(2, name);
            result = pstmt.executeUpdate();

            // 진짜 수정 시 주석처리
            // con.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con);
            close(pstmt);
        }


        return result;
    }

    public int deleteUser(Connection con, String name) {

        PreparedStatement pstmt = null;
        int result = 0;

        try {
            // 진짜 삭제 시 주석처리
            //con.setAutoCommit(false);

            pstmt = con.prepareStatement(prop.getProperty("deleteUser"));
            pstmt.setString(1, name);
            result = pstmt.executeUpdate();

            // 진짜 삭제 시 주석처리
            //con.rollback();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("도서를 대여/예약 중인 회원입니다.");

        } finally {
            close(con);
            close(pstmt);
        }


        return result;
    }

    public List<UserDTO> selectAllUser(Connection con) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<UserDTO> userList = new ArrayList<>();
        List<List<String>> borrowedList = new ArrayList<>();

        List<String> userName = new ArrayList<>();
        List<String> userPhone = new ArrayList<>();
        List<Integer> userCode = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(prop.getProperty("selectAllUser"));
            rset = pstmt.executeQuery();


            while(rset.next()){

                userName.add(rset.getString(1));
                userPhone.add(rset.getString(2));
                userCode.add(rset.getInt(3));
            }

            pstmt = con.prepareStatement(prop.getProperty("selectBorrowedUser"));

            for (int i = 0; i < userCode.size(); i++) {
                pstmt.setInt(1, userCode.get(i));
                rset = pstmt.executeQuery();

                List<String> subject = new ArrayList<>();
                while (rset.next()) {
                    String bookTitle = rset.getString(1);
                    subject.add(bookTitle != null ? bookTitle : "");  // Null일 경우 빈 문자열 추가
                }
                borrowedList.add(subject);

                UserDTO user = new UserDTO(userName.get(i), userPhone.get(i), borrowedList.get(i));
                userList.add(user);
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con);
            close(pstmt);
            close(rset);
        }

        return userList;
    }

    public int findUserCode(Connection con, String name) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        int userCode = 0;

        try {
            pstmt = con.prepareStatement(prop.getProperty("findUserCode"));
            pstmt.setString(1, name);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                userCode = rset.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }
        return userCode;
    }

    public int deleteUserStatus(Connection con, int userCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        int result = 0;

        try {

            pstmt = con.prepareStatement(prop.getProperty("deleteUserStatus"));
            pstmt.setInt(1, userCode);
            result = pstmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int deleteUserReserveStatus(Connection con, int userCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        int result = 0;

        try {

            pstmt = con.prepareStatement(prop.getProperty("deleteUserReserveStatus"));
            pstmt.setInt(1, userCode);
            result = pstmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int deleteUserOverdue(Connection con, int userCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        int result = 0;

        try {

            pstmt = con.prepareStatement(prop.getProperty("deleteUserOverdue"));
            pstmt.setInt(1, userCode);
            result = pstmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
