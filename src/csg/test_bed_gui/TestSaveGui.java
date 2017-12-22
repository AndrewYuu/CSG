/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.test_bed_gui;

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
import static djf.settings.AppStartupConstants.APP_PROPERTIES_FILE_NAME;
import static djf.settings.AppStartupConstants.PATH_DATA;
import static djf.settings.AppStartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import java.text.ParseException;
import java.util.HashMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class TestSaveGui {
    CSGApp app;


    HashMap<String, StringProperty> officeHourHashMap;
    public TestSaveGui(CSGApp initApp) throws Exception{

///////////////////RUN WITH APP WITH TESTLOAD.JAVA CLASS, SO SET APP = NULL. HOWEVER, IT STILL USES THE SAVE AND LOAD METHODS IN THE CSG FILES PACKAGE.
        app = initApp;
        CSGData tempData = (CSGData) app.getDataComponent();
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

//        files = new CSGFiles(app);
        CSGFiles files = (CSGFiles) app.getFileComponent();
        files.saveData(tempData, "./work/testfile.json"); //CALLS THE SAVADATA METHOD IN AppFileController so that it can save the data based on whats below.
    }
//    public CSGFiles getFiles(){
//        return files;
//    }
//    public CSGData getData(){
//        return tempData;
//    }
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
       data.getCourseDetailsData().setRightFooterImage("image3.png");
       data.getCourseDetailsData().setStylesheet("seawolf.css");
    }
    
    public void setRecitationDataValues(CSGData data){
        RecitationData recitationData = data.getRecitationData();
        Recitation recitation1 = new Recitation("01", "McKenna", "Wed 3:30 - 4:00pm", "Old CS 2114", "Joe Shmoe", "John Doe");
        recitationData.addRecitation(recitation1);
    }
    
    public void setTADataValues(CSGData data){  
        TAData taData = data.getTAData();
        taData.setHours(9, 10);
        officeHourHashMap = new HashMap<>();
        taData.setDataMap(officeHourHashMap);
        taData.addTA("Joe Shmoe", "joe@stonybrook.edu");
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
        String startingMondayString = "04/20/2017";
        String endingFridayString = "05/20/2017";
        String scheduleItem1DateString = "06/20/2017";
        scheduleData.setStartingMonday(startingMondayString);
        scheduleData.setEndingFriday(endingFridayString);
//        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//        Date startingMonday = format.parse(startingMondayString);
//        Date endingFriday = format.parse(endingFridayString);
//        scheduleData.setStartingMonday(startingMonday);
//        scheduleData.setEndingFriday(endingFriday);
//        Date scheduleItem1Date = format.parse(scheduleItem1DateString);
        ScheduleItem scheduleItem1 = new ScheduleItem("Holiday", scheduleItem1DateString , "1:00pm", "Snow Day", " ", "somelink.com/stuff", "Criteria1");
        scheduleData.addScheduleItem(scheduleItem1);
    }
    
    public void setProjectDataValues(CSGData data){
        ProjectData projectData = data.getProjectData();
        Team team1 = new Team("Atomic Comics", "552211", "ffffff", "atomiccomic.com");
        Student student1 = new Student("Beau", "Bummell", "Atomic Comics", "Lead Designer");
        projectData.addStudent(student1);
        projectData.addTeam(team1);
    }
}
