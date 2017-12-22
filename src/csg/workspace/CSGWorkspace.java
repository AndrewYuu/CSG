/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CSGApp;
import csg.CSGProp;
import static csg.CSGProp.TYPE_CHOICE_HOLIDAY;
import static csg.CSGProp.TYPE_CHOICE_HW;
import static csg.CSGProp.TYPE_CHOICE_LECTURE;
import static csg.CSGProp.TYPE_CHOICE_RECITATION;
import static csg.CSGProp.TYPE_CHOICE_REFERENCE;
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
import csg.data.TeachingAssistant;
import csg.data.Team;
import csg.style.CSGStyle;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppPropertyType.INVALID_START_END_MESSAGE;
import static djf.settings.AppPropertyType.INVALID_START_END_TITLE;
import static djf.settings.AppPropertyType.START_END_WARNING_MESSAGE;
import static djf.settings.AppPropertyType.START_END_WARNING_TITLE;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import properties_manager.PropertiesManager;
import csg.transactions.StartEnd_Transaction;
import csg.transactions.ToggleTA_Transaction;
import djf.controller.AppFileController;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.settings.AppStartupConstants.PATH_WORK;
import djf.ui.AppGUI;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jtps.jTPS;
import jtps.jTPS_Projects;
import jtps.jTPS_Recitation;
import jtps.jTPS_Schedule;

/**
 *
 * @author Andrew
 */
public class CSGWorkspace extends AppWorkspaceComponent {
    CSGApp app;
     
    jTPS transactionComponent = new jTPS();
    jTPS_Projects projectsTransactionComponent = new jTPS_Projects();
    jTPS_Schedule scheduleTransactionComponent = new jTPS_Schedule();
    jTPS_Recitation recitationTransactionComponent = new jTPS_Recitation();
    ProjectsController projectsController;
    ScheduleController scheduleController;
    RecitationController recitationController;
    CourseDetailsController courseDetailsController;
    
    public jTPS getTransactionComponent(){
        return transactionComponent;
    }
    
    public jTPS_Recitation getRecitationComponent(){
        return recitationTransactionComponent;
    }
    
    public jTPS_Schedule getScheduleComponent(){
        return scheduleTransactionComponent;
    }
    
    public jTPS_Projects getProjectsComponent(){
        return projectsTransactionComponent;
    }
    
    //OVERALL PANE FOR COURSE DETAILS TAB
    VBox courseDetails;
    
    //SUBPANES FOR COURSE DETAILS TAB
    VBox courseInfo;
    GridPane subCourseInfo;
    HBox subCourseInfo1;
    HBox subCourseInfo2;
    HBox subCourseInfo3;
    HBox subCourseInfo4;
    HBox subCourseInfo5;
    HBox courseInfoTextBox1;
    HBox courseInfoTextBox2;
    HBox courseInfoTextBox3;
    Button changeCourseInfoButton;
    //SITE TEMPLATE SUBPANE FOR COURSE DETAILS TAB
    VBox siteTemplate;
    TableView siteTemplateTable;
    Button siteDirButton;
    TableColumn<SitePages, Boolean> useColumn;
    TableColumn<String, String> navbarColumn;
    TableColumn<String, String> fileNameColumn;
    TableColumn<String, String> scriptColumn;
    ChoiceBox<String> courseInfoSubjectChoices;
    ChoiceBox<String> courseInfoNumberChoices;
    ObservableList<String> courseInfoNumberCSEChoices;
    ObservableList<String> courseInfoNumberAMSChoices;
    ChoiceBox<String> courseInfoSemesterChoices;
    ChoiceBox<String> courseInfoYearChoices;
    TextField courseTitleTextField;
    TextField courseInstructorNameTextField;
    TextField courseInstructorHomeTextField;
    Label courseExportDirTextField;
    
    CheckBoxTableCell siteTemplateCheckBox;
    
    Label courseInfoTitleLabel, courseInfoSubjectLabel, courseInfoNumberLabel, courseInfoSemesterLabel, courseInfoYearLabel,
           courseTitleLabel, instructorNameLabel, instructorHomeLabel, exportDirLabel ;
    Label siteTemplateTitleLabel, siteTemplateMessageLabel, sitePagesLabel, directoryChosenLabel;
    Label bannerSchoolImageLabel, pageStyleTitleLabel, leftFooterImageLabel, rightFooterImageLabel, styleSheetSelectorLabel, styleNoticeLabel;

    String useHeaderTextLabel, navbarTitleLabel, fileNameHeaderLabel, scriptHeaderLabel; //TABLE COLUMN HEADERS
    
    Label addEditTitleLabel;
    
    //SUBPANES FOR SITE TEMPLATE SUBPANE FOR COURSEDETAILS TAB
    VBox pageStyle;
    HBox subPageStyle1, subPageStyle2, subPageStyle3, subPageStyle4;
    Button changePageStyleButton1;
    ImageView pageStyleImage1;
    Button changePageStyleButton2;
    ImageView pageStyleImage2;
    Button changePageStyleButton3;
    ImageView pageStyleImage3;
    ChoiceBox<String> styleSheetSelectorChoices;
    
    String bannerSchoolImageURI;
    String leftFooterImageURI;
    String rightFooterImageURI;
    
   //////////////////////////////////////////////////////////////////////////////////////////////////// 
    //OVERALL PANE FOR RECITATIONS TAB
    VBox recitations;
    
    //SUBPANES FOR RECITATIONS TAB
    HBox subRecitationsTitle;
    VBox subRecitations;
    
    //SUBPANES FOR RECITATION SUBPANEs
    HBox subSubRecitations1, subSubRecitations2, subSubRecitations3, subSubRecitations4, subSubRecitations5,
            subSubRecitations6, subSubRecitations7;
    
    //STUFF FOR RECITATIONS TAB
    Button recitationsDeleteButton;
    TableView recitationsTable;
    TableColumn<String, String> sectionColumn;
    TableColumn<String, String> instructorColumn;
    TableColumn<String, String> dayTimeColumn;
    TableColumn<String, String> locationColumn;
    TableColumn<String, String> ta1Column;
    TableColumn<String, String> ta2Column;
    
    TextField sectionTextField;
    TextField instructorTextField;
    TextField dayTimeTextField;
    TextField locationTextField;
    ChoiceBox<String> supervisingTA1ChoiceBox;
    ChoiceBox<String> supervisingTA2ChoiceBox;
    Button addUpdateRecitationsButton;
    Button clearRecitationsButton;
    
    ObservableList<String> supervisingTAsChoices = FXCollections.observableArrayList();
    
    Label recitationTitleLabel, sectionLabel, instructorLabel, recDayTimeLabel, locationLabel, ta1Label, ta2Label;
    Label addEditTitleLabelRecitation;

    ////////////////////////////////////////////////////////////////////////////////////////
    //OVERALL PANE FOR SCHEDULE TAB
    VBox schedule;
    //SUBPANES FOR THE SCHEDULE TAB
    VBox calendarSchedule;
    VBox scheduleItems;
   
    //SUBPANES FOR THE SUBPANES
    HBox subCalendarSchedule1;
    HBox subCalendarSchedule2;
    HBox subScheduleItems1, subScheduleItems2, subScheduleItems3, subScheduleItems4, subScheduleItems5,
            subScheduleItems6, subScheduleItems7, subScheduleItems8, subScheduleItems9, subScheduleItems10;
    
    //STUFF FOR THE SCHEDULE TAB
    DatePicker startingDay;
    DatePicker endingDay;
    Button clearScheduleItemsFromTable;
    TableView scheduleTable;
    TableColumn<String, String> typeColumn;
    TableColumn<String, String> dateColumn;
    TableColumn<String, String> titleColumn;
    TableColumn<String, String> topicColumn;
    ChoiceBox<String> typeChoices;
    DatePicker currentDay;
    TextField scheduleTime, scheduleTitle, scheduleTopic, scheduleLink, scheduleCriteria;

    Button addUpdateSchedulesButton;
    Button clearSchedulesButton;
    
    Label scheduleTitleLabel,calendarBoundariesTitleLabel,scheduleStartDayLabel,scheduleEndDayLabel
	,scheduleItemsTitleLabel,typeLabel ,dateLabel,timeLabel,titleLabel,topicLabel 
        ,linkLabel,criteriaLabel, projectsTitleLabel ;
    
    Label addEditTitleLabelSchedule;
    /////////////////////////////////////////////////////////////////////////////////////////////////
    //OVERALL PANE FOR THE PROJECTS TAB
    VBox projects;
    //SUBPANES FOR THE PROJECTS TAB
    VBox teams;
    VBox students;
    
    //SUBPANES FOR THE TEAMS AND STUDENTS PANES
    HBox subTeams1, subTeams2, subTeams3, subTeams4, subTeams5;
    StackPane teamColorOverlay;
    StackPane textColorOverlay;
    HBox subStudents1, subStudents2, subStudents3, subStudents4, subStudents5, subStudents6;
    
    //STUFF FOR THE PROJECTS TAB
    //TEAMS SUBTAB
    Button clearTeamsFromTableButton;
    TableView teamsTable;
    TableColumn<String, String> teamNameColumn;
    TableColumn<String, String> teamColorColumn;
    TableColumn<String, String> teamTextColorColumn;
    TableColumn<String, String> teamLinkColumn;
    
    TextField teamNameField;
    TextField teamColorTextField;
    TextField textColorTextField;
    Circle teamColorCircle;
    Circle textColorCircle;
    TextField teamLink;
    Button addUpdateTeamButton;
    Button clearTeamButton;
    
    //STUDENTS SUBTAB
    Button clearStudentsFromTableButton;
    TableView studentsTable;
    TableColumn<String, String> studentFirstNameColumn;
    TableColumn<String, String> studentLastNameColumn;
    TableColumn<String, String> studentTeamColumn;
    TableColumn<String, String> studentRoleColumn;
    
    TextField studentFirstName;
    TextField studentLastName;
    ChoiceBox<String> studentTeam;
    TextField studentRole;
    Button addUpdateStudentButton;
    Button clearStudentButton;
    
    ObservableList<String> studentTeamChoices = FXCollections.observableArrayList();
    
    Label teamTitleLabel, teamNameLabel,teamColorLabel, teamTextColorLabel, teamLinkLabel,studentTitleLabel, studentFirstLabel,studentLastLabel,
        studentTeamLabel,studentRoleLabel;
    Label addEditTitleLabelStudents, addEditTitleLabelTeams;
    
    ////////////////////////////////////////////////////////
    //THE TABS
    TabPane tabPane;
    Tab tab1;
    Tab tab2;
    Tab tab3;
    Tab tab4;
    Tab tab5;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TA DATA PANE STUFF
    
    VBox taPane;    
    TAController controller;
    VBox taDataPane;
    SplitPane sPane;
    
    // FOR THE HEADER ON THE LEFT
    HBox tasHeaderBox;
    Label tasHeaderLabel;
    
    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    //TableView taTable;
    TableColumn<TeachingAssistant, String> nameColumn;
    TableColumn<TeachingAssistant, String> emailColumn;
    TableColumn<TeachingAssistant, Boolean> undergradColumn;

    // THE TA INPUT
    HBox addBox;
    TextField nameTextField;
    Button addButton;
    
    // ADD: THE TAEMAIL INPUT
    TextField emailTextField;

    // THE HEADER ON THE RIGHT
    HBox officeHoursHeaderBox;
    Label officeHoursHeaderLabel;
    
    //Button to clear textfields and change update TA button back to add TA;
    Button clearButton;
    Button deleteTAButton;
    //Button to submit office hours TA changes
    Button submitButton;
    Label startTitle;
    Label endTitle;
    Label comboBoxHeaderLabel;
    // THE OFFICE HOURS GRID
    //EACH HASHMAP IS A CERTAIN PART OF THE GRIDPANE, WHICH SEPARATES THE DIFFERENT STYLES IN THE GRID
    GridPane officeHoursGridPane;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;
    
    ObservableList<String> choices;
    ChoiceBox<String> choiceBox1;
    ChoiceBox<String> choiceBox2;
    
