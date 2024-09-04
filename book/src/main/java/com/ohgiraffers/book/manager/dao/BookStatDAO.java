package com.ohgiraffers.book.manager.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.close;

public class BookStatDAO {

    Properties prop = new Properties();

    public BookStatDAO(String url) {
        try {
            prop.loadFromXML(new FileInputStream(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void countAllBook(Connection con) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            pstmt = con.prepareStatement(prop.getProperty("countAllBook"));
            rset = pstmt.executeQuery();

            while (rset.next()) {
                String num = rset.getString("COUNT(*)");
                System.out.println("도서의 총 개수는 " + num + " 권 입니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }
    }

    public void countByGenre(Connection con, String genre) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            pstmt = con.prepareStatement(prop.getProperty("countByGenre"));
            pstmt.setString(1, genre);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                int result = rset.getInt(1);
                System.out.println(genre + " 장르의 도서의 총 개수는 " + result + " 권 입니다.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }

    }

    public void countRented(Connection con) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            pstmt = con.prepareStatement(prop.getProperty("countRented"));
            rset = pstmt.executeQuery();
            while (rset.next()) {
                int result = rset.getInt(1);
                System.out.println("대여중인 도서의 총 개수는 " + result + " 권 입니다.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }

    }

    public void showAllGenre(Connection con) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        try {
            pstmt = con.prepareStatement(prop.getProperty("showAllGenre"));
            rset = pstmt.executeQuery();
            System.out.println("장르 목록 : ");
            while (rset.next()) {
                System.out.print(rset.getString("GENRE") + " | ");
            }
            System.out.println();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }
    }
}
