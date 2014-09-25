package me.nolanfoster.mm.client;

import java.util.ArrayList;
import java.util.Date;

import me.nolanfoster.mm.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tasks implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	  private static final int REFRESH_INTERVAL = 5000; // ms
	  private VerticalPanel mainPanel = new VerticalPanel();
	  private FlexTable tasksFlexTable = new FlexTable();
	  private HorizontalPanel addPanel = new HorizontalPanel();
	  private TextBox newSymbolTextBox = new TextBox();
	  private Button addTaskButton = new Button("Add");
	  private Label lastUpdatedLabel = new Label();
	  private ArrayList<String> tasks = new ArrayList<String>();  
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		  // Create table for task data.
	    tasksFlexTable.setText(0, 0, "Task");
	    tasksFlexTable.setText(0, 1, "Date Assigned");
	    tasksFlexTable.setText(0, 2, "Description");
	    tasksFlexTable.setText(0, 3, "Remove");
		//styling for table
	    tasksFlexTable.setCellPadding(6);

	    // Assemble Add task panel.
	    addPanel.add(newSymbolTextBox);
	    addPanel.add(addTaskButton);

	    // Assemble Main panel.
	    mainPanel.add(tasksFlexTable);
	    mainPanel.add(addPanel);
	    mainPanel.add(lastUpdatedLabel);

	    // Associate the Main panel with the HTML host page.
	    RootPanel.get("taskList").add(mainPanel);

	    // Move cursor focus to the input box.
	    newSymbolTextBox.setFocus(true);
		
	    // Setup timer to refresh list automatically.
	    Timer refreshTimer = new Timer() {
	      @Override
	      public void run() {
	        refreshWatchList();
	      }
	    };
	    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
		
	    // Listen for mouse events on the Add button.
	    addTaskButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        addTask();
	      }
	    });
	    
	    // Listen for keyboard events in the input box.
	    newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {
	      public void onKeyDown(KeyDownEvent event) {
	        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	          addTask();
	        }
	      }
	    });

	  }

	  /**
	   * Add task to FlexTable. Executed when the user clicks the addTaskButton or
	   * presses enter in the newSymbolTextBox.
	   */
	  private void addTask() {
		    final String task = newSymbolTextBox.getText().toUpperCase().trim();
		    newSymbolTextBox.setFocus(true);

		    // Task code must be between 1 and 10 chars that are numbers, letters, or dots.
		    if (!task.matches("^[0-9A-Z\\.]{1,10}$")) {
		      Window.alert("'" + task + "' is not a valid Task.");
		      newSymbolTextBox.selectAll();
		      return;
		    }

		    newSymbolTextBox.setText("");

		    // Don't add the task if it's already in the table.
		    if (tasks.contains(task))
		      return;

		    // Add the Task to the table.
		    int row = tasksFlexTable.getRowCount();
		    tasks.add(task);
		    tasksFlexTable.setText(row, 0, task);

		    // Add a button to remove this stask from the table.
		    Button removeTaskButton = new Button("x");
		    removeTaskButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        int removedIndex = tasks.indexOf(task);
		        tasks.remove(removedIndex);        tasksFlexTable.removeRow(removedIndex + 1);
		      }
		    });
		    tasksFlexTable.setWidget(row, 3, removeTaskButton);

		    // Get the task price.
		    refreshWatchList();
	  }
	  
	 
		  private void refreshWatchList() {
			  
			    
			    TaskDate[] prices = new TaskDate[tasks.size()];
			    for (int i = 0; i < tasks.size(); i++) {
			    	Date d = new Date(Math.abs(System.currentTimeMillis() - Random.nextInt()));
			      String price = d.toString();
			      String change = "Do something";
			         

			      prices[i] = new TaskDate(tasks.get(i), price, change);
			    }

			    updateTable(prices);
			  }

		private void updateTable(TaskDate[] prices) {
			 for (int i = 0; i < prices.length; i++) {
			      updateTable(prices[i]);
			
		}

		  
}

		
/**
 * Update a single row in the stock table.
 *
 * @param price Stock data for a single row.
 */
private void updateTable(TaskDate price) {
  // Make sure the stock is still in the stock table.
  if (!tasks.contains(price.getSymbol())) {
    return;
  }

  int row = tasks.indexOf(price.getSymbol()) + 1;

  // Format the data in the Price and Change fields.
  String priceText = price.getPrice();
  
  String changeText = price.getChange();


  // Populate the Price and Change fields with new data.
  tasksFlexTable.setText(row, 1, priceText);
  tasksFlexTable.setText(row, 2, changeText);
}
}