package entity;

public class PayDetails {
    private ReceiveInfo receiveInfo;
    private User saler;
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getSaler() {
        return saler;
    }

    public void setSaler(User saler) {
        this.saler = saler;
    }

    public ReceiveInfo getReceiveInfo() {
        return receiveInfo;
    }

    public void setReceiveInfo(ReceiveInfo receiveInfo) {
        this.receiveInfo = receiveInfo;
    }
}
