/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andrew
 */
public class ScheduleItem<E extends Comparable<E>> implements Comparable<E>  {
    StringProperty type;
    StringProperty date;
    StringProperty time;
    StringProperty title;
    StringProperty topic;
    StringProperty link;
    StringProperty criteria;

    
    public ScheduleItem(String initType, String initDate, String initTime, String initTitle, String initTopic, String initLink, String initCriteria){
        type = new SimpleStringProperty(initType);
        date = new SimpleStringProperty(initDate);
        time = new SimpleStringProperty(initTime);
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        link = new SimpleStringProperty(initLink);
        criteria = new SimpleStringProperty(initCriteria);
    }
    
    
    public String getType() {
        return type.get();
    }

    public String getDateString() {
        return date.get();
    }
    
    public Date getDate() throws ParseException{
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date scheduleItemDate = format.parse(date.get());
        return scheduleItemDate;
    }

    public String getTime() {
        return time.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getTopic() {
        return topic.get(); 
    }

    public String getLink() {
        return link.get();
    }

    public String getCriteria() {
        return criteria.get();
    }
    
    @Override
    public int compareTo(E otherScheduleItem){
        String thisDateString = this.date.get();
        ScheduleItem otherItem = (ScheduleItem) otherScheduleItem;
        String otherDateString = otherItem.getDateString();
        if(thisDateString != null && otherDateString != null){
            DateFormat f = new SimpleDateFormat("MM/dd/yyyy");
            try {
                return f.parse(thisDateString).compareTo(f.parse(otherDateString));
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        else{
            return 0;
        }
    }
}
