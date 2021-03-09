package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import com.bayesserver.inference.InconsistentEvidenceException;

import entity.Resource;
import entity.Reviewer;
import entity.Task;
import entity.UserStory;
import gui.schedule.FrameAgile;
import model.AlgorithmScheduling;
import model.BayesNet;
import model.BayesPoker;
import model.SchedulingWithPoker;
import util.FileManage;

public class MainView {

    private JFrame frame;
    private JTextField txtNameProject;
    private File teamSelectedFile = null;
    private File taskSelectedFile = null;
    private File taskReviewFile = null;
    private JLabel labelTeamFile;
    private JLabel labelTaskFile;
    private JLabel labelReviewFile;
    // private String projectName;
    // private String iterID;
    private JTextField txtLength;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainView window = new MainView();
                    // window.initialize();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainView() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblProjectName = new JLabel("Project Name ");
        lblProjectName.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblProjectName.setBounds(49, 35, 95, 39);
        frame.getContentPane().add(lblProjectName);

        JLabel lblTeamData = new JLabel("Team Data");
        lblTeamData.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblTeamData.setBounds(49, 116, 89, 39);
        frame.getContentPane().add(lblTeamData);
        labelTeamFile = new JLabel("");
        labelTeamFile.setBounds(280, 129, 200, 14);
        frame.getContentPane().add(labelTeamFile);
        JButton btnChooseFileTeam = new JButton("Choose File");
        btnChooseFileTeam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    teamSelectedFile = jfc.getSelectedFile();
                    labelTeamFile.setText(teamSelectedFile.getAbsolutePath());
                    System.out.println(teamSelectedFile.getAbsolutePath());
                }
            }
        });
        btnChooseFileTeam.setBounds(152, 125, 120, 23);
        frame.getContentPane().add(btnChooseFileTeam);

        JLabel lblTaskData = new JLabel("Task Data");
        lblTaskData.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblTaskData.setBounds(49, 166, 75, 29);
        frame.getContentPane().add(lblTaskData);
        labelTaskFile = new JLabel("");
        labelTaskFile.setBounds(280, 174, 200, 14);
        frame.getContentPane().add(labelTaskFile);
        JButton btnChooseFileTask = new JButton("Choose File");
        btnChooseFileTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    taskSelectedFile = jfc.getSelectedFile();
                    labelTaskFile.setText(taskSelectedFile.getAbsolutePath());
                    System.out.println(taskSelectedFile.getAbsolutePath());
                }
            }
        });
        btnChooseFileTask.setBounds(152, 170, 120, 23);
        frame.getContentPane().add(btnChooseFileTask);

        JLabel lblTaskReview = new JLabel("Task Review");
        lblTaskReview.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblTaskReview.setBounds(49, 216, 75, 29);
        frame.getContentPane().add(lblTaskReview);
        labelReviewFile = new JLabel("");
        labelReviewFile.setBounds(280, 224, 200, 14);
        frame.getContentPane().add(labelReviewFile);
        JButton btnChooseFileReview = new JButton("Choose File");
        btnChooseFileReview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    taskReviewFile = jfc.getSelectedFile();
                    labelReviewFile.setText(taskReviewFile.getAbsolutePath());
                }
            }
        });
        btnChooseFileReview.setBounds(152, 220, 120, 23);
        frame.getContentPane().add(btnChooseFileReview);

        txtNameProject = new JTextField("Name");
        txtNameProject.setBounds(152, 45, 180, 20);
        frame.getContentPane().add(txtNameProject);
        txtNameProject.setColumns(10);

        JButton btnSubmit = new JButton("Schedule");
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int iterationLength = Integer.parseInt(txtLength.getText());
                if (teamSelectedFile == null) {
                    JOptionPane.showMessageDialog(null, "Hãy chọn file lưu thông tin các resource");
                } else if (taskSelectedFile == null) {
                    JOptionPane.showMessageDialog(null, "Hãy chọn file lưu thông tin các task");
                } else 
                {
                    try {
                        // đọc dữ liệu resource và task
                        List<Resource> resources = FileManage.readTeams(teamSelectedFile.getAbsolutePath());
                        List<UserStory> userStories = FileManage.readStory(taskSelectedFile.getAbsolutePath());
                        
                        // thực hiện lập lịch
                        AlgorithmScheduling algorithm = new AlgorithmScheduling(userStories, resources,
                                iterationLength);
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
                                    algorithm.getScheduledWithNameTask(), "");
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Không thể lập lịch với bộ dữ liệu đã chọn. Hãy chọn bộ dữ liệu khác.");
                        }
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage());
                    } catch (InconsistentEvidenceException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage());
                    }

                }
            }
        });
        btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnSubmit.setBounds(150, 277, 89, 23);
        frame.getContentPane().add(btnSubmit);

        JButton btnSubmitPoker = new JButton("Schedule with Poker");
        btnSubmitPoker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int iterationLength = Integer.parseInt(txtLength.getText());
                if (teamSelectedFile == null) {
                    JOptionPane.showMessageDialog(null, "Hãy chọn file lưu thông tin các resource");
                } else if (taskSelectedFile == null) {
                    JOptionPane.showMessageDialog(null, "Hãy chọn file lưu thông tin các task");
                } else 
                {

                    try {

                        // đọc dữ liệu resource và task

                        List<Resource> resources = FileManage.readResource(teamSelectedFile.getAbsolutePath());
                        List<UserStory> userStories = FileManage.readStory(taskSelectedFile.getAbsolutePath());
                        List<List<Reviewer>> ls = FileManage.readReviewer(taskReviewFile.getAbsolutePath());
                        
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
                                iterationLength);
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
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage());
                    } catch (InconsistentEvidenceException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage());
                    }

                }
            }
        });
        btnSubmitPoker.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnSubmitPoker.setBounds(250, 277, 150, 23);
        frame.getContentPane().add(btnSubmitPoker);

        JLabel lblLength = new JLabel("Length Sprint");
        lblLength.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblLength.setBounds(49, 76, 95, 29);
        frame.getContentPane().add(lblLength);

        txtLength = new JTextField("80");
        txtLength.setBounds(152, 81, 86, 20);
        frame.getContentPane().add(txtLength);
        txtLength.setColumns(10);

    }

}
