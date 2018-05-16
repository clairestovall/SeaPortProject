/*
* File: Thing.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the Thing class.
*/

import java.util.*;

class Thing implements Comparable<Thing> {
  private int index;
  private String name;
  private int parent;

  // Constructors
  public Thing(Scanner sc) {
    this(sc.next().trim(), sc.nextInt(), sc.nextInt());
  }

  public Thing(String name, int index, int parent) {
    this.name = name;
    this.index = index;
    this.parent = parent;
  }

  // Getter and setter methods
  public int getIndex() {
    return index;
  }

  public int getParent() {
    return parent;
  }

  public void setParent(int parent) {
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  // To string method
  @Override
  public String toString() {
    return (name + " " + index);
  }

  // Method for comparison
  @Override
  public int compareTo(Thing thing) {
    return name.compareTo(thing.getName());
  }
}
