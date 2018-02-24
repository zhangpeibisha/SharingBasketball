package main.page;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Controller;
import main.entity.Project;
import main.entity.ProjectSon;
import main.file.FileController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Create by zhangpe0312@qq.com on 2018/2/15.
 * <p>
 * 填写工程信息
 */
public class ProjectInfo {

    /**
     * 接受参数项目名字和数量
     *
     * @param name
     * @param number
     */
    public static void setProjectInfo(String name, int number) {
        Stage stage = new Stage();

        //初始化项目
        Project project = new Project(name);
        for (int i = 0; i < number; i++) {
            ProjectSon son = new ProjectSon(i, "");
            project.addSons(son);
        }
        /**
         * 依然为上中下布局
         *
         * 题目 - 项目具体信息 -    按钮
         */

        VBox mainBox = new VBox(30);
        mainBox.setAlignment(Pos.CENTER);

        Label title = new Label("题目");
        mainBox.getChildren().addAll(title);

        //项目名信息
        HBox scro = new HBox();
        scro.setMinWidth(500);
        scro.setAlignment(Pos.CENTER);

        VBox pro = new VBox(10);
        for (int i = 0; i < number; i++) {
            HBox hBox = AddInfo.setInfo(i);
            pro.getChildren().add(hBox);
        }
        //滑动条
        ScrollPane sp = new ScrollPane();
        sp.setPrefViewportWidth(50);
        sp.setVmax(440);
        sp.setPrefSize(600, 150);
        sp.setContent(pro);

        scro.getChildren().add(sp);
        mainBox.getChildren().addAll(scro);


        ArrayList<TextField> inputs = new ArrayList<>();
        ArrayList<Label> texts = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            inputs.add((TextField) (
                    (HBox) pro.getChildren().get(i))
                    .getChildren().get(1));
            texts.add((Label) (
                    (HBox) pro.getChildren().get(i))
                    .getChildren().get(2));
        }
        //检验数据是否合格
        BooleanBinding isTrue = new BooleanBinding() {
            {
                for (int i = 0; i < number; i++) {
                    super.bind(inputs.get(i).textProperty());
                }
            }

            @Override
            protected boolean computeValue() {

                boolean flag = false;

                for (int i = 0; i < number; i++) {
                    try {
                        if (inputs.get(i).getText().equals("")) {
                            texts.get(i).setText("请输入项目信息，不能为空。");
                            flag = true;
                        } else {
                            Integer.parseInt(inputs.get(i).getText());
                            texts.get(i).setText("请输入人名（字符），不能为数字。");
                            flag = true;
                        }
                    } catch (Exception e) {
                        texts.get(i).setText("");
                    }
                }
                return flag;
            }
        };


        //按钮
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button save = new Button("Save Project");
        save.setDefaultButton(true);
        save.setMinWidth(200);

        save.disableProperty().bind(isTrue);
        save.setOnAction(event -> {

            ArrayList<ProjectSon> projectSon = project.getSons();
            for (int i = 0; i < number; i++) {
                projectSon.get(i).setText(inputs.get(i).getText());
            }

            //保存到文本文档
            try {
                ArrayList<Project> projects = FileController.ToJava();
                if (projects==null){
                    projects = new ArrayList<>();
                    projects.add(project);
                }else {
                    projects.add(project);
                }
                FileController.toTxt(projects);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"添加成功");
            alert.show();

            try {
                new Controller().start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button Menu = new Button("Main Menu");
        Menu.setDefaultButton(true);
        Menu.setMinWidth(200);
        Menu.setOnAction(event -> {
            try {
                new Controller().start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        buttons.getChildren().addAll(save, Menu);

        mainBox.getChildren().addAll(buttons);

        Scene scene = new Scene(mainBox, 800, 400);
        stage.setScene(scene);
        stage.show();


    }


}