    CheckBoxTableCell undergradTACheckBox;
    
    
    
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public CSGWorkspace(CSGApp initApp){
        app = initApp;
        
        CSGData data = (CSGData) app.getDataComponent();
        projectsController = new ProjectsController(app);
        //INITIALIZE ALL THE OVERALL PANES FOR ALL TABS
        courseDetails = new VBox(10);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        
        //////////////////////////BELOW CONSISTS OF THE COURSE DETAILS TAB WORKSPACE INITIALIZATION///////////////////////////////////////
        //COURSE INFO PANE BOX
        courseInfo = new VBox(5);
        subCourseInfo = new GridPane();
        subCourseInfo1 = new HBox();
        subCourseInfo2 = new HBox();
        subCourseInfo3 = new HBox(); //3 is for export dir
        subCourseInfo4 = new HBox();
        subCourseInfo5 = new HBox();
        
        courseInfoTextBox1 = new HBox();
        courseInfoTextBox2 = new HBox();
        courseInfoTextBox3 = new HBox();
        
        courseTitleTextField = new TextField();
        courseTitleTextField.setPrefWidth(150);
        courseInstructorNameTextField = new TextField();
        courseInstructorNameTextField.setPrefWidth(150);
        courseInstructorHomeTextField = new TextField();
        courseInstructorHomeTextField.setPrefWidth(150);
        courseExportDirTextField = new Label();
        courseExportDirTextField.setPrefWidth(150);
        
        courseInfoSubjectChoices = new ChoiceBox();
        courseInfoSubjectChoices.getItems().addAll("CSE", "AMS");
        courseInfoNumberChoices = new ChoiceBox();
        courseInfoNumberCSEChoices = FXCollections.observableArrayList();
        courseInfoNumberCSEChoices.addAll("101", "114", "215", "220", "219", "300", "303", "305", "306", "307", "308", "310", "312", "320", "380", "381");
        courseInfoNumberAMSChoices = FXCollections.observableArrayList();
        courseInfoNumberAMSChoices.addAll("101", "131", "132", "151", "152", "161", "162", "210", "261", "301", "310", "361");
        courseInfoSemesterChoices = new ChoiceBox();
        courseInfoSemesterChoices.getItems().addAll("Fall", "Winter", "Spring", "Summer");
        courseInfoYearChoices = new ChoiceBox();
        
        ArrayList<String> years = new ArrayList();
        for(int year = 2017; year < 2050; year++){
            years.add(Integer.toString(year));
        }
        courseInfoYearChoices.getItems().addAll(years);
        
        changeCourseInfoButton = new Button(props.getProperty(CSGProp.CHANGE_BUTTON_TEXT));
        changeCourseInfoButton.setPrefWidth(100);
        courseInfoTitleLabel = new Label(props.getProperty(CSGProp.COURSEINFO_TITLE));
        subCourseInfo.setHgap(100);
        subCourseInfo.setVgap(5);
        subCourseInfo.add(subCourseInfo1,0, 1);
        subCourseInfo.add(subCourseInfo4,1, 1);
        subCourseInfo.add(subCourseInfo2, 0, 2 );
        subCourseInfo.add(subCourseInfo5, 1,2);
        courseInfo.getChildren().addAll(courseInfoTitleLabel, subCourseInfo, courseInfoTextBox1, courseInfoTextBox2, courseInfoTextBox3);
        
        courseInfoSubjectLabel = new Label(props.getProperty(CSGProp.SUBCOURSEINFO1_SUBJECT));
        courseInfoSubjectLabel.setPrefWidth(100);
        courseInfoNumberLabel = new Label(props.getProperty(CSGProp.SUBCOURSEINFO1_NUMBER));
        courseInfoNumberLabel.setPrefWidth(100);
        courseInfoSemesterLabel = new Label (props.getProperty(CSGProp.SUBCOURSEINFO2_SEMESTER));
        courseInfoSemesterLabel.setPrefWidth(100);
        courseInfoYearLabel = new Label (props.getProperty(CSGProp.SUBCOURSEINFO2_YEAR));
        courseInfoYearLabel.setPrefWidth(100);
        courseTitleLabel = new Label(props.getProperty(CSGProp.COURSE_TITLE));
        courseTitleLabel.setPrefWidth(200);
        instructorNameLabel = new Label(props.getProperty(CSGProp.COURSE_INSTRUCTOR_NAME));
        instructorNameLabel.setPrefWidth(200);
        instructorHomeLabel = new Label(props.getProperty(CSGProp.COURSE_INSTRUCTOR_HOME));
        instructorHomeLabel.setPrefWidth(200);
        exportDirLabel = new Label(props.getProperty(CSGProp.COURSE_EXPORT_DIR));
        exportDirLabel.setPrefWidth(200);
        
        courseInfoSubjectChoices.setPrefWidth(100);
        courseInfoNumberChoices.setPrefWidth(100);
        courseInfoSemesterChoices.setPrefWidth(100);
        courseInfoYearChoices.setPrefWidth(100);
        
        subCourseInfo1.getChildren().addAll(courseInfoSubjectLabel, courseInfoSubjectChoices);
       // subCourseInfo1.setSpacing(12);
        subCourseInfo4.getChildren().addAll(courseInfoNumberLabel, courseInfoNumberChoices);
       // subCourseInfo4.setSpacing(12);
        subCourseInfo2.getChildren().addAll(courseInfoSemesterLabel, courseInfoSemesterChoices);
       // subCourseInfo2.setSpacing(5);
        subCourseInfo5.getChildren().addAll(courseInfoYearLabel, courseInfoYearChoices);
       // subCourseInfo5.setSpacing(26);
        courseInfoTextBox1.getChildren().addAll(courseTitleLabel, courseTitleTextField); 
       // courseInfoTextBox1.setSpacing(82);
        courseInfoTextBox2.getChildren().addAll(instructorNameLabel, courseInstructorNameTextField);
       // courseInfoTextBox2.setSpacing(10);
        courseInfoTextBox3.getChildren().addAll(instructorHomeLabel, courseInstructorHomeTextField);
       // courseInfoTextBox3.setSpacing(10);
        subCourseInfo3.getChildren().add(exportDirLabel);
        courseInfo.getChildren().add(subCourseInfo3);
        subCourseInfo3.getChildren().add(courseExportDirTextField);
        subCourseInfo3.getChildren().add(changeCourseInfoButton);
       // subCourseInfo3.setSpacing(46);
        
        
        //SITE TEMPLATE PANE BOX
        siteTemplate = new VBox(5);
        
        siteTemplateTable = new TableView();
        siteDirButton = new Button(props.getProperty(CSGProp.SITE_TEMPLATE_BUTTON_TEXT));
        
        siteTemplateTitleLabel = new Label(props.getProperty(CSGProp.SITE_TEMPLATE_TITLE));
        siteTemplateMessageLabel = new Label(props.getProperty(CSGProp.SITE_TEMPLATE_MESSAGE));
        sitePagesLabel = new Label(props.getProperty(CSGProp.SITE_PAGES));
        directoryChosenLabel = new Label();
        siteTemplate.getChildren().add(siteTemplateTitleLabel);
        siteTemplate.getChildren().add(siteTemplateMessageLabel);
        siteTemplate.getChildren().add(directoryChosenLabel);
        siteTemplate.getChildren().add(siteDirButton);
        siteTemplate.getChildren().add(sitePagesLabel);
        
        
        String useHeaderText = props.getProperty(CSGProp.USE_HEADER_TEXT.toString());
        String navbarTitleText = props.getProperty(CSGProp.NAV_BAR_TITLE_TEXT.toString());
        String fileNameHeaderText = props.getProperty(CSGProp.FILE_NAME_HEADER_TEXT.toString());
        String scriptHeaderText = props.getProperty(CSGProp.SCRIPT_HEADER_TEXT.toString());
        useColumn = new TableColumn(useHeaderText);
        useColumn.setCellFactory(column -> new CheckBoxTableCell());
        navbarColumn = new TableColumn(navbarTitleText);
        fileNameColumn = new TableColumn(fileNameHeaderText);
        scriptColumn = new TableColumn(scriptHeaderText);
        
        useColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.1));
        navbarColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.3));
        fileNameColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.3));
        scriptColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.3));
        useColumn.setResizable(false);
        navbarColumn.setResizable(false);
        fileNameColumn.setResizable(false);
        scriptColumn.setResizable(false);
        
        siteTemplateTable.getColumns().addAll(useColumn, navbarColumn, fileNameColumn, scriptColumn);
        siteTemplate.getChildren().add(siteTemplateTable);
        
        ObservableList<SitePages> sitePagesData = data.getCourseDetailsData().getSitePages();
        siteTemplateTable.setItems(sitePagesData);
        
        navbarColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("navbarTitle")
        );
        fileNameColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("fileName")
        );
        scriptColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("script")
        );
        
                
        siteTemplateCheckBox = new CheckBoxTableCell();

        useColumn.setEditable(true);
        siteTemplateTable.setEditable(true);
        
        useColumn.setCellValueFactory(
                new Callback<CellDataFeatures<SitePages, Boolean>, ObservableValue<Boolean>>(){
                    @Override
                    public ObservableValue<Boolean> call(CellDataFeatures<SitePages, Boolean> param) {
                        BooleanProperty useBoolean = param.getValue().isUseProperty();
                        return useBoolean;
                    }
                }
        );
        useColumn.setCellFactory(siteTemplateCheckBox.forTableColumn(useColumn));
        
        
        
        //PAGE STYLE PANE BOX
        pageStyle = new VBox(5);
        subPageStyle1 = new HBox();
        subPageStyle2 = new HBox();
        subPageStyle3 = new HBox();
        subPageStyle4 = new HBox();
        
        changePageStyleButton1 = new Button(props.getProperty(CSGProp.CHANGE_BUTTON_TEXT));
        pageStyleImage1 = new ImageView();
        pageStyleImage1.setFitHeight(25);
        pageStyleImage1.setFitWidth(150);
        changePageStyleButton2 = new Button(props.getProperty(CSGProp.CHANGE_BUTTON_TEXT));
        pageStyleImage2 = new ImageView();
        pageStyleImage2.setFitHeight(25);
        pageStyleImage2.setFitWidth(150);
        changePageStyleButton3 = new Button(props.getProperty(CSGProp.CHANGE_BUTTON_TEXT));
        pageStyleImage3 = new ImageView();
        pageStyleImage3.setFitHeight(25);
        pageStyleImage3.setFitWidth(150);
        styleSheetSelectorChoices = new ChoiceBox(); 
        
        File sourceDirectory = new File(PATH_WORK);
        
        File[] files = sourceDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File sourceDirectory, String name) {
                return name.toLowerCase().endsWith(".css");
            }
        });
        ArrayList<String> styleSheets = new ArrayList();
        
        for(int i = 0; i < files.length; i++){
            styleSheets.add(files[i].getName());
        }
        styleSheetSelectorChoices.getItems().setAll(styleSheets);
        
        
        styleSheetSelectorChoices.setPrefWidth(300);
        pageStyleTitleLabel = new Label(props.getProperty(CSGProp.PAGE_STYLE_TITLE));
        pageStyle.getChildren().add(pageStyleTitleLabel);
        pageStyle.getChildren().addAll(subPageStyle1, subPageStyle2, subPageStyle3, subPageStyle4);

        
        bannerSchoolImageLabel = new Label(props.getProperty(CSGProp.BANNER_SCHOOL_IMAGE));
        bannerSchoolImageLabel.setPrefWidth(150);
        leftFooterImageLabel = new Label(props.getProperty(CSGProp.LEFT_FOOTER_IMAGE));
        leftFooterImageLabel.setPrefWidth(150);
        rightFooterImageLabel = new Label(props.getProperty(CSGProp.RIGHT_FOOTER_IMAGE));
        rightFooterImageLabel.setPrefWidth(150);
        styleSheetSelectorLabel = new Label(props.getProperty(CSGProp.STYLESHEET_SELECTOR));
        
        styleNoticeLabel = new Label(props.getProperty(CSGProp.STYLE_NOTICE));
        subPageStyle1.getChildren().addAll(bannerSchoolImageLabel, pageStyleImage1, changePageStyleButton1);
        //subPageStyle1.setSpacing(15);
        subPageStyle2.getChildren().addAll(leftFooterImageLabel, pageStyleImage2, changePageStyleButton2);
        //subPageStyle2.setSpacing(30);
        subPageStyle3.getChildren().addAll(rightFooterImageLabel,pageStyleImage3, changePageStyleButton3);
        //subPageStyle3.setSpacing(23);
        subPageStyle4.getChildren().addAll(styleSheetSelectorLabel, styleSheetSelectorChoices);
        //subPageStyle4.setSpacing(10);
        pageStyle.getChildren().add(styleNoticeLabel);
        
        courseDetails.getChildren().addAll(courseInfo, siteTemplate, pageStyle);
        
        ////////////////////////////////////////////BELOW CONSISTS OF THE RECITATIONS TAB WORKSPACE INITIALIZATION/////////////////////////////////////////////////////////////////
        recitations = new VBox();
        subRecitationsTitle = new HBox();
        recitationTitleLabel = new Label(props.getProperty(CSGProp.RECITATION_TITLE));
        subRecitationsTitle.getChildren().add(recitationTitleLabel);
        recitationsDeleteButton = new Button(props.getProperty(CSGProp.DELETE_BUTTON_TEXT));
        subRecitationsTitle.getChildren().add(recitationsDeleteButton);
        
        recitationsTable = new TableView();
        String sectionColumnText = props.getProperty(CSGProp.SECTION_HEADER_TEXT.toString());
        String instructorColumnText = props.getProperty(CSGProp.INSTRUCTOR_HEADER_TEXT.toString());
        String dayTimeColumnText = props.getProperty(CSGProp.REC_DAY_TIME_HEADER_TEXT.toString());
        String locationColumnText = props.getProperty(CSGProp.LOCATION_HEADER_TEXT.toString());
        String ta1HeaderText = props.getProperty(CSGProp.TA1_HEADER_TEXT.toString());
        String ta2HeaderText = props.getProperty(CSGProp.TA2_HEADER_TEXT.toString());
        sectionColumn = new TableColumn<>(sectionColumnText);
        instructorColumn = new TableColumn<>(instructorColumnText);
        dayTimeColumn = new TableColumn<>(dayTimeColumnText);
        locationColumn = new TableColumn<>(locationColumnText);
        ta1Column = new TableColumn<>(ta1HeaderText);
        ta2Column = new TableColumn<>(ta2HeaderText);
        recitationsTable.getColumns().addAll(sectionColumn, instructorColumn, dayTimeColumn, locationColumn, ta1Column, ta2Column);

        sectionColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.1));
        instructorColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.15));
        dayTimeColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.15));
        locationColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.20));
        ta1Column.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.2));
        ta2Column.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.2));
        sectionColumn.setResizable(false);
        instructorColumn.setResizable(false);
        dayTimeColumn.setResizable(false);
        locationColumn.setResizable(false);
        ta1Column.setResizable(false);
        ta2Column.setResizable(false);
        
        subRecitations = new VBox();
        addEditTitleLabelRecitation = new Label(props.getProperty(CSGProp.ADD_EDIT_TITLE.toString()));
        subRecitations.getChildren().add(addEditTitleLabelRecitation);
        
        subSubRecitations1 = new HBox();
        subSubRecitations2 = new HBox();
        subSubRecitations3 = new HBox();
        subSubRecitations4 = new HBox();
        subSubRecitations5 = new HBox();
        subSubRecitations6 = new HBox();
        subSubRecitations7 = new HBox();
        
        sectionTextField = new TextField();
        sectionTextField.setPrefWidth(150);
        instructorTextField = new TextField();
        instructorTextField.setPrefWidth(150);
        dayTimeTextField = new TextField();
        dayTimeTextField.setPrefWidth(150);
        locationTextField = new TextField();
        locationTextField.setPrefWidth(150);
        supervisingTA1ChoiceBox = new ChoiceBox();
        supervisingTA1ChoiceBox.setPrefWidth(150);
        supervisingTA2ChoiceBox = new ChoiceBox();
        supervisingTA2ChoiceBox.setPrefWidth(150);
        addUpdateRecitationsButton = new Button(props.getProperty(CSGProp.ADD_BUTTON_TEXT_GENERIC));
        addUpdateRecitationsButton.setPrefWidth(150);
        clearRecitationsButton = new Button(props.getProperty(CSGProp.CLEAR_BUTTON_TEXT));
        clearRecitationsButton.setPrefWidth(100);
        
        subRecitations.getChildren().addAll(subSubRecitations1, subSubRecitations2, subSubRecitations3, subSubRecitations4, subSubRecitations5, subSubRecitations6, subSubRecitations7);
        
        sectionLabel = new Label(props.getProperty(CSGProp.SECTION_LABEL.toString()));
        sectionLabel.setPrefWidth(150);
        instructorLabel = new Label(props.getProperty(CSGProp.INSTRUCTOR_LABEL.toString()));
        instructorLabel.setPrefWidth(150);
        recDayTimeLabel = new Label(props.getProperty(CSGProp.REC_DAY_TIME_LABEL.toString()));
        recDayTimeLabel.setPrefWidth(150);
        locationLabel = new Label(props.getProperty(CSGProp.LOCATION_LABEL.toString()));
        locationLabel.setPrefWidth(150);
        ta1Label = new Label(props.getProperty(CSGProp.TA1_LABEL.toString()));
        ta1Label.setPrefWidth(150);
        ta2Label = new Label(props.getProperty(CSGProp.TA2_LABEL.toString()));
        ta2Label.setPrefWidth(150);
        subSubRecitations1.getChildren().addAll(sectionLabel, sectionTextField);
        //subSubRecitations1.setSpacing(60);
        subSubRecitations2.getChildren().addAll(instructorLabel, instructorTextField);
        //subSubRecitations2.setSpacing(38);
        subSubRecitations3.getChildren().addAll(recDayTimeLabel, dayTimeTextField);
        //subSubRecitations3.setSpacing(53);
        subSubRecitations4.getChildren().addAll(locationLabel, locationTextField);
        //subSubRecitations4.setSpacing(53);
        subSubRecitations5.getChildren().addAll(ta1Label, supervisingTA1ChoiceBox);
        //subSubRecitations5.setSpacing(9);
        subSubRecitations6.getChildren().addAll(ta2Label, supervisingTA2ChoiceBox);
        //subSubRecitations6.setSpacing(9);
        subSubRecitations7.getChildren().addAll(addUpdateRecitationsButton, clearRecitationsButton);
        

        recitations.getChildren().add(subRecitationsTitle);
        recitations.getChildren().add(recitationsTable);
        recitations.getChildren().add(subRecitations);
        
        ObservableList<Recitation> recitationData = data.getRecitationData().getRecitations();
        recitationsTable.setItems(recitationData);
        sectionColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("section")
        );
        instructorColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("instructor")
        );
        dayTimeColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("dayTime")
        );
        locationColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("location")
        );
        ta1Column.setCellValueFactory(
                new PropertyValueFactory<String, String>("firstTA")
        );
        ta2Column.setCellValueFactory(
                new PropertyValueFactory<String, String>("secondTA")
        );
        
        
        /////////////////////////////////////////////BELOW CONSISTS OF THE SCHEDULES TAB WORKSPACE INITIALIZATION//////////////////////////////////////////////////////////
                //OVERALL PANE FOR SCHEDULE TAB
        schedule = new VBox();
    //SUBPANES FOR THE SCHEDULE TAB
        calendarSchedule = new VBox();
        scheduleItems = new VBox();
   
    //SUBPANES FOR THE SUBPANES
        subCalendarSchedule1 = new HBox();
        subCalendarSchedule2 = new HBox();
        subScheduleItems1 = new HBox();
        subScheduleItems2 = new HBox();
        subScheduleItems3 = new HBox();
        subScheduleItems4 = new HBox();
        subScheduleItems5 = new HBox();
        subScheduleItems6 = new HBox();
        subScheduleItems7 = new HBox();
        subScheduleItems8 = new HBox();
        subScheduleItems9 = new HBox();
        subScheduleItems10 = new HBox();
    
    //STUFF FOR THE SCHEDULE TAB
        startingDay = new DatePicker();
        endingDay = new DatePicker();
        clearScheduleItemsFromTable = new Button(props.getProperty(CSGProp.DELETE_BUTTON_TEXT.toString()));
        scheduleTable = new TableView();
        typeColumn = new TableColumn<>(props.getProperty(CSGProp.TYPE_HEADER_TEXT.toString()));
        dateColumn = new TableColumn<>(props.getProperty(CSGProp.DATE_SCHED_HEADER_TEXT.toString()));
        titleColumn = new TableColumn<>(props.getProperty(CSGProp.TITLE_HEADER_TEXT.toString()));
        topicColumn = new TableColumn<>(props.getProperty(CSGProp.TOPIC_HEADER_TEXT.toString()));
        typeChoices = new ChoiceBox<>();
        
        typeChoices.getItems().addAll(props.getProperty(TYPE_CHOICE_HOLIDAY.toString()), props.getProperty(TYPE_CHOICE_LECTURE.toString()),
                props.getProperty(TYPE_CHOICE_REFERENCE.toString()), props.getProperty(TYPE_CHOICE_RECITATION.toString()), props.getProperty(TYPE_CHOICE_HW.toString()));
        
        
        typeChoices.setPrefWidth(150);
        currentDay = new DatePicker();
        currentDay.setPrefWidth(150);
        scheduleTime = new TextField();
        scheduleTime.setPrefWidth(150);
        scheduleTitle = new TextField();
        scheduleTitle.setPrefWidth(150);
        scheduleTopic = new TextField();
        scheduleTopic.setPrefWidth(150);
        scheduleLink = new TextField();
        scheduleLink.setPrefWidth(150);
        scheduleCriteria = new TextField();
        scheduleCriteria.setPrefWidth(150);
        addUpdateSchedulesButton = new Button(props.getProperty(CSGProp.ADD_BUTTON_TEXT_GENERIC));
        addUpdateSchedulesButton.setPrefWidth(150);
        clearSchedulesButton = new Button(props.getProperty(CSGProp.CLEAR_BUTTON_TEXT));
        clearSchedulesButton.setPrefWidth(100);
        
        scheduleTable.getColumns().addAll(typeColumn, dateColumn, titleColumn, topicColumn);
        
        scheduleTitleLabel = new Label(props.getProperty(CSGProp.SCHEDULE_TITLE));
        calendarBoundariesTitleLabel = new Label(props.getProperty(CSGProp.CALENDAR_BOUNDARIES_TITLE));
        scheduleStartDayLabel = new Label(props.getProperty(CSGProp.SCHEDULE_STARTING_DAY_TEXT));
        scheduleEndDayLabel = new Label(props.getProperty(CSGProp.SCHEDULE_ENDING_DAY_TEXT));
        schedule.getChildren().add(scheduleTitleLabel);
        calendarSchedule.getChildren().add(calendarBoundariesTitleLabel);
        subCalendarSchedule1.getChildren().addAll(scheduleStartDayLabel, startingDay, scheduleEndDayLabel, endingDay);
        subCalendarSchedule1.setSpacing(15);
        
        calendarSchedule.getChildren().add(subCalendarSchedule1);
        
        scheduleItemsTitleLabel = new Label(props.getProperty(CSGProp.SCHEDULE_ITEMS_TITLE));
        subScheduleItems1.getChildren().addAll(scheduleItemsTitleLabel, clearScheduleItemsFromTable);
        scheduleItems.getChildren().addAll(subScheduleItems1, scheduleTable);
        
        
        
        projectsTitleLabel = new Label(props.getProperty(CSGProp.PROJECTS_TITLE));
        addEditTitleLabelSchedule = new Label(props.getProperty(CSGProp.ADD_EDIT_TITLE));
        typeLabel = new Label(props.getProperty(CSGProp.TYPE_LABEL));
        typeLabel.setPrefWidth(150);
        dateLabel = new Label(props.getProperty(CSGProp.DATE_LABEL));
        dateLabel.setPrefWidth(150);
        timeLabel = new Label(props.getProperty(CSGProp.TIME_LABEL));
        timeLabel.setPrefWidth(150);
        titleLabel = new Label(props.getProperty(CSGProp.TITLE_LABEL));
        titleLabel.setPrefWidth(150);
        topicLabel = new Label(props.getProperty(CSGProp.TOPIC_LABEL));
        topicLabel.setPrefWidth(150);
        linkLabel = new Label(props.getProperty(CSGProp.LINK_LABEL));
        linkLabel.setPrefWidth(150);
        criteriaLabel = new Label(props.getProperty(CSGProp.CRITERIA_LABEL));
        criteriaLabel.setPrefWidth(150);
        subScheduleItems2.getChildren().addAll(typeLabel, typeChoices);
        //subScheduleItems2.setSpacing(33);
        subScheduleItems3.getChildren().addAll(dateLabel, currentDay);
       //subScheduleItems3.setSpacing(33);
        subScheduleItems4.getChildren().addAll(timeLabel, scheduleTime);
        //subScheduleItems4.setSpacing(33);
        subScheduleItems5.getChildren().addAll(titleLabel, scheduleTitle);
        //subScheduleItems5.setSpacing(26);
        subScheduleItems6.getChildren().addAll(topicLabel, scheduleTopic);
        //subScheduleItems6.setSpacing(26);
        subScheduleItems7.getChildren().addAll(linkLabel, scheduleLink);
        //subScheduleItems7.setSpacing(33);
        subScheduleItems8.getChildren().addAll(criteriaLabel, scheduleCriteria);
        //subScheduleItems8.setSpacing(5);
        subScheduleItems9.getChildren().addAll(addUpdateSchedulesButton, clearSchedulesButton);
        scheduleItems.getChildren().add(addEditTitleLabelSchedule);
        scheduleItems.getChildren().addAll(subScheduleItems2, subScheduleItems3, subScheduleItems4, subScheduleItems5, subScheduleItems6, 
                subScheduleItems7, subScheduleItems8, subScheduleItems9);
        schedule.getChildren().addAll(calendarSchedule, scheduleItems);
        
        
        ObservableList<ScheduleItem> scheduleItemsData = data.getScheduleData().getScheduleItems();
        scheduleTable.setItems(scheduleItemsData);
        typeColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("type")
        );
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("date")
        );
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("title")
        );
        topicColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("topic")
        );






