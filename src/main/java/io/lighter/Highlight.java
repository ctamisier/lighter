package io.lighter;

public class Highlight {

    private String hl;
    private String fullPathFieldName;
    private String pathFieldName;
    private Object originalValue;
    private Object value;

    public Highlight(String hl, String fullPathFieldName, String pathFieldName, Object originalValue, Object value) {
        this.hl = hl;
        this.fullPathFieldName = fullPathFieldName;
        this.pathFieldName = pathFieldName;
        this.originalValue = originalValue;
        this.value = value;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String getFullPathFieldName() {
        return fullPathFieldName;
    }

    public void setFullPathFieldName(String fullPathFieldName) {
        this.fullPathFieldName = fullPathFieldName;
    }

    public String getPathFieldName() {
        return pathFieldName;
    }

    public void setPathFieldName(String pathFieldName) {
        this.pathFieldName = pathFieldName;
    }

    public Object getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(Object originalValue) {
        this.originalValue = originalValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
