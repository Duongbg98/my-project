package gui.risk;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;

import com.bayesserver.Node;

import controller.ActionButtonNode;

public class ButtonNode extends JButton {
	private static final long serialVersionUID = 1L;
	private String text;
	private int x1;
	private int width;
	private int y1;
	private int height;
	private volatile int screenX = 0;
	private volatile int screenY = 0;
	private volatile int myX = 0;
	private volatile int myY = 0;
	private ProbabilityTable probabilityTable;
	private Node node;
	private ArrayList<ButtonNode> listChildButton;

	public ButtonNode(Node node, int row, int col) {

		initiateButton(node, row, col, new Color(255, 252, 206));
		this.setForeground(Color.BLUE);

	}

	public ButtonNode(Node node, int row, int col, Boolean isSumNode, Boolean isTaskNode) {
		if (isSumNode) {
			initiateButton(node, row, col, new Color(255, 138, 62));
		}
		if (isTaskNode) {
			initiateButton(node, row, col, new Color(189, 238, 195));
		}
		this.setForeground(Color.BLUE);
	}

	/**
	 * Khởi tạo các giá trị cho ButtonNode
	 * 
	 * @param node:
	 *            Node gắn với button
	 * @param row:
	 * @param col
	 * @param color
	 * @throws IOException
	 */
	private void initiateButton(Node node, int row, int col, Color color) {
		this.text = node.getName();
		this.node = node;
		this.x1 = 10 + col * 150;
		this.y1 = 10 + row * 130;
		this.width = 130;
		this.height = 50;
		this.setBounds(x1, y1, width, height);
		this.setBorder(new RoundedBorder(width, text, color));
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		listChildButton = new ArrayList<ButtonNode>();
		addMouseListener(new ActionButtonNode(this));

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				int deltaX = e.getXOnScreen() - screenX;
				int deltaY = e.getYOnScreen() - screenY;
				setLocation(myX + deltaX, myY + deltaY);
			}

			public void mouseMoved(MouseEvent e) {
			}

		});
	}

	public int getCenterX() {
		return getX() + width / 2;
	}

	public int getCenterY() {
		return getY() + height / 2;
	}

	/**
	 * @return the screenX
	 */
	public int getScreenX() {
		return screenX;
	}

	/**
	 * @param screenX
	 *            the screenX to set
	 */
	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	/**
	 * @return the screenY
	 */
	public int getScreenY() {
		return screenY;
	}

	/**
	 * @param screenY
	 *            the screenY to set
	 */
	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	/**
	 * @param myX
	 *            the myX to set
	 */
	public void setMyX(int myX) {
		this.myX = myX;
	}

	/**
	 * @param myY
	 *            the myY to set
	 */
	public void setMyY(int myY) {
		this.myY = myY;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @return the probabilityTable
	 */
	public ProbabilityTable getProbabilityTable() {
		return probabilityTable;
	}

	/**
	 * @param probabilityTable
	 *            the probabilityTable to set
	 */
	public void setProbabilityTable(ProbabilityTable probabilityTable) {
		this.probabilityTable = probabilityTable;
	}

	/**
	 * @return the listChildButton
	 */
	public ArrayList<ButtonNode> getListChildButton() {
		return listChildButton;
	}


}

