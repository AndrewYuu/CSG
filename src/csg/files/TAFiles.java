package csg.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import static djf.settings.AppStartupConstants.PATH_WORK;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jtps.jTPS;
import csg.CSGApp;
import csg.data.CSGData;
import csg.data.TeachingAssistant;
import csg.workspace.CSGWorkspace;
import java.math.BigDecimal;
import javafx.beans.property.StringProperty;
import jtps.jTPS_Projects;
import org.apache.commons.io.FileUtils;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class TAFiles{
    // THIS IS THE APP ITSELF
    CSGApp app;
    JsonObject dataManagerJSO;
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_DAY = "day";
    static final String JSON_TIME = "time";
    static final String JSON_NAME = "name";
    static final String JSON_UNDERGRAD = "undergrad";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_EMAIL = "email";
    public TAFiles(CSGApp initApp) {
        app = initApp;
    }

    public void loadData(AppDataComponent data, String filePath) throws IOException {
//dataManager IS CSGDATA. THEREFORE dataManager.getTAData() WILL GIVE THE TADATA PORTION.
	// CLEAR THE OLD DATA OUT
	CSGData dataManager = (CSGData)data;

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonObject taJson = json.getJsonObject("taData");
	// LOAD THE START AND END HOURS
	String startHour = taJson.getString(JSON_START_HOUR);
        String endHour = taJson.getString(JSON_END_HOUR);
        dataManager.getTAData().initHours(startHour, endHour);

        // NOW RELOAD THE WORKSPACE WITH THE LOADED DATA
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        workspace.reloadWorkspaceTA(app.getDataComponent());
        
        jTPS transactionComponent = workspace.getTransactionComponent();
        transactionComponent.clearTransactions();
        
        // NOW LOAD ALL THE UNDERGRAD TAs
        JsonArray jsonTAArray = taJson.getJsonArray(JSON_UNDERGRAD_TAS);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            boolean isUndergrad = true;
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            String undergrad = jsonTA.getString(JSON_UNDERGRAD);
            if(undergrad.equals("true")){
                isUndergrad = true;
            }
            else{
                isUndergrad = false;
            }
            dataManager.getTAData().addTA(name, email);
            dataManager.getTAData().getTA(name).setUndergrad(isUndergrad);
        }

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = taJson.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String day = jsonOfficeHours.getString(JSON_DAY);
            String time = jsonOfficeHours.getString(JSON_TIME);
            String name = jsonOfficeHours.getString(JSON_NAME);
            dataManager.getTAData().addOfficeHoursReservation(day, time, name); 
        }
        
//        workspace.setSupervisingTAChoicesAll(dataManager.getTAData().getTeachingAssistants());
//        dataManager.getTAData().addTAWithoutConstruct(new TeachingAssistant("", ""));
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


    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	CSGData dataManager = (CSGData)data;

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
	ObservableList<TeachingAssistant> tas = dataManager.getTAData().getTeachingAssistants();
	for (TeachingAssistant ta : tas) {
            String isUndergrad;
            if(ta.isUndergrad().get()){
                isUndergrad = "true";
            }
            else{
                isUndergrad = "false";
            }
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName()).add(JSON_EMAIL, ta.getEmail()).add(JSON_UNDERGRAD, isUndergrad).build();
	    taArrayBuilder.add(taJson);
	}
	JsonArray undergradTAsArray = taArrayBuilder.build();

	// NOW BUILD THE TIME SLOT JSON OBJCTS TO SAVE
	JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
	ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(dataManager);
	for (TimeSlot ts : officeHours) {	    
	    JsonObject tsJson = Json.createObjectBuilder()
		    .add(JSON_DAY, ts.getDay())
		    .add(JSON_TIME, ts.getTime())
		    .add(JSON_NAME, ts.getName()).build();
	    timeSlotArrayBuilder.add(tsJson);
	}
	JsonArray timeSlotsArray = timeSlotArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getTAData().getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getTAData().getEndHour())
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray)
		.build();
	
