package com.ohgiraffers.book.manager.dao;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.close;
import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.getConnection;

public class OverdueDAO {
    static Scanner sc = new Scanner(System.in);
    static PreparedStatement pstmt = null;
    static ResultSet rset = null;
    static Properties prop = new Properties();
    static LocalDate startTime;
    Map<Integer, String> isbnDate = new HashMap<>();

    public OverdueDAO(String url){
        try {
            prop.load(new FileReader(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void overlist(Connection con){

        try {
            prop.loadFromXML(new FileInputStream("src/main/resources/mapper/book-query.xml"));
            pstmt = con.prepareStatement(prop.getProperty("overduelist"));
            rset = pstmt.executeQuery();

            Boolean hasNext = rset.next();
            if(hasNext) {
                do{
                    System.out.print(rset.getString(1) + "(을/를) ");
                    System.out.print(rset.getString(2) + "님이 반납일로부터 ");
                    System.out.print(rset.getInt(3) + "일 지났습니다. 연체료는 ");
                    System.out.println(rset.getInt(4) + "원 입니다.");
                } while (rset.next());
            }else{
                System.out.println("연체중인 회원이 없습니다");
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);

        }
    }
    public void mapinsert(Connection con){


        try {
            prop.loadFromXML(new FileInputStream("src/main/resources/mapper/book-query.xml"));
            pstmt = con.prepareStatement(prop.getProperty("keyAndval"));
            rset = pstmt.executeQuery();
            while(rset.next()) {
                if (rset.getString("STATUS_RENT").equals("대여 중")){
                    isbnDate.put(rset.getInt("ISBN"), rset.getString("DATE_END"));
                 }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }
    }
    public Map<Integer, String> getIsbnDate() {
        return isbnDate;
    }

    public void insertauto(Map<Integer,String> isbnDate, Connection con){
        try {
            pstmt = con.prepareStatement("TRUNCATE TABLE tbl_overdue");
            pstmt.executeUpdate();

            prop.loadFromXML(new FileInputStream("src/main/resources/mapper/book-query.xml"));

            for(Map.Entry<Integer,String> entry : isbnDate.entrySet()) {
                int isbn = entry.getKey();
                String endday = entry.getValue();
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //이 형식으로 초기화하고
                LocalDate dueday = LocalDate.parse(endday); // ,formatter
                LocalDate currentDate = LocalDate.now();

                long dateover = ChronoUnit.DAYS.between(dueday,currentDate); //일수빼기
                if (dateover > 0) {
                    int fee = calculateFee(dateover);
                    int userCode = getUserCode(isbn, getConnection());
                    int mul = fee / 100;

                    pstmt = con.prepareStatement(prop.getProperty("overduestatus"));
                    pstmt.setInt(1, isbn);
                    pstmt.executeUpdate();


                    pstmt = con.prepareStatement(prop.getProperty("autoinsert"));
                    pstmt.setInt(1, isbn);
                    pstmt.setInt(2, userCode);
                    pstmt.setInt(3, fee);
                    pstmt.setInt(4, mul);
                    pstmt.executeUpdate();

                }
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int calculateFee(long dateover){
        //연체일을 통해 계산하는 과정
        int a = (int)dateover;
        return a*100;
    }


    public int getUserCode(int isbn, Connection con) {
        int userCode=0;
        PreparedStatement pstmt = null;
        try {
            prop.loadFromXML(new FileInputStream("src/main/resources/mapper/book-query.xml"));
            pstmt = con.prepareStatement(prop.getProperty("finduserbyisbn"));
            pstmt.setInt(1, isbn);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                userCode = rset.getInt("user_code");
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }
        return userCode;
    }
}

