/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.files;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.CourseDetailsData;
import csg.data.ExportTeam;
import csg.data.ProjectData;
import csg.data.SitePages;
import csg.data.Student;
import csg.data.Team;
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
import jtps.jTPS_Projects;

/**
 *
 * @author Andrew
 */
public class ProjectFiles {
    CSGApp app;
    //Saving and loading json format
    public JsonObject projectJson;
    //export json format
    public JsonObject exportTeamsAndStudentsJson;
    public JsonObject workJson;
    public final String JSON_TEAMS = "teams";
        public final String JSON_TEAM_NAME = "team_name";
        public final String JSON_TEAM_COLOR = "team_color";
        public final String JSON_TEXT_COLOR = "text_color";
        public final String JSON_LINK = "link";
    public final String JSON_STUDENTS = "students";
        public final String JSON_FIRST_NAME = "first_name";
        public final String JSON_LAST_NAME = "last_name";
        public final String JSON_TEAM = "team";
        public final String JSON_ROLE = "role";
    
    
    public ProjectFiles(CSGApp initApp){
        app = initApp;
    }
    
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        //dataManager IS CSGDATA. THEREFORE dataManager.getCourseDetailsData() WILL GIVE THE CourseDetailsDATA PORTION.
        CSGData dataManager = (CSGData)data;
        ProjectData projectData = dataManager.getProjectData();

        // NOW BUILD THE SITE PAGES JSON OBJCTS (ARRAY)TO SAVE
	JsonArrayBuilder teamsArrayBuilder = Json.createArrayBuilder();
	ObservableList<Team> teams = projectData.getTeams();
	for (Team team : teams) {	    
	    JsonObject teamArrayJson = Json.createObjectBuilder()
		    .add(JSON_TEAM_NAME, team.getTeamName())
		    .add(JSON_TEAM_COLOR, team.getTeamColor())
		    .add(JSON_TEXT_COLOR, team.getTextColor())
                    .add(JSON_LINK, team.getLink()).build();
	    teamsArrayBuilder.add(teamArrayJson);
	}
	JsonArray teamsArray = teamsArrayBuilder.build();
        
        JsonArrayBuilder studentsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Student> students = projectData.getStudents();
        for(Student student : students){
            JsonObject studentArrayJson = Json.createObjectBuilder()
                    .add(JSON_FIRST_NAME, student.getFirstName())
                    .add(JSON_LAST_NAME, student.getLastName())
                    .add(JSON_TEAM, student.getTeam())
                    .add(JSON_ROLE, student.getRole())
                    .build();
            studentsArrayBuilder.add(studentArrayJson);
        }
        
