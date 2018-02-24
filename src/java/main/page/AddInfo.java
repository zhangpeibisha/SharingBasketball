package main.page;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


/**
 * Create by zhangpe0312@qq.com on 2018/2/16.
 */
public class AddInfo {

    public static HBox setInfo(int id){
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);

        Label pr = new Label("Name of Participants " + id + ": ");
        pr.setMinWidth(160);

        TextField input = new TextField();
        input.setMinWidth(200);

        Label isText = new Label();
        isText.setMinWidth(160);

        hBox.getChildren().addAll(pr,input,isText);
        return hBox;
    }
}
