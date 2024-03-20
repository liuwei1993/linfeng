package io.linfeng.common.enums;

/**
 * 通用数量操作状态
 */
public enum CountOperatorType {
    /**
     * 减少
     */
    REDUCE(-1),
    /**
     * 增加
     */
    ADD(1);

    private int value;

    CountOperatorType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
