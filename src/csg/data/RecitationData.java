/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import csg.workspace.CSGWorkspace;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Andrew
 */
public class RecitationData {
    CSGApp app;
    ObservableList<Recitation> recitations;
    
    public RecitationData(CSGApp initApp){
        app = initApp;
        
        recitations = FXCollections.observableArrayList();
    }
    
    public void resetData(){
        recitations.clear();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        workspace.clearSupervisingChoices();
    }
    
    public void addRecitation(Recitation recitation){
        recitations.add(recitation);
        Collections.sort(recitations);
    }
    
    public void editRecitation(Recitation oldRecitation, Recitation newRecitation){
        recitations.remove(oldRecitation);
        recitations.add(newRecitation);
        Collections.sort(recitations);
    }
    
    public void removeRecitation(Recitation recitation){
        recitations.remove(recitation);
        Collections.sort(recitations);
    }
    
    public ObservableList<Recitation> getRecitations(){
        return recitations;
    }
    
}
