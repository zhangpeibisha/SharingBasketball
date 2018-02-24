package main.page;


import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Controller;

/**
 * Create by zhangpe0312@qq.com on 2018/2/14.
 */
public class CreateProject {

    public static void createProjectStage(){
        Stage stage = new Stage();

        /**
         * 布局 上中下上个模块 标题 填写内容 按钮
         *
         * 使用Vbox布局
         */

         VBox mainBox = new VBox(50);
         mainBox.setAlignment(Pos.CENTER);

         //题目
        Label title = new Label("Create Project");
        mainBox.getChildren().add(title);

        //项目名字文本框
        Label createpageHeaderName = new Label("Project Name:");
        //项目数量文本框
        Label createpageHeaderNumber = new Label("Number of Participants:");

        //项目名字输入框
        TextField nameInput = new TextField();
        nameInput.setMinSize(400,20);

        //项目数量输入框
        TextField numberInput = new TextField();
        numberInput.setMinSize(400,20);

        //添加提示语言
        Label isText = new Label();
        isText.setMinWidth(200);
        Label isNumber = new Label();
        isNumber.setMinWidth(200);


        GridPane input = new GridPane();
        input.setHgap(10);
        input.setVgap(10);
        input.setAlignment(Pos.CENTER);

        //调试使用，查看网格位子
        input.setGridLinesVisible(false);
        //添加孩子
        input.add(createpageHeaderName,0,0);
        input.add(createpageHeaderNumber,0,1);
        input.add(nameInput,1,0);
        input.add(numberInput,1,1);
        input.add(isText,2,0);
        input.add(isNumber,2,1);
        mainBox.getChildren().add(input);

        //next按钮
        Button nextButton = new Button("Next");
        nextButton.setDefaultButton(true);
        nextButton.setMinWidth(200);

        //绑定事件，如果满足要求才使用这个按钮
        BooleanBinding isTrue = new BooleanBinding() {
            {
                super.bind(nameInput.textProperty());
                super.bind(numberInput.textProperty());
            }
            @Override
            protected boolean computeValue() {
                try {
                    Integer.parseInt(nameInput.getText());
                    isText.setText("请输入文本，项目名字不能为数字");
                    return true;
                }catch (Exception e){
                    if (nameInput.getText().equals("")){
                        isText.setText("项目名称不能为空");
                        return true;
                    }
                    isText.setText("");
                }

                try {
                    Integer.parseInt(numberInput.getText());
                    isNumber.setText("");
                    return false;
                }catch (Exception e){
                    isNumber.setText("项目个数不能为字符，请输入数字");
                    return true;
                }
            }
        };
        nextButton.disableProperty().bind(isTrue);
        nextButton.setOnAction(event -> {
            ProjectInfo.setProjectInfo(nameInput.getText(),Integer.parseInt(numberInput.getText()));
            stage.close();
        });
        //返回主菜单
        Button aboutButton = new Button("Main Menu");
        aboutButton.setDefaultButton(true);
        aboutButton.setMinWidth(200);

        //返回主界面
        aboutButton.setOnAction(event -> {
            try {
                new Controller().start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(nextButton,aboutButton);

        mainBox.getChildren().add(buttonBox);
        //建立舞台
        Scene scene = new Scene(mainBox,800,500);
        stage.setScene(scene);
        stage.show();
    }

}
