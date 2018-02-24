package main.file;

import main.entity.Project;
import main.entity.Votes;

import java.io.*;
import java.util.ArrayList;

/**
 * Create by zhangpe0312@qq.com on 2018/2/14.
 * <p>
 * 向文件中添加删除数据
 */
public class FileController {


    private static final String ProjectPath = "C:\\Users\\Lenovo\\Desktop\\test.txt";
    private static final String VotesPath = "C:\\Users\\Lenovo\\Desktop\\votes.txt";
    /**
     * 添加信息进入文件中
     */
    public static void toTxt(ArrayList<Project> projects) throws IOException {
        File file = new File(ProjectPath);

        if (!file.exists()) {
            file.createNewFile();
        }

        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));

        os.writeObject(projects);
    }

    /**
     * 将数据从文件中读出
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static ArrayList<Project> ToJava() throws IOException, ClassNotFoundException {
        File file = new File(ProjectPath);
        if (!file.exists()) {
            return null;
        }
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream(file));
            Object ob = is.readObject();
            System.out.println("读取成功");
            return (ArrayList<Project>) ob;
        } catch (Exception e) {
            System.out.println("读取异常");
            return new ArrayList<>();
        }
    }

    /**
     * 添加信息进入文件中
     */
    public static void VotestoTxt(ArrayList<Votes> projects) throws IOException {
        File file = new File(VotesPath);

        if (!file.exists()) {
            file.createNewFile();
        }

        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));

        os.writeObject(projects);
    }

    /**
     * 将数据从文件中读出
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static ArrayList<Votes> VotesToJava() {
        File file = new File(VotesPath);
        if (!file.exists()) {
            return null;
        }
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream(file));
            Object ob = is.readObject();
            System.out.println("读取成功");
            return (ArrayList<Votes>) ob;
        } catch (Exception e) {
            System.out.println("读取异常");
            return new ArrayList<>();
        }
    }
}
