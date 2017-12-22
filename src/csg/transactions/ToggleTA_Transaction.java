/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import javafx.scene.layout.Pane;
import jtps.jTPS_Transaction;
import csg.CSGApp;
import csg.data.CSGData;
import csg.workspace.TAController;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Andrew
 */
public class ToggleTA_Transaction implements jTPS_Transaction {
    private String cellKey;
    private String taName;
    CSGApp app;
    
    
    public ToggleTA_Transaction(CSGApp initApp, String initCellKey, String initTaName){
        app = initApp;
        cellKey = initCellKey;
        taName = initTaName;
    }
    
    @Override
    public void doTransaction(){
        CSGData data = (CSGData) app.getDataComponent();
        data.getTAData().toggleTAOfficeHours(cellKey, taName);
    }
    
    @Override
    public void undoTransaction(){
        CSGData data = (CSGData) app.getDataComponent();
        data.getTAData().toggleTAOfficeHours(cellKey, taName);
    }
}
