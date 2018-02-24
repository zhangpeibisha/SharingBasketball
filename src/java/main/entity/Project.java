package main.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by zhangpe0312@qq.com on 2018/2/14.
 *
 * 工程母体 装载了子项目和工程名字
 *
 */
public class Project implements Serializable{

    private String name;
    private ArrayList<ProjectSon> sons = new ArrayList<>();

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<ProjectSon> getSons() {
        return sons;
    }

    public void addSons(ProjectSon son) {
        sons.add(son);
    }

    /**
     * @return 得到这个母项目有多少个子项目
     */
    public int getSize(){
        return sons.size();
    }
}
