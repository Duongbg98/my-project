package entity;

/**
 * @author tuanl
 *
 */
public class Reviewer {
    private String experience, time;
    private String effort;

    /**
     * @return the experience
     */
    public String getExperience() {
        return experience;
    }

    /**
     * @param experience
     *            the experience to set
     */
    public void setExperience(String experience) {
        this.experience = experience;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     *            the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the effort
     */
    public String getEffort() {
        return effort;
    }

    /**
     * @param effort
     *            the effort to set
     */
    public void setEffort(String effort) {
        this.effort = effort;
    }
    
    public String toString(){
        return experience + ", " + time + ", " + effort;
    }

}
