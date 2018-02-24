package main.page;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.EnterVoteStage;
import main.entity.Votes;
import main.file.FileController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Create by zhangpe0312@qq.com on 2018/2/18.
 *
 * 这个类不适用，我保留下来你可以看下，如果不需要就删了呗
 */
public class SetVotes {

    //票设置完成后，进入enter votes
    public static void setVotes() {
        Stage stage = new Stage();

        //两个部分，一个部分为添加框，一个部分为展示框
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);

        Label title = new Label("设置票的标题");
        title.setMinWidth(200);
        title.setAlignment(Pos.CENTER);

        mainBox.getChildren().add(title);


        //添加
        HBox addVotes = new HBox(10);
        addVotes.setAlignment(Pos.CENTER);

        //必须为文本才能保存
        TextField addText = new TextField();
        addText.setMinWidth(200);

        Button button = new Button("save votes");
        button.setMinWidth(50);
        button.setAlignment(Pos.CENTER);


        Label isText = new Label("");
        isText.setMinWidth(200);
        isText.setAlignment(Pos.CENTER);

        BooleanBinding isSave = new BooleanBinding() {
            {
                super.bind(addText.textProperty());
            }

            @Override
            protected boolean computeValue() {

                try {
                    Integer.parseInt(addText.getText());
                    isText.setText("票的名字必须为字符，不能为纯数字");
                    return true;
                }catch (Exception e){
                    isText.setText("");
                    return false;
                }
            }
        };
        button.disableProperty().bind(isSave);
        button.setOnAction(event -> {
            ArrayList<Votes> votes = FileController.VotesToJava();
            Votes votes1 = new Votes(addText.getText(),0);
            if (votes == null){
                votes = new ArrayList<>();
            }
            votes.add(votes1);
            try {
                FileController.VotestoTxt(votes);
                isText.setText("添加成功");
            } catch (IOException e) {
                button.setText("");
                isText.setText("添加失败");
                e.printStackTrace();
            }

            SetVotes.setVotes();
            stage.close();

        });

        addVotes.getChildren().addAll(addText,button,isText);
        mainBox.getChildren().add(addVotes);


        //获取得到的布局
        GridPane votesPane = setVotesArea(stage);

        //显示历史数据并提供删除功能 需要使用滑动条功能
        ScrollPane sp = new ScrollPane();
        sp.setPrefViewportWidth(50);
        sp.setVmax(440);
        sp.setPrefSize(600, 200);
        sp.setContent(votesPane);
        sp.setId("sp");

        mainBox.getChildren().addAll(sp);

        //添加按钮
        Button back = new Button("Back");
        back.setAlignment(Pos.CENTER);
        back.setMinWidth(50);
        back.setDefaultButton(true);

        back.setOnAction(event -> {
            //返回添加页面
            new EnterVoteStage();
            stage.close();
        });

        mainBox.getChildren().addAll(back);

        Scene scene = new Scene(mainBox,800,500);
        stage.setScene(scene);
        stage.show();
    }

    public static GridPane setVotesArea(Stage stage){

        ArrayList<Votes> hostVotes = FileController.VotesToJava();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.getChildren().addListener((InvalidationListener) c -> {

        });
        if (hostVotes == null){
            return gridPane;
        }

        for (int i = 0; i <hostVotes.size() ; i++) {
            //第一列为票的名字 第二列为操作
            Label name = new Label(hostVotes.get(i).getName());
            name.setMinWidth(200);
            name.setId(i+"_laber");

            Button deleteButton = new Button("删除");
            deleteButton.setMinWidth(50);
            deleteButton.setDefaultButton(true);
            deleteButton.setId(i+"");


            gridPane.add(name,0,i);
            gridPane.add(deleteButton,1,i);

            deleteButton.setOnAction(event -> {
               String id =  deleteButton.getId();
               int VoteID = Integer.parseInt(id);
               hostVotes.remove(VoteID);
                try {
                    FileController.VotestoTxt(hostVotes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //从新进入这个设置页面 刷新内容
                SetVotes.setVotes();
                stage.close();
            });
        }

        return gridPane;
    }
}
