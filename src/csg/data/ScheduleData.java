/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Andrew
 */
public class ScheduleData {
    CSGApp app;
    String startingMonday;
    String endingFriday;
    
    ObservableList<ScheduleItem> scheduleItems;
    
    public ScheduleData(CSGApp initApp){
        app = initApp;
        
        scheduleItems = FXCollections.observableArrayList();
    }
    public void resetData(){
        scheduleItems.clear();
    }
    public void addScheduleItem(ScheduleItem scheduleItem){
        scheduleItems.add(scheduleItem);
        Collections.sort(scheduleItems);
    }
    
    public void removeScheduleItem(ScheduleItem scheduleItem){
        scheduleItems.remove(scheduleItem);
        Collections.sort(scheduleItems);
    }
    
    public void editScheduleItem(ScheduleItem oldScheduleItem, ScheduleItem newScheduleItem){
        scheduleItems.remove(oldScheduleItem);
        scheduleItems.add(newScheduleItem);
        Collections.sort(scheduleItems);
    }
    public ObservableList<ScheduleItem> getScheduleItems(){
        return scheduleItems;
    }
    
    
    public String getStartingMondayString() {
        return startingMonday;
    }

    public String getEndingFridayString() {
        return endingFriday;
    }
    
    public LocalDate getStartingMonday() throws ParseException{
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        if(!startingMonday.equals("")){
            LocalDate startingMondayDate = LocalDate.parse(startingMonday, format);
            return startingMondayDate;
        }
        else{
            return null;
        }
    }
    
    public LocalDate getEndingFriday() throws ParseException{
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        if(!endingFriday.equals("")){
            LocalDate endingFridayDate = LocalDate.parse(endingFriday, format);
            return endingFridayDate;
        }
        else{
            return null;
        }
    }
    
    public void setStartingMonday(String startingMonday) {
        this.startingMonday = startingMonday;
    }

    public void setEndingFriday(String endingFriday) {
        this.endingFriday = endingFriday;
    }
}
