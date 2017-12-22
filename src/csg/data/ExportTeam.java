/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andrew
 */
public class ExportTeam {
    private StringProperty teamName;
    private StringProperty red;
    private StringProperty green;
    private StringProperty blue;
    private StringProperty textColor;
    
    public ExportTeam(String initTeamName, String initRed, String initGreen, String initBlue, String initTextColor){
        teamName =  new SimpleStringProperty(initTeamName);
        red = new SimpleStringProperty(initRed);
        green = new SimpleStringProperty(initGreen);
        blue = new SimpleStringProperty(initBlue);
        textColor = new SimpleStringProperty(initTextColor);
    }
    
    
    public String getTeamName() {
        return teamName.get();
    }

    public String getRed() {
        return red.get();
    }
    
    public String getBlue(){
        return blue.get();
    }
    
    public String getGreen(){
        return green.get();
    }

    public String getTextColor() {
        return textColor.get();
    }
}
