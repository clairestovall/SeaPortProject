/*
* File: SeaPort.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the SeaPort class.
*/

import java.util.*;
import javax.swing.*;
import java.awt.*;

class SeaPort extends Thing {

  private ArrayList<Dock> docks;
  private ArrayList<Ship> que; // the list of ships waiting to dock
  private ArrayList<Ship> ships; // a list of all the ships at this port
  private ArrayList<Person> persons; // people with skills at this port
  private HashMap<String, ResourcePool> pools; // skill pools at this port

  // Constructor
  public SeaPort (Scanner sc) {
    super(sc);
    docks = new ArrayList<Dock>();
    que = new ArrayList<Ship>();
    ships = new ArrayList<Ship>();
    persons = new ArrayList<Person>();
    // Resource pools - SeaPort.ArrayList <Person> list of persons with
    // particular skills at each port, treated as resource pools, along with
    // supporting assignment to ships and jobs.
    pools = new HashMap<String, ResourcePool>();
  }

  // Getter
  public HashMap<String, ResourcePool> getPools() {
    return pools;
  }

  // Method to check if worker(s) are available to meet Job requirements
  public ArrayList<Person> getResources(Job currentJob,
      HashMap<String, JButton> reqButtonMap,
      HashMap<String, Integer> tallyMap) {
    ResourcePool currentPool;
    HashMap<String, Integer> completedMap = new HashMap<String, Integer>();
    ArrayList<Person> workers = new ArrayList<Person>();

    for (String req: currentJob.getRequirements()) {
      int duplicates;
      if (pools.containsKey(req)) {
        if (tallyMap.get(req) > pools.get(req).getMax()) {
          // If a job can never progress because the port doesn't have enough
          // skills among all the persons at the port, the program should report
          // this and cancel the job.
          currentJob.cancelJob();
          currentJob.setImpossible();
          if (workers != null) {
            // the Job should not hold any resources if it cannot progress.
            this.returnResources(workers, reqButtonMap);
            workers = null;
          }
          return workers;
        } else if (pools.get(req).getAvailableWorkerCount() > 0) {
          workers.add(pools.get(req).getWorker());
          if (completedMap.containsKey(req)) {
            duplicates = completedMap.get(req);
            duplicates++;
            completedMap.replace(req, duplicates);
          } else {
            duplicates = 0;
            completedMap.put(req, duplicates);
          }
          JButton tempWorkerButton = reqButtonMap.get(req + duplicates);
          tempWorkerButton.setBackground(Color.GREEN);
        } else {
          if (workers != null) {
            // the Job should not hold any resources if it cannot progress.
            this.returnResources(workers, reqButtonMap);
            workers = null;
          }
          return workers;
        }
      } else {
        // If a job can never progress because the port doesn't have enough
        // skills among all the persons at the port, the program should report
        // this and cancel the job.
        currentJob.cancelJob();
        currentJob.setImpossible();
        if (workers != null) {
          // the Job should not hold any resources if it cannot progress.
          this.returnResources(workers, reqButtonMap);
          workers = null;
        }
        return workers;
      }
    }
    return workers;
  }

  // Method to return workers to ResourcePool and mark as idle
  public void returnResources(ArrayList<Person> workerList,
      HashMap<String, JButton> reqButtonMap) {
    if (workerList != null && !workerList.isEmpty()) {
      for (Person returnedWorker: workerList) {
        pools.get(returnedWorker.getSkill()).returnWorker(returnedWorker);
      }
      for (String currentKey : reqButtonMap.keySet()) {
        JButton currentButton = reqButtonMap.get(currentKey);
        currentButton.setBackground(Color.RED);
      }
    }
  }

  // Getter methods
  public ArrayList<Ship> getShips() {
    return ships;
  }

  public ArrayList<Ship> getQue() {
    return que;
  }

  public ArrayList<Dock> getDocks() {
    return docks;
  }

  public ArrayList<Person> getPeople() {
    return persons;
  }

  // To string method
  @Override
  public String toString() {
    String output = "*SeaPort: " + super.toString() + "\n";
    for (Dock md: docks) {
      output += "\n" + md;
    }
    output += "\n --- List of all ships in que:";
    for (Ship ms: que) {
      output += "\n   > " + ms;
    }
    output += "\n\n --- List of all ships:";
    for (Ship ms: ships) {
      output += "\n   > " + ms;
    }
    output += "\n\n --- List of all persons:";
    for (Person mp: persons) {
      output += "\n   > " + mp;
    }
    output += "\n";
    return output;
  }
}
