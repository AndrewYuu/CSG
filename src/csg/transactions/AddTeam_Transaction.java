/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.Team;
import csg.workspace.CSGWorkspace;
import jtps.jTPS_Transaction;

/**
 *
 * @author Andrew
 */
public class AddTeam_Transaction implements jTPS_Transaction {
    private Team team;
    CSGApp app;
    
    public AddTeam_Transaction(CSGApp initApp, Team initTeam){
        team = initTeam;
        app = initApp;
    }
    
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        data.getProjectData().addTeam(team);
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        
        data.getProjectData().removeTeam(team);
    }
}
