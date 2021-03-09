package entity;

import java.util.ArrayList;
import java.util.List;

public class UserStory {
    private String storyId;
    private int storyPoint;
    private List<Task> listTask;
    private double valueExpect;

    public UserStory() {
        listTask = new ArrayList<Task>();
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public int getStoryPoint() {
        return storyPoint;
    }
    public void setStoryPoint(int storyPoint) {
        this.storyPoint = storyPoint;
    }

    public List<Task> getListTask() {
        return listTask;
    }

    public void setListTask(List<Task> listTask) {
        this.listTask = listTask;
    }
    
    /**
     * @return the valueExpect
     */
    public double getValueExpect() {
        return valueExpect;
    }

    /**
     * @param valueExpect the valueExpect to set
     */
    public void setValueExpect(double valueExpect) {
        this.valueExpect = valueExpect;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(storyId);
        sb.append(",  ");
        sb.append(storyPoint);
        sb.append(",  ");
        sb.append(listTask.size());
        sb.append("\n");
        for (Task task: listTask){
            sb.append(task.getID());
            sb.append(",  ");
            sb.append(task.getPrecedence());
            sb.append(",  ");
            sb.append(task.getTime());
            sb.append(",  ");
            sb.append(task.getPriority());
            sb.append("\n");
        }
        return sb.toString();
    }
}
