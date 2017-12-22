/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import jtps.jTPS_Transaction;
import csg.CSGApp;
import csg.data.CSGData;
import csg.data.TeachingAssistant;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Andrew
 */
public class EditTA_Transaction implements jTPS_Transaction {
    private TeachingAssistant ta;
    private TeachingAssistant ta2;
    private String oldName;
    private String oldEmail;
    CSGApp app;
    ArrayList<String> keysToEdit;
    //TAData data;
    public EditTA_Transaction(TeachingAssistant oldTa, TeachingAssistant editedTa, CSGApp initApp, ArrayList<String> keysToKeep){
        ta = editedTa;
        ta2 = oldTa;
        app = initApp;
        keysToEdit = keysToKeep;
        oldName = ta2.getName();
        oldEmail = ta2.getEmail();
    }
    
    @Override
    public void doTransaction() {
       CSGData data = (CSGData) app.getDataComponent();
       data.getTAData().editTAWithoutConstruct(ta2, ta); //TA IS THE NEW TA THAT HAS BEEN UPDATED BY THE USER
       HashMap<String, StringProperty> officeHours = data.getTAData().getOfficeHours();
       StringProperty cellProp;
       for(int i = 0; i < keysToEdit.size(); i++){
            cellProp = officeHours.get(keysToEdit.get(i));
            data.getTAData().editTAFromCell(cellProp, oldName, ta.getName());
       }
    }

    @Override
    public void undoTransaction() {
        //THE OLDTA SHOULD BE ADDED BACK AND THE EDITED ONE REMOVED
       CSGData data = (CSGData) app.getDataComponent();
       data.getTAData().editTAWithoutConstruct(ta, ta2);
       HashMap<String, StringProperty> officeHours = data.getTAData().getOfficeHours();
       StringProperty cellProp;
       for(int i = 0; i < keysToEdit.size(); i++){
            cellProp = officeHours.get(keysToEdit.get(i));
            data.getTAData().editTAFromCell(cellProp, ta.getName(), oldName);
       }
    }

}
