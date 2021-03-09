package model;

import com.bayesserver.*;
import com.bayesserver.inference.*;

import entity.Resource;
import entity.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BayesNet {
    private List<Network> resourceNet;
    private List<Resource> resources;
    private int numResources;
    private int totalTasks;

    public BayesNet(List<Resource> resources, List<Task> tasks) {
        this.numResources = resources.size();
        this.resources = resources;
        this.totalTasks = tasks.size();
        resourceNet = new ArrayList<Network>();

    }

    public BayesNet(List<Resource> resources, int totalTasks) {
        this.numResources = resources.size();
        this.resources = resources;
        this.totalTasks = totalTasks;
        resourceNet = new ArrayList<Network>();

    }

    /**
     * xây dựng mạng rủi ro cho các rủi ro và các task cho các tài nguyên
     * 
     * @throws IOException
     */

    public void construct() throws IOException {

        // duyệt từng tài nguyên
        for (int i = 0; i < numResources; i++) {

            // lấy ra số lượng task được phân cho resource này
            int numTasks = resources.get(i).getListTask().size();

            // Khởi tạo mảng các rủi ro
            RisksNet risknet = new RisksNet(Integer.toString(i));
            Network net = risknet.getRiskNet();

            // thêm các node tương ứng với các task được phân cho resource vào
            // mạng cùng với bảng phân phối xác suất của từng node
            String[] arrStateBoolean = new String[] { "True", "False" };
            Node task0 = new Node("task0", arrStateBoolean);
            net.getNodes().add(task0);
            Node riskNode = net.getNodes().get("risk");
            net.getLinks().add(new Link(riskNode, task0));

            Table table = task0.newDistribution().getTable();
            TableIterator iterator = new TableIterator(table, new Node[] { task0, riskNode });
            iterator.copyFrom(new double[] { 0.95, 0.6, 0.05, 0.4 });
            task0.setDistribution(table);

            Node previousTask = task0;
            for (int j = 1; j < numTasks; j++) {
                Node taskj;
                if (j == numTasks - 1)
                    taskj = new Node("lastTask", arrStateBoolean);
                else
                    taskj = new Node("task" + j, arrStateBoolean);

                net.getNodes().add(taskj);
                net.getLinks().add(new Link(riskNode, taskj));
                net.getLinks().add(new Link(previousTask, taskj));
                table = taskj.newDistribution().getTable();
                iterator = new TableIterator(table, new Node[] { taskj, previousTask, riskNode });
                iterator.copyFrom(new double[] { 0.95, 0.65, 0.8, 0.4, 0.05, 0.35, 0.2, 0.6 });
                taskj.setDistribution(table);
                previousTask = taskj;
            }
            resourceNet.add(net);
        }
    }

    /**
     * suy diễn để tính xác suất thực hiện thành công các task với từng resource
     * 
     * @return mảng lưu xác suất thực hiện thành công của các task được thực
     *         hiện bởi resource tương ứng
     * @throws InconsistentEvidenceException
     */
    public double[][] infer() throws InconsistentEvidenceException {
        double[][] result = new double[numResources][totalTasks];

        // khởi tạo giá trị ban đầu
        for (int i = 0; i < numResources; i++)
            for (int j = 0; j < totalTasks; j++)
                result[i][j] = 0;

        // duyệt từng resource
        for (int i = 0; i < numResources; i++) {

            // lấy số lượng task phân cho resource
            int numTasks = resources.get(i).getListTask().size();

            // lấy ra mạng các rủi ro
            Network neti = resourceNet.get(i);

            InferenceFactory factory = new RelevanceTreeInferenceFactory();
            Inference inference = factory.createInferenceEngine(neti);
            QueryOptions queryOptions = factory.createQueryOptions();
            QueryOutput queryOutput = factory.createQueryOutput();

            // lấy ra các trạng thái tương ứng với resource và set các nhân tố
            // tương ứng với resource đó
            StateCollection stateNode19 = neti.getNodes().get("node19").getVariables().get(0).getStates();
            StateCollection stateNode16 = neti.getNodes().get("node16").getVariables().get(0).getStates();
            StateCollection stateNode7 = neti.getNodes().get("node7").getVariables().get(0).getStates();
            StateCollection stateNode9 = neti.getNodes().get("node9").getVariables().get(0).getStates();

            State valueNode19 = stateNode19.get(resources.get(i).getAgileExperience());
            State valueNode16 = stateNode16.get(Integer.toString(resources.get(i).getAgileSkill()));
            State valueNode7 = stateNode7.get(Integer.toString(resources.get(i).getSkillLevel()));
            State valueNode9 = stateNode9.get(resources.get(i).getDailyMeeting());

            inference.getEvidence().setState(valueNode19);
            inference.getEvidence().setState(valueNode16);
            inference.getEvidence().setState(valueNode7);
            inference.getEvidence().setState(valueNode9);
            neti.getNodes().get("risk").getVariables().get(0).getStates().get("False");
            // duyệt các task được giao và thực hiện suy diễn với resource đó
            // rồi gán vào mảng lưu kết quả
            if (numTasks == 1) {
                String nameTask = "task0";
                Table queryTaskj = new Table(neti.getNodes().get(nameTask));
                inference.getQueryDistributions().add(queryTaskj);
                inference.query(queryOptions, queryOutput);
                State stateTrue = neti.getNodes().get(nameTask).getVariables().get(0).getStates().get("True");
                result[i][0] = queryTaskj.get(stateTrue);
            } else {

                for (int j = 0; j < numTasks - 1; j++) {
                    String nameTask = "task" + Integer.toString(j);
                    Table queryTaskj = new Table(neti.getNodes().get(nameTask));
                    inference.getQueryDistributions().add(queryTaskj);
                    inference.query(queryOptions, queryOutput);
                    State stateTrue = neti.getNodes().get(nameTask).getVariables().get(0).getStates().get("True");
                    result[i][j] = queryTaskj.get(stateTrue);

                }

                if (numTasks > 1) {
                    Table queryLastTask = new Table(neti.getNodes().get("lastTask"));
                    State stateTrue = neti.getNodes().get("lastTask").getVariables().get(0).getStates().get("True");
                    inference.getQueryDistributions().add(queryLastTask);
                    inference.query(queryOptions, queryOutput);
                    result[i][numTasks - 1] = queryLastTask.get(stateTrue);
               
                }
            }
        }
        return result;

    }

    /**
     * lấy ra mảng id phân bố các task cho các resource
     * 
     * @return
     */
    // public int[][] getMatrixTaskID() {
    // return matrixTaskID;
    // }

    /**
     * lấy ra tổng số resource
     * 
     * @return
     */
    public int getNumResources() {
        return numResources;
    }

    /**
     * lấy ra tổng số task
     * 
     * @return
     */
    public int getNumTotalTasks() {
        return totalTasks;
    }

    /**
     * lấy ra các node cha của 1 node
     * 
     * @param node:
     *            node cần tìm các node cha
     * @return: mảng lưu các nhãn tương ứng với các node cha
     */
    public static String[] getParentNodes(Node node) {
        // lấy ra số lượng link tới node đang xét
        NodeLinkCollection collection = node.getLinksIn();
        int size = collection.size();
        String[] pNode = new String[size];

        String prename = node.getName().substring(0, 4);

        // nếu node là các task
        if (prename.compareTo("task") == 0 || prename.compareTo("last") == 0) {

            // lấy ra 1 node cha là risk thì kiểm tra xem số lượng node cha là
            // bao nhiêu. Nếu có 1 thì là task đầu tiên (chỉ có node cha là
            // risk) được còn lại là các task tiếp theo (có 2 node cha là risk
            // và task đứng trước nó) trong danh sách task phân cho tài nguyên
            if (collection.get(0).getFrom().getName().compareTo("risk") == 0) {
                if (size > 1) {
                    pNode[0] = collection.get(1).getFrom().getName();
                    pNode[1] = "risk";
                } else
                    pNode[0] = "risk";
            } else {
                pNode[0] = collection.get(0).getFrom().getName();
                pNode[1] = "risk";
            }
        } else {
            // nếu ko phải là các task
            int[] arr = new int[size];
            // duyệt lần lượt từng link tới để lấy tên node cha
            for (int i = 0; i < size; i++) {
                arr[i] = Integer.parseInt(collection.get(i).getFrom().getName().substring(4));
            }

            // sắp xếp các node theo thứ tự tăng dần
            Arrays.sort(arr);
            for (int i = 0; i < size; i++) {
                pNode[i] = "node" + arr[i];
            }
        }

        return pNode;
    }

    /**
     * chuẩn hóa dữ liệu của bảng phân phối xác suất
     * 
     * @param dist:
     *            mảng 1 chiều lưu trữ các xác suất
     * @param row:
     *            số lượng trạng thái của node đó
     * @param col:
     *            số lượng trạng thái phụ thuộc node cha
     * @return: mảng xác suất sau khi chuẩn hóa
     */
    public static double[] normalize(double[] dist, int row, int col) {
        int len = dist.length;

        if (len != (row * col)) {
            System.out.println("Error in normalize function!");
            return null;
        }

        // mảng lưu giá trị sau chuẩn hóa
        double[] newDist = new double[row * col];
        double sum;
        for (int j = 0; j < col; j++) {
            sum = 0;
            for (int i = 0; i < row; i++)
                sum = sum + dist[i * col + j];
            // System.out.println("sum: " + sum);
            for (int i = 0; i < row; i++)
                newDist[i * col + j] = dist[i * col + j] / sum;
        }
        return newDist;
    }

    /**
     * lấy ra bảng phân phối xác suất của 1 node
     * 
     * @param node:
     *            node cần lấy bảng phân phối xác suất
     * @return bảng phân phối xác suất dưới dạng mảng 2 chiều
     */
    public static double[][] getDistribution(Node node) {
        // lấy số trạng thái của node đó
        int numState = node.getVariables().get(0).getStates().size();

        // lấy danh sách các bảng phân phối xác suất
        ArrayList<double[]> listDistribution = RisksNet.getListDistribution();
        double[] table = null;
        // nếu là các node thì vị trí bảng trong danh sách chính là thứ tự node
        // đó -1
        if (node.getName().substring(0, 4).compareTo("node") == 0) {
            int index = Integer.parseInt(node.getName().substring(4));
            table = listDistribution.get(index - 1);
            table = normalize(table, numState, table.length / numState);

            // nếu là task đầu tiên
        } else if (node.getName().compareTo("task0") == 0) {
            table = new double[] { 0.7, 0.95, 0.3, 0.05 };

            // nếu là node risk thì vị trí là 19
        } else if (node.getName().compareTo("risk") == 0) {
            table = listDistribution.get(19);
            table = normalize(table, numState, table.length / numState);
        } else {

            table = new double[] { 0.7, 0.95, 0.1, 0.3, 0.3, 0.05, 0.9, 0.7 };
        }

        int size = table.length;
        int length = size / numState;
        double[][] dist = new double[numState][length];
        for (int i = 0; i < numState; i++)
            for (int j = 0; j < length; j++)
                dist[i][j] = table[i * length + j];
        return dist;
    }

    /**
     * @return the resourceNet
     */
    public List<Network> getResourceNet() {
        return resourceNet;
    }

    /**
     * @return the resources
     */
    public List<Resource> getResources() {
        return resources;
    }

}
