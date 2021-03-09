package model;

import com.bayesserver.*;

import au.com.bytecode.opencsv.CSVReader;
import util.FileManage;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RisksNet {

	private Network riskNet;
	private ArrayList<Node> node;

	private static ArrayList<double[]> listDistribution = new ArrayList<double[]>();

	// đọc dữ liệu bảng phân phối xác suất từ file
	static {
		CSVReader reader = null;
		try {
			String[] line = null;
			reader = new CSVReader(new FileReader("./distribution.csv"));

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

	public RisksNet(String id) throws IOException {
		riskNet = new Network(id);
		node = new ArrayList<Node>();

		// add the nodes (variables)
		String arrStateBool[] = { "False", "True" };
		String[] arrStateLevel = { "1", "2", "3", "4", "5" };
		String[] arrSateNodeYesNo = { "no", "yes" };

		// tạo các node với các trạng thái tương ứng
		Node node1 = new Node("node1", arrStateBool);
		riskNet.getNodes().add(node1);
		node.add(node1);

		Node node2 = new Node("node2", arrStateBool);
		riskNet.getNodes().add(node2);
		node.add(node2);

		Node node3 = new Node("node3", arrStateBool);
		riskNet.getNodes().add(node3);
		node.add(node3);

		Node node4 = new Node("node4", arrStateBool);
		riskNet.getNodes().add(node4);
		node.add(node4);

		Node node5 = new Node("node5", arrStateBool);
		riskNet.getNodes().add(node5);
		node.add(node5);

		Node node6 = new Node("node6", arrStateBool);
		riskNet.getNodes().add(node6);
		node.add(node6);

		Node node7 = new Node("node7", arrStateLevel);
		riskNet.getNodes().add(node7);
		node.add(node7);

		Node node8 = new Node("node8", arrStateBool);
		riskNet.getNodes().add(node8);
		node.add(node8);

		Node node9 = new Node("node9", arrSateNodeYesNo);
		riskNet.getNodes().add(node9);
		node.add(node9);

		Node node10 = new Node("node10", arrStateBool);
		riskNet.getNodes().add(node10);
		node.add(node10);

		Node node11 = new Node("node11", arrStateBool);
		riskNet.getNodes().add(node11);
		node.add(node11);

		Node node12 = new Node("node12", arrStateBool);
		riskNet.getNodes().add(node12);
		node.add(node12);

		Node node13 = new Node("node13", arrStateBool);
		riskNet.getNodes().add(node13);
		node.add(node13);

		Node node14 = new Node("node14", arrStateBool);
		riskNet.getNodes().add(node14);
		node.add(node14);

		Node node15 = new Node("node15", arrStateBool);
		riskNet.getNodes().add(node15);
		node.add(node15);

		Node node16 = new Node("node16", arrStateLevel);
		riskNet.getNodes().add(node16);
		node.add(node16);

		Node node17 = new Node("node17", arrStateBool);
		riskNet.getNodes().add(node17);
		node.add(node17);

		Node node18 = new Node("node18", arrStateBool);
		riskNet.getNodes().add(node18);
		node.add(node18);

		Node node19 = new Node("node19", arrSateNodeYesNo);
		riskNet.getNodes().add(node19);
		node.add(node19);

		Node risk = new Node("risk", arrStateBool);
		riskNet.getNodes().add(risk);
		node.add(risk);

		// add các link cho các node trong mạng

		riskNet.getLinks().add(new Link(node1, node2));
		riskNet.getLinks().add(new Link(node1, node5));
		riskNet.getLinks().add(new Link(node1, node19));
		riskNet.getLinks().add(new Link(node2, node7));
		riskNet.getLinks().add(new Link(node2, node10));
		riskNet.getLinks().add(new Link(node3, node4));
		riskNet.getLinks().add(new Link(node3, node8));
		riskNet.getLinks().add(new Link(node3, node9));
		riskNet.getLinks().add(new Link(node4, node8));
		riskNet.getLinks().add(new Link(node5, risk));
		riskNet.getLinks().add(new Link(node6, node5));
		riskNet.getLinks().add(new Link(node6, node4));
		riskNet.getLinks().add(new Link(node7, node15));
		riskNet.getLinks().add(new Link(node7, risk));
		riskNet.getLinks().add(new Link(node8, risk));
		riskNet.getLinks().add(new Link(node9, node4));
		riskNet.getLinks().add(new Link(node9, node14));
		riskNet.getLinks().add(new Link(node10, risk));
		riskNet.getLinks().add(new Link(node11, node12));
		riskNet.getLinks().add(new Link(node11, node10));
		riskNet.getLinks().add(new Link(node11, node7));
		riskNet.getLinks().add(new Link(node11, node13));
		riskNet.getLinks().add(new Link(node12, node5));
		riskNet.getLinks().add(new Link(node12, node8));
		riskNet.getLinks().add(new Link(node12, node9));
		riskNet.getLinks().add(new Link(node12, node17));
		riskNet.getLinks().add(new Link(node13, node14));
		riskNet.getLinks().add(new Link(node13, node17));
		riskNet.getLinks().add(new Link(node14, node8));
		riskNet.getLinks().add(new Link(node15, risk));
		riskNet.getLinks().add(new Link(node16, node14));
		riskNet.getLinks().add(new Link(node16, node17));
		riskNet.getLinks().add(new Link(node17, risk));
		riskNet.getLinks().add(new Link(node18, node4));
		riskNet.getLinks().add(new Link(node18, node17));
		riskNet.getLinks().add(new Link(node19, node3));
		riskNet.getLinks().add(new Link(node19, node6));
		riskNet.getLinks().add(new Link(node19, node15));

		// thêm bảng phân phối xác suất cho các node
		int index = 0;
		Table table1 = node1.newDistribution().getTable();
		TableIterator iterator1 = new TableIterator(table1, new Node[] { node1 });
		double[] dist = listDistribution.get(index++);

		dist = normalize(dist, 2, dist.length / 2);
		iterator1.copyFrom(dist);
		node1.setDistribution(table1);

		Table table2 = node2.newDistribution().getTable();
		TableIterator iterator2 = new TableIterator(table2, new Node[] { node2, node1 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator2.copyFrom(dist);
		node2.setDistribution(table2);

		Table table3 = node3.newDistribution().getTable();
		TableIterator iterator3 = new TableIterator(table3, new Node[] { node3, node19 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator3.copyFrom(dist);
		node3.setDistribution(table3);

		Table table4 = node4.newDistribution().getTable();
		TableIterator iterator4 = new TableIterator(table4, new Node[] { node4, node3, node6, node9, node18 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator4.copyFrom(dist);
		node4.setDistribution(table4);

		Table table5 = node5.newDistribution().getTable();
		TableIterator iterator5 = new TableIterator(table5, new Node[] { node5, node1, node6, node12 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator5.copyFrom(dist);
		node5.setDistribution(table5);

		Table table6 = node6.newDistribution().getTable();
		TableIterator iterator6 = new TableIterator(table6, new Node[] { node6, node19 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator6.copyFrom(dist);
		node6.setDistribution(table6);

		Table table7 = node7.newDistribution().getTable();
		TableIterator iterator7 = new TableIterator(table7, new Node[] { node7, node2, node11 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 5, dist.length / 5);
		iterator7.copyFrom(dist);
		node7.setDistribution(table7);

		Table table8 = node8.newDistribution().getTable();
		TableIterator iterator8 = new TableIterator(table8, new Node[] { node8, node3, node4, node12, node14 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator8.copyFrom(dist);
		node8.setDistribution(table8);

		Table table9 = node9.newDistribution().getTable();
		TableIterator iterator9 = new TableIterator(table9, new Node[] { node9, node3, node12 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator9.copyFrom(dist);
		node9.setDistribution(table9);

		Table table10 = node10.newDistribution().getTable();
		TableIterator iterator10 = new TableIterator(table10, new Node[] { node10, node2, node11 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator10.copyFrom(dist);
		node10.setDistribution(table10);

		Table table11 = node11.newDistribution().getTable();
		TableIterator iterator11 = new TableIterator(table11, new Node[] { node11 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator11.copyFrom(dist);
		node11.setDistribution(table11);

		Table table12 = node12.newDistribution().getTable();
		TableIterator iterator12 = new TableIterator(table12, new Node[] { node12, node11 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator12.copyFrom(dist);
		node12.setDistribution(table12);

		Table table13 = node13.newDistribution().getTable();
		TableIterator iterator13 = new TableIterator(table13, new Node[] { node13, node11 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator13.copyFrom(dist);
		node13.setDistribution(table13);

		Table table14 = node14.newDistribution().getTable();
		TableIterator iterator14 = new TableIterator(table14, new Node[] { node14, node9, node13, node16 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator14.copyFrom(dist);
		node14.setDistribution(table14);

		Table table15 = node15.newDistribution().getTable();
		TableIterator iterator15 = new TableIterator(table15, new Node[] { node15, node7, node19 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator15.copyFrom(dist);
		node15.setDistribution(table15);

		Table table16 = node16.newDistribution().getTable();
		TableIterator iterator16 = new TableIterator(table16, new Node[] { node16 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 5, dist.length / 5);
		iterator16.copyFrom(dist);
		node16.setDistribution(table16);

		Table table17 = node17.newDistribution().getTable();
		TableIterator iterator17 = new TableIterator(table17, new Node[] { node17, node12, node13, node16, node18 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator17.copyFrom(dist);
		node17.setDistribution(table17);

		Table table18 = node18.newDistribution().getTable();
		TableIterator iterator18 = new TableIterator(table18, new Node[] { node18 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator18.copyFrom(dist);
		node18.setDistribution(table18);

		Table table19 = node19.newDistribution().getTable();
		TableIterator iterator19 = new TableIterator(table19, new Node[] { node19, node1 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);
		iterator19.copyFrom(dist);
		node19.setDistribution(table19);

		Table tableRisk = risk.newDistribution().getTable();
		TableIterator iteratorRisk = new TableIterator(tableRisk,
				new Node[] { risk, node5, node7, node8, node10, node15, node17 });
		dist = listDistribution.get(index++);
		dist = normalize(dist, 2, dist.length / 2);

		iteratorRisk.copyFrom(dist);
		risk.setDistribution(tableRisk);
	}
	
	/**
     * Lấy ra mạng các rủi ro
     * 
     * @return
     */
    public Network getRiskNet() {
        return riskNet;
    }

    /**
     * lấy ra danh sách các node trong mạng rủi ro
     * 
     * @return
     */
    public ArrayList<Node> getNode() {
        return node;
    };

    /**
     * lấy ra bảng phân phối xác suất của các node
     * 
     * @return
     */
    public static ArrayList<double[]> getListDistribution() {
        return listDistribution;
    }

    private static double[] convertDouble(String[] line) {
        String[] list = line[0].split(FileManage.COLUMN_SEPARATOR);
        double[] dist = new double[list.length];
        int i = 0;
        for (i = 0; i < list.length; i++) {

            if (!list[i].isEmpty()) {
                dist[i] = Double.parseDouble(list[i]);
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
}
