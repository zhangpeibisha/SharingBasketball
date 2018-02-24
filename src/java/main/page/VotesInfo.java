package main.page;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.entity.Project;
import main.entity.ProjectSon;
import main.entity.Votes;
import main.file.FileController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by zhangpe0312@qq.com on 2018/2/17.
 */
public class VotesInfo {

    public static VBox createVotes(String sonName, Project project) {

        VBox mainBox = new VBox(20);

        //id = 0
        String str = "Enter " + sonName + "'s votes, points must add up to 100 :";
        Label title = new Label(str);
        title.setMinWidth(200);
        mainBox.getChildren().add(title);

        // id = 1
        GridPane addVotesInfo = new GridPane();
        addVotesInfo.setHgap(10);
        addVotesInfo.setVgap(10);

        ArrayList<ProjectSon> sons = project.getSons();

        int temp = 0;
        for (int i = 0; i < sons.size(); i++) {

            //找出除了这个人的所有名字
            if (!sons.get(i).getText().equals(sonName)) {
                Label text = new Label("Enter " + sonName + "'s points for " + sons.get(i).getText() + " :");
                text.setMinWidth(200);
                TextField input = new TextField("");
                input.setMaxWidth(200);
                Label isTrue = new Label("");
                isTrue.setMinWidth(200);
                addVotesInfo.add(text, 0, temp);
                addVotesInfo.add(input, 1, temp);
                addVotesInfo.add(isTrue, 2, temp);
                temp++;
            }

        }

        mainBox.getChildren().add(addVotesInfo);

        return mainBox;
    }
}
