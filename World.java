/*
* File: World.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the World class.
*/

import java.util.*;
import javax.swing.*;
import java.io.*;

class World extends Thing {

  private ArrayList<SeaPort> ports;
  private PortTime time;
  private Scanner scanner;
  private Scanner processScanner;
  private String line;
  private boolean scanningComplete;

  // Constructor
  public World(String name, int index, int parent, File file,
      JPanel jobsPanel, JPanel workerPanel, JScrollPane rightPanel) {
    super(name, index, parent);
    time = new PortTime();
    ports = new ArrayList<SeaPort>();
    readFile(file, jobsPanel, workerPanel, rightPanel, this);
    scanningComplete = true;
  }

  // Getter
  public boolean getScanningComplete() {
    return scanningComplete;
  }

  // Read file method
  private void readFile(File file, JPanel jobsPanel, JPanel workerPanel,
      JScrollPane rightPanel, World world) {
    HashMap<Integer, SeaPort> seaPortMap = new HashMap<Integer, SeaPort>();
    HashMap<Integer, Dock> dockMap = new HashMap<Integer, Dock>();
    HashMap<Integer, Ship> shipMap = new HashMap<Integer, Ship>();

    // Read the data file
    try {
      scanner = new Scanner(file);
      while (scanner.hasNext()) {
        line = scanner.nextLine();
        process(line, seaPortMap, dockMap, shipMap, jobsPanel, workerPanel,
            rightPanel, world);
      }
    } catch (FileNotFoundException ex) {
      JOptionPane.showMessageDialog(null, "File did not open.",
        "Warning", JOptionPane.WARNING_MESSAGE);
    } finally {
      if (scanner != null) {
        scanner.close();
      }
    }
  }

  public void stopPriorThreads() {
    for (SeaPort allPorts: ports) {
      for (Ship ship: allPorts.getShips()) {
        for (Job job: ship.getJobs()) {
          job.setStop();
        }
      }
    }
  }

  // Create the internal data structure
  private void process(String nextLine, HashMap<Integer, SeaPort> seaPortMap,
      HashMap<Integer, Dock> dockMap, HashMap<Integer, Ship> shipMap,
      JPanel jobsPanel, JPanel workerPanel, JScrollPane rightPanel,
      World world) {
    if (nextLine.equals("")) {
      return;
    }
    try {
      processScanner = new Scanner(nextLine);
      switch (processScanner.next().trim().toLowerCase()) {
        case "port":
          assignPort(new SeaPort(processScanner), seaPortMap);
          break;
        case "dock": assignDock(new Dock(processScanner), dockMap,
            seaPortMap);
          break;
        case "pship": assignShip(new PassengerShip(processScanner), shipMap,
            seaPortMap, dockMap);
          break;
        case "cship": assignShip(new CargoShip(processScanner), shipMap,
            seaPortMap, dockMap);
          break;
        case "job":
          JPanel panel = new JPanel();
          BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
          panel.setLayout(layout);
          assignJob(new Job(processScanner, panel, shipMap, seaPortMap,
              dockMap, world), shipMap);
          jobsPanel.add(panel);
          break;
        case "person": assignPerson(new Person(processScanner, seaPortMap,
            workerPanel, rightPanel), seaPortMap);
          break;
        case "//":
          break;
      }
    } finally {
      if (processScanner != null) {
        processScanner.close();
      }
    }
  }

  // Methods to link objects to parents
  private void assignPerson(Person person,
      HashMap<Integer, SeaPort> seaPortMap) {
    seaPortMap.get(person.getParent()).getPeople().add(person);
  }

  private void assignPort(SeaPort port, HashMap<Integer, SeaPort> seaPortMap) {
    seaPortMap.put(port.getIndex(), port);
    ports.add(port);
  }

  private void assignJob(Job job, HashMap<Integer, Ship> shipMap) {
    shipMap.get(job.getParent()).getJobs().add(job);
  }

  private void assignDock(Dock dock, HashMap<Integer, Dock> dockMap,
        HashMap<Integer, SeaPort> seaPortMap) {
    dockMap.put(dock.getIndex(), dock);
    seaPortMap.get(dock.getParent()).getDocks().add(dock);
  }

  private void assignShip(Ship ship, HashMap<Integer, Ship> shipMap,
      HashMap<Integer, SeaPort> seaPortMap, HashMap<Integer, Dock> dockMap) {
    shipMap.put(ship.getIndex(), ship);
    Dock myDock = dockMap.get(ship.getParent());
    if (myDock == null) {
      seaPortMap.get(ship.getParent()).getQue().add(ship);
      seaPortMap.get(ship.getParent()).getShips().add(ship);
      return;
    }
    myDock.setShip(ship);
    seaPortMap.get(myDock.getParent()).getShips().add(ship);
  }

  // Search methods
  public String getPeopleBySkill(String skill) {
    String listOfPeople = "Results: ";
    for (SeaPort allPorts: ports) {
      for (Person person: allPorts.getPeople()) {
        if (person.getSkill().equals(skill)) {
          listOfPeople += "\n" + person.toString();
        }
      }
    }
    return listOfPeople;
  }

