package csg.workspace;

import csg.CSGApp;
import djf.AppTemplate;
import djf.components.AppStyleComponent;
import djf.controller.AppFileController;
import djf.ui.AppGUI;
import static csg.CSGProp.*;
import djf.ui.AppMessageDialogSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;
import csg.CSGProp;
import csg.data.CSGData;
import csg.data.Recitation;
import csg.data.TeachingAssistant;
import csg.style.CSGStyle;
import csg.workspace.CSGWorkspace;
import jtps.*;
import csg.transactions.AddTA_Transaction;
import csg.transactions.EditTA_Transaction;
import csg.transactions.RemoveTA_Transaction;
import csg.transactions.ToggleTA_Transaction;
import djf.ui.AppYesNoCancelDialogSingleton;
/**
 * This class provides responses to all workspace interactions, meaning
 * interactions with the application controls not including the file
 * toolbar.
 * 
 * @author Richard McKenna
 * @coauthor: Andrew Yu
 * @version 1.0
 */
public class TAController {
    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    CSGApp app;
    
    //cannot instantiate fileController here with getFileController because
    //these are global static variables which are first constructed before all of the 
    //other methods are constructed, which is why you CANNOT USE GETTERS AND SETTERS HERE to
    //instantiate variables.
    //EXAMPLE: Using AppFileController fileController = gui.getAppFileController(); will break
    //because getAppFileController() DNE.
    //gui is the OVERALL OBJECT THAT ALL CLASSES CAN REFERENCE AND IS THE OBJECT TO REFERENCE IN ORDER
    //TO OBTAIN OBJECTS IN OTHER CLASSES FROM ONE CLASS.
    
    //AppFileController fileController = new AppFileController(app);

    /**
     * Constructor, note that the app must already be constructed.
     */
    public TAController(CSGApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
    }
    
    boolean startTimeOnHour = true;
    boolean endTimeOnHour = true;
    
    ArrayList<Recitation> recitationsEditedFromRemoval;
    ArrayList<Recitation> recitationsEditedFromEdit;
    
    
    public boolean getStartTimeOnHour(){
        return startTimeOnHour;
    }
    
    public void setStartTimeOnHour(boolean value){
        startTimeOnHour = value;
    }
    
    public boolean getEndTimeOnHour(){
        return endTimeOnHour;
    }
    
    public void setEndTimeOnHour(boolean value){
        endTimeOnHour = value;
    }
    
    
    /**
     * This method responds to when the user requests to add
     * a new TA via the UI. Note that it must first do some
     * validation to make sure a unique name and email address
     * has been provided.
     */
    public void handleAddTA() {
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        

        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        TextField nameTextField = workspace.getNameTextField();
        String name = nameTextField.getText();
        
        TextField emailTextField = workspace.getEmailTextField();
        String email = emailTextField.getText();
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        CSGData data = (CSGData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
               
        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE AN EMAIL ADDRESS?
        if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));            
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.getTAData().containsTA(name) || data.getTAData().containsTAEmail(email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
        }
        
