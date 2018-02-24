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
//          |   createprojectStage.java - by Louis Heery      |
//          |_________________________________________________|

// Import relavent libraries.

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;


public class createprojectStage {

    createprojectStage() {
        Stage createprojectPage = new Stage();
        createprojectPage.setTitle("Split-It - Create Project"); // Set title of the window.

        // Create the registration form grid pane
        GridPane createprojectPageGrid = layoutSetup.createFrameLayout(); // create new instance of 'createFrameLayout'
        createprojectPageGrid.setAlignment(Pos.CENTER); // align window in center of computer display
        Scene createprojectScene = new Scene(createprojectPageGrid, 800, 500); // set the size of the window

        // Add Header Text
        Label createpageHeader = new Label("Create Project");
        createpageHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        createprojectPageGrid.add(createpageHeader, 0, 0, 2, 1);
        createprojectPageGrid.setHalignment(createpageHeader, HPos.CENTER);
        createprojectPageGrid.setMargin(createpageHeader, new Insets(20, 0, 20, 0));

        ////// CREATE PROJECT SECTION //////
        // Add Subtitle Text
        Label createpageSubtitle = new Label("Please enter the project details below.\n\t\tThen Click 'Next'.");
        createpageSubtitle.setFont(Font.font("Arial", 12));
        createprojectPageGrid.add(createpageSubtitle, 0, 2, 2, 1);
        GridPane.setHalignment(createpageSubtitle, HPos.CENTER);
        GridPane.setMargin(createpageSubtitle, new Insets(20, 0, 20, 0));

        // Add 'Name of Project' text & set its location
        Label createpageTextbox = new Label("Project Name:");
        TextField projectField = new TextField();
        createprojectPageGrid.add(createpageTextbox, 0, 3, 1, 1);
        createpageTextbox.setPrefWidth(400);
        projectField.setMaxWidth(400);
        createprojectPageGrid.add(projectField, 1, 3, 1, 1);
        GridPane.setHalignment(projectField, HPos.RIGHT);

        // Add About Button & Set its position & Set its action on click
        Button nextButton = new Button("Next");
        nextButton.setPrefHeight(40);
        nextButton.setDefaultButton(true);
        nextButton.setPrefWidth(200);
        createprojectPageGrid.add(nextButton, 0, 4, 2, 1);
        GridPane.setHalignment(nextButton, HPos.CENTER);
        GridPane.setMargin(nextButton, new Insets(10, 0,10,0));
        projectField.setOnAction(eve-> {});

			// Block the user from clicking the "Next" button until they enter the name of the project.
        BooleanBinding isProjectFieldBlank = new BooleanBinding() {
          {
            super.bind(projectField.textProperty());
          }

          @Override
          protected boolean computeValue() {return (projectField.getText().isEmpty());}

        };

//        nextButton.disableProperty().bind(isProjectFieldBlank);
        nextButton.setOnAction(eve-> { // When User clicks enter in project field -> Run this:
		
            String Projectname = projectField.getText();

            createpageSubtitle.setText("Please enter the participants' names seperated by commas.\n\t\tEXAMPLE:   Jeff, Jill, John\n\n\t\t\tThen click 'Save Project.'");

            createprojectPageGrid.getChildren().remove(nextButton);
            createprojectPageGrid.getChildren().remove(createpageTextbox);
            createprojectPageGrid.getChildren().remove(projectField);
            TextField participantnameField = new TextField();

            Label createpageTextbox2 = new Label("Name of Participants");
            //  TextField participantnameField = new TextField ();
            createprojectPageGrid.add(createpageTextbox2, 0, 4, 1, 1);
            GridPane.setHalignment(createpageTextbox2, HPos.LEFT);
            createpageTextbox2.setMinWidth(400);
            participantnameField.setMaxWidth(400);
            createprojectPageGrid.add(participantnameField, 1, 4, 1, 1);
            GridPane.setHalignment(participantnameField, HPos.RIGHT);
            participantnameField.requestFocus();
			
			/*ScrollBar ScrollBarY = new ScrollBar();
			ScrollBarY.setMin(0);
            ScrollBarY.setMax(Double.MAX_VALUE);
            ScrollBarY.setValue(Double.MAX_VALUE);
			ScrollBarY.setOrientation(Orientation.VERTICAL);
			ScrollBarY.setTranslateY(50);	
			createprojectPageGrid.getChildren().add(ScrollBarY);
			
			ScrollBarY.valueProperty().addListener(event->{
				createprojectPageGrid.setTranslateY(20 + ScrollBarY.getValue());
			});*/
        
           
            // Add Save Button
            Button saveButton = new Button("Save Project");
            saveButton.setPrefHeight(40);
            saveButton.setDefaultButton(true);
            saveButton.setPrefWidth(200);
            createprojectPageGrid.add(saveButton, 0, 5, 2, 1);
            GridPane.setHalignment(saveButton, HPos.CENTER);
            GridPane.setMargin(saveButton, new Insets(10, 0, 10, 0));

            participantnameField.setOnAction(eve10 -> {});

            // Block the user from clicking the "Save Project" button until they enter the names of 1 or more participants.
            BooleanBinding isParticipantNameFieldBlank = new BooleanBinding() {
              {
                super.bind(participantnameField.textProperty());
              }

              @Override
              protected boolean computeValue() {return (participantnameField.getText().isEmpty());}

            };

            saveButton.disableProperty().bind(isParticipantNameFieldBlank);

            saveButton.setOnAction(eveSAVE -> {

                String personNamesALL = participantnameField.getText();

                createprojectPageGrid.getChildren().remove(createpageTextbox2);
                createpageHeader.setText("Project Created");
                createpageSubtitle.setText("Please return to main menu.");
                createprojectPageGrid.getChildren().remove(participantnameField);
                createprojectPageGrid.getChildren().remove(saveButton);

                personNamesALL = personNamesALL.replaceAll(" +", " ");
                personNamesALL = personNamesALL.replaceAll(" ,", ",");
                personNamesALL = personNamesALL.replaceAll(", ", ",");

                String[] items = personNamesALL.split(",");
                List <String> personNamesArray = Arrays.asList(items);

            });

        });

        // Add Main Menu Button
        Button aboutButton = new Button("Main Menu");
        aboutButton.setPrefHeight(40);
        aboutButton.setDefaultButton(true);
        aboutButton.setPrefWidth(200);
        createprojectPageGrid.add(aboutButton, 0, 6, 2, 1);
        GridPane.setHalignment(aboutButton, HPos.CENTER);
        GridPane.setMargin(aboutButton, new Insets(10, 0, 10, 0));
        aboutButton.setOnAction(eve -> createprojectPage.hide());

	    createprojectPage.setOnCloseRequest(eve -> Platform.exit());//////////////////////////new thing here, when clicking cross in window, it will terminate all platforms.
        createprojectPage.setScene(createprojectScene);
        createprojectPage.show();
		
    }

}
