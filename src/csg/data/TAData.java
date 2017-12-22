/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import csg.CSGProp;
import csg.workspace.CSGWorkspace;
import djf.components.AppDataComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class TAData{
    
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CSGApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistant> teachingAssistants;

    // THIS WILL STORE ALL THE OFFICE HOURS GRID DATA, WHICH YOU
    // SHOULD NOTE ARE StringProperty OBJECTS THAT ARE CONNECTED
    // TO UI LABELS, WHICH MEANS IF WE CHANGE VALUES IN THESE
    // PROPERTIES IT CHANGES WHAT APPEARS IN THOSE LABELS
    HashMap<String, StringProperty> officeHours;
    
    // THESE ARE THE LANGUAGE-DEPENDENT VALUES FOR
    // THE OFFICE HOURS GRID HEADERS. NOTE THAT WE
    // LOAD THESE ONCE AND THEN HANG ON TO THEM TO
    // INITIALIZE OUR OFFICE HOURS GRID
    ArrayList<String> gridHeaders;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 9;
    public static final int MAX_END_HOUR = 20;

    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public TAData(CSGApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        teachingAssistants = FXCollections.observableArrayList();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        //THIS WILL STORE OUR OFFICE HOURS
        officeHours = new HashMap();
        
        // THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> timeHeaders = props.getPropertyOptionsList(CSGProp.OFFICE_HOURS_TABLE_HEADERS);
        ArrayList<String> dowHeaders = props.getPropertyOptionsList(CSGProp.DAYS_OF_WEEK);
        gridHeaders = new ArrayList();
        gridHeaders.addAll(timeHeaders);
        gridHeaders.addAll(dowHeaders);
    }
    
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    public void resetData() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        officeHours.clear();
    }
    
    // ACCESSOR METHODS

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }
    
    public ArrayList<String> getGridHeaders() {
        return gridHeaders;
    }

    public ObservableList getTeachingAssistants() {
        return teachingAssistants;
    }
    
    public String getCellKey(int col, int row) {
        return col + "_" + row;
    }

    public StringProperty getCellTextProperty(int col, int row) {
        String cellKey = getCellKey(col, row);
        return officeHours.get(cellKey);
    }

    public HashMap<String, StringProperty> getOfficeHours() {
        return officeHours;
    }
    
    public int getNumRows() {
        return ((endHour - startHour) * 2) + 1;
    }

    public String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    public String getCellKey(String day, String time) {
        int col = gridHeaders.indexOf(day);
        int row = 1;
        int hour = Integer.parseInt(time.substring(0, time.indexOf("_")));
        int milHour = hour; 
        if(time.contains("pm") && !time.contains("12")){
            milHour += 12;
        }
        
        if(time.contains("am") && time.contains("12")){
            milHour -= 12;
        }
  
        row += (milHour - startHour) * 2;
        if (time.contains("_30"))
            row += 1;
        return getCellKey(col, row);
    }
    
    public TeachingAssistant getTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName))
                return ta;
        }
        return null;
    }
    
    /**
     * This method is for giving this data manager the string property
     * for a given cell.
     */
    public void setCellProperty(int col, int row, StringProperty prop) {
        String cellKey = getCellKey(col, row);
        officeHours.put(cellKey, prop);
    }    
    
    /**
     * This method is for setting the string property for a given cell.
     */
    public void setGridProperty(ArrayList<ArrayList<StringProperty>> grid, int column, int row, StringProperty prop) {
        grid.get(row).set(column, prop);
    }
    
    public void setHours(int initStartHour, int initEndHour){
        startHour = initStartHour;
        endHour = initEndHour;
    }
    
    private void initOfficeHours(int initStartHour, int initEndHour) {
        // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
        startHour = initStartHour;
        endHour = initEndHour;
        // EMPTY THE CURRENT OFFICE HOURS VALUES
        officeHours.clear();   
        // WE'LL BUILD THE USER INTERFACE COMPONENT FOR THE
        // OFFICE HOURS GRID AND FEED THEM TO OUR DATA
        // STRUCTURE AS WE GO
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        workspaceComponent.reloadOfficeHoursGrid(this);
    }
    
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if((initStartHour <= initEndHour))
            // THESE ARE VALID HOURS SO KEEP THEM
            initOfficeHours(initStartHour, initEndHour);
    }

    public boolean containsTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName))
                return true;
        }
        return false;
    }
    
     public boolean containsTAEmail(String testEmail) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getEmail().equals(testEmail))
                return true;
        }
        return false;
    }

    public void addTA(String initName, String initEmail) {
        // MAKE THE TA
        TeachingAssistant ta = new TeachingAssistant(initName, initEmail);
        // ADD THE TA
        if (!containsTA(initName))
            teachingAssistants.add(ta);
        // SORT THE TAS
        Collections.sort(teachingAssistants);
//        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
//        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices();
//        supervisingTAsChoices.add(initName);
    }
    
    public void addTAWithoutConstruct(TeachingAssistant ta){
        if(!ta.getName().equals("")){
            teachingAssistants.add(ta);
        }
        Collections.sort(teachingAssistants);
//        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
//        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices();
//        for(int i = 0; i < teachingAssistants.size(); i++){
//            if(!teachingAssistants.get(i).getName().equals("")){
//                supervisingTAsChoices.add(teachingAssistants.get(i).getName());
//            }
//        }
//        workspace.setSupervisingTAChoices();

        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices();
        workspace.getSupervising1().getItems().removeAll(supervisingTAsChoices);
        workspace.getSupervising2().getItems().removeAll(supervisingTAsChoices);
        supervisingTAsChoices.add(ta.getName());
        supervisingTAsChoices.sort(Comparator.naturalOrder());
        
        workspace.setSupervisingTAChoices();
    }
    
    public void removeTA(TeachingAssistant ta){
        String oldTAName = ta.getName();
//        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
//        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices(); //GET THE LIST OF STRINGS TO LOAD INTO CHOICEBOX. THOSE ARE THE OPTIONS.
//        workspace.removeSupervisingTAChoice();//REMOVE ALL OF THE CHOICES IN THE CHOICEBOX THAT CONTAINS WHATEVER IS IN THE SUPERVISINGTA CHOICES LIST. THIS IS SO THERE CANNOT BE DUPLICATES.
//        for(int i = 0; i < teachingAssistants.size(); i++){
//            if(teachingAssistants.get(i).getName().equals(oldTAName)){ //LOOP THROUGH THE TEACHING ASSISTANTS LIST. FIND THE TA THAT WILL BE REMOVED AND REMOVE IT FROM THE CHOICES LIST FIRST. THIS IS
//                                                                        //BECAUSE IF THE TA IS REMOVED FIRST, THEN IT CANNOT BE FOUND TO REMOVE IT FROM THE CHOICES.
//                supervisingTAsChoices.remove(teachingAssistants.get(i).getName());
//            }
//        }
        teachingAssistants.remove(ta);
        Collections.sort(teachingAssistants);
//        workspace.setSupervisingTAChoices(); //SET THE CHOICES OF THE CHOICEBOX WITH THE NEW UPDATED LIST OF CHOICES, WHICH IS THE CHOICE LIST AFTER THE NAME IS REMOVED FROM IT.

        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices();
        workspace.getSupervising1().getItems().removeAll(supervisingTAsChoices);
        workspace.getSupervising2().getItems().removeAll(supervisingTAsChoices);
        supervisingTAsChoices.remove(ta.getName());
        supervisingTAsChoices.sort(Comparator.naturalOrder());
        
        workspace.setSupervisingTAChoices();
    }
    
    public void editTA(String name, String email, TeachingAssistant ta){
//        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
//        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices();
//        String oldTAName = ta.getName();
//        workspace.removeSupervisingTAChoice(); //REMOVE ALL OF THE CHOICES IN THE CHOICEBOX THAT CONTAINS WHATEVER IS IN THE SUPERVISINGTA CHOICES LIST. THIS IS SO THERE CANNOT BE DUPLICATES.
//         for(int i = 0; i < teachingAssistants.size(); i++){
//            if(teachingAssistants.get(i).getName().equals(oldTAName)){
//                supervisingTAsChoices.remove(teachingAssistants.get(i).getName());
//            }
//        };
        teachingAssistants.remove(ta); //REMOVES THE OLD TA 
        TeachingAssistant updatedTA = new TeachingAssistant(name, email);
        teachingAssistants.add(updatedTA); //ADDS THE UPDATED/EDITED TA
        if (!containsTA(name))
            teachingAssistants.add(updatedTA);
        // SORT THE TAS
        Collections.sort(teachingAssistants);
//        supervisingTAsChoices.add(updatedTA.getName()); //ABOVE IS THE SAME AS REMOVE. HOWEVER, ONE MORE LINE IS NEEDED HERE, WHICH IS TO ADD THE UPDATED CHOICE INTO THE LIST OF SUPERVISING TA CHOICES.
//        Collections.sort(supervisingTAsChoices);
//        workspace.setSupervisingTAChoices();

        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices();
        workspace.getSupervising1().getItems().removeAll(supervisingTAsChoices);
        workspace.getSupervising2().getItems().removeAll(supervisingTAsChoices);
        supervisingTAsChoices.remove(ta.getName());
        supervisingTAsChoices.add(updatedTA.getName());
        supervisingTAsChoices.sort(Comparator.naturalOrder());
        
        workspace.setSupervisingTAChoices();
    }
    public void editTAWithoutConstruct(TeachingAssistant oldTa, TeachingAssistant ta){
//        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
//        String oldTAName = oldTa.getName();
//        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices();
//        workspace.removeSupervisingTAChoice(); //REMOVE ALL OF THE CHOICES IN THE CHOICEBOX THAT CONTAINS WHATEVER IS IN THE SUPERVISINGTA CHOICES LIST. THIS IS SO THERE CANNOT BE DUPLICATES.
//         for(int i = 0; i < teachingAssistants.size(); i++){
//            if(teachingAssistants.get(i).getName().equals(oldTAName)){
//                supervisingTAsChoices.remove(teachingAssistants.get(i).getName());
//            }
//        }
        teachingAssistants.remove(oldTa); //REMOVES THE OLD TA 
        teachingAssistants.add(ta); //ADDS THE UPDATED/EDITED TA
        if (!containsTA(ta.getName()))
            teachingAssistants.add(ta);
        // SORT THE TAS
        Collections.sort(teachingAssistants);
//        supervisingTAsChoices.add(ta.getName()); //ABOVE IS THE SAME AS REMOVE. HOWEVER, ONE MORE LINE IS NEEDED HERE, WHICH IS TO ADD THE UPDATED CHOICE INTO THE LIST OF SUPERVISING TA CHOICES.
//        Collections.sort(supervisingTAsChoices);
//        workspace.setSupervisingTAChoices();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        ObservableList<String> supervisingTAsChoices = workspace.getSupervisingTAsChoices();
        workspace.getSupervising1().getItems().removeAll(supervisingTAsChoices);
        workspace.getSupervising2().getItems().removeAll(supervisingTAsChoices);
        supervisingTAsChoices.remove(oldTa.getName());
        supervisingTAsChoices.add(ta.getName());
        supervisingTAsChoices.sort(Comparator.naturalOrder());
        
        workspace.setSupervisingTAChoices();
    }
    

    public void addOfficeHoursReservation(String day, String time, String taName) {
        String cellKey = getCellKey(day, time);
        toggleTAOfficeHours(cellKey, taName);
    }
    
    /**
     * This function toggles the taName in the cell represented
     * by cellKey. Toggle means if it's there it removes it, if
     * it's not there it adds it.
     */
    public void toggleTAOfficeHours(String cellKey, String taName) {
        StringProperty cellProp = officeHours.get(cellKey);
        String cellText = cellProp.getValue();
        if(cellText.contains(taName))
           removeTAFromCell(cellProp, taName);
        else
           cellProp.setValue(cellText + "\n" + taName);
    }
    
    /**
     * This method removes taName from the office grid cell
     * represented by cellProp.
     */
    public void removeTAFromCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // IS IT THE ONLY TA IN THE CELL?
        if(!cellText.equals("MONDAY") && !cellText.equals("TUESDAY") && !cellText.equals("WEDNESDAY") && !cellText.equals("THURSDAY") && !cellText.equals("FRIDAY")){
            if (cellText.equals(taName))
                cellProp.setValue("");
            // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
            else if (cellText.indexOf(taName) == 0) {
                int startIndex = cellText.indexOf("\n") + 1;
                cellText = cellText.substring(startIndex);
                cellProp.setValue(cellText);
            }
            // IS IT IN THE MIDDLE OF A LIST OF TAs
            else if (cellText.indexOf(taName) < cellText.indexOf("\n", cellText.indexOf(taName))) {
                int startIndex = cellText.indexOf("\n" + taName);
                int endIndex = startIndex + taName.length() + 1;
                cellText = cellText.substring(0, startIndex) + cellText.substring(endIndex);
                cellProp.setValue(cellText);
            }
            // IT MUST BE THE LAST TA
            else {
                if(cellText.length() > 1){
                    int startIndex = cellText.indexOf("\n" + taName);
                    cellText = cellText.substring(0, startIndex);
                    cellProp.setValue(cellText);
                }
                else{
                    cellText = cellText.substring(0, 0);
                    cellProp.setValue(cellText);
                }
            }
        }
    }
    
    public void removeUnnecessaryTAs(StringProperty cellProp){
        cellProp.setValue("");
    }
    
    /**
     * This method EDITS taName from the office grid cell
     * represented by cellProp.
     */
    public void editTAFromCell(StringProperty cellProp, String existingName, String newName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(existingName))
            cellProp.setValue(newName);
        // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (cellText.indexOf(existingName) == 1) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex + existingName.length());
            cellProp.setValue("\n" + newName + cellText);
        }
        // IT MUST BE ANOTHER TA IN THE CELL
        else {
            int startIndex = cellText.indexOf("\n" + existingName);
            String cellText1 = cellText.substring(0, startIndex);
            String cellText2 = cellText.substring(startIndex + existingName.length()+1);
            cellText = cellText1 + cellText2 + "\n"+ newName;
            cellProp.setValue(cellText);
        }
    }
    public void setDataMap(HashMap<String, StringProperty> HashMap){
        officeHours = HashMap;
    }  
    public void addTAToCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // NO TAs IN THE CELL?
        if (cellText == null || cellText.equals(""))
            cellProp.setValue("\n" + taName);
        // IT MUST BE THE LAST TA
        else {
              cellText = cellText + "\n" + taName;
              cellProp.setValue(cellText);    
        }
    }
    
}
