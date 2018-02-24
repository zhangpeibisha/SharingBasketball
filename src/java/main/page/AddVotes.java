package main.page;

import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Controller;
import main.entity.Project;
import main.entity.Votes;
import main.file.FileController;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Create by zhangpe0312@qq.com on 2018/2/17.
 */
public class AddVotes {

    public static void votes(String projectName) {
        Stage stage = new Stage();
        /**
         * 当输入一个项目名字后，会搜索出这个项目中有哪些人，然后手动填写
         * 每个人对应的票数
         */

        //获取项目信息
        try {
            //获取所有项目
            ArrayList<Project> projects = FileController.ToJava();
            //得到指定的项目信息
            Project project = null;
            for (int i = 0; i < projects.size(); i++) {
                if (projects.get(i).getName().equals(projectName)) {
                    project = projects.get(i);
                    break;
                }
            }
            //上下两个区域

            //票数区域 - 按钮区域

            VBox mainBox = new VBox(50);
            mainBox.setAlignment(Pos.CENTER);

            Label title = new Label("There are " + project.getSize() + " team member.");
            mainBox.getChildren().add(title);

//            // //读取所有票的信息，如果没有票的信息，申请添加票在进行操作
//            ArrayList<Votes> votesList = FileController.VotesToJava();
//            if (votesList==null||votesList.size()==0){
//                Alert alert = new Alert(Alert.AlertType.INFORMATION,"你还没有添加票据，是否需要立即添加",
//                        new ButtonType("是",ButtonBar.ButtonData.YES),
//                        new ButtonType("否",ButtonBar.ButtonData.NO));
//                Optional<ButtonType> buttonType = alert.showAndWait();
//                //点击确定后做的事
//                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
//                    //进入添加页面
//                    SetVotes.setVotes();
//                    stage.close();
//                }else {
//                    //什么也不做，但是也不能添加票
//                }
//            }else {
//                //首先移除所有票的信息然后再初始化票信息
//                for (int j = 0; j < project.getSons().size(); j++) {
//                    for (int k = 0; k < votesList.size(); k++) {
//                        try {
//                            project.getSons().get(j).getVotes().remove(k);
//                        }catch (Exception e){
//
//                        }
//                        project.getSons().get(j).addVotes(votesList.get(k));
//                    }
//                }
//            }

            VBox votesBox = new VBox(10);
            System.out.println("project size " + project.getSize());
            for (int i = 0; i < project.getSize(); i++) {
                votesBox.getChildren().add(VotesInfo.createVotes(project.getSons().get(i).getText(),project));
            }

            VBox spBox = new VBox();
            spBox.setAlignment(Pos.CENTER);

            //滑动条
            ScrollPane sp = new ScrollPane();
            sp.setPrefViewportWidth(50);
            sp.setVmax(440);
            sp.setPrefSize(600, 200);
            sp.setContent(votesBox);

            spBox.getChildren().add(sp);
            mainBox.getChildren().add(spBox);

            //按钮

            VBox buttonBox = new VBox(10);
            buttonBox.setAlignment(Pos.CENTER);
            Button save = new Button("Save Votes");
            save.setDefaultButton(true);
            save.setMinWidth(200);

            Project finalProject = project;
            BooleanBinding isTrue = new BooleanBinding() {

                {

                    for (int i = 0; i < votesBox.getChildren().size(); i++) {
                        int gridPaneSize = ((GridPane) ((VBox) votesBox.getChildren().get(i)).getChildren().get(1)).getChildren().size();
                        //需要绑定每一个输入框
                        for (int j = 0; j < gridPaneSize; j = j + 3) {
                            super.bind(((TextField) ((GridPane) ((VBox) votesBox
                                    .getChildren().get(i))
                                    .getChildren().get(1))
                                    .getChildren().get(1 + j)).textProperty()
                            );
                        }
                    }

                    //                        System.out.println("vbox size " + votesBox.getChildren().size());
                    //因为为三种票 一种票需要三个id来填充  所以共9个格子
//                        System.out.println("grpane size " +    ((GridPane)((VBox) votesBox.getChildren().get(i)).getChildren().get(1)).getChildren().size());
                }

                @Override
                protected boolean computeValue() {

                    boolean flag = false;

                    try {
                        for (int i = 0; i < votesBox.getChildren().size(); i++) {
                            int gridPaneSize = ((GridPane) ((VBox) votesBox.getChildren().get(i)).getChildren().get(1)).getChildren().size();

                            int temp = 0;
                            int votes = 0;
                            //需要检验每一个输入框
                            for (int j = 0; j < gridPaneSize; j = j + 3) {
                                try {
                                    temp += Integer.parseInt(((TextField) ((GridPane) ((VBox) votesBox
                                            .getChildren().get(i))
                                            .getChildren().get(1))
                                            .getChildren().get(1 + j)).getText());

                                    //设置这个票对应的这个人的票数 并存储
                                    finalProject.getSons().get(i).getVotes().get(votes).setNumber(Integer.parseInt(((TextField) ((GridPane) ((VBox) votesBox
                                            .getChildren().get(i))
                                            .getChildren().get(1))
                                            .getChildren().get(1 + j)).getText()));
                                    votes++;


                                    ((Label) ((GridPane) ((VBox) votesBox
                                            .getChildren().get(i))
                                            .getChildren().get(1))
                                            .getChildren().get(j + 2)).setText("");
                                } catch (NumberFormatException e) {
                                    System.out.println(e + " " + (j + 2));
                                    ((Label) ((GridPane) ((VBox) votesBox
                                            .getChildren().get(i))
                                            .getChildren().get(1))
                                            .getChildren().get(j + 2)).setText("票数请输入数字，且和为100");
                                } catch (IndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }

                            }

                            System.out.println("temp = " + temp);
                            if (temp != 100) {
                                ((Label) ((GridPane) ((VBox) votesBox
                                        .getChildren().get(i))
                                        .getChildren().get(1))
                                        .getChildren().get(2)).setText("请检查你的票的数量，请让他们之和为100");
                                flag = true;
                            }

                            if (temp == 100) {
                                ((Label) ((GridPane) ((VBox) votesBox
                                        .getChildren().get(i))
                                        .getChildren().get(1))
                                        .getChildren().get(2)).setText("");
                                flag = false;
                            }
                        }
                    }catch (Exception e){
                        flag = true;
                        System.out.println("发生错误 "  + e);
                    }

                    return flag;
                }
            };

            save.disableProperty().bind(isTrue);
            save.setOnAction(event -> {
                //读取文件中的所有项目并得到想要的这个项目并更新

                for (int i = 0; i < projects.size(); i++) {
                    if (projects.get(i).getName().equals(projectName)) {
                        projects.remove(i);
                        projects.add(finalProject);
                        break;
                    }
                }

                //存入文档
                try {
                    FileController.toTxt(projects);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"保存成功");
                    alert.show();
                    new Controller().start(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                System.out.println("in");
            });

            Button menu = new Button("Main Menu");
            menu.setDefaultButton(true);
            menu.setMinWidth(200);
            menu.setOnAction(event -> {
                try {
                    new Controller().start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            buttonBox.getChildren().add(save);
            buttonBox.getChildren().add(menu);

            mainBox.getChildren().add(buttonBox);


            Scene scene = new Scene(mainBox, 800, 500);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