//////////////////////////////////////////////////BELOW CONSISTS OF THE PROJECT DATA TAB WORKSPACE INITIALIZATION/////////////////////////////////////////////////////////
        projects = new VBox();
    //SUBPANES FOR THE PROJECTS TAB
        teams = new VBox();
        students = new VBox();
    
    //SUBPANES FOR THE TEAMS AND STUDENTS PANES
        subTeams1 = new HBox();
        subTeams2 = new HBox();
        subTeams3 = new HBox();
        subTeams4 = new HBox();
        subTeams5 = new HBox();
        teamColorOverlay = new StackPane();
        textColorOverlay = new StackPane();
        subStudents1 = new HBox();
        subStudents2 = new HBox();
        subStudents3 = new HBox();
        subStudents4 = new HBox();
        subStudents5 = new HBox();
        subStudents6 = new HBox();
    
    //STUFF FOR THE PROJECTS TAB

    //TEAMS SUBTAB
        projects.getChildren().add(projectsTitleLabel);
    
        clearTeamButton = new Button(props.getProperty(CSGProp.CLEAR_BUTTON_TEXT));
        clearTeamButton.setPrefWidth(100);
        teamsTable = new TableView();
        teamNameColumn = new TableColumn<>(props.getProperty(CSGProp.TEAM_NAME_HEADER_TEXT));
        teamColorColumn = new TableColumn<>(props.getProperty(CSGProp.TEAM_COLOR_HEADER_TEXT));
        teamTextColorColumn = new TableColumn<>(props.getProperty(CSGProp.TEAM_TEXT_COLOR_HEADER_TEXT));
        teamLinkColumn = new TableColumn<>(props.getProperty(CSGProp.TEAM_LINK_HEADER_TEXT));
        
        teamNameColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.1));
        teamColorColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.15));
        teamTextColorColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.15));
        teamLinkColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.1));
    
        teamNameField = new TextField();
        teamColorCircle = new Circle();
        textColorCircle = new Circle();
        teamLink = new TextField();
        teamLink.setPrefWidth(300);
        addUpdateTeamButton = new Button(props.getProperty(CSGProp.ADD_BUTTON_TEXT_GENERIC));
        addUpdateTeamButton.setPrefWidth(150);
        clearTeamsFromTableButton = new Button(props.getProperty(CSGProp.DELETE_BUTTON_TEXT));
        
    //STUDENTS SUBTAB
        clearStudentsFromTableButton = new Button(props.getProperty(CSGProp.DELETE_BUTTON_TEXT));
        studentsTable = new TableView();
        studentFirstNameColumn = new TableColumn<>(props.getProperty(CSGProp.STUDENT_FIRST_HEADER_TEXT));
        studentLastNameColumn = new TableColumn<>(props.getProperty(CSGProp.STUDENT_LAST_HEADER_TEXT));
        studentTeamColumn = new TableColumn<>(props.getProperty(CSGProp.STUDENT_TEAM_HEADER_TEXT));
        studentRoleColumn = new TableColumn<>(props.getProperty(CSGProp.STUDENT_ROLE_HEADER_TEXT));
        studentFirstNameColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.1));
        studentLastNameColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.1));
        studentTeamColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.1));
        studentRoleColumn.prefWidthProperty().bind(siteTemplateTable.widthProperty().multiply(0.1));
    
        studentFirstName = new TextField();
        studentFirstName.setPrefWidth(150);
        studentLastName = new TextField();
        studentLastName.setPrefWidth(150);
        studentTeam = new ChoiceBox<>();
        studentTeam.setPrefWidth(150);
        studentRole = new TextField();
        studentRole.setPrefWidth(150);
        addUpdateStudentButton = new Button(props.getProperty(CSGProp.ADD_BUTTON_TEXT_GENERIC));
        addUpdateStudentButton.setPrefWidth(150);
        clearStudentButton = new Button(props.getProperty(CSGProp.CLEAR_BUTTON_TEXT));
        clearStudentButton.setPrefWidth(100);
        
        addEditTitleLabelTeams = new Label(props.getProperty(CSGProp.ADD_EDIT_TITLE));
        teamTitleLabel = new Label(props.getProperty(CSGProp.TEAMS_TITLE));
        teamNameLabel = new Label(props.getProperty(CSGProp.TEAM_NAME_LABEL));
        subTeams1.getChildren().addAll(teamTitleLabel, clearTeamsFromTableButton);
        subTeams2.getChildren().addAll(teamNameLabel, teamNameField);
        subTeams2.setSpacing(10);
        teamColorTextField = new TextField();
        teamColorTextField.setText("#ffffff");
        teamColorTextField.setMaxWidth(70);
        textColorTextField = new TextField();
        textColorTextField.setText("#ffffff");
        textColorTextField.setMaxWidth(70);
        teamColorCircle.setRadius(50);
        teamColorCircle.setFill(Color.web("#ffffff"));
        textColorCircle.setRadius(50);
        textColorCircle.setFill(Color.web("#ffffff"));
        
        teamColorOverlay.getChildren().addAll(teamColorCircle, teamColorTextField);
        textColorOverlay.getChildren().addAll(textColorCircle, textColorTextField);
        teamColorLabel = new Label(props.getProperty(CSGProp.TEAM_COLOR_LABEL));
        teamTextColorLabel = new Label(props.getProperty(CSGProp.TEAM_TEXT_COLOR_LABEL));
        teamLinkLabel = new Label(props.getProperty(CSGProp.TEAM_LINK_LABEL));
        subTeams3.getChildren().addAll(teamColorLabel, teamColorOverlay,teamTextColorLabel, textColorOverlay);
        subTeams4.getChildren().addAll(teamLinkLabel, teamLink);
        subTeams4.setSpacing(10);
        subTeams5.getChildren().addAll(addUpdateTeamButton, clearTeamButton);

        teams.getChildren().addAll(subTeams1);
        teamsTable.getColumns().addAll(teamNameColumn, teamColorColumn, teamTextColorColumn, teamLinkColumn);
        teams.getChildren().add(teamsTable);
        teams.getChildren().add(addEditTitleLabelTeams);
        teams.getChildren().addAll(subTeams2, subTeams3, subTeams4, subTeams5);
        
        addEditTitleLabelStudents = new Label(props.getProperty(CSGProp.ADD_EDIT_TITLE));
        studentTitleLabel = new Label(props.getProperty(CSGProp.STUDENTS_TITLE));
        studentFirstLabel = new Label(props.getProperty(CSGProp.STUDENT_FIRST_LABEL));
        studentFirstLabel.setPrefWidth(150);
        studentLastLabel = new Label(props.getProperty(CSGProp.STUDENT_LAST_LABEL));
        studentLastLabel.setPrefWidth(150);
        studentTeamLabel = new Label(props.getProperty(CSGProp.STUDENT_TEAM_LABEL));
        studentTeamLabel.setPrefWidth(150);
        studentRoleLabel = new Label(props.getProperty(CSGProp.STUDENT_ROLE_LABEL));
        studentRoleLabel.setPrefWidth(150);
        subStudents1.getChildren().addAll(studentTitleLabel, clearStudentsFromTableButton);
        studentsTable.getColumns().addAll(studentFirstNameColumn, studentLastNameColumn, studentTeamColumn, studentRoleColumn);
        students.getChildren().addAll(subStudents1, studentsTable, addEditTitleLabelStudents);
        subStudents2.getChildren().addAll(studentFirstLabel, studentFirstName);
        //subStudents2.setSpacing(15);
        subStudents3.getChildren().addAll(studentLastLabel, studentLastName);
        //subStudents3.setSpacing(22);
        subStudents4.getChildren().addAll(studentTeamLabel, studentTeam);
        //subStudents4.setSpacing(58);
        subStudents5.getChildren().addAll(studentRoleLabel, studentRole);
        //subStudents5.setSpacing(58);
        subStudents6.getChildren().addAll(addUpdateStudentButton, clearStudentButton);
        
        students.getChildren().addAll(subStudents2,subStudents3, subStudents4, subStudents5, subStudents6);

        projects.getChildren().addAll(teams, students);
        
        ObservableList<Team> teamTableData = data.getProjectData().getTeams();
        teamsTable.setItems(teamTableData);
        teamNameColumn.setCellValueFactory(
            new PropertyValueFactory<String, String>("teamName")
        );
        teamColorColumn.setCellValueFactory(
            new PropertyValueFactory<String, String>("teamColor")
        );
        teamTextColorColumn.setCellValueFactory(
            new PropertyValueFactory<String, String>("textColor")
        );
        teamLinkColumn.setCellValueFactory(
            new PropertyValueFactory<String, String>("link")
        );
        
        ObservableList<Student> studentTableData = data.getProjectData().getStudents();
        studentsTable.setItems(studentTableData);
        studentFirstNameColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("firstName") //SETS THE FIRST NAME COLUMN OF THE STUDENTS TABLE WITH THE firstName PARAMETER OF THE STUDENT OBJECT IS studentTableData.
        );
        studentLastNameColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("lastName") //SETS THE LAST NAME COLUMN OF THE STUDENTS TABLE WITH THE lastName PARAMETER OF THE STUDENT OBJECT IS studentTableData.
        );
        studentTeamColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("team") //SETS THE TEAM COLUMN OF THE STUDENTS TABLE WITH THE team NAME PARAMETER OF THE STUDENT OBJECT IS studentTableData.
        );
        studentRoleColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("role") //SETS THE ROLE COLUMN OF THE STUDENTS TABLE WITH THE role PARAMETER OF THE STUDENT OBJECT IS studentTableData.
        );
        
        
        ///////////////////////////////////////////////////BELOW CONSISTS OF THE TA DATA TAB WORKSPACE INITIALIZATION////////////////////////////////////////////////////
        // INIT THE HEADER ON THE LEFT
        tasHeaderBox = new HBox();
        String tasHeaderText = props.getProperty(CSGProp.TAS_HEADER_TEXT.toString());
        deleteTAButton = new Button(props.getProperty(CSGProp.DELETE_BUTTON_TEXT));
        tasHeaderLabel = new Label(tasHeaderText);
        tasHeaderBox.getChildren().add(tasHeaderLabel);
        tasHeaderBox.getChildren().add(deleteTAButton);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        taTable = new TableView();
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
       
        ObservableList<TeachingAssistant> tableData = data.getTAData().getTeachingAssistants();
        taTable.setItems(tableData);
        String nameColumnText = props.getProperty(CSGProp.NAME_COLUMN_TEXT.toString());
        String emailColumnText = props.getProperty(CSGProp.EMAIL_COLUMN_TEXT.toString());
        undergradColumn = new TableColumn(props.getProperty(CSGProp.UNDERGRAD_COLUMN_TEXT));
        nameColumn = new TableColumn(nameColumnText);
        emailColumn = new TableColumn(emailColumnText);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("name")
        );
        
        undergradTACheckBox = new CheckBoxTableCell();

        undergradColumn.setEditable(true);
        taTable.setEditable(true);
        
        undergradColumn.setCellValueFactory(
                new Callback<CellDataFeatures<TeachingAssistant, Boolean>, ObservableValue<Boolean>>(){
                    @Override
                    public ObservableValue<Boolean> call(CellDataFeatures<TeachingAssistant, Boolean> param) {
                        BooleanProperty taBoolean = param.getValue().isUndergrad();
//                        taBoolean.setValue(!taBoolean.getValue());
//                        System.out.print(taBoolean);
//                        undergradTACheckBox.setDisable(taBoolean.getValue());
                        return taBoolean;
                    }
                }
        );
        undergradColumn.setCellFactory(undergradTACheckBox.forTableColumn(undergradColumn));
        
