package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bayesserver.inference.InconsistentEvidenceException;

import entity.Resource;
import entity.Reviewer;
import entity.Task;
import entity.UserStory;
import util.FileManage;

/**
 * @author tuanl
 *
 */
public class SchedulingWithPoker {

    /* biến lưu độ dài sprint */
    private int cap;

    /* Mảng lưu thông tin các task gán cho mỗi resource */
    private List<List<String>> schedule;

    /* Biến trạng thái có lập lịch được hay không */
    private boolean scheduled;

    /* Thông tin các resource */
    private List<Resource> resources;

    /* Thông tin các userstory */
    private List<UserStory> userStories;

    /* Biến tổng số resource */
    private int numResource;

    /* Biến tổng số task */
    private int totalTask;

    /* List lưu các task đã được phân */
    private List<Task> sheduledTask;

    private BayesPoker netPoker;

    public SchedulingWithPoker(List<UserStory> userStories, List<Resource> resources, int cap) {
        netPoker = new BayesPoker();
        this.resources = resources;
        this.cap = cap;
        this.scheduled = true;
        numResource = resources.size();

        sheduledTask = new ArrayList<Task>();
        this.userStories = userStories;

        this.totalTask = caculateNumberTask(userStories);

        /*
         * Khởi tạo mảng lưu các task tương ứng với mỗi team sau khi lập lịch
         */
        schedule = new ArrayList<List<String>>();
        for (int i = 0; i < numResource; i++) {
            schedule.add(new ArrayList<String>());
        }

    }

    /**
     * tính index của task đầu tiên trong userstory
     * 
     * @param indexStory
     * @return
     */
    private int getIndexTaskOfUserStory(int indexStory) {
        int indexTask = 0;
        for (int index = 0; index < indexStory; index++) {
            index += userStories.get(index).getListTask().size();
        }
        return indexTask;
    }

