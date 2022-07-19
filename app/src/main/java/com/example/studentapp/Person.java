package com.example.studentapp;

import java.io.Serializable;

class Person implements Serializable{
    int id;
    String name;
    String tg;
    String pr;
    String pg;

    Person(int id,String name, String tg, String pg,String pr) {
        this.id = id;
        this.name = name;
        this.tg = tg;
        this.pr = pr;
        this.pg = pg;
    }
}