//        undergradTACheckBox.setOnMouseClicked(e ->{
//            TeachingAssistant currentTA = (TeachingAssistant) taTable.getSelectionModel().getSelectedItem();
//            currentTA.setUndergrad(!currentTA.isUndergrad().getValue());
//        });
        
//
//        Callback<TableColumn<TeachingAssistant, Boolean>, TableCell<TeachingAssistant, Boolean>> callback = (TableColumn<TeachingAssistant, Boolean> s) -> new TACheckBox(false);
//              
//       
//        undergradColumn.setCellValueFactory(cellData -> cellData.getValue().isUndergrad());
//        undergradColumn.setCellFactory(callback);
//
//        if(taTable.getSelectionModel().getSelectedItem() != null){
//            
//            TeachingAssistant currentTA = (TeachingAssistant) taTable.getSelectionModel().getSelectedItem();
//            
//            if(currentTA != null){
//                Callback<TableColumn<TeachingAssistant, Boolean>, TableCell<TeachingAssistant, Boolean>> callback = (TableColumn<TeachingAssistant, Boolean> s) -> new TACheckBox(currentTA.isUndergrad().getValue());
//                undergradColumn.setCellValueFactory(cellData -> cellData.getValue().isUndergrad());
//                undergradColumn.setCellFactory(callback);
//            }
//        }
//        else if(data.getTeachingAssistants().size() > 0){
//            TeachingAssistant currentTA = (TeachingAssistant) data.getTeachingAssistants().get(data.getTeachingAssistants().size()-1);
//            
//                        
//            if(currentTA != null){
//                Callback<TableColumn<TeachingAssistant, Boolean>, TableCell<TeachingAssistant, Boolean>> callback = (TableColumn<TeachingAssistant, Boolean> s) -> new TACheckBox(currentTA.isUndergrad().getValue());
//                undergradColumn.setCellValueFactory(cellData -> cellData.getValue().isUndergrad());
//                undergradColumn.setCellFactory(callback);
//            }
//        }
//        if(currentTA != null){
//            Callback<TableColumn<TeachingAssistant, Boolean>, TableCell<TeachingAssistant, Boolean>> callback = (TableColumn<TeachingAssistant, Boolean> s) -> new TACheckBox(currentTA.isUndergrad().getValue());
//            undergradColumn.setCellValueFactory(cellData -> cellData.getValue().isUndergrad());
//            undergradColumn.setCellFactory(callback);
//        }

//        else{
//            ObservableList<TeachingAssistant> currentTAList = data.getTeachingAssistants();
//            TeachingAssistant currentTA = currentTAList.get(currentTAList.size()-1);
//            Callback<TableColumn<TeachingAssistant, Boolean>, TableCell<TeachingAssistant, Boolean>> callback = (TableColumn<TeachingAssistant, Boolean> s) -> new TACheckBox(currentTA.isUndergrad().getValue());
//            undergradColumn.setCellValueFactory(cellData -> cellData.getValue().isUndergrad());
//            undergradColumn.setCellFactory(callback);
//        }
        
        
        
        
        undergradTACheckBox.selectedProperty().addListener(new ChangeListener<Boolean>(){
            TeachingAssistant currentTA = (TeachingAssistant) taTable.getSelectionModel().getSelectedItem();
            
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                currentTA.setUndergrad(!currentTA.isUndergrad().getValue());
            }
                    
        });

        
        taTable.getColumns().add(undergradColumn);
        taTable.getColumns().add(nameColumn);
        taTable.getColumns().add(emailColumn);

        // ADD BOX FOR ADDING A TA
        String namePromptText = props.getProperty(CSGProp.NAME_PROMPT_TEXT.toString());
        String addButtonText = props.getProperty(CSGProp.ADD_BUTTON_TEXT.toString());
        String emailPromptText = props.getProperty(CSGProp.EMAIL_PROMPT_TEXT.toString());
        String clearButtonText = props.getProperty(CSGProp.CLEAR_BUTTON_TEXT.toString());
        String submitButtonText = props.getProperty(CSGProp.SUBMIT_BUTTON_TEXT.toString());
        String startHourText = props.getProperty(CSGProp.START_TIME_TEXT.toString());
        String endHourText = props.getProperty(CSGProp.END_TIME_TEXT.toString());
        String comboBoxHeaderLabelText = props.getProperty(CSGProp.COMBO_BOX_HEADER_LABEL_TEXT.toString());
        nameTextField = new TextField();
        nameTextField.setPromptText(namePromptText);
        addButton = new Button(addButtonText);
        addBox = new HBox();
        
        clearButton = new Button(clearButtonText);
        clearButton.setDisable(true);
        clearButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        nameTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.3));
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        addBox.getChildren().add(nameTextField);
       

        emailTextField = new TextField();
        emailTextField.setPromptText(emailPromptText);
        emailTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.3));
        
        addBox.getChildren().add(emailTextField);  
        
        addBox.getChildren().add(addButton);
        
        addBox.getChildren().add(clearButton);
        
        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        String officeHoursGridText = props.getProperty(CSGProp.OFFICE_HOURS_SUBHEADER.toString());
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);
        
        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();

        
        // ORGANIZE THE LEFT AND RIGHT PANES
        VBox leftPane = new VBox();
        leftPane.getChildren().add(tasHeaderBox);        
        leftPane.getChildren().add(taTable);        
        leftPane.getChildren().add(addBox);
        VBox rightPane = new VBox();
        rightPane.getChildren().add(officeHoursHeaderBox);
        rightPane.getChildren().add(officeHoursGridPane); 
        
        VBox rightPane2 = new VBox();
        HBox comboBoxHeader = new HBox();
        HBox comboBoxPaneTitles = new HBox(15);
        HBox comboBoxPane = new HBox();
        VBox comboSubmitButtonPane = new VBox();
        choices = FXCollections.observableArrayList(
                "12:00am", "1:00am", "2:00am","3:00am", "4:00am", "5:00am","6:00am",
                "7:00am", "8:00am",  "9:00am", "10:00am",  "11:00am", 
                "12:00pm", "1:00pm", "2:00pm","3:00pm", "4:00pm", "5:00pm","6:00pm",
                 "7:00pm", "8:00pm", "9:00pm","10:00pm",  "11:00pm"
        );
        
        choiceBox1 = new ChoiceBox<>();
        choiceBox2 = new ChoiceBox<>();
        startTitle = new Label(startHourText);
        endTitle = new Label(endHourText);
        comboBoxHeaderLabel = new Label(comboBoxHeaderLabelText);
        
        rightPane2.getChildren().add(comboBoxHeader);
        rightPane2.getChildren().add(comboBoxPaneTitles);
        rightPane2.getChildren().add(comboBoxPane);
        rightPane2.getChildren().add(comboSubmitButtonPane);
        comboBoxPaneTitles.getChildren().add(startTitle);
        comboBoxPaneTitles.getChildren().add(endTitle);
        comboBoxHeader.getChildren().add(comboBoxHeaderLabel);
        
        comboBoxPane.getChildren().add(choiceBox1);
        comboBoxPane.getChildren().add(choiceBox2);
        rightPane2.setMaxWidth(150);
        submitButton = new Button(submitButtonText);
        comboSubmitButtonPane.getChildren().add(submitButton);

        choiceBox1.getItems().addAll(choices);
        choiceBox2.getItems().addAll(choices);    
        

        
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        sPane = new SplitPane(leftPane, new ScrollPane(rightPane), rightPane2);
        taDataPane = new VBox();
        taDataPane.getChildren().add(sPane);
        taPane = new VBox();
        taPane.getChildren().add(taDataPane);

        // MAKE SURE THE TABLE EXTENDS DOWN FAR ENOUGH
        //taTable.prefHeightProperty().bind(workspace.heightProperty().multiply(1.9));
        taTable.setPrefHeight(550);
        
               /////////////////////////////////////////////////////////////INITIALIZE ALL THE TABS AND THUS ALL THE WORKSPACES FOR EACH TAB/////////////////////////////////////////////////////////////
        tabPane = new TabPane();
        tabPane.setTabMinWidth(120);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        tab1 = new Tab();
        tab2 = new Tab();
        tab3 = new Tab();
        tab4 = new Tab();
        tab5 = new Tab();
        tab1.setText(props.getProperty(CSGProp.TAB1_TITLE));
        tab2.setText(props.getProperty(CSGProp.TAB2_TITLE));
        tab3.setText(props.getProperty(CSGProp.TAB3_TITLE));
        tab4.setText(props.getProperty(CSGProp.TAB4_TITLE));
        tab5.setText(props.getProperty(CSGProp.TAB5_TITLE));
        

        
        //ADD ALL STUFF TO THE FIRST TAB
        tab1.setContent(courseDetails);
        tab2.setContent(taDataPane);
        tab3.setContent(recitations);
        tab4.setContent(schedule);
        tab5.setContent(projects);
        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);
        
        
        VBox workspacePane = new VBox();
        workspacePane.getChildren().add(tabPane);
        
        
        //ADD PANE TO THE WORKSPACE SO THAT IT CAN SHOW
        workspace = new BorderPane();
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(workspacePane);
        
///////////////////////////////////////////////////////////////////////////////ABOUT BUTTON FOR INFO//////////////////////////////////////////////////////////////////////////////////////////
        app.getGUI().getAboutButton().setOnAction(e -> {
            Stage dialogStage = new Stage();
            dialogStage.setWidth(150);
            dialogStage.setHeight(200);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Label about = new Label(props.getProperty(CSGProp.ABOUT_BLURB));
            about.setAlignment(Pos.CENTER);
            about.setWrapText(true);
            VBox vbox = new VBox(about);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(15));
            Button ok = new Button("OK");
            vbox.getChildren().add(ok);
            dialogStage.setScene(new Scene(vbox));
            dialogStage.show();
        });
        
