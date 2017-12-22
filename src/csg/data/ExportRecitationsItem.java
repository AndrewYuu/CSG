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
public class ExportRecitationsItem {
    private StringProperty month;
    private StringProperty day;
    private StringProperty title;
    private StringProperty topic;
    
    public ExportRecitationsItem(String initMonth, String initDay, String initTitle, String initTopic){
        month = new SimpleStringProperty(initMonth);
        day = new SimpleStringProperty(initDay);
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
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
}
