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
public class ExportRecitation {
    StringProperty section;
    StringProperty dayTime;
    StringProperty location;
    StringProperty firstTA;
    StringProperty secondTA;
    
    
    public ExportRecitation(String initSection, String initDayTime, String initLocation, String initFirstTA, String initSecondTA){
        section = new SimpleStringProperty(initSection);
        dayTime = new SimpleStringProperty(initDayTime);
        location = new SimpleStringProperty(initLocation);
        firstTA = new SimpleStringProperty(initFirstTA);
        secondTA = new SimpleStringProperty(initSecondTA);
    }
    
    
    public String getSection() {
        return section.get();
    }

    public String getDayTime() {
        return dayTime.get();
    }

    public String getLocation() {
        return location.get();
    }

    public String getFirstTA() {
        return firstTA.get();
    }

    public String getSecondTA() {
        return secondTA.get();
    }
    
}
