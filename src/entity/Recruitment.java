package entity;

public class Recruitment {


    private String recruId;
    private String recruName;
    private String recruDescribe; //兼职描述
    private double reward; //赏金or工资
    private int type;  //兼职种类
    private int requestNum; //需求的人数
    private String address;  //兼职地点
    private String contactInfo; //联系方式
    private String userId;  //发布人id

    public String getRecruId() {
        return recruId;
    }

    public void setRecruId(String recruId) {
        this.recruId = recruId;
    }

    public String getRecruName() {
        return recruName;
    }

    public void setRecruName(String recruName) {
        this.recruName = recruName;
    }

    public String getRecruDescribe() {
        return recruDescribe;
    }

    public void setRecruDescribe(String recruDescribe) {
        this.recruDescribe = recruDescribe;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(int requestNum) {
        this.requestNum = requestNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
