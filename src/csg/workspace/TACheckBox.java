/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.data.TeachingAssistant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

/**
 *
 * @author Andrew
 */
public class TACheckBox extends TableCell<TeachingAssistant,Boolean>{
    
    CheckBox checkBox;
    Boolean isUndergrad;
    
    public TACheckBox(Boolean value){
        checkBox = new CheckBox();
        isUndergrad = false;
   //     checkBox.setDisable(true);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>(){   
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(isEditing())
                    commitEdit(!isUndergrad);
            }
                    
        });
        setGraphic(checkBox);
        setAlignment(Pos.CENTER);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setEditable(true);
            
    }
    @Override
    public void startEdit(){ //enable edit for checkbox (enable checkbox)
        super.startEdit();
        if(isEmpty()){
            return;
        }
        checkBox.setDisable(false);
        checkBox.requestFocus();
    }
    
    @Override
    public void cancelEdit(){
        super.cancelEdit();
        checkBox.setDisable(true);
    }
    
    @Override
    public void commitEdit(Boolean value){
        super.commitEdit(value);
        checkBox.setDisable(true);
    }
    
    @Override
    public void updateItem(Boolean item, boolean empty){
        super.updateItem(item, empty);
        if(!isEmpty()){
            checkBox.setSelected(item);
        }
    }
    
}
