package com.ohgiraffers.book.login.controller;

import com.ohgiraffers.book.CommonMember.CommonMemberUI;
import com.ohgiraffers.book.login.dao.LoginDAO;
import com.ohgiraffers.book.login.dto.UserDTO;

import java.util.Scanner;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.getConnection;

public class LoginController {

    private LoginDAO loginDAO = new LoginDAO("src/main/resources/mapper/book-query.xml");


    public void login(){
        Scanner scr = new Scanner(System.in);
        UserDTO userDTO = new UserDTO();
        CommonMemberUI commonMemberUI = new CommonMemberUI();
        System.out.println("--------------로그인-----------------");
        System.out.println("ID 입력 : ");
        userDTO.setUser_id(scr.nextLine());
        System.out.println("Password 입력 : ");
        userDTO.setUser_pwd(scr.nextLine());
        int result = loginDAO.login(getConnection(),userDTO);

        if(result == 0){
            System.out.println("로그인 실패! ID 또는 Password 가 맞지 않습니다.");

        } else if(result == 1) {
            System.out.println("관리자라면 관리자 로그인 메뉴로 가세요~~");

        } else {
            System.out.println("로그인 성공!!");
            commonMemberUI.userUI(result);

        }

    }

}
