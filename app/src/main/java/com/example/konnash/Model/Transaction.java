package com.example.konnash.Model;

public class Transaction {

    public enum Type { INCOME, EXPENSE }

    private long    id;
    private long    archiveId;  // 0 = نشطة | > 0 = تنتمي لأرشيف
    private Type    type;
    private double  amount;
    private String  description;
    private String  imagePath;
    private String  category;
    private long    dateTime;
    private boolean isArchived;

    public Transaction(long id, long archiveId, Type type, double amount,
                       String description, String imagePath, String category,
                       long dateTime, boolean isArchived) {
        this.id          = id;
        this.archiveId   = archiveId;
        this.type        = type;
        this.amount      = amount;
        this.description = description;
        this.imagePath   = imagePath;
        this.category    = category;
        this.dateTime    = dateTime;
        this.isArchived  = isArchived;
    }

    // Getters
    public long    getId()          { return id;          }
    public long    getArchiveId()   { return archiveId;   }
    public Type    getType()        { return type;        }
    public double  getAmount()      { return amount;      }
    public String  getDescription() { return description; }
    public String  getImagePath()   { return imagePath;   }
    public String  getCategory()    { return category;    }
    public long    getDateTime()    { return dateTime;    }
    public boolean isArchived()     { return isArchived;  }

    // Setters
    public void setArchiveId(long archiveId)         { this.archiveId   = archiveId;   }
    public void setType(Type type)                   { this.type        = type;        }
    public void setAmount(double amount)             { this.amount      = amount;      }
    public void setDescription(String description)   { this.description = description; }
    public void setImagePath(String imagePath)       { this.imagePath   = imagePath;   }
    public void setCategory(String category)         { this.category    = category;    }
    public void setDateTime(long dateTime)           { this.dateTime    = dateTime;    }
    public void setArchived(boolean isArchived)      { this.isArchived  = isArchived;  }
}