/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CSGApp;
import csg.CSGProp;
import csg.data.CSGData;
import csg.data.Recitation;
import csg.data.Team;
import csg.transactions.AddRecitation_Transaction;
import csg.transactions.EditRecitation_Transaction;
import csg.transactions.RemoveRecitation_Transaction;
import djf.controller.AppFileController;
import djf.ui.AppGUI;
import djf.ui.AppMessageDialogSingleton;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Recitation;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class RecitationController {
    CSGApp app;
    
    public RecitationController(CSGApp initApp){
        app = initApp;
    }
    
    public void handleAddRecitation(TextField sectionTextField, TextField instructorTextField, TextField dayTimeTextField, TextField locationTextField, ChoiceBox supervisingTA1ChoiceBox, ChoiceBox supervisingTA2ChoiceBox){
        CSGData data = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        Recitation recitation;
        
        if(sectionTextField.getText() == null || sectionTextField.getText().trim().isEmpty()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_ENTRY_TITLE), props.getProperty(CSGProp.EMPTY_SECTION_MESSAGE)); 
        }
        else if(instructorTextField.getText() == null || instructorTextField.getText().trim().isEmpty()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_ENTRY_TITLE), props.getProperty(CSGProp.EMPTY_INSTRUCTOR_MESSAGE)); 
        }
        else if(dayTimeTextField.getText() == null || dayTimeTextField.getText().trim().isEmpty()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_ENTRY_TITLE), props.getProperty(CSGProp.EMPTY_DAYTIME_MESSAGE));
        }
        else if(locationTextField.getText() == null || locationTextField.getText().trim().isEmpty()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_ENTRY_TITLE), props.getProperty(CSGProp.EMPTY_LOCATION_MESSAGE)); 
        }
        else{
            String section = sectionTextField.getText();
            String instructor = instructorTextField.getText();
            String dayTime = dayTimeTextField.getText();
            String location = locationTextField.getText();
        
            if((supervisingTA1ChoiceBox.getSelectionModel().getSelectedItem() == null) && (supervisingTA2ChoiceBox.getSelectionModel().getSelectedItem() == null) ){
                String ta1 = "";
                String ta2 = "";
                recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            }
            else if(supervisingTA1ChoiceBox.getSelectionModel().getSelectedItem() == null){
                String ta1 = "";
                String ta2 = supervisingTA2ChoiceBox.getSelectionModel().getSelectedItem().toString();
                recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            }
            else if(supervisingTA2ChoiceBox.getSelectionModel().getSelectedItem() == null){
                String ta1 = supervisingTA1ChoiceBox.getSelectionModel().getSelectedItem().toString();
                String ta2 = "";
                recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            }
            else{
                String ta1 = supervisingTA1ChoiceBox.getSelectionModel().getSelectedItem().toString();
                String ta2 = supervisingTA2ChoiceBox.getSelectionModel().getSelectedItem().toString();
                recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            }
            data.getRecitationData().addRecitation(recitation);

            fileController.markAsEdited(gui);

            sectionTextField.setText("");
            instructorTextField.setText("");
            dayTimeTextField.setText("");
            locationTextField.setText("");
            supervisingTA1ChoiceBox.getSelectionModel().clearSelection();
            supervisingTA2ChoiceBox.getSelectionModel().clearSelection();

            sectionTextField.requestFocus();

            //ADD JTPS TRANSACTION OBJECT
            jTPS_Recitation transactionComponent = workspace.getRecitationComponent();
            AddRecitation_Transaction transaction = new AddRecitation_Transaction(app, recitation);
            transactionComponent.addTransaction(transaction);
        }
    }
    
    public void handleEditRecitation(TableView recitationsTable, TextField sectionTextField, TextField instructorTextField, TextField dayTimeTextField, TextField locationTextField, ChoiceBox supervisingTA1ChoiceBox, ChoiceBox supervisingTA2ChoiceBox){
        CSGData data = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        Recitation oldRecitation = (Recitation) recitationsTable.getSelectionModel().getSelectedItem();

        Recitation recitation;
         
        if(sectionTextField.getText() == null || sectionTextField.getText().trim().isEmpty()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_ENTRY_TITLE), props.getProperty(CSGProp.E_EMPTY_SECTION_MESSAGE));
        }
        else if(instructorTextField.getText() == null || instructorTextField.getText().trim().isEmpty()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_ENTRY_TITLE), props.getProperty(CSGProp.E_EMPTY_INSTRUCTOR_MESSAGE)); 
        }
        else if(dayTimeTextField.getText() == null || dayTimeTextField.getText().trim().isEmpty()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_ENTRY_TITLE), props.getProperty(CSGProp.E_EMPTY_DAYTIME_MESSAGE)); 
        }
        else if(locationTextField.getText() == null || locationTextField.getText().trim().isEmpty()){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_ENTRY_TITLE), props.getProperty(CSGProp.E_EMPTY_LOCATION_MESSAGE)); 
        }
        else{
            String section = sectionTextField.getText();
            String instructor = instructorTextField.getText();
            String dayTime = dayTimeTextField.getText();
            String location = locationTextField.getText();
            if((supervisingTA1ChoiceBox.getSelectionModel().getSelectedItem() == null) && (supervisingTA2ChoiceBox.getSelectionModel().getSelectedItem() == null) ){
                String ta1 = "";
                String ta2 = "";
                recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            }
            else if(supervisingTA1ChoiceBox.getSelectionModel().getSelectedItem() == null){
                String ta1 = "";
                String ta2 = supervisingTA2ChoiceBox.getSelectionModel().getSelectedItem().toString();
                recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            }
            else if(supervisingTA2ChoiceBox.getSelectionModel().getSelectedItem() == null){
                String ta1 = supervisingTA1ChoiceBox.getSelectionModel().getSelectedItem().toString();
                String ta2 = "";
                recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            }
            else{
                String ta1 = supervisingTA1ChoiceBox.getSelectionModel().getSelectedItem().toString();
                String ta2 = supervisingTA2ChoiceBox.getSelectionModel().getSelectedItem().toString();
                recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            }
            data.getRecitationData().editRecitation(oldRecitation, recitation);

            fileController.markAsEdited(gui);

//            sectionTextField.setText("");
//            instructorTextField.setText("");
//            dayTimeTextField.setText("");
//            locationTextField.setText("");
//            supervisingTA1ChoiceBox.getSelectionModel().clearSelection();
//            supervisingTA2ChoiceBox.getSelectionModel().clearSelection();

            sectionTextField.requestFocus();

            //ADD AS JTPS COMPONENT
            jTPS_Recitation transactionComponent = workspace.getRecitationComponent();
            EditRecitation_Transaction transaction = new EditRecitation_Transaction(app, oldRecitation, recitation);
            transactionComponent.addTransaction(transaction);
        }
        
    }
    
    public void handleRemoveRecitation(TableView recitationsTable, TextField sectionTextField, TextField instructorTextField, TextField dayTimeTextField, TextField locationTextField, ChoiceBox supervisingTA1ChoiceBox, ChoiceBox supervisingTA2ChoiceBox){
        CSGData data = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        Recitation recitation = (Recitation) recitationsTable.getSelectionModel().getSelectedItem();
        
        data.getRecitationData().removeRecitation(recitation);
        
        fileController.markAsEdited(gui);
        
//        sectionTextField.setText("");
//        instructorTextField.setText("");
//        dayTimeTextField.setText("");
//        locationTextField.setText("");
//        supervisingTA1ChoiceBox.getSelectionModel().clearSelection();
//        supervisingTA2ChoiceBox.getSelectionModel().clearSelection();
        sectionTextField.requestFocus();
        //ADD AS JTPS COMPONENT
        jTPS_Recitation transactionComponent = workspace.getRecitationComponent();
        RemoveRecitation_Transaction transaction = new RemoveRecitation_Transaction(app, recitation);
        transactionComponent.addTransaction(transaction);
    }
    
}
