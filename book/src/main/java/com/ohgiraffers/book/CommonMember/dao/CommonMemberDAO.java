package com.ohgiraffers.book.CommonMember.dao;

import com.ohgiraffers.book.CommonMember.CommonMemberUI;
import com.ohgiraffers.book.manager.dao.BookStatusDAO;
import com.ohgiraffers.book.manager.dto.StatusDTO;
import com.ohgiraffers.book.login.dto.UserDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.close;
import static com.ohgiraffers.book.manager.dao.BookStatusDAO.statusList;

public class CommonMemberDAO {

    static Scanner sc = new Scanner(System.in);
    static PreparedStatement pstmt = null;
    static ResultSet rset = null;
    static Properties prop = new Properties();
    static CommonMemberUI ui = new CommonMemberUI();
    static LocalDate startTime;
    static String url = "src/main/resources/mapper/book-query.xml";

    private BookStatusDAO bookStatusDAO = new BookStatusDAO("src/main/resources/mapper/manager-query.xml");

    public CommonMemberDAO(String url) {
        try {
            prop.loadFromXML(new FileInputStream(url));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String rental(int a, Connection con){ //책 대여

        int userCode = a;
        int result =0;
        System.out.println("대여하실 책 제목 : ");
        String subject = sc.nextLine();


        // 임시 리스트 생성
        List<StatusDTO> tempStatusList = new ArrayList<>();

        try {
            // 첫번째 상태 저장
            StatusDTO currentStatusDTO = bookStatusDAO.getStatusDTO(con, subject);
            //System.out.println(currentStatusDTO);

            if(currentStatusDTO != null){
                tempStatusList.add(currentStatusDTO);
            }

            startTime = LocalDate.now();
            prop.loadFromXML(new FileInputStream(url));

            pstmt = con.prepareStatement(prop.getProperty("findISBN"));
            //System.out.println(pstmt);
            pstmt.setString(1, subject);
            rset = pstmt.executeQuery();
            int num = 0;
            while(rset.next()){
                num = rset.getInt("ISBN");
            }

            // System.out.println(prop);
            pstmt = con.prepareStatement(prop.getProperty("checkbookstatus"));
            pstmt.setInt(1, num);
            rset = pstmt.executeQuery();


            if (!rset.isBeforeFirst()) {
                throw new Exception("해당 제목의 책이 존재하지 않습니다.");
            }
            // System.out.println(rset);
            while(rset.next()){
                String currentStatus = rset.getString("status_rent");
                if("대여 중".equals(currentStatus)){
                    System.out.println("이미 대여중인 책입니다. 대여할 수 없습니다");
                    System.out.println("이전 메뉴로 돌아갑니다");
                    return ui.userUI(userCode);
                }else {

                    pstmt = con.prepareStatement(prop.getProperty("rentalable"));
                    pstmt.setInt(6, num);
                    pstmt.setString(1, "대여 중");
                    //pstmt.setString(2, "예약가능");
                    pstmt.setString(2, startTime.toString());
                    pstmt.setString(3, null);
                    pstmt.setString(4, startTime.plusDays(30).toString());
                    pstmt.setInt(5, userCode);
                    result = pstmt.executeUpdate();


                    pstmt = con.prepareStatement(prop.getProperty("rentalable_reserve"));
                    pstmt.setString(1, "예약가능");
                    pstmt.setInt(2, num);


                    // 변경된 이력 저장
                    StatusDTO updateStatus = new StatusDTO(subject, "대여 중", "예약가능", startTime.toString(), null, num, userCode, startTime.plusDays(30).toString());
                    tempStatusList.add(updateStatus);

                    statusList.addAll(tempStatusList);
                    // System.out.println(updateStatus.getStatus_reserve());

                    bookStatusDAO.storeHistory(con);

                    result = pstmt.executeUpdate();

                    if (result == 1) {
                        System.out.println("대여 완료했습니다");
                        System.out.println("대여 기간은 " + startTime + " ~ " + startTime.plusDays(30) + " 입니다");
                    }
                }
            }
        }
         catch (Exception e) {
           System.out.println("오류!! "+ e.getMessage()+"이전으로 돌아갑니다");
        } finally {
            close(con);
            close(pstmt);
            close(rset);
        }
        return null;
    }


    public String returnBook(int a, Connection con){
        int userCode = a;
        int result = 0;

        System.out.println("반납하실 책 제목 : ");
        String subject = sc.nextLine();
        startTime = LocalDate.now();

        // 임시 리스트 생성
        List<StatusDTO> tempStatusList = new ArrayList<>();

        try {
            // 첫번째 상태 저장
            StatusDTO currentStatusDTO = bookStatusDAO.getStatusDTO(con, subject);

            if(currentStatusDTO != null){
                tempStatusList.add(currentStatusDTO);
            }


            pstmt = con.prepareStatement(prop.getProperty("findISBN"));
            pstmt.setString(1, subject);
            rset = pstmt.executeQuery();
            int num = 0;
            while(rset.next()){
                num = rset.getInt("ISBN");
            }

            pstmt = con.prepareStatement(prop.getProperty("checkbookstatus"));
            pstmt.setInt(1, num);
            rset = pstmt.executeQuery();

            if (!rset.isBeforeFirst()) {
                throw new Exception("해당 제목의 책이 존재하지 않습니다.");
            }

            while(rset.next()){
                String currentStatus = rset.getString("STATUS_RENT");
                if("대여가능".equals(currentStatus)){
                    System.out.println("대여중인 책이 아닙니다. 이전으로 돌아갑니다");
                    return ui.userUI(userCode);
                } else {

                    //대여 테이블 저장
                    pstmt = con.prepareStatement(prop.getProperty("returnBook"));
                    pstmt.setInt(3, num);
                    pstmt.setString(1, "대여가능");
                    pstmt.setString(2, startTime.toString());

                    result = pstmt.executeUpdate();
                    // pstmt.close();

                    // 예약 테이블 저장
                    pstmt = con.prepareStatement(prop.getProperty("rentalable_reserve"));

                    pstmt.setString(1, "예약불가");
                    pstmt.setInt(2, num);


                    result = pstmt.executeUpdate();

                    // 변경된 이력 저장
                    StatusDTO updateStatus = new StatusDTO(subject, "대여가능", "예약불가", currentStatusDTO.getDate_rent(), startTime.toString(), num, userCode, currentStatusDTO.getDate_end());
                    tempStatusList.add(updateStatus);

                    statusList.addAll(tempStatusList);

                    bookStatusDAO.storeHistory(con);



                    result = pstmt.executeUpdate();


                    if (result == 1) {
                        System.out.println("정상적으로 반납 완료했습니다.");
                    } else {
                        System.out.println("반납 불가 이번 메뉴로 돌아갑니다");
                    }
                }
            }

        }  catch (Exception e) {
            System.out.println("오류!! "+ e.getMessage()+"이전으로 돌아갑니다");
        }finally {
            close(con);
            close(pstmt);
        }
        return null;
    }

    public void titleSearch(Connection con) {

        try {
            System.out.println("책 제목 : ");
            String title = sc.nextLine();


            pstmt = con.prepareStatement(prop.getProperty("titleSearch"));
            pstmt.setString(1, title);
            rset = pstmt.executeQuery();
            if (!rset.isBeforeFirst()) {
                throw new Exception("해당 제목의 책이 존재하지 않습니다.");
            }
            while (rset.next()) {
                System.out.println(
                        "책 번호 : " + rset.getInt("ISBN") +
                        " | 책제목 : " + rset.getString("SUBJECT") +
                        " | 저자 : " + rset.getString("AUTHOR") +
                        " | 출판사 : " + rset.getString("PUBLISHER") +
                        " | 출판연도 : " + rset.getInt("PUBLIC_YEAR") +
                        " | 장르 : " + rset.getString("GENRE") +
                        " | 대여 상태 : " + rset.getString("STATUS_RENT")
                );
            }

        } catch (Exception e) {
            System.out.println("오류!! "+ e.getMessage()+"이전으로 돌아갑니다");
        } finally {
            close(con);
            close(pstmt);
            close(rset);
        }
    }

    public void authorSearch(Connection con) {

        try {
            System.out.println("저자 이름 : ");
            String author = sc.nextLine();

            pstmt = con.prepareStatement(prop.getProperty("authorsearch"));
            pstmt.setString(1, author);
            rset = pstmt.executeQuery();
           if (author.matches("\\d+")) {
                throw new IllegalArgumentException("이름에 숫자입력은 안됩니다.");
           }
            if (!rset.isBeforeFirst()) {
              throw new Exception("해당 저자의 책이 존재하지 않습니다.");
         }
            while (rset.next()) {
                System.out.println(
                        "책 번호 : " + rset.getInt("ISBN") +
                        " | 책제목 : " + rset.getString("SUBJECT") +
                        " | 저자 : " + rset.getString("AUTHOR") +
                        " | 출판사 : " + rset.getString("PUBLISHER") +
                        " | 출판연도 : " + rset.getInt("PUBLIC_YEAR") +
                        " | 장르 : " + rset.getString("GENRE") +
                        " | 대여 상태 : " + rset.getString("STATUS_RENT")
                 );
            }

        }  catch (Exception e) {
            System.out.println("오류!! "+ e.getMessage()+"이전으로 돌아갑니다");
        } finally {
            close(con);
            close(pstmt);
            close(rset);
        }
    }

    public void genreSearch(Connection con) {
      
        try {
            System.out.println("장르 검색 : ");
            String genre = sc.nextLine();

            pstmt = con.prepareStatement(prop.getProperty("genresearch"));
            pstmt.setString(1, genre);
            rset = pstmt.executeQuery();
            if (!rset.isBeforeFirst()) {
                throw new Exception("해당 장르의 책이 존재하지 않습니다.");
            }
            if (genre.matches("\\d+")) {
                throw new IllegalArgumentException("이름에 숫자입력은 안됩니다.");
            }

            while (rset.next()) {
                System.out.println(
                        "책 번호 : " + rset.getInt("ISBN") +
                                " | 책제목 : " + rset.getString("SUBJECT") +
                                " | 저자 : " + rset.getString("AUTHOR") +
                                " | 출판사 : " + rset.getString("PUBLISHER") +
                                " | 출판연도 : " + rset.getInt("PUBLIC_YEAR") +
                                " | 장르 : " + rset.getString("GENRE") +
                                " | 대여 상태 : " + rset.getString("STATUS_RENT")
                );
            }

        }  catch (Exception e) {
            System.out.println("오류!! "+ e.getMessage()+"이전으로 돌아갑니다");
        } finally {
            close(con);
            close(pstmt);
            close(rset);
        }
    }

    public void yearSearch(Connection con) {

        try {
            System.out.println("출판 년도 : ");
            String year = sc.nextLine();
            String tran = year.replaceAll("\\D", "");
            int year1 = Integer.parseInt(tran);
            if(year1 > 2024){
                throw new Exception("출판년도가 미래일 수 없습니다");
            }

            pstmt = con.prepareStatement(prop.getProperty("yearsearch"));
            pstmt.setString(1, year);
            rset = pstmt.executeQuery();

            if (!rset.isBeforeFirst()) {
                throw new Exception("해당 년도의 책이 존재하지 않습니다.");
            }

            while (rset.next()) {
                System.out.println(
                        "책 번호 : " + rset.getInt("ISBN") +
                                " | 책제목 : " + rset.getString("SUBJECT") +
                                " | 저자 : " + rset.getString("AUTHOR") +
                                " | 출판사 : " + rset.getString("PUBLISHER") +
                                " | 출판연도 : " + rset.getInt("PUBLIC_YEAR") +
                                " | 장르 : " + rset.getString("GENRE") +
                                " | 대여 상태 : " + rset.getString("STATUS_RENT")
                );
            }

        } catch (Exception e) {
            System.out.println("오류!! "+ e.getMessage()+"이전으로 돌아갑니다");
        } finally {
            close(con);
            close(pstmt);
            close(rset);
        }
    }

    public void allSearch(Connection con){


        System.out.println("책 전체 리스트");
        try {
            //prop.loadFromXML(new FileInputStream(url));
            pstmt = con.prepareStatement(prop.getProperty("allSearch"));

            rset = pstmt.executeQuery();

            while(rset.next()){
                System.out.println(
                        "책 번호 : " + rset.getInt("ISBN") +
                                " | 책제목 : " + rset.getString("SUBJECT") +
                                " | 저자 : " + rset.getString("AUTHOR") +
                                " | 출판사 : " + rset.getString("PUBLISHER") +
                                " | 출판연도 : " + rset.getInt("PUBLIC_YEAR") +
                                " | 장르 : " + rset.getString("GENRE") +
                                " | 대여 상태 : " + rset.getString("STATUS_RENT")
                );
            }
        }  catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(con);
            close(pstmt);
            close(rset);
        }
    }

    public int reserves(int a, Connection con){
        int userCode = a;

        //대여중만 예약하기
        System.out.println("예약하실 책 제목을 입력해주세요.");
        String subject = sc.nextLine();
        int result = 0;

        // 임시 리스트 생성
        List<StatusDTO> tempStatusList = new ArrayList<>();

        try {
            // 첫번째 상태 저장
            StatusDTO currentStatusDTO = bookStatusDAO.getStatusDTO(con, subject);

            if(currentStatusDTO != null){
                tempStatusList.add(currentStatusDTO);
            }


            pstmt = con.prepareStatement(prop.getProperty("findISBN"));
            pstmt.setString(1, subject);
            rset = pstmt.executeQuery();
            if (!rset.isBeforeFirst()) {
                throw new Exception("해당 제목의 책이 존재하지 않습니다.");
            }
            int reserveNum = 0;
            while(rset.next()){
                reserveNum = rset.getInt("ISBN");
            }

            pstmt = con.prepareStatement(prop.getProperty("checkbookstatus"));
            pstmt.setInt(1, reserveNum);
            rset = pstmt.executeQuery();
            String rentStataus = "";
            int retnUserCode = 0;

            while(rset.next()) {
                rentStataus = rset.getString("STATUS_RENT");
                retnUserCode = rset.getInt("USER_CODE");
            }

            pstmt = con.prepareStatement(prop.getProperty("checkbookReserve"));
            pstmt.setInt(1, reserveNum);
            rset = pstmt.executeQuery();

            String currentStatus = "";
            while(rset.next()) {
                currentStatus = rset.getString("STATUS_RESERVE");
            }

            if(rentStataus.equals("대여 중")){

                if(retnUserCode == userCode){
                    System.out.println("본인이 대여중인 책입니다. 다시 시도해주세요.");
                    result = 0;
                    return result;
                }

                if ("예약 중".equals(currentStatus)) {
                    // System.out.println("예약 중 ");

                    System.out.println("예약 중인 책입니다. 이전으로 돌아갑니다");
                    result = 0;
                    return result;
                }else{
                    // System.out.println("대여 중 예약가능");

                    // 대여 중  예약가능
                    pstmt = con.prepareStatement(prop.getProperty("setReserve"));
                    pstmt.setString(1,"예약 중");
                    pstmt.setInt(2, userCode);
                    pstmt.setInt(3, reserveNum);
                    result = pstmt.executeUpdate();



                    // 변경된 이력 저장
                    StatusDTO updateStatus = new StatusDTO(subject, currentStatusDTO.getStatus_rent(), "예약 중", currentStatusDTO.getDate_rent(), currentStatusDTO.getDate_return(), reserveNum, userCode, currentStatusDTO.getDate_end());
                    tempStatusList.add(updateStatus);

                    statusList.addAll(tempStatusList);

                    bookStatusDAO.storeHistory(con);

                    /*

                    pstmt = con.prepareStatement(prop.getProperty("checkbookstatus"));
                    pstmt.setInt(1, reserveNum);

                    rset = pstmt.executeQuery();

                    String endDay = "";
                    while(rset.next()) {
                        endDay = rset.getString("DATE_END");
                    }*/
                    // System.out.println("예약 완료했습니다 "+ endDay+" 이후로 수령 가능합니다.");


                }
            }else {
                // 대여가능
                System.out.println("예약불가인 책입니다. 이전으로 돌아갑니다");
                result = 0;
                return result;
            }
        } catch (Exception e) {
            System.out.println("오류!! "+ e.getMessage()+"이전으로 돌아갑니다");
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }
        return result;
    }

    public UserDTO showUserInfo(Connection con, int userCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        UserDTO userDTO = new UserDTO();
        try {
            pstmt = con.prepareStatement(prop.getProperty("showUserInfo"));
            pstmt.setInt(1, userCode);
            rset = pstmt.executeQuery();
            while(rset.next()) {
                userDTO.setName(rset.getString("NAME"));
                userDTO.setPhone(rset.getString("PHONE"));
                userDTO.setUser_id(rset.getString("USER_ID"));
                userDTO.setUser_pwd(rset.getString("USER_PWD"));
                userDTO.setUser_code(userCode);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }
        return userDTO;
    }

    public int updateUser(Connection con, UserDTO userDTO) {
        PreparedStatement pstmt = null;
        int result = 0;

        String query = "UPDATE tbl_user SET ";

        if (userDTO.getName() != null) {
            query = query + "name = '" + userDTO.getName() + "', ";
        }
        if (userDTO.getPhone() != null) {
            query = query + "phone = '" + userDTO.getPhone() + "', ";
        }
        if (userDTO.getUser_id() != null) {
            query = query + "user_id = '" + userDTO.getUser_id() + "', ";
        }
        if (userDTO.getUser_pwd() != null) {
            query = query + "user_pwd = '" + userDTO.getUser_pwd() + "', ";
        }

        query = query.substring(0,query.length()-2);
        query = query + " WHERE user_code = " + userDTO.getUser_code();

//        System.out.println(query);

        try {
            pstmt = con.prepareStatement(query);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
        }
    return result;
    }

    public void showRentedList(Connection con, int userCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            pstmt = con.prepareStatement(prop.getProperty("showRentedList"));
            pstmt.setInt(1, userCode);
            rset = pstmt.executeQuery();
            System.out.println("현재 대여중인 책 목록 : ");
            System.out.println("제목 | 대여 날짜 | 반납 기한 | 연체 여부");
            if (!rset.next()) {
                System.out.println("===============없음================");
            } else{
                do {
                    System.out.print(rset.getString(1) + " | " + rset.getString(2) + " | " + rset.getString(3) + " | " + rset.getString(4));
                    System.out.println();
                } while (rset.next());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }

    }

    public void showOverdueList(Connection con, int userCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            pstmt = con.prepareStatement(prop.getProperty("showOverdueList"));
            pstmt.setInt(1, userCode);
            rset = pstmt.executeQuery();
            System.out.println("현재 연체중인 책 목록 : ");
            System.out.println("제목 | 연체료 | 연체일");
            if (!rset.next()) {
                System.out.println("===============없음================");
            } else {
                do {
                    System.out.print(rset.getString(1) + " | " + rset.getString(2) + "원 | " + rset.getString(3) + "일");
                    System.out.println();
                } while (rset.next());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }

    }

    public void showReservedList(Connection con, int userCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            pstmt = con.prepareStatement(prop.getProperty("showReservedList"));
            pstmt.setInt(1, userCode);
            rset = pstmt.executeQuery();
            System.out.println("현재 예약중인 책 목록 : ");
            if (!rset.next()) {
                System.out.println("===============없음================");
            } else{
                do {
                    System.out.println(rset.getString(1));
                } while (rset.next());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con);
            close(pstmt);
            close(rset);
        }

    }
}