  public String getByIndex(String index) {
    String resultsList = "Results: ";
    int searchIndex = 0;
    try {
      searchIndex = Integer.parseInt(index);
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(null, "Please enter a "
          + "valid index.", "Warning", JOptionPane.WARNING_MESSAGE);
    }
    for (SeaPort allPorts: ports) {
      if (allPorts.getIndex() == searchIndex) {
        resultsList += "\nPort: " + allPorts.getName() + " "
            + allPorts.getIndex();
      }
      for (Person person: allPorts.getPeople()) {
        if (person.getIndex() == searchIndex) {
          resultsList += "\n" + person.toString();
        }
      }
      for (Dock dock: allPorts.getDocks()) {
        if (dock.getIndex() == searchIndex) {
          resultsList += "\nDock: " + dock.getName() + " " + dock.getIndex();
        }
      }
      for (Ship ship: allPorts.getShips()) {
        if (ship.getIndex() == searchIndex) {
          resultsList += "\n" + ship.toString();
        }
        for (Job job: ship.getJobs()) {
          if (job.getIndex() == searchIndex) {
            resultsList += "\nJob: " + job.getName() + " " + job.getIndex();
          }
        }
      }
    }
    return resultsList;
  }

  public String getByName(String name) {
    String resultsOutput = "Results: ";
    for (SeaPort allPorts: ports) {
      if (allPorts.getName().equals(name)) {
        resultsOutput += "\nPort: " + allPorts.getName() + " "
            + allPorts.getIndex();
      }
      for (Person person: allPorts.getPeople()) {
        if (person.getName().equals(name)) {
          resultsOutput += "\n" + person.toString();
        }
      }
      for (Dock dock: allPorts.getDocks()) {
        if (dock.getName().equals(name)) {
          resultsOutput += "\nDock: " + dock.getName() + " " + dock.getIndex();
        }
      }
      for (Ship ship: allPorts.getShips()) {
        if (ship.getName().equals(name)) {
          resultsOutput += "\n" + ship.toString();
        }
        for (Job job: ship.getJobs()) {
          if (job.getName().equals(name)) {
            resultsOutput += "\nJob: " + job.getName() + " " + job.getIndex();
          }
        }
      }
    }
    return resultsOutput;
  }

  // Getter method
  public ArrayList<SeaPort> getPorts() {
    return ports;
  }

