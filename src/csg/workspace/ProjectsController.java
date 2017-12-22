/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CSGApp;
import csg.CSGProp;
import csg.data.CSGData;
import csg.data.Student;
import csg.data.Team;
import csg.transactions.AddStudent_Transaction;
import csg.transactions.AddTeam_Transaction;
import csg.transactions.EditStudent_Transaction;
import csg.transactions.EditTeam_Transaction;
import csg.transactions.RemoveStudent_Transaction;
import csg.transactions.RemoveTeam_Transaction;
import djf.controller.AppFileController;
import djf.ui.AppGUI;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.util.ArrayList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jtps.jTPS_Projects;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class ProjectsController {
    CSGApp app;
    
    public ProjectsController(CSGApp initApp){
        app = initApp;
    }
    
    public void handleAddTeams(TextField teamNameField, TextField teamColorTextField, TextField textColorTextField, TextField teamLink, Circle teamColorCircle, Circle textColorCircle){
            CSGData data = (CSGData)app.getDataComponent();
            CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            AppGUI gui = app.getGUI();
            AppFileController fileController = gui.getAppFileController();
            
            
            String teamName = teamNameField.getText();
            String teamColorValue = teamColorTextField.getText();
            String textColorValue = textColorTextField.getText();

            String teamLinkString = teamLink.getText();
            
            if(teamName == null || teamName.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_TEAM_TITLE), props.getProperty(CSGProp.INVALID_TEAM_MESSAGE));
            }
            
            else if(teamColorValue == null || teamColorValue.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_COLOR_TITLE), props.getProperty(CSGProp.INVALID_COLOR_MESSAGE)); 
            }
            
            else if(textColorValue == null || textColorValue.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_COLOR_TITLE), props.getProperty(CSGProp.INVALID_COLOR_MESSAGE));
            }
            
            else if(teamLinkString == null || teamLinkString.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_LINK_TITLE), props.getProperty(CSGProp.INVALID_LINK_MESSAGE));
            }
            
            else{
                //IF IT CONTAINS THE TEAM NAME ALREADY DONT ADD A DUPLICATE.
                    if(data.getProjectData().containsTeam(teamName)){
                        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                        dialog.show(props.getProperty(CSGProp.INVALID_TEAM_TITLE), props.getProperty(CSGProp.INVALID_TEAM_DUPLICATE_MESSAGE));
                    }
                    else{
                                    
                        if(teamColorValue != null && textColorValue != null){
                            teamColorCircle.setFill(Color.web(teamColorValue));
                            textColorCircle.setFill(Color.web(textColorValue));
                        }
            
                        Team team = new Team(teamName, teamColorValue, textColorValue, teamLinkString);
                        data.getProjectData().addTeam(team);

                        fileController.markAsEdited(gui);

                        teamNameField.setText("");
//                        teamColorTextField.setText("#ffffff");
//                        textColorTextField.setText("#ffffff");
                        teamLink.setText("");

                        teamNameField.requestFocus();
                        //ADD AS JTPS TRANSACTION BELOW HERE FOR THIS TAB ONLY. JTPS TRANSACTION IS ONLY SPECIFIC FOR THIS TAB.
                        jTPS_Projects transactionComponent = workspace.getProjectsComponent();
                        AddTeam_Transaction transaction = new AddTeam_Transaction(app, team);
                        
                        transactionComponent.addTransaction(transaction);
                    }
                
            }
    }
    
    public void handleEditTeams(TableView teamsTable, TextField teamNameField, TextField teamColorTextField, TextField textColorTextField, TextField teamLink, Circle teamColorCircle, Circle textColorCircle){
            CSGData data = (CSGData)app.getDataComponent();
            CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            AppGUI gui = app.getGUI();
            AppFileController fileController = gui.getAppFileController();
            Team oldTeam = (Team)teamsTable.getSelectionModel().getSelectedItem();
            
            String teamName = teamNameField.getText();
            String teamColorValue = teamColorTextField.getText();
            String textColorValue = textColorTextField.getText();

            
            String teamLinkString = teamLink.getText();
            if(oldTeam != null){
                if(teamName == null || teamName.trim().isEmpty()){
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(CSGProp.INVALID_TEAM_TITLE), props.getProperty(CSGProp.INVALID_TEAM_MESSAGE)); 
                }

                else if(teamColorValue == null || teamColorValue.trim().isEmpty()){
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(CSGProp.INVALID_COLOR_TITLE), props.getProperty(CSGProp.INVALID_COLOR_MESSAGE));
                }

                else if(textColorValue == null || textColorValue.trim().isEmpty()){
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(CSGProp.INVALID_COLOR_TITLE), props.getProperty(CSGProp.INVALID_COLOR_MESSAGE));
                }

                else if(teamLinkString == null || teamLinkString.trim().isEmpty()){
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(CSGProp.INVALID_LINK_TITLE), props.getProperty(CSGProp.INVALID_LINK_MESSAGE));
                }

                else{
                    //WARNING TO THE USER. REMOVING A TEAM WOULD REMOVE ALL STUDENTS ASSOCIATED WITH THAT TEAM ARE YOU SURE YOU WANT TO DO THIS?

                    AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
                    yesNoDialog.show(props.getProperty(CSGProp.EDIT_TEAM_WARNING_TITLE), props.getProperty(CSGProp.EDIT_TEAM_WARNING_MESSAGE));

                    // AND NOW GET THE USER'S SELECTION
                    String selection = yesNoDialog.getSelection();

                    // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
                    if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {

                        int i = 0;
                        ArrayList<Student> studentsToKeep = new ArrayList();
                        while(i < data.getProjectData().getStudents().size()){
                            if(data.getProjectData().getStudents().get(i).getTeam().equals(oldTeam.getTeamName())){
                                studentsToKeep.add(data.getProjectData().getStudents().get(i));
                                data.getProjectData().getStudents().get(i).setTeam(teamName);
                                i = 0; //after removing, the list is reordered so it needs to be researched. Loop will only exit when the ENTIRE list is empty of students in that team to be removed.
                            }
                            else{
                                i++;
                            }
                        }
                        
                        if(teamColorValue != null && textColorValue != null){
                            teamColorCircle.setFill(Color.web(teamColorValue));
                            textColorCircle.setFill(Color.web(textColorValue));
                        }

                        Team newTeam = new Team(teamName, teamColorValue, textColorValue, teamLinkString);

                        data.getProjectData().editTeam(oldTeam, newTeam);

                        fileController.markAsEdited(gui);

    //                    teamNameField.setText("");
    //                    teamColorTextField.setText("#ffffff");
    //                    textColorTextField.setText("#ffffff");
    //                    teamLink.setText("");

                        teamNameField.requestFocus();
                        fileController.markAsEdited(gui);
                        //ADD AS JTPS TRANSACTION BELOW HERE FOR THIS TAB ONLY. JTPS TRANSACTION IS ONLY SPECIFIC FOR THIS TAB.
                        jTPS_Projects transactionComponent = workspace.getProjectsComponent();
                        EditTeam_Transaction transaction = new EditTeam_Transaction(app, oldTeam, newTeam, studentsToKeep);
                        
                        transactionComponent.addTransaction(transaction);

                    }
                   
                }
            }
    }
    
    public void handleRemoveTeams(TableView teamsTable, TextField teamNameField, TextField teamColorTextField, TextField textColorTextField, TextField teamLink, Circle teamColorCircle, Circle textColorCircle){
          CSGData data = (CSGData)app.getDataComponent();
          CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
          PropertiesManager props = PropertiesManager.getPropertiesManager();
          AppGUI gui = app.getGUI();
          AppFileController fileController = gui.getAppFileController();
          Team team = (Team)teamsTable.getSelectionModel().getSelectedItem();
          
          
          //WARNING TO THE USER. REMOVING A TEAM WOULD REMOVE ALL STUDENTS ASSOCIATED WITH THAT TEAM ARE YOU SURE YOU WANT TO DO THIS?
          
	  AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
          yesNoDialog.show(props.getProperty(CSGProp.TEAM_REMOVE_WARNING_TITLE), props.getProperty(CSGProp.TEAM_REMOVE_WARNING_MESSAGE));
        
          // AND NOW GET THE USER'S SELECTION
          String selection = yesNoDialog.getSelection();

          // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
          if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {

            data.getProjectData().removeTeam(team);
            int i = 0;
            ArrayList<Student> studentsToRemove = new ArrayList();
            while(i < data.getProjectData().getStudents().size()){
                if(data.getProjectData().getStudents().get(i).getTeam().equals(team.getTeamName())){
                    studentsToRemove.add(data.getProjectData().getStudents().get(i));
                    data.getProjectData().removeStudent(data.getProjectData().getStudents().get(i));
                    i = 0; //after removing, the list is reordered so it needs to be researched. Loop will only exit when the ENTIRE list is empty of students in that team to be removed.
                }
                else{
                    i++;
                }
            }
            fileController.markAsEdited(gui);
            //ADD AS JTPS TRANSACTION BELOW HERE FOR THIS TAB ONLY. JTPS TRANSACTION IS ONLY SPECIFIC FOR THIS TAB.
            jTPS_Projects transactionComponent = workspace.getProjectsComponent();
            RemoveTeam_Transaction transaction = new RemoveTeam_Transaction(app, team, studentsToRemove);           
            transactionComponent.addTransaction(transaction);
          }
    }
    
    public void handleAddStudents(TextField studentFirstNameField, TextField studentLastNameField, ChoiceBox studentTeam, TextField studentRoleField){
        CSGData data = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        String firstName = studentFirstNameField.getText();
        String lastName = studentLastNameField.getText();
        String studentTeamString = (String) studentTeam.getSelectionModel().getSelectedItem();
        String role = studentRoleField.getText();
        
         if(firstName == null || firstName.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_FIRSTNAME_TITLE), props.getProperty(CSGProp.INVALID_FIRSTNAME_MESSAGE));
            }
            
            else if(lastName == null || lastName.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_LASTNAME_TITLE), props.getProperty(CSGProp.INVALID_LASTNAME_MESSAGE));
            }
            
            else if(studentTeamString == null || studentTeamString.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_STUDENTTEAM_TITLE), props.getProperty(CSGProp.INVALID_STUDENTTEAM_MESSAGE)); 
            }
            
            else if(role == null || role.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_ROLE_TITLE), props.getProperty(CSGProp.INVALID_ROLE_MESSAGE));
            }
            
            else{

                Student student = new Student(firstName, lastName, studentTeamString, role);

                data.getProjectData().addStudent(student);

                fileController.markAsEdited(gui);
                
                studentFirstNameField.setText("");
                studentLastNameField.setText("");
                studentTeam.getSelectionModel().clearSelection();
                studentRoleField.setText("");

                studentFirstNameField.requestFocus();
                //ADD AS JTPS TRANSACTION BELOW HERE FOR THIS TAB ONLY. JTPS TRANSACTION IS ONLY SPECIFIC FOR THIS TAB.
                jTPS_Projects transactionComponent = workspace.getProjectsComponent();
                AddStudent_Transaction transaction = new AddStudent_Transaction(app, student);           
                transactionComponent.addTransaction(transaction);
            }
    }
    
    public void handleEditStudent(TableView studentsTable, TextField studentFirstNameField, TextField studentLastNameField, ChoiceBox studentTeams, TextField studentRoleField){
        CSGData data = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        Student oldStudent = (Student)studentsTable.getSelectionModel().getSelectedItem();
            
        String newFirstName = studentFirstNameField.getText();
        String newLastName = studentLastNameField.getText();
        String newStudentTeam = (String) studentTeams.getSelectionModel().getSelectedItem();

        String newStudentRole = studentRoleField.getText();
            
        if(newFirstName == null || newFirstName.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_FIRSTNAME_TITLE), props.getProperty(CSGProp.INVALID_FIRSTNAME_MESSAGE)); 
            }
            
            else if(newLastName == null || newLastName.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_LASTNAME_TITLE), props.getProperty(CSGProp.INVALID_LASTNAME_MESSAGE)); 
            }
            
            else if(newStudentTeam == null || newStudentTeam.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_STUDENTTEAM_TITLE), props.getProperty(CSGProp.INVALID_STUDENTTEAM_MESSAGE)); 
            }
            
            else if(newStudentRole == null || newStudentRole.trim().isEmpty()){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_ROLE_TITLE), props.getProperty(CSGProp.INVALID_ROLE_MESSAGE)); 
            }
            
            else{

                Student newStudent = new Student(newFirstName, newLastName, newStudentTeam, newStudentRole);

                data.getProjectData().editStudent(oldStudent, newStudent);

                fileController.markAsEdited(gui);
                
//                studentFirstNameField.setText("");
//                studentLastNameField.setText("");
//                studentTeams.getSelectionModel().clearSelection();
//                studentRoleField.setText("");

                studentFirstNameField.requestFocus();
                //ADD AS JTPS TRANSACTION BELOW HERE FOR THIS TAB ONLY. JTPS TRANSACTION IS ONLY SPECIFIC FOR THIS TAB.
                jTPS_Projects transactionComponent = workspace.getProjectsComponent();
                EditStudent_Transaction transaction = new EditStudent_Transaction(app, oldStudent, newStudent);           
                transactionComponent.addTransaction(transaction);
            }
    }
    
    public void handleRemoveStudent(TableView studentsTable, TextField studentFirstName, TextField studentLastName, ChoiceBox studentTeam, TextField studentRole){
        CSGData data = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        Student student = (Student)studentsTable.getSelectionModel().getSelectedItem();
          
        data.getProjectData().removeStudent(student);
        
//        studentFirstName.setText("");
//        studentLastName.setText("");
//        studentTeam.getSelectionModel().clearSelection();
//        studentRole.setText("");
        
        fileController.markAsEdited(gui);
        //ADD AS JTPS TRANSACTION BELOW HERE FOR THIS TAB ONLY. JTPS TRANSACTION IS ONLY SPECIFIC FOR THIS TAB.
        jTPS_Projects transactionComponent = workspace.getProjectsComponent();
        RemoveStudent_Transaction transaction = new RemoveStudent_Transaction(app, student);           
        transactionComponent.addTransaction(transaction);
    }
}
