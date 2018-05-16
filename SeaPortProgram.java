/*
* File: SeaPortProgram.java
* Author: Claire Stovall
* Date: March 25, 2018
* Purpose: This program defines the GUI for the sea port program.
*/

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;

class SeaPortProgram extends JFrame {

  private JPanel panel;
  private JPanel topPanel;
  private JScrollPane rightPanel;
  private JPanel inputPanel;
  private JPanel workerPanel1;
  private JPanel bottomPanel;
  private JPanel sortPanel;
  private BorderLayout borderLayout;
  private JScrollPane scrollPane;
  private JScrollPane scrollPane2;
  private JScrollPane scrollPane3;
  private JScrollPane scrollPane4;
  private JScrollPane scrollPane5;
  private JTextArea textArea;
  private JTextArea textArea2;
  private JTextArea searchTextArea;
  private JTabbedPane tabbedPane;
  private JTextField textFieldInput;
  private Handler handler;
  private JLabel comboBoxLabel;
  private JLabel chooseFileLabel;
  private JLabel sortLabel;
  private JLabel sortByLabel;
  private JLabel inputSearchLabel;
  private JButton chooseFileButton;
  private JButton sortButton;
  private JButton searchButton;
  private FlowLayout flowLayout;
  private FlowLayout flowLayout2;
  private BoxLayout boxLayout;
  private BoxLayout boxLayout2;
  private GridBagConstraints c;
  private JComboBox<String> selectionBox;
  private JComboBox<String> sortSelectionBox;
  private JComboBox<String> sortBySelectionBox;
  private GridBagLayout gridBagLayout;
  private String results;
  private static final String[] SELECTION_BOX_LIST = {"Name", "Index", "Skill"};
  private static final String[] SORT_LIST = {"SeaPorts", "Docks", "Ships",
      "Ques", "People", "Jobs", "Job Requirements"};
  private static final String[] BY_SORT_LIST = {"Name"};
  private static final long serialVersionUID = 1L;
  private static final int WIDTH = 1250;
  private static final int HEIGHT = 800;
  private static final int ZERO = 0;
  private static final int ONE = 1;
  private static final int TWO = 2;
  private static final int THREE = 3;
  private static final int FOUR = 4;

