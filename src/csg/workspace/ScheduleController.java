/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CSGApp;
import csg.CSGProp;
import csg.data.CSGData;
import csg.data.ScheduleData;
import csg.data.ScheduleItem;
import csg.transactions.AddSchedule_Transaction;
import csg.transactions.EditEndSchedule_Transaction;
import csg.transactions.EditSchedule_Transaction;
import csg.transactions.EditStartSchedule_Transaction;
import csg.transactions.RemoveSchedule_Transaction;
import djf.controller.AppFileController;
import djf.ui.AppGUI;
import djf.ui.AppMessageDialogSingleton;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Schedule;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class ScheduleController {
    CSGApp app;
    
    public ScheduleController(CSGApp initApp){
        app = initApp;
    }
    
    
    public void handleAddScheduleItem(ChoiceBox typeChoices, DatePicker currentDay, TextField scheduleTime, TextField scheduleTitle, TextField scheduleTopic, TextField scheduleLink, TextField scheduleCriteria){
        CSGData data = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        if(typeChoices.getSelectionModel().getSelectedItem() == null){
            //THROW A DIALOG TO PROMPT USER TO PICK A SCHEDULE ITEM TYPE
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.ILLEGAL_SCHEDULE_DATA_TITLE), props.getProperty(CSGProp.ILLEGAL_SCHEDULE_DATA_MESSAGE));
            return;
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_HOLIDAY))){ //ITS A HOLIDAY ITEM.
                if(currentDay.getValue() != null && (scheduleTitle.getText() != null || !scheduleTitle.getText().trim().isEmpty()) 
                        && (scheduleLink.getText() != null || !scheduleLink.getText().trim().isEmpty())){
                    LocalDate currentDate = currentDay.getValue();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    String currentDateString = currentDate.format(format);
                    String title = scheduleTitle.getText();
                    String link = scheduleLink.getText();
                    ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, "", title, "", link, "");
                    data.getScheduleData().addScheduleItem(scheduleItem);
                    
                    //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
                    jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                    AddSchedule_Transaction transaction = new AddSchedule_Transaction(app, scheduleItem);
                    transactionComponent.addTransaction(transaction);
                }
                else{
                     //THROW A DIALOG TO PROMPT USER TO PICK A SCHEDULE ITEM TYPE
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(CSGProp.INVALID_SCHEDULE_ENTRY_TITLE), props.getProperty(CSGProp.INVALID_SCHEDULE_ENTRY_MESSAGE));
                }    
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_LECTURE))){ //ITS A LECTURE ITEM.
            LocalDate currentDate = currentDay.getValue();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDateString = currentDate.format(format);
            String title = scheduleTitle.getText();
            String topic = scheduleTopic.getText();
            String link = scheduleLink.getText();
            
            ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, "", title, topic, link, "");
            data.getScheduleData().addScheduleItem(scheduleItem);
            
            //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
            jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
            AddSchedule_Transaction transaction = new AddSchedule_Transaction(app, scheduleItem);
            transactionComponent.addTransaction(transaction);
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_REFERENCE))){ //ITS A REFERENCE ITEM.
            LocalDate currentDate = currentDay.getValue();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDateString = currentDate.format(format);
            String title = scheduleTitle.getText();
            String topic = scheduleTopic.getText();
            String link = scheduleLink.getText();
            
            ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, "", title, topic, link, "");
            data.getScheduleData().addScheduleItem(scheduleItem);
            
            //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
            jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
            AddSchedule_Transaction transaction = new AddSchedule_Transaction(app, scheduleItem);
            transactionComponent.addTransaction(transaction);
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_RECITATION))){ //ITS A RECITATION ITEM.
            LocalDate currentDate = currentDay.getValue();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDateString = currentDate.format(format);
            String title = scheduleTitle.getText();
            String topic = scheduleTopic.getText();
            ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, "", title, topic, "", "");
            data.getScheduleData().addScheduleItem(scheduleItem);
            
            //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
            jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
            AddSchedule_Transaction transaction = new AddSchedule_Transaction(app, scheduleItem);
            transactionComponent.addTransaction(transaction);
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_HW))){ //ITS A HOMEWORK ITEM.
            LocalDate currentDate = currentDay.getValue();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDateString = currentDate.format(format);
            String title = scheduleTitle.getText();
            String topic = scheduleTopic.getText();
            String link = scheduleLink.getText();
            String time = scheduleTime.getText();
            String criteria = scheduleCriteria.getText();
            
            ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, time, title, topic, link, criteria);
            data.getScheduleData().addScheduleItem(scheduleItem);
            
            //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
            jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
            AddSchedule_Transaction transaction = new AddSchedule_Transaction(app, scheduleItem);
            transactionComponent.addTransaction(transaction);
            
        }
        
        typeChoices.getSelectionModel().clearSelection();
        currentDay.setValue(null);
        scheduleTime.setText("");
        scheduleTitle.setText("");
        scheduleTopic.setText("");
        scheduleLink.setText("");
        scheduleCriteria.setText("");               
        scheduleTime.setDisable(false);
        scheduleTitle.setDisable(false);
        scheduleTopic.setDisable(false);
        scheduleLink.setDisable(false);
        scheduleCriteria.setDisable(false);
        typeChoices.requestFocus();
        
        fileController.markAsEdited(gui);

    }
    
    public void handleEditScheduleItem(TableView scheduleTable, ChoiceBox typeChoices, DatePicker currentDay, TextField scheduleTime, TextField scheduleTitle, TextField scheduleTopic, TextField scheduleLink, TextField scheduleCriteria){
        CSGData data = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        ScheduleItem oldScheduleItem = (ScheduleItem)scheduleTable.getSelectionModel().getSelectedItem();
        
        if(typeChoices.getSelectionModel().getSelectedItem() == null){
            //THROW A DIALOG TO PROMPT USER TO PICK A SCHEDULE ITEM TYPE
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.ILLEGAL_SCHEDULE_DATA_TITLE), props.getProperty(CSGProp.ILLEGAL_SCHEDULE_DATA_MESSAGE));
            return;
        }
        
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_HOLIDAY))){ //ITS A HOLIDAY ITEM.
                
                if(currentDay.getValue() != null && (scheduleTitle.getText() != null || !scheduleTitle.getText().trim().isEmpty()) 
                        && (scheduleLink.getText() != null || !scheduleLink.getText().trim().isEmpty())){
                    LocalDate currentDate = currentDay.getValue();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    String currentDateString = currentDate.format(format);
                    String title = scheduleTitle.getText();
                    String link = scheduleLink.getText();
                    ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, "", title, "", link, "");
                    data.getScheduleData().editScheduleItem(oldScheduleItem, scheduleItem);
                    
                    //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
                    jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                    EditSchedule_Transaction transaction = new EditSchedule_Transaction(app, oldScheduleItem, scheduleItem);
                    transactionComponent.addTransaction(transaction);
                }
                else{
                    //THROW A DIALOG TO PROMPT USER TO PICK A SCHEDULE ITEM TYPE
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(CSGProp.INVALID_SCHEDULE_ENTRY_TITLE), props.getProperty(CSGProp.INVALID_SCHEDULE_ENTRY_MESSAGE));
                } 
                
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_LECTURE))){ //ITS A LECTURE ITEM.
            
            LocalDate currentDate = currentDay.getValue();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDateString = currentDate.format(format);
            String title = scheduleTitle.getText();
            String topic = scheduleTopic.getText();
            String link = scheduleLink.getText();
            
            ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, "", title, topic, link, "");
            data.getScheduleData().editScheduleItem(oldScheduleItem, scheduleItem);
                                
            //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
            jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
            EditSchedule_Transaction transaction = new EditSchedule_Transaction(app, oldScheduleItem, scheduleItem);
            transactionComponent.addTransaction(transaction);
            
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_REFERENCE))){ //ITS A REFERENCE ITEM.
            
            LocalDate currentDate = currentDay.getValue();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDateString = currentDate.format(format);
            String title = scheduleTitle.getText();
            String topic = scheduleTopic.getText();
            String link = scheduleLink.getText();
            
            ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, "", title, topic, link, "");
            data.getScheduleData().editScheduleItem(oldScheduleItem, scheduleItem);
                                
            //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
            jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
            EditSchedule_Transaction transaction = new EditSchedule_Transaction(app, oldScheduleItem, scheduleItem);
            transactionComponent.addTransaction(transaction);
            
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_RECITATION))){ //ITS A RECITATION ITEM.
            
            LocalDate currentDate = currentDay.getValue();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDateString = currentDate.format(format);
            String title = scheduleTitle.getText();
            String topic = scheduleTopic.getText();
            
            ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, "", title, topic, "", "");
            data.getScheduleData().editScheduleItem(oldScheduleItem, scheduleItem);
                                
            //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
            jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
            EditSchedule_Transaction transaction = new EditSchedule_Transaction(app, oldScheduleItem, scheduleItem);
            transactionComponent.addTransaction(transaction);
            
        }
        if(typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_HW))){ //ITS A HOMEWORK ITEM.
            
            LocalDate currentDate = currentDay.getValue();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDateString = currentDate.format(format);
            String title = scheduleTitle.getText();
            String topic = scheduleTopic.getText();
            String link = scheduleLink.getText();
            String time = scheduleTime.getText();
            String criteria = scheduleCriteria.getText();
            
            ScheduleItem scheduleItem = new ScheduleItem((String)typeChoices.getSelectionModel().getSelectedItem(), currentDateString, time, title, topic, link, criteria);
            data.getScheduleData().editScheduleItem(oldScheduleItem, scheduleItem);
            
            //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULE TAB
            jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
            EditSchedule_Transaction transaction = new EditSchedule_Transaction(app, oldScheduleItem, scheduleItem);
            transactionComponent.addTransaction(transaction);

        }
        
