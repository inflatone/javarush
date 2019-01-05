package com.javarush.task.task29.task2909.user;

public class User {
    private String name;
    private String surname;
    private int age;
    private boolean man;

    private Address address;
    private Work work;

    public User(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMan() {
        return man;
    }

    public void setMan(boolean man) {
        this.man = man;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public void printInfo() {
        System.out.printf("Имя: %s%nФамилия: %s%n", name, surname);
    }

    public void printAdditionalInfo() {
        System.out.printf(
                "Пользователь %s 16 лет%n",
                age < 16 ? "моложе" : "старше"
        );
    }

    public String getCountry() {
        return address.getCountry();
    }

    public void setCountry(String country) {
        address.setCountry(country);
    }

    public String getCity() {
        return address.getCity();
    }

    public void setCity(String city) {
        address.setCity(city);
    }

    public String getAddress() {
        return String.format("%s %s %s", address.getCountry(), address.getCity(), address.getHouse());
    }

    public String getBoss() {
        return getWork().getBoss();
    }
}