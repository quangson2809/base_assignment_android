package com.example.base;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String name;
    private int isCheck;
    private int isLike;

    public Item(int id, String name,int isLike) {
        this.id = id;
        this.name = name;
        this.isCheck = 0;
        this.isLike = isLike;
    }
    public Item(String name,int isLike) {
        this.name = name;
        this.isCheck = 0;
        this.isLike = isLike;
    }
    public Item(){};

    public boolean isChecked(){
        return isCheck == 1 ? true : false;
    }

    public boolean isLiked(){
        return isLike == 1 ? true : false;
    }

    public String makeName(){
        if(isLiked())
            return name + "(+like)";
        else
            return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
