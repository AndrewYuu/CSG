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
public class Recitation <E extends Comparable<E>> implements Comparable<E> {
    StringProperty section;
    StringProperty instructor;
    StringProperty dayTime;
    StringProperty location;
    StringProperty firstTA;
    StringProperty secondTA;
    
    
    public Recitation(String initSection, String initInstructor, String initDayTime, String initLocation, String initFirstTA, String initSecondTA){
        section = new SimpleStringProperty(initSection);
        instructor = new SimpleStringProperty(initInstructor);
        dayTime = new SimpleStringProperty(initDayTime);
        location = new SimpleStringProperty(initLocation);
        firstTA = new SimpleStringProperty(initFirstTA);
        secondTA = new SimpleStringProperty(initSecondTA);
    }
    
    
    public String getSection() {
        return section.get();
    }

    public String getInstructor() {
        return instructor.get();
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
    
    
    public void setSection(String section) {
        this.section.set(section);
    }

    public void setInstructor(String instructor) {
        this.instructor.set(instructor);
    }

    public void setDayTime(String dayTime) {
        this.dayTime.set(dayTime);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public void setFirstTA(String firstTA) {
        this.firstTA.set(firstTA);
    }

    public void setSecondTA(String secondTA) {
        this.secondTA.set(secondTA);
    }
    
    
    @Override
    public int compareTo(E otherRecitation){
        return getSection().compareTo(((Recitation)otherRecitation).getSection());
    }
    
}
