package main.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Create by zhangpe0312@qq.com on 2018/2/14.
 *
 * 工程里面的子项目 每个项目内容为 id号和内容
 */
public class ProjectSon implements Serializable {

    private int id;
    //会员名字
    private String text;
    //该会员的票
    private ArrayList<Votes> votes = new ArrayList<>();

    public ProjectSon(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Votes> getVotes() {
        return votes;
    }

    public void addVotes(Votes votes) {
        this.votes.add(votes);
    }
}
