package entity;

/**
 * Created by Administrator on 2019/3/18 0018.
 */
public class ReceiveInfo {

    private String userId;

    private String phone;

    private String address;

    public void setAddress(String address) { this.address = address; }

    public String getAddress() { return address; }

    public void setUserId(String username) {this.userId = username;}

    public String getUserId() {return userId;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getPhone() {return phone;}
}
