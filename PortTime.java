/*
* File: PortTime.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the PortTime class.
*/

import java.util.*;

class PortTime {
  private int time;

  // Default constructor
  public PortTime() {
    time = 0;
  }

  // Default constructor
  public PortTime(int time) {
    this.time = time;
  }

  // Constructor
  public PortTime(Scanner sc) {
    if (sc.hasNextInt()) {
      time = sc.nextInt();
    }
  }

  // To string method
  public String toString() {
    return "Time: " + time;
  }
}
