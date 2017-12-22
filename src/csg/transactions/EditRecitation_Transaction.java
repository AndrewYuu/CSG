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
public class EditRecitation_Transaction implements jTPS_Transaction{
    
    CSGApp app;
    Recitation oldRecitation;
    Recitation newRecitation;
    
    public EditRecitation_Transaction(CSGApp initApp, Recitation initOldRecitation, Recitation initNewRecitation){
        app = initApp;
        oldRecitation = initOldRecitation;
        newRecitation = initNewRecitation;
    }
    
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        data.getRecitationData().editRecitation(oldRecitation, newRecitation);
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        data.getRecitationData().editRecitation(newRecitation, oldRecitation);
    }
}
