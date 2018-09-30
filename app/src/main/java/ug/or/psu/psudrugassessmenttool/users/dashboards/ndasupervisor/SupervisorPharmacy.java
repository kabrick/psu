package ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor;

public class SupervisorPharmacy {
    private String name;
    private String location;
    private String status_image;
    private String id;

    public SupervisorPharmacy() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatusImage() {
        return status_image;
    }

    public void setStatusImage(String status) {
        this.status_image = status;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