/////////////////////////////////////////////////////////////////////////////////EVENT HANDLING OF TA COMPONENTS///////////////////////////////////////////////////////////////////////////////        
        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new TAController(app);

        // CONTROLS FOR ADDING TAs
        nameTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        
        emailTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        
        addButton.setOnAction(e -> {
            controller.handleAddTA();
        });
        
        deleteTAButton.setOnAction(e ->{
            controller.handleRemoveTA();
        });

        // CONTROL FOR REMOVING TAs
        taTable.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.BACK_SPACE)){
                controller.handleRemoveTA();
            }
            recitationsTable.refresh();
        });
        
        
        
        
        //CONTROL FOR UNDO REDO

        workspace.setOnKeyPressed(e -> {
            if(e.isControlDown()){
                if(e.getCode().equals(KeyCode.Z)){
                    //UNDO TA PANE
                    if(tabPane.getSelectionModel().getSelectedItem() == tab2){
                        app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                        transactionComponent.undoTransaction();
                    }
                    //UNDO PROJECTS PANE
                    if(tabPane.getSelectionModel().getSelectedItem() == tab5){
                        app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                        projectsTransactionComponent.undoTransaction();
                    }
                    //UNDO SCHEDULE PANE
                    if(tabPane.getSelectionModel().getSelectedItem() == tab4){
                        app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                        scheduleTransactionComponent.undoTransaction();
                    }
                    //UNDO RECITATION PANE
                    if(tabPane.getSelectionModel().getSelectedItem() == tab3){
                        app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                        recitationTransactionComponent.undoTransaction();
                    }
                }
                if(e.getCode().equals(KeyCode.Y)){
                    //DO TA PANE
                    if(tabPane.getSelectionModel().getSelectedItem() == tab2){
                        app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                        transactionComponent.doTransaction();
                    }
                    //DO PROJECTS PANE
                    if(tabPane.getSelectionModel().getSelectedItem() == tab5){
                        app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                        projectsTransactionComponent.doTransaction();
                    }
                    //DO SCHEDULE PANE
                    if(tabPane.getSelectionModel().getSelectedItem() == tab4){
                        app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                        scheduleTransactionComponent.doTransaction();
                    }
                    //DO RECITATION PANE
                    if(tabPane.getSelectionModel().getSelectedItem() == tab3){
                        app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                        recitationTransactionComponent.doTransaction();
                    }
                }
            }
        });
        
        //CONTROL FOR UNDO REDO WITH THE BUTTONS ON THE APP UI
        app.getGUI().getUndoButton().setOnAction(e -> {
            //UNDO TA PANE
            if(tabPane.getSelectionModel().getSelectedItem() == tab2){
                app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                transactionComponent.undoTransaction();
            }
            //UNDO PROJECTS PANE
            if(tabPane.getSelectionModel().getSelectedItem() == tab5){
                app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                projectsTransactionComponent.undoTransaction();
            }
            //UNDO SCHEDULE PANE
            if(tabPane.getSelectionModel().getSelectedItem() == tab4){
                app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                scheduleTransactionComponent.undoTransaction();
            }
            //UNDO RECITATION PANE
            if(tabPane.getSelectionModel().getSelectedItem() == tab3){
                app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                recitationTransactionComponent.undoTransaction();
            }
            
        });
        
        app.getGUI().getRedoButton().setOnAction(e -> {
            //DO TA PANE
            if(tabPane.getSelectionModel().getSelectedItem() == tab2){
                app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                transactionComponent.doTransaction();
            }
            //DO PROJECTS PANE
            if(tabPane.getSelectionModel().getSelectedItem() == tab5){
                app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                projectsTransactionComponent.doTransaction();
            }
            //DO SCHEDULE PANE
            if(tabPane.getSelectionModel().getSelectedItem() == tab4){
                app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                scheduleTransactionComponent.doTransaction();
            }
            //DO RECITATION PANE
            if(tabPane.getSelectionModel().getSelectedItem() == tab3){
                app.getGUI().getAppFileController().markAsEdited(app.getGUI());
                recitationTransactionComponent.doTransaction();
            }
        });

        
        //CONTROL FOR TA NAME AND EMAIL LOADING INTO TEXT FIELDS
        taTable.setOnMouseClicked(e -> {

            Object selectedItem = taTable.getSelectionModel().getSelectedItem();
            // GET THE TA
            if(selectedItem != null && (!data.getTAData().getTeachingAssistants().isEmpty())){
                TeachingAssistant ta = (TeachingAssistant)selectedItem;
                String taName = ta.getName();
                String email = ta.getEmail();
            
                nameTextField.setText(taName);
                emailTextField.setText(email);
            
                addButton.setText(props.getProperty(CSGProp.UPDATE_BUTTON_TEXT_GENERIC.toString()));
                clearButton.setDisable(false);
                addButton.setOnAction(e2 -> {
                    controller.handleEditTA();
                    recitationsTable.refresh();
                });
            }
            else{
                nameTextField.setText("");
                emailTextField.setText("");
                addButton.setText(addButtonText);
                clearButton.setDisable(true);
                addButton.setOnAction(e2 -> {
                    controller.handleAddTA();
                    recitationsTable.refresh();
                });
            }

        });
        
        //CONTROL FOR NEW BUTTON CALLED CLEAR
        
        clearButton.setOnAction(e -> {
            nameTextField.setText("");
            emailTextField.setText("");
            
            addButton.setText(addButtonText);
            clearButton.setDisable(true);
            addButton.setOnAction(e2 -> {
                controller.handleAddTA();
                recitationsTable.refresh();
            });
            nameTextField.requestFocus();
        });
    
        //CONTROL FOR RESIZING THE TA GRID
        
        submitButton.setOnAction(e -> {
            
                boolean isEmpty = true;
                String startTime = choiceBox1.getValue();
                String endTime = choiceBox2.getValue();
                String startHour = startTime.split(":")[0];
                if(startTime.contains("pm") && !startTime.contains("12")){
                    int startHourNum = Integer.parseInt(startHour);
                    startHourNum += 12;
                    startHour = Integer.toString(startHourNum);
                }
                if(startTime.contains("12") && startTime.contains("am")){
                    startHour = "0";
                }
                String endHour = endTime.split(":")[0];
                if(endTime.contains("pm") && !endTime.contains("12")){
                    int endHourNum = Integer.parseInt(endHour);
                    endHourNum += 12;
                    endHour = Integer.toString(endHourNum);
                }
                if(endTime.contains("12") && endTime.contains("am")){
                    endHour = "0";
                }

                if(Integer.parseInt(startHour) < Integer.parseInt(endHour)){ 
                   
                        //IF THE COL_ROW CONTAINS DATA, THROW THE YESNOMESSAGESINGLETON
                    String[] days = new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
                    
                    for(int i = 0; i < days.length; i++){
                        //LOOP THROUGH CELLS FROM ORIGINAL START TIME TO NEW START TIME TO SEE IF IT CONTAINS ANY DATA
                        //IF THERE IS DATA, SET BOOLEAN TO FALSE AND THROW A PROMPT TO USER TO CONFIRM DELETION.
                        if(Integer.parseInt(startHour) > data.getTAData().getStartHour()){
                            for(int j = getSelectionIndex(choices, data.getTAData().getTimeString(data.getTAData().getStartHour(), controller.getStartTimeOnHour())); 
                                j < getSelectionIndex(choices, choiceBox1.getValue()); j++){
                                
                                String key = data.getTAData().getCellKey(days[i], choices.get(j).replace(":", "_"));

                                if(/*data.getOfficeHours().get(key) != null ||*/ !data.getTAData().getOfficeHours().get(key).getValue().equals("")){
                                    isEmpty = false;
                                    break;
                                }
                            }
                        }
                        //LOOP THROUGH CELLS FROM ORIGINAL END TIME TO NEW END TIME TO SEE IF IT CONTAINS ANY DATA
                        //IF THERE IS DATA, SET BOOLEAN TO FALSE AND THROWN A PROMPT TO USER TO CONFIRM DELETION
                        if(Integer.parseInt(endHour) < data.getTAData().getEndHour()){
                            
                            for(int j = getSelectionIndex(choices, choiceBox2.getValue()); 
                                    j < getSelectionIndex(choices, data.getTAData().getTimeString(data.getTAData().getEndHour(), controller.getEndTimeOnHour())); j++){
                                                        
                                String key = data.getTAData().getCellKey(days[i], choices.get(j).replace(":", "_"));

                                if(data.getTAData().getOfficeHours().get(key) == null || !data.getTAData().getOfficeHours().get(key).getValue().equals("")){
                                    isEmpty = false;
                                    break;
                                }
                            }
                        }
                    }
                    
                    if(isEmpty == false){
                        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
                        yesNoDialog.show(props.getProperty(START_END_WARNING_TITLE), props.getProperty(START_END_WARNING_MESSAGE));
        
                        // AND NOW GET THE USER'S SELECTION
                        String selection = yesNoDialog.getSelection();

                        // IF THE USER SAID YES, THEN DO ACTION
                        if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {
                            
                            StartEnd_Transaction transaction = new StartEnd_Transaction(data.getTAData().getOfficeHours(), 
                                    data.getTAData().getTimeString(data.getTAData().getStartHour(), true), data.getTAData().getTimeString(data.getTAData().getEndHour(), true),
                                    choiceBox1.getValue(), choiceBox2.getValue(), app);
                            transactionComponent.addTransaction(transaction);
                            controller.resetOHGrid(choiceBox1.getValue(), choiceBox2.getValue());

                        }
                        //OTHERWISE, NOTHING
                    }
                    else{
                        StartEnd_Transaction transaction= new StartEnd_Transaction(data.getTAData().getOfficeHours(), data.getTAData().getTimeString(data.getTAData().getStartHour(), true), data.getTAData().getTimeString(data.getTAData().getEndHour(), true), 
                                choiceBox1.getValue(), choiceBox2.getValue(), app);
                        transactionComponent.addTransaction(transaction);
                        controller.resetOHGrid(choiceBox1.getValue(), choiceBox2.getValue());
                    }
                }
                    
                //IF THE END HOUR IS EARLIER THAN THE START HOUR, THROW EXCEPTION
                else{
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(INVALID_START_END_TITLE), props.getProperty(INVALID_START_END_MESSAGE));
                }
               
        });
        
//////////////////////////////////////////////////////////////////////////EVENT HANDLING FOR TEAMS AND STUDENTS TAB/////////////////////////////////////////////////////////////////////////

        projectsController = new ProjectsController(app);

     
        addUpdateTeamButton.setOnAction(e -> {
              projectsController.handleAddTeams(teamNameField, teamColorTextField, textColorTextField, teamLink, teamColorCircle, textColorCircle);
        });
        
        teamColorTextField.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.ENTER)){
                String teamColorValue = teamColorTextField.getText();
                if(teamColorValue != null){
                    teamColorCircle.setFill(Color.web(teamColorValue));
                }
            }
        });
        
        textColorTextField.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.ENTER)){
                String textColorValue = textColorTextField.getText();
                if(textColorValue != null){
                    textColorCircle.setFill(Color.web(textColorValue));
                }
            }
        });
        
        teamsTable.setOnMouseClicked(e -> {
            Object selectedItem = teamsTable.getSelectionModel().getSelectedItem();
            // GET THE TEAM
            if(selectedItem != null && (!data.getProjectData().getTeams().isEmpty())){
                Team team = (Team)selectedItem;
                String teamName = team.getTeamName();
                String teamColorValue = team.getTeamColor();
                String textColorValue = team.getTextColor();
                String teamLinkValue = team.getLink();
            
                teamNameField.setText(teamName);
                teamColorTextField.setText(teamColorValue);
                textColorTextField.setText(textColorValue);
                if(teamColorValue != null && textColorValue != null){
                    teamColorCircle.setFill(Color.web(teamColorValue));
                    textColorCircle.setFill(Color.web(textColorValue));
                }
                teamLink.setText(teamLinkValue);
                
                addUpdateTeamButton.setText(props.getProperty(CSGProp.UPDATE_BUTTON_TEXT_GENERIC));
                
                addUpdateTeamButton.setOnAction(e2 -> {    
                    projectsController.handleEditTeams(teamsTable, teamNameField, teamColorTextField, textColorTextField, teamLink, teamColorCircle, textColorCircle);
                    teamsTable.getSelectionModel().clearSelection();
                    studentsTable.refresh();
                });
            }
        });
        
        //ONCE THE CLEAR BUTTON IS CLICKED, THE TABLE ROW SHOULD BE DECSELECTED AS WELL.
        clearTeamButton.setOnAction(e -> {
            teamsTable.getSelectionModel().clearSelection();
            teamNameField.setText("");
            teamColorTextField.setText("#ffffff");
            textColorTextField.setText("#ffffff");
            teamColorCircle.setFill(Color.web("#ffffff"));
            textColorCircle.setFill(Color.web("#ffffff"));

            teamLink.setText("");
            teamNameField.requestFocus();
            addUpdateTeamButton.setText(props.getProperty(CSGProp.ADD_BUTTON_TEXT_GENERIC));
            addUpdateTeamButton.setOnAction(e2 -> {
               projectsController.handleAddTeams(teamNameField, teamColorTextField, textColorTextField, teamLink, teamColorCircle, textColorCircle);
               studentsTable.refresh();
            });
        });
        
        clearTeamsFromTableButton.setOnAction(e -> {
            Object selectedItem = teamsTable.getSelectionModel().getSelectedItem();
            // GET THE TEAM
            if(selectedItem != null && (!data.getProjectData().getTeams().isEmpty())){
                Team team = (Team)selectedItem;  
                projectsController.handleRemoveTeams(teamsTable, teamNameField, teamColorTextField, textColorTextField, teamLink, teamColorCircle, textColorCircle);
                teamNameField.requestFocus();
                teamsTable.getSelectionModel().clearSelection();
            }
        });
        
         // CONTROL FOR REMOVING TEAMS
        teamsTable.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.BACK_SPACE)){
                projectsController.handleRemoveTeams(teamsTable, teamNameField, teamColorTextField, textColorTextField, teamLink, teamColorCircle, textColorCircle);
                teamNameField.requestFocus();
                teamsTable.getSelectionModel().clearSelection();
            }
        });
        
        addUpdateStudentButton.setOnAction(e -> {
            projectsController.handleAddStudents(studentFirstName, studentLastName, studentTeam, studentRole);
        });
        
        
        studentsTable.setOnMouseClicked(e -> {
            Object selectedItem = studentsTable.getSelectionModel().getSelectedItem();
            // GET THE STUDENT
            if(selectedItem != null && (!data.getProjectData().getStudents().isEmpty())){
                Student student = (Student)selectedItem;
                String firstName = student.getFirstName();
                String lastName = student.getLastName();
                String studentTeamName = student.getTeam();
                String studentRoleName = student.getRole();
            
                studentFirstName.setText(firstName);
                studentLastName.setText(lastName);
                if(studentTeamChoices.contains(studentTeamName)){
                    studentTeam.getSelectionModel().select(studentTeamName);
                }
                studentRole.setText(studentRoleName);
                
                addUpdateStudentButton.setText(props.getProperty(CSGProp.UPDATE_BUTTON_TEXT_GENERIC));
                
                addUpdateStudentButton.setOnAction(e2 -> {
                    projectsController.handleEditStudent(studentsTable, studentFirstName, studentLastName, studentTeam, studentRole);
                    studentsTable.getSelectionModel().clearSelection();
                });
            }
        });
        
        //CONTROL FOR REMOVING STUDENTS
        clearStudentsFromTableButton.setOnAction(e -> {
            Object selectedItem = studentsTable.getSelectionModel().getSelectedItem();
            // GET THE STUDENT
            if(selectedItem != null && (!data.getProjectData().getStudents().isEmpty())){
                Student student = (Student)selectedItem;  
                projectsController.handleRemoveStudent(studentsTable, studentFirstName, studentLastName, studentTeam, studentRole);
                studentFirstName.requestFocus();
                studentsTable.getSelectionModel().clearSelection();
            }
        });
        
        // CONTROL FOR REMOVING STUDENTS
        studentsTable.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.BACK_SPACE)){
                projectsController.handleRemoveStudent(studentsTable, studentFirstName, studentLastName, studentTeam, studentRole);
                studentFirstName.requestFocus();
                studentsTable.getSelectionModel().clearSelection();
            }
        });
        
        clearStudentButton.setOnAction(e -> {
            studentsTable.getSelectionModel().clearSelection();
            studentFirstName.setText("");
            studentLastName.setText("");
            studentTeam.getSelectionModel().select(null);
            studentRole.setText("");
            
            studentFirstName.requestFocus();
            
            addUpdateStudentButton.setText(props.getProperty(CSGProp.ADD_BUTTON_TEXT_GENERIC));
            addUpdateStudentButton.setOnAction(e2 -> {
               projectsController.handleAddStudents(studentFirstName, studentLastName, studentTeam, studentRole);
            });
        });
        
