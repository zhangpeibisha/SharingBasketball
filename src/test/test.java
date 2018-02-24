import main.entity.Project;
import main.file.FileController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Create by zhangpe0312@qq.com on 2018/2/17.
 */
public class test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ArrayList<Project> projects = FileController.ToJava();
        System.out.println();
    }
}
