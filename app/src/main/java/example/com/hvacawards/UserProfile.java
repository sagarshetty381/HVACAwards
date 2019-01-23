package example.com.hvacawards;

public class UserProfile {

    private String Address,EmailId,FullName,MobileNo,PromoCode,ReferencePromo;

    public UserProfile() {

    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String promoCode) {
        PromoCode = promoCode;
    }

    public String getReferencePromo() {
        return ReferencePromo;
    }

    public void setReferencePromo(String referencePromo) {
        ReferencePromo = referencePromo;
    }

    public UserProfile(String address, String emailId, String fullName, String mobileNo, String promoCode, String referencePromo) {

        Address = address;
        EmailId = emailId;
        FullName = fullName;
        MobileNo = mobileNo;
        PromoCode = promoCode;
        ReferencePromo = referencePromo;
    }
}
