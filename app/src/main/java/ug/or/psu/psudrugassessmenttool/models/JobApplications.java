package ug.or.psu.psudrugassessmenttool.models;

public class JobApplications {
    private String photo, name, email, phone, cover_letter, cv;

    public JobApplications(String photo, String name, String email, String phone, String cover_letter, String cv) {
        this.photo = photo;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cover_letter = cover_letter;
        this.cv = cv;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCoverLetter() {
        return cover_letter;
    }

    public void setCoverLetter(String cover_letter) {
        this.cover_letter = cover_letter;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }
}
