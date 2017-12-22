/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.style;

import csg.data.TeachingAssistant;
import csg.workspace.CSGWorkspace;
import djf.AppTemplate;
import djf.components.AppStyleComponent;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Andrew
 */
public class CSGStyle extends AppStyleComponent{
    
        // FIRST WE SHOULD DECLARE ALL OF THE STYLE TYPES WE PLAN TO USE
    
    // WE'LL USE THIS FOR ORGANIZING LEFT AND RIGHT CONTROLS
    public static String LABEL = "label";
    public static String CLASS_PLAIN_PANE = "plain_pane";
    
    // THESE ARE THE HEADERS FOR EACH SIDE
    public static String CLASS_HEADER_PANE = "header_pane";
    public static String CLASS_HEADER_LABEL = "header_label";

    // ON THE LEFT WE HAVE THE TA ENTRY
    public static String CLASS_TA_TABLE = "ta_table";
    public static String CLASS_TA_TABLE_COLUMN_HEADER = "ta_table_column_header";
    public static String CLASS_ADD_TA_PANE = "add_ta_pane";
    public static String CLASS_ADD_TA_TEXT_FIELD = "add_ta_text_field";
    public static String CLASS_ADD_TA_BUTTON = "add_ta_button";
    
    public static String CLASS_CLEAR_TA_BUTTON = "clear_ta_button";
    
    public static String CLASS_CHOICE_BOX = "choice_box";
    public static String CLASS_SUBMIT_BUTTON = "submit_button";
    public static String CLASS_CHOICE_BOX_TITLE_LABEL = "choice_box_title_label";
    public static String CLASS_COMBO_BOX_HEADER_LABEL = "combo_box_header_label";

    // ON THE RIGHT WE HAVE THE OFFICE HOURS GRID
    public static String CLASS_OFFICE_HOURS_GRID = "office_hours_grid";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE = "office_hours_grid_time_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL = "office_hours_grid_time_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE = "office_hours_grid_day_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL = "office_hours_grid_day_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE = "office_hours_grid_time_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL = "office_hours_grid_time_cell_label";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE = "office_hours_grid_ta_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL = "office_hours_grid_ta_cell_label";
    public static String CLASS_TEXT_LABEL = "text_label";
    public static String CLASS_SUBPANES = "sub_panes";
    public static String CLASS_MAIN_PANES = "main_panes";
    public static String CLASS_TITLE_LABEL = "text_title_label";
    public static String CLASS_SUBTITLE_LABEL = "text_subtitle_label";
    public static String CLASS_WORKSPACE_BUTTON = "workspace_button";
    
    
    // THIS PROVIDES ACCESS TO OTHER COMPONENTS
    private AppTemplate app;
        
    public CSGStyle(AppTemplate initApp){
        app = initApp;
        super.initStylesheet(app);
        app.getGUI().initFileToolbarStyle();
        initTAWorkspaceStyle();
        initRecitationsStyle();
        initCourseDetailsStyle();
        initSchedulesStyle();
        initProjectsStyle();
    }
    
