/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.*;
import javafx.collections.ObservableList;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import csg.data.*;
import csg.workspace.CSGWorkspace;
import csg.workspace.TAController;
/**
 *
 * @author Andrew
 */
public class AddTA_Transaction implements jTPS_Transaction {
    private TeachingAssistant ta;
    CSGApp app;
    //TAData data;
    public AddTA_Transaction(TeachingAssistant initTa, CSGApp initApp/*, TAData initData*/){
        ta = initTa;
        app = initApp;
       // data = initData;
    }
    
    @Override
    public void doTransaction() {
        //ADD
        
//        ObservableList<TeachingAssistant> teachingAssistants = data.getTeachingAssistants();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
//        TAController controller = workspace.getController();
//        controller.handleAddTA();
            CSGData data = (CSGData) app.getDataComponent();
            //data.addTA(ta.getName(), ta.getEmail());
            data.getTAData().addTAWithoutConstruct(ta);
            
//            //ADD THE ACTION AS A TRANSACTION
//            jTPS transactionComponent = workspace.getTransactionComponent();
//            AddTA_Transaction transaction= new AddTA_Transaction(data.getTA(ta.getName()), app); //add a specific AddTA_Transaction transaction 
//                                                                    //object to the jTPS array. So when undo, undoTransaction() 
//                                                                    //in jTPS will call the undoTransaction() method in AddTA_Transaction.java
//            transactionComponent.addTransaction(transaction);
    }

    @Override
    public void undoTransaction() {
       //REMOVE
//        ObservableList<TeachingAssistant> teachingAssistants = data.getTeachingAssistants();
        CSGData data = (CSGData) app.getDataComponent();
        data.getTAData().removeTA(ta);
    }
}
