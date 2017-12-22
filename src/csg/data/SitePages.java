/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andrew
 */
public class SitePages {
    private boolean use;
    private BooleanProperty useProperty;
    private StringProperty navbarTitle;

    private StringProperty fileName;
    private StringProperty script;
    
    public SitePages(boolean initUse, String initNavbarTitle, String initFileName, String initScript){
//        use = initUse; //WHEN SITEPAGES OBJECTS ARE CONSTRUCTED, THE DEFAULT VALUE OF use THAT IS PASSED IN SHOULD BE "TRUE".
        useProperty = new SimpleBooleanProperty(initUse);
        navbarTitle =  new SimpleStringProperty(initNavbarTitle);
        fileName =  new SimpleStringProperty(initFileName);
        script =  new SimpleStringProperty(initScript);
        
    }
    
    
    public String getUse() {
        if(use){
            return ("true");
        }
        return ("false");
    }

    public String getNavbarTitle() {
        return navbarTitle.get();
    }

    public String getFileName() {
        return fileName.get();
    }

    public String getScript() {
        return script.get();
    }
    
    public boolean isUse(){
        return use;
    }
    
    public BooleanProperty isUseProperty(){
        return useProperty;
    }
}
