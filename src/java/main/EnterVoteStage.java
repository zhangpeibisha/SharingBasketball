package main;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.entity.Project;
import main.entity.Votes;
import main.file.FileController;
import main.page.AddVotes;
import main.page.SetVotes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


public class EnterVoteStage {
	
    public EnterVoteStage(){
        Stage EnterVotePage = new Stage();// Stage is the window.

        VBox mainBox = new VBox(40);
         mainBox.setAlignment(Pos.CENTER);

        Label title = new Label("题目");
        title.setAlignment(Pos.CENTER);
        mainBox.getChildren().add(title);

        //输入
        HBox projectName = new HBox(20);
        projectName.setAlignment(Pos.CENTER);

        Label name = new Label("Enter Project Name: ");
        name.setMaxWidth(200);
        TextField inputName = new TextField();
        inputName.setMaxWidth(400);
        Label isText = new Label("");
        isText.setMinWidth(150);

        projectName.getChildren().addAll(name,inputName,isText);

        mainBox.getChildren().addAll(projectName);


        //按钮
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button nextButton = new Button("Next");
        nextButton.setMinWidth(200);
        nextButton.setDefaultButton(true);

        BooleanBinding isTrue = new BooleanBinding() {
            {
                super.bind(inputName.textProperty());
            }
            @Override
            protected boolean computeValue() {
                //判断输入合法
                try {
                    Integer.parseInt(inputName.getText());
                    isText.setText("项目名不为数字，请输入正确的项目名");
                    return true;
                }catch (Exception e){
                    isText.setText("");
                }
                //查找是否有这个项目
                boolean flag = true;
                try {
                    ArrayList<Project> projects = FileController.ToJava();

                    if (projects!=null){
                        for (int i = 0; i <projects.size(); i++) {
                            System.out.println("projects " + i + " " + projects.get(i).getName());
                            if (projects.get(i).getName().equals(inputName.getText())){
                                flag =  false;
                                break;
                            }
                        }
                        if (flag){
                            isText.setText("没有找到这个项目，请重新输入");
                        }else{
                            isText.setText("");
                        }
                    }else {
                        isText.setText("当前项目为空，请先创建项目");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return flag;
            }
        };

        nextButton.disableProperty().bind(isTrue);
        nextButton.setOnAction(event -> {
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
//                    EnterVotePage.close();
//                }else {
//                    //什么也不做
//                }
//            }else {
//
//            }
            AddVotes.votes(inputName.getText());
            EnterVotePage.close();
        });


//        Button addVotes = new Button("票的操作");
//        addVotes.setDefaultButton(true);
//        addVotes.setPrefWidth(200);
//
//        addVotes.setOnAction(event -> {
//            SetVotes.setVotes();
//            EnterVotePage.close();
//        });

        Button menu = new Button("Main Menu");
        menu.setMinWidth(200);
        menu.setDefaultButton(true);
        menu.setOnAction(event -> {
            try {
                new Controller().start(EnterVotePage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        buttons.getChildren().addAll(nextButton,menu);

        mainBox.getChildren().addAll(buttons);


        Scene EnterScene = new Scene(mainBox, 800, 500);// Set the size of pane
        EnterVotePage.setScene(EnterScene);
        EnterVotePage.show();
	}
}