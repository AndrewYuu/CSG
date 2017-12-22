/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg;

import csg.data.CSGData;
import csg.files.CSGFiles;
import csg.style.CSGStyle;
import csg.workspace.CSGWorkspace;
import djf.AppTemplate;
import java.util.Locale;

/**
 *
 * @author Andrew
 */
public class CSGApp extends AppTemplate {
    
    @Override
    public void buildAppComponentsHook() {
        dataComponent = new CSGData(this);
        workspaceComponent = new CSGWorkspace(this);
        styleComponent = new CSGStyle(this);
        fileComponent = new CSGFiles(this);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }


}
