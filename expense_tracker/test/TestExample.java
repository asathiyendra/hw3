// package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.List;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.AmountFilter;
import model.Filter.CategoryFilter;
import view.ExpenseTrackerView;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

public class TestExample {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private ExpenseTrackerController controller;

  @Before
  public void setup() {
    model = new ExpenseTrackerModel();
    view = new ExpenseTrackerView();
    controller = new ExpenseTrackerController(model, view);
  }

    public double getTotalCost() {
        double totalCost = 0.0;
        List<Transaction> allTransactions = model.getTransactions(); // Using the model's getTransactions method
        for (Transaction transaction : allTransactions) {
            totalCost += transaction.getAmount();
        }
        return totalCost;
    }


    public void checkTransaction(double amount, String category, Transaction transaction) {
    	assertEquals(amount, transaction.getAmount(), 0.01);
        assertEquals(category, transaction.getCategory());
        String transactionDateString = transaction.getTimestamp();
        Date transactionDate = null;
        try {
            transactionDate = Transaction.dateFormatter.parse(transactionDateString);
        }
        catch (ParseException pe) {
            pe.printStackTrace();
            transactionDate = null;
        }
        Date nowDate = new Date();
        assertNotNull(transactionDate);
        assertNotNull(nowDate);
        // They may differ by 60 ms
        assertTrue(nowDate.getTime() - transactionDate.getTime() < 60000);
    }


    @Test
    public void testAddTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add a transaction
		double amount = 50.0;
		String category = "food";
        assertTrue(controller.addTransaction(amount, category));
    
        // Post-condition: List of transactions contains only
        //                 the added transaction	
        assertEquals(1, model.getTransactions().size());
    
        // Check the contents of the list
        Transaction firstTransaction = model.getTransactions().get(0);
        checkTransaction(amount, category, firstTransaction);
	
