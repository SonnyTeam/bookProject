package com.ohgiraffers.book.manager.controller;

import com.ohgiraffers.book.manager.dao.UserDAO;
import com.ohgiraffers.book.manager.dto.UserDTO;

import java.util.List;
import java.util.Scanner;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.getConnection;

public class UserController {
    /*
    * - **사용자 관리**
    - 도서를 대여하는 사용자(회원) 정보를 관리할 수 있는 기능을 추가한다.
    - 사용자 정보에는 이름, 연락처, 대여 중인 도서 목록 등이 포함된다.
    * */

    UserDAO userDAO = new UserDAO("src/main/resources/mapper/manager-query.xml");

    public void selectUser(){

            Scanner scr = new Scanner(System.in);
            System.out.println("검색할 이름을 입력해주세요.");
            String name = scr.nextLine();

            UserDTO user = userDAO.selectUser(getConnection(), name);

            if(user != null){
                System.out.print("이름 : " + user.getName() + " | 연락처 : " + user.getPhone() + " | 대여 중인 도서 목록 : ");
                if(user.getBorrowedList().isEmpty()){
                    System.out.println("없음");
                }else {
                    for(String subject : user.getBorrowedList()){
                        System.out.print(subject + " ");
                    }
                    System.out.println();
                }
            }

    }


    public void deleteUser(){

            Scanner scr = new Scanner(System.in);
            System.out.println("삭제할 회원의 이름을 입력해주세요.");
            String name = scr.nextLine();
            int userCode = userDAO.findUserCode(getConnection(), name);

            // 대여 테이블 삭제
            int num = userDAO.deleteUserStatus(getConnection(), userCode);

            // 예약 테이블 삭제
            int result1 = userDAO.deleteUserReserveStatus(getConnection(), userCode);

            // 연체 테이블 삭제
            int result2 = userDAO.deleteUserOverdue(getConnection(), userCode);

            // 유저 테이블 삭제
            int result = userDAO.deleteUser(getConnection(), name);
            if(result == 1){
                System.out.println("회원 삭제를 성공하였습니다.");
            }else {
                System.out.println("회원 삭제를 실패했습니다.");
            }

    }



    public void userList(){

        List<UserDTO> users = userDAO.selectAllUser(getConnection());

        for (int i = 1; i < users.size(); i++) {

            System.out.print("이름 : " + users.get(i).getName() + " | 연락처 : " + users.get(i).getPhone() + " | 대여 중인 도서 목록 : ");
            if(users.get(i).getBorrowedList().isEmpty()){
                System.out.println("없음");
            }else {
                for(String subject : users.get(i).getBorrowedList()){
                    System.out.print(subject + " ");

                }
                System.out.println();
            }
        }


    }

}
