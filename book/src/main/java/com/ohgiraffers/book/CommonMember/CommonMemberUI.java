package com.ohgiraffers.book.CommonMember;

import com.ohgiraffers.book.CommonMember.controller.CommonMemberController;
import com.ohgiraffers.book.manager.controller.BookStatController;

import java.util.Scanner;

public class CommonMemberUI {
    Scanner sc = new Scanner(System.in);
    CommonMemberController ft = new CommonMemberController();

    public String userUI(int a){
        int userCode = a;
        loop:while(true){
            System.out.println("일반 회원 메뉴");
            System.out.println("1. 도서 검색");
            System.out.println("2. 대여");
            System.out.println("3. 반납");
            System.out.println("4. 예약하기");
            System.out.println("5. 마이페이지");
            System.out.println("9. 로그아웃");
            int num = sc.nextInt();
            sc.nextLine();
            switch (num) {
                case 1:
                    searchBook();
                    break;
                case 2:
                    ft.rental(userCode);
                    break;
                case 3:
                    ft.returnBook(userCode);
                    break;
                case 4:
                    ft.reserves(userCode);
                    break;
                case 5:
                    mypage(userCode);
                    break;
                case 9:
                    System.out.println("로그아웃 성공 ! 👋");
                    break loop;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }
        return null;
    }

    private void mypage(int userCode) {
        myloop:while(true) {
            System.out.println("===마이페이지===");
            System.out.println("1. 내 정보 수정");
            System.out.println("2. 대여 중인 책 목록");
            System.out.println("3. 예약 중인 책 목록");
            System.out.println("4. 연체 중인 책 목록");
            System.out.println("9. 이전으로 돌아가기");
            int num = sc.nextInt();
            sc.nextLine();
            switch (num) {
                case 1:
                    ft.updateUser(userCode);
                    break;
                case 2:
                    ft.showRentedList(userCode);
                    break;
                case 3:
                    ft.showReservedList(userCode);
                    break;
                case 4:
                    ft.showOverdueList(userCode);
                    break;
                case 9:
                    break myloop;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }
    }

    public void searchBook(){

        loop:while (true) {

            System.out.println("1. 제목 검색");
            System.out.println("2. 저자 검색");
            System.out.println("3. 출판연도 검색");
            System.out.println("4. 장르 검색");
            System.out.println("5. 전체 조회 검색");
            System.out.println("9. 이전으로 돌아가기");

            int num = sc.nextInt();
            sc.nextLine();
            switch (num){
                case 1: ft.titleSearch(); break;
                case 2: ft.authorSearch(); break;
                case 3: ft.yearSearch(); break;
                case 4:
                    BookStatController bookStatController = new BookStatController();
                    bookStatController.showAllGenre();
                    ft.genreSearch(); break;
                case 5: ft.allSearch(); break;
                //case 6: userUI(userCode); break;
                case 9:
                     break loop;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }

    }
}
