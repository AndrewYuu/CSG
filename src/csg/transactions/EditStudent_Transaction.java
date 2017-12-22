/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.Student;
import csg.workspace.CSGWorkspace;
import jtps.jTPS_Transaction;

/**
 *
 * @author Andrew
 */
public class EditStudent_Transaction implements jTPS_Transaction{
    CSGApp app;
    Student oldStudent;
    Student newStudent;

    public EditStudent_Transaction(CSGApp initApp, Student initOldStudent, Student initNewStudent){
        app = initApp;
        oldStudent = initOldStudent;
        newStudent = initNewStudent;
    }
    @Override
    public void doTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        data.getProjectData().editStudent(oldStudent, newStudent);
    }
    
    @Override
    public void undoTransaction(){
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        data.getProjectData().editStudent(newStudent, oldStudent);
    }
    
}
