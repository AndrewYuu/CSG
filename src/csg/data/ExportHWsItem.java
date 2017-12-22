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
public class ExportHWsItem {
      private StringProperty month;
    private StringProperty day;
    private StringProperty title;
    private StringProperty topic;
    private StringProperty link;
    private StringProperty time;
    private StringProperty criteria;
    
    public ExportHWsItem(String initMonth, String initDay, String initTitle, String initTopic, String initLink, String initTime, String initCriteria){
        month = new SimpleStringProperty(initMonth);
        day = new SimpleStringProperty(initDay);
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        link = new SimpleStringProperty(initLink);
        time = new SimpleStringProperty(initTime);
        criteria = new SimpleStringProperty(initCriteria);
    }
    
    
    public String getMonth() {
        return month.get();
    }

    public String getDay() {
        return day.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getTopic() {
        return topic.get();
    }
    
    public String getLink(){
        return link.get();
    }
    
    public String getTime(){
        return time.get();
    }
    
    public String getCriteria(){
        return criteria.get();
    }
}
