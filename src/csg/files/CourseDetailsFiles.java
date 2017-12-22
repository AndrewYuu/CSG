/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.files;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.CourseDetailsData;
import csg.data.SitePages;
import csg.data.TeachingAssistant;
import static csg.files.TAFiles.JSON_DAY;
import djf.components.AppDataComponent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author Andrew
 */
public class CourseDetailsFiles {
    CSGApp app;
    JsonObject courseDetailsJson;
    JsonObject exportCourseDetailJSON;
    static final String JSON_SUBJECT= "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_INSTRUCTOR_NAME = "instructor_name";
    static final String JSON_INSTRUCTOR_HOME= "instructor_home";
    static final String JSON_EXPORT_DIR = "export_dir";
    static final String JSON_SITE_TEMPLATE_DIR = "site_template_dir";
    static final String JSON_SITE_PAGES = "site_pages";
        static final String JSON_USE = "use";
        static final String JSON_NAVBAR_TITLE = "narbar_title";
        static final String JSON_FILE_NAME = "file_name";
        static final String JSON_SCRIPT = "script";
    static final String JSON_BANNER_SCHOOL_IMAGE = "banner_school_image";
    static final String JSON_LEFT_FOOTER_IMAGE = "left_footer_image";
    static final String JSON_RIGHT_FOOTER_IMAGE = "right_footer_image";
    static final String JSON_STYLESHEET = "stylesheet";
    
    public CourseDetailsFiles(CSGApp initApp){
        app = initApp;
    }
        
    public JsonObject getCourseDetailsJson(){
        return courseDetailsJson;
    }
    
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        //dataManager IS CSGDATA. THEREFORE dataManager.getCourseDetailsData() WILL GIVE THE CourseDetailsDATA PORTION.
        CSGData dataManager = (CSGData)data;
        CourseDetailsData courseDetailsData = dataManager.getCourseDetailsData();

