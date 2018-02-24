package main.entity;

import java.io.Serializable;

/**
 * Create by zhangpe0312@qq.com on 2018/2/17.
 */
public class Votes implements Serializable {
    private String name;
    private int number;

    public Votes(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
