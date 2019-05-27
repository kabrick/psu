package ug.or.psu.psudrugassessmenttool.models;

public class PharmacistAssessmentFeed {
    private String id;
    private String pharmacy_name;
    private String from_period;
    private String to_period;
    private String score;
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPharmacyName() {
        return pharmacy_name;
    }

    public void setPharmacyName(String pharmacy_name) {
        this.pharmacy_name = pharmacy_name;
    }

    public String getFromPeriod() {
        return from_period;
    }

    public void setFromPeriod(String from_period) {
        this.from_period = from_period;
    }

    public String getToPeriod() {
        return to_period;
    }

    public void setToPeriod(String to_period) {
        this.to_period = to_period;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
