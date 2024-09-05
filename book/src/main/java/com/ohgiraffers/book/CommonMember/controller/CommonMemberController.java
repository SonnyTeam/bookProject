package com.ohgiraffers.book.CommonMember.controller;

import com.ohgiraffers.book.CommonMember.dao.CommonMemberDAO;
import com.ohgiraffers.book.login.dao.SignUpDAO;
import com.ohgiraffers.book.login.dto.UserDTO;

import java.util.Scanner;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.getConnection;

public class CommonMemberController {


    public CommonMemberDAO dao = new CommonMemberDAO("src/main/resources/mapper/book-query.xml");
    private SignUpDAO signUpDAO = new SignUpDAO("src/main/resources/mapper/book-query.xml");


    public String rental(int a){ //책 대여
        String returnRental = dao.rental(a, getConnection());

        return returnRental;
    }

    public String returnBook(int a){
        String returnBook = dao.returnBook(a, getConnection());

        return returnBook;
    }

    public void titleSearch() {
        dao.titleSearch(getConnection());
    }
    public void authorSearch() {
        dao.authorSearch(getConnection());
    }
    public void genreSearch() {
        dao.genreSearch(getConnection());
    }

    public void yearSearch() {
        dao.yearSearch(getConnection());
    }

    public void allSearch(){
        dao.allSearch(getConnection());
    }

    public void reserves(int a){
        int result = dao.reserves(a, getConnection());

        if(result == 1){
            System.out.println("예약 완료했습니다 ");
        }else {
            System.out.println("예약에 실패했습니다.");
        }
    }

    public void updateUser(int userCode) {
        Scanner scr = new Scanner(System.in);
        System.out.println("===나의 정보 수정===");
        UserDTO userDTO = dao.showUserInfo(getConnection(), userCode);
        System.out.println("현재 나의 정보 : \n" + userDTO);
        System.out.println("수정 내용 입력\n수정불필요 항목은 ENTER로 스킵!!!");

        System.out.println("이름을 입력 : ");
        userDTO.setName(scr.nextLine());
        System.out.println("전화번호를 입력 : ");
        userDTO.setPhone(scr.nextLine());
        while (true){
            System.out.println("id를 입력 : ");
            userDTO.setUser_id(scr.nextLine());
            int result =signUpDAO.signUpIDCheck(getConnection(), userDTO);
            if(result == 0){
                if(userDTO.getUser_id()==null){
                    break;
                }
                System.out.println("ID 체크 완료!!\n중복되는 ID가 없습니다.");
                break;
            }else {
                System.out.println(userDTO.getUser_id() + " 은/는 이미 사용중인 ID 입니다. ID 를 다시 입력해주세요.");
            }
        }

        System.out.println("password를 입력 : ");
        userDTO.setUser_pwd(scr.nextLine());

        int result =dao.updateUser(getConnection(), userDTO);
        if(result==1){
            System.out.println("나의 정보 수정 완료");
            userDTO =dao.showUserInfo(getConnection(), userCode);
            System.out.println("수정된 나의 정보 : " + userDTO);
        }else {
            System.out.println("수정 실패!!");
        }
    }

    public void showRentedList(int userCode) {dao.showRentedList(getConnection(), userCode);}

    public void showOverdueList(int userCode) {dao.showOverdueList(getConnection(), userCode);}

    public void showReservedList(int userCode) {dao.showReservedList(getConnection(), userCode);}
}