/////////////////////////////////////////////////////////////////////////CONTROLS FOR HANDLING THE SCHEDULES TAB/////////////////////////////////////////////////////////////////////////////////

        scheduleController = new ScheduleController(app);

        startingDay.setOnAction(e -> {
            LocalDate date = startingDay.getValue();
            try {
                scheduleController.handleSetStartMonday(date);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        
        endingDay.setOnAction(e -> {
            LocalDate date = endingDay.getValue();
            try {
                scheduleController.handleSetEndFriday(date);
            } catch (Exception exception){
                exception.printStackTrace();
            }
        });
        
        
        addUpdateSchedulesButton.setOnAction(e -> {
            scheduleController.handleAddScheduleItem(typeChoices, currentDay, scheduleTime, scheduleTitle, scheduleTopic, scheduleLink, scheduleCriteria);
        });
        
        clearSchedulesButton.setOnAction(e -> {
            scheduleTable.getSelectionModel().clearSelection();
            typeChoices.getSelectionModel().clearSelection();
            currentDay.setValue(null);
            scheduleTime.setText("");
            scheduleTitle.setText("");
            scheduleTopic.setText("");
            scheduleLink.setText("");
            scheduleCriteria.setText("");               
            scheduleTime.setDisable(false);
            scheduleTitle.setDisable(false);
            scheduleTopic.setDisable(false);
            scheduleLink.setDisable(false);
            scheduleCriteria.setDisable(false);
            typeChoices.requestFocus();
            
            addUpdateSchedulesButton.setText(props.getProperty(CSGProp.ADD_BUTTON_TEXT_GENERIC));
            addUpdateSchedulesButton.setOnAction(e2 -> {
                scheduleController.handleAddScheduleItem(typeChoices, currentDay, scheduleTime, scheduleTitle, scheduleTopic, scheduleLink, scheduleCriteria);
                scheduleTable.getSelectionModel().clearSelection();
            });
        });
        
        scheduleTable.setOnMouseClicked(e -> {
            Object selectedItem = scheduleTable.getSelectionModel().getSelectedItem();  
            ScheduleItem selectedSchedule = (ScheduleItem) selectedItem;    
            
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate scheduleItemDate = LocalDate.parse(selectedSchedule.getDateString(), format);

            
            if(selectedItem != null && (!data.getScheduleData().getScheduleItems().isEmpty())){
                
                typeChoices.getSelectionModel().select(selectedSchedule.getType());
                if(selectedSchedule.getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_HOLIDAY))){ //ITS A HOLIDAY ITEM.
                    currentDay.setValue(scheduleItemDate);
                    scheduleTime.setText("");
                    scheduleTitle.setText(selectedSchedule.getTitle());
                    scheduleTopic.setText("");
                    scheduleLink.setText(selectedSchedule.getLink());
                    scheduleCriteria.setText("");
                    scheduleTime.setDisable(true);
                    scheduleTitle.setDisable(false);
                    scheduleTopic.setDisable(true);
                    scheduleLink.setDisable(false);
                    scheduleCriteria.setDisable(true);
                }
                if(selectedSchedule.getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_LECTURE))){ //ITS A LECTURE ITEM.
                    currentDay.setValue(scheduleItemDate);
                    scheduleTime.setText("");
                    scheduleTitle.setText(selectedSchedule.getTitle());
                    scheduleTopic.setText(selectedSchedule.getTopic());
                    scheduleLink.setText(selectedSchedule.getLink());
                    scheduleCriteria.setText("");                
                    scheduleTime.setDisable(true);
                    scheduleTitle.setDisable(false);
                    scheduleTopic.setDisable(false);
                    scheduleLink.setDisable(false);
                    scheduleCriteria.setDisable(true);
                }
                if(selectedSchedule.getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_REFERENCE))){ //ITS A REFERENCE ITEM.
                    currentDay.setValue(scheduleItemDate);
                    scheduleTime.setText("");
                    scheduleTitle.setText(selectedSchedule.getTitle());
                    scheduleTopic.setText(selectedSchedule.getTopic());
                    scheduleLink.setText(selectedSchedule.getLink());
                    scheduleCriteria.setText("");                
                    scheduleTime.setDisable(true);
                    scheduleTitle.setDisable(false);
                    scheduleTopic.setDisable(false);
                    scheduleLink.setDisable(false);
                    scheduleCriteria.setDisable(true);
                }
                if(selectedSchedule.getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_RECITATION))){ //ITS A RECITATION ITEM.
                   currentDay.setValue(scheduleItemDate);
                   scheduleTime.setText("");
                   scheduleTitle.setText(selectedSchedule.getTitle());
                   scheduleTopic.setText(selectedSchedule.getTopic());
                   scheduleLink.setText("");
                   scheduleCriteria.setText("");               
                   scheduleTime.setDisable(true);
                   scheduleTitle.setDisable(false);
                   scheduleTopic.setDisable(false);
                   scheduleLink.setDisable(true);
                   scheduleCriteria.setDisable(true);
                }
                if(selectedSchedule.getType().equals(props.getProperty(CSGProp.TYPE_CHOICE_HW))){ //ITS A HOMEWORK ITEM.
                    currentDay.setValue(scheduleItemDate);
                    scheduleTime.setText(selectedSchedule.getTime());
                    scheduleTitle.setText(selectedSchedule.getTitle());
                    scheduleTopic.setText(selectedSchedule.getTopic());
                    scheduleLink.setText(selectedSchedule.getLink());
                    scheduleCriteria.setText(selectedSchedule.getCriteria());                
                    scheduleTime.setDisable(false);
                    scheduleTitle.setDisable(false);
                    scheduleTopic.setDisable(false);
                    scheduleLink.setDisable(false);
                    scheduleCriteria.setDisable(false);
                }
                
                addUpdateSchedulesButton.setText(props.getProperty(CSGProp.UPDATE_BUTTON_TEXT_GENERIC));        
                addUpdateSchedulesButton.setOnAction(e2 -> {
                       scheduleController.handleEditScheduleItem(scheduleTable,typeChoices, currentDay, scheduleTime, scheduleTitle, scheduleTopic, scheduleLink, scheduleCriteria);
                       scheduleTable.getSelectionModel().clearSelection();
                });
            
            }
        });
        
        
        scheduleTable.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.BACK_SPACE)){
                scheduleController.handleRemoveScheduleItem(scheduleTable, typeChoices, currentDay, scheduleTime, scheduleTitle, scheduleTopic, scheduleLink, scheduleCriteria);
                currentDay.requestFocus();
                
                scheduleTable.getSelectionModel().clearSelection();
                typeChoices.getSelectionModel().clearSelection();
                currentDay.setValue(null);
                scheduleTime.setText("");
                scheduleTitle.setText("");
                scheduleTopic.setText("");
                scheduleLink.setText("");
                scheduleCriteria.setText("");               
                scheduleTime.setDisable(false);
                scheduleTitle.setDisable(false);
                scheduleTopic.setDisable(false);
                scheduleLink.setDisable(false);
                scheduleCriteria.setDisable(false);
            }
        });
        
        
        clearScheduleItemsFromTable.setOnAction(e -> {
                scheduleController.handleRemoveScheduleItem(scheduleTable, typeChoices, currentDay, scheduleTime, scheduleTitle, scheduleTopic, scheduleLink, scheduleCriteria);
                currentDay.requestFocus();
                
                scheduleTable.getSelectionModel().clearSelection();
                typeChoices.getSelectionModel().clearSelection();
                currentDay.setValue(null);
                scheduleTime.setText("");
                scheduleTitle.setText("");
                scheduleTopic.setText("");
                scheduleLink.setText("");
                scheduleCriteria.setText("");               
                scheduleTime.setDisable(false);
                scheduleTitle.setDisable(false);
                scheduleTopic.setDisable(false);
                scheduleLink.setDisable(false);
                scheduleCriteria.setDisable(false);
        });
        
        
        
        typeChoices.setOnAction(e -> {
            if(typeChoices.getSelectionModel().getSelectedItem() != null && typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_HOLIDAY))){ //ITS A HOLIDAY ITEM.
                currentDay.setValue(null);
                scheduleTime.setText("");
                scheduleTitle.setText("");
                scheduleTopic.setText("");
                scheduleLink.setText("");
                scheduleCriteria.setText("");
                scheduleTime.setDisable(true);
                scheduleTitle.setDisable(false);
                scheduleTopic.setDisable(true);
                scheduleLink.setDisable(false);
                scheduleCriteria.setDisable(true);
               return;
            }
            if(typeChoices.getSelectionModel().getSelectedItem() != null && typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_LECTURE))){ //ITS A LECTURE ITEM.
                currentDay.setValue(null);
                scheduleTime.setText("");
                scheduleTitle.setText("");
                scheduleTopic.setText("");
                scheduleLink.setText("");
                scheduleCriteria.setText("");                
                scheduleTime.setDisable(true);
                scheduleTitle.setDisable(false);
                scheduleTopic.setDisable(false);
                scheduleLink.setDisable(false);
                scheduleCriteria.setDisable(true);
               return;
            }
            if(typeChoices.getSelectionModel().getSelectedItem() != null && typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_REFERENCE))){ //ITS A REFERENCE ITEM.
                currentDay.setValue(null);
                scheduleTime.setText("");
                scheduleTitle.setText("");
                scheduleTopic.setText("");
                scheduleLink.setText("");
                scheduleCriteria.setText("");                
                scheduleTime.setDisable(true);
                scheduleTitle.setDisable(false);
                scheduleTopic.setDisable(false);
                scheduleLink.setDisable(false);
                scheduleCriteria.setDisable(true);
               return;
            }
            if(typeChoices.getSelectionModel().getSelectedItem() != null && typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_RECITATION))){ //ITS A RECITATION ITEM.
               currentDay.setValue(null);
               scheduleTime.setText("");
               scheduleTitle.setText("");
               scheduleTopic.setText("");
               scheduleLink.setText("");
               scheduleCriteria.setText("");               
               scheduleTime.setDisable(true);
               scheduleTitle.setDisable(false);
               scheduleTopic.setDisable(false);
               scheduleLink.setDisable(true);
               scheduleCriteria.setDisable(true);
               return;
            }
            if(typeChoices.getSelectionModel().getSelectedItem() != null && typeChoices.getSelectionModel().getSelectedItem().equals(props.getProperty(CSGProp.TYPE_CHOICE_HW))){ //ITS A HOMEWORK ITEM.
                currentDay.setValue(null);
                scheduleTime.setText("");
                scheduleTitle.setText("");
                scheduleTopic.setText("");
                scheduleLink.setText("");
                scheduleCriteria.setText("");                
                scheduleTime.setDisable(false);
                scheduleTitle.setDisable(false);
                scheduleTopic.setDisable(false);
                scheduleLink.setDisable(false);
                scheduleCriteria.setDisable(false);
                return;
            }
            else{
                currentDay.setValue(null);
                scheduleTime.setText("");
                scheduleTitle.setText("");
                scheduleTopic.setText("");
                scheduleLink.setText("");
                scheduleCriteria.setText("");                
                scheduleTime.setDisable(false);
                scheduleTitle.setDisable(false);
                scheduleTopic.setDisable(false);
                scheduleLink.setDisable(false);
                scheduleCriteria.setDisable(false);
                return;
            }
        });
        
////////////////////////////////////////////////////////////////////CONTROLS FOR HANDLING RECITATIONS////////////////////////////////////////////////////////////////////////////
        
        recitationController = new RecitationController(app);
    

        addUpdateRecitationsButton.setOnAction(e -> {
            recitationController.handleAddRecitation(sectionTextField, instructorTextField, dayTimeTextField, locationTextField, supervisingTA1ChoiceBox, supervisingTA2ChoiceBox);
        });
        
        clearRecitationsButton.setOnAction(e -> {
            sectionTextField.setText("");
            instructorTextField.setText("");
            dayTimeTextField.setText("");
            locationTextField.setText("");
            supervisingTA1ChoiceBox.getSelectionModel().clearSelection();
            supervisingTA2ChoiceBox.getSelectionModel().clearSelection();
            
            addUpdateRecitationsButton.setText(props.getProperty(CSGProp.ADD_BUTTON_TEXT_GENERIC));
            addUpdateRecitationsButton.setOnAction(e2 -> {
                recitationController.handleAddRecitation(sectionTextField, instructorTextField, dayTimeTextField, locationTextField, supervisingTA1ChoiceBox, supervisingTA2ChoiceBox);
            });
        });
        
        recitationsTable.setOnMouseClicked(e -> {
            Object selectedItem = recitationsTable.getSelectionModel().getSelectedItem();
            // GET THE STUDENT
            if(selectedItem != null && (!data.getRecitationData().getRecitations().isEmpty())){
                Recitation recitation = (Recitation)selectedItem;
                String section = recitation.getSection();
                String instructor = recitation.getInstructor();
                String dayTime = recitation.getDayTime();
                String location = recitation.getLocation();
                String ta1 = recitation.getFirstTA();
                String ta2 = recitation.getSecondTA();
            
                sectionTextField.setText(section);
                instructorTextField.setText(instructor);
                dayTimeTextField.setText(dayTime);
                locationTextField.setText(location);
                
                if(supervisingTAsChoices.contains(ta1)){
                    supervisingTA1ChoiceBox.getSelectionModel().select(ta1);
                }
                if(supervisingTAsChoices.contains(ta2)){
                    supervisingTA2ChoiceBox.getSelectionModel().select(ta2);
                }
                
                addUpdateRecitationsButton.setText(props.getProperty(CSGProp.UPDATE_BUTTON_TEXT_GENERIC));
                addUpdateRecitationsButton.setOnAction(e2 -> {
                    recitationController.handleEditRecitation(recitationsTable,sectionTextField, instructorTextField, dayTimeTextField, locationTextField, supervisingTA1ChoiceBox, supervisingTA2ChoiceBox);
                    recitationsTable.getSelectionModel().clearSelection();
                });
            }
        });
        
        recitationsTable.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.BACK_SPACE)){
                recitationController.handleRemoveRecitation(recitationsTable, sectionTextField, instructorTextField, dayTimeTextField, locationTextField, supervisingTA1ChoiceBox, supervisingTA2ChoiceBox);
                
                sectionTextField.setText("");
                instructorTextField.setText("");
                dayTimeTextField.setText("");
                locationTextField.setText("");
                supervisingTA1ChoiceBox.getSelectionModel().clearSelection();
                supervisingTA2ChoiceBox.getSelectionModel().clearSelection();
            }
        });
    
        recitationsDeleteButton.setOnAction(e -> {
            recitationController.handleRemoveRecitation(recitationsTable, sectionTextField, instructorTextField, dayTimeTextField, locationTextField, supervisingTA1ChoiceBox, supervisingTA2ChoiceBox);
            
            sectionTextField.setText("");
            instructorTextField.setText("");
            dayTimeTextField.setText("");
            locationTextField.setText("");
            supervisingTA1ChoiceBox.getSelectionModel().clearSelection();
            supervisingTA2ChoiceBox.getSelectionModel().clearSelection();
        });
        
        
        
