package com.ohgiraffers.book.manager.dto;

import java.util.List;

public class UserDTO {

    private String name;
    private String phone;
    private List<String> borrowedList;


    public UserDTO(String name, String phone, List<String> borrowedList) {
        this.name = name;
        this.phone = phone;
        this.borrowedList = borrowedList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getBorrowedList() {
        return borrowedList;
    }

    public void setBorrowedList(List<String> borrowedList) {
        this.borrowedList = borrowedList;
    }


    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", borrowedList=" + borrowedList +
                '}';
    }
}
