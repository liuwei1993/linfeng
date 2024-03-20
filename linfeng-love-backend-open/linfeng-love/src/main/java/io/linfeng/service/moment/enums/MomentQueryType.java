package io.linfeng.service.moment.enums;

/**
 * 动态查询方式
 */
public enum MomentQueryType {
    /**
     * 最新
     */
    NEW(3),
    /**
     * 指定嘉宾
     */
    GUEST(4),
    /**
     * 自己
     */
    MY(5);

    private int value;

    MomentQueryType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
