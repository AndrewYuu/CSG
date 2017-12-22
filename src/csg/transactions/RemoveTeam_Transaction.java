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
public class RemoveTeam_Transaction implements jTPS_Transaction{
    CSGApp app;
    Team team;
    ArrayList<Student> studentsToRemove;
    
    public RemoveTeam_Transaction(CSGApp initApp, Team initTeam, ArrayList<Student> initStudentsToRemove){
        app = initApp;
        team = initTeam;
        studentsToRemove = initStudentsToRemove;
    }
    
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        data.getProjectData().removeTeam(team);
        for(int i = 0; i < studentsToRemove.size(); i++){
            Student student = studentsToRemove.get(i);
            data.getProjectData().removeStudent(student);
        }
        workspace.getStudentsTable().refresh();
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        data.getProjectData().addTeam(team);
        
        for(int i = 0; i < studentsToRemove.size(); i++){
            Student student = studentsToRemove.get(i);
            data.getProjectData().addStudent(student);
        }
        workspace.getStudentsTable().refresh();
    }
    
}
