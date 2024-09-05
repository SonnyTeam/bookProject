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
        while (true){
            System.out.println("ID 입력 : ");
            userDTO.setUser_id(scr.nextLine());
            int result = signUpDAO.signUpIDCheck(getConnection(), userDTO);
            if(result == 0){
                System.out.println("ID 체크 완료!!\n중복되는 ID가 없습니다.");
                break;
            }else {
                System.out.println(userDTO.getUser_id() + " 은/는 이미 사용중인 ID 입니다. ID 를 다시 입력해주세요.");
            }
        }

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