//////////////////////////////////////////////////////////////COURSE DETAILS TAB CONTROLLERS//////////////////////////////////////////////////////////////////////////////////
        courseDetailsController = new CourseDetailsController(app);

        courseInfoSubjectChoices.setOnAction(e -> {
            if(courseInfoSubjectChoices.getSelectionModel().getSelectedItem() != null && courseInfoSubjectChoices.getSelectionModel().getSelectedItem().equals("CSE")){
                courseInfoNumberChoices.getItems().clear();
                courseInfoNumberChoices.getItems().addAll(courseInfoNumberCSEChoices);
                courseDetailsController.handleSetSubjectChoice(courseInfoSubjectChoices);
            }
            if(courseInfoSubjectChoices.getSelectionModel().getSelectedItem() != null && courseInfoSubjectChoices.getSelectionModel().getSelectedItem().equals("AMS")){
                courseInfoNumberChoices.getItems().clear();
                courseInfoNumberChoices.getItems().addAll(courseInfoNumberAMSChoices);
                courseDetailsController.handleSetSubjectChoice(courseInfoSubjectChoices);
            }
        });
        
        courseInfoNumberChoices.setOnAction(e -> {
            courseDetailsController.handleSetNumberChoice(courseInfoNumberChoices);
        });
        
        courseInfoYearChoices.setOnAction(e -> {
            courseDetailsController.handleSetYearChoice(courseInfoYearChoices);
        });
        
        courseInfoSemesterChoices.setOnAction(e -> {
            courseDetailsController.handleSetSemesterChoice(courseInfoSemesterChoices);
        });
        
        courseTitleTextField.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.ENTER)){
                courseDetailsController.handleSetTitle(courseTitleTextField);
            }
        });
        
        courseInstructorNameTextField.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.ENTER)){
                courseDetailsController.handleSetInstructorName(courseInstructorNameTextField);
            }
        });
        
        courseInstructorHomeTextField.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode().equals(KeyCode.ENTER)){
                courseDetailsController.handleSetInstructorHome(courseInstructorHomeTextField);
            }
        });
        
        changeCourseInfoButton.setOnAction(e -> {
            courseDetailsController.handleSetExportDir(courseExportDirTextField);
        });
        
        siteDirButton.setOnAction(e -> {
            courseDetailsController.handleSiteTemplate(directoryChosenLabel);
        });
        
        changePageStyleButton1.setOnAction(e -> {
            courseDetailsController.handleBannerSchoolImage(pageStyleImage1);
        });
        
        changePageStyleButton2.setOnAction(e -> {
            courseDetailsController.handleLeftFooterImage(pageStyleImage2);
        });
        
        changePageStyleButton3.setOnAction(e -> {
            courseDetailsController.handleRightFooterImage(pageStyleImage3);
        });
        
        styleSheetSelectorChoices.setOnAction(e -> {
            courseDetailsController.handleStyleSheet(styleSheetSelectorChoices);
        });

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
////////////////////////////////////////////////////////////////////////////////VARIOUS HELPER METHODS///////////////////////////////////////////////////////////////////////////////////////////
    public void setStudentTeamsInChoiceBox(){
        studentTeam.getItems().addAll(studentTeamChoices);
    }
        
    public void removeStudentTeamFromChoiceBox(){
        studentTeam.getItems().removeAll(studentTeamChoices);
    }
    
    public ChoiceBox getStudentTeamChoiceBox(){
        return studentTeam;
    }

    public ObservableList getStudentTeamChoices(){
        return studentTeamChoices;
    }
    
    
    
    public void loadChoiceBoxSelections(){
        CSGData data = (CSGData)app.getDataComponent();
        
        boolean isFound = false;
        int index = 0;
        while(!isFound){
            String initialTime = choices.get(index);
            String loadedInitialTime = data.getTAData().getTimeString(data.getTAData().getStartHour(), controller.getStartTimeOnHour());
            if(initialTime.contains("12") && initialTime.contains("am")){
                initialTime = initialTime.replace("12", "0");
            }
            if(initialTime.equals(loadedInitialTime)){
                isFound = true;
            }
            index++;
        }

        choiceBox1.getSelectionModel().select(choices.get(index-1));

        isFound = false;
        index = 0;
        while(!isFound){
            if(choices.get(index).equals(data.getTAData().getTimeString(data.getTAData().getEndHour(), controller.getEndTimeOnHour()))){
                isFound = true;
            }
            index++;
        }

        choiceBox2.getSelectionModel().select(choices.get(index-1));
    }

    public String buildCellKey(int col, int row) {
        return "" + col + "_" + row;
    }

    public String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        if(militaryHour == 0){
            hour = 12;
        }
        String cellText = "" + hour + ":" + minutes;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    @Override
    public void resetWorkspace() {
        // CLEAR OUT THE GRID PANE
        // 
        
        officeHoursGridPane.getChildren().clear();
        
        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();
        
    }
    
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        CSGData CSGData = (CSGData)dataComponent;
        TAData taData = CSGData.getTAData();
        CourseDetailsData courseDetailsData = CSGData.getCourseDetailsData();
        reloadOfficeHoursGrid(taData);
