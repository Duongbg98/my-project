package gui.schedule;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.bayesserver.Network;

/**
 * panel vẽ resource với các task được phân
 * 
 * @author tuanl
 *
 */
public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int maxTime;
	private int time;
	private Color cl;
	private Network network;
	private ArrayList<ButtonTask> lisBtnTk;

	public Panel(int maxTime, Network network) {
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.cl = new Color((int) (Math.random() * 0x1000000));
		this.maxTime = maxTime;
		this.network = network;
		this.setLayout(null);
		this.lisBtnTk = new ArrayList<ButtonTask>();
	}

	public void createButton(int[] arrSL, List<String> listNameTask, int width, int height, double[] arrProb, int nameResource)
			throws IOException {

		this.time = this.getSize().width / (this.maxTime + 2);
		int start = (this.getSize().width - this.time * this.maxTime) / 2;

		// tạo button resource và add vào panel
		ButtonResource btnResource = new ButtonResource("R" + nameResource, network);
		btnResource.setForeground(Color.BLACK);
		btnResource.setBackground(cl);
		btnResource.setBounds(0, height / 4, start * 3 / 4, height / 2);
		this.add(btnResource);

		// tạo các button cho các Task
		int currentTime = 0;
		for (int i = 0; i < arrSL.length; i++) {
			if (arrSL[i] != 0) {
				ButtonTask btn = new ButtonTask(listNameTask.get(i), arrSL[i], arrProb[i]);
				btn.setForeground(Color.BLACK);
				btn.setBackground(cl);

				btn.setBounds(start + (currentTime) * this.time, height / 4, arrSL[i] * this.time, height / 2);
				currentTime += arrSL[i];
				this.add(btn);
				lisBtnTk.add(btn);

			}
		}
	}

	public void reloadData(double[] arrProb) {

		for (int i = 0; i < lisBtnTk.size(); i++) {
			lisBtnTk.get(i).reloadData(arrProb[i]);
		}
	}

	// public void paintComponent(Graphics g) {
	// super.paintComponent(g);
	// int height = getSize().height;
	// int width = getSize().width;
	// this.time = (int) (this.getSize().width / (maxTime + 2));
	//
	//// // draw horizontal rule at bottom (left to right)
	//// for (int x = 0; x < width; x++) {
	//// if (x % this.time == 0) {
	//// g.drawLine(x, height / 15 * 14, x, height);
	//// }
	//// }
	// }
}
