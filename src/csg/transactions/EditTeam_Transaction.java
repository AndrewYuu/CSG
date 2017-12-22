/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.Student;
import csg.data.Team;
import csg.workspace.CSGWorkspace;
import java.util.ArrayList;
import jtps.jTPS_Transaction;

/**
 *
 * @author Andrew
 */
public class EditTeam_Transaction implements jTPS_Transaction{
    CSGApp app;
    Team oldTeam;
    Team newTeam;
    ArrayList<Student> studentsToKeep;
    public EditTeam_Transaction(CSGApp initApp, Team initOldTeam, Team initNewTeam, ArrayList<Student> initStudentsToKeep){
        app = initApp;
        oldTeam = initOldTeam;
        newTeam= initNewTeam;
        studentsToKeep = initStudentsToKeep;
    }
    
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        data.getProjectData().editTeam(oldTeam, newTeam);
        for(int i = 0; i < studentsToKeep.size(); i++){
            studentsToKeep.get(i).setTeam(newTeam.getTeamName());
        }
        workspace.getStudentsTable().refresh();
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        data.getProjectData().editTeam(newTeam, oldTeam);
        for(int i = 0; i < studentsToKeep.size(); i++){
            studentsToKeep.get(i).setTeam(oldTeam.getTeamName());
        }
        workspace.getStudentsTable().refresh();
    }
}