    private void initCourseDetailsStyle(){
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getCourseDetailsBox().getStyleClass().add(CLASS_TEXT_LABEL);
        workspaceComponent.getCourseInfoSubBox().getStyleClass().add(CLASS_SUBPANES);
        workspaceComponent.getSiteTemplateSubBox().getStyleClass().add(CLASS_SUBPANES);
        workspaceComponent.getPageStyleSubBox().getStyleClass().add(CLASS_SUBPANES);
        workspaceComponent.getCourseDetailsBox().getStyleClass().add(CLASS_MAIN_PANES);
        workspaceComponent.getCourseDetailsTitleLabel().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getSiteTemplateTitleLabel().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getPageStyleTitleLabel().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getTab1().getStyleClass().add(CLASS_TEXT_LABEL);
        workspaceComponent.getTab2().getStyleClass().add(CLASS_TEXT_LABEL);
        workspaceComponent.getTab3().getStyleClass().add(CLASS_TEXT_LABEL);
        workspaceComponent.getTab4().getStyleClass().add(CLASS_TEXT_LABEL);
        workspaceComponent.getTab5().getStyleClass().add(CLASS_TEXT_LABEL);
        workspaceComponent.getChangeCourseInfoButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getChangePageStyleButton1().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getChangePageStyleButton2().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getChangePageStyleButton3().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getSiteDirButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        
    }
    private void initRecitationsStyle(){
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getRecitationsBox().getStyleClass().add(CLASS_TEXT_LABEL);
       workspaceComponent.getSubRecitationsBox().getStyleClass().add(CLASS_SUBPANES);
        workspaceComponent.getRecitationsBox().getStyleClass().add(CLASS_MAIN_PANES);
        workspaceComponent.getRecitationTitleLabel().getStyleClass().add(CLASS_TITLE_LABEL);
        workspaceComponent.getAddEditTitleLabelRecitation().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getRecitationDeleteButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getAddUpdateRecitationsButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getClearRecitationsButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
    }
    private void initSchedulesStyle(){
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getSchedulesBox().getStyleClass().add(CLASS_TEXT_LABEL);
        workspaceComponent.getCalendarScheduleSubBox().getStyleClass().add(CLASS_SUBPANES);
        workspaceComponent.getScheduleItemsSubBox().getStyleClass().add(CLASS_SUBPANES);
        workspaceComponent.getSchedulesBox().getStyleClass().add(CLASS_MAIN_PANES);
        workspaceComponent.getScheduleTitleLabel().getStyleClass().add(CLASS_TITLE_LABEL);
        workspaceComponent.getCalendarBoundariesTitleLabel().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getAddEditTitleLabelSchedule().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getScheduleItemsTitleLabel().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getClearScheduleItemsFromTable().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getAddUpdateSchedulesButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getClearSchedulesButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        
    }
    private void initProjectsStyle(){
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getProjectsBox().getStyleClass().add(CLASS_TEXT_LABEL);
        workspaceComponent.getTeamsSubBox().getStyleClass().add(CLASS_SUBPANES);
        workspaceComponent.getStudentsSubBox().getStyleClass().add(CLASS_SUBPANES);
        workspaceComponent.getProjectsBox().getStyleClass().add(CLASS_MAIN_PANES);
        workspaceComponent.getProjectTitleLabel().getStyleClass().add(CLASS_TITLE_LABEL);
        workspaceComponent.getTeamTitleLabel().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getStudentTitleLabel().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getAddEditTitleLabelTeams().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getAddEditTitleLabelStudent().getStyleClass().add(CLASS_SUBTITLE_LABEL);
        workspaceComponent.getClearTeamsFromTableButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getAddUpdateTeamButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getClearTeamButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getClearStudentsFromTableButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getAddUpdateStudentButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
        workspaceComponent.getClearStudentButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);
    }
    
    
     private void initTAWorkspaceStyle() {
        // LEFT SIDE - THE HEADER
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getTAsHeaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getTAsHeaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        workspaceComponent.getTAPane().getStyleClass().add(CLASS_MAIN_PANES);
        workspaceComponent.getTADataPane().getStyleClass().add(CLASS_MAIN_PANES);
        workspaceComponent.getSPane().getStyleClass().add(CLASS_MAIN_PANES);
        workspaceComponent.getDeleteTAButton().getStyleClass().add(CLASS_WORKSPACE_BUTTON);

        // LEFT SIDE - THE TABLE
        TableView<TeachingAssistant> taTable = workspaceComponent.getTATable();
        taTable.getStyleClass().add(CLASS_TA_TABLE);
//        for (TableColumn tableColumn : taTable.getColumns()) {
//            tableColumn.getStyleClass().add(CLASS_TA_TABLE_COLUMN_HEADER);
//        }

        // LEFT SIDE - THE TA DATA ENTRY
        workspaceComponent.getAddBox().getStyleClass().add(CLASS_ADD_TA_PANE); //getAddBox(). This line will display the addBox by using the getter method.
        workspaceComponent.getNameTextField().getStyleClass().add(CLASS_ADD_TA_TEXT_FIELD);
        workspaceComponent.getAddButton().getStyleClass().add(CLASS_ADD_TA_BUTTON);
        
        workspaceComponent.getClearButton().getStyleClass().add(CLASS_CLEAR_TA_BUTTON);
        workspaceComponent.getChoiceBox1().getStyleClass().add(CLASS_CHOICE_BOX);
        workspaceComponent.getChoiceBox2().getStyleClass().add(CLASS_CHOICE_BOX);
        
        workspaceComponent.getSubmitButton().getStyleClass().add(CLASS_SUBMIT_BUTTON);
        workspaceComponent.getChoiceBoxTitleLabelStart().getStyleClass().add(CLASS_CHOICE_BOX_TITLE_LABEL);
        workspaceComponent.getChoiceBoxTitleLabelEnd().getStyleClass().add(CLASS_CHOICE_BOX_TITLE_LABEL);
        workspaceComponent. getComboBoxHeaderLabel().getStyleClass().add(CLASS_COMBO_BOX_HEADER_LABEL);
        // RIGHT SIDE - THE HEADER
        workspaceComponent.getOfficeHoursSubheaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getOfficeHoursSubheaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);
    }
    
    /**
     * This method initializes the style for all UI components in
     * the office hours grid. Note that this should be called every
     * time a new TA Office Hours Grid is created or loaded.
     */
    public void initOfficeHoursGridStyle() {
        // RIGHT SIDE - THE OFFICE HOURS GRID TIME HEADERS
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getOfficeHoursGridPane().getStyleClass().add(CLASS_OFFICE_HOURS_GRID);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderPanes(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderLabels(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderPanes(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderLabels(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellPanes(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellLabels(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellLabels(), CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
    }
    
    
    
    
    
     private void setStyleClassOnAll(HashMap nodes, String styleClass) { 
        for (Object nodeObject : nodes.values()) {   //FOR EACH NODE IN THE HASHMAP, SET THE STYLE. 
                                                     //EACH HASHMAP IS A CERTAIN PART OF THE GRIDPANE, WHICH SEPARATES THE DIFFERENT STYLES IN THE GRID
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
        }
    }
    
}
