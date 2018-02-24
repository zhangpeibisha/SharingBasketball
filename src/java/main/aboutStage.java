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
//          |   aboutStage.java - by Ziming He                |
//          |_________________________________________________|

// Import relavent libraries.

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class aboutStage {
    aboutStage() {
        Stage aboutPage = new Stage();
        aboutPage.setTitle("Split-It - About"); // Set title of the window.

        // Create the registration form grid pane
        GridPane aboutPageGrid = layoutSetup.createFrameLayout();
        aboutPageGrid.setAlignment(Pos.CENTER);
        Scene aboutScene = new Scene(aboutPageGrid, 800, 500);

        // Add Header Text field & set its position
        Label aboutHeader = new Label("About Split-It");
        aboutHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        aboutPageGrid.add(aboutHeader, 0, 0, 2, 1);
        aboutPageGrid.setHalignment(aboutHeader, HPos.CENTER);
        aboutPageGrid.setMargin(aboutHeader, new Insets(20, 0, 20, 0));

        // Add About Page Description & set its position
        Label aboutSubtitle = new Label("Split-It is an Java application that lets you allocate the marks which each team member should be awarded for a project!\nThe purpose of the application is to help teams allocate the credit for a project fairly so that all parties are satisfied with\nthe outcome. The idea is inspired by the work of Ariel Procaccia and Jonathan Goldman of Carnegie Mellon University.\n\n\t\t\t\t\t\t\t-- How to Use this Application --\n\n1. CREATE A PROJECT:\t\tEnter project name, number of participants and names of participants.\n\n2. ENTER VOTES:\t\t\t\tEach participant enters a score for himself and his team members on how much work he\n\t\t\t\t\t\t\tthinks all members of the team contributed to the project.\n\n3. VIEW SCORES:\t\t\t\tView the project scores which are automatically calculated.");
        aboutSubtitle.setFont(Font.font("Arial", 12));
        aboutPageGrid.add(aboutSubtitle, 0, 2, 2, 1);
        GridPane.setHalignment(aboutSubtitle, HPos.CENTER);
        GridPane.setMargin(aboutSubtitle, new Insets(20, 0, 20, 0));

        // Add Main Menu Button & set its position & set what happens if button is clicked
        Button aboutButton = new Button("Main Menu");
        aboutButton.setPrefHeight(40);
        aboutButton.setDefaultButton(true);
        aboutButton.setPrefWidth(200);
        aboutPageGrid.add(aboutButton, 0, 5, 2, 1);
        GridPane.setHalignment(aboutButton, HPos.CENTER);
        GridPane.setMargin(aboutButton, new Insets(10, 0, 10, 0));
        aboutButton.setOnAction(eve2 -> aboutPage.hide()); 

		aboutPage.setOnCloseRequest(eve -> Platform.exit());//////////////////////////////////////////new thing here, when clicking cross in window, it will terminate all platforms.
        aboutPage.setScene(aboutScene);
        aboutPage.show();

    }

}