//        typeChoices.getSelectionModel().clearSelection();
//        currentDay.setValue(null);
//        scheduleTime.setText("");
//        scheduleTitle.setText("");
//        scheduleTopic.setText("");
//        scheduleLink.setText("");
//        scheduleCriteria.setText("");               
//        scheduleTime.setDisable(false);
//        scheduleTitle.setDisable(false);
//        scheduleTopic.setDisable(false);
//        scheduleLink.setDisable(false);
//        scheduleCriteria.setDisable(false);
        typeChoices.requestFocus();
        
        fileController.markAsEdited(gui);
    }
    
    public void handleRemoveScheduleItem(TableView scheduleTable, ChoiceBox typeChoices, DatePicker currentDay, TextField scheduleTime, TextField scheduleTitle, TextField scheduleTopic, TextField scheduleLink, TextField scheduleCriteria){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        ScheduleItem selectedScheduleItem = (ScheduleItem)scheduleTable.getSelectionModel().getSelectedItem();
        CSGData data = (CSGData) app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        data.getScheduleData().removeScheduleItem(selectedScheduleItem);
        
//        typeChoices.getSelectionModel().clearSelection();
//        currentDay.setValue(null);
//        scheduleTime.setText("");
//        scheduleTitle.setText("");
//        scheduleTopic.setText("");
//        scheduleLink.setText("");
//        scheduleCriteria.setText("");               
//        scheduleTime.setDisable(false);
//        scheduleTitle.setDisable(false);
//        scheduleTopic.setDisable(false);
//        scheduleLink.setDisable(false);
//        scheduleCriteria.setDisable(false);
        typeChoices.requestFocus();
        fileController.markAsEdited(gui);
        //ADD JTPS TRANSACTION BELOW HERE SPECIFIC FOR THE SCHEDULETAB
        jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
        RemoveSchedule_Transaction transaction = new RemoveSchedule_Transaction(app, selectedScheduleItem);
        transactionComponent.addTransaction(transaction);
    }
    
    public void handleSetStartMonday(LocalDate startingDate) throws ParseException{
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        ScheduleData schedData = data.getScheduleData();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        DatePicker startingMondayPicker = workspace.getStartingDay();
                
        if(startingDate != null && !startingDate.getDayOfWeek().name().equals("MONDAY")){
            startingMondayPicker.setValue(null);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.NOT_MONDAY_TITLE), props.getProperty(CSGProp.NOT_MONDAY_MESSAGE)); 
            return;
        }
        
        //IF FRIDAY IS GIVEN / NOT NULL   AND THE USER ENTERED STARTING DATE ISNT NULL
        if(startingDate != null && schedData.getEndingFridayString() != null){
            LocalDate endingFriday = schedData.getEndingFriday();
            if(endingFriday != null && startingDate.isAfter(endingFriday)){ //IF THE ENDING FRIDAY COMES BEFORE THE STARTING MONDAY
//                startingMondayPicker.setValue(schedData.getStartingMonday());
                startingMondayPicker.setValue(null);
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_START_DAY_TITLE), props.getProperty(CSGProp.INVALID_START_DAY_MESSAGE)); 
            }
             //IF FRIDAY IS GIVEN / NOT NULL AND THE MONDAY HAS A PREVIOUS VALUE
            else if(schedData.getStartingMondayString() != null){
                LocalDate oldStartingDate = schedData.getStartingMonday();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String currentDateString = startingDate.format(format);
                data.getScheduleData().setStartingMonday(currentDateString);
                fileController.markAsEdited(gui);

                jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                EditStartSchedule_Transaction transaction = new EditStartSchedule_Transaction(app, oldStartingDate, startingDate);
                transactionComponent.addTransaction(transaction);
            }
            //IF FRIDAY IS GIVEN / NOT NULL AND THE MONDAY DOES NOT HAVE A PREVIOUS VALUE
            else{
                LocalDate oldStartingDate = null;
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String currentDateString = startingDate.format(format);
                data.getScheduleData().setStartingMonday(currentDateString);
                fileController.markAsEdited(gui);

                jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                EditStartSchedule_Transaction transaction = new EditStartSchedule_Transaction(app, oldStartingDate, startingDate);
                transactionComponent.addTransaction(transaction);
            }
        }
       
        //IF FRIDAY IS NOT GIVEN / IS NULL   AND THE USER ENTERED STARTING DATE ISNT NULL
        else if (startingDate != null && schedData.getEndingFridayString() == null){
            //IF FRIDAY IS NOT GIVEN / NOT NULL AND THE MONDAY HAS A PREVIOUS VALUE
            if(data.getScheduleData().getStartingMondayString() != null){
                LocalDate oldStartingDate = data.getScheduleData().getStartingMonday();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String currentDateString = startingDate.format(format);
                data.getScheduleData().setStartingMonday(currentDateString);
                fileController.markAsEdited(gui);

                jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                EditStartSchedule_Transaction transaction = new EditStartSchedule_Transaction(app, oldStartingDate, startingDate);
                transactionComponent.addTransaction(transaction);
            }
            //IF FRIDAY IS NOT GIVEN / NOT NULL AND THE MONDAY DOES NOT HAVE A PREVIOUS VALUE
            else{
                LocalDate oldStartingDate = null;
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String currentDateString = startingDate.format(format);
                data.getScheduleData().setStartingMonday(currentDateString);
                fileController.markAsEdited(gui);

                jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                EditStartSchedule_Transaction transaction = new EditStartSchedule_Transaction(app, oldStartingDate, startingDate);
                transactionComponent.addTransaction(transaction);
            }
        }
        
    }
    
    public void handleSetEndFriday(LocalDate endingDate) throws ParseException{
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        CSGData data = (CSGData) app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        ScheduleData schedData = data.getScheduleData();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        DatePicker endingFridayPicker = workspace.getEndingDay();
        
        
        if(endingDate != null && !endingDate.getDayOfWeek().name().equals("FRIDAY")){
            endingFridayPicker.setValue(null);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.NOT_FRIDAY_TITLE), props.getProperty(CSGProp.NOT_FRIDAY_MESSAGE)); 
            return;
        }
        
        //IF USER ENTERED FRIDAY IS NOT NULL AND THE MONDAY IS NOT NULL
        if(endingDate != null && schedData.getStartingMondayString() != null){
            LocalDate startingMonday = schedData.getStartingMonday();
            if(startingMonday != null && endingDate.isBefore(startingMonday)){ //IF THE STARTING MONDAY COMES AFTER THE ENDING FRIDAY.
//                endingFridayPicker.setValue(schedData.getEndingFriday());
                endingFridayPicker.setValue(null);
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(CSGProp.INVALID_END_DAY_TITLE), props.getProperty(CSGProp.INVALID_END_DAY_MESSAGE)); 
            }
            //IF THE FRIDAY HAS A PREVIOUS VALUE
            else if(data.getScheduleData().getEndingFridayString() != null){
                LocalDate oldEndingDate = data.getScheduleData().getEndingFriday();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String currentDateString = endingDate.format(format);
                data.getScheduleData().setEndingFriday(currentDateString);
                fileController.markAsEdited(gui);
                jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                EditEndSchedule_Transaction transaction = new EditEndSchedule_Transaction(app, oldEndingDate, endingDate);
                transactionComponent.addTransaction(transaction);
            }
            //IF THE FRIDAY DOES NOT HAVE A PREVIOUS VALUE
            else{
                LocalDate oldEndingDate = null;
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String currentDateString = endingDate.format(format);
                data.getScheduleData().setEndingFriday(currentDateString);
                fileController.markAsEdited(gui);
                jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                EditEndSchedule_Transaction transaction = new EditEndSchedule_Transaction(app, oldEndingDate, endingDate);
                transactionComponent.addTransaction(transaction);
            }
        }
        //IF USER ENTERED FRIDAY IS NOT NULL AND THE MONDAY IS NULL
        else if(endingDate != null && schedData.getStartingMondayString() == null){
            //IF THE FRIDAY HAS A PREVIOUS VALUE
            if(data.getScheduleData().getEndingFridayString() != null){
                LocalDate oldEndingDate = data.getScheduleData().getEndingFriday();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String currentDateString = endingDate.format(format);
                data.getScheduleData().setEndingFriday(currentDateString);
                fileController.markAsEdited(gui);
                jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                EditEndSchedule_Transaction transaction = new EditEndSchedule_Transaction(app, oldEndingDate, endingDate);
                transactionComponent.addTransaction(transaction); 
            }
            //IF THE FRIDAY DOES NOT HAVE A PREVIOUS VALUE
            else{
                LocalDate oldEndingDate = null;
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String currentDateString = endingDate.format(format);
                data.getScheduleData().setEndingFriday(currentDateString);
                fileController.markAsEdited(gui);
                jTPS_Schedule transactionComponent = workspace.getScheduleComponent();
                EditEndSchedule_Transaction transaction = new EditEndSchedule_Transaction(app, oldEndingDate, endingDate);
                transactionComponent.addTransaction(transaction); 
            }
        }
    }
}
