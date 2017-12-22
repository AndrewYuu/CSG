/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.files;

import csg.CSGApp;
import csg.CSGProp;
import csg.data.CSGData;
import csg.data.CourseDetailsData;
import csg.data.ExportHWsItem;
import csg.data.ExportHolidaysItem;
import csg.data.ExportLecturesItem;
import csg.data.ExportRecitationsItem;
import csg.data.ExportReferencesItem;
import csg.data.ProjectData;
import csg.data.ScheduleData;
import csg.data.ScheduleItem;
import csg.data.SitePages;
import static csg.files.CourseDetailsFiles.JSON_SITE_PAGES;
import static csg.files.CourseDetailsFiles.JSON_USE;
import csg.workspace.CSGWorkspace;
import djf.components.AppDataComponent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jtps.jTPS_Schedule;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class ScheduleFiles {
    CSGApp app;
    JsonObject scheduleJson;
    JsonObject exportScheduleJson;
    public final String JSON_START_MON = "starting_monday";
    public final String JSON_END_FRI = "ending_friday";
    public final String JSON_SCHEDULE_ITEMS = "schedule_items";
        public final String JSON_TYPE = "type";
        public final String JSON_DATE = "date";
        public final String JSON_TIME = "time";
        public final String JSON_TITLE = "title";
        public final String JSON_TOPIC = "topic";
        public final String JSON_LINK = "link";
        public final String JSON_CRITERIA = "criteria";
        
    public ScheduleFiles(CSGApp initApp){
        app = initApp;
    }
    
    
     
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        //dataManager IS CSGDATA. THEREFORE dataManager.getCourseDetailsData() WILL GIVE THE CourseDetailsDATA PORTION.
        CSGData dataManager = (CSGData)data;
        ScheduleData scheduleData = dataManager.getScheduleData();

        // NOW BUILD THE SITE PAGES JSON OBJCTS (ARRAY)TO SAVE
	JsonArrayBuilder scheduleItemsArrayBuilder = Json.createArrayBuilder();
	ObservableList<ScheduleItem> scheduleItems = scheduleData.getScheduleItems();
	for (ScheduleItem scheduleItem : scheduleItems) {	    
	    JsonObject scheduleItemJson = Json.createObjectBuilder()
		    .add(JSON_TYPE, scheduleItem.getType())
		    .add(JSON_DATE, scheduleItem.getDateString())
		    .add(JSON_TIME, scheduleItem.getTime())
                    .add(JSON_TITLE, scheduleItem.getTitle())
                    .add(JSON_TOPIC, scheduleItem.getTopic())
                    .add(JSON_LINK, scheduleItem.getLink())
                    .add(JSON_CRITERIA, scheduleItem.getCriteria()).build();
	    scheduleItemsArrayBuilder.add(scheduleItemJson);
	}
	JsonArray scheduleItemsArray = scheduleItemsArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
        if(scheduleData.getStartingMondayString() != null && scheduleData.getEndingFridayString() != null){
            scheduleJson = Json.createObjectBuilder()
                    .add(JSON_START_MON, scheduleData.getStartingMondayString())
                    .add(JSON_END_FRI, scheduleData.getEndingFridayString())
                    .add(JSON_SCHEDULE_ITEMS, scheduleItemsArray).build();
        }
        else{
            scheduleJson = Json.createObjectBuilder()
                    .add(JSON_START_MON, "")
                    .add(JSON_END_FRI, "")
                    .add(JSON_SCHEDULE_ITEMS, scheduleItemsArray).build();
        }
	
    }
    
    public JsonObject getScheduleJson(){
        return scheduleJson;
    }
    
    public void loadData(AppDataComponent data, String filePath) throws IOException, ParseException {
        CSGData dataManager = (CSGData)data;
        ScheduleData scheduleData = dataManager.getScheduleData();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        jTPS_Schedule scheduleTransactionComponent = workspace.getScheduleComponent();
        scheduleTransactionComponent.clearTransactions();
        
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonObject scheduleJson = json.getJsonObject("scheduleData");
        String startingMondayString = scheduleJson.getString(JSON_START_MON);
//        Date startingMonday = format.parse(startingMondayString);
        scheduleData.setStartingMonday(startingMondayString);
        String endingFridayString = scheduleJson.getString(JSON_END_FRI);
//        Date endingFriday = format.parse(endingFridayString);
        scheduleData.setEndingFriday(endingFridayString);
        // LOAD THE ARRAY OF SCHEDULE ITEMS
        JsonArray scheduleArray = scheduleJson.getJsonArray(JSON_SCHEDULE_ITEMS);
        for(int i = 0; i < scheduleArray.size(); i++){
            JsonObject scheduleItemJson = scheduleArray.getJsonObject(i);
            String type = scheduleItemJson.getString(JSON_TYPE);
            String dateString = scheduleItemJson.getString(JSON_DATE);
//            Date date = format.parse(dateString);
            String time = scheduleItemJson.getString(JSON_TIME);
            String title = scheduleItemJson.getString(JSON_TITLE);
            String topic = scheduleItemJson.getString(JSON_TOPIC);
            String link = scheduleItemJson.getString(JSON_LINK);
            String criteria = scheduleItemJson.getString(JSON_CRITERIA);
            
            ScheduleItem scheduleItem = new ScheduleItem(type, dateString, time, title, topic, link, criteria);
            scheduleData.getScheduleItems().add(scheduleItem);
        }
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
    
    
    public void exportData(AppDataComponent data, String filePath) throws IOException, ParseException {
        loadDataExporting(data);
         
        filePath = filePath + "/ScheduleData.json";
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(exportScheduleJson);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(exportScheduleJson);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
    
    }
    
    public void loadDataExporting(AppDataComponent data) throws IOException, ParseException{
        CSGData dataManager = (CSGData)data;
        ScheduleData scheduleData = dataManager.getScheduleData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        String exportStartingMondayMonthKey = "startingMondayMonth";
        String exportStartingMondayDayKey = "startingMondayDay";
        String exportEndingFridayMonthKey = "endingFridayMonth";
        String exportEndingFridayDayKey = "endingFridayDay";
        
        String exportMonthKey = "month";
        String exportDayKey = "day";
        String exportTitleKey = "title";
        String exportLinkKey = "link";
        String exportTopicKey = "topic";
        String exportTimeKey = "time";
        String exportCriteriaKey = "criteria";
        
        String exportHolidayKey = "holidays";
        String exportLectureKey = "lectures";
        String exportReferenceKey = "references";
        String exportRecitationKey = "recitations";
        String exportHWsKey = "hws";
        
        
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(java.sql.Date.valueOf(scheduleData.getStartingMonday()));
        
        String exportStartingMondayMonth = Integer.toString(startCal.get(Calendar.MONTH) + 1);
        String exportStartingMondayDay = Integer.toString(startCal.get(Calendar.DAY_OF_MONTH));
        
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(java.sql.Date.valueOf(scheduleData.getEndingFriday()));
        
        String exportEndingFridayMonth = Integer.toString(endCal.get(Calendar.MONTH) + 1);
        String exportEndingFridayDay = Integer.toString(endCal.get(Calendar.DAY_OF_MONTH));
        
        ObservableList<ScheduleItem> scheduleItemsToGetData = scheduleData.getScheduleItems();
        //FOR HOLIDAYS ITEMS TO BE SAVED TO THE EXPORTED JSON
        ObservableList<ExportHolidaysItem> holidaysExportItems = FXCollections.observableArrayList();
        //FOR LECTURES ITEMS TO BE SAVED TO THE EXPORTED JSON
        ObservableList<ExportLecturesItem> lecturesExportItems = FXCollections.observableArrayList();
        //FOR REFERENCES ITEMS TO BE SAVED TO THE EXPORTED JSON
        ObservableList<ExportReferencesItem> referencesExportItems = FXCollections.observableArrayList();
        //FOR RECITATIONS ITEMS TO BE SAVED TO THE EXPORTED JSON
        ObservableList<ExportRecitationsItem> recitationsExportItems = FXCollections.observableArrayList();
        //FOR HOMEWORKS ITEMS TO BE SAVED TO THE EXPORTED JSON
        ObservableList<ExportHWsItem> hwsExportItems = FXCollections.observableArrayList();
        for(int i = 0; i < scheduleItemsToGetData.size(); i++){
            if(scheduleItemsToGetData.get(i).getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_HOLIDAY))){
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(scheduleItemsToGetData.get(i).getDate());
                String exportMonth = Integer.toString(dateCal.get(Calendar.MONTH) + 1);
                String exportDay = Integer.toString(dateCal.get(Calendar.DAY_OF_MONTH));
                String exportTitle = scheduleItemsToGetData.get(i).getTitle();
                String exportLink = scheduleItemsToGetData.get(i).getLink();
                ExportHolidaysItem holidayItem = new ExportHolidaysItem(exportMonth, exportDay, exportTitle, exportLink);
                holidaysExportItems.add(holidayItem);
            }
            if(scheduleItemsToGetData.get(i).getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_LECTURE))){
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(scheduleItemsToGetData.get(i).getDate());
                String exportMonth = Integer.toString(dateCal.get(Calendar.MONTH) + 1);
                String exportDay = Integer.toString(dateCal.get(Calendar.DAY_OF_MONTH));
                String exportTitle = scheduleItemsToGetData.get(i).getTitle();
                String exportTopic = scheduleItemsToGetData.get(i).getTopic();
                String exportLink = scheduleItemsToGetData.get(i).getLink();
                ExportLecturesItem lectureItem = new ExportLecturesItem(exportMonth, exportDay, exportTitle, exportTopic, exportLink);
                lecturesExportItems.add(lectureItem);
            }
            if(scheduleItemsToGetData.get(i).getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_REFERENCE))){
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(scheduleItemsToGetData.get(i).getDate());
                String exportMonth = Integer.toString(dateCal.get(Calendar.MONTH) + 1);
                String exportDay = Integer.toString(dateCal.get(Calendar.DAY_OF_MONTH));
                String exportTitle = scheduleItemsToGetData.get(i).getTitle();
                String exportTopic = scheduleItemsToGetData.get(i).getTopic();
                String exportLink = scheduleItemsToGetData.get(i).getLink();
                ExportReferencesItem referenceItem = new ExportReferencesItem(exportMonth, exportDay, exportTitle, exportTopic, exportLink);
                referencesExportItems.add(referenceItem);
            }
            if(scheduleItemsToGetData.get(i).getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_RECITATION))){
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(scheduleItemsToGetData.get(i).getDate());
                String exportMonth = Integer.toString(dateCal.get(Calendar.MONTH) + 1);
                String exportDay = Integer.toString(dateCal.get(Calendar.DAY_OF_MONTH));
                String exportTitle = scheduleItemsToGetData.get(i).getTitle();
                String exportTopic = scheduleItemsToGetData.get(i).getTopic();
                ExportRecitationsItem recitationsItem = new ExportRecitationsItem(exportMonth, exportDay, exportTitle, exportTopic);
                recitationsExportItems.add(recitationsItem);
            }
            if(scheduleItemsToGetData.get(i).getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_HW))){
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(scheduleItemsToGetData.get(i).getDate());
                String exportMonth = Integer.toString(dateCal.get(Calendar.MONTH) + 1);
                String exportDay = Integer.toString(dateCal.get(Calendar.DAY_OF_MONTH));
                String exportTitle = scheduleItemsToGetData.get(i).getTitle();
                String exportTopic = scheduleItemsToGetData.get(i).getTopic();
                String exportLink = scheduleItemsToGetData.get(i).getLink();
                String exportTime = scheduleItemsToGetData.get(i).getTime();
                String exportCriteria = scheduleItemsToGetData.get(i).getCriteria();
                ExportHWsItem hwsItem = new ExportHWsItem(exportMonth, exportDay, exportTitle, exportTopic, exportLink, exportTime, exportCriteria);
                hwsExportItems.add(hwsItem);
            }
            
        }
        
        //NOW BUILD THE JSON OBJECTS INTO JSON ARRAYS
        
        //CREATE THE HOLIDAYS JSON ARRAY
        JsonArrayBuilder holidaysArrayBuilder = Json.createArrayBuilder();
	for (ExportHolidaysItem exportHoliday : holidaysExportItems) {	    
	    JsonObject holidaysExportArrayJson = Json.createObjectBuilder()
		    .add(exportMonthKey, exportHoliday.getMonth())
		    .add(exportDayKey, exportHoliday.getDay())
		    .add(exportTitleKey, exportHoliday.getTitle())
                    .add(exportLinkKey, exportHoliday.getLink())
                    .build();
	    holidaysArrayBuilder.add(holidaysExportArrayJson);
	}
	JsonArray holidayExportArray = holidaysArrayBuilder.build();
        
        //CREATE THE LECTURES JSON ARRAY
        JsonArrayBuilder lecturesArrayBuilder = Json.createArrayBuilder();
	for (ExportLecturesItem exportLectures : lecturesExportItems) {	    
	    JsonObject lecturesExportArrayJson = Json.createObjectBuilder()
		    .add(exportMonthKey, exportLectures.getMonth())
		    .add(exportDayKey, exportLectures.getDay())
		    .add(exportTitleKey, exportLectures.getTitle())
                    .add(exportTopicKey, exportLectures.getTopic())
                    .add(exportLinkKey, exportLectures.getLink())
                    .build();
	    lecturesArrayBuilder.add(lecturesExportArrayJson);
	}
	JsonArray lecturesExportArray = lecturesArrayBuilder.build();
        
        //CREATE THE REFERENCES JSON ARRAY
        JsonArrayBuilder referencesArrayBuilder = Json.createArrayBuilder();
	for (ExportReferencesItem exportReferences : referencesExportItems) {	    
	    JsonObject referencesExportArrayJson = Json.createObjectBuilder()
		    .add(exportMonthKey, exportReferences.getMonth())
		    .add(exportDayKey, exportReferences.getDay())
		    .add(exportTitleKey, exportReferences.getTitle())
                    .add(exportTopicKey, exportReferences.getTopic())
                    .add(exportLinkKey, exportReferences.getLink())
                    .build();
	    referencesArrayBuilder.add(referencesExportArrayJson);
	}
	JsonArray referencesExportArray = referencesArrayBuilder.build();
        
        //CREATE THE RECITATIONS JSON ARRAY
        JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
	for (ExportRecitationsItem exportRecitations : recitationsExportItems) {	    
	    JsonObject recitationsExportArrayJson = Json.createObjectBuilder()
		    .add(exportMonthKey, exportRecitations.getMonth())
		    .add(exportDayKey, exportRecitations.getDay())
		    .add(exportTitleKey, exportRecitations.getTitle())
                    .add(exportTopicKey, exportRecitations.getTopic())
                    .build();
	    recitationsArrayBuilder.add(recitationsExportArrayJson);
	}
	JsonArray recitationsExportArray = recitationsArrayBuilder.build();
        
        //CREATE THE HOMEWORKS JSON ARRAY
        JsonArrayBuilder hwsArrayBuilder = Json.createArrayBuilder();
	for (ExportHWsItem exportHWs : hwsExportItems) {	    
	    JsonObject recitationsExportArrayJson = Json.createObjectBuilder()
		    .add(exportMonthKey, exportHWs.getMonth())
		    .add(exportDayKey, exportHWs.getDay())
		    .add(exportTitleKey, exportHWs.getTitle())
                    .add(exportTopicKey, exportHWs.getTopic())
                    .add(exportLinkKey, exportHWs.getLink())
                    .add(exportTimeKey, exportHWs.getTime())
                    .add(exportCriteriaKey, exportHWs.getCriteria())
                    .build();
	    hwsArrayBuilder.add(recitationsExportArrayJson);
	}
	JsonArray hwsExportArray = hwsArrayBuilder.build();
        
        exportScheduleJson = Json.createObjectBuilder()
                .add(exportStartingMondayMonthKey,exportStartingMondayMonth)
                .add(exportStartingMondayDayKey, exportStartingMondayDay)
                .add(exportEndingFridayMonthKey, exportEndingFridayMonth)
                .add(exportEndingFridayDayKey, exportEndingFridayDay)
                .add(exportHolidayKey, holidayExportArray)
                .add(exportLectureKey, lecturesExportArray)
                .add(exportReferenceKey, referencesExportArray)
                .add(exportRecitationKey, recitationsExportArray)
                .add(exportHWsKey, hwsExportArray)
                .build();     
    }
    
    
}
