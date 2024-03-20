package io.linfeng.common.enums;

/**
 * 附件类型
 */
public enum MediaType {
    /**
     * 图片
     */
    IMAGE(1);

    private int value;

    MediaType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
