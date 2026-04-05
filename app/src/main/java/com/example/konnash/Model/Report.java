package com.example.konnash.Model;

import java.util.List;

public class Report {

    private double            totalIncome;
    private double            totalExpense;
    private int               count;
    private List<Transaction> transactions;

    public Report(double totalIncome, double totalExpense,
                  int count, List<Transaction> transactions) {
        this.totalIncome  = totalIncome;
        this.totalExpense = totalExpense;
        this.count        = count;
        this.transactions = transactions;
    }

    // Getters
    public double            getTotalIncome()   { return totalIncome;              }
    public double            getTotalExpense()  { return totalExpense;             }
    public double            getBalance()       { return totalIncome - totalExpense; }
    public int               getCount()         { return count;                    }
    public List<Transaction> getTransactions()  { return transactions;             }
}