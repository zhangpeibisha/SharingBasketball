package main;//           _________________________________________________
//          |   COMP210P Final Project - "Split-It"           |
//          |   Deliverables 1 -- Submitted: 7th Feb 2018     |
//          |   COMP210P Final Project - "Split-It"           |
//          |    By LOUIS HEERY and ZIMING HE                 |
//          |                                                 |
//          |   Split-It is an Java application that lets     |
//          |   you allocate the marks which each team member |
//          |   should be awarded for a project!              |
//          |                                                 |
//          |   Controller.java - by Louis Heery              |
//          |_________________________________________________|

// Import relavent libraries.

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.page.CreateProject;
import main.page.SetVotes;

public class Controller extends Application {

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Split-It");

        // Create the registration form grid pane
        GridPane menuPageGrid = layoutSetup.createFrameLayout();

        Scene menuScene = new Scene(menuPageGrid, 800, 500); // set the size of the window

        // Add Header Text & Set its position
        Label menuHeader = new Label("Welcome to Split-It");
        menuHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        menuPageGrid.add(menuHeader, 0,0,2,1);
        GridPane.setHalignment(menuHeader, HPos.CENTER);
        GridPane.setMargin(menuHeader, new Insets(20, 0,20,0));

        // Add Subtitle Text & Set its position
        Label menuSubtitle = new Label("Please choose an option:");
        menuSubtitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        menuPageGrid.add(menuSubtitle, 0,2,2,1);
        GridPane.setHalignment(menuSubtitle, HPos.CENTER);
        GridPane.setMargin(menuSubtitle, new Insets(20,0,20,0));

        // Add About Button & Set its position & Set its action on click
        Button aboutButton = new Button("About");
        aboutButton.setPrefHeight(40);
        aboutButton.setDefaultButton(true);
        aboutButton.setPrefWidth(200);
        menuPageGrid.add(aboutButton, 0, 5, 2, 1);
        GridPane.setHalignment(aboutButton, HPos.CENTER);
        GridPane.setMargin(aboutButton, new Insets(10, 0,10,0));
        aboutButton.setOnAction(eve -> new aboutStage()); // On Click -> Open the 'About' Stage

        // Add Create Project Button & Set its position & Set its action on click
        Button createButton = new Button("Create Project");
        createButton.setPrefHeight(40);
        createButton.setDefaultButton(true);
        createButton.setPrefWidth(200);
        createButton.fire();
        menuPageGrid.add(createButton, 0, 6, 2, 1);
        GridPane.setHalignment(createButton, HPos.CENTER);
        GridPane.setMargin(createButton, new Insets(10, 0, 10,0));
        createButton.setOnAction(eve ->
        {
            CreateProject.createProjectStage();
            primaryStage.close();
        }); // On Click -> Open the 'Create Project' Stage

        // Add Enter Votes Button & Set its position & Set its action on click
        Button votesButton = new Button("Enter Votes");
        votesButton.setPrefHeight(40);
        votesButton.setDefaultButton(true);
        votesButton.setPrefWidth(200);
        menuPageGrid.add(votesButton, 0, 7, 2, 1);
        GridPane.setHalignment(votesButton, HPos.CENTER);
        GridPane.setMargin(votesButton, new Insets(10, 0, 10,0));
        votesButton.setOnAction(eve -> {
            new EnterVoteStage();
            primaryStage.close();
        }); // On Click -> Open the 'Enter Vote' Stage

        // Add Show Project Button & Set its position & Set its action on click
        Button showprojectButton = new Button("Show Project");
        showprojectButton.setPrefHeight(40);
        showprojectButton.setDefaultButton(true);
        showprojectButton.setPrefWidth(200);
        menuPageGrid.add(showprojectButton, 0, 8, 2, 1);
        GridPane.setHalignment(showprojectButton, HPos.CENTER);
        GridPane.setMargin(showprojectButton, new Insets(10, 0, 10,0));
        // showprojectButton.setOnAction(eve-> new createprojectStage()); // On Click -> Open the 'Create Project' Stage

        // Add Quit Button
        Button quitButton = new Button("Quit");
        quitButton.setPrefHeight(40);
        quitButton.setDefaultButton(true);
        quitButton.setPrefWidth(200);
        menuPageGrid.add(quitButton, 0, 9, 2, 1);
        GridPane.setHalignment(quitButton, HPos.CENTER);
        GridPane.setMargin(quitButton, new Insets(10, 0,10,0));//////////////////////////////////////////////////////////////change a bit here 

        //暂时用来添加票
        quitButton.setOnAction(eve -> System.exit(0));//On Click -> exit







	    primaryStage.setOnCloseRequest(eve -> Platform.exit());//////////////////////////////////////////new thing here, when clicking cross in window, it will terminate all platforms.
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Split-It"); // Set title of the window.
        primaryStage.show();
    }
}
