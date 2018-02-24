package com.scorpio.myexpensemanager.db.vo;

/**
 * Created by hkundu on 15-02-2017.
 */

public class ConfigParam {
    private Long id;
    private Long configId;
    private String key;
    private String value;

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
