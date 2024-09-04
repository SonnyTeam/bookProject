package com.ohgiraffers.book.manager.dto;

public class StatusDTO {

    private String subject;
    private String status_rent;
    private String status_reserve;
    private String date_rent;
    private String date_return;
    private int isbn;
    private int user_code;
    private String date_end;

    public StatusDTO() {
    }

    public StatusDTO(String status_rent, String status_reserve, int isbn) {
        this.status_rent = status_rent;
        this.status_reserve = status_reserve;
        this.isbn = isbn;
    }

    public StatusDTO(String subject, String status_rent, String date_rent, String date_return) {
        this.subject = subject;
        this.status_rent = status_rent;
        this.date_rent = date_rent;
        this.date_return = date_return;
    }

    public StatusDTO(String subject, String status_rent, String status_reserve, String date_rent, String date_return, int isbn) {
        this.subject = subject;
        this.status_rent = status_rent;
        this.status_reserve = status_reserve;
        this.date_rent = date_rent;
        this.date_return = date_return;
        this.isbn = isbn;

    }public StatusDTO(String subject, String status_rent, String status_reserve, String date_rent, String date_return, int isbn, int user_code, String date_end) {
        this.subject = subject;
        this.status_rent = status_rent;
        this.status_reserve = status_reserve;
        this.date_rent = date_rent;
        this.date_return = date_return;
        this.isbn = isbn;
        this.user_code = user_code;
        this.date_end = date_end;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus_rent() {
        return status_rent;
    }

    public void setStatus_rent(String status_rent) {
        this.status_rent = status_rent;
    }

    public String getDate_rent() {
        return date_rent;
    }

    public void setDate_rent(String date_rent) {
        this.date_rent = date_rent;
    }

    public String getDate_return() {
        return date_return;
    }

    public void setDate_return(String date_return) {
        this.date_return = date_return;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getStatus_reserve() {
        return status_reserve;
    }

    public void setStatus_reserve(String status_reserve) {
        this.status_reserve = status_reserve;
    }

    public int getUser_code() {
        return user_code;
    }

    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    @Override
    public String toString() {
        return "StatusDTO{" +
                "subject='" + subject + '\'' +
                ", status_rent='" + status_rent + '\'' +
                ", status_reserve='" + status_reserve + '\'' +
                ", date_rent='" + date_rent + '\'' +
                ", date_return='" + date_return + '\'' +
                ", isbn=" + isbn +
                ", user_code=" + user_code +
                ", date_end='" + date_end + '\'' +
                '}';
    }
}
