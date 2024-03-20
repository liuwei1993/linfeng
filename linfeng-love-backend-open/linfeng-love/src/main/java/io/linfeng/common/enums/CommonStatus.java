package io.linfeng.common.enums;

/**
 * 通用审核状态
 */
public enum CommonStatus {
    /**
     * 审核未通过
     */
    UN_CHECKED(-1),
    /**
     * 初始态
     */
    DEFAULT(0),
    /**
     * 审核中
     */
    CHECKING(1),
    /**
     * 审核通过
     */
    CHECKED(2);

    private int value;

    CommonStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}