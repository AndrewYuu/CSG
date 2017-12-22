/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.testbed;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.ProjectData;
import csg.data.Recitation;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import csg.data.ScheduleItem;
import csg.data.SitePages;
import csg.data.Student;
import csg.data.TAData;
import csg.data.Team;
import csg.files.CSGFiles;
import csg.style.CSGStyle;
import csg.workspace.CSGWorkspace;
import djf.components.AppDataComponent;
import static djf.settings.AppPropertyType.PROPERTIES_LOAD_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.PROPERTIES_LOAD_ERROR_TITLE;
import static djf.settings.AppStartupConstants.APP_PROPERTIES_FILE_NAME;
import static djf.settings.AppStartupConstants.PATH_DATA;
import static djf.settings.AppStartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import djf.ui.AppMessageDialogSingleton;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import properties_manager.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class TestSave { //INITIALIZE ALL OF THE FIELDS OF ALL OF THE OBJECTS TO BE SAVED INTO THE JSON FILE
    CSGApp app;
    CSGFiles files; //INITIALIZE FILES OBJECT FOR THE APP HERE BECAUSE THE APP ISNT RUNNING. USUALLY IT IS INITIALIZED IN CSGAPP. CURRENTLY THIS ACTS AS A DUMMY APP.
    CSGData tempData; //INITALIZE DATA OBJECT HERE BECAUSE THE APP ISNT RUNNING. USUALLY IT IS INITIALIZED IN CSGAPP. CURRENT THIS CLASS ACTS AS A DUMMY APP.
    HashMap<String, StringProperty> officeHourHashMap;
    
    public TestSave(/*CSGApp initApp*/) throws Exception{
        //app = initApp;
///////////////////RUN INDEPENDENTLY FROM THE APP WITH TESTLOAD.JAVA CLASS, SO SET APP = NULL. HOWEVER, IT STILL USES THE SAVE AND LOAD METHODS IN THE CSG FILES PACKAGE.
        app = null;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	// LOAD THE SETTINGS FOR STARTING THE APP
	props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA); //PATH_DATA LOADS FILE FROM /data/ --> app_properties.xml FILE
	props.loadProperties(APP_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
        
        tempData = new CSGData(app);
        setCourseDetailsDataValues(tempData);
        setRecitationDataValues(tempData);
        setTADataValues(tempData);
        setScheduleDataValues(tempData);
        setProjectDataValues(tempData);

//        CSGData tempData = (CSGData) app.getDataComponent();
//        setCourseDetailsDataValues(tempData);
//        setRecitationDataValues(tempData);
//        setScheduleDataValues(tempData);
//        setProjectDataValues(tempData);

        files = new CSGFiles(app);
        files.saveData(tempData, "./work/SiteSaveTest.json"); //CALLS THE SAVADATA METHOD IN AppFileController so that it can save the data based on whats below.
    }
    public CSGFiles getFiles(){
        return files;
    }
    public CSGData getData(){
        return tempData;
    }
    public HashMap<String, StringProperty> getOfficeHourHash(){
        return officeHourHashMap;
    }
    
    public void setCourseDetailsDataValues(CSGData data){
       data.getCourseDetailsData().setSubject("CSE");
       data.getCourseDetailsData().setNumber("219");
       data.getCourseDetailsData().setSemester("Spring");
       data.getCourseDetailsData().setYear("2017");
       data.getCourseDetailsData().setTitle("Computer Science III");
       data.getCourseDetailsData().setInstructorName("McKenna");
       data.getCourseDetailsData().setInstructorHome("temporary\\url");
       data.getCourseDetailsData().setExportDir("temporary\\dir");
       data.getCourseDetailsData().setSiteTemplateDir("temporary\\directory");
       SitePages sitePage1 = new SitePages(true, "Home", "index.html", "HomeBuilder.js");
       SitePages sitePage2 = new SitePages(true, "Syllabus", "syllabus.html", "SyllabusBuilder.js");
       data.getCourseDetailsData().getSitePages().add(sitePage1);
       data.getCourseDetailsData().getSitePages().add(sitePage2);
       data.getCourseDetailsData().setBannerSchoolImage("image1.png");
       data.getCourseDetailsData().setLeftFooterImage("image2.png");
       data.getCourseDetailsData().setRightFooterImage("image3.jpg");
       data.getCourseDetailsData().setStylesheet("seawolf.css");
    }
    
    public void setRecitationDataValues(CSGData data){
        RecitationData recitationData = data.getRecitationData();
        Recitation recitation1 = new Recitation("01", "McKenna", "Wed 3:30 - 4:00pm", "Old CS 2114", "Joe Shmoe", "John Doe");
        Recitation recitation2 = new Recitation("02", "Banerjee", "Fri 5:00 - 6:00pm", "Old CS 2010", "Dank Meme", "Git");
        Recitation recitation3 = new Recitation("03", "McKenna", "Thu 12:00 - 1:00pm", "Old CS 2114", "Map", "Joe Shmoe");
        Recitation recitation4 = new Recitation("01", "McKenna", "Mon 10:30 - 11:00am", "Old CS 2114", "Joe Shmoe", "John Doe");
        recitationData.addRecitation(recitation1);
        recitationData.addRecitation(recitation2);
        recitationData.addRecitation(recitation3);
        recitationData.addRecitation(recitation4);
    }
    
    public void setTADataValues(CSGData data){  
        TAData taData = data.getTAData();
        taData.setHours(9, 10);
        officeHourHashMap = taData.getOfficeHours();
//        officeHourHashMap = new HashMap<>();
//        taData.setDataMap(officeHourHashMap);
        taData.addTA("Joe Shmoe", "joe@stonybrook.edu");
        taData.addTA("Dank Meme", "dank@stonybrook.edu");
        taData.addTA("Map", "map@stonybrook.edu");
        taData.getTA("Map").setUndergrad(false);
        taData.addTA("Git", "git@stonybrook.edu");
        taData.addTA("D", "d@stonybrook.edu");
        taData.getTA("D").setUndergrad(false);
        taData.addTA("E", "e@stonybrook.edu");
        taData.getTA("E").setUndergrad(false);
        taData.addTA("John Doe", "john@stonybrook.edu");
        taData.addTA("Gitlab", "gitlab@stonybrook.edu");
        taData.addTA("H", "h@stonybrook.edu");
        int numRows = taData.getNumRows();
        officeHourHashMap.put("0_1", new SimpleStringProperty("9:00am"));
        officeHourHashMap.put("1_1", new SimpleStringProperty("9:30am"));
        officeHourHashMap.put("0_2", new SimpleStringProperty("9:30am"));
        officeHourHashMap.put("1_2", new SimpleStringProperty("10:00am"));
        officeHourHashMap.put("0_3", new SimpleStringProperty("10:00am"));
        officeHourHashMap.put("1_3", new SimpleStringProperty("10:30am"));
        for(int rows = 1; rows < taData.getNumRows(); rows++ ){
            for(int col = 2; col < 7; col++){
                String cellKey = taData.getCellKey(col, rows);
                officeHourHashMap.put(cellKey, new SimpleStringProperty(""));
            }
        }
        officeHourHashMap.get("3_2").set("Wonje Kang");
        officeHourHashMap.get("2_2").set("Joe Shmoe");
        officeHourHashMap.get("2_1").set("Dank Meme");
        officeHourHashMap.get("2_1").set("Map");
        officeHourHashMap.get("5_2").set("Git");
        officeHourHashMap.get("6_1").set("John Doe");
        officeHourHashMap.get("3_1").set("Bitbucket");
        officeHourHashMap.get("5_2").set("Gitlab");
//        Label cellLabel = new Label();
        
////        HashMap<String, StringProperty> officeHours = taData.getOfficeHours();
//        String cellKey = taData.getCellKey(2, 3);
//        cellLabel.setId(cellKey);
//        taData.setCellProperty(2, 3, cellLabel.textProperty());
//        taData.addOfficeHoursReservation("MONDAY", "10_00am", "Wongje Kang");
//       
    }
    
    public void setScheduleDataValues(CSGData data) throws ParseException{
        ScheduleData scheduleData = data.getScheduleData();
        String startingMondayString = "04/10/2017";
        String endingFridayString = "05/12/2017";
        String scheduleItem1DateString = "04/17/2017";
        String scheduleItem2DateString = "04/18/2017";
        String scheduleItem3DateString = "04/19/2017";
        String scheduleItem4DateString = "05/02/2017";
        String scheduleItem5DateString = "05/03/2017";
        String scheduleItem6DateString = "05/04/2017";
        scheduleData.setStartingMonday(startingMondayString);
        scheduleData.setEndingFriday(endingFridayString);
//        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//        Date startingMonday = format.parse(startingMondayString);
//        Date endingFriday = format.parse(endingFridayString);
//        scheduleData.setStartingMonday(startingMonday);
//        scheduleData.setEndingFriday(endingFriday);
//        Date scheduleItem1Date = format.parse(scheduleItem1DateString);
        ScheduleItem scheduleItem1 = new ScheduleItem("Holiday", scheduleItem1DateString , "1:00pm", "SNOW DAY", " ", "somelink.com/stuff", "Criteria1");
        ScheduleItem scheduleItem2 = new ScheduleItem("Lecture", scheduleItem2DateString , "2:00pm", "Lecture 3", "Event Programming", "somelink.com/stuff", "Criteria2");
        ScheduleItem scheduleItem3 = new ScheduleItem("Lecture", scheduleItem3DateString , "10:00am", "Lecture 4", "JAVAFX", "homepage.com", "Criteria3");
        ScheduleItem scheduleItem4 = new ScheduleItem("Reference", scheduleItem4DateString , "9:00pm", "Reference", "CSS and HTML", "homepage.co.uk", "Criteria4");
        ScheduleItem scheduleItem5 = new ScheduleItem("Recitation", scheduleItem5DateString , "1:00pm", "Recitation 1", "Unit Testing", "somelink.co.uk", "Criteria5");
        ScheduleItem scheduleItem6 = new ScheduleItem("Homework", scheduleItem6DateString , "11:00pm", "HW 3", "due @ 11:00pm", "somelink.com/stuff", "Criteria6");
        scheduleData.addScheduleItem(scheduleItem1);
        scheduleData.addScheduleItem(scheduleItem2);
        scheduleData.addScheduleItem(scheduleItem3);
        scheduleData.addScheduleItem(scheduleItem4);
        scheduleData.addScheduleItem(scheduleItem5);
        scheduleData.addScheduleItem(scheduleItem6);
    }
    
    public void setProjectDataValues(CSGData data){
        ProjectData projectData = data.getProjectData();
        Team team1 = new Team("Atomic Comics", "#552211", "#ffffff", "atomiccomic.com");
        Team team2 = new Team("C4 Comics", "#341256", "#ffffff", "c4comics.com");
        Team team3 = new Team("Instant Doodles", "#781290", "#ffffff", "doodles.com");
        Team team4 = new Team("Sharpeez Comics", "#119911", "#000000", "sharpie.com");
        Student student1 = new Student("Beau", "Bummell", "Atomic Comics", "Lead Designer");
        Student student2 = new Student("Alan", "Thomas", "Atomic Comics", "Lead Programmer");
        Student student3 = new Student("Anthony", "Musco", "Atomic Comics", "Project Manager");
        Student student4 = new Student("C", "CZDBN", "Atomic Comics", "Data Designer");
        
        Student student5 = new Student("Jose", "Rodriguez", "C4 Comics", "Data Designer");
        Student student6 = new Student("Elvis", "Fernandez", "C4 Comics", "Lead Designer");
        Student student7 = new Student("DASD", "ASDasdfd", "C4 Comics", "Project Manager");
        Student student8 = new Student("REAREEE", "Fhdfsasdj", "C4 Comics", "Lead Programmer");
        
        Student student9 = new Student("Stanley", "Chen", "Instant Doodles", "Lead Programmer");
        Student student10 = new Student("Kwun", "Chan", "Instant Doodles", "Lead Designer");
        Student student11 = new Student("Ryan", "Gopfert", "Instant Doodles", "Project Manager");
        Student student12 = new Student("Chris", "Fenton", "Instant Doodles", "Data Designer");
        
        Student student13 = new Student("Yongcong", "Lei", "Sharpeez Comics", "Lead Designer");
        Student student14 = new Student("RETJHGF", "Dsfdsgb", "Sharpeez Comics", "Data Designer");
        Student student15 = new Student("Matthew", "Monsur", "Sharpeez Comics", "Project Manager");
        Student student16 = new Student("Wei Bin", "Wang", "Sharpeez Comics", "Lead Programmer");
        projectData.addStudent(student1);
        projectData.addStudent(student2);
        projectData.addStudent(student3);
        projectData.addStudent(student4);
        projectData.addStudent(student5);
        projectData.addStudent(student6);
        projectData.addStudent(student7);
        projectData.addStudent(student8);
        projectData.addStudent(student9);
        projectData.addStudent(student10);
        projectData.addStudent(student11);
        projectData.addStudent(student12);
        projectData.addStudent(student13);
        projectData.addStudent(student14);
        projectData.addStudent(student15);
        projectData.addStudent(student16);
        projectData.addTeam(team1);
        projectData.addTeam(team2);
        projectData.addTeam(team3);
        projectData.addTeam(team4);
    }
}