        JsonArray studentsArray = studentsArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
        projectJson = Json.createObjectBuilder()
                .add(JSON_TEAMS, teamsArray)
                .add(JSON_STUDENTS, studentsArray)
                .build();	
    }
    
    public JsonObject getProjectJson(){
        return projectJson;
    }
    
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        CSGData dataManager = (CSGData)data;
        ProjectData projectData = dataManager.getProjectData();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        jTPS_Projects projectsTransactionComponent = workspace.getProjectsComponent();
        projectsTransactionComponent.clearTransactions();
        
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonObject projectJson = json.getJsonObject("projectData");
        //LOAD THE ARRAY OF TEAMS
        JsonArray teamsArray = projectJson.getJsonArray(JSON_TEAMS);
        for(int i = 0; i < teamsArray.size(); i++){
            JsonObject teamJson = teamsArray.getJsonObject(i);
            String teamName = teamJson.getString(JSON_TEAM_NAME);
            String teamColor = teamJson.getString(JSON_TEAM_COLOR);
            String textColor = teamJson.getString(JSON_TEXT_COLOR);
            String link = teamJson.getString(JSON_LINK);
            Team team = new Team(teamName, teamColor, textColor, link);
            projectData.getTeams().add(team);
        }
        //LOAD THE ARRAY OF STUDENTS
        JsonArray studentsArray = projectJson.getJsonArray(JSON_STUDENTS);
        for(int i = 0; i < studentsArray.size(); i++){
            JsonObject studentJson = studentsArray.getJsonObject(i);
            String firstName = studentJson.getString(JSON_FIRST_NAME);
            String lastName = studentJson.getString(JSON_LAST_NAME);
            String team = studentJson.getString(JSON_TEAM);
            String role = studentJson.getString(JSON_ROLE);
            Student student = new Student(firstName, lastName, team, role);
            projectData.getStudents().add(student);
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
        
        String teamsAndStudentsfilePath = filePath + "/TeamsAndStudents.json";
        String projectsDatafilePath = filePath + "/ProjectsData.json";
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
        StringWriter sw2 = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(exportTeamsAndStudentsJson);
        jsonWriter = writerFactory.createWriter(sw2);
        jsonWriter.writeObject(workJson);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(teamsAndStudentsfilePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(exportTeamsAndStudentsJson);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(teamsAndStudentsfilePath);
	pw.write(prettyPrinted);
	pw.close();
        
        OutputStream os2 = new FileOutputStream(projectsDatafilePath);
        jsonFileWriter = Json.createWriter(os2);
        jsonFileWriter.writeObject(workJson);
        String prettyPrinted2 = sw2.toString();
        PrintWriter pw2 = new PrintWriter(projectsDatafilePath);
        pw2.write(prettyPrinted2);
        pw2.close();
    }
    
        
    //HELPER METHOD FOR EXPORTING DATA BY READING THE SECTION OF THE JSON FILE WITH THE TEAMS AND STUDENTS DATA
    public void loadDataExporting(AppDataComponent data) throws IOException{
        CSGData dataManager = (CSGData)data;
        ProjectData projectData = dataManager.getProjectData();
////////////////////////////////////////////////////////TEAMS AND STUDENTS.JSON///////////////////////////////////////////////////        
        //INITIALIZE THE EXPORT KEY STRINGS
        String exportNameKey = "name";
        String exportRedKey = "red";
        String exportGreenKey = "green";
        String exportBlueKey = "blue";
        String exportTextColorKey = "text_color";
        
	String exportLastNameKey = "lastName";
        String exportFirstNameKey = "firstName";
        String exportTeamKey = "team";
        String exportRoleKey = "role";
        

//        // LOAD THE JSON FILE WITH ALL THE DATA
//	JsonObject json = loadJSONFile(filePath);
//        JsonObject projectJson = json.getJsonObject("projectData");
        ObservableList<ExportTeam> exportTeamData = FXCollections.observableArrayList();
        //LOAD THE ARRAY OF TEAMS
//        JsonArray teamsArray = projectJson.getJsonArray(JSON_TEAMS);
        ObservableList<Team> teamsToGetData = projectData.getTeams();
        for(int i = 0; i < teamsToGetData.size(); i++){
//            JsonObject teamJson = teamsArray.getJsonObject(i);
            String teamName = teamsToGetData.get(i).getTeamName();
            String teamColor = teamsToGetData.get(i).getTeamColor();
            String textColor = teamsToGetData.get(i).getTextColor();
            String link = teamsToGetData.get(i).getLink();
           //////CONVERT TO THE JSON FORMAT THE EXPORTED FORMAT 
            String exportRedHex = teamColor.substring(1,3);
            String exportGreenHex = teamColor.substring(3,5);
            String exportBlueHex = teamColor.substring(5,7);
            int exportRedDecInteger = Integer.parseInt(exportRedHex, 16);
            int exportGreenDecInteger = Integer.parseInt(exportGreenHex, 16);
            int exportBueDecInteger = Integer.parseInt(exportBlueHex, 16);
            String exportRedDec = Integer.toString(exportRedDecInteger);
            String exportGreenDec = Integer.toString(exportGreenDecInteger);
            String exportBlueDec = Integer.toString(exportBueDecInteger);
            String exportTextColor;
            if(textColor.equals("ffffff")){
                exportTextColor = "white";
            }
            else{
                exportTextColor = "black";
            }
            ExportTeam exportTeam = new ExportTeam(teamName, exportRedDec, exportGreenDec, exportBlueDec, exportTextColor);
            exportTeamData.add(exportTeam);
        }
        
        ObservableList<Student> exportStudentData = FXCollections.observableArrayList();
        //LOAD THE ARRAY OF STUDENTS
//        JsonArray studentsArray = projectJson.getJsonArray(JSON_STUDENTS);
        ObservableList<Student> studentsToGetData = projectData.getStudents();
        for(int i = 0; i < studentsToGetData.size(); i++){
//            JsonObject studentJson = studentsArray.getJsonObject(i);
            String firstName = studentsToGetData.get(i).getFirstName();
            String lastName = studentsToGetData.get(i).getLastName();
            String team = studentsToGetData.get(i).getTeam();
            String role = studentsToGetData.get(i).getRole();
            ////////NO NEED TO CONVERT BECAUSE THE EXPORT JSON FORMAT IS THE SAME AS THE SAVED JSON FORMAT FOR STUDENTS
            Student student = new Student(firstName, lastName, team, role);
            exportStudentData.add(student);
        }
        
        
        // NOW BUILD THE TEAMS JSON OBJCTS (ARRAY)TO EXPORT
	JsonArrayBuilder teamsExportArrayBuilder = Json.createArrayBuilder();
	for (ExportTeam exportTeam : exportTeamData) {	    
	    JsonObject teamExportArrayJson = Json.createObjectBuilder()
		    .add(exportNameKey, exportTeam.getTeamName())
		    .add(exportRedKey, exportTeam.getRed())
		    .add(exportGreenKey, exportTeam.getGreen())
                    .add(exportBlueKey, exportTeam.getBlue())
                    .add(exportTextColorKey, exportTeam.getTextColor()).build();
	    teamsExportArrayBuilder.add(teamExportArrayJson);
	}
	JsonArray teamsExportArray = teamsExportArrayBuilder.build();
        
        //NOW BUILD THE STUDENTS JSON OBJECTS (ARRAY) TO EXPORT
        JsonArrayBuilder studentsExportArrayBuilder = Json.createArrayBuilder();
        ObservableList<Student> students = projectData.getStudents();
        for(Student student : students){
            JsonObject studentExportArrayJson = Json.createObjectBuilder()
                    .add(exportLastNameKey, student.getLastName())
                    .add(exportFirstNameKey, student.getFirstName()) 
                    .add(exportTeamKey, student.getTeam())
                    .add(exportRoleKey, student.getRole())
                    .build();
            studentsExportArrayBuilder.add(studentExportArrayJson);
        }
        
        JsonArray studentsExportArray = studentsExportArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
        exportTeamsAndStudentsJson = Json.createObjectBuilder()
                .add(JSON_TEAMS, teamsExportArray)
                .add(JSON_STUDENTS, studentsExportArray)
                .build();	
        
        ////////////////////////////////////////////PROJECTSDATA.JSON////////////////////////////////////////
        String exportWorkKey = "work";
        String exportSemesterKey = "semester";
        String exportProjectsKey = "projects";
        String exportNamesKey = "name";
        String exportStudentsKey = "students";
        String exportLinksKey = "link";
        

        //PROJECTS JSON ARRAY
        JsonArrayBuilder projectArrayBuilder = Json.createArrayBuilder();
        for(int h = 0; h < teamsToGetData.size(); h++){
            //INITIALIZING AND CREATING EACH PROJECT JSON OBJECT TO PUT IN THE ARRAY
            JsonArrayBuilder projectStudentsArrayBuilder = Json.createArrayBuilder();
            String teamProjectName = teamsToGetData.get(h).getTeamName();
            String studentProjectFirstName;
            String studentProjectSecondName;
            String studentProject;
            //STUDENTS JSON ARRAY. ONLY GET THE STUDENTS IN THE ARRAY THAT ARE IN THE CURRENT TEAM BEING CYCLED THROUGH IN THE OUTER FOR LOOP. PUT THESE STUDENTS INTO THE STUDENT JSON ARRAY.
            //EACH STUDENT ARRAY IS IN A PROJECT JSON OBJECT.
            for(int i = 0; i < studentsToGetData.size(); i++){
                if(studentsToGetData.get(i).getTeam().equals(teamProjectName)){
                    studentProjectFirstName = studentsToGetData.get(i).getFirstName();
                    studentProjectSecondName = studentsToGetData.get(i).getLastName();
                    studentProject = studentProjectFirstName + " " + studentProjectSecondName;
                    projectStudentsArrayBuilder.add(studentProject);
                }
            }
            JsonArray studentsArray = projectStudentsArrayBuilder.build();
            String teamProjectLink = teamsToGetData.get(h).getLink();
            JsonObject projectExportJson = Json.createObjectBuilder()
                    .add(exportNamesKey, teamProjectName)
                    .add(exportStudentsKey, studentsArray)
                    .add(exportLinksKey, teamProjectLink)
                    .build();
            projectArrayBuilder.add(projectExportJson);
        }
        
        JsonArray projectArray = projectArrayBuilder.build();
        JsonArrayBuilder workArrayBuilder = Json.createArrayBuilder();
        
        CourseDetailsData courseDetailsData = dataManager.getCourseDetailsData();
        String semester = courseDetailsData.getSemester();
        String year = courseDetailsData.getYear();
        semester = semester + " " + year;
        
        JsonObject workJsonObject = Json.createObjectBuilder()
                .add(exportSemesterKey, semester)
                .add(exportProjectsKey, projectArray)
                .build();
        
       workArrayBuilder.add(workJsonObject);
       
       JsonArray workArray = workArrayBuilder.build();
       
        workJson = Json.createObjectBuilder()
                .add(exportWorkKey, workArray)
                .build();
        
    }
    
    
}