        // NOW BUILD THE SITE PAGES JSON OBJCTS (ARRAY)TO SAVE
	JsonArrayBuilder sitePagesArrayBuilder = Json.createArrayBuilder();
	ObservableList<SitePages> sitePages = courseDetailsData.getSitePages();
	for (SitePages sp : sitePages) {
            String isUse;
            if(sp.isUseProperty().get()){
                isUse = "true";
            }
            else{
                isUse = "false";
            }
	    JsonObject spJson = Json.createObjectBuilder()
		    .add(JSON_USE, isUse)
		    .add(JSON_NAVBAR_TITLE, sp.getNavbarTitle())
		    .add(JSON_FILE_NAME, sp.getFileName())
                    .add(JSON_SCRIPT, sp.getScript()).build();
	    sitePagesArrayBuilder.add(spJson);
	}
	JsonArray sitePagesArray = sitePagesArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
        courseDetailsJson = Json.createObjectBuilder()
                .add(JSON_SUBJECT, courseDetailsData.getSubject())
                .add(JSON_NUMBER, courseDetailsData.getNumber())
                .add(JSON_SEMESTER, courseDetailsData.getSemester())
                .add(JSON_YEAR, courseDetailsData.getYear())
                .add(JSON_TITLE, courseDetailsData.getTitle())
                .add(JSON_INSTRUCTOR_NAME, courseDetailsData.getInstructorName())
                .add(JSON_INSTRUCTOR_HOME, courseDetailsData.getInstructorHome())
                .add(JSON_EXPORT_DIR, courseDetailsData.getExportDir())
                .add(JSON_SITE_TEMPLATE_DIR, courseDetailsData.getSiteTemplateDir())
                .add(JSON_SITE_PAGES, sitePagesArray)
                .add(JSON_BANNER_SCHOOL_IMAGE, courseDetailsData.getBannerSchoolImage())
                .add(JSON_LEFT_FOOTER_IMAGE, courseDetailsData.getLeftFooterImage())
                .add(JSON_RIGHT_FOOTER_IMAGE, courseDetailsData.getRightFooterImage())
                .add(JSON_STYLESHEET, courseDetailsData.getStylesheet()).build();
	
    }

    public void loadData(AppDataComponent data, String filePath) throws IOException {
        CSGData dataManager = (CSGData)data;
        CourseDetailsData courseDetailsData = dataManager.getCourseDetailsData();
        
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonObject courseDetailsJson = json.getJsonObject("courseDetailsData");
        String subject = courseDetailsJson.getString(JSON_SUBJECT);
        String number = courseDetailsJson.getString(JSON_NUMBER);
        String semester = courseDetailsJson.getString(JSON_SEMESTER);
        String year = courseDetailsJson.getString(JSON_YEAR);
        String title = courseDetailsJson.getString(JSON_TITLE);
        String instructorName = courseDetailsJson.getString(JSON_INSTRUCTOR_NAME);
        String instructorHome = courseDetailsJson.getString(JSON_INSTRUCTOR_HOME);
        String exportDir = courseDetailsJson.getString(JSON_EXPORT_DIR);
        String siteTemplateDir = courseDetailsJson.getString(JSON_SITE_TEMPLATE_DIR);
        String bannerSchoolImage = courseDetailsJson.getString(JSON_BANNER_SCHOOL_IMAGE);
        String leftFooterImage = courseDetailsJson.getString(JSON_LEFT_FOOTER_IMAGE);
        String rightFooterImage = courseDetailsJson.getString(JSON_RIGHT_FOOTER_IMAGE);
        String stylesheet = courseDetailsJson.getString(JSON_STYLESHEET);
        
        
        courseDetailsData.setSubject(subject);
        courseDetailsData.setNumber(number);
        courseDetailsData.setSemester(semester);
        courseDetailsData.setYear(year);
        courseDetailsData.setTitle(title);
        courseDetailsData.setInstructorName(instructorName);
        courseDetailsData.setInstructorHome(instructorHome);
        courseDetailsData.setExportDir(exportDir);
        courseDetailsData.setSiteTemplateDir(siteTemplateDir);
        
         //LOAD THE ARRAY OF SITE PAGES
        JsonArray sitePagesArray = courseDetailsJson.getJsonArray(JSON_SITE_PAGES);
        for(int i = 0; i < sitePagesArray.size(); i++){
            JsonObject sitePagesJson = sitePagesArray.getJsonObject(i);
            String use = sitePagesJson.getString(JSON_USE);
            boolean isUse;
            if(use.equals("true")){
                isUse = true;
            }
            else{
                isUse = false;
            }
            String navbarTitle = sitePagesJson.getString(JSON_NAVBAR_TITLE);
            String fileName = sitePagesJson.getString(JSON_FILE_NAME);
            String script = sitePagesJson.getString(JSON_SCRIPT);
            SitePages sitepage = new SitePages(isUse, navbarTitle, fileName, script);
            courseDetailsData.getSitePages().add(sitepage);
        }
        
       courseDetailsData.setBannerSchoolImage(bannerSchoolImage);
       courseDetailsData.setLeftFooterImage(leftFooterImage);
       courseDetailsData.setRightFooterImage(rightFooterImage);
       courseDetailsData.setStylesheet(stylesheet);
        
    }
    
     // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        loadDataExporting(data);
         
        filePath = filePath + "/CourseDetails.json";
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(exportCourseDetailJSON);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(exportCourseDetailJSON);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
    }
    
    public void loadDataExporting(AppDataComponent data){
        CSGData dataManager = (CSGData) data;
        CourseDetailsData courseDetailsData = dataManager.getCourseDetailsData();
        
        String subjectKey = "subject";
        String numberKey = "number";
        String semesterKey = "semester";
        String yearKey = "year";
        String titleKey = "title";
        String instrNameKey = "instructor_name";
        String instrHomeKey = "instructor_home";
        String bannerKey = "banner_school_image";
        String leftKey = "left_footer_image";
        String rightKey = "right_footer_image";
        String ssKey = "stylesheet";
        
        
        String subject = courseDetailsData.getSubject();
        String number = courseDetailsData.getNumber();
        String semester = courseDetailsData.getSemester();
        String year = courseDetailsData.getYear();
        String title = courseDetailsData.getTitle();
        String instrName = courseDetailsData.getInstructorName();
        String instrHome = courseDetailsData.getInstructorHome();
        String banner = courseDetailsData.getBannerSchoolImage();
        String exportBanner = "./images/" + banner.replaceFirst(".*/([^/?]+).*", "$1");
        String left = courseDetailsData.getLeftFooterImage();
        String exportLeft = "./images/" + left.replaceFirst(".*/([^/?]+).*", "$1");
        String right = courseDetailsData.getRightFooterImage();
        String exportRight = "./images/" + right.replaceFirst(".*/([^/?]+).*", "$1");
        String exportStyle = "./css/" + courseDetailsData.getStylesheet();
                
        exportCourseDetailJSON = Json.createObjectBuilder()
                .add(subjectKey, subject)
                .add(numberKey, number)
                .add(semesterKey, semester)
                .add(yearKey, year)
                .add(titleKey, title)
                .add(instrNameKey, instrName)
                .add(instrHomeKey, instrHome)
                .add(bannerKey, exportBanner)
                .add(leftKey, exportLeft)
                .add(rightKey, exportRight)
                .add(ssKey, exportStyle)
                .build();

    }
}