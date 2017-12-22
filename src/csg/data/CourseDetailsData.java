/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class CourseDetailsData {
    CSGApp app;
    private StringProperty subject;
    private StringProperty number;
    private StringProperty semester;
    private StringProperty year;
    private StringProperty title;
    private StringProperty instructorName;
    private StringProperty instructorHome;
    private StringProperty exportDir;
    private StringProperty siteTemplateDir;
    private StringProperty bannerSchoolImage;
    private StringProperty leftFooterImage;
    private StringProperty rightFooterImage;
    private StringProperty stylesheet;
    private ObservableList<SitePages> sitePages;
    
    public CourseDetailsData(CSGApp initApp){
        app = initApp;
        
        sitePages = FXCollections.observableArrayList();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
    }
    public void resetData(){
        subject = new SimpleStringProperty("");
        number = new SimpleStringProperty("");
        semester =  new SimpleStringProperty("");
        year  = new SimpleStringProperty("");
        title = new SimpleStringProperty("");
        instructorName = new SimpleStringProperty("");
        instructorHome = new SimpleStringProperty("");
        exportDir = new SimpleStringProperty("");
        siteTemplateDir = new SimpleStringProperty("");
        bannerSchoolImage = new SimpleStringProperty("");
        leftFooterImage = new SimpleStringProperty("");
        rightFooterImage = new SimpleStringProperty("");
        stylesheet = new SimpleStringProperty("");
        sitePages.clear();
    }
    
    public void setSubject(String initSubject){ //PARAMETERS ARE SET USING SETTERS INSTEAD OF A CONSTRUCTOR BECAUSE A CONSTRUCTOR WILL BE TOO LONG
                                                //SO TO SET THE DATA OF CPOURSE DETAILS, 
        subject = new SimpleStringProperty(initSubject);
    }
    
    public void setNumber(String initNumber){
        number = new SimpleStringProperty(initNumber);
    }
    
    
    public void setSemester(String initSemester) {
        semester = new SimpleStringProperty(initSemester);
    }

    public void setYear(String initYear) {
        year =  new SimpleStringProperty(initYear);
    }

    public void setTitle(String initTitle) {
        title =  new SimpleStringProperty(initTitle);
    }

    public void setInstructorName(String initInstructorName) {
        this.instructorName =  new SimpleStringProperty(initInstructorName);
    }

    public void setInstructorHome(String initInstructorHome) {
        instructorHome =  new SimpleStringProperty(initInstructorHome);
    }

    public void setExportDir(String initExportDir) {
        exportDir =  new SimpleStringProperty(initExportDir);
    }

    public void setSiteTemplateDir(String initSiteTemplateDir) {
        siteTemplateDir =  new SimpleStringProperty(initSiteTemplateDir);
    }

    public void setBannerSchoolImage(String initBannerSchoolImage) {
        bannerSchoolImage =  new SimpleStringProperty(initBannerSchoolImage);
    }

    public void setLeftFooterImage(String initLeftFooterImage) {
        leftFooterImage =  new SimpleStringProperty(initLeftFooterImage);
    }

    public void setRightFooterImage(String initRightFooterImage) {
        rightFooterImage =  new SimpleStringProperty(initRightFooterImage);
    }

    public void setStylesheet(String initStylesheet) {
        stylesheet =  new SimpleStringProperty(initStylesheet);
    }
    
    public void addSitePages(SitePages sitePage){
        sitePages.add(sitePage);
    }
    
      public String getSubject() {
        return subject.get();
    }

    public String getNumber() {
        return number.get();
    }

    public String getSemester() {
        return semester.get();
    }

    public String getYear() {
        return year.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getInstructorName() {
        return instructorName.get();
    }

    public String getInstructorHome() {
        return instructorHome.get();
    }

    public String getExportDir() {
        return exportDir.get();
    }

    public String getSiteTemplateDir() {
        return siteTemplateDir.get();
    }

    public String getBannerSchoolImage() {
        return bannerSchoolImage.get();
    }

    public String getLeftFooterImage() {
        return leftFooterImage.get();
    }

    public String getRightFooterImage() {
        return rightFooterImage.get();
    }

    public String getStylesheet() {
        return stylesheet.get();
    }

    public ObservableList<SitePages> getSitePages() {
        return sitePages;
    }
}
