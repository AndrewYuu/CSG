/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import java.util.HashMap;
import java.util.Set;
import javafx.beans.property.StringProperty;
import jtps.jTPS_Transaction;
import csg.CSGApp;
import csg.data.CSGData;
import csg.workspace.TAController;
import csg.workspace.CSGWorkspace;


/**
 *
 * @author Andrew
 */
public class StartEnd_Transaction implements jTPS_Transaction{
    private HashMap<String, StringProperty> officeHoursCurrent;  
    CSGApp app;
    private String oldStartHour;
    private String oldEndHour;
    
    private String redoStartHour;
    private String redoEndHour;
    
    private HashMap<String, StringProperty> officeHoursCurrentCopy = new HashMap<>();
    
    public StartEnd_Transaction(HashMap<String, StringProperty> initOfficeHours, String initStart, String initEnd, String initRedoStart, String initRedoEnd, CSGApp initApp){
        officeHoursCurrent = initOfficeHours;
        oldStartHour = initStart;
        oldEndHour = initEnd;
        app = initApp;
        
        redoStartHour = initRedoStart;
        redoEndHour = initRedoEnd;
        
        Set<String> keySet = officeHoursCurrent.keySet();
        for(String key : keySet){
            StringProperty value = officeHoursCurrent.get(key);
            officeHoursCurrentCopy.put(key, value);
        }
    }
    
    @Override
    public void doTransaction(){
        //REPLACE THE START AND END TIME HOURS
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        TAController controller = workspace.getController(); 
        controller.resetOHGrid(redoStartHour, redoEndHour);
    }
    
    @Override
    public void undoTransaction(){
        //THE START AND END TIME HOURS SHOULD BE PLACED BACK TO WHAT THEY WERE IN THE PREVIOUS TRANSACTION
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        TAController controller = workspace.getController(); 
        CSGData data = (CSGData) app.getDataComponent();
        controller.resetOHGrid(oldStartHour, oldEndHour);
        controller.resetOHGridPutBack(officeHoursCurrentCopy);
//        officeHoursCurrent = officeHoursCurrentCopy;

    }
    
}
