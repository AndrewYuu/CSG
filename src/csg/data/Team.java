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
public class Team<E extends Comparable<E>> implements Comparable<E> {
    private StringProperty teamName;
    private StringProperty teamColor;
    private StringProperty textColor;
    private StringProperty link;
    
    public Team(String initTeamName, String initTeamColor, String initTextColor, String initLink){
        teamName =  new SimpleStringProperty(initTeamName);
        teamColor = new SimpleStringProperty(initTeamColor);
        textColor = new SimpleStringProperty(initTextColor);
        link = new SimpleStringProperty(initLink);
    }
    
    
    public String getTeamName() {
        return teamName.get();
    }

    public String getTeamColor() {
        return teamColor.get();
    }

    public String getTextColor() {
        return textColor.get();
    }

    public String getLink() {
        return link.get();
    }
    
    @Override
    public int compareTo(E otherTeam){
        return getTeamName().compareTo(((Team)otherTeam).getTeamName());
    }
    
}
