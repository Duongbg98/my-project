package model;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bayesserver.Link;
import com.bayesserver.Network;
import com.bayesserver.Node;
import com.bayesserver.State;
import com.bayesserver.StateCollection;
import com.bayesserver.Table;
import com.bayesserver.TableIterator;
import com.bayesserver.inference.InconsistentEvidenceException;
import com.bayesserver.inference.Inference;
import com.bayesserver.inference.InferenceFactory;
import com.bayesserver.inference.QueryOptions;
import com.bayesserver.inference.QueryOutput;
import com.bayesserver.inference.RelevanceTreeInferenceFactory;

import au.com.bytecode.opencsv.CSVReader;
import entity.Resource;
import entity.Reviewer;

/**
 * Xây dựng mạng bayes để tính toán giá trị của task và userstory
 *
 */
public class BayesPoker {

    private Network networkPoker;
    private static List<Node> listNode;

    private static ArrayList<double[]> listDistribution = new ArrayList<double[]>();

    // đọc dữ liệu bảng phân phối xác suất từ file
    static {
        CSVReader reader = null;
        try {
            String[] line = null;
            reader = new CSVReader(new FileReader("distribution_poker.csv"));

            line = reader.readNext();

            listDistribution.add(convertDouble(line));

            line = reader.readNext();
            listDistribution.add(convertDouble(line));

            line = reader.readNext();
            listDistribution.add(convertDouble(line));

            line = reader.readNext();
            listDistribution.add(convertDouble(line));

            line = reader.readNext();
            listDistribution.add(convertDouble(line));

            line = reader.readNext();
            listDistribution.add(convertDouble(line));

            line = reader.readNext();
            listDistribution.add(convertDouble(line));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BayesPoker() {
        networkPoker = new Network("id");
        listNode = new ArrayList<Node>();

        // add the nodes (variables)
        String arrState2Level[] = { "High", "Low" };
        String arrState3Level[] = { "High", "Medium", "Low" };

        // tạo các node với các trạng thái tương ứng
        Node nodeValue = new Node("Value", arrState2Level);
        networkPoker.getNodes().add(nodeValue);
        listNode.add(nodeValue);

        Node nodeComplexity = new Node("Complexity", arrState2Level);
        networkPoker.getNodes().add(nodeComplexity);
        listNode.add(nodeComplexity);

        Node nodeImportance = new Node("Importance", arrState3Level);
        networkPoker.getNodes().add(nodeImportance);
        listNode.add(nodeImportance);

        Node nodeTimeEffort = new Node("Time-Effort", arrState2Level);
        networkPoker.getNodes().add(nodeTimeEffort);
        listNode.add(nodeTimeEffort);
        
        Node nodeExperience = new Node("Experience", arrState3Level);
        networkPoker.getNodes().add(nodeExperience);
        listNode.add(nodeExperience);

        Node nodeTime = new Node("Time", arrState3Level);
        
        networkPoker.getNodes().add(nodeTime);
        listNode.add(nodeTime);

        Node nodeEffort = new Node("Effort", arrState3Level);
        networkPoker.getNodes().add(nodeEffort);
        listNode.add(nodeEffort);

        // add các link cho các node trong mạng

        networkPoker.getLinks().add(new Link(nodeComplexity, nodeValue));
        networkPoker.getLinks().add(new Link(nodeImportance, nodeValue));
        networkPoker.getLinks().add(new Link(nodeExperience, nodeComplexity));
        networkPoker.getLinks().add(new Link(nodeTimeEffort, nodeComplexity));
        networkPoker.getLinks().add(new Link(nodeTime, nodeTimeEffort));
        networkPoker.getLinks().add(new Link(nodeEffort, nodeTimeEffort));

        // thêm bảng phân phối xác suất cho các node
        int index = 0;
//        Node kinh nghiệm
        Table tableNode = nodeExperience.newDistribution().getTable();
        TableIterator iterator = new TableIterator(tableNode, new Node[] { nodeExperience});
        double[] dist = listDistribution.get(index++);
        dist = normalize(dist, 3, dist.length / 3);
        iterator.copyFrom(dist);
        nodeExperience.setDistribution(tableNode);

//        node time
        tableNode = nodeTime.newDistribution().getTable();
        iterator = new TableIterator(tableNode, new Node[] { nodeTime});
        dist = listDistribution.get(index++);
        dist = normalize(dist, 3, dist.length / 3);
        iterator.copyFrom(dist);
        nodeTime.setDistribution(tableNode);

//        node effort
        tableNode = nodeEffort.newDistribution().getTable();
        iterator = new TableIterator(tableNode, new Node[] { nodeEffort });
        dist = listDistribution.get(index++);
        dist = normalize(dist, 3, dist.length / 3);
        iterator.copyFrom(dist);
        nodeEffort.setDistribution(tableNode);

//        node importance
        tableNode = nodeImportance.newDistribution().getTable();
        iterator = new TableIterator(tableNode, new Node[] { nodeImportance });
        dist = listDistribution.get(index++);
        dist = normalize(dist, 3, dist.length / 3);
        iterator.copyFrom(dist);
        nodeImportance.setDistribution(tableNode);
        
//        node time-efort
        tableNode = nodeTimeEffort.newDistribution().getTable();
        iterator = new TableIterator(tableNode, new Node[] { nodeTimeEffort, nodeTime, nodeEffort });
        dist = listDistribution.get(index++);
        dist = normalize(dist, 2, dist.length / 2);
        iterator.copyFrom(dist);
        nodeTimeEffort.setDistribution(tableNode);
        
//        node complex
        tableNode = nodeComplexity.newDistribution().getTable();
        iterator = new TableIterator(tableNode, new Node[] { nodeComplexity, nodeExperience, nodeTimeEffort });
        dist = listDistribution.get(index++);
        dist = normalize(dist, 2, dist.length / 2);
        
        iterator.copyFrom(dist);
        nodeComplexity.setDistribution(tableNode);
        
//        node Value
         tableNode = nodeValue.newDistribution().getTable();
         iterator = new TableIterator(tableNode, new Node[] { nodeValue,  nodeComplexity, nodeImportance});
        dist = listDistribution.get(index++);

        dist = normalize(dist, 2, dist.length / 2);
        iterator.copyFrom(dist);
        nodeValue.setDistribution(tableNode);

    }
    
    public double calculateValue(Reviewer reviewer, String priority){
        
        InferenceFactory factory = new RelevanceTreeInferenceFactory();
        Inference inference = factory.createInferenceEngine(networkPoker);
        QueryOptions queryOptions = factory.createQueryOptions();
        QueryOutput queryOutput = factory.createQueryOutput();

        // lấy ra các trạng thái tương ứng với resource và set các nhân tố
        // tương ứng với resource đó
        StateCollection stateNodeExperience = networkPoker.getNodes().get("Experience").getVariables().get(0).getStates();
        StateCollection stateNodeTime = networkPoker.getNodes().get("Time").getVariables().get(0).getStates();
        StateCollection stateNodeEffort = networkPoker.getNodes().get("Effort").getVariables().get(0).getStates();
//        System.out.println(stateNodeEffort.toString());
        StateCollection stateNodeImportance = networkPoker.getNodes().get("Importance").getVariables().get(0).getStates();

        State valueStateExperience = stateNodeExperience.get(reviewer.getExperience());
//System.out.println("Time: " + reviewer.getTime());
        State valueStateTime = stateNodeTime.get(reviewer.getTime());
//        System.out.println(reviewer.getEffort());
        State valueStateEffort = stateNodeEffort.get(reviewer.getEffort());
//        System.out.println(valueStateEffort.toString());
        State valueStateProirity = stateNodeImportance.get(priority);
//        System.out.println(valueStateProirity);

        inference.getEvidence().setState(valueStateExperience);
        inference.getEvidence().setState(valueStateTime);
        inference.getEvidence().setState(valueStateEffort);
        inference.getEvidence().setState(valueStateProirity);

        // tính value với các thông số truyền vào
        Table queryTaskj = new Table(networkPoker.getNodes().get("Value"));
        inference.getQueryDistributions().add(queryTaskj);
        
        try {
            inference.query(queryOptions, queryOutput);
            State stateTrue = networkPoker.getNodes().get("Value").getVariables().get(0).getStates().get("High");
            return queryTaskj.get(stateTrue);
        } catch (InconsistentEvidenceException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            return 0;
        }
        
    }

    /**
     * Chuẩn hóa xác suất trong bảng phân phối xác suất
     * 
     * @param dist:
     *            dữ liệu cần chuẩn hóa
     * @param row:
     *            số lượng giá trị của node cần xét
     * @param col:
     *            số lượng
     * @return
     */
    private double[] normalize(double[] dist, int row, int col) {
        
        int len = dist.length;
        double[] newDist = new double[row * col];
        if (len != (row * col)) {
            System.out.println("Error in normalize function!");
            return null;
        }
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

    private static double[] convertDouble(String[] line) {
           
        double[] dist = new double[line.length];
        int i = 0;
        for (i = 0; i < line.length; i++) {

            if (!line[i].isEmpty()) {
                dist[i] = Double.parseDouble(line[i]);
            } else {
                break;
            }
        }
        double[] temp = new double[i];
        for (int j = 0; j < i; j++) {
            temp[j] = dist[j];
        }
        return temp;
    }
    
    public double estimateValue(List<Resource> resources, String priority, int indexTask){
        double sum = 0;
        for(Resource r: resources){
            sum += this.calculateValue(r.getTaskReviews().get(indexTask), priority);
        }
        return sum/resources.size();
    }
}
