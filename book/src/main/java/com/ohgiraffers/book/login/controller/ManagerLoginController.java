package com.ohgiraffers.book.login.controller;

import com.ohgiraffers.book.login.dao.LoginDAO;
import com.ohgiraffers.book.manager.ManagerUI;
import com.ohgiraffers.book.login.dto.UserDTO;

import java.util.Scanner;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.getConnection;

public class ManagerLoginController {

    private LoginDAO loginDAO = new LoginDAO("src/main/resources/mapper/book-query.xml");

    public void managerLogin() {
        Scanner scr = new Scanner(System.in);
        UserDTO userDTO = new UserDTO();

        System.out.println("--------------관리자 로그인-----------------");
        System.out.println("관리자 ID 입력 : ");
        userDTO.setUser_id(scr.nextLine());
        System.out.println("관리자 Password 입력 : ");
        userDTO.setUser_pwd(scr.nextLine());
        int result = loginDAO.login(getConnection(), userDTO);

        if (result == 1) {
            System.out.println("관리자 로그인 성공");
            ManagerUI managerUI = new ManagerUI();
            managerUI.manager();
        } else {
            System.out.println("관리자 로그인 실패!!\nID 또는 Password가 맞지 않습니다.");
        }
    }
}
