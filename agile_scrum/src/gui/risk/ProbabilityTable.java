package gui.risk;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bayesserver.Node;

import model.BayesNet;

public class ProbabilityTable extends JFrame implements ComponentListener {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	public ArrayList<ArrayList<JTextField>> listTextField;
	public ArrayList<ArrayList<JTextField>> listDataTextField;
	private Node node;

	public ProbabilityTable(Node node) {
		this.node = node;
		this.setTitle("Probability " + node.getName());
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		this.listTextField = new ArrayList<ArrayList<JTextField>>();
		this.listDataTextField = new ArrayList<ArrayList<JTextField>>();

		this.setSize(640, 480);
		panel = new JPanel();

		this.add(panel);
		// frame.setLayout(new GridLayout());

		this.setVisible(true);

		panel.setBounds(0, 0, 600, 400);
		panel.setLayout(null);
		addTextField(panel);
		this.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				int height = getHeight();
				int width = getWidth();
				panel.setBounds(0, 0, width - 15, height - 50);
				for (int i = 0; i < listTextField.size(); i++) {
					ArrayList<JTextField> rowTextField = listTextField.get(i);
					int col = rowTextField.size();
					rowTextField.get(0).setBounds(0, i * 50, 100, 50);

					for (int j = 1; j < col; j++) {
						rowTextField.get(j).setBounds((j - 1) * (panel.getWidth() - 100) / (col - 1) + 100, i * 50,
								(panel.getWidth() - 100) / (col - 1), 50);

					}
				}
			}

			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * add các label hiển thị trong bảng phân phối xác suất
	 * 
	 * @param panel
	 */
	private void addTextField(JPanel panel) {

		// lấy các node cha của 1 node
		String[] parentNodes = BayesNet.getParentNodes(this.node);

		// add tên các node cha
		for (int i = 0; i < parentNodes.length; i++) {
			ArrayList<JTextField> rowTextField = new ArrayList<JTextField>();
			rowTextField.add(new JTextField(parentNodes[i]));
			listTextField.add(rowTextField);
		}

		// lấy các giá trị của node đó và add vào các dòng của bảng xác suất
		String[] values = getValue(this.node.getName());

		for (int i = 0; i < values.length; i++) {
			ArrayList<JTextField> rowTextField = new ArrayList<JTextField>();
			rowTextField.add(new JTextField(values[i]));
			listTextField.add(rowTextField);
			ArrayList<JTextField> rowDataTextField = new ArrayList<JTextField>();
			listDataTextField.add(rowDataTextField);
		}

		// add các giá trị vào các dòng của node cha
		for (int i = 0; i < parentNodes.length; i++) {

			// nếu là cha đầu tiên thì thêm luôn các giá trị vào mảng
			if (i == 0) {
				String[] valueParents = getValue(parentNodes[i]);
				for (String value : valueParents) {
					JTextField tf = new JTextField(value);
					tf.setToolTipText(value);
					listTextField.get(i).add(tf);
				}

			} else {

				// nếu là node cha thứ 2 trở đi thì sẽ duyệt với mỗi giá trị của
				// node đang xét sẽ thêm các giá trị cho node trước nó
				for (int k = 0; k < listTextField.get(i - 1).size() - 1; k++) {

					// lấy các giá trị của node đang xét và thêm vào mảng
					// textfield
					String[] valueParent = getValue(parentNodes[i]);
					for (int j = 0; j < valueParent.length; j++) {
						JTextField tf = new JTextField(valueParent[j]);
						tf.setToolTipText(valueParent[j]);
						listTextField.get(i).add(tf);
					}
				}
			}
		}

		// thêm các giá trị xác suất của node hiện tại phụ thuộc vào các node
		// cha
		for (int i = 0; i < values.length; i++) {
			if (parentNodes.length >= 1) {
				for (int j = 0; j < listTextField.get(parentNodes.length - 1).size() - 1; j++) {
					listTextField.get(i + parentNodes.length).add(new JTextField());
					listDataTextField.get(i).add(listTextField.get(i + parentNodes.length).get(j + 1));
				}
			} else {

				// nếu không có node cha sẽ add luôn giá trị tương ứng với mỗi
				// giá trị
				listTextField.get(i).add(new JTextField());
				listDataTextField.get(i).add(listTextField.get(i).get(1));
			}
		}

		// set vị trí cho các textfield
		for (int i = 0; i < listTextField.size(); i++) {
			ArrayList<JTextField> rowTextField = listTextField.get(i);
			int col = rowTextField.size();
			rowTextField.get(0).setBounds(0, i * 50, 80, 50);

			panel.add(rowTextField.get(0));
			for (int j = 1; j < rowTextField.size(); j++) {
				rowTextField.get(j).setBounds((j - 1) * (panel.getWidth() - 80) / (col - 1) + 80, i * 50,
						(panel.getWidth() - 80) / (col - 1), 50);
				panel.add(rowTextField.get(j));
			}

		}
		addDataFromBayesNet(listDataTextField);

	}

	/**
	 * set giá trị xác suất cho list data
	 * 
	 * @param listDataText
	 * @throws IOException
	 */
	private void addDataFromBayesNet(ArrayList<ArrayList<JTextField>> listDataText) {
		// lấy bảng xác suất tương ứng với node
		double[][] distribution = BayesNet.getDistribution(node);

		// gán các giá trị cho các textfield data
		for (int i = 0; i < distribution.length; i++) {
			for (int j = 0; j < distribution[i].length; j++) {
				JTextField tf = listDataText.get(i).get(j);
				tf.setText(distribution[i][j] + "");
				tf.setToolTipText(tf.getText());
			}
		}

	}

	/**
	 * lấy ra các giá trị tương ứng với nhãn của 1 node
	 * 
	 * @param nameNode:
	 *            nhãn node cần lấy giá trị
	 * @return
	 */
	public String[] getValue(String nameNode) {
		if (nameNode.equals("node7")) {
			return new String[] { "Very high", "high", "normal", "low", "very low" };
		} else if (nameNode.equals("node16")) {
			return new String[] { "very good", "good", "average", "poor", "very poor" };
		} else {
			return new String[] { "true", "false" };
		}
	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
