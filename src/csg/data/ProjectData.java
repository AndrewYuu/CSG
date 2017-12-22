/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import csg.workspace.CSGWorkspace;
import java.util.Collections;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class ProjectData{
    CSGApp app;
    ObservableList<Team> teams;
    ObservableList<Student> students;
    
    public ProjectData(CSGApp initApp){
        app = initApp;
        
        teams = FXCollections.observableArrayList();
        students = FXCollections.observableArrayList();
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
    }
    
    public void resetData(){
        teams.clear();
        students.clear();
    }
    
    public void addTeam(Team team){
        teams.add(team);
        Collections.sort(teams);
        
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        ObservableList<String> studentTeamChoices = workspace.getStudentTeamChoices();
        workspace.getStudentTeamChoiceBox().getItems().removeAll(studentTeamChoices);

        studentTeamChoices.add(team.getTeamName());
        studentTeamChoices.sort(Comparator.naturalOrder());
        
        workspace.setStudentTeamsInChoiceBox();
    }
    
    public void editTeam(Team oldTeam, Team newTeam){
            
        if(!containsTeam(newTeam.getTeamName())){
            teams.remove(oldTeam);
            teams.add(newTeam);
            
              
            CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
            ObservableList<String> studentTeamChoices = workspace.getStudentTeamChoices();
            workspace.getStudentTeamChoiceBox().getItems().removeAll(studentTeamChoices);

            studentTeamChoices.remove(oldTeam.getTeamName());
            studentTeamChoices.add(newTeam.getTeamName());
            studentTeamChoices.sort(Comparator.naturalOrder());

            workspace.setStudentTeamsInChoiceBox();
        }
        Collections.sort(teams);
    }
    
    public void removeTeam(Team team){
        teams.remove(team);
        Collections.sort(teams);
        
         
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        ObservableList<String> studentTeamChoices = workspace.getStudentTeamChoices();
        workspace.getStudentTeamChoiceBox().getItems().removeAll(studentTeamChoices);

        studentTeamChoices.remove(team.getTeamName());
        studentTeamChoices.sort(Comparator.naturalOrder());
        
        workspace.setStudentTeamsInChoiceBox();
    }
    
    public void addStudent(Student student){
        students.add(student);
        Collections.sort(students);
    }
    
    public void editStudent(Student oldStudent, Student newStudent){
            students.remove(oldStudent);
            students.add(newStudent);
            Collections.sort(students);
    }
    
    public void removeStudent(Student student){
            students.remove(student);
            Collections.sort(students);
    }
    
    public ObservableList<Student> getStudents(){
        return students;
    }
   
    public ObservableList<Team> getTeams(){
        return teams;
    }
    
    public boolean containsTeam(String teamName) {
        for (Team team : teams) {
            if (team.getTeamName().equals(teamName))
                return true;
        }
        return false;
    }
    
}