//        //SETS DATA TO THE FIELDS TO THE MOST RECENTLY ADDED DATA IN THE TABLEVIEW BY DEFAULT
        reloadCourseDetailsFields(app.getDataComponent());
        reloadRecitationFields(app.getDataComponent());
        try{
            reloadScheduleFields(app.getDataComponent());
        }catch(Exception e){
            e.printStackTrace();
        }
        reloadProjectFields(app.getDataComponent());
    }
    
    public void reloadWorkspaceTA(AppDataComponent dataComponent) {
        CSGData CSGData = (CSGData)dataComponent;
        TAData taData = CSGData.getTAData();
        CourseDetailsData courseDetailsData = CSGData.getCourseDetailsData();
        reloadOfficeHoursGrid(taData);
    }
    
    public void reloadOH(AppDataComponent dataComponent){
        CSGData CSGData = (CSGData)dataComponent;
        TAData taData = CSGData.getTAData();
        reloadOfficeHoursGrid(taData);
    }
    
    public void reloadWorkspaceLoad(AppDataComponent dataComponent) {
        CSGData CSGData = (CSGData)dataComponent;
        TAData taData = CSGData.getTAData();
        CourseDetailsData courseDetailsData = CSGData.getCourseDetailsData();
//        reloadOfficeHoursGrid(taData);
        //SETS DATA TO THE FIELDS TO THE MOST RECENTLY ADDED DATA IN THE TABLEVIEW BY DEFAULT
        reloadCourseDetailsFields(app.getDataComponent());
        reloadRecitationFields(app.getDataComponent());
        try{
            reloadScheduleFields(app.getDataComponent());
        }catch(Exception e){
            e.printStackTrace();
        }
        reloadProjectFields(app.getDataComponent());
    }
    
    public void reloadCourseDetailsFields(AppDataComponent dataComponent){
        CSGData CSGData = (CSGData) dataComponent;
        CourseDetailsData courseDetailsData = CSGData.getCourseDetailsData();
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
//        courseInfoSubjectChoices.getItems().add(courseDetailsData.getSubject());
        String subject = courseDetailsData.getSubject();
        courseInfoSubjectChoices.getSelectionModel().select(subject);
//        courseInfoNumberChoices.getItems().add(courseDetailsData.getNumber());
        if(courseDetailsData.getSubject().equals("CSE")){
            courseInfoNumberChoices.getItems().clear();
            courseInfoNumberChoices.getItems().addAll(courseInfoNumberCSEChoices);
            String number = courseDetailsData.getNumber();
            courseInfoNumberChoices.getSelectionModel().select(number);
            String semester = courseDetailsData.getSemester();
            courseInfoSemesterChoices.getSelectionModel().select(semester);
            courseInfoYearChoices.getSelectionModel().select(courseDetailsData.getYear());
        }
        
        if(courseDetailsData.getSubject().equals("AMS")){
            courseInfoNumberChoices.getItems().clear();
            courseInfoNumberChoices.getItems().addAll(courseInfoNumberAMSChoices);
            String number = courseDetailsData.getNumber();
            courseInfoNumberChoices.getSelectionModel().select(number);
            String semester = courseDetailsData.getSemester();
            courseInfoSemesterChoices.getSelectionModel().select(semester);
            courseInfoYearChoices.getSelectionModel().select(courseDetailsData.getYear());
        }
        if(courseDetailsData.getSubject().equals("")){
            courseInfoSubjectChoices.getSelectionModel().clearSelection();
            courseInfoNumberChoices.getSelectionModel().clearSelection();
            courseInfoSemesterChoices.getSelectionModel().clearSelection();
            courseInfoYearChoices.getSelectionModel().clearSelection();
        }
        
//        courseInfoSemesterChoices.getItems().add(courseDetailsData.getSemester());
                
//        courseInfoYearChoices.getItems().add(courseDetailsData.getYear());

        courseTitleTextField.setText(courseDetailsData.getTitle());
        courseInstructorNameTextField.setText(courseDetailsData.getInstructorName());
        courseInstructorHomeTextField.setText(courseDetailsData.getInstructorHome());
        
        courseExportDirTextField.setText(courseDetailsData.getExportDir());
        
        if(!courseDetailsData.getExportDir().equals("")){
            fileController.markAsExportSelected(gui);
            fileController.setExportDirectoryChoose(courseDetailsData.getExportDir());
        }
        
        directoryChosenLabel.setText(courseDetailsData.getSiteTemplateDir());
        
//        styleSheetSelectorChoices.getItems().add(courseDetailsData.getStylesheet());
        styleSheetSelectorChoices.getSelectionModel().select(courseDetailsData.getStylesheet());
        
        bannerSchoolImageURI = /*FILE_PROTOCOL + PATH_IMAGES + */ courseDetailsData.getBannerSchoolImage();
        if(!bannerSchoolImageURI.isEmpty()){
            Image bannerSchoolImage = new Image(bannerSchoolImageURI);
            pageStyleImage1.setImage(bannerSchoolImage);
        }
        
        leftFooterImageURI = /*FILE_PROTOCOL + PATH_IMAGES + */courseDetailsData.getLeftFooterImage();
        if(!leftFooterImageURI.isEmpty()){
            Image leftFooterImage = new Image(leftFooterImageURI);
            pageStyleImage2.setImage(leftFooterImage);
        }
        
        rightFooterImageURI = /*FILE_PROTOCOL + PATH_IMAGES + */courseDetailsData.getRightFooterImage();
        if(!rightFooterImageURI.isEmpty()){
            Image rightFooterImage = new Image(rightFooterImageURI);
            pageStyleImage3.setImage(rightFooterImage);
        }
        else{
            pageStyleImage1.setImage(null);
            pageStyleImage2.setImage(null);
            pageStyleImage3.setImage(null);
        }
        
        
    }
    
    public void reloadRecitationFields(AppDataComponent dataComponent){ //FILLS THE FIELDS WITH THE LAST ADDED RECITATION IN THE TABLEVIEW
        CSGData data = (CSGData) dataComponent;
        RecitationData recitationData = data.getRecitationData();
        ObservableList<TeachingAssistant> taList = data.getTAData().getTeachingAssistants();
        ObservableList<Recitation> recitationList = recitationData.getRecitations();
        
        
        supervisingTAsChoices.clear();

        for(int i = 0; i < taList.size(); i++){
            supervisingTAsChoices.add(taList.get(i).getName());
        }
        supervisingTA1ChoiceBox.getItems().addAll(supervisingTAsChoices);
        supervisingTA2ChoiceBox.getItems().addAll(supervisingTAsChoices);

        
        if(recitationList.size() > 0){
            sectionTextField.setText(recitationList.get(recitationList.size() - 1).getSection());
            instructorTextField.setText(recitationList.get(recitationList.size() - 1).getInstructor());
            dayTimeTextField.setText(recitationList.get(recitationList.size() - 1).getDayTime());
            locationTextField.setText(recitationList.get(recitationList.size() - 1).getLocation());
            supervisingTA1ChoiceBox.getSelectionModel().select(recitationList.get(recitationList.size() - 1).getFirstTA());
            supervisingTA2ChoiceBox.getSelectionModel().select(recitationList.get(recitationList.size() - 1).getSecondTA());
        }
        else{
            sectionTextField.setText("");
            instructorTextField.setText("");
            dayTimeTextField.setText("");
            locationTextField.setText("");
        }
    }
    
    public void reloadScheduleFields(AppDataComponent dataComponent) throws ParseException{
        CSGData data = (CSGData) dataComponent;
        ScheduleData scheduleData = data.getScheduleData();
        ObservableList<ScheduleItem> scheduleItemData = scheduleData.getScheduleItems();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        if(scheduleItemData.size() > 0){
            LocalDate localStartDate = LocalDate.parse(scheduleData.getStartingMondayString(), format);
            LocalDate localEndDate = LocalDate.parse(scheduleData.getEndingFridayString(), format);
            LocalDate localScheduleItemDate = LocalDate.parse(scheduleItemData.get(scheduleItemData.size()-1).getDateString(), format);
            startingDay.setValue(localStartDate);
            endingDay.setValue(localEndDate);

//            typeChoices.getItems().add(scheduleItemData.get(scheduleItemData.size()-1).getType());
            typeChoices.getSelectionModel().select(scheduleItemData.get(scheduleItemData.size()-1).getType());
            currentDay.setValue(localScheduleItemDate);
            scheduleTime.setText(scheduleItemData.get(scheduleItemData.size()-1).getTime());
            scheduleTitle.setText(scheduleItemData.get(scheduleItemData.size()-1).getTitle());
            scheduleTopic.setText(scheduleItemData.get(scheduleItemData.size()-1).getTopic());
            scheduleLink.setText(scheduleItemData.get(scheduleItemData.size()-1).getLink());
            scheduleCriteria.setText(scheduleItemData.get(scheduleItemData.size()-1).getCriteria());
        }
        else if(scheduleData.getStartingMondayString() != null && scheduleData.getStartingMondayString() != null && !scheduleData.getStartingMondayString().equals("")
                && !scheduleData.getEndingFridayString().equals("")){
            LocalDate localStartDate = LocalDate.parse(scheduleData.getStartingMondayString(), format);
            LocalDate localEndDate = LocalDate.parse(scheduleData.getEndingFridayString(), format);
            startingDay.setValue(localStartDate);
            endingDay.setValue(localEndDate);
            scheduleTime.setText("");
            scheduleTitle.setText("");
            scheduleTopic.setText("");
            scheduleLink.setText("");
            scheduleCriteria.setText("");
        }
        else if(scheduleData.getStartingMondayString() != null  && !scheduleData.getStartingMondayString().equals("")){
            LocalDate localStartDate = LocalDate.parse(scheduleData.getStartingMondayString(), format);
            startingDay.setValue(localStartDate);
            endingDay.setValue(null);
            scheduleTime.setText("");
            scheduleTitle.setText("");
            scheduleTopic.setText("");
            scheduleLink.setText("");
            scheduleCriteria.setText("");
        }
        else if(scheduleData.getEndingFridayString() != null && !scheduleData.getEndingFridayString().equals("")){
            LocalDate localEndDate = LocalDate.parse(scheduleData.getEndingFridayString(), format);
            startingDay.setValue(null);
            endingDay.setValue(localEndDate);
            scheduleTime.setText("");
            scheduleTitle.setText("");
            scheduleTopic.setText("");
            scheduleLink.setText("");
            scheduleCriteria.setText("");
        }
        else{
            startingDay.setValue(null);
            endingDay.setValue(null);
            currentDay.setValue(null);
            scheduleTime.setText("");
            scheduleTitle.setText("");
            scheduleTopic.setText("");
            scheduleLink.setText("");
            scheduleCriteria.setText("");
        }
    }
    
    public void reloadProjectFields(AppDataComponent dataComponent){
        
        CSGData data = (CSGData) dataComponent;
        ProjectData projectData = data.getProjectData();
        ObservableList<Team> teamData = projectData.getTeams();
        ObservableList<Student> studentData = projectData.getStudents();
                
        if(teamData.size() > 0){
            teamNameField.setText(teamData.get(teamData.size()-1).getTeamName());
            teamColorTextField.setText(teamData.get(teamData.size()-1).getTeamColor());
            textColorTextField.setText(teamData.get(teamData.size()-1).getTextColor());
            String teamColorValue = teamColorTextField.getText();
            String textColorValue = textColorTextField.getText();
            if(teamColorValue != null && textColorValue != null){
                teamColorCircle.setFill(Color.web(teamColorValue));
                textColorCircle.setFill(Color.web(textColorValue));
            }
            teamLink.setText(teamData.get(teamData.size()-1).getLink());
        }
        else{
            teamNameField.setText("");
            teamColorTextField.setText("#ffffff");
            textColorTextField.setText("#ffffff");
            String teamColorValue = teamColorTextField.getText();
            String textColorValue = textColorTextField.getText();
            if(teamColorValue != null && textColorValue != null){
                teamColorCircle.setFill(Color.web(teamColorValue));
                textColorCircle.setFill(Color.web(textColorValue));
            }
            teamLink.setText("");
        }
        if(studentData.size() > 0){
            studentFirstName.setText(studentData.get(studentData.size()-1).getFirstName());
            studentLastName.setText(studentData.get(studentData.size()-1).getLastName());
            
            studentTeamChoices.clear();
            
            for(int i = 0; i < teamData.size(); i++){
                studentTeamChoices.add(teamData.get(i).getTeamName());
            }
            
            studentTeam.getItems().addAll(studentTeamChoices);

            studentTeam.getSelectionModel().select(studentData.get(studentData.size()-1).getTeam());
            studentRole.setText(studentData.get(studentData.size()-1).getRole());
        }
        else{
            studentFirstName.setText("");
            studentLastName.setText("");
            studentTeam.getItems().clear();
            studentRole.setText("");
        }
    }

    public void reloadOfficeHoursGrid(TAData dataComponent) {  
        
        loadChoiceBoxSelections();
        
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();

        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes, officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }
        
        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes, officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));            
        }

        int row = 1;
        for (int i = dataComponent.getStartHour(); i < dataComponent.getEndHour(); i++) {
            // START TIME COLUMN
            int col = 0;
                addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
                dataComponent.getCellTextProperty(col, row).set(buildCellText(i, "00"));
                addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
                dataComponent.getCellTextProperty(col, row+1).set(buildCellText(i, "30"));
            // END TIME COLUMN
            col++;
            int endHour = i;
            
                addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
                dataComponent.getCellTextProperty(col, row).set(buildCellText(endHour, "30"));
                addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
                dataComponent.getCellTextProperty(col, row+1).set(buildCellText(endHour+1, "00"));
                col++;
            

            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row+1);
                col++;
            }
            row += 2;
        }

        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setFocusTraversable(true);
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
//                
//                ToggleTA_Transaction transaction= new ToggleTA_Transaction((Pane) e.getSource(), app);
//                transactionComponent.addTransaction(transaction);
            });
        }
        
        // AND MAKE SURE ALL THE COMPONENTS HAVE THE PROPER STYLE
        CSGStyle csgStyle = (CSGStyle)app.getStyleComponent();
        csgStyle.initOfficeHoursGridStyle();
    }
    //}
    
    
    public void addCellToGrid(TAData dataComponent, HashMap<String, Pane> panes, HashMap<String, Label> labels, int col, int row) {       
        // MAKE THE LABEL IN A PANE
        Label cellLabel = new Label("");
        HBox cellPane = new HBox();
        cellPane.setPrefWidth(100);
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);
        
        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);
        
         
        cellPane.setId(col+"_"+row);
        cellPane.setOnMouseEntered(e->{  //Event handler for each individual cell in the GRIDPANE. OfficeHoursGridPane IS THE ENTIRE RIGHT HAND BOX PANE. 
                                         //The HBox created in this method are the individual cells (individual panes) in the entire overall GRIDPANE.
            HBox cell = (HBox) e.getSource();
            String[] coordinate = cell.getId().split("_");
            String rowCoordinate = coordinate[1];
            String colCoordinate = coordinate[0];
      
            int rowNum = Integer.parseInt(rowCoordinate);
            int colNum = Integer.parseInt(colCoordinate);
            
            CSGStyle csgStyle = (CSGStyle)app.getStyleComponent();
            
            //CELLS TO THE LEFT
            if((rowNum > 0) && (colNum >= 2)){
                for(int i = 2 ; i <= colNum; i++){
                   String cellKeySpecific = dataComponent.getCellKey(i, rowNum);
                   DropShadow b = new DropShadow();
                   b.setColor(Color.CYAN);
                   HBox tempCell =   gridPaneGetCellByCoord(officeHoursGridPane, i, rowNum);
                   tempCell.setEffect(b);            
                }
            }
            
            //CELLS ABOVE
            if((rowNum > 0) && (colNum >= 2)){
                for(int i = rowNum; i >= 1; i--){
                   String cellKeySpecific = dataComponent.getCellKey(colNum, i);
                   DropShadow b = new DropShadow();
                   b.setColor(Color.CYAN);

                   //Pane empCell = getTACellPane(cellKeySpecific);
                   HBox tempCell =   gridPaneGetCellByCoord(officeHoursGridPane, colNum, i);
                   tempCell.setEffect(b);            
                }
            }
        });
                  
       cellPane.setOnMouseExited(e->{
            HBox cell = (HBox) e.getSource();
            String[] coordinate = cell.getId().split("_");
            String rowCoordinate = coordinate[1];
            String colCoordinate = coordinate[0];
 
            int rowNum = Integer.parseInt(rowCoordinate);
            int colNum = Integer.parseInt(colCoordinate);
            
            CSGStyle csgStyle = (CSGStyle)app.getStyleComponent();
            
            
            //CELLS TO THE LEFT
            for(int i = 0 ; i <= colNum; i++){
               String cellKeySpecific = dataComponent.getCellKey(i, rowNum);

               HBox tempCell =   gridPaneGetCellByCoord(officeHoursGridPane, i, rowNum);
               tempCell.setEffect(null);          
            }
            
            
            //CELLS ABOVE
            for(int i = rowNum; i >= 1; i--){
               String cellKeySpecific = dataComponent.getCellKey(colNum, i);

               HBox tempCell =   gridPaneGetCellByCoord(officeHoursGridPane, colNum, i);
               tempCell.setEffect(null);            
            }
        });
        
        
        
        
        
        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);
        
        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty());        
    }
    
    public HBox gridPaneGetCellByCoord(GridPane pane, int col, int row){
        for(Node node : pane.getChildren()){
            int columnIndex = pane.getColumnIndex(node);
            int rowIndex = pane.getRowIndex(node);
            if(columnIndex == col && rowIndex == row){
               return (HBox)node;
            }
        }
        return null;   
    }
    
    public int getSelectionIndex(ObservableList<String> choices, String toCompareValue){
        for(int i = 0; i < choices.size(); i++){
            if(choices.get(i).equals(toCompareValue)){
                return i;
            }
        }
        return 0;
    }
    
    public void setSupervisingTAChoices(){
        CSGData data = (CSGData) app.getDataComponent();
//        if(data.getTAData().getTA(currentName).isUndergrad().get()){

//            supervisingTAsChoices = data.getTAData().getTeachingAssistants();
            supervisingTA1ChoiceBox.getItems().addAll(supervisingTAsChoices);
            supervisingTA2ChoiceBox.getItems().addAll(supervisingTAsChoices);
//        }
    }
    
    public void removeSupervisingTAChoice(){
            supervisingTA1ChoiceBox.getItems().removeAll(supervisingTAsChoices);
            supervisingTA2ChoiceBox.getItems().removeAll(supervisingTAsChoices);
    }
    
    public ChoiceBox getSupervising1(){
        return supervisingTA1ChoiceBox;
    }
    public ChoiceBox getSupervising2(){
        return supervisingTA2ChoiceBox;
    }
    public ObservableList getSupervisingTAsChoices(){
        return supervisingTAsChoices;
    }
    
    public void setSupervisingTAChoicesAll(ObservableList s){
            supervisingTA1ChoiceBox.getItems().addAll(s);
            supervisingTA2ChoiceBox.getItems().addAll(s);   
    }
    
    public void clearSupervisingChoices(){
        supervisingTA1ChoiceBox.getItems().removeAll(supervisingTAsChoices);
        supervisingTA2ChoiceBox.getItems().removeAll(supervisingTAsChoices);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
        // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
   
    public TAController getController(){
        return controller;
    }
    public Tab getTab1(){
        return tab1;
    }
    public Tab getTab2(){
        return tab2;
    }
    public Tab getTab3(){
        return tab3;
    }
    public Tab getTab4(){
        return tab4;
    }
    public Tab getTab5(){
        return tab5;
    }
    
    
    public VBox getCourseDetailsBox(){
        return courseDetails;
    }
    public VBox getCourseInfoSubBox(){
        return courseInfo;
    }
    public VBox getSiteTemplateSubBox(){
        return siteTemplate;
    }
    public VBox getPageStyleSubBox(){
        return pageStyle;
    }
    
    public Label getCourseDetailsTitleLabel(){
        return courseInfoTitleLabel;
    }
    
    public Label getSiteTemplateTitleLabel(){
        return siteTemplateTitleLabel;
    }
    
    public TableView getSiteTemplateTable(){
        return siteTemplateTable;
    }
    
    public Label getPageStyleTitleLabel(){
        return pageStyleTitleLabel;
    }
    
    public Button getChangeCourseInfoButton(){
        return changeCourseInfoButton;
    }
    
    public Button getChangePageStyleButton1(){
        return changePageStyleButton1;
    }
    
    public Button getChangePageStyleButton2(){
        return changePageStyleButton2;
    }
    public Button getChangePageStyleButton3(){
        return changePageStyleButton3;
    }
    
    public Button getSiteDirButton(){
        return siteDirButton;
    }
    
    public String getBannerURI(){
        return bannerSchoolImageURI;
    }
    
    public void setBannerURI(String bannerURI){
        bannerSchoolImageURI = bannerURI;
    }
    
    public String getLeftURI(){
        return leftFooterImageURI;
    }
    
    public void setLeftURI(String leftURI){
        leftFooterImageURI = leftURI;
    }
    public String getRightURI(){
        return rightFooterImageURI;
    }
    
    public void setRightURI(String rightURI){
        rightFooterImageURI = rightURI;
    }
   

///////////////////////////////////////////////////
    public RecitationController getRecitationController(){
        return recitationController;
    }
    public VBox getRecitationsBox(){
        return recitations;
    }
    public VBox getSubRecitationsBox(){
        return subRecitations;
    }
    
     public Label getRecitationTitleLabel(){
        return recitationTitleLabel;
    }
     
    public TableView getRecitationsTable(){
            return recitationsTable;
    }
    
    public Button getRecitationDeleteButton(){
        return recitationsDeleteButton;
    }
    
    public Button getAddUpdateRecitationsButton(){
        return addUpdateRecitationsButton;
    }
    public Button getClearRecitationsButton(){
        return clearRecitationsButton;
    }
    
    
//////////////////////////////////////////////////////
    public ScheduleController getScheduleController(){
        return scheduleController;
    }
    public VBox getSchedulesBox(){
        return schedule;
    }
    public VBox getCalendarScheduleSubBox(){
        return calendarSchedule;
    }
    public VBox getScheduleItemsSubBox(){
        return scheduleItems;
    }
    
    public Label getCalendarBoundariesTitleLabel(){
        return calendarBoundariesTitleLabel;
    }
    public Label getScheduleTitleLabel(){
        return scheduleTitleLabel;
    }
    
    public Label getScheduleItemsTitleLabel(){
        return scheduleItemsTitleLabel;
    }
    
    public TableView getScheduleTable(){
        return scheduleTable;
    }
    
    public Button getClearScheduleItemsFromTable(){
        return clearScheduleItemsFromTable;
    }
    public Button getAddUpdateSchedulesButton(){
        return addUpdateSchedulesButton;
    }
    public Button getClearSchedulesButton(){
        return clearSchedulesButton;
    }
    

///////////////////////////////////////////////////////////
    public ProjectsController getProjectsController(){
        return projectsController;
    }
    public VBox getProjectsBox(){
        return projects;
    }
    public VBox getTeamsSubBox(){
        return teams;
    }
    public VBox getStudentsSubBox(){
        return students;
    }
     public Label getProjectTitleLabel(){
        return projectsTitleLabel;
    }
        
    public Label getTeamTitleLabel(){
        return teamTitleLabel;
    }
    
    public TableView getTeamsTable(){
        return teamsTable;
    }
    
    public Label getStudentTitleLabel(){
        return studentTitleLabel;
    }
    
    public Label getAddEditTitleLabelRecitation(){
        return addEditTitleLabelRecitation;
    }
    
    public Label getAddEditTitleLabelSchedule(){
        return addEditTitleLabelSchedule;
    }
    
    public Label getAddEditTitleLabelTeams(){
        return addEditTitleLabelTeams;
    }
    
    public Label getAddEditTitleLabelStudent(){
        return addEditTitleLabelStudents;
    }
    
    public TableView getStudentsTable(){
        return studentsTable;
    }
    
    public Button getClearTeamsFromTableButton(){
        return clearTeamsFromTableButton;
    }
    public Button getAddUpdateTeamButton(){
        return addUpdateTeamButton;
    }
    public Button getClearTeamButton(){
        return clearTeamButton;
    }
    public Button getClearStudentsFromTableButton(){
        return clearStudentsFromTableButton;
    }
    public Button getAddUpdateStudentButton(){
        return addUpdateStudentButton;
    }
    public Button getClearStudentButton(){
        return clearStudentButton;
    }

    
//////////////////////////////////////////////////////////////////////////////
    
    public VBox getTAPane(){
        return taPane;
    }
    
    public VBox getTADataPane(){
        return taDataPane;
    }
    
    public SplitPane getSPane(){
        return sPane;
    }

    public HBox getTAsHeaderBox() {
        return tasHeaderBox;
    }

    public Label getTAsHeaderLabel() {
        return tasHeaderLabel;
    }

    public TableView getTATable() {
        return taTable;
    }

    public HBox getAddBox() {
        return addBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }
    
    public TextField getEmailTextField(){
        return emailTextField;
    }

    public Button getAddButton() {
        return addButton;
    }
    
    public Button getClearButton(){
        return clearButton;
    }
    
    public Button getDeleteTAButton(){
        return deleteTAButton;
    }
    
    public ChoiceBox getChoiceBox1(){
        return choiceBox1;
    }
    
    public ChoiceBox getChoiceBox2(){
        return choiceBox2;
    }
    
    public Label getChoiceBoxTitleLabelStart(){
        return startTitle;
    }
    
    public Label getChoiceBoxTitleLabelEnd(){
        return endTitle;
    }
    
    public Label getComboBoxHeaderLabel(){
            return comboBoxHeaderLabel;
    }
    
    public Button getSubmitButton(){
        return submitButton;
    }

    public HBox getOfficeHoursSubheaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursSubheaderLabel() {
        return officeHoursHeaderLabel;
    }

    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }
   
    
    
    public String getCellKey(Pane testPane) {
        for (String key : officeHoursGridTACellLabels.keySet()) {
            if (officeHoursGridTACellPanes.get(key) == testPane) {
                return key;
            }
        }
        return null;
    }

    public Label getTACellLabel(String cellKey) {
        return officeHoursGridTACellLabels.get(cellKey);
    }

    public Pane getTACellPane(String cellPane) {
        return officeHoursGridTACellPanes.get(cellPane);
    }

    public DatePicker getStartingDay() {
        return startingDay;
    }
    
    public DatePicker getEndingDay(){
        return endingDay;
    }
    
}
