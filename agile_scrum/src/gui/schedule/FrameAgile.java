package gui.schedule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.bayesserver.inference.InconsistentEvidenceException;

import model.BayesNet;
import util.FileManage;

public class FrameAgile extends JFrame {
    private static final long serialVersionUID = 1L;
    private int width = 1024;
    private int high = 200;
    private int maxTime;
    private String message;
    private static BayesNet network;
    private static ArrayList<Panel> listPanel;
    private static JButton save;
//    private String type;

    /**
     * 
     * @param net
     * @param dataSL:
     *            mảng thời gian thực hiện của mỗi task tương ứng với mối team
     * @param schedule:
     *            mảng lưu tên các task phân cho từng resource
     * @throws IOException
     * @throws InconsistentEvidenceException
     */
    public FrameAgile(BayesNet net, final int[][] arrTimeExcute, final List<List<String>> scheduleNameTask, String type)
            throws IOException, InconsistentEvidenceException {
//        this.type = type;
        FrameAgile.network = net;
        int nResources = net.getNumResources();
        this.high = 30 * (nResources + 3);

        // xây dựng mạng rủi ro cho các rủi ro và các task cho các tài nguyên
        network.construct();

        // tính xác suất của mỗi task tương ứng với mỗi resource
        final double[][] dataProb = network.infer();

        this.setSize(width, high);
        if ("poker".equals(type)) {
            this.setTitle("Frame Agile Using Poker");
        } else
            this.setTitle("Frame Agile ");
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);

        // tính thời gian của resource dài nhất
        this.maxTime = maxTime(arrTimeExcute);

        // list panel lưu các panel tương ứng với từng resource có các task được
        // phân
        FrameAgile.listPanel = new ArrayList<Panel>();
        //
        int widthPanel = width;
        int heightPanel = (high - 10) / (nResources + 2);
        for (int i = 0; i < nResources; i++) {

            int x1 = 0;
            int y1 = (high - 10) / (nResources + 2) * i;

            Panel panelArea = new Panel(maxTime, network.getResourceNet().get(i));
            panelArea.setBounds(x1, y1, widthPanel, heightPanel);
            createPanel(arrTimeExcute[i], scheduleNameTask.get(i), panelArea, widthPanel, heightPanel, dataProb[i], i);
            this.add(panelArea);
        }

        // thêm các button chức năng
        int x1 = 0;
        int y1 = (high - 10) / (nResources + 2) * nResources;
        JPanel panelArea = new JPanel();
        panelArea.setBounds(x1, y1, widthPanel, heightPanel);
        // panelArea.setLayout(null);

        JButton okBtn = new JButton("Tổng hợp");
        message = "Xác xuất hoàn thành của cả lịch trình: "
                + FrameAgile.caculateProbTotal(FrameAgile.network.infer()) * 100 + "%";
        System.out.println(message);
        okBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Tong họp lai : \n" + message);

            }
        });

        JButton reload = new JButton("Cập nhật");
        reload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 new InforResource(network);
            }
        });

        save = new JButton("Lưu kết quả");
        save.addActionListener(new ActionSaveData(scheduleNameTask, dataProb, type));
        panelArea.add(okBtn);
        panelArea.add(reload);
        panelArea.add(save);
        this.add(panelArea);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * tìm thời gian thực hiện dài nhất trong các resource
     * 
     * @param dataSL:
     *            mảng lưu trữ thời gian thực hiện các task của các resource
     * @return
     */
    private int maxTime(int[][] dataSL) {
        int max = 0;
        for (int i = 0; i < dataSL.length; i++) {
            int totalTime = 0;
            for (int j = 0; j < dataSL[i].length; j++) {
                totalTime += dataSL[i][j];
            }
            if (totalTime > max) {
                max = totalTime;
            }
        }
        return max;
    }

    /**
     * tạo các button trên mỗi panel
     * 
     * @param arrSL:
     *            mảng thời gian thực hiện các task của resource
     * @param arrID:
     *            mảng các id task phân cho resource
     * @param panelArea:
     *            panel chứa các button
     * @param widthPanel:
     *            chiều dài của panel
     * @param heightPanel:
     *            chiều cao panel
     * @param arrProb:
     *            mảng xác suất thành công của các task phân cho resource
     * @param i:
     *            thứ tự của resource
     * @throws IOException
     */
    private void createPanel(int[] arrSL, List<String> nameTasks, Panel panelArea, int widthPanel, int heightPanel,
            double[] arrProb, int i) throws IOException {

        panelArea.createButton(arrSL, nameTasks, widthPanel, heightPanel, arrProb, i + 1);
        this.add(panelArea);
        listPanel.add(panelArea);
    }

    /**
     * tính lại xác suất thực hiện các task trong dự án
     */
    public static void reLoad() {
        try {
            double[][] prob = network.infer();
            for (int l = 0; l < listPanel.size(); l++) {
                listPanel.get(l).reloadData(prob[l]);
            }
        } catch (InconsistentEvidenceException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * tính lại xác suất thực hiện dự án
     */
    public static double caculateProbTotal(double[][] dataProb) throws InconsistentEvidenceException {
        double totalProb = 0;
        int nResources = dataProb.length;
        int numResourceNotEmpty = nResources;
        for (int i = 0; i < nResources; i++) {
            int nTasksPerRes = 0;
            for (int j = 0; j < dataProb[i].length; j++) {
                if (dataProb[i][j] > 0)
                    nTasksPerRes++;
            }
            if(nTasksPerRes > 0){
                totalProb += dataProb[i][nTasksPerRes - 1];
            }else{
                numResourceNotEmpty--;
            }

        }
        return totalProb / numResourceNotEmpty;
    }
}

final class ActionSaveData implements ActionListener {
    private List<List<String>> scheduleNameTask;
    private double[][] prob;
    private String type;

    public ActionSaveData(List<List<String>> scheduleNameTask, double[][] prob, String type) {
        this.scheduleNameTask = scheduleNameTask;
        this.prob = prob;
        this.type = type;
    }

    public void actionPerformed(ActionEvent e) {
        while (true) {
            String fileName = JOptionPane.showInputDialog("Nhập tên file lưu");
            System.out.println(fileName);

            if (fileName == null) {
                break;
            }

            // nếu không nhập thì thông báo nhập lại
            if (fileName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Hãy nhập tên file lưu dữ liệu!");
            } else {

                // nếu nhập thì ghi dữ liệu ra file
                try {
                    File folder = new File("result");
                    if (!folder.isDirectory() || !folder.exists()) {
                        folder.mkdir();
                    }
                    if ("poker".equals(type))
                        FileManage.writeCSVFile("./result/" + fileName + "_poker.csv", scheduleNameTask, prob);
                    else
                        FileManage.writeCSVFile("./result/" + fileName + ".csv", scheduleNameTask, prob);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Error when write file: " + e1.getMessage());
                } catch (InconsistentEvidenceException e1) {
                    JOptionPane.showMessageDialog(null, "Error when inference: " + e1.getMessage());
                }
                JOptionPane.showMessageDialog(null, "Ghi dữ liệu thành công");
                break;
            }
        }
    }
}