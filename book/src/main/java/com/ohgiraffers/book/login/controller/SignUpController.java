package com.ohgiraffers.book.login.controller;

import com.ohgiraffers.book.login.dao.SignUpDAO;
import com.ohgiraffers.book.login.dto.UserDTO;

import java.util.Scanner;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.getConnection;

public class SignUpController {

    private SignUpDAO signUpDAO = new SignUpDAO("src/main/resources/mapper/book-query.xml");

    public void signUp() {

        Scanner scr = new Scanner(System.in);
        UserDTO userDTO = new UserDTO();

        System.out.println("--------------회원가입-----------------");
        System.out.println("이름 입력 : ");
        userDTO.setName(scr.nextLine());
        System.out.println("전화번호 입력 : ");
        userDTO.setPhone(scr.nextLine());
        System.out.println("ID 입력 : ");
        userDTO.setUser_id(scr.nextLine());
        System.out.println("Password 입력 : ");
        userDTO.setUser_pwd(scr.nextLine());
        int result = signUpDAO.signUp(getConnection(), userDTO);
        if (result == 1) {
            System.out.println("회원가입 성공");
        } else {
            System.out.println("회원가입 실패???????");
        }

    }
}