//	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
//	Map<String, Object> properties = new HashMap<>(1);
//	properties.put(JsonGenerator.PRETTY_PRINTING, true);
//	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
//	StringWriter sw = new StringWriter();
//	JsonWriter jsonWriter = writerFactory.createWriter(sw);
//	jsonWriter.writeObject(dataManagerJSO);
//	jsonWriter.close();
//
//	// INIT THE WRITER
//	OutputStream os = new FileOutputStream(filePath);
//	JsonWriter jsonFileWriter = Json.createWriter(os);
//	jsonFileWriter.writeObject(dataManagerJSO);
//	String prettyPrinted = sw.toString();
//	PrintWriter pw = new PrintWriter(filePath);
//	pw.write(prettyPrinted);
//	pw.close();
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    public JsonObject getTAFilesJson(){
        return dataManagerJSO;
    }
    
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public void exportData(AppDataComponent data, String filePath) throws IOException {
////        String source = "./TAManagerTester/public_html"; //GET THE ORIGINAL FILE DIRECTORY TO COPY
//        File sourceDirectory = new File(PATH_WORK);
//        //System.out.print(sourceDirectory.getAbsolutePath());
//        File sourceDirectoryParent = sourceDirectory.getParentFile();
//        //System.out.print(sourceDirectoryParent.getAbsolutePath());
//        sourceDirectoryParent = sourceDirectoryParent.getAbsoluteFile();
//        
//        File sourceDirectoryGrandParent = sourceDirectoryParent.getParentFile();
//        sourceDirectoryGrandParent = sourceDirectoryGrandParent.getAbsoluteFile();
//        
//        File sourceDirectoryGreatGrandParent = sourceDirectoryGrandParent.getParentFile();
//        sourceDirectoryGreatGrandParent = sourceDirectoryGreatGrandParent.getAbsoluteFile();
//                
//        
//        System.out.println(sourceDirectoryGreatGrandParent.getAbsolutePath());
//        
//        String sourceDirectoryGreatGrandParentPath = sourceDirectoryGreatGrandParent.getAbsolutePath() + "\\TAManagerTester\\public_html\\";
//        
//        System.out.println(sourceDirectoryGreatGrandParentPath);
//        
//        
//        File sourceDirectoryToCopy = new File(sourceDirectoryGreatGrandParentPath);
//        File destDirectory = new File(filePath);
//        FileUtils.copyDirectory(sourceDirectoryToCopy, destDirectory); //COPY THE DIRECTORY AND ALL OF ITS CONTENTS TO THE destDirectory with "filePath" WHICH
//                                                                 //IS THE DESTINATION FILE INDICATED BY THE USER
        loadDataExporting(data);
        filePath = filePath + "/TAsData.json";
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    
    public void loadDataExporting(AppDataComponent data){
        // GET THE DATA
	CSGData dataManager = (CSGData)data;

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
	ObservableList<TeachingAssistant> tas = dataManager.getTAData().getTeachingAssistants();
	for (TeachingAssistant ta : tas) {
            String isUndergrad;
            if(ta.isUndergrad().get()){
                isUndergrad = "true";
            }
            else{
                isUndergrad = "false";
            }
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName()).add(JSON_EMAIL, ta.getEmail()).add(JSON_UNDERGRAD, isUndergrad).build();
	    taArrayBuilder.add(taJson);
	}
	JsonArray undergradTAsArray = taArrayBuilder.build();

	// NOW BUILD THE TIME SLOT JSON OBJCTS TO SAVE
	JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
	ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(dataManager);//WAS EMPTY BECAUSE OFFICEHOURS HASHMAP DATA WAS BEING RESET WITH resetWorkspace()
                                                                                    //CALLING RELOAD OFFICEHOURS GRID A SECOND TIME AFTER THE OFFICEHOURS HASHMAP WAS SET WITH VALUES, SO IT WAS CLEARED. SOLVED.
	for (TimeSlot ts : officeHours) {	    
	    JsonObject tsJson = Json.createObjectBuilder()
		    .add(JSON_DAY, ts.getDay())
		    .add(JSON_TIME, ts.getTime())
		    .add(JSON_NAME, ts.getName()).build();
	    timeSlotArrayBuilder.add(tsJson);
	}
	JsonArray timeSlotsArray = timeSlotArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getTAData().getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getTAData().getEndHour())
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray)
		.build();
    }
    
    
    
    
    
    
    
    public void loadDataTestingMethod(AppDataComponent data, String filePath) throws IOException {
//dataManager IS CSGDATA. THEREFORE dataManager.getTAData() WILL GIVE THE TADATA PORTION.
	// CLEAR THE OLD DATA OUT
	CSGData dataManager = (CSGData)data;

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonObject taJson = json.getJsonObject("taData");
	// LOAD THE START AND END HOURS
	String startHour = taJson.getString(JSON_START_HOUR);
        String endHour = taJson.getString(JSON_END_HOUR);
//        dataManager.getTAData().initHours(startHour, endHour);
        dataManager.getTAData().setHours(Integer.parseInt(startHour), Integer.parseInt(endHour));

        // NOW RELOAD THE WORKSPACE WITH THE LOADED DATA
//        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
        
//        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
//        jTPS transactionComponent = workspace.getTransactionComponent();
//        transactionComponent.clearTransactions();

        // NOW LOAD ALL THE UNDERGRAD TAs
        JsonArray jsonTAArray = taJson.getJsonArray(JSON_UNDERGRAD_TAS);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            boolean isUndergrad = true;
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            String undergrad = jsonTA.getString(JSON_UNDERGRAD);
            if(undergrad.equals("true")){
                isUndergrad = true;
            }
            else{
                isUndergrad = false;
            }
            dataManager.getTAData().addTA(name, email);
            dataManager.getTAData().getTA(name).setUndergrad(isUndergrad);
        }

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = taJson.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String day = jsonOfficeHours.getString(JSON_DAY);
            String time = jsonOfficeHours.getString(JSON_TIME);
            String name = jsonOfficeHours.getString(JSON_NAME);
//            dataManager.getTAData().addOfficeHoursReservation(day, time, name);
        }
    }
}