  // Sort methods
  public String sort(String sortType, String sortByType) {
    String output = "Sort results: ";
    switch (sortByType) {
      case "Name":
        switch (sortType) {
          case "SeaPorts":
            Collections.sort(ports);
            for (SeaPort allPorts: ports) {
              output += "\n" + allPorts.toString();
            }
            return output;
          case "Docks":
            for (SeaPort allPorts: ports) {
              output += "\n*Port: " + allPorts.getName() + "\n";
              Collections.sort(allPorts.getDocks());
              for (Dock dock: allPorts.getDocks()) {
                output += "\n" + dock.toString();
              }
            }
            return output;
          case "Ques":
            for (SeaPort allPorts: ports) {
              output += "\n\n*Port: " + allPorts.getName();
              output += "\n\n --- List of all ships in que:";
              Collections.sort(allPorts.getQue());
              for (Ship ms: allPorts.getQue()) {
                output += "\n   > " + ms;
              }
            }
            return output;
          case "Ships":
            for (SeaPort allPorts: ports) {
              output += "\n\n*Port: " + allPorts.getName();
              output += "\n\n --- List of all ships:";
              Collections.sort(allPorts.getShips());
              for (Ship ms: allPorts.getShips()) {
                output += "\n   > " + ms;
              }
            }
            return output;
          case "People":
            for (SeaPort allPorts: ports) {
              output += "\n\n*Port: " + allPorts.getName();
              output += "\n\n --- List of all persons:";
              Collections.sort(allPorts.getPeople());
              for (Person mp: allPorts.getPeople()) {
                output += "\n   > " + mp;
              }
            }
            return output;
          case "Jobs":
            for (SeaPort allPorts: ports) {
              output += "\n\n*Port: " + allPorts.getName();
              output += "\n\n --- List of all ships:\n";
              for (Ship ms: allPorts.getShips()) {
                output += "\nShip: " + ms.getName() + "\n";
                Collections.sort(ms.getJobs());
                for (Job mj: ms.getJobs()) {
                  output += "       - " + mj + "\n";
                }
              }
            }
            return output;
          case "Job Requirements":
            for (SeaPort allPorts: ports) {
              output += "\n\n*Port: " + allPorts.getName();
              output += "\n\n --- List of all ships:\n";
              for (Ship ms: allPorts.getShips()) {
                output += "\nShip: " + ms.getName() + "\n";
                for (Job mj: ms.getJobs()) {
                  output += "       - " + mj + "\n";
                  output += "            Requirements: ";
                  Collections.sort(mj.getRequirements());
                  for (String req : mj.getRequirements()) {
                    output += req + " ";
                  }
                  output += "\n";
                }
              }
            }
            return output;
        }
        break;
      case "Weight":
        if (sortType.equals("Ships")) {
          for (SeaPort allPorts: ports) {
            output += "\n\n*Port: " + allPorts.getName();
            output += "\n\n --- List of all ships:";
            Collections.sort(allPorts.getShips(), new ShipComparator("Weight"));
            for (Ship ms: allPorts.getShips()) {
              output += "\n   > " + ms + "\n       Weight: " + ms.getWeight();
            }
          }
        } else if (sortType.equals("Ques")){
          for (SeaPort allPorts: ports) {
            output += "\n\n*Port: " + allPorts.getName();
            output += "\n\n --- List of all ships in que:";
            Collections.sort(allPorts.getQue(), new ShipComparator("Weight"));
            for (Ship ms: allPorts.getQue()) {
              output += "\n   > " + ms + "\n       Weight: " + ms.getWeight();
            }
          }
        }
        return output;
      case "Length":
        if (sortType.equals("Ships")) {
          for (SeaPort allPorts: ports) {
            output += "\n\n*Port: " + allPorts.getName();
            output += "\n\n --- List of all ships:";
            Collections.sort(allPorts.getShips(), new ShipComparator("Length"));
            for (Ship ms: allPorts.getShips()) {
              output += "\n   > " + ms + "\n       Length: " + ms.getLength();
            }
          }
        } else if (sortType.equals("Ques")){
          for (SeaPort allPorts: ports) {
            output += "\n\n*Port: " + allPorts.getName();
            output += "\n\n --- List of all ships in que:";
            Collections.sort(allPorts.getQue(), new ShipComparator("Length"));
            for (Ship ms: allPorts.getQue()) {
              output += "\n   > " + ms + "\n       Length: " + ms.getLength();
            }
          }
        }
        return output;
      case "Width":
        if (sortType.equals("Ships")) {
          for (SeaPort allPorts: ports) {
            output += "\n\n*Port: " + allPorts.getName();
            output += "\n\n --- List of all ships:";
            Collections.sort(allPorts.getShips(), new ShipComparator("Width"));
            for (Ship ms: allPorts.getShips()) {
              output += "\n   > " + ms + "\n       Width: " + ms.getWidth();
            }
          }
        } else if (sortType.equals("Ques")){
          for (SeaPort allPorts: ports) {
            output += "\n\n*Port: " + allPorts.getName();
            output += "\n\n --- List of all ships in que:";
            Collections.sort(allPorts.getQue(), new ShipComparator("Width"));
            for (Ship ms: allPorts.getQue()) {
              output += "\n   > " + ms + "\n       Width: " + ms.getWidth();
            }
          }
        }
        return output;
      case "Draft":
        if (sortType.equals("Ships")) {
          for (SeaPort allPorts: ports) {
            output += "\n\n*Port: " + allPorts.getName();
            output += "\n\n --- List of all ships:";
            Collections.sort(allPorts.getShips(), new ShipComparator("Draft"));
            for (Ship ms: allPorts.getShips()) {
              output += "\n   > " + ms + "\n       Draft: " + ms.getDraft();
            }
          }
        } else if (sortType.equals("Ques")){
          for (SeaPort allPorts: ports) {
            output += "\n\n*Port: " + allPorts.getName();
            output += "\n\n --- List of all ships in que:";
            Collections.sort(allPorts.getQue(), new ShipComparator("Draft"));
            for (Ship ms: allPorts.getQue()) {
              output += "\n   > " + ms + "\n       Draft: " + ms.getDraft();
            }
          }
        }
        return output;
    }
    return output;
  }

  // To string method
  @Override
  public String toString() {
    String output = ">>>>> The world: \n\n\n";
    for (SeaPort pt: ports) {
      output += "\n" + pt.toString();
    }
    return output;
  }

  private class ShipComparator implements Comparator<Ship> {
    private String sortByType;

    // Constructor
    public ShipComparator(String sortByType) {
      this.sortByType = sortByType;
    }

    // Compare method
    @Override
    public int compare(Ship first, Ship second) {
      int output = 0;
      switch (sortByType) {
        case "Weight":
          if (first.getWeight() > second.getWeight()) {
            output = 1;
          } else if (first.getWeight() < second.getWeight()) {
            output = -1;
          }
          break;
        case "Length":
          if (first.getLength() > second.getLength()) {
            output = 1;
          } else if (first.getLength() < second.getLength()) {
            output = -1;
          }
          break;
        case "Width":
          if (first.getWidth() > second.getWidth()) {
            output = 1;
          } else if (first.getWidth() < second.getWidth()) {
            output = -1;
          }
          break;
        case "Draft":
          if (first.getDraft() > second.getDraft()) {
            output = 1;
          } else if (first.getDraft() < second.getDraft()) {
            output = -1;
          }
          break;
      }
      return output;
    }
  }
}
