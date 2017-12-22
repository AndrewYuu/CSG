/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CSGApp;
import csg.CSGProp;
import csg.data.CSGData;
import csg.data.CourseDetailsData;
import csg.data.SitePages;
import djf.controller.AppFileController;
import static djf.settings.AppStartupConstants.PATH_WORK;
import djf.ui.AppGUI;
import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class CourseDetailsController {
    CSGApp app;
    
    public CourseDetailsController(CSGApp initApp){
        app = initApp;
    }
    
    public void handleSetSubjectChoice(ChoiceBox courseInfoSubjectChoices){
        CSGData data = (CSGData)app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        courseData.setSubject((String)courseInfoSubjectChoices.getSelectionModel().getSelectedItem());
        fileController.markAsEdited(gui);
    }
    
    public void handleSetNumberChoice(ChoiceBox courseInfoNumberChoices){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        courseData.setNumber((String)courseInfoNumberChoices.getSelectionModel().getSelectedItem());
        fileController.markAsEdited(gui);
    }
    
    public void handleSetSemesterChoice(ChoiceBox courseInfoSemesterChoices){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        courseData.setSemester((String)courseInfoSemesterChoices.getSelectionModel().getSelectedItem());
        fileController.markAsEdited(gui);
    }
    
    public void handleSetYearChoice(ChoiceBox courseInfoYearChoices){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        courseData.setYear((String)courseInfoYearChoices.getSelectionModel().getSelectedItem());
        fileController.markAsEdited(gui);
    }
    
    public void handleSetTitle(TextField courseTitleTextField){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        String toAdd = courseTitleTextField.getText();
        
        courseData.setTitle(toAdd);
        fileController.markAsEdited(gui);
    }
    
    public void handleSetInstructorName(TextField courseInstructorNameTextField){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        String toAdd = courseInstructorNameTextField.getText();
        courseData.setInstructorName(toAdd);
        fileController.markAsEdited(gui);
    }
    
    public void handleSetInstructorHome(TextField courseInstructorHomeTextField){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        String toAdd = courseInstructorHomeTextField.getText();
        courseData.setInstructorHome(toAdd);
        fileController.markAsEdited(gui);
    }
    
    public void handleSetExportDir(Label courseExportDirLabel){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        String exportDirectoryString = fileController.exportDirectoryChoose();
        if(exportDirectoryString == null){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CSGProp.INVALID_DIRECTORY_TITLE), props.getProperty(CSGProp.INVALID_DIRECTORY_MESSAGE));
        }
        else{
            courseExportDirLabel.setText(exportDirectoryString);
            courseData.setExportDir(exportDirectoryString);

            fileController.markAsEdited(gui);
            fileController.markAsExportSelected(gui);
        }
    }
    
    public void handleSiteTemplate(Label siteTemplateDir){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle(props.getProperty(CSGProp.SELECT_TEMPLATE_TITLE));
        File selectedDirectory;
        selectedDirectory = dc.showDialog(app.getGUI().getWindow()); //USER SELECTS DIRECTORY TO COPY FOLDER
        String selectedDestinationPath = selectedDirectory.getPath(); //GET THE PATH OF THE USER SELECTED DIRECTORY
        
        siteTemplateDir.setText(selectedDestinationPath);
        courseData.setSiteTemplateDir(selectedDestinationPath);
        
        File[] files = selectedDirectory.listFiles(); //GET ALL THE FILES IN THE USER SELECTED DIRECTORY
        ArrayList<File> filesNeeded = new ArrayList();
        for(File file : files){ //FOR EACH FILE IN THE FILE ARRAY
            if(file.getName().endsWith(".html")){
                filesNeeded.add(file);
            }
        }
        for(int i = 0; i < filesNeeded.size(); i++){
            File currentFile = filesNeeded.get(i);
            if(currentFile.getName().equals("index.html")){
                SitePages sitepage = new SitePages(true, "Home", "index.html", "HomeBuilder.js");
                courseData.getSitePages().add(sitepage);
            }
            if(currentFile.getName().equals("syllabus.html")){
                SitePages sitepage = new SitePages(true, "Syllabus", "syllabus.html", "SyllabusBuilder.js");
                courseData.getSitePages().add(sitepage);
            }
            if(currentFile.getName().equals("schedule.html")){
                SitePages sitepage = new SitePages(true, "Schedule", "schedule.html", "ScheduleBuilder.js");
                courseData.getSitePages().add(sitepage);
            }
            if(currentFile.getName().equals("hws.html")){
                SitePages sitepage = new SitePages(true, "HWs", "hws.html", "HWsBuilder.js");
                courseData.getSitePages().add(sitepage);
            }
            if(currentFile.getName().equals("projects.html")){
                SitePages sitepage = new SitePages(true, "Projects", "projects.html", "ProjectsBuilder.js");
                courseData.getSitePages().add(sitepage);
            }
        }
        fileController.markAsEdited(gui);    
    }
    
    public void handleBannerSchoolImage(ImageView pageStyleImage1){
        CSGData data = (CSGData) app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(props.getProperty(CSGProp.PICK_IMAGE_TITLE));

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedImage;
        selectedImage = fileChooser.showOpenDialog(app.getGUI().getWindow());
        if(selectedImage != null){

            Image bannerSchoolImage = new Image(selectedImage.toURI().toString());
            pageStyleImage1.setImage(bannerSchoolImage);
            courseData.setBannerSchoolImage(selectedImage.toURI().toString());
            workspace.setBannerURI(courseData.getBannerSchoolImage());

            fileController.markAsEdited(gui);
        }
    }
    
    public void handleLeftFooterImage(ImageView pageStyleImage2){
        CSGData data = (CSGData) app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();

        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(props.getProperty(CSGProp.PICK_IMAGE_TITLE));

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedImage;
        selectedImage = fileChooser.showOpenDialog(app.getGUI().getWindow());
        if(selectedImage != null){

            Image leftFooterImage = new Image(selectedImage.toURI().toString());
            pageStyleImage2.setImage(leftFooterImage);
            courseData.setLeftFooterImage(selectedImage.toURI().toString());
            workspace.setLeftURI(courseData.getLeftFooterImage()); 
            
            fileController.markAsEdited(gui);
        }
    }
    
    public void handleRightFooterImage(ImageView pageStyleImage3){
        CSGData data = (CSGData) app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
       
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(props.getProperty(CSGProp.PICK_IMAGE_TITLE));

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedImage;
        selectedImage = fileChooser.showOpenDialog(app.getGUI().getWindow());
        if(selectedImage != null){

            Image rightFooterImage = new Image(selectedImage.toURI().toString());
            pageStyleImage3.setImage(rightFooterImage);
            courseData.setRightFooterImage(selectedImage.toURI().toString());
            workspace.setRightURI(courseData.getRightFooterImage());
            
            fileController.markAsEdited(gui);
        }
    }
    
    public void handleStyleSheet(ChoiceBox styleSheetSelectorChoices){
        CSGData data = (CSGData) app.getDataComponent();
        CourseDetailsData courseData = data.getCourseDetailsData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        
        String cssName = (String) styleSheetSelectorChoices.getSelectionModel().getSelectedItem();
//        for(File file : files){ //FOR EACH FILE IN THE FILE ARRAY
//            if(file.getName().equals("js")){ //IF THE FOLDER IS NAMED js
//                
//            }
//        }
        courseData.setStylesheet(cssName);
        fileController.markAsEdited(gui);
    }
   
}
