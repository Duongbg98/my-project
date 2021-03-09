package entity;

public class Task {
    private int ID;
    private int precedence;
    private int time;
    private String priority;
    private double value;
    private int startTime;

    /**
     * @return the iD
     */
    public int getID() {
        return ID;
    }

    /**
     * @param iD
     *            the iD to set
     */
    public void setID(int iD) {
        ID = iD;
    }

    /**
     * @return the precedence
     */
    public int getPrecedence() {
        return precedence;
    }

    /**
     * @param precedence
     *            the precedence to set
     */
    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time
     *            the time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the startTime
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String toString() {
        return "ID: " + ID + " precedence: " + precedence + " Time: " + time + "  Priority: " + priority;
    }

}
