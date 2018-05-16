/*
* File: PassengerShip.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the PassengerShip class.
*/

import java.util.*;

class PassengerShip extends Ship {
  private int numberOfPassengers;
  private int numberOfRooms;
  private int numberOfOccupiedRooms;

  // Constructor
  public PassengerShip (Scanner sc) {
    super(sc);
    if (sc.hasNextInt()) {
      numberOfPassengers = sc.nextInt();
    }
    if (sc.hasNextInt()) {
      numberOfRooms = sc.nextInt();
    }
    if (sc.hasNextInt()) {
      numberOfOccupiedRooms = sc.nextInt();
    }
  }

  // To string method
  @Override
  public String toString() {
    return "Passenger Ship: " + super.toString();
  }
}
