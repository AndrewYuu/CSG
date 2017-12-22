/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import csg.CSGProp;
import csg.workspace.CSGWorkspace;
import djf.components.AppDataComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class CSGData implements AppDataComponent{
    CSGApp app;
    TAData taData;
    CourseDetailsData courseDetailsData;
    RecitationData recitationData;
    ScheduleData scheduleData;
    ProjectData projectData;
    
    public CSGData(CSGApp initApp){
        app = initApp;
        taData = new TAData(app);
        courseDetailsData = new CourseDetailsData(app);
        recitationData = new RecitationData(app);
        scheduleData = new ScheduleData(app);
        projectData = new ProjectData(app);
    }
    
    @Override
    public void resetData(){
        courseDetailsData.resetData();
        recitationData.resetData();
        taData.resetData();
        scheduleData.resetData();
        projectData.resetData();
        
    }
    
    public TAData getTAData(){
        return taData;
    }
    
    public CourseDetailsData getCourseDetailsData(){
        return courseDetailsData;
    }
    
    public RecitationData getRecitationData(){
        return recitationData;
    }
    
    public ScheduleData getScheduleData(){
        return scheduleData;
    }
    
    public ProjectData getProjectData(){
        return projectData;
    }
}