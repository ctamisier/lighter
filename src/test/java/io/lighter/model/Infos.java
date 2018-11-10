package io.lighter.model;

public class Infos extends SuperInfos {

    private String infosValue;

    private SubInfos moreInfos;

    private transient Customer backReference;

    public String getInfosValue() {
        return infosValue;
    }

    public void setInfosValue(String infosValue) {
        this.infosValue = infosValue;
    }

    public Customer getBackReference() {
        return backReference;
    }

    public void setBackReference(Customer backReference) {
        this.backReference = backReference;
    }

    public SubInfos getMoreInfos() {
        return moreInfos;
    }

    public void setMoreInfos(SubInfos moreInfos) {
        this.moreInfos = moreInfos;
    }
}
