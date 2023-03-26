package tbs.api_server.config;

public enum AccessEnum {
    STAFF(2),RESOURCE_ACCESS(1),ACTIVATED(0);

    int value;

    AccessEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
