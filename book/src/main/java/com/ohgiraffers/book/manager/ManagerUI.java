package com.ohgiraffers.book.manager;

import com.ohgiraffers.book.CommonMember.controller.CommonMemberController;
import com.ohgiraffers.book.manager.controller.BookStatusController;
import com.ohgiraffers.book.manager.controller.BookManageController;
import com.ohgiraffers.book.manager.controller.BookStatController;
import com.ohgiraffers.book.manager.controller.UserController;
import com.ohgiraffers.book.manager.controller.OverdueController;

import java.util.Scanner;

public class ManagerUI {

    public void manager(){

        OverdueController overdueController = new OverdueController();
        while(true){
            Scanner scr = new Scanner(System.in);
            System.out.println("==========================================");
            System.out.println("메뉴를 선택해주세요.");
            System.out.println("1. 도서 정보 관리");
            System.out.println("2. 사용자 관리");
            System.out.println("3. 도서 상태 관리");
            System.out.println("4. 도서 통계");
            System.out.println("5. 연체 회원 조회");
            System.out.println("9. 로그아웃");
            int choice = scr.nextInt();
            scr.nextLine();

            switch(choice){
                case 1:
                    book_manage();
                    break;
                case 2:
                    user_manage();
                    break;
                case 3:
                    book_status();
                    break;
                case 4:
                    book_stat();
                    break;
                case 5:
                    overdueController.overduelist();
                    break;
                case 9:
                    System.out.println("로그아웃 성공 ! 👋");
                    return;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }

    }

    private void book_stat() {
        BookStatController bookStatController = new BookStatController();

        sloop: while(true){
            Scanner scr = new Scanner(System.in);
            System.out.println("==========================================");
            System.out.println("1. 도서 총 개수");
            System.out.println("2. 장르별 도서 개수");
            System.out.println("3. 대여 중인 도서 개수");
            System.out.println("9. 이전으로 돌아가기");
            int choice = scr.nextInt();
            scr.nextLine();

            switch(choice){
                case 1: bookStatController.countAllBook();
                break;
                case 2:
                    bookStatController.showAllGenre();
                    bookStatController.countByGenre();
                break;
                case 3: bookStatController.countRented();
                break;
                case 9: break sloop;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }

    }

    public void book_manage() {
        BookManageController bookManageController = new BookManageController();

        mloop: while(true){
            Scanner scr = new Scanner(System.in);
            System.out.println("==========================================");
            System.out.println("1. 도서 정보 검색");
            System.out.println("2. 도서 추가");
            System.out.println("3. 도서 수정");
            System.out.println("4. 도서 삭제");
            System.out.println("9. 이전으로 돌아가기");
            int choice = scr.nextInt();
            scr.nextLine();
            switch(choice){
                case 1 :
                    bookSearch();
                    break;
                case 2:
                    bookManageController.insertBook();
                    break;
                case 3:
                    bookManageController.updateBook();
                    break;
                case 4:
                    bookManageController.deleteBook();
                    break;
                case 9:
                    break mloop;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }
    }

    public void bookSearch(){
        Scanner scr = new Scanner(System.in);

        bsloop:while(true){
            System.out.println("==========================================");
            System.out.println("1. 제목 검색");
            System.out.println("2. 저자 검색");
            System.out.println("3. 출판연도 검색");
            System.out.println("4. 장르 검색");
            System.out.println("5. 전체 조회 검색");
            System.out.println("9. 이전으로 돌아가기");

            int num = scr.nextInt();
            scr.nextLine();
            CommonMemberController ft = new CommonMemberController();

            switch (num){
                case 1: ft.titleSearch(); break;
                case 2: ft.authorSearch(); break;
                case 3: ft.yearSearch(); break;
                case 4:
                    BookStatController bookStatController = new BookStatController();
                    bookStatController.showAllGenre();
                    ft.genreSearch(); break;
                case 5: ft.allSearch(); break;
                case 9: break bsloop;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }

    }


    public void book_status(){

        BookStatusController bookStatusController = new BookStatusController();

        loop:while(true){

            Scanner scr = new Scanner(System.in);
            System.out.println("==========================================");
            System.out.println("1. 도서 상태 변경");
            System.out.println("2. 도서 상태 변경이력 조회");
            System.out.println("9. 이전으로 돌아가기");
            int choice = scr.nextInt();

            switch (choice){
                case 1:
                    bookStatusController.updateStatus();
                    break;
                case 2:
                    bookStatusController.selectStatus();
                    break;
                case 9:
                    break loop;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }

    }

    public void user_manage(){
        UserController userController = new UserController();

        loop : while(true){
            Scanner scr = new Scanner(System.in);
            System.out.println("==========================================");
            System.out.println("1. 회원 검색");
            System.out.println("2. 회원 삭제");
            System.out.println("3. 전체 회원 리스트 보기");
            System.out.println("9. 이전으로 돌아가기");

            int choice = scr.nextInt();

            switch (choice){
                case 1: userController.selectUser(); break;
                case 2: userController.deleteUser(); break;
                case 3: userController.userList(); break;
                case 9: break loop;
                default:
                    System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
            }
        }
    }

    public void overDue(){
        OverdueController overdueController = new OverdueController();
        Scanner sc = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("1. 연체중인 회원 리스트조회");
        System.out.println("2. 연체회원 새로고침");
        int num = sc.nextInt();
        sc.nextLine();
        switch (num){
            case 1:
                overdueController.overduelist(); break;
            case 2:
                overdueController.overdueAutoInsert(); break;
            default:
                System.out.println("잘못된 번호 입력!! 다시 입력해주세요"); break;
        }

    }
}
