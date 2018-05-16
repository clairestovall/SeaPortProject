/*
* File: Person.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the Person class.
*/

import java.util.*;
import javax.swing.*;

class Person extends Thing {
  private String skill;
  private boolean working;

  // Constructor
  public Person(Scanner sc, HashMap<Integer, SeaPort> seaPortMap,
      JPanel workerPanel, JScrollPane rightPanel) {
    super(sc);
    if (sc.hasNext()) {
      skill = sc.next().trim();
    }
    addToPool(seaPortMap, workerPanel, rightPanel);
    working = false;
  }

  // Add Person to ResourcePool
  public void addToPool(HashMap<Integer, SeaPort> seaPortMap,
      JPanel workerPanel, JScrollPane rightPanel) {
    SeaPort tempSeaPort = seaPortMap.get(this.getParent());
    HashMap<String, ResourcePool> tempPoolMap =
        tempSeaPort.getPools();
    if (!tempPoolMap.containsKey(skill)) {
      ResourcePool resourcePool = new ResourcePool(skill, workerPanel, tempSeaPort.getName(), rightPanel);
      tempPoolMap.put(skill, resourcePool);
      tempPoolMap.get(skill).addWorker(this);
    } else {
      tempPoolMap.get(skill).addWorker(this);
    }
  }

  // Mark worker as working
  public void setWorking(boolean status) {
    working = status;
  }

  // Getter method
  public boolean getWorking() {
    return working;
  }

  // Getter method
  public String getSkill() {
    return skill;
  }

  // To string method
  @Override
  public String toString() {
    return "Person: " + super.getName() + " " + super.getIndex() + " "
        + skill;
  }
}
