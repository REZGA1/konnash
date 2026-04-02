package com.example.konnash.Model;



public class UserProfile {

    private int    id;
    private String storeName;
    private String phone;
    private String countryCode;
    private String activityType;
    private String sector;
    private double initialBalance;
    private String openDate;

    public UserProfile(int id, String storeName, String phone, String countryCode,
                       String activityType, String sector,
                       double initialBalance, String openDate) {
        this.id             = id;
        this.storeName      = storeName;
        this.phone          = phone;
        this.countryCode    = countryCode;
        this.activityType   = activityType;
        this.sector         = sector;
        this.initialBalance = initialBalance;
        this.openDate       = openDate;
    }

    public int    getId()             { return id;             }
    public String getStoreName()      { return storeName;      }
    public String getPhone()          { return phone;          }
    public String getCountryCode()    { return countryCode;    }
    public String getActivityType()   { return activityType;   }
    public String getSector()         { return sector;         }
    public double getInitialBalance() { return initialBalance; }
    public String getOpenDate()       { return openDate;       }

    public void setId(int id)                        { this.id             = id;             }
    public void setStoreName(String storeName)        { this.storeName      = storeName;      }
    public void setPhone(String phone)                { this.phone          = phone;          }
    public void setCountryCode(String countryCode)    { this.countryCode    = countryCode;    }
    public void setActivityType(String activityType)  { this.activityType   = activityType;   }
    public void setSector(String sector)              { this.sector         = sector;         }
    public void setInitialBalance(double balance)     { this.initialBalance = balance;        }
    public void setOpenDate(String openDate)          { this.openDate       = openDate;       }
}