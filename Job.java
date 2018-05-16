/*
* File: Job.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the Job class.
*/

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Job extends Thing implements Runnable {
  private double duration;
  private ArrayList<String> requirements;
  private JPanel parent;
  private JPanel resourceProgressPanel;
  private JProgressBar progressBar;
  private Ship parentShip;
  private SeaPort parentPort;
  private boolean isFinished;
  private Dock parentDock;
  private HashMap<Integer, Dock> dockMap;
  private boolean go = true;
  private boolean stopFlag = false;
  private boolean noKillFlag = true;
  private HashMap<String, JButton> reqButtonMap;
  private HashMap<String, Integer> tallyMap;
  private GridLayout layout2;
  // Use JButton's on the Job panel to allow the job
  // to be suspended or cancelled.
  private JButton statusButton = new JButton("Status");
  private JButton goButton = new JButton("Pause/Resume");
  private JButton locationButton;
  private JButton cancelButton = new JButton("Cancel");
  private ArrayList<Person> workers;
  private boolean impossibleFlag;
  private World world;
  Status status = Status.SUSPENDED;
  private enum Status {RUNNING, SUSPENDED, WAITING, DONE, IMPOSSIBLE};

  // Constructor
  public Job (Scanner sc, JPanel panel, HashMap<Integer, Ship> shipMap,
      HashMap<Integer, SeaPort> seaPortMap, HashMap<Integer, Dock> dockMap,
      World world) {
    super(sc);
    // Reading Job specifications from a data file and adding the required
    // resources to each Job instance.
    requirements = new ArrayList<String>();
    if (sc.hasNextDouble()) {
      duration = sc.nextDouble();
    }
    while (sc.hasNext()) {
      requirements.add(sc.next().trim());
    }
    parent = panel;
    impossibleFlag = false;
    workers = null;
    this.world = world;
    isFinished = false;
    parentShip = shipMap.get(this.getParent());
    parentDock = dockMap.get(parentShip.getParent());
    locationButton = new JButton("Location: ");
    parentPort = seaPortMap.get(parentShip.getParent());
    reqButtonMap = new HashMap<String, JButton>();
    this.dockMap = dockMap;
    if (parentPort != null) {
      // If the Ship doesn't have a Dock resource, update the GUI
      locationButton.setText("Location: "+ parentPort.getName());
    } else {
      // If the Ship does have a Dock resource, update the GUI
      parentPort = seaPortMap.get(dockMap.get(parentShip.getParent()).getParent());
      locationButton.setText("Location: "+ parentPort.getName());
    }
    parent.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.black), this.getName()));

    //  Use a JProgressBar for each job to display the progress of that job.
    progressBar = new JProgressBar();
    progressBar.setStringPainted(true);
    JPanel labelPanel = new JPanel();
    labelPanel.add(new JLabel("Ship: " + parentShip.getName()
        + " | Port: " + parentPort.getName()));
    parent.add(labelPanel);
    resourceProgressPanel = new JPanel();
    resourceProgressPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.black), "Resource Acquisition "
        + "Progress"));
    resourceProgressPanel.add(locationButton);
    locationButton.setBorderPainted(false);
    locationButton.setOpaque(true);
    // Count the frequency of each Job requirement in requirements
    tallyMap = new HashMap<String, Integer>();
    for (String req: requirements) {
      int tally = 0;
      if (tallyMap.containsKey(req)) {
        tally = tallyMap.get(req);
        tally++;
        tallyMap.replace(req, tally);
      } else {
        tallyMap.put(req, 1);
      }
    }
    layout2 = new GridLayout(0, 1);
    resourceProgressPanel.setLayout(layout2);
    // Create JButton(s) for each necessary resource needed for a Job and store
    // those in a HashMap so they can be updated to reflect changes in
    // resource acquisition
    for (String currentKey : tallyMap.keySet()) {
      int count = tallyMap.get(currentKey);
      for (int i = 0; i < count; i++) {
        JButton tempButton = new JButton(currentKey);
        tempButton.setOpaque(true);
        tempButton.setBorderPainted(false);
        tempButton.setBackground(Color.RED);
        resourceProgressPanel.add(tempButton);
        reqButtonMap.put(currentKey + i, tempButton);
      }
    }
    parent.add(resourceProgressPanel);
    parent.add(progressBar);
    statusButton.setOpaque(true);
    statusButton.setBorderPainted(false);
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(statusButton);
    buttonPanel.add(goButton);
    buttonPanel.add(cancelButton);
    parent.add(buttonPanel);

    goButton.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        toggleGoFlag();
      }
    });

    cancelButton.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        cancelJob();
        cancelButton.setText("Cancelled!");
      }
    });

    // Implement a thread for each job representing a task that ship requires.
    // The thread for each job should be started as the job is read in
    // from the data file.
    new Thread(this, this.getName()).start();
  }

  // Setters
  public void setStop() {
    stopFlag = true;
  }

  public void cancelJob() {
    noKillFlag = false;
  }

  public void setImpossible() {
    impossibleFlag = true;
  }

  private void toggleGoFlag() {
    go = !go;
  }

  // Job threads - using the resource pools and supporting the concept of
  // blocking until required resources are available before proceeding.
  private synchronized boolean checkResources() {
    if (parentPort.getQue().contains(parentShip)) {
      // If the Ship doesn't have a dock
      return true;
    }
    parentDock = dockMap.get(parentShip.getParent());
    // Update the GUI to reflect the resource acquisition
    locationButton.setText("Location: " + parentDock.getName());
    if (this.getRequirements().isEmpty()) {
      // If the Job has no requirements
      return false;
    } else if (!this.getRequirements().isEmpty()) {
      // If the job has requirements
      workers = parentPort.getResources(this, reqButtonMap, tallyMap);
      if (workers == null) {
        if (impossibleFlag) {
          if (!world.getScanningComplete()) {
            try {
              Thread.sleep(1000); // Wait a temporary period to allow the rest of
              // the Jobs in the input file to be read in. Otherwise, the Ship
              // may leave the Dock too soon because the World has not yet
              // instantiated all of the Jobs on that Ship, but the Jobs that
              // have already been read in are finished or impossible.
            } catch (InterruptedException e) {
              System.out.println("Interrupted on " + this.getName() + " !");
            }
          }
          return false;
        }
        // If the necessary workers aren't available
        return true;
      }
      // If the ship is at a dock and all the people with required skills are
      // available, the job should start.
      return false;
    }
    return false;
  }

  // Run method
  @Override
  public void run() {
    long time = System.currentTimeMillis();
    long startTime = time;
    double stopTime = time + 1000 * duration;
    double runDuration = stopTime - time;
    // Use synchronization to avoid race conditions.
    synchronized (parentPort) {
      while (checkResources()) {
        showStatus(Status.WAITING);
        try {
          parentPort.wait();
        } catch (InterruptedException e) {
          System.out.println("Interrupted on " + this.getName() + " !");
        }
        if (stopFlag) {
          // Wake up the other threads so that they can return out of their
          // run() methods if a new World has been created in the GUI
          parentPort.notifyAll();
          return;
        }
      }
    }
    while (time < stopTime && noKillFlag) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        System.out.println("Interrupted on " + this.getName() + " !");
      }
      if (go) {
        showStatus(Status.RUNNING);
        time += 100;
        progressBar.setValue((int)(((time - startTime) / runDuration) * 100));
      } else {
        showStatus(Status.SUSPENDED);
      }
    }
    showStatus(Status.DONE);
    progressBar.setValue(100);
    synchronized (parentPort) {
      if (stopFlag) {
        // Wake up the other threads so that they can return out of their
        // run() methods if a new World has been created in the GUI
        parentPort.notifyAll();
        return;
      }
      if (workers != null) {
        // When a job is over, all the resources used by the job (the people)
        // should be released back to the port.
        parentPort.returnResources(workers, reqButtonMap);
      }
      for (Dock tempDock: parentPort.getDocks()) {
        if (tempDock.getShip() != null) {
          if (tempDock.getShip().getJobs().size() == 0) {
            // Make Ships that don't have any Jobs leave their Dock
            leaveDock(tempDock.getShip());
          }
        }
      }
      isFinished = true;
      if (getJobsFinished()) {
        // When all the jobs of a ship are done, the ship should depart the
        // dock and if there are any ships in the port que, one of them
        // should should be assigned to the free dock, and that ships jobs
        // can now try to progress.
        leaveDock();
      }
      parentPort.notifyAll();
    }
  }

  // Getter
  public JButton getLocationButton() {
    return locationButton;
  }

  private void leaveDock() {
    ArrayList<Job> tempJobList = parentShip.getJobs();
    for (int i = 0; i < tempJobList.size(); i++) {
      Job buttonJob = tempJobList.get(i);
      JButton tempButton = buttonJob.getLocationButton();
      tempButton.setText("Location: left dock");
    }
    parentShip.setParent(0);
    parentDock.setNullShip();
    if (!parentPort.getQue().isEmpty()) {
      parentShip = parentPort.getQue().remove(0);
      parentDock.setShip(parentShip);
      parentShip.setParent(parentDock.getIndex());
      if (parentShip.getJobs().size() == 0) {
        leaveDock();
      }
    }
  }

  private void leaveDock(Ship tempShip) {
    ArrayList<Job> tempJobList = tempShip.getJobs();
    for (int i = 0; i < tempJobList.size(); i++) {
      Job buttonJob = tempJobList.get(i);
      JButton tempButton = buttonJob.getLocationButton();
      tempButton.setText("Location: left dock");
    }
    Dock tempParentDock = dockMap.get(tempShip.getParent());
    tempShip.setParent(0);
    tempParentDock.setNullShip();
    if (!parentPort.getQue().isEmpty()) {
      Ship tempParentShip = parentPort.getQue().remove(0);
      tempParentDock.setShip(tempParentShip);
      tempParentShip.setParent(tempParentDock.getIndex());
      if (tempParentShip.getJobs().size() == 0) {
        leaveDock(tempParentShip);
      }
    }
  }

  // Getter
  public boolean getFinished() {
    return isFinished;
  }

  // Check if the Ship can leave the Dock because all Jobs are impossible
  // or done
  private boolean getJobsFinished() {
    for (Job job: parentShip.getJobs()) {
      if (!job.getFinished()) {
        return false;
      }
    }
    return true;
  }

  private void showStatus(Status st) {
    status = st;
    switch (status) {
      case RUNNING:
        statusButton.setBackground(Color.GREEN);
        statusButton.setText("Running");
        break;
      case SUSPENDED:
        statusButton.setBackground(Color.YELLOW);
        statusButton.setText("Suspended");
        break;
      case WAITING:
        statusButton.setBackground(Color.ORANGE);
        statusButton.setText("Waiting");
        break;
      case DONE:
        statusButton.setBackground(Color.RED);
        statusButton.setText("Done");
        if (impossibleFlag) {
          statusButton.setText("Impossible");
          statusButton.setBackground(Color.BLUE);
        }
        break;
    }
  }

  // Getter
  public ArrayList<String> getRequirements() {
    return requirements;
  }

  // To string method
  @Override
  public String toString() {
    return "Job: " + super.toString();
  }
}
