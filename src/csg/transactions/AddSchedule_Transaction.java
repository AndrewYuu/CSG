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
public class AddSchedule_Transaction implements jTPS_Transaction{
    CSGApp app;
    ScheduleItem scheduleItem;
    
    public AddSchedule_Transaction(CSGApp initApp, ScheduleItem initScheduleItem){
        app = initApp;
        scheduleItem = initScheduleItem;
    }
    
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        data.getScheduleData().addScheduleItem(scheduleItem);
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        data.getScheduleData().removeScheduleItem(scheduleItem);
    }
    
}
