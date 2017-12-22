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
import csg.test_bed_gui.TestSaveGui;
import csg.workspace.CSGWorkspace;
//import csg.testbed.TestSave;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import static djf.settings.AppStartupConstants.PATH_WORK;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Andrew
 */
public class CSGFiles implements AppFileComponent{
    
    CSGApp app;
    TAFiles taFiles;
    CourseDetailsFiles courseDetailsFiles;
    RecitationFiles recitationFiles;
    ScheduleFiles scheduleFiles;
    ProjectFiles projectFiles;
    //TEMPORARY TEST SAVER. WHEN THIS INITIALIZES, IT WILL SET ALL THE DATA FOR ALL 5 OF THE DATA CLASSES.
    TestSaveGui testSaver;
    
    public final String COURSEDETAILSDATA = "courseDetailsData";
    public final String TAFILESDATA = "taData";
    public final String RECITATIONDATA = "recitationData";
    public final String SCHEDULEDATA = "scheduleData";
    public final String PROJECTDATA = "projectData";
    
    public CSGFiles(CSGApp initApp){
        app = initApp;
        taFiles = new TAFiles(app);
        courseDetailsFiles = new CourseDetailsFiles(app);
        recitationFiles = new RecitationFiles(app);
        scheduleFiles = new ScheduleFiles(app);
        projectFiles = new ProjectFiles(app);
//        /////////////////TEMPORARY TEST SAVER CLASS TO TEST SAVING WITH THE WORKSPACE TOOLBAR////////////////////
//        try{
//            testSaver = new TestSaveGui(app);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException { //AppDataComponent data IS THE CSGDATA, NOT THE SPECIFIC DATA CLASSES.
        //SAVES ALL 5 TABS AT ONCE INTO ONE JSON FILE
        //
        
        courseDetailsFiles.saveData(data, filePath);
        recitationFiles.saveData(data, filePath);
        taFiles.saveData(data, filePath);
        scheduleFiles.saveData(data, filePath);
        projectFiles.saveData(data, filePath);
        
        JsonObject overallData = Json.createObjectBuilder()
                .add(COURSEDETAILSDATA, courseDetailsFiles.getCourseDetailsJson())
                .add(TAFILESDATA, taFiles.getTAFilesJson())
                .add(RECITATIONDATA, recitationFiles.getRecitationsJson())
                .add(SCHEDULEDATA, scheduleFiles.getScheduleJson())
                .add(PROJECTDATA, projectFiles.getProjectJson())
                .build();
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(overallData);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(overallData);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        //LOADS ALL 5 TABS AT ONCE FROM ONE JSON FILE
        courseDetailsFiles.loadData(data, filePath);
        taFiles.loadData(data, filePath);
        recitationFiles.loadData(data, filePath);
        try {
            scheduleFiles.loadData(data, filePath);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        projectFiles.loadData(data, filePath);
        workspace.reloadWorkspaceLoad(app.getDataComponent());
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        //String source = "./CourseSiteGeneratorTester/public_html"; //GET THE ORIGINAL FILE DIRECTORY TO COPY
        File sourceDirectory = new File(PATH_WORK);
        //System.out.print(sourceDirectory.getAbsolutePath());
        File sourceDirectoryParent = sourceDirectory.getParentFile();
        //System.out.print(sourceDirectoryParent.getAbsolutePath());
        sourceDirectoryParent = sourceDirectoryParent.getAbsoluteFile();
        
        File sourceDirectoryGrandParent = sourceDirectoryParent.getParentFile();
        sourceDirectoryGrandParent = sourceDirectoryGrandParent.getAbsoluteFile();
        
        File sourceDirectoryGreatGrandParent = sourceDirectoryGrandParent.getParentFile();
        sourceDirectoryGreatGrandParent = sourceDirectoryGreatGrandParent.getAbsoluteFile();
                
        
        System.out.println(sourceDirectoryGreatGrandParent.getAbsolutePath());
        
        String sourceDirectoryGreatGrandParentPath = sourceDirectoryGreatGrandParent.getAbsolutePath() + "\\CourseSiteGeneratorTester\\public_html\\";
        
        System.out.println(sourceDirectoryGreatGrandParentPath);
        
        
        File sourceDirectoryToCopy = new File(sourceDirectoryGreatGrandParentPath);
        File destDirectory = new File(filePath);
        FileUtils.copyDirectory(sourceDirectoryToCopy, destDirectory); //COPY THE DIRECTORY AND ALL OF ITS CONTENTS TO THE destDirectory with "filePath" WHICH
                                                                 //IS THE DESTINATION FILE INDICATED BY THE USER
                                                                 
        //AS EXPORTING, MOVE THE USER SELECTED IMAGES TO THE EXPORTED FOLDER IN THE IMAGES FOLDER.
/////////////////////////////////////////////BANNER IMAGE////////////////////////////////////////////////////////////  
        String banner = workspace.getBannerURI().substring(5);
        File bannerImageDirectory = new File(workspace.getBannerURI().substring(5));
        String bannerEXT = "";
        int counter1 = bannerImageDirectory.getPath().lastIndexOf('.');
        if (counter1 > 0) {
            bannerEXT = bannerImageDirectory.getPath().substring(counter1+1);
        } 
        BufferedImage bannerImage = ImageIO.read(bannerImageDirectory);
        File destBannerImageDirectory = new File(filePath + "\\images\\" + bannerImageDirectory.getName());
//////////////////////////////////////////////LEFT IMAGE/////////////////////////////////////////////////////////////////////
        File leftImageDirectory = new File(workspace.getLeftURI().substring(5));
        String leftEXT = "";
        int counter2 = leftImageDirectory.getPath().lastIndexOf('.');
        if(counter2 > 0){
            leftEXT = leftImageDirectory.getPath().substring(counter2+1);
        }
        BufferedImage leftImage = ImageIO.read(leftImageDirectory);
        File destLeftImageDirectory = new File(filePath + "\\images\\" + leftImageDirectory.getName());
//////////////////////////////////////////////RIGHT IMAGE///////////////////////////////////////////////////////////////        
        File rightImageDirectory = new File(workspace.getRightURI().substring(5));
        String rightEXT = "";
        int counter3 = rightImageDirectory.getPath().lastIndexOf('.');
        if(counter3 > 0){
            rightEXT = rightImageDirectory.getPath().substring(counter3+1);
        }
        BufferedImage rightImage = ImageIO.read(rightImageDirectory);
        File destRightImageDirectory = new File(filePath + "\\images\\" + rightImageDirectory.getName());
        
        ImageIO.write(bannerImage, bannerEXT , destBannerImageDirectory);
        ImageIO.write(leftImage, leftEXT, destLeftImageDirectory);
        ImageIO.write(rightImage, rightEXT, destRightImageDirectory);

        
//        Path bannerImagePath = bannerImageDirectory.toPath();
//        Path leftImagePath = leftImageDirectory.toPath();
//        Path rightImagePath = rightImageDirectory.toPath();
//        Path destPath = destImageDirectory.toPath();
        

        
        //EXPORTS TO 5 DIFFERENT JSON FILES, ONE JSON FILE FOR EACH TAB.
        String filePathExport = destDirectory + "\\js\\";
        
        //DONT EXPORT THE HTML FILES AND JS FILES THAT ARE NOT CHECKED TRUE, SO ESSENTIALLY DELETE THOSE FROM THE USER SELECTED EXPORT DIRECTORY AFTER THEY HAVE BEEN COPIED
        CSGData dataFeature = (CSGData) data;

        ObservableList<SitePages> sitePagesToRemove = FXCollections.observableArrayList();    
        ObservableList<SitePages> sitePagesArray = dataFeature.getCourseDetailsData().getSitePages();
        
        for(int i = 0; i < sitePagesArray.size(); i++){
            if(sitePagesArray.get(i).isUseProperty().get() == false){
                sitePagesToRemove.add(sitePagesArray.get(i));
            }
        }
        for(int i = 0; i < sitePagesToRemove.size(); i++){
            if(sitePagesToRemove.get(i).getFileName().equals("syllabus.html")){
                File htmlToDelete = new File(destDirectory + "\\" + sitePagesToRemove.get(i).getFileName());
                htmlToDelete.delete();
                File js1ToDelete = new File(filePathExport + "\\" + "RecitationsBuilder.js");
                js1ToDelete.delete();
                File js2ToDelete = new File(filePathExport + "\\" + "OfficeHoursGridBuilder.js");
                js2ToDelete.delete();
            }
            else{
                File htmlToDelete = new File(destDirectory + "\\" + sitePagesToRemove.get(i).getFileName());
                htmlToDelete.delete();
                File jsToDelete = new File(filePathExport + "\\" + sitePagesToRemove.get(i).getScript());
                jsToDelete.delete();
            }
        }
        
        courseDetailsFiles.exportData(data, filePathExport);
        taFiles.exportData(data, filePathExport);
        recitationFiles.exportData(data, filePathExport);
        try{
            scheduleFiles.exportData(data, filePathExport);
        }catch(Exception e){
            e.printStackTrace();
        }
        projectFiles.exportData(data, filePathExport);
        
        
        File cssFile = new File(sourceDirectoryGreatGrandParent.getAbsolutePath() + "\\CourseSiteGenerator\\work\\" + dataFeature.getCourseDetailsData().getStylesheet());
        String filePathCSSExport = destDirectory + "\\css\\" + dataFeature.getCourseDetailsData().getStylesheet();
        File cssDestination = new File(filePathCSSExport);
        Files.copy(cssFile.toPath(), cssDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    public TAFiles getTaFiles() {
        return taFiles;
    }

    public CourseDetailsFiles getCourseDetailsFiles() {
        return courseDetailsFiles;
    }

    public RecitationFiles getRecitationFiles() {
        return recitationFiles;
    }

    public ScheduleFiles getScheduleFiles() {
        return scheduleFiles;
    }

    public ProjectFiles getProjectFiles() {
        return projectFiles;
    }
    
    
    
    
    
    
    
    public void loadDataTestingMethod(AppDataComponent data, String filePath) throws IOException {
        //LOADS ALL 5 TABS AT ONCE FROM ONE JSON FILE
        courseDetailsFiles.loadData(data, filePath);
        taFiles.loadDataTestingMethod(data, filePath);
        recitationFiles.loadData(data, filePath);
        try {
            scheduleFiles.loadData(data, filePath);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        projectFiles.loadData(data, filePath);
        
//        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
    }
    
}
