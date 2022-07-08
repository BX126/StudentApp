package com.example.studentapp;

import java.io.Serializable;

class Person implements Serializable{
    String name;
    String tg;
    String pr;
    String pg;

    Person(String name, String tg, String pg,String pr) {
        this.name = name;
        this.tg = tg;
        this.pr = pr;
        this.pg = pg;
    }
}