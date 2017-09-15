/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Date;
import java.util.Objects;

/**
 * Problem 1.2.13
 * @author Levi
 */
public class Transaction {
    private final String customer;
    private final Date date;
    private final double amount;
    public Transaction(String customer, Date date, double amount)
    {
        this.customer = customer;
        this.date = date;
        this.amount = amount;
    }
    
    public String customer()
    {
        return customer;
    }
    
    public Date date()
    {
        return date;
    }
    
    public double amount()
    {
        return amount;
    }
    
    @Override
    public String toString()
    {
        return "Customer name: " + customer + '\n' + "Date: " + date 
                + '\n' + "Amount: " + amount;
    }
    
    @Override
    public boolean equals(Object x)
    {
        if(this == x) return true;
        if(x == null) return false;
        if(x.getClass() != this.getClass()) return false;
        
        Transaction that = (Transaction) x;
        
        if(!this.customer.equals(that.customer)) return false;
        if(this.amount != that.amount) return false;
        return this.date == that.date;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.customer);
        hash = 29 * hash + Objects.hashCode(this.date);
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
        return hash;
    }
}