    /**
     * thuật toán lập lịch
     * 
     * @return có lập lịch thành công ko
     * @throws InconsistentEvidenceException
     * @throws IOException
     */
    public boolean runAlgorithm() throws InconsistentEvidenceException, IOException {

        int numUserStory = userStories.size();
        int[] arrStories = new int[numUserStory];

        for (int indexStory = 0; indexStory < numUserStory; indexStory++) {
            UserStory userStory = this.findUserStory(arrStories);

            if (userStory == null) {
                System.out.println("Khong tim duoc userstory phu hop.");
                this.scheduled = false;
                return false;
            }

            int indexTask = getIndexTaskOfUserStory(indexStory);

            List<Task> listTask = userStory.getListTask();
            int numTask = listTask.size();

            // phân bổ các task
            for (int i = 0; i < numTask; i++) {

                // tìm task có giá trị lớn nhất, nếu có nhiều ưu tiên thời gian
                // dài để lập lịch
                Task task = this.findTask(listTask);
                // System.out.println(task);

                // nếu tìm được task thì tiến hành phân bổ
                if (task.getID() != 0) {
                    // chọn tài nguyên phù hợp để phân công
                    Resource resource = this.findResource(task, indexTask + task.getID(), listTask);
                    if (resource != null && (resource.getLoaded() + task.getTime() < this.cap)) {
                        this.sheduledTask.add(task);
                        // phân công task cho resource
                        resource.getListTask().add(task);

                        schedule.get(resource.getID() - 1).add(userStory.getStoryId() + "." + task.getID());
                    } else {
                        System.out.println("không tìm được resource ở lần thứ: " + i);
                        if ((resource.getLoaded() + task.getTime() > this.cap)) {
                            System.out.println("vuot qua sprint");
                        }
                        scheduled = false;
                        return false;
                    }
                } else {
                    System.out.println("không tìm được task ở task thứ: " + i + " cua user story thu " + indexStory);
                    scheduled = false;
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * lấy về lịch sau khi lập
     * 
     * @return
     */
    public int[][] getScheduleWithTimeTask() {
        int[][] schedule = new int[resources.size()][totalTask];
        for (int i = 0; i < resources.size(); i++) {
            ArrayList<Task> listScheduled = resources.get(i).getListTask();
            int numTask = listScheduled.size();
            for (int j = 0; j < totalTask; j++) {
                if (j < numTask) {

                    schedule[i][j] = listScheduled.get(j).getTime();
                } else {
                    schedule[i][j] = 0;
                }
            }
        }

        return schedule;
    }

    /**
     * trả về có lập được lịch hay không
     * 
     * @return có lập được lịch hay không
     */
    public boolean isExistSchedule() {
        return scheduled;
    }

    public List<List<String>> getScheduledWithNameTask() {
        return schedule;
    }

    private int caculateNumberTask(List<UserStory> listUserStory) {
        int sum = 0;
        for (UserStory userStory : listUserStory) {
            sum += userStory.getListTask().size();
        }
        return sum;
    }

    /**
     * Tìm userStory chưa được lập lịch có point cao nhất
     * 
     * @param arrStories:
     *            mảng lưu thông tin các userstory đã được lập lịch
     * @return null nếu đã lập lịch hết, ngược lại trả về userstory có điểm cao
     *         nhất
     */
    private UserStory findUserStory(int[] arrStories) {
        int point = -1;
        int indexMaxPoint = -1;
        int numStories = arrStories.length;
        for (int index = 0; index < numStories; index++) {
            UserStory userStory = userStories.get(index);
            if (arrStories[index] == 0 && userStory.getStoryPoint() > point) {
                point = userStory.getStoryPoint();
                indexMaxPoint = index;
            }
        }
        if (indexMaxPoint == -1) {
            return null;
        } else {
            arrStories[indexMaxPoint] = 1;
            return userStories.get(indexMaxPoint);
        }
    }

    /**
     * tìm Resource thích hợp nhất để phần công task
     * 
     * @return: resource thích hợp
     */
    public Resource findResource() {

        Resource resource = null;
        int minResource = 99999999;

        // tìm resource đã phân bố ít việc nhất
        for (Resource r : resources) {
            if (minResource > r.getLoaded()) {
                minResource = r.getLoaded();
                resource = r;
            }
        }

        return resource;
    }

    /**
     * tìm Resource thích hợp nhất để phần công task
     * 
     * @return: resource thích hợp
     * @throws InconsistentEvidenceException
     * @throws IOException
     */
    private Resource findResource(Task task, int indexTask, List<Task> listTask)
            throws InconsistentEvidenceException, IOException {

        List<Resource> res = new ArrayList<Resource>(resources);

        /*
         * lấy ra tải của các nguồn lực trước khi phân, lấy xong giả sử gán task
         * cho nguồn lực đó, Tính xác suất thực hiện thành công task đó của các
         * resource, rồi chọn ra resource có tải nhỏ nhất nhưng xác suất thực
         * hiện cao nhất
         */
        int loadeds[] = new int[numResource];
        for (int i = 0; i < numResource; i++) {
            loadeds[i] = res.get(i).getLoaded();
            res.get(i).getListTask().add(task);

        }

        // xây dựng mạng bayes rồi
        // tính xác suất thực hiện task đó của từng nguồn lực
        BayesNet net = new BayesNet(res, totalTask);
        net.construct();
        double pro[][] = net.infer();
        double prob[] = new double[numResource];

        for (int i = 0; i < numResource; i++) {
            int size = res.get(i).getListTask().size();
            prob[i] = pro[i][size - 1];
            res.get(i).getListTask().remove(size - 1);
        }

        Task par = findTaskById(task.getPrecedence(), listTask);
        int timeStart = 0;
        if (par != null)
            timeStart = par.getStartTime() + par.getTime();
        int index = this.findMax(prob, loadeds, timeStart, indexTask, task.getPriority());
        return resources.get(index);
    }

    /**
     * Tìm vị trí của nguồn lực có tải nhỏ nhất và xác suất thực hiện với giá
     * trị mang lại cao nhất
     * 
     * @param prob
     * @param loaded
     * @param startTime
     * @return
     */
    private int findMax(double prob[], int loaded[], int startTime, int indexTask, String priority) {
        double max = -1;
        int index = -1;
        int minLoad = findMinLoaded(loaded, startTime);
        for (int i = 0; i < prob.length; i++) {
            if (loaded[i] == minLoad) {
                if (indexTask >= resources.get(i).getTaskReviews().size()) {
                    continue;
                }
                Reviewer reviewer = resources.get(i).getTaskReviews().get(indexTask);

                double value = netPoker.calculateValue(reviewer, priority);
                if (prob[i] * value > max) {

                    max = prob[i] * value;
                    index = i;
                }
            }
        }
        return index;
    }

    private int findMinLoaded(int loaded[], int startTime) {
        int min = 99999999;
        for (int r : loaded) {
            if (r >= startTime && min > r) {
                min = r;
            }
        }
        return min;
    }

    /**
     * chọn task có giá trị lớn nhất, nếu bằng nhau thì ưu tiên thời gian dài và
     * không có điều kiện task ưu tiên hoặc task ưu tiên đã thực hiện xong
     * 
     * @return task thoả mãn
     */
    private Task findTask(List<Task> tasks) {
        Task task = new Task();
        for (Task t : tasks) {
            if (task.getValue() < t.getValue()) {
                if (t.getPrecedence() == 0 && !this.sheduledTask.contains(t)) {
                    task = t;
                } else {
                    // nếu task có task ưu tiên đã thực hiện cũng có tiềm
                    // năng
                    Task parent = this.findTaskById(t.getPrecedence(), tasks);
                    if (this.sheduledTask.contains(parent) && !this.sheduledTask.contains(t)) {
                        task = t;
                    }

                }
            } else if (task.getValue() == t.getValue()) {
                if (t.getTime() > task.getTime()) {
                    // nếu task ko có task ưu tiên
                    if (t.getPrecedence() == 0 && !this.sheduledTask.contains(t)) {
                        task = t;
                    } else {
                        // nếu task có task ưu tiên đã thực hiện cũng có tiềm
                        // năng
                        Task parent = this.findTaskById(t.getPrecedence(), tasks);
                        if (this.sheduledTask.contains(parent) && !this.sheduledTask.contains(t)) {
                            task = t;
                        }

                    }
                }
            }

        }

        return task;
    }

    /**
     * tìm task theo ID
     * 
     * @param id:
     *            id của task cần tìm kiếm
     * @return
     */
    private Task findTaskById(int id, List<Task> tasks) {
        for (Task task : tasks) {
            if (task.getID() == id) {
                return task;
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException, InconsistentEvidenceException {
        List<UserStory> listUserStories = FileManage.readUserStory("data1.csv");
        List<Resource> listResource = FileManage.readResource("resources_poker.csv");
        SchedulingWithPoker algorithmScheduling = new SchedulingWithPoker(listUserStories, listResource, 100);
        if (algorithmScheduling.runAlgorithm()) {
            System.out.println("lap duoc lich");
        } else {
            System.out.println("ko lap lich duoc");
        }

        // List<List<String>> arr =
        // algorithmScheduling.getScheduledWithNameTask();
        // System.out.println(arr.size());
        // int i = 0;
        // for (List<String> a: arr){
        // System.out.println("line " + i++);
        // for(String b: a){
        // System.out.print(b + " ");
        // }
        // System.out.println();
        // }
    }

    /**
     * @return the scheduled
     */
    public boolean isScheduled() {
        return scheduled;
    }

}
