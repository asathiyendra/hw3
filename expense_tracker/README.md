# hw3

The homework will be based on this project named "Expense Tracker",where users will be able to add/remove daily transaction. 

## Compile

To compile the code from terminal, use the following command:
```
cd src
javac ExpenseTrackerApp.java
java ExpenseTracker
```

You should be able to view the GUI of the project upon successful compilation. 

## Java Version
This code is compiled with ```openjdk 17.0.7 2023-04-18```. Please update your JDK accordingly if you face any incompatibility issue.

## New Functionality
The Undo Functionality has been added. The user can press the undo button and enter the row (int) they would like removed from the table.
If the row is on the table, it will be removed, and if not, the user will receive an error message stating that the row selected 
does not exist on this table.