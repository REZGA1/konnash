package com.example.konnash.Model;

public class TransactionArchive {

    private long   id;
    private long   openDate;   // تاريخ أول معاملة
    private long   closeDate;  // تاريخ آخر معاملة
    private int    count;      // عدد المعاملات
    private double income;     // مجموع الدخل
    private double expense;    // مجموع المصروف

    public TransactionArchive(long id, long openDate, long closeDate,
                              int count, double income, double expense) {
        this.id        = id;
        this.openDate  = openDate;
        this.closeDate = closeDate;
        this.count     = count;
        this.income    = income;
        this.expense   = expense;
    }

    // Getters
    public long   getId()        { return id;               }
    public long   getOpenDate()  { return openDate;         }
    public long   getCloseDate() { return closeDate;        }
    public int    getCount()     { return count;            }
    public double getIncome()    { return income;           }
    public double getExpense()   { return expense;          }
    public double getBalance()   { return income - expense; }

    // Setters
    public void setOpenDate(long openDate)   { this.openDate  = openDate;  }
    public void setCloseDate(long closeDate) { this.closeDate = closeDate; }
    public void setCount(int count)          { this.count     = count;     }
    public void setIncome(double income)     { this.income    = income;    }
    public void setExpense(double expense)   { this.expense   = expense;   }
}