/*
* File: ResourcePool.java
* Author: Claire Stovall
* Date: April 30, 2018
* Purpose: This program defines the ResourcePool class.
*/

import java.util.*;
import javax.swing.*;
import java.awt.*;

class ResourcePool {
  private ArrayList<Person> workersInPool;
  private ArrayList<Person> allWorkers;
  private String poolSkill;
  private JPanel poolPanel;
  private JPanel workerPanel;
  private JScrollPane rightPanel;
  private HashMap<Integer, JButton> workerButtons;
  private GridLayout layout;

  // Constructor
  public ResourcePool(String skill, JPanel workerPanel, String portName,
      JScrollPane rightPanel) {
    poolSkill = skill;
    workersInPool = new ArrayList<Person>();
    allWorkers = new ArrayList<Person>();
    workerButtons = new HashMap<Integer, JButton>();
    this.workerPanel = workerPanel;
    this.rightPanel = rightPanel;
    poolPanel = new JPanel();
    poolPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.black), portName
        + " | " + skill));
    layout = new GridLayout(0, 1);
    poolPanel.setLayout(layout);
    workerPanel.add(poolPanel);
    rightPanel.setViewportView(workerPanel);
  }

  // Add workers to pool
  public void addWorker(Person worker) {
    workersInPool.add(worker);
    allWorkers.add(worker);
    JButton tempButton = new JButton(worker.getName()
        + " (" + worker.getSkill() + ")");
    tempButton.setOpaque(true);
    tempButton.setBorderPainted(false);
    workerButtons.put(worker.getIndex(), tempButton);
    poolPanel.add(tempButton);
    worker.setWorking(false);
    updateWorkerView(worker);
    rightPanel.setViewportView(workerPanel);
  }

  // Method to update GUI to reflect that worker is busy or idle
  public void updateWorkerView(Person worker) {
    boolean status = worker.getWorking();
    JButton setButton = workerButtons.get(worker.getIndex());
    if (status) {
      setButton.setBackground(Color.RED);
    } else {
      setButton.setBackground(Color.GREEN);
    }
    rightPanel.setViewportView(workerPanel);
  }

  // Method to return a worker as idle to the pool
  public void returnWorker(Person worker) {
    worker.setWorking(false);
    updateWorkerView(worker);
    workersInPool.add(worker);
  }

  // Get total number of idle workers in pool
  public int getAvailableWorkerCount() {
    return workersInPool.size();
  }

  // Get total number of workers in pool
  public int getMax() {
    return allWorkers.size();
  }

  // Method to add a worker to a job from the pool
  public Person getWorker() {
    if (!workersInPool.isEmpty()) {
      Person tempWorker = workersInPool.remove(0);
      tempWorker.setWorking(true);
      updateWorkerView(tempWorker);
      return tempWorker;
    }
    return null;
  }
}
