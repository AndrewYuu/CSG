/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.ScheduleItem;
import csg.workspace.CSGWorkspace;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jtps.jTPS_Transaction;

/**
 *
 * @author Andrew
 */
public class EditEndSchedule_Transaction implements jTPS_Transaction{
     CSGApp app;
    LocalDate oldDay;
    LocalDate newDay;
    
    public EditEndSchedule_Transaction(CSGApp initApp, LocalDate initOldDay, LocalDate initNewDay){
        app = initApp;
        oldDay = initOldDay;
        newDay = initNewDay;
    }
    
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        if(newDay != null){
            String currentDateString = newDay.format(format);    
            data.getScheduleData().setEndingFriday(currentDateString);
            workspace.getEndingDay().getEditor().setText(currentDateString);
        }
        else{
           data.getScheduleData().setEndingFriday(""); 
           workspace.getEndingDay().getEditor().setText("");
        }
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        if(oldDay != null){
            String currentDateString = oldDay.format(format);    
            data.getScheduleData().setEndingFriday(currentDateString);
            workspace.getEndingDay().getEditor().setText(currentDateString);
        }
        else{
           data.getScheduleData().setEndingFriday(""); 
           workspace.getEndingDay().getEditor().setText("");
        }
        
    }
}
