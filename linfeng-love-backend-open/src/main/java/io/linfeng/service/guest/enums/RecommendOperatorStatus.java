package io.linfeng.service.guest.enums;

/**
 * 推荐操作状态
 */
public enum RecommendOperatorStatus {
    /**
     * 初始状态
     */
    DEFAULT(0),
    /**
     * 操作完成
     */
    DONE(1);

    private int value;

    RecommendOperatorStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
