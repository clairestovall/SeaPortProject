/*
* File: Ship.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the Ship class.
*/

import java.util.*;

class Ship extends Thing {
  private PortTime arrivalTime;
  private PortTime dockTime;
  private double draft;
  private double length;
  private double weight;
  private double width;
  private ArrayList<Job> jobs;

  // Constructor
  public Ship (Scanner sc) {
    super(sc);
    jobs = new ArrayList<Job>();
    arrivalTime = null;
    dockTime = null;
    if (sc.hasNextDouble()) {
      weight = sc.nextDouble();
    }
    if (sc.hasNextDouble()) {
      length = sc.nextDouble();
    }
    if (sc.hasNextDouble()) {
      width = sc.nextDouble();
    }
    if (sc.hasNextDouble()) {
      draft = sc.nextDouble();
    }
  }

  // Getter methods
  public ArrayList<Job> getJobs() {
    return jobs;
  }

  public double getDraft() {
    return draft;
  }

  public double getLength() {
    return length;
  }

  public double getWeight() {
    return weight;
  }

  public double getWidth() {
    return width;
  }

  // To string method
  @Override
  public String toString() {
    String output = super.toString();
    if (jobs.size() == 0) {
      return output;
    }
    for (Job mj: jobs) {
      output += "\n       - " + mj;
    }
    return output;
  }
}
