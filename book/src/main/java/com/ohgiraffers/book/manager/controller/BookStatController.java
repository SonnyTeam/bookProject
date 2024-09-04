package com.ohgiraffers.book.manager.controller;

import com.ohgiraffers.book.manager.dao.BookStatDAO;

import java.util.Scanner;

import static com.ohgiraffers.book.JDBCTemplate.JDBCTemplate.getConnection;

public class BookStatController {

    /*
    * 도서 통계
    * 도서의 총 개수, 장르별 도서 개수, 대여 중인 도서 개수
    * */

    private BookStatDAO bookStatDAO =
            new BookStatDAO("src/main/resources/mapper/bookstat-query.xml");

    public void countAllBook() {
        bookStatDAO.countAllBook(getConnection());
    }

    public void countByGenre() {
        Scanner scr = new Scanner(System.in);
        System.out.println("총 개수를 찾을 장르를 입력 : ");
        String genre = scr.nextLine();
        bookStatDAO.countByGenre(getConnection(), genre);
    }

    public void countRented() {
        bookStatDAO.countRented(getConnection());
    }

    public void showAllGenre(){bookStatDAO.showAllGenre(getConnection());}
}
