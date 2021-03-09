package util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.bayesserver.inference.InconsistentEvidenceException;

import au.com.bytecode.opencsv.CSVReader;
import entity.Resource;
import entity.Reviewer;
import entity.Task;
import entity.UserStory;
import gui.schedule.FrameAgile;
import model.BayesNet;
import model.BayesPoker;
import model.SchedulingWithPoker;

public class FileManage {

    public static final String COLUMN_SEPARATOR = ";";
    public static final String COMMA = ",";

    /**
     * đọc dữ liệu các team từ file csv
     * 
     * @param teamFile:
     *            file lưu dữ liệu
     * @return: danh sách các team đọc từ file csv
     * @throws IOException
     */
    public static List<Resource> readTeams(String teamFile) throws IOException {
        ArrayList<Resource> resources = new ArrayList<Resource>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(teamFile));
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length == 1) {
                    line = line[0].split(FileManage.COLUMN_SEPARATOR);
                }
                // System.out.println("length " + line.length);
                if (line.length <= 1) {
                    continue;
                }

                Resource resource = new Resource();
                resource.setID(Integer.parseInt(line[0]));
                resource.setAgileExperience(line[1]);
                resource.setAgileSkill(Integer.parseInt(line[2]));
                resource.setSkillLevel(Integer.parseInt(line[3]));
                resource.setDailyMeeting(line[4]);
                resources.add(resource);
            }
        } finally {
            reader.close();
        }
        return resources;
    }

    /**
     * đọc dữ liệu các task từ file csv
     * 
     * @param taskFile:
     *            file lưu trữ thông tin các task
     * @return danh sách các task đọc từ file csv
     * @throws IOException
     */
    public static List<Task> readTask(String taskFile) throws IOException {
        CSVReader reader = null;
        List<Task> tasks = new ArrayList<Task>();
        try {
            reader = new CSVReader(new FileReader(taskFile));
            String[] line;
            line = reader.readNext();
            while (line != null) {
                if (line[0].contains(";")) {
                    line = line[0].split(";");
                }
                Task task = new Task();
                task.setID(Integer.parseInt(line[0]));
                task.setPrecedence(Integer.parseInt(line[1]));
                task.setTime(Integer.parseInt(line[2]));
                tasks.add(task);
                line = reader.readNext();
            }
        } finally {
            if (reader != null)
                reader.close();
        }
        return tasks;
    }

    /**
     * lưu dữ liệu sau lập lịch ra file csv
     * 
     * @param fileName:
     *            tên file lưu dữ liệu
     * @param taskId:
     *            mảng các task giao cho các resource
     * @param probability:
     *            mảng các xác suất thực hiện
     * @throws IOException
     * @throws InconsistentEvidenceException
     */
    public static void writeCSVFile(String fileName, List<List<String>> scheduleNameTask, double[][] probability)
            throws IOException, InconsistentEvidenceException {
        Writer writer = null;
        try {
            writer = new FileWriter(fileName);

            for (int i = 0; i < scheduleNameTask.size(); i++) {
                List<String> tasks = scheduleNameTask.get(i);
                StringBuilder task = new StringBuilder("Resource " + (i + 1) + FileManage.COMMA);
                StringBuilder prob = new StringBuilder(FileManage.COMMA);

                for (int j = 0; j < tasks.size(); j++) {

                    task.append("Task " + tasks.get(j) + FileManage.COMMA);
                    prob.append(probability[i][j] * 100 + "%" + FileManage.COMMA);

                }
                task.append("\n");
                prob.append("\n");
                writer.append(task);
                writer.append(prob);
                writer.append("\n");
            }
            writer.append("\n");
            writer.append("\n");
            writer.append("Total" + FileManage.COMMA + (FrameAgile.caculateProbTotal(probability) * 100) + "%");

        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    public static List<UserStory> readUserStory(String path) throws IOException {
        List<UserStory> listStory = new ArrayList<UserStory>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(path));
            String[] line;
            line = reader.readNext();

            /*
             * đọc dòng đầu tiên ra thông tin userstory(id, point, number task)
             * sau đó đọc <number task> dòng thông tin của từng task đối với
             * userstory đó
             */
            while (line != null) {

                if (line.length <= 1) {
                    line = reader.readNext();
                    continue;
                }
                UserStory userStory = new UserStory();
                userStory.setStoryId("U" + line[0]);
                userStory.setStoryPoint(Integer.parseInt(line[1]));

                int numTask = Integer.parseInt(line[2]);
                List<Task> listTask = new ArrayList<Task>();
                for (int i = 0; i < numTask; i++) {
                    String[] lineTask = reader.readNext();
                    if (lineTask == null) {
                        break;
                    }

                    if (lineTask[0].contains(FileManage.COLUMN_SEPARATOR)) {
                        lineTask = lineTask[0].split(FileManage.COLUMN_SEPARATOR);
                    }

                    Task task = new Task();
                    task.setID(Integer.parseInt(lineTask[0]));
                    task.setPrecedence(Integer.parseInt(lineTask[1]));
                    task.setTime(Integer.parseInt(lineTask[2]));
                    task.setPriority(lineTask[3]);
                    listTask.add(task);
                }
                userStory.setListTask(listTask);
                listStory.add(userStory);

                line = reader.readNext();
            }
        } finally {
            if (reader != null)
                reader.close();
        }
        return listStory;
    }

    /**
     * đọc dữ liệu các team từ file csv sử dụng với poker
     * 
     * @param teamFile:
     *            file lưu dữ liệu
     * @return: danh sách các team đọc từ file csv
     * @throws IOException
     */
    public static List<Resource> readResource(String teamFile) throws IOException {
        ArrayList<Resource> resources = new ArrayList<Resource>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(teamFile));
            String[] line;
            while ((line = reader.readNext()) != null) {

                if (line.length == 1) {
                    line = line[0].split(FileManage.COLUMN_SEPARATOR);
                }

                if (line.length <= 1) {
                    continue;
                }

                Resource resource = new Resource();
                resource.setID(Integer.parseInt(line[0]));
                resource.setAgileExperience(line[1]);
                resource.setAgileSkill(Integer.parseInt(line[2]));
                resource.setSkillLevel(Integer.parseInt(line[3]));
                resource.setDailyMeeting(line[4]);

                // // đọc các review
                // int numberTask = Integer.parseInt(line[5]);
                // List<Reviewer> listReviewer = new ArrayList<Reviewer>();
                // for (int i = 0; i < numberTask; i++){
                // line = reader.readNext();
                // if (line.length == 1){
                // line = line[0].split(FileManage.COLUMN_SEPARATOR);
                // }
                // Reviewer reviewer = new Reviewer();
                // reviewer.setExperience(line[0]);
                // reviewer.setTime(line[1]);
                // reviewer.setEffort(line[2]);
                //
                // listReviewer.add(reviewer);
                // }
                // resource.setTaskReviews(listReviewer);
                resources.add(resource);
            }
        } finally {
            reader.close();
        }
        return resources;
    }

    public static List<List<Reviewer>> readReviewer(String pathFile) throws IOException {
        System.out.println(pathFile);
        List<List<Reviewer>> lists = new ArrayList<List<Reviewer>>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(pathFile));
            String[] line;
            while ((line = reader.readNext()) != null) {

                if (line[0].length() == 0) {
                    continue;
                }

                // đọc các review
                int numberTask = Integer.parseInt(line[5]);
                List<Reviewer> listReviewer = new ArrayList<Reviewer>();
                for (int i = 0; i < numberTask; i++) {
                    line = reader.readNext();
                    if (line.length == 1) {
                        line = line[0].split(FileManage.COLUMN_SEPARATOR);
                    }
                    Reviewer reviewer = new Reviewer();
                    reviewer.setExperience(line[0]);
                    reviewer.setTime(line[1]);
                    reviewer.setEffort(line[2]);

                    listReviewer.add(reviewer);
                }
                lists.add(listReviewer);

            }
        } finally {
            reader.close();
        }
        return lists;
    }

    public static void main(String[] args) throws IOException, InconsistentEvidenceException {
        List<Resource> resources = FileManage.readResource("C:\\Users\\tuanl\\Desktop\\data\\new\\resource.csv");
        String[] tasks = { "C:\\Users\\tuanl\\Desktop\\data\\new\\sprint1.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\sprint2.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\sprint3.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\sprint4.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\sprint5.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\sprint6.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\sprint7.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\sprint8.csv" };
        String[] reviews = { "C:\\Users\\tuanl\\Desktop\\data\\new\\reviews\\review_sprint1.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\reviews\\review_sprint2.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\reviews\\review_sprint3.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\reviews\\review_sprint4.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\reviews\\review_sprint5.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\reviews\\review_sprint6.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\reviews\\review_sprint7.csv",
                "C:\\Users\\tuanl\\Desktop\\data\\new\\reviews\\review_sprint8.csv" };

        for (int k = 0; k < 8; k++) {
            List<UserStory> userStories = FileManage.readStory(tasks[k]);
            List<List<Reviewer>> ls = FileManage.readReviewer(reviews[k]);
            for (int i = 0; i < resources.size(); i++) {
                resources.get(i).setTaskReviews(ls.get(i));
            }
            
            int indexTask = 0;
            BayesPoker poker = new BayesPoker();
            for (UserStory u: userStories){
                for(Task t: u.getListTask()){
                    double value = poker.estimateValue(resources, t.getPriority(), indexTask);
                    t.setValue(value);
                }
            }
            

            // thực hiện lập lịch
            SchedulingWithPoker algorithm = new SchedulingWithPoker(userStories, resources,
                    80);
            algorithm.runAlgorithm();

            // nếu lập được lịch thì hiển thị kết quả
            if (algorithm.isExistSchedule()) {

                int numTask = 0;
                for (UserStory u : userStories) {
                    numTask += u.getListTask().size();
                }

                BayesNet net = new BayesNet(resources, numTask);
                // frame.dispose();

                new FrameAgile(net, algorithm.getScheduleWithTimeTask(),
                        algorithm.getScheduledWithNameTask(), "poker");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Không thể lập lịch với bộ dữ liệu đã chọn. Hãy chọn bộ dữ liệu khác.");
            }
            
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * read userstory from file
     * 
     * @param location
     * @return
     * @throws IOException
     */
    public static List<UserStory> readStory(String location) throws IOException {
        List<UserStory> listStory = new ArrayList<UserStory>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(location));
            String[] line;
            line = reader.readNext();

            /*
             * đọc dòng đầu tiên ra thông tin userstory(id, point, number task)
             * sau đó đọc <number task> dòng thông tin của từng task đối với
             * userstory đó
             */
            while (line != null) {

                if (line[0].length() <= 0) {
                    line = reader.readNext();
                    continue;
                }

                UserStory userStory = new UserStory();
                userStory.setStoryId("U" + line[0]);
                userStory.setStoryPoint(Integer.parseInt(line[1]));
                // System.out.println(userStory);
                int numTask = Integer.parseInt(line[2]);
                List<Task> listTask = new ArrayList<Task>();
                for (int i = 0; i < numTask; i++) {
                    String[] lineTask = reader.readNext();
                    // for(String s: line){
                    // System.out.print(s +',');
                    // }
                    // System.out.println();
                    Task task = new Task();
                    task.setID(Integer.parseInt(lineTask[0]));
                    task.setPrecedence(Integer.parseInt(lineTask[1]));
                    task.setTime(Integer.parseInt(lineTask[2]));
                    task.setPriority(lineTask[3]);
                    listTask.add(task);

                }
                userStory.setListTask(listTask);
                listStory.add(userStory);
                line = reader.readNext();
            }
        } finally {
            if (reader != null)
                reader.close();
        }
        return listStory;
    }
}
