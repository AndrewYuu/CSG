/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andrew
 */
public class Student<E extends Comparable<E>> implements Comparable<E> {
    
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty team;
    private StringProperty role;
    
    public Student(String initFirstName, String initLastName, String initTeam, String initRole){
        firstName = new SimpleStringProperty(initFirstName);
        lastName = new SimpleStringProperty(initLastName);
        team = new SimpleStringProperty(initTeam);
        role = new SimpleStringProperty(initRole);
    }
    
    
    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getTeam() {
        return team.get();
    }
    
    public void setTeam(String team){
        this.team.set(team);
    }

    public String getRole() {
        return role.get();
    }
    
    @Override
    public int compareTo(E otherStudent){
        return getFirstName().compareTo(((Student)otherStudent).getFirstName());
    }
}
