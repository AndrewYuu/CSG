/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.files;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.CourseDetailsData;
import csg.data.ExportRecitation;
import csg.data.Recitation;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import csg.data.SitePages;
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
import java.math.BigDecimal;
import java.util.HashMap;
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
import jtps.jTPS_Recitation;

/**
 *
 * @author Andrew
 */
public class RecitationFiles {
    CSGApp app;
    public JsonObject recitationJson;
    public JsonObject exportRecitationJson;
    public final String JSON_RECITATION = "recitation";
        public final String JSON_SECTION = "section";
        public final String JSON_INSTRUCTOR = "instructor";
        public final String JSON_DAY_TIME = "day_time";
        public final String JSON_LOCATION = "location";
        public final String JSON_TA1 = "ta_1";
        public final String JSON_TA2 = "ta_2";
    RecitationFiles(CSGApp initApp){
        app = initApp;
    }
    
     public void saveData(AppDataComponent data, String filePath) throws IOException {
        //dataManager IS CSGDATA. THEREFORE dataManager.getCourseDetailsData() WILL GIVE THE CourseDetailsDATA PORTION.
        CSGData dataManager = (CSGData)data;
        RecitationData recitationData = dataManager.getRecitationData();

        // NOW BUILD THE SITE PAGES JSON OBJCTS (ARRAY)TO SAVE
	JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
	ObservableList<Recitation> recitationsList = recitationData.getRecitations();
	for (Recitation recitation : recitationsList) {	    
	    JsonObject recitationArrayJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, recitation.getSection())
		    .add(JSON_INSTRUCTOR, recitation.getInstructor())
		    .add(JSON_DAY_TIME, recitation.getDayTime())
                    .add(JSON_LOCATION, recitation.getLocation())
                    .add(JSON_TA1, recitation.getFirstTA())
                    .add(JSON_TA2, recitation.getSecondTA())
                    .build();
	    recitationsArrayBuilder.add(recitationArrayJson);
	}
	JsonArray recitationsArray = recitationsArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
        recitationJson = Json.createObjectBuilder()
                .add(JSON_RECITATION, recitationsArray)
                .build();
	
    }
    
    public JsonObject getRecitationsJson(){
        return recitationJson;
    }
    
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        CSGData dataManager = (CSGData)data;
        RecitationData recitationData = dataManager.getRecitationData();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        jTPS_Recitation recitationTransactionComponent = workspace.getRecitationComponent();
        recitationTransactionComponent.clearTransactions();
        
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonObject overallRecitationJson = json.getJsonObject("recitationData");
        
        //LOAD THE ARRAY OF RECITATIONS
        JsonArray recitationArray = overallRecitationJson.getJsonArray(JSON_RECITATION);
        for(int i = 0; i < recitationArray.size(); i++){
            JsonObject recitationJson = recitationArray.getJsonObject(i);
            String section = recitationJson.getString(JSON_SECTION);
            String instructor = recitationJson.getString(JSON_INSTRUCTOR);
            String dayTime = recitationJson.getString(JSON_DAY_TIME);
            String location = recitationJson.getString(JSON_LOCATION);
            String ta1 = recitationJson.getString(JSON_TA1);
            String ta2 = recitationJson.getString(JSON_TA2);
            
            Recitation recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            recitationData.getRecitations().add(recitation);
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
    
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        loadDataExporting(data);
         
        filePath = filePath + "/RecitationsData.json";
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(exportRecitationJson);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(exportRecitationJson);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
    }
    
    //HELPER METHOD FOR EXPORTING DATA BY READING THE SECTION OF THE JSON FILE WITH THE TEAMS AND STUDENTS DATA
    public void loadDataExporting(AppDataComponent data){
        CSGData dataManager = (CSGData)data;
        RecitationData recitationData = dataManager.getRecitationData();
        
        String exportSectionKey = "section";
        String exportDayTimeKey = "day_time";
        String exportLocationKey = "location";
        String exportTA1Key = "ta_1";
        String exportTA2Key = "ta_2";
        
        String exportRecitationKey = "recitations";
        
        ObservableList<Recitation> recitationToGetData = recitationData.getRecitations();
        ObservableList<ExportRecitation> recitationExportData = FXCollections.observableArrayList();

        for(int i = 0; i < recitationToGetData.size(); i++){
            String section = recitationToGetData.get(i).getSection();
            String instructor = recitationToGetData.get(i).getInstructor();
            section = section + " (" + instructor + ")";
            String dayTime = recitationToGetData.get(i).getDayTime();
            String location = recitationToGetData.get(i).getLocation();
            String ta1 = recitationToGetData.get(i).getFirstTA();
            String ta2 = recitationToGetData.get(i).getSecondTA();
            
            ExportRecitation recitationExport = new ExportRecitation(section, dayTime,location, ta1, ta2);
            recitationExportData.add(recitationExport);
        }
        
        //CREATE THE RECITATION JSON ARRAY
        JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
        
        for(ExportRecitation exportRecitation : recitationExportData){
            JsonObject recitationsExportArrayJson = Json.createObjectBuilder()
                    .add(exportSectionKey, exportRecitation.getSection())
                    .add(exportDayTimeKey, exportRecitation.getDayTime())
                    .add(exportLocationKey, exportRecitation.getLocation())
                    .add(exportTA1Key, exportRecitation.getFirstTA())
                    .add(exportTA2Key, exportRecitation.getSecondTA())
                    .build();
                    recitationsArrayBuilder.add(recitationsExportArrayJson);
            
        }
        JsonArray recitationsExportArray = recitationsArrayBuilder.build();
        
        exportRecitationJson = Json.createObjectBuilder()
                .add(exportRecitationKey, recitationsExportArray)
                .build();
    }
}
