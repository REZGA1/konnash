package com.example.konnash.Model;

public class BusinessCard {
    // جميع الحقول اختيارية — لا يوجد NOT NULL
    private String personalName;
    private String storeName;
    private String phone;
    private String businessDescription;
    private String address;
    private String city;

    public BusinessCard(String personalName, String storeName, String phone,
                        String businessDescription, String address, String city) {

        this.personalName        = personalName;
        this.storeName           = storeName;
        this.phone               = phone;
        this.businessDescription = businessDescription;
        this.address             = address;
        this.city                = city;
    }

    // Getters
    public String getPersonalName()        { return personalName;        }
    public String getStoreName()           { return storeName;           }
    public String getPhone()               { return phone;               }
    public String getBusinessDescription() { return businessDescription; }
    public String getAddress()             { return address;             }
    public String getCity()                { return city;                }

    // Setters
    public void setPersonalName(String personalName)               { this.personalName        = personalName;        }
    public void setStoreName(String storeName)                     { this.storeName           = storeName;           }
    public void setPhone(String phone)                             { this.phone               = phone;               }
    public void setBusinessDescription(String businessDescription) { this.businessDescription = businessDescription; }
    public void setAddress(String address)                         { this.address             = address;             }
    public void setCity(String city)                               { this.city                = city;                }
}

