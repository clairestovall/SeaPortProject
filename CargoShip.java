/*
* File: CargoShip.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the CargoShip class.
*/

import java.util.*;

class CargoShip extends Ship {
  private double cargoValue;
  private double cargoVolume;
  private double cargoWeight;

  // Constructor
  public CargoShip(Scanner sc) {
    super(sc);
    if (sc.hasNextDouble()) {
      cargoWeight = sc.nextDouble();
    }
    if (sc.hasNextDouble()) {
      cargoVolume = sc.nextDouble();
    }
    if (sc.hasNextDouble()) {
      cargoValue = sc.nextDouble();
    }
  }

  // To string method
  @Override
  public String toString() {
    return "Cargo Ship: " + super.toString();
  }
}
