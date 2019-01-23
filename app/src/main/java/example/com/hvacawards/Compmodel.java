package example.com.hvacawards;

public class Compmodel {
    private String Compliant,EmailID,Experience,Fullname,MobileNo,UserId,Installation,Service,CompDetails;

    public Compmodel(){

    }

    public Compmodel(String compliant, String emailID, String experience, String fullname, String mobileNo, String userId, String installation, String service, String compDetails) {
        Compliant = compliant;
        EmailID = emailID;
        Experience = experience;
        Fullname = fullname;
        MobileNo = mobileNo;
        UserId = userId;
        Installation = installation;
        Service = service;
        CompDetails = compDetails;
    }

    public String getCompliant() {
        return Compliant;
    }

    public void setCompliant(String compliant) {
        Compliant = compliant;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getInstallation() {
        return Installation;
    }

    public void setInstallation(String installation) {
        Installation = installation;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getCompDetails() {
        return CompDetails;
    }

    public void setCompDetails(String compDetails) {
        CompDetails = compDetails;
    }
}
