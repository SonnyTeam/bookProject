package com.ohgiraffers.book.manager.controller;

import com.ohgiraffers.book.manager.dto.BookDTO;
import com.ohgiraffers.book.manager.dao.BookManageDAO;

import java.util.Scanner;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.getConnection;

public class BookManageController {
    /*
    * 도서 정보 관리
    * 도서 추가,수정,삭제
    * */

    private BookManageDAO bookManageDAO =
            new BookManageDAO("src/main/resources/mapper/bookmanage-query.xml");

    public void insertBook(){

        Scanner scr = new Scanner(System.in);
        BookDTO bookDTO = new BookDTO();

        System.out.println("도서 추가를 시작");
        System.out.println("추가할 도서의 제목을 입력 : ");
        bookDTO.setSubject(scr.nextLine());
        System.out.println("추가할 도서의 저자를 입력 : ");
        bookDTO.setAuthor(scr.nextLine());
        System.out.println("추가할 도서의 출판사를 입력 : ");
        bookDTO.setPublisher(scr.nextLine());
        System.out.println("추가할 도서의 출판연도를 입력 : ");
        try {
            bookDTO.setPublic_year(scr.nextInt());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        scr.nextLine();
        System.out.println("추가할 도서의 장르를 입력 : ");
        bookDTO.setGenre(scr.nextLine());
        System.out.println("추가할 도서의 페이지수를 입력 : ");
        try {
            bookDTO.setPages(scr.nextInt());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        scr.nextLine();


        int result = bookManageDAO.insertBook(getConnection(), bookDTO);

        if(result == 0){
            System.out.println("도서 추가 실패");
        }else {
            System.out.println("도서 추가를 완료!!");
            System.out.println(bookDTO);
            int ISBN = bookManageDAO.findISBN(getConnection(), bookDTO.getSubject());

            //tbl_status 추가 시작
            //subject로 ISBN 찾아와서 insert
            int num = bookManageDAO.insertOrDeleteBookStatus(getConnection(), ISBN,1);
            if(num == 0){
                System.out.println("도서 상태 추가 실패");
            }else {
                System.out.println("도서 상태 추가 완료!");
                int result1 = bookManageDAO.insertOrdeleteBookReserveStatus(getConnection(), ISBN, 1);
                if(result1 == 0){
                    System.out.println("도서 예약 상태 추가 실패");
                }else {
                    System.out.println("도서 예약 상태 추가 완료!!");
                }
            }
        }

    }

    public void deleteBook(){

        Scanner scr = new Scanner(System.in);
        System.out.println("도서 삭제를 시작");
        System.out.println("삭제할 도서의 제목을 입력 : ");
        String subject = scr.nextLine();
        int ISBN = bookManageDAO.findISBN(getConnection(), subject);
        int num = bookManageDAO.insertOrDeleteBookStatus(getConnection(), ISBN, 2);
        if(num == 0){
            System.out.println("도서 상태 삭제 실패");
        }else {
            System.out.println("도서 상태 삭제 완료!");
            int result1 = bookManageDAO.insertOrdeleteBookReserveStatus(getConnection(), ISBN, 2);
            if(result1 == 0) {
                System.out.println("도서 예약 상태 삭제 실패");
            } else {
                System.out.println("도서 예약 상태 삭제 완료!!");
                int result = bookManageDAO.deleteBook(getConnection(), subject);
                if(result == 1){
                    System.out.println("도서 삭제를 성공하였습니다.");
                }else {
                    System.out.println("도서 삭제 실패");
                }
            }

        }

    }

    public void updateBook() {
        Scanner scr = new Scanner(System.in);
        BookDTO bookDTO = new BookDTO();
        int result = 0;

        uloop :while (true) {
//            System.out.println("도서 수정을 시작");
            System.out.println("수정할 도서의 제목을 입력 : ");
            String subject =scr.nextLine();
            bookDTO =bookManageDAO.selectBySubject(getConnection(), subject);
            if(bookDTO.getPages() == 0){continue;}

            System.out.println("수정 내용 입력\n수정불필요 항목은 ENTER로 스킵!!!");
            System.out.println("수정할 도서의 제목을 입력 : ");
            bookDTO.setSubject(scr.nextLine());
            System.out.println("수정할 도서의 저자를 입력 : ");
            bookDTO.setAuthor(scr.nextLine());
            System.out.println("수정할 도서의 출판사를 입력 : ");
            bookDTO.setPublisher(scr.nextLine());

            System.out.println("수정할 도서의 출판연도를 입력 : ");
            String public_year = scr.nextLine();
            if (public_year.equals("")) {
                bookDTO.setPublic_year(0, "skip");
            }
            else{
                bookDTO.setPublic_year(Integer.parseInt(public_year));
            }

            System.out.println("수정할 도서의 장르를 입력 : ");
            bookDTO.setGenre(scr.nextLine());
            System.out.println("수정할 도서의 페이지수를 입력 : ");
            String genre = scr.nextLine();
            if(genre.equals("")){
                bookDTO.setPages(0,"skip");
            } else {
                bookDTO.setPages(Integer.parseInt(genre));
            }

            result = bookManageDAO.updateBook(getConnection(), subject, bookDTO);
            if(result == 1){
                System.out.println("도서 수정을 완료했습니다.");
                bookManageDAO.selectBySubject(getConnection(), subject);
                break uloop;
            }else {
                System.out.println("도서 수정 실패");
            }
        }

    }
}
