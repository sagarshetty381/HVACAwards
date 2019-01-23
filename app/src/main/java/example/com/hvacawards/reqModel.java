package example.com.hvacawards;

public class reqModel {
    private String Userid;

    public reqModel(){

    }

    public reqModel(String userid) {
        Userid = userid;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }
}