  // Constructor to define the GUI
  public SeaPortProgram() {
    // Customize frame
    super("Sea Port Program");
    setSize(WIDTH, HEIGHT);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    handler = new Handler();

    // Create feature panel
    panel = new JPanel();
    borderLayout = new BorderLayout();
    panel.setLayout(borderLayout);

    // Create panel for resources in pools
    workerPanel1 = new JPanel();
    rightPanel = new JScrollPane(workerPanel1);

    // Create sorting functions
    sortLabel = new JLabel("Sort:");
    sortSelectionBox = new JComboBox<String>(SORT_LIST);
    sortSelectionBox.addActionListener(handler);
    sortByLabel = new JLabel("by:");
    sortBySelectionBox = new JComboBox<String>(BY_SORT_LIST);
    sortButton = new JButton("Sort");
    sortButton.addActionListener(handler);
    sortPanel = new JPanel();
    flowLayout2 = new FlowLayout();
    sortPanel.setLayout(flowLayout2);
    sortPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.black), "Sort"));
    sortPanel.add(sortLabel);
    sortPanel.add(sortSelectionBox);
    sortPanel.add(sortByLabel);
    sortPanel.add(sortBySelectionBox);
    sortPanel.add(sortButton);
    textArea2 = new JTextArea("The sort results are displayed here.");
    textArea2.setFont(new java.awt.Font("Monospaced", 0, 12));
    textArea2.setEditable(false);
    scrollPane3 = new JScrollPane(textArea2);

    // Create and add file selection functions
    chooseFileButton = new JButton("Choose File");
    chooseFileLabel = new JLabel("Choose file:");
    chooseFileButton.addActionListener(handler);
    inputPanel = new JPanel();
    flowLayout = new FlowLayout();
    inputPanel.setLayout(flowLayout);
    inputPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.black), "Build World"));
    inputPanel.add(chooseFileLabel);
    inputPanel.add(chooseFileButton);
    topPanel = new JPanel();
    boxLayout = new BoxLayout(topPanel, BoxLayout.PAGE_AXIS);
    topPanel.setLayout(boxLayout);
    topPanel.add(inputPanel);
    topPanel.add(sortPanel);
    panel.add(topPanel, BorderLayout.PAGE_START);

    // Create searching functions
    searchTextArea = new JTextArea("Search results are displayed here.");
    searchTextArea.setFont(new java.awt.Font("Monospaced", 0, 12));
    searchTextArea.setEditable(false);
    scrollPane2 = new JScrollPane(searchTextArea);
    textFieldInput = new JTextField();
    inputSearchLabel = new JLabel("Input search term:");
    comboBoxLabel = new JLabel("Choose search type:");
    searchButton = new JButton("Search");
    searchButton.addActionListener(handler);
    selectionBox = new JComboBox<String>(SELECTION_BOX_LIST);
    bottomPanel = new JPanel();
    bottomPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.black), "Search"));
    gridBagLayout = new GridBagLayout();
    bottomPanel.setLayout(gridBagLayout);
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = ZERO;
    c.gridy = ZERO;
    bottomPanel.add(inputSearchLabel, c);
    c.gridx = ZERO;
    c.gridy = ONE;
    bottomPanel.add(comboBoxLabel, c);
    c.gridx = ONE;
    c.gridy = ONE;
    bottomPanel.add(selectionBox, c);
    c.gridx = ONE;
    c.gridy = ZERO;
    c.weightx = ONE;
    bottomPanel.add(textFieldInput, c);
    c.gridx = TWO;
    c.gridy = ONE;
    c.weightx = ZERO;
    bottomPanel.add(searchButton, c);
    panel.add(bottomPanel, BorderLayout.PAGE_END);

    // Create results output area
    textArea = new JTextArea("The internal data structure is displayed here.");
    textArea.setFont(new java.awt.Font("Monospaced", 0, 12));
    textArea.setEditable(false);
    scrollPane = new JScrollPane(textArea);
    tabbedPane = new JTabbedPane();

    tabbedPane.addTab("Text View", scrollPane);
    scrollPane4 = new JScrollPane();
    scrollPane5 = new JScrollPane();
    tabbedPane.addTab("Tree View", scrollPane4);
    tabbedPane.addTab("Search Results", scrollPane2);
    tabbedPane.addTab("Sort Results", scrollPane3);
    tabbedPane.addTab("Jobs", scrollPane5);
    tabbedPane.addTab("Resources In Pools", rightPanel);
    panel.add(tabbedPane, BorderLayout.CENTER);

    // Add panel to frame
    add(panel);
  }

  // Method to make frame visible
  public void display() {
   setVisible(true);
 }

  // Event handler class for button clicks
  private class Handler implements ActionListener {
    private JFileChooser fileChooser;
    private int returnValue;
    private File file;
    private World world;
    private String searchTerm = "";
    private String sortType;
    private String sortResults;
    private String sortByType;
    private String searchType = "";

    @Override
    public void actionPerformed(ActionEvent e) {
      // If the choose file button was pressed
      if (e.getSource() == chooseFileButton) {
        if (world != null) {
          world.stopPriorThreads();
        }
        // Let user choose a file
        fileChooser = new JFileChooser(".");
        returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          textArea.setText("");
          JPanel jobsPanel = new JPanel();
          GridLayout grid = new GridLayout(0, 3);
          jobsPanel.setLayout(grid);
          JPanel workerPanel = new JPanel();
          workerPanel1.add(workerPanel);
          GridLayout grid2 = new GridLayout(0, 5);
          workerPanel.setLayout(grid2);
          world = new World("The World", ZERO, ZERO,
              fileChooser.getSelectedFile(), jobsPanel, workerPanel, rightPanel);
          // Display the internal data structure in the GUI
          textArea.setText(world.toString());
          textArea.setCaretPosition(ZERO);
          tabbedPane.setSelectedIndex(FOUR);
          scrollPane4.getViewport().add(createNodes());
          scrollPane5.setViewportView(jobsPanel);
          JOptionPane.showMessageDialog(null, "The internal data structure "
              + "has been constructed.", "Complete",
          JOptionPane.INFORMATION_MESSAGE);
        }
      }

      // If the sort type was selected
      if (e.getSource() == sortSelectionBox) {
        sortType = (String) sortSelectionBox.getSelectedItem();
        if (sortType.equals("Ships")) {
          sortBySelectionBox.removeAllItems();
          sortBySelectionBox.addItem("Name");
          sortBySelectionBox.addItem("Weight");
          sortBySelectionBox.addItem("Length");
          sortBySelectionBox.addItem("Width");
          sortBySelectionBox.addItem("Draft");
        } else if (sortType.equals("Ques")) {
          sortBySelectionBox.removeAllItems();
          sortBySelectionBox.addItem("Name");
          sortBySelectionBox.addItem("Weight");
          sortBySelectionBox.addItem("Length");
          sortBySelectionBox.addItem("Width");
          sortBySelectionBox.addItem("Draft");
        } else {
          sortBySelectionBox.removeAllItems();
          sortBySelectionBox.addItem("Name");
        }
      }

      // If the sort button is pressed
      if (e.getSource() == sortButton) {
        sortType = (String) sortSelectionBox.getSelectedItem();
        sortByType = (String) sortBySelectionBox.getSelectedItem();
        if (world == null) {
          JOptionPane.showMessageDialog(null, "You must choose a valid file "
              + "before you can sort.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
          sortResults = world.sort(sortType, sortByType);
          textArea2.setText(sortResults);
          textArea.setText(world.toString());
          scrollPane4.getViewport().add(createNodes());
          if ((!searchTerm.equals("")) && (!searchType.equals(""))) {
            searchTextArea.setText(search(searchTerm, searchType));
          }
          tabbedPane.setSelectedIndex(THREE);
          textArea2.setCaretPosition(ZERO);
          JOptionPane.showMessageDialog(null, "The internal data structure "
              + "has been sorted.\nIf you have previous search results in"
              + " the Search Results tab, they have been sorted, too.\n"
              + "Please also see the Sort tab for a sorted list.", "Complete",
          JOptionPane.INFORMATION_MESSAGE);
        }
      }

      // If the search button was pressed
      if (e.getSource() == searchButton) {
        searchTextArea.setText("");
        searchTerm = textFieldInput.getText();
        searchType = (String) selectionBox.getSelectedItem();
        if (world == null) {
          JOptionPane.showMessageDialog(null, "You must choose a valid file "
              + "before you can search.", "Warning",
              JOptionPane.WARNING_MESSAGE);
        } else if (searchTerm.equals("")) {
          JOptionPane.showMessageDialog(null, "Please enter a "
              + "search term.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
          // Display the results of a search by the user
          results = search(searchTerm, searchType);
          if (results.equals("Results: ")){
            JOptionPane.showMessageDialog(null, "No results were "
                + "found.", "Complete",
            JOptionPane.INFORMATION_MESSAGE);
          }
          searchTextArea.setText(results);
          tabbedPane.setSelectedIndex(TWO);
        }
      }
    }

    // Tree building method
    private JTree createNodes() {
      DefaultMutableTreeNode top = new DefaultMutableTreeNode("The World");
      JTree tree = new JTree(top);
      DefaultMutableTreeNode portTotal =
          new DefaultMutableTreeNode("Ports");
      top.add(portTotal);
      for (SeaPort allPorts: world.getPorts()) {
        DefaultMutableTreeNode port =
            new DefaultMutableTreeNode(allPorts.getName());
        portTotal.add(port);
        DefaultMutableTreeNode people =
            new DefaultMutableTreeNode("People");
        port.add(people);
        for (Person person: allPorts.getPeople()) {
          DefaultMutableTreeNode individual =
              new DefaultMutableTreeNode(person.getName());
          people.add(individual);
        }
        DefaultMutableTreeNode docks =
            new DefaultMutableTreeNode("Docks");
        port.add(docks);
        for (Dock dock: allPorts.getDocks()) {
          DefaultMutableTreeNode individualDock =
              new DefaultMutableTreeNode(dock.getName());
          docks.add(individualDock);
          if (dock.getShip() != null) {
            DefaultMutableTreeNode dockedShip =
                new DefaultMutableTreeNode(dock.getShip().getName());
            individualDock.add(dockedShip);
            for (Job job: dock.getShip().getJobs()) {
              DefaultMutableTreeNode dockedShipJob =
                  new DefaultMutableTreeNode(job.getName());
              dockedShip.add(dockedShipJob);
            }
          }
        }
        DefaultMutableTreeNode que =
            new DefaultMutableTreeNode("Que");
        port.add(que);
        for (Ship ship: allPorts.getQue()) {
          DefaultMutableTreeNode quedShip =
              new DefaultMutableTreeNode(ship.getName());
          que.add(quedShip);
          for (Job job: ship.getJobs()) {
            DefaultMutableTreeNode quedShipJob =
                new DefaultMutableTreeNode(job.getName());
            quedShip.add(quedShipJob);
          }
        }
      }
      return tree;
    }


    // Search method
    private String search(String term, String type) {
      switch (type) {
        case "Name":
          return world.getByName(term);
        case "Index":
          return world.getByIndex(term);
        case "Skill":
          return world.getPeopleBySkill(term);
      }
      return "";
    }
  }

  // Main method to build and display the GUI
  public static void main(String[] args) {
    // Constructor to build the GUI
    SeaPortProgram gui = new SeaPortProgram();
    gui.display();
  }
}
