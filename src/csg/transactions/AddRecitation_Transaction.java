/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.Recitation;
import csg.workspace.CSGWorkspace;
import jtps.jTPS_Transaction;

/**
 *
 * @author Andrew
 */
public class AddRecitation_Transaction implements jTPS_Transaction{
    
    CSGApp app;
    Recitation recitation;
    
    public AddRecitation_Transaction(CSGApp initApp, Recitation initRecitation){
        app = initApp;
        recitation = initRecitation;
    }
    
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        data.getRecitationData().addRecitation(recitation);
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        data.getRecitationData().removeRecitation(recitation);
    }
}
