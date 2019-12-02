package uk.ac.rgu.rectv;
import java.util.Date;

public class ShowName {
    private int uid;
    private String reference;
    private int scqfCredits;
    private Date createdOn;
    private boolean registered;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getScqfCredits() {
        return scqfCredits;
    }

    public void setScqfCredits(int scqfCredits) {
        this.scqfCredits = scqfCredits;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "Module{" +
                "uid=" + uid +
                ", reference='" + reference + '\'' +
                ", SCQF credits=" + scqfCredits +
                ", createdOn=" + createdOn +
                ", registered=" + registered +
                '}';
    }
}