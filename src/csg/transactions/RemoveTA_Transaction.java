/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import djf.controller.AppFileController;
import djf.ui.AppGUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import csg.CSGApp;
import csg.data.CSGData;
import csg.data.Recitation;
import csg.data.TeachingAssistant;
import csg.workspace.CSGWorkspace;
import csg.workspace.TAController;

/**
 *
 * @author Andrew
 */
public class RemoveTA_Transaction implements jTPS_Transaction {
    private TeachingAssistant ta;
    CSGApp app;
    ArrayList<String> keysToAdd;
    
    ArrayList<Recitation> recitationsEdited;
    //TAData data;
    public RemoveTA_Transaction(TeachingAssistant initTa, CSGApp initApp, ArrayList<String> keysToKeep, ArrayList<Recitation> initRecitationsEdited){
        ta = initTa;
        app = initApp;
        keysToAdd = keysToKeep;
        recitationsEdited = initRecitationsEdited;
    }
    
    @Override
    public void doTransaction() {
        //DELETE THE TA FROM TABLEVIEW AND GRID
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
         
        //MARK AS EDIT ONCE ANY CHANGE IS MADE
        fileController.markAsEdited(app.getGUI());
        
        
        // GET THE TABLE
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
        //GET THE NAME OF THE TA AND ADD IT BACK TO THE TABLEVIEW
        String taName = ta.getName();
        CSGData data = (CSGData)app.getDataComponent();
        data.getTAData().removeTA(ta);
        
        HashMap<String, StringProperty> officeHours = data.getTAData().getOfficeHours();
        StringProperty cellProp;
        for(int i = 0; i < keysToAdd.size(); i++){
            cellProp = officeHours.get(keysToAdd.get(i));
            data.getTAData().removeTAFromCell(cellProp, taName);
        }
//                
//        for(int i = 0; i < data.getRecitationData().getRecitations().size(); i++){
//            if((data.getRecitationData().getRecitations().get(i).getFirstTA().equals(taName)) && (data.getRecitationData().getRecitations().get(i).getSecondTA().equals(taName))){
//                Recitation oldRecitationRemoved = data.getRecitationData().getRecitations().get(i);
//                Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                        data.getRecitationData().getRecitations().get(i).getInstructor(),
//                        data.getRecitationData().getRecitations().get(i).getDayTime(),
//                        data.getRecitationData().getRecitations().get(i).getLocation(),
//                        "", "");
//                data.getRecitationData().editRecitation(oldRecitationRemoved, newRecitation);
//            }
//            else if(data.getRecitationData().getRecitations().get(i).getFirstTA().equals(taName)){
//                Recitation oldRecitationRemoved = data.getRecitationData().getRecitations().get(i);
//                Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                        data.getRecitationData().getRecitations().get(i).getInstructor(),
//                        data.getRecitationData().getRecitations().get(i).getDayTime(),
//                        data.getRecitationData().getRecitations().get(i).getLocation(),
//                        "",
//                        data.getRecitationData().getRecitations().get(i).getSecondTA());
//                data.getRecitationData().editRecitation(oldRecitationRemoved, newRecitation);
//            }
//            else if(data.getRecitationData().getRecitations().get(i).getSecondTA().equals(taName)){
//                Recitation oldRecitationRemoved = data.getRecitationData().getRecitations().get(i);
//                Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                        data.getRecitationData().getRecitations().get(i).getInstructor(),
//                        data.getRecitationData().getRecitations().get(i).getDayTime(),
//                        data.getRecitationData().getRecitations().get(i).getLocation(),
//                        data.getRecitationData().getRecitations().get(i).getFirstTA(),
//                        "");
//                data.getRecitationData().editRecitation(oldRecitationRemoved, newRecitation);
//            }
//        }
        
    }

    @Override
    public void undoTransaction() {
        //ADD THE TA BACK TO TABLEVIEW AND TO CORRECT CELLS IF THEY EXISTED
        
        
        
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
    
        //MARK AS EDIT ONCE ANY CHANGE IS MADE
        fileController.markAsEdited(app.getGUI());
        
        
        // GET THE TABLE
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
        //GET THE NAME OF THE TA AND ADD IT BACK TO THE TABLEVIEW
        String taName = ta.getName();
        CSGData data = (CSGData)app.getDataComponent();
        data.getTAData().addTAWithoutConstruct(ta);
        
        HashMap<String, StringProperty> officeHours = data.getTAData().getOfficeHours();
        StringProperty cellProp;
        for(int i = 0; i < keysToAdd.size(); i++){
            cellProp = officeHours.get(keysToAdd.get(i));
            data.getTAData().addTAToCell(cellProp, taName);
        }
        
//        for(int j = 0; j < recitationsEdited.size(); j++){
//            for(int i = 0; i < data.getRecitationData().getRecitations().size(); i++){
//                if((data.getRecitationData().getRecitations().get(i).equals(recitationsEdited.get(j)))){
//                    if(recitationsEdited.get(j).getFirstTA().equals("") && recitationsEdited.get(j).getSecondTA().equals("")){
//                        Recitation oldRecitationRemoved = data.getRecitationData().getRecitations().get(i);
//                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                                data.getRecitationData().getRecitations().get(i).getInstructor(),
//                                data.getRecitationData().getRecitations().get(i).getDayTime(),
//                                data.getRecitationData().getRecitations().get(i).getLocation(),
//                                taName, taName);
//                        data.getRecitationData().editRecitation(oldRecitationRemoved, newRecitation);
//                    }
//                    else if(recitationsEdited.get(j).getFirstTA().equals("")){
//                        Recitation oldRecitationRemoved = data.getRecitationData().getRecitations().get(i);
//                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                                data.getRecitationData().getRecitations().get(i).getInstructor(),
//                                data.getRecitationData().getRecitations().get(i).getDayTime(),
//                                data.getRecitationData().getRecitations().get(i).getLocation(),
//                                taName,
//                                data.getRecitationData().getRecitations().get(i).getSecondTA());
//                        data.getRecitationData().editRecitation(oldRecitationRemoved, newRecitation);
//                    }
//                    else if(recitationsEdited.get(j).getSecondTA().equals("")){
//                        Recitation oldRecitationRemoved = data.getRecitationData().getRecitations().get(i);
//                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                                data.getRecitationData().getRecitations().get(i).getInstructor(),
//                                data.getRecitationData().getRecitations().get(i).getDayTime(),
//                                data.getRecitationData().getRecitations().get(i).getLocation(),
//                                data.getRecitationData().getRecitations().get(i).getFirstTA(),
//                                taName);
//                        data.getRecitationData().editRecitation(oldRecitationRemoved, newRecitation);
//                    }
//                }
//            }   
//        }
    }
}
