package ug.or.psu.psudrugassessmenttool.models;

public class Pharmacists {
    public String id;
    public String name;
    public String pharmacy;

    public Pharmacists(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }
}