        // Check the total amount
        assertEquals(amount, getTotalCost(), 0.01);
    }


    @Test
    public void testRemoveTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add and remove a transaction
        double amount = 50.0;
        String category = "food";
        Transaction addedTransaction = new Transaction(amount, category);
        model.addTransaction(addedTransaction);
    
        // Pre-condition: List of transactions contains only
        //                the added transaction
        assertEquals(1, model.getTransactions().size());
        Transaction firstTransaction = model.getTransactions().get(0);
        checkTransaction(amount, category, firstTransaction);

        assertEquals(amount, getTotalCost(), 0.01);
	
        // Perform the action: Remove the transaction
        model.removeTransaction(addedTransaction);
    
        // Post-condition: List of transactions is empty
        List<Transaction> transactions = model.getTransactions();
        assertEquals(0, transactions.size());
    
        // Check the total cost after removing the transaction
        double totalCost = getTotalCost();
        assertEquals(0.00, totalCost, 0.01);
    }
    
    @Test
    public void addTransactionTest() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add a transaction
	    double amount = 50.0;
	    String category = "food";
        assertTrue(controller.addTransaction(amount, category));
    
        // Post-condition: List of transactions contains only
	    //                 the added transaction	
        assertEquals(1, model.getTransactions().size());
    
        // Check the contents of the list
	    Transaction firstTransaction = model.getTransactions().get(0);
	    checkTransaction(amount, category, firstTransaction);
	
	    // Check the total amount
        assertEquals(amount, getTotalCost(), 0.01);
    }

    @Test
    public void invalidInputTest() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Try to add transaction with invalid amount
	    double amount = -50.0;
	    String category = "food";
        assertFalse(controller.addTransaction(amount, category));
    
        // Try to add transaction with invalid category
	    amount = 50.0;
	    category = "invalid";        
        assertFalse(controller.addTransaction(amount, category));

        //try to add transaction with invalid amount and category
        amount = -50.0;
        category = "invalid";
        assertFalse(controller.addTransaction(amount, category));

        // Post-condition: List of transactions contains nothing          	
        assertEquals(0, model.getTransactions().size());
    
	
	    // Check the total amount
        assertEquals(0, getTotalCost(), 0.01);

        // Try to add a valid transaction
	    amount = 50.0;
	    category = "food";
        assertTrue(controller.addTransaction(amount, category));

        // Post-condition: List of transactions contains nothing          	
        assertEquals(1, model.getTransactions().size());
    }
    @Test
    public void filterByAmountTest(){
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Add transactions
        controller.addTransaction(50, "food");
        controller.addTransaction(50, "travel");
        controller.addTransaction(50, "other");            
        controller.addTransaction(7, "food");
        controller.addTransaction(2, "travel");
        controller.addTransaction(5, "other"); 

        // Post-condition: List of transactions contains 
        //                 the added transaction	
        assertEquals(6, model.getTransactions().size());

        //set the amountFilter
        AmountFilter amountFilter = new AmountFilter(50.0);
        controller.setFilter(amountFilter);
        
        //apply the filter
        controller.applyFilter();

        // Post-condition: First 3 transactions contains amount 50.0
       for (int row = 0; row < 3; row++) {
            Object value = view.getTableModel().getValueAt(row, 1);
            assertEquals(50.0, value);
       }
    }

    @Test
    public void filterByCategoryTest(){
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Add transactions
        controller.addTransaction(5, "food");
        controller.addTransaction(40, "food");
        controller.addTransaction(58, "food");            
        controller.addTransaction(7, "food");
        controller.addTransaction(2, "travel");
        controller.addTransaction(5, "other"); 

        // Post-condition: List of transactions contains 
        //                 the added transaction	
        assertEquals(6, model.getTransactions().size());

        //set the categoryFilter
        CategoryFilter categoryFilter = new CategoryFilter("food");
        controller.setFilter(categoryFilter);
        
        //apply the filter
        controller.applyFilter();

        // Post-condition: First 4 transactions contains amount 50.0
       for (int row = 0; row < 3; row++) {
            Object value = view.getTableModel().getValueAt(row, 2);
            assertEquals("food", value);
       }
    }
    
    @Test
    public void undoDisallowedTest() {
        // Pre-condition: List of transactions is empty
    	List<Transaction> transactions = model.getTransactions();
        assertEquals(0, model.getTransactions().size());
        // Testing removing transactions when empty
        // False means nothing was removed due to not valid input value
        view.addUndoListener(e -> {
        	// negative row is invalid, does not remove
            assertFalse(controller.undoTransaction(-1));
            // first element (row one) when empty is invalid to remove
            assertFalse(controller.undoTransaction(0));
            // second element (row two) when empty is invalid to remove
            assertFalse(controller.undoTransaction(1));
        });
        // pressing undo button
        view.getUndoBtn().doClick();       
        // Post-condition: List of transactions remain the same (empty)
        assertEquals(transactions, model.getTransactions());
    }
    
    @Test
    public void undoAllowedTest() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
        // Adding Transaction
        double addingCost = 123.0;
        String addingCategory = "Food";
        assertTrue(controller.addTransaction(addingCost, addingCategory));
        // Checking if Transaction is in List
        assertEquals(1, model.getTransactions().size());
        // Finding Total Cost of Transactions
        double totalCost = getTotalCost();
        // Testing that removing transaction works correctly
        // undoTransaction returns true if successfully removes transaction
        view.addUndoListener(e -> {
            assertTrue(controller.undoTransaction(0));
        });
        // pressing undo button
        view.getUndoBtn().doClick();
        // Finding Total Cost After Undoing a Row
        double newTotalCost = getTotalCost();
        // Post-condition: Total cost is different after undoing
        assertEquals(totalCost-addingCost, newTotalCost, 0.01);
        // Table is back to empty after its one element is removed
        view.refreshTable(model.getTransactions());
        assertEquals(0, model.getTransactions().size());
    }
}
