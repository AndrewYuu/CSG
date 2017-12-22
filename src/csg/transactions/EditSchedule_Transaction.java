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
import jtps.jTPS_Transaction;

/**
 *
 * @author Andrew
 */
public class EditSchedule_Transaction implements jTPS_Transaction{
    CSGApp app;
    ScheduleItem oldScheduleItem;
    ScheduleItem scheduleItem;
    
    public EditSchedule_Transaction(CSGApp initApp, ScheduleItem initOldScheduleItem, ScheduleItem initScheduleItem){
        app = initApp;
        oldScheduleItem = initOldScheduleItem;
        scheduleItem = initScheduleItem;
    }
    
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        data.getScheduleData().editScheduleItem(oldScheduleItem, scheduleItem);
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        data.getScheduleData().editScheduleItem(scheduleItem, oldScheduleItem);
    }
    
}
