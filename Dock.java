/*
* File: Dock.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the Dock class.
*/

import java.util.*;

class Dock extends Thing {
  private Ship ship;

  // Constructor
  public Dock (Scanner sc) {
    super(sc);
    ship = null;
  }

  public void setShip(Ship ship) {
    this.ship = ship;
    System.out.println(ship.getName() + " is docking at "
        + this.getName());
  }

  public void setNullShip() {
    System.out.println(ship.getName() + " has left "
        + this.getName());
    ship = null;
  }

  // Getter method
  public Ship getShip() {
    return ship;
  }

  // To string method
  @Override
  public String toString() {
    String output = "Dock: " + super.getName() + " " + super.getIndex();
    if (ship != null) {
      output += "\n" + ship.toString();
    }
    output += "\n";
    return output;
  }
}
