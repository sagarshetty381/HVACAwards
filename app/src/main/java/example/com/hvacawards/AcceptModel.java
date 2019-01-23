package example.com.hvacawards;

public class AcceptModel {
    private String Status;

    public AcceptModel(){

    }

    public AcceptModel(String status) {
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
