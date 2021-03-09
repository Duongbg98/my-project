package gui.risk;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.bayesserver.Link;
import com.bayesserver.NetworkLinkCollection;
import com.bayesserver.NetworkNodeCollection;

public class RiskPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ArrayList<ButtonNode> listButton;

	public RiskPanel() {
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		listButton = new ArrayList<ButtonNode>();
		this.setPreferredSize(new Dimension(1000, 1600));
		this.setLayout(null);
		// this.setOpaque(false);
		Timer timer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				revalidate();
				repaint();
			}
		});
		timer.start();
	}

	public ArrayList<ButtonNode> getListButton() {
		return listButton;
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setStroke(new BasicStroke(1F));
		g2d.setColor(Color.BLACK);

		for (int i = 0; i < listButton.size(); i++) {
			ArrayList<ButtonNode> listChild = listButton.get(i).getListChildButton();
			// tọa độ nút cha
			int x1 = listButton.get(i).getCenterX();
			int y1 = listButton.get(i).getCenterY();

			for (int j = 0; j < listChild.size(); j++) {

				// tọa độ nút con
				int x2 = listChild.get(j).getCenterX();
				int y2 = listChild.get(j).getCenterY();

				int[] xy1 = findGiaoDiem(x1, y1, x2, y2);
				int[] xy2 = findGiaoDiem(x2, y2, x1, y1);

				g2d.setColor(Color.BLACK);
				g2d.drawLine(xy1[0], xy1[1], xy2[0], xy2[1]);
				g2d.setColor(Color.BLUE);
				g2d.drawOval(xy2[0], xy2[1], 5, 5);
				g2d.fillOval(xy2[0], xy2[1], 5, 5);
			}
		}

	}

	/**
	 * tìm vị trí ở ngoài đường elip để vẽ đường link nối 2 điểm
	 * 
	 * @param x1
	 *            tọa độ x điểm đang xét
	 * @param y1
	 *            tọa độ y điểm đang xét
	 * @param x2:
	 *            tọa độ x điểm kia của đoạn thẳng
	 * @param y2:
	 *            : tọa độ y điểm kia của đoạn thẳng
	 * @return
	 */
	public int[] findGiaoDiem(int x1, int y1, int x2, int y2) {
		if (Math.abs(y1 - y2) > Math.abs(x1 - x2)) {
			if (y1 < y2) {
				for (int i = 0; i < y2 - y1; i++) {
					int y0 = y1 + i;
					int x0 = (((y2 - y1) * x1 + (x1 - x2) * y1) - y0 * (x1 - x2)) / (y2 - y1);
					if ((x0 - x1) * (x0 - x1) / (50 * 50) + (y0 - y1) * (y0 - y1) / (20 * 20) > 1) {
						return new int[] { x0, y0 };
					}
				}
			} else {
				for (int i = 0; i < y1 - y2; i++) {
					int y0 = y1 - i;
					int x0 = (((y2 - y1) * x1 + (x1 - x2) * y1) - y0 * (x1 - x2)) / (y2 - y1);
					if ((x0 - x1) * (x0 - x1) / (50 * 50) + (y0 - y1) * (y0 - y1) / (20 * 20) > 1) {
						return new int[] { x0, y0 };
					}
				}
			}
		} else {
			if (x1 < x2) {
				for (int i = 0; i < x2 - x1; i++) {
					int x0 = x1 + i;
					int y0 = ((y2 - y1) * x1 + (x1 - x2) * y1 - x0 * (y2 - y1)) / (x1 - x2);
					if ((x0 - x1) * (x0 - x1) / (50 * 50) + (y0 - y1) * (y0 - y1) / (20 * 20) > 1) {

						return new int[] { x0, y0 };
					}
				}
			} else {
				for (int i = 0; i < x1 - x2; i++) {
					int x0 = x1 - i;
					int y0 = ((y2 - y1) * x1 + (x1 - x2) * y1 - x0 * (y2 - y1)) / (x1 - x2);
					if ((x0 - x1) * (x0 - x1) / (50 * 50) + (y0 - y1) * (y0 - y1) / (20 * 20) > 1) {

						return new int[] { x0, y0 };
					}
				}
			}
		}
		return new int[] { x1, y1 };
	}

	/**
	 * tạo các node trong mạng bayes để vẽ lên giao diện
	 * 
	 * @param listNode:
	 *            danh sách các node trong mạng
	 * @param listLink:
	 *            danh sách các link trong mạng
	 */
	public void createButtons(NetworkNodeCollection listNode, NetworkLinkCollection listLink) {
		int[][] arrLocation = { { 0, 0 }, { 3, 0 }, { 2, 3 }, { 4, 2 }, { 3, 2 }, { 2, 2 }, { 5, 1 }, { 6, 2 },
				{ 4, 3 }, { 4, 1 }, { 1, 5 }, { 3, 4 }, { 4, 5 }, { 5, 4 }, { 6, 0 }, { 3, 5 }, { 6, 4 }, { 5, 3 },
				{ 1, 1 }, { 7, 3 } };
		ButtonNode node = null;
		// tạo các node và thêm để vẽ lên giao diện

		// tạo và thêm các node rủi ro
		for (int i = 0; i < arrLocation.length - 1; i++) {
			node = new ButtonNode(listNode.get(i), arrLocation[i][0], arrLocation[i][1]);
			listButton.add(node);
			this.add(node);
		}

		// thêm node tổng hợp các rủi ro
		node = new ButtonNode(listNode.get(19), arrLocation[arrLocation.length - 1][0],
				arrLocation[arrLocation.length - 1][1], true, false);
		listButton.add(node);
		this.add(node);

		// tạo và thêm các node task
		for (int i = 20; i < listNode.size(); i++) {
			int row = 8 + ((i - 20) / 6);
			int col = (i - 20) % 6;
			node = new ButtonNode(listNode.get(i), row, col, false, true);
			listButton.add(node);
			this.add(node);
		}

		// dựa vào các link để thêm các node con vào node cha
		int numLinks = listLink.size();
		int indexParent, indexChild;
		for (int i = 0; i < numLinks; i++) {
			Link link = listLink.get(i);
			// lấy tên node cha từ link
			String parent = link.getFrom().getName();
			// lấy tên node con từ link
			String child = link.getTo().getName();

			// lấy thứ tự của node
			indexParent = convertNameToIndex(parent, listNode.size());
			indexChild = convertNameToIndex(child, listNode.size());

			// thêm node con vào list các node con của 1 node từ link
			listButton.get(indexParent).getListChildButton().add(listButton.get(indexChild));
		}
	}

	/**
	 * Chuyển nhãn sang số thứ tự
	 * 
	 * @param nameNode:
	 *            nhãn của node
	 * @param size:
	 *            số lượng node
	 * @return
	 */
	private int convertNameToIndex(String nameNode, int size) {
		// các node rủi ro thì index là số thự tự -1
		if (nameNode.startsWith("node")) {
			return (Integer.parseInt(nameNode.substring(4)) - 1);
		}

		// nếu là các node task thì index là số thứ tự trên node + 20
		if (nameNode.startsWith("task")) {
			return Integer.parseInt(nameNode.substring(4)) + 20;
		}
		// nếu là node rủi ro thì index là 19
		if (nameNode.startsWith("risk")) {
			return 19;
		}
		// nếu là task cuối hoặc các tên khác thì là index cuối cùng
		return size - 1;
	}
}