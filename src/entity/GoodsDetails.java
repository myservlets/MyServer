package entity;

import java.util.List;

public class GoodsDetails {
    private Goods choosedGoods;
    private Comment latestComment;
    private User saler;
    private List<Goods> recommendGoods;

    public Goods getChoosedGoods() {
        return choosedGoods;
    }

    public void setChoosedGoods(Goods choosedGoods) {
        this.choosedGoods = choosedGoods;
    }

    public Comment getLatestComment() {
        return latestComment;
    }

    public void setLatestComment(Comment latestComment) {
        this.latestComment = latestComment;
    }

    public User getSaler() {
        return saler;
    }

    public void setSaler(User saler) {
        this.saler = saler;
    }

    public List<Goods> getRecommendGoods() {
        return recommendGoods;
    }

    public void setRecommendGoods(List<Goods> recommendGoods) {
        this.recommendGoods = recommendGoods;
    }
}
