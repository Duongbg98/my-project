package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tuanl
 *
 */
public class GenerateData {
    public void generateDistributionPoker(Node par, List<Node> nodes) {
        double[] lv1 = nodes.get(0).getXacSuat();
        double[] lv2 = nodes.get(1).getXacSuat();
        int[] weight_lv1, weight_lv2;
        weight_lv1 = generateWeigt(nodes.get(0).getName());
        weight_lv2 = generateWeigt(nodes.get(1).getName());
        int valNode1[] = generateValue(nodes.get(0).getName());
        int valNode2[] = generateValue(nodes.get(1).getName());
        for (int k = 0; k < par.getNumberValue(); k++) {
            double[][] dis_part1 = new double[lv1.length][lv2.length];
            double sum = 0;
            for (int i = 0; i < lv1.length; i++) {
                sum = lv1[i] * weight_lv1[i] * valNode1[i];
                for (int j = 0; j < lv2.length; j++) {
                    dis_part1[i][j] = sum + lv2[j] * weight_lv2[j] * valNode2[j];
                }
            }

            double max = 1;
            for (int i = 0; i < lv1.length; i++) {
                for (int j = 0; j < lv2.length; j++) {
                    max = max > dis_part1[i][j] ? max : dis_part1[i][j];
                }
            }

            int total = 0;
            for (int i = 0; i < lv1.length; i++) {
                for (int j = 0; j < lv2.length; j++) {
                    dis_part1[i][j] /= max;
                    total += dis_part1[i][j];
                    System.out.print(dis_part1[i][j] + "   ");
                }
                System.out.println();
            }
            
            par.getXacSuats().add(dis_part1);
            par.getXacSuat()[k] = total;
        }
    }

    private int[] generateValue(String nameNode) {
        if ("Time".equals(nameNode) || "Effort".equals(nameNode) || "Importance".equals(nameNode)) {
            int[] w = new int[3];
            w[0] = 1;
            w[1] = 2;
            w[2] = 3;
            return w;
        } else if ("Experience".equals(nameNode)) {
            int[] w = new int[3];
            w[0] = 1;
            w[1] = 2;
            w[2] = 3;
            return w;
        } else {
            int[] w = new int[2];
            w[0] = 1;
            w[1] = 2;
            return w;
        }
    }

    private int[] generateWeigt(String nameNode) {
        if ("Time".equals(nameNode) || "Effort".equals(nameNode) || "Importance".equals(nameNode)) {
            int[] w = new int[3];
            w[0] = 3;
            w[1] = 2;
            w[2] = 1;
            return w;
        } else if ("Experience".equals(nameNode)) {
            int[] w = new int[3];
            w[0] = 1;
            w[1] = 2;
            w[2] = 3;
            return w;
        } else if ("Time-Effort".equals(nameNode)) {
            int[] w = new int[2];
            w[0] = 3;
            w[1] = 1;
            return w;
        } else if ("Complexity".equals(nameNode)) {
            int[] w = new int[2];
            w[0] = 5;
            w[1] = 1;
            return w;
        } else {
            return null;
        }
    }
    
    public void generateDistribution() throws IOException{
        StringBuilder dataWrite = new StringBuilder();
        double p[] = { 0.34, 0.33, 0.33 };
        Node Time = new Node(p, "Time");
        appendData(dataWrite, Time.getXacSuats());
        Node ef = new Node(p, "Effort");
        appendData(dataWrite, ef.getXacSuats());
        Node ex = new Node(p, "Experience");
        appendData(dataWrite, ex.getXacSuats());
        Node importance = new Node(p, "Importance");
        appendData(dataWrite, importance.getXacSuats());
        Node complexity = new Node( "Complexity", 2);
        Node value = new Node( "Value", 2);
        // node Time-Effort
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(Time);
        nodes.add(ef);
        Node timeEffort = new Node("Time-Effort", 2);
        generateDistributionPoker(timeEffort, nodes);
        timeEffort.normalize();
        appendData(dataWrite, timeEffort.getXacSuats());
     // node Complex
        nodes = new ArrayList<Node>();
        nodes.add(ex);
        nodes.add(timeEffort);
        generateDistributionPoker(complexity, nodes);
        timeEffort.normalize();
        appendData(dataWrite, complexity.getXacSuats());
     // node Value
        nodes = new ArrayList<Node>();
        nodes.add(complexity);
        nodes.add(importance);
        generateDistributionPoker(value, nodes);
        timeEffort.normalize();
        appendData(dataWrite, value.getXacSuats());
        Writer writer = null;
        try {
            writer = new FileWriter("distribution_poker.csv");
            writer.append(dataWrite);

        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GenerateData generateData = new GenerateData();
        StringBuilder dataWrite = new StringBuilder();
        double p[] = { 0.34, 0.33, 0.33 };
        Node Time = new Node(p, "Time");
        generateData.appendData(dataWrite, Time.getXacSuats());
        Node ef = new Node(p, "Effort");
        generateData.appendData(dataWrite, ef.getXacSuats());
        Node ex = new Node(p, "Experience");
        generateData.appendData(dataWrite, ex.getXacSuats());
        Node importance = new Node(p, "Importance");
        generateData.appendData(dataWrite, importance.getXacSuats());
        Node complexity = new Node( "Complexity", 2);
        Node value = new Node( "Value", 2);
        // node Time-Effort
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(Time);
        nodes.add(ef);
        Node timeEffort = new Node("Time-Effort", 2);
        generateData.generateDistributionPoker(timeEffort, nodes);
        timeEffort.normalize();
        generateData.appendData(dataWrite, timeEffort.getXacSuats());
     // node Complex
        nodes = new ArrayList<Node>();
        nodes.add(ex);
        nodes.add(timeEffort);
        generateData.generateDistributionPoker(complexity, nodes);
        timeEffort.normalize();
        generateData.appendData(dataWrite, complexity.getXacSuats());
     // node Value
        nodes = new ArrayList<Node>();
        nodes.add(complexity);
        nodes.add(importance);
        generateData.generateDistributionPoker(value, nodes);
        timeEffort.normalize();
        generateData.appendData(dataWrite, value.getXacSuats());
        Writer writer = null;
        try {
            writer = new FileWriter("distribution_poker1.csv");
            writer.append(dataWrite);

        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
    
    public void appendData(StringBuilder sb,List<double[][]> list){
        for(double[][] a: list){
            for(int i = 0; i< a.length; i++){
                for(int j = 0; j< a[i].length; j++){
                    sb.append(a[i][j]);
                    sb.append(",");
                }
            }
        }
        
        sb.append("\n");
    }
}
