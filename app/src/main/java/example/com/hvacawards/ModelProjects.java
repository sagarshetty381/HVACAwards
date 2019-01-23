package example.com.hvacawards;

public class ModelProjects {

    private String Name;
    private String CapacityTR;
    private String Address;
    private String Area;
    private String Lattitude;
    private String Longitude;
    private String Status;
    private String PushId;
    private String State;
    private String HVACfields;
    private String City;
    private String Rating;
    private String Userid;
    private String img0,img1,img2,img3,img4,img5,img6,img7,img8,img9;

    public ModelProjects(){

    }

    public ModelProjects(String name, String capacityTR, String address, String area, String lattitude, String longitude, String status, String pushId, String state, String HVACfields, String city, String rating, String userid, String img0, String img1, String img2, String img3, String img4, String img5, String img6, String img7, String img8, String img9) {
        Name = name;
        CapacityTR = capacityTR;
        Address = address;
        Area = area;
        Lattitude = lattitude;
        Longitude = longitude;
        Status = status;
        PushId = pushId;
        State = state;
        this.HVACfields = HVACfields;
        City = city;
        Rating = rating;
        Userid = userid;
        this.img0 = img0;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
        this.img6 = img6;
        this.img7 = img7;
        this.img8 = img8;
        this.img9 = img9;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCapacityTR() {
        return CapacityTR;
    }

    public void setCapacityTR(String capacityTR) {
        CapacityTR = capacityTR;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPushId() {
        return PushId;
    }

    public void setPushId(String pushId) {
        PushId = pushId;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getHVACfields() {
        return HVACfields;
    }

    public void setHVACfields(String HVACfields) {
        this.HVACfields = HVACfields;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    public String getImg6() {
        return img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }

    public String getImg7() {
        return img7;
    }

    public void setImg7(String img7) {
        this.img7 = img7;
    }

    public String getImg8() {
        return img8;
    }

    public void setImg8(String img8) {
        this.img8 = img8;
    }

    public String getImg9() {
        return img9;
    }

    public void setImg9(String img9) {
        this.img9 = img9;
    }
}