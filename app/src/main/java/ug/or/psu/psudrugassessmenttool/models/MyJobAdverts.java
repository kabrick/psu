package ug.or.psu.psudrugassessmenttool.models;

public class MyJobAdverts {
    private String title;
    private String company_name;
    private String deadline;
    private String id;

    public MyJobAdverts(String title, String company_name, String deadline, String id) {
        this.title = title;
        this.company_name = company_name;
        this.deadline = deadline;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return company_name;
    }

    public void setCompanyName(String company_name) {
        this.company_name = company_name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
