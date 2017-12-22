package csg.testbed;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.CourseDetailsData;
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
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew
 */
public class TestLoad {
    TestSave testsave;
    CSGFiles files;
    CSGData tempData;
    public TestLoad() throws Exception {
        testsave = new TestSave();
        files = testsave.getFiles();
        tempData = testsave.getData();
        files.loadDataTestingMethod(tempData, "./work/SiteSaveTest.json");
    }

    @Test
    public void loadDataTest() throws ParseException{ //COMPARE EXPECTED VALUES WHICH ARE HARD CODED TO THE LOADED VALUES FROM THE SAVED JSON FILE.
        CSGApp appExpected = null;
        
        /////////////////////////////////////////////////////////COURSEDETAILS LOAD CHECK///////////////////////////////////////////////////////////////////////
        CSGData expectedData = new CSGData(appExpected);
        CourseDetailsData courseDetailsExpected = expectedData.getCourseDetailsData();
        courseDetailsExpected.setSubject("CSE");
        courseDetailsExpected.setNumber("219");
        courseDetailsExpected.setSemester("Spring");
        courseDetailsExpected.setYear("2017");
        courseDetailsExpected.setTitle("Computer Science III");
        courseDetailsExpected.setInstructorName("McKenna");
        courseDetailsExpected.setInstructorHome("temporary\\url");
        courseDetailsExpected.setExportDir("temporary\\dir");
        courseDetailsExpected.setSiteTemplateDir("temporary\\directory");
        SitePages sitePage1 = new SitePages(true, "Home", "index.html", "HomeBuilder.js");
        SitePages sitePage2 = new SitePages(true, "Syllabus", "syllabus.html", "SyllabusBuilder.js");
        courseDetailsExpected.getSitePages().addAll(sitePage1, sitePage2);
        courseDetailsExpected.setBannerSchoolImage("image1.png");
        courseDetailsExpected.setLeftFooterImage("image2.png");
        courseDetailsExpected.setRightFooterImage("image3.jpg");
        courseDetailsExpected.setStylesheet("seawolf.css");
        CourseDetailsData courseDetailsLoaded = tempData.getCourseDetailsData();
        assertEquals(courseDetailsExpected.getSubject(), courseDetailsLoaded.getSubject());
        assertEquals(courseDetailsExpected.getNumber(), courseDetailsLoaded.getNumber());
        assertEquals(courseDetailsExpected.getSemester(), courseDetailsLoaded.getSemester());
        assertEquals(courseDetailsExpected.getYear(), courseDetailsLoaded.getYear());
        assertEquals(courseDetailsExpected.getTitle(), courseDetailsLoaded.getTitle());
        assertEquals(courseDetailsExpected.getInstructorName(), courseDetailsLoaded.getInstructorName());
        assertEquals(courseDetailsExpected.getInstructorHome(), courseDetailsLoaded.getInstructorHome());
        assertEquals(courseDetailsExpected.getExportDir(), courseDetailsLoaded.getExportDir());
        assertEquals(courseDetailsExpected.getSiteTemplateDir(), courseDetailsLoaded.getSiteTemplateDir());
        for(int i = 0; i < courseDetailsExpected.getSitePages().size(); i++){
            assertEquals(courseDetailsExpected.getSitePages().get(i).getUse(), courseDetailsLoaded.getSitePages().get(i).getUse());
            assertEquals(courseDetailsExpected.getSitePages().get(i).getNavbarTitle(), courseDetailsLoaded.getSitePages().get(i).getNavbarTitle());
            assertEquals(courseDetailsExpected.getSitePages().get(i).getFileName(), courseDetailsLoaded.getSitePages().get(i).getFileName());
            assertEquals(courseDetailsExpected.getSitePages().get(i).getScript(), courseDetailsLoaded.getSitePages().get(i).getScript());
        }
        assertEquals(courseDetailsExpected.getBannerSchoolImage(), courseDetailsLoaded.getBannerSchoolImage());
        assertEquals(courseDetailsExpected.getLeftFooterImage(), courseDetailsLoaded.getLeftFooterImage());
        assertEquals(courseDetailsExpected.getRightFooterImage(), courseDetailsLoaded.getRightFooterImage());
        assertEquals(courseDetailsExpected.getStylesheet(), courseDetailsLoaded.getStylesheet());
//////////////////////////////////////////////////////////////TADATA LOAD CHECK///////////////////////////////////////////////////////////////        
        TAData taData = tempData.getTAData();
        TAData taDataExpected = expectedData.getTAData();
        taDataExpected.setHours(9, 10);
        HashMap<String, StringProperty> officeHourHashMapExpected = new HashMap<>();
        taDataExpected.setDataMap(officeHourHashMapExpected);
        taDataExpected.addTA("Joe Shmoe", "joe@stonybrook.edu");
        taDataExpected.addTA("Dank Meme", "dank@stonybrook.edu");
        taDataExpected.addTA("Map", "map@stonybrook.edu");
        taDataExpected.getTA("Map").setUndergrad(false);
        taDataExpected.addTA("Git", "git@stonybrook.edu");
        taDataExpected.addTA("D", "d@stonybrook.edu");
        taDataExpected.getTA("D").setUndergrad(false);
        taDataExpected.addTA("E", "e@stonybrook.edu");
        taDataExpected.getTA("E").setUndergrad(false);
        taDataExpected.addTA("John Doe", "john@stonybrook.edu");
        taDataExpected.addTA("Gitlab", "gitlab@stonybrook.edu");
        taDataExpected.addTA("H", "h@stonybrook.edu");
        int numRows = taDataExpected.getNumRows();
        officeHourHashMapExpected.put("0_1", new SimpleStringProperty("9:00am"));
        officeHourHashMapExpected.put("1_1", new SimpleStringProperty("9:30am"));
        officeHourHashMapExpected.put("0_2", new SimpleStringProperty("9:30am"));
        officeHourHashMapExpected.put("1_2", new SimpleStringProperty("10:00am"));
        officeHourHashMapExpected.put("0_3", new SimpleStringProperty("10:00am"));
        officeHourHashMapExpected.put("1_3", new SimpleStringProperty("10:30am"));
        for(int rows = 1; rows < taDataExpected.getNumRows(); rows++ ){
            for(int col = 2; col < 7; col++){
                String cellKey = taDataExpected.getCellKey(col, rows);
                officeHourHashMapExpected.put(cellKey, new SimpleStringProperty(""));
            }
        }
        officeHourHashMapExpected.get("3_2").set("Wonje Kang");
        officeHourHashMapExpected.get("2_2").set("Joe Shmoe");
        officeHourHashMapExpected.get("2_1").set("Dank Meme");
        officeHourHashMapExpected.get("2_1").set("Map");
        officeHourHashMapExpected.get("5_2").set("Git");
        officeHourHashMapExpected.get("6_1").set("John Doe");
        officeHourHashMapExpected.get("3_1").set("Bitbucket");
        officeHourHashMapExpected.get("5_2").set("Gitlab");
        assertEquals(taDataExpected.getStartHour(), taData.getStartHour());
        assertEquals(taDataExpected.getEndHour(), taData.getEndHour());
        for(int rows = 1; rows < taDataExpected.getNumRows(); rows++){
            for(int col = 2; col < 7; col++){
               String cellKey = taDataExpected.getCellKey(col, rows);
               assertEquals(officeHourHashMapExpected.get(cellKey).get(), taData.getOfficeHours().get(cellKey).get());
            }
        }
/////////////////////////////////////////////RECITATION DATA LOAD CHECK////////////////////////////////////////////////////////////////////////////
        RecitationData recitationData = tempData.getRecitationData();
        RecitationData recDataExpected = expectedData.getRecitationData();
        Recitation recitation1 = new Recitation("01", "McKenna", "Wed 3:30 - 4:00pm", "Old CS 2114", "Joe Shmoe", "John Doe");
        Recitation recitation2 = new Recitation("02", "Banerjee", "Fri 5:00 - 6:00pm", "Old CS 2010", "Dank Meme", "Git");
        Recitation recitation3 = new Recitation("03", "McKenna", "Thu 12:00 - 1:00pm", "Old CS 2114", "Map", "Joe Shmoe");
        Recitation recitation4 = new Recitation("01", "McKenna", "Mon 10:30 - 11:00am", "Old CS 2114", "Joe Shmoe", "John Doe");
        recDataExpected.addRecitation(recitation1);
        recDataExpected.addRecitation(recitation2);
        recDataExpected.addRecitation(recitation3);
        recDataExpected.addRecitation(recitation4);
        for(int i = 0; i < recDataExpected.getRecitations().size(); i++){
            assertEquals(recDataExpected.getRecitations().get(i).getSection(), recitationData.getRecitations().get(i).getSection());
            assertEquals(recDataExpected.getRecitations().get(i).getInstructor(), recitationData.getRecitations().get(i).getInstructor());
            assertEquals(recDataExpected.getRecitations().get(i).getDayTime(), recitationData.getRecitations().get(i).getDayTime());
            assertEquals(recDataExpected.getRecitations().get(i).getLocation(), recitationData.getRecitations().get(i).getLocation());
            assertEquals(recDataExpected.getRecitations().get(i).getFirstTA(), recitationData.getRecitations().get(i).getFirstTA());
            assertEquals(recDataExpected.getRecitations().get(i).getSecondTA(), recitationData.getRecitations().get(i).getSecondTA());
        }
        
//////////////////////////////////////////SCHEDULE DATA LOAD CHECK///////////////////////////////////////////////////////////////////////////////
        ScheduleData scheduleData = tempData.getScheduleData();
        ScheduleData scheduleDataExpected = expectedData.getScheduleData();
        String startingMondayString = "04/10/2017";
        String endingFridayString = "05/12/2017";
        String scheduleItem1DateString = "04/17/2017";
        String scheduleItem2DateString = "04/18/2017";
        String scheduleItem3DateString = "04/19/2017";
        String scheduleItem4DateString = "05/02/2017";
        String scheduleItem5DateString = "05/03/2017";
        String scheduleItem6DateString = "05/04/2017";
//        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//        Date startingMonday = format.parse(startingMondayString);
//        Date endingFriday = format.parse(endingFridayString);
        scheduleDataExpected.setStartingMonday(startingMondayString);
        scheduleDataExpected.setEndingFriday(endingFridayString);
//        Date scheduleItem1Date = format.parse(scheduleItem1DateString);
        ScheduleItem scheduleItem1 = new ScheduleItem("Holiday", scheduleItem1DateString , "1:00pm", "SNOW DAY", " ", "somelink.com/stuff", "Criteria1");
        ScheduleItem scheduleItem2 = new ScheduleItem("Lecture", scheduleItem2DateString , "2:00pm", "Lecture 3", "Event Programming", "somelink.com/stuff", "Criteria2");
        ScheduleItem scheduleItem3 = new ScheduleItem("Lecture", scheduleItem3DateString , "10:00am", "Lecture 4", "JAVAFX", "homepage.com", "Criteria3");
        ScheduleItem scheduleItem4 = new ScheduleItem("Reference", scheduleItem4DateString , "9:00pm", "Reference", "CSS and HTML", "homepage.co.uk", "Criteria4");
        ScheduleItem scheduleItem5 = new ScheduleItem("Recitation", scheduleItem5DateString , "1:00pm", "Recitation 1", "Unit Testing", "somelink.co.uk", "Criteria5");
        ScheduleItem scheduleItem6 = new ScheduleItem("Homework", scheduleItem6DateString , "11:00pm", "HW 3", "due @ 11:00pm", "somelink.com/stuff", "Criteria6");
        scheduleDataExpected.addScheduleItem(scheduleItem1);
        scheduleDataExpected.addScheduleItem(scheduleItem2);
        scheduleDataExpected.addScheduleItem(scheduleItem3);
        scheduleDataExpected.addScheduleItem(scheduleItem4);
        scheduleDataExpected.addScheduleItem(scheduleItem5);
        scheduleDataExpected.addScheduleItem(scheduleItem6);
        
        
        for(int i = 0; i < scheduleDataExpected.getScheduleItems().size(); i++){
            assertEquals(scheduleDataExpected.getScheduleItems().get(i).getType(), scheduleData.getScheduleItems().get(i).getType());
            assertEquals(scheduleDataExpected.getScheduleItems().get(i).getDateString(), scheduleData.getScheduleItems().get(i).getDateString());
            assertEquals(scheduleDataExpected.getScheduleItems().get(i).getTime(), scheduleData.getScheduleItems().get(i).getTime());
            assertEquals(scheduleDataExpected.getScheduleItems().get(i).getTitle(), scheduleData.getScheduleItems().get(i).getTitle());
            assertEquals(scheduleDataExpected.getScheduleItems().get(i).getTopic(), scheduleData.getScheduleItems().get(i).getTopic());
            assertEquals(scheduleDataExpected.getScheduleItems().get(i).getLink(), scheduleData.getScheduleItems().get(i).getLink());
            assertEquals(scheduleDataExpected.getScheduleItems().get(i).getCriteria(), scheduleData.getScheduleItems().get(i).getCriteria());
        }
/////////////////////////////////////////PROJECT DATA LOAD CHECK//////////////////////////////////////////////////////////////////////////
        ProjectData projectData = tempData.getProjectData();
        ProjectData projectDataExpected = expectedData.getProjectData();
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
        projectDataExpected.addStudent(student1);
        projectDataExpected.addStudent(student2);
        projectDataExpected.addStudent(student3);
        projectDataExpected.addStudent(student4);
        projectDataExpected.addStudent(student5);
        projectDataExpected.addStudent(student6);
        projectDataExpected.addStudent(student7);
        projectDataExpected.addStudent(student8);
        projectDataExpected.addStudent(student9);
        projectDataExpected.addStudent(student10);
        projectDataExpected.addStudent(student11);
        projectDataExpected.addStudent(student12);
        projectDataExpected.addStudent(student13);
        projectDataExpected.addStudent(student14);
        projectDataExpected.addStudent(student15);
        projectDataExpected.addStudent(student16);
        projectDataExpected.addTeam(team1);
        projectDataExpected.addTeam(team2);
        projectDataExpected.addTeam(team3);
        projectDataExpected.addTeam(team4);
        
        for(int i = 0; i < projectDataExpected.getTeams().size(); i++){
            assertEquals(projectDataExpected.getTeams().get(i).getTeamName(), projectData.getTeams().get(i).getTeamName());
            assertEquals(projectDataExpected.getTeams().get(i).getTeamColor(), projectData.getTeams().get(i).getTeamColor());
            assertEquals(projectDataExpected.getTeams().get(i).getTextColor(), projectData.getTeams().get(i).getTextColor());
            assertEquals(projectDataExpected.getTeams().get(i).getLink(), projectData.getTeams().get(i).getLink());  
        }
        
        for(int i = 0; i < projectDataExpected.getStudents().size(); i++){
            assertEquals(projectDataExpected.getStudents().get(i).getFirstName(), projectData.getStudents().get(i).getFirstName());
            assertEquals(projectDataExpected.getStudents().get(i).getLastName(), projectData.getStudents().get(i).getLastName());
            assertEquals(projectDataExpected.getStudents().get(i).getTeam(), projectData.getStudents().get(i).getTeam());
            assertEquals(projectDataExpected.getStudents().get(i).getRole(), projectData.getStudents().get(i).getRole());
        }
    }
}