        //DID THE USER ENTER AN INVALID EMAIL FORMAT? I.E: NOT A PROPER EMAIL ADDRESS?
        else if(!isCorrectEmail(email)){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(INVALID_TA_EMAIL_FORMAT_TITLE), props.getProperty(INVALID_TA_EMAIL_FORMAT_MESSAGE)); 
        }
        
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            //MARK AS EDITED ONCE ANY CHANGE IS MADE
            fileController.markAsEdited(app.getGUI());

            // ADD THE NEW TA TO THE DATA
            //data.addTA(name, email);
            TeachingAssistant ta = new TeachingAssistant(name, email);
            String nameTest = ta.getName();
            String emailTest = ta.getEmail();
            data.getTAData().addTAWithoutConstruct(ta);
            
            // CLEAR THE TEXT FIELDS
            nameTextField.setText("");
            emailTextField.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            nameTextField.requestFocus();
            emailTextField.requestFocus();
            
            //ADD THE ACTION AS A TRANSACTION
            jTPS transactionComponent = workspace.getTransactionComponent();
            AddTA_Transaction transaction= new AddTA_Transaction(data.getTAData().getTA(name), app); //add a specific AddTA_Transaction transaction 
                                                                    //object to the jTPS array. So when undo, undoTransaction() 
                                                                    //in jTPS will call the undoTransaction() method in AddTA_Transaction.java
            transactionComponent.addTransaction(transaction);

        }
    }
    
    public void handleRemoveTA(){
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        PropertiesManager props = PropertiesManager.getPropertiesManager();


        // GET THE TABLE
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
         // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        CSGData data = (CSGData)app.getDataComponent();
        String taName = "";
        
        //WARNING TO THE USER. REMOVING A TA WOULD REMOVE THE TAS IN RESPECTIVE RECITATIONS. ARE YOU SURE YOU WANT TO DO THIS?
          
	AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show(props.getProperty(CSGProp.REMOVE_TA_WARNING_TITLE),
                props.getProperty(CSGProp.REMOVE_TA_WARNING_MESSAGE)); 
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {
       
            if(ta != null){
                taName = ta.getName();
                data.getTAData().removeTA(ta);
                
                int i = 0;
                int size = data.getRecitationData().getRecitations().size();
                
                recitationsEditedFromRemoval = new ArrayList();
                
               
                while(i < size){
                    String firstTAName = data.getRecitationData().getRecitations().get(i).getFirstTA();
                    String secondTAName = data.getRecitationData().getRecitations().get(i).getSecondTA();
                    if((firstTAName.equals(taName)) && (secondTAName.equals(taName))){
//                        Recitation oldRecitation = data.getRecitationData().getRecitations().get(i);
//                        
//                        
//                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                        data.getRecitationData().getRecitations().get(i).getInstructor(),
//                        data.getRecitationData().getRecitations().get(i).getDayTime(),
//                        data.getRecitationData().getRecitations().get(i).getLocation(), "", "");
//                        
//                        recitationsEditedFromRemoval.add(newRecitation);
//                        
//                        data.getRecitationData().editRecitation(oldRecitation, newRecitation);
//                        
                        Recitation recitation = data.getRecitationData().getRecitations().get(i);
                        recitation.setFirstTA("");
                        recitation.setSecondTA("");
                        i = 0;
                    }
                    else if(data.getRecitationData().getRecitations().get(i).getFirstTA().equals(taName)){
//                        Recitation oldRecitation = data.getRecitationData().getRecitations().get(i);
//                        
//                        
//                        
//                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                        data.getRecitationData().getRecitations().get(i).getInstructor(),
//                        data.getRecitationData().getRecitations().get(i).getDayTime(),
//                        data.getRecitationData().getRecitations().get(i).getLocation(), "", data.getRecitationData().getRecitations().get(i).getSecondTA());
//                        
//                        recitationsEditedFromRemoval.add(newRecitation);
//                        
//                        data.getRecitationData().editRecitation(oldRecitation, newRecitation);
                        Recitation recitation = data.getRecitationData().getRecitations().get(i);
                        recitation.setFirstTA("");
                        i = 0;
                    }
                    else if(data.getRecitationData().getRecitations().get(i).getSecondTA().equals(taName)){
//                        Recitation oldRecitation = data.getRecitationData().getRecitations().get(i);
//                        
//                        
//                        
//                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
//                        data.getRecitationData().getRecitations().get(i).getInstructor(),
//                        data.getRecitationData().getRecitations().get(i).getDayTime(),
//                        data.getRecitationData().getRecitations().get(i).getLocation(), data.getRecitationData().getRecitations().get(i).getFirstTA(), "" );
//                        
//                        recitationsEditedFromRemoval.add(newRecitation);
//                        
//                        data.getRecitationData().editRecitation(oldRecitation, newRecitation);                        
                        Recitation recitation = data.getRecitationData().getRecitations().get(i);
                        recitation.setSecondTA("");
                        i = 0;
                    }
                    else{
                        i++;
                    }
                }
            }
            //Remove TAs of the same name that is marked deleted by 
            //looping through the hashmap datastructure to the grid on the right
            HashMap<String, StringProperty> officeHours = data.getTAData().getOfficeHours();
            Set<String> keySet = officeHours.keySet();
            ArrayList<String> keysToKeep = new ArrayList<>();
            for(String key : keySet){
                StringProperty cellProp = officeHours.get(key);
                String cellText = cellProp.getValue();
                if(!cellText.equals("MONDAY") && !cellText.equals("TUESDAY") && !cellText.equals("WEDNESDAY")
                    && !cellText.equals("THURSDAY") && !cellText.equals("FRIDAY") && cellText.contains(taName)){
                    data.getTAData().removeTAFromCell(cellProp, taName);
                    keysToKeep.add(key);
                }
            }
                    
            //MARK AS EDIT ONCE ANY CHANGE IS MADE
            fileController.markAsEdited(app.getGUI());

            //ADD THE ACTION AS A TRANSACTION
            if(ta != null){
                jTPS transactionComponent = workspace.getTransactionComponent();
                RemoveTA_Transaction transaction= new RemoveTA_Transaction(ta, app, keysToKeep, recitationsEditedFromRemoval); //add a specific RemoveTA_Transaction transaction 
                                                                            //object to the jTPS array. So when undo, undoTransaction() 
                                                                            //in jTPS will call the undoTransaction() method in RemoveTA_Transaction.java
                transactionComponent.addTransaction(transaction);
            }
        }
    }
    
    
    /**
     * This method edits all of the TAs in the grid to the right
     */
    public void handleEditTA(){
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        TextField nameTextField = workspace.getNameTextField();
        String name = nameTextField.getText(); //THE USER UPDATED NAME
        TextField emailTextField = workspace.getEmailTextField();
        String email = emailTextField.getText(); //THE USER UPDATED EMAIL
        
        // GET THE TABLE
        TableView taTable = workspace.getTATable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        
        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        CSGData data = (CSGData)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE AN EMAIL ADDRESS?
        if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));            
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.getTAData().containsTA(name) && data.getTAData().containsTAEmail(email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
        }
        
        //DID THE USER ENTER AN INVALID EMAIL FORMAT? I.E: NOT A PROPER EMAIL ADDRESS?
        else if(!isCorrectEmail(email)){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(INVALID_TA_EMAIL_FORMAT_TITLE), props.getProperty(INVALID_TA_EMAIL_FORMAT_MESSAGE)); 
        }
        
        // EVERYTHING IS FINE, EDIT THE TA
        else {

            //WARNING TO THE USER. EDITING A TA WOULD EDIT THE TAS IN RESPECTIVE RECITATIONS. ARE YOU SURE YOU WANT TO DO THIS?

            AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
            yesNoDialog.show(props.getProperty(CSGProp.EDIT_TA_WARNING_TITLE), props.getProperty(CSGProp.EDIT_TA_WARNING_MESSAGE)); 

            // AND NOW GET THE USER'S SELECTION
            String selection = yesNoDialog.getSelection();

            // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
            if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {

                if(ta != null){
 

                    int i = 0;
                    int size = data.getRecitationData().getRecitations().size();

                    recitationsEditedFromEdit = new ArrayList();


                    while(i < size){
                        String firstTAName = data.getRecitationData().getRecitations().get(i).getFirstTA();
                        String secondTAName = data.getRecitationData().getRecitations().get(i).getSecondTA();
                        if((firstTAName.equals(ta.getName())) && (secondTAName.equals(ta.getName()))){
    //                        Recitation oldRecitation = data.getRecitationData().getRecitations().get(i);
    //                        
    //                        
    //                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
    //                        data.getRecitationData().getRecitations().get(i).getInstructor(),
    //                        data.getRecitationData().getRecitations().get(i).getDayTime(),
    //                        data.getRecitationData().getRecitations().get(i).getLocation(), "", "");
    //                        
    //                        recitationsEditedFromRemoval.add(newRecitation);
    //                        
    //                        data.getRecitationData().editRecitation(oldRecitation, newRecitation);
    //                        
                            Recitation recitation = data.getRecitationData().getRecitations().get(i);
                            recitation.setFirstTA(name);
                            recitation.setSecondTA(name);
                            i = 0;
                        }
                        else if(data.getRecitationData().getRecitations().get(i).getFirstTA().equals(ta.getName())){
    //                        Recitation oldRecitation = data.getRecitationData().getRecitations().get(i);
    //                        
    //                        
    //                        
    //                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
    //                        data.getRecitationData().getRecitations().get(i).getInstructor(),
    //                        data.getRecitationData().getRecitations().get(i).getDayTime(),
    //                        data.getRecitationData().getRecitations().get(i).getLocation(), "", data.getRecitationData().getRecitations().get(i).getSecondTA());
    //                        
    //                        recitationsEditedFromRemoval.add(newRecitation);
    //                        
    //                        data.getRecitationData().editRecitation(oldRecitation, newRecitation);
                            Recitation recitation = data.getRecitationData().getRecitations().get(i);
                            recitation.setFirstTA(name);
                            i = 0;
                        }
                        else if(data.getRecitationData().getRecitations().get(i).getSecondTA().equals(ta.getName())){
    //                        Recitation oldRecitation = data.getRecitationData().getRecitations().get(i);
    //                        
    //                        
    //                        
    //                        Recitation newRecitation = new Recitation(data.getRecitationData().getRecitations().get(i).getSection(),
    //                        data.getRecitationData().getRecitations().get(i).getInstructor(),
    //                        data.getRecitationData().getRecitations().get(i).getDayTime(),
    //                        data.getRecitationData().getRecitations().get(i).getLocation(), data.getRecitationData().getRecitations().get(i).getFirstTA(), "" );
    //                        
    //                        recitationsEditedFromRemoval.add(newRecitation);
    //                        
    //                        data.getRecitationData().editRecitation(oldRecitation, newRecitation);                        
                            Recitation recitation = data.getRecitationData().getRecitations().get(i);
                            recitation.setSecondTA(name);
                            i = 0;
                        }
                        else{
                            i++;
                        }
                    }
                }
                 //MARK AS EDITED ONCE ANY CHANGE IS MADE
                 fileController.markAsEdited(app.getGUI());

                 // EDIT THE TA TO THE DATA 
                 data.getTAData().editTA(name, email, ta);

                 // REUPDATE THE TEXT FIELDS
                 nameTextField.setText(name);
                 emailTextField.setText(email);

                 // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
                 nameTextField.requestFocus();
                 emailTextField.requestFocus();
                    
                    
                //EDIT TAs of the same name into the new name by
                //looping through the hashmap datastructure to the grid on the right
                HashMap<String, StringProperty> officeHours = data.getTAData().getOfficeHours();
                Set<String> keySet = officeHours.keySet();
                ArrayList<String> keysToKeep = new ArrayList<>();
                for(String key : keySet){
                    StringProperty cellProp = officeHours.get(key);
                    String cellText = cellProp.getValue();
                    String taName = ta.getName();
                    if(!cellText.equals("MONDAY") && !cellText.equals("TUESDAY") && !cellText.equals("WEDNESDAY")
                            && !cellText.equals("THURSDAY") && !cellText.equals("FRIDAY") && cellText.contains(taName)){
                        data.getTAData().editTAFromCell(cellProp, taName, name);
                        keysToKeep.add(key);
                    }
                }

                jTPS transactionComponent = workspace.getTransactionComponent();
                EditTA_Transaction transaction= new EditTA_Transaction(ta, data.getTAData().getTA(name), app, keysToKeep);//ta = oldTa, data.getTA(name) = editedTa
                transactionComponent.addTransaction(transaction);
            }
        }  
    }

    /**
     * This function provides a response for when the user clicks
     * on the office hours grid to add or remove a TA to a time slot.
     * 
     * @param pane The pane that was toggled.
     */
    public void handleCellToggle(Pane pane) {
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        //MARK AS EDITED ONCE ANY CHANGE IS MADE
        fileController.markAsEdited(app.getGUI());
         
        // GET THE TABLE
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // GET THE TA
            TeachingAssistant ta = (TeachingAssistant)selectedItem;
            String taName = ta.getName();
            CSGData data = (CSGData)app.getDataComponent();
            String cellKey = pane.getId();
            
            // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
            data.getTAData().toggleTAOfficeHours(cellKey, taName);
            
            jTPS transactionComponent = workspace.getTransactionComponent();
            ToggleTA_Transaction transaction= new ToggleTA_Transaction(app, cellKey, taName);//ta = oldTa, data.getTA(name) = editedTa
            transactionComponent.addTransaction(transaction);
        }
    }
    
    
    
    /**
     * Function checks if email is proper
     * @param email
     * @return 
     */
    public boolean isCorrectEmail(String email){
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();    
    }
    
    public ArrayList getRecitationsEditedArray(){
        return recitationsEditedFromRemoval;
    }

    public void resetOHGrid(String startTime, String endTime){
        
        AppGUI gui = app.getGUI();
        AppFileController fileController = gui.getAppFileController();
        //MARK AS EDITED ONCE ANY CHANGE IS MADE
        fileController.markAsEdited(app.getGUI());
        
        CSGData data = (CSGData)app.getDataComponent();
        
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
            
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
        
        
        HashMap<String, StringProperty> officeHoursTemp = new HashMap<>();
        int originalStartTime = data.getTAData().getStartHour();
        int originalEndTime = data.getTAData().getEndHour();
        
        
        String[] Days = new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
        
        int startTimeHour = Integer.parseInt(startHour);
        int endTimeHour = Integer.parseInt(endHour);
        
        boolean isOnHour = true;
        
        if (startTime.contains("30")){
            isOnHour = false;
            startTimeOnHour = false;
           //counter = 1;
        }
        
        if (endTime.contains("30")){
            endTimeOnHour = false;
        }
        
        // THIS IS SO THAT THE DIFFERENCE IS CONSTANT THROUGHOUT THE LOOP, AND THE KEY IS NOT ALWAYS WITH THE FIRST ROW
        //NEW START HOUR
        String tempStringStartHour0 = data.getTAData().getTimeString(startTimeHour, isOnHour);
        String JSONReadableHour0 = tempStringStartHour0.replace(":", "_");
                
        //OLD START HOUR TO MODIFY
        String tempStringOriginalStartHour0 = data.getTAData().getTimeString(originalStartTime, true); //TEMPORARILY HARD CODE SO THAT IT ALWAYS STARTS WHOLE HOUR
        String JSONReadableOGHour0 = tempStringOriginalStartHour0.replace(":", "_");
        
        //KEYS
        String newStartCellKey = data.getTAData().getCellKey(Days[0], JSONReadableHour0); 
        String oldStartCellKey = data.getTAData().getCellKey(Days[0], JSONReadableOGHour0);
        
        
        int newStartRow = Integer.parseInt(newStartCellKey.substring(newStartCellKey.lastIndexOf("_")+1));
        //reason for this instead of subtracting '0' with charAt
        //is because if the key row is double digit, i.e: "2_10", charAt will obtain the row as '1', instead of "10".
        int originalStartRow = Integer.parseInt(oldStartCellKey.substring(oldStartCellKey.lastIndexOf("_")+1));
        
        
        
        int difference = Math.abs(newStartRow - originalStartRow);
                
        
        for(int i = 0; i < Days.length; i++){
            int j = startTimeHour;

            //INCREMENT EVERY TWO ITERATIONS
            while(j <  endTimeHour){    
                if(j == endTimeHour){
                    break;
                }
                //get the NECESSARY DATA TO NOT DELETE
                
                //NEW START HOUR
                String tempStringStartHour = data.getTAData().getTimeString(j, isOnHour);
                String JSONReadableHour = tempStringStartHour.replace(":", "_");
                
                //OLD START HOUR TO MODIFY
                String tempStringOriginalStartHour = data.getTAData().getTimeString(originalStartTime, true);
                String JSONReadableOGHour = tempStringOriginalStartHour.replace(":", "_");
                
                String currentCellKey = data.getTAData().getCellKey(Days[i], JSONReadableHour); //getting the correct keys based on the time and day!
                
                StringProperty currentData = new SimpleStringProperty();
                

                
                int currentCol = currentCellKey.charAt(0)-'0';
                int currentRow = Integer.parseInt(currentCellKey.substring(currentCellKey.lastIndexOf("_")+1));
                
                if(currentRow > 0){
                    currentData = data.getTAData().getOfficeHours().get(currentCellKey); 
                }
                else{
                    currentData.setValue(null);
                }
                
                String originalStartCellKey = data.getTAData().getCellKey(Days[i], JSONReadableOGHour);
                
                int originalCol = (int)originalStartCellKey.charAt(0) - '0';
                int originalRow = Integer.parseInt(originalStartCellKey.substring(originalStartCellKey.lastIndexOf("_")+1));
                
                if((originalRow < currentRow) && (startTimeHour > originalStartTime)){
                    //subtract the rows (shift up)
                    
                    currentRow = currentRow - difference;      
                    String currentDataNames;
                    if(currentData == null){
                        currentDataNames = "";
                    }
                    else{
                        currentDataNames = currentData.getValue();
                    }
                    String modifiedKey = data.getTAData().getCellKey(currentCol, currentRow);
                    StringProperty forUpdatedLocation = data.getTAData().getOfficeHours().get(modifiedKey);
                    
                    Label cellLabel = new Label();
                    CSGStyle csgStyle = (CSGStyle)app.getStyleComponent();
                    cellLabel.getStyleClass().add(csgStyle.CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
                    cellLabel.setId(modifiedKey);
                    cellLabel.textProperty().setValue(currentDataNames);
                    
                    officeHoursTemp.put(modifiedKey, cellLabel.textProperty());
                }
                else if(currentData == null || currentData.getValue() == null){
                    
                    currentRow = currentRow + difference;   
                    Label cellLabel = new Label();
                    CSGStyle csgStyle = (CSGStyle)app.getStyleComponent();
                    cellLabel.getStyleClass().add(csgStyle.CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
                    String modifiedKey = data.getTAData().getCellKey(currentCol, currentRow);
                    cellLabel.setId(modifiedKey);
                    cellLabel.textProperty().setValue("");
                    officeHoursTemp.put(modifiedKey, cellLabel.textProperty());
                }
                
                else{ 
                    //add the difference to the rows (shift down)
                    currentRow = currentRow + difference;      
                    
                    String currentDataNames = currentData.getValue();
                    String modifiedKey = data.getTAData().getCellKey(currentCol, currentRow);
                    StringProperty forUpdatedLocation = data.getTAData().getOfficeHours().get(modifiedKey);

                        Label cellLabel = new Label();
                        CSGStyle csgStyle = (CSGStyle)app.getStyleComponent();
                        cellLabel.getStyleClass().add(csgStyle.CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
                        cellLabel.setId(modifiedKey);
                        cellLabel.textProperty().setValue(currentDataNames);
                        officeHoursTemp.put(modifiedKey, cellLabel.textProperty());

                }
                
                isOnHour = !isOnHour;
                
                if(isOnHour){
                    j++;
                }

            }
        }
        
        

        workspace.resetWorkspace();

        data.getTAData().initHours(startHour, endHour);
          
//        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
        
        workspace.reloadOH(app.getDataComponent());

        Set<String> keySet = officeHoursTemp.keySet();
        
        for(String key : keySet){
           workspace.getOfficeHoursGridTACellLabels().get(key).setText(officeHoursTemp.get(key).getValue());
        }
      
        
    }
    
    
    public void resetOHGridPutBack(HashMap<String, StringProperty> keysToPutBack){
        CSGData data = (CSGData) app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        Set<String> keySet = keysToPutBack.keySet();
        HashMap<String, StringProperty> originalOfficeHours = data.getTAData().getOfficeHours();
        for(String key : keySet){
            if(((originalOfficeHours.get(key).getValue() == null) || originalOfficeHours.get(key).getValue().equals("")) && (keysToPutBack.get(key).getValue() != null || !keysToPutBack.get(key).getValue().equals(""))){  //IF THE CURRENT OFFICEHOURS HASHMAP DOES NOT HAVE DATA BUT THE KEYSTOPUTBACK HASHMAP DOES WITH RESPECT TO THAT KEY THEN PUT THE DATA BACK IN
                //PUT BACK
                String contents = keysToPutBack.get(key).getValue();
                originalOfficeHours.get(key).set(contents);
                workspace.getOfficeHoursGridTACellLabels().get(key).setText(originalOfficeHours.get(key).getValue());

            }
                //IF THE CURRENT OFFICEHOURS HASHMAP HAS DATA IN THAT KEY (UPDATED ONE AFTER USER CHOICES)
                //DONT PUT BACK BECAUSE THE DATA STILL EXISTS IN THE UPDATED GRID
        }
    }
}