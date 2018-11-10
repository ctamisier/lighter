package io.lighter.model;

import java.util.List;
import java.util.Set;

public class Customer extends Person {

    private Infos infos;

    private Set<Address> addresses;

    private String aNullValue;

    private List<String> emails;

    public Infos getInfos() {
        return infos;
    }

    public void setInfos(Infos infos) {
        this.infos = infos;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public String getaNullValue() {
        return aNullValue;
    }

    public void setaNullValue(String aNullValue) {
        this.aNullValue = aNullValue;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
