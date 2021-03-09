package gui.risk;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.bayesserver.Network;

public class RiskFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;

	public RiskFrame(Network net) {
		this.setTitle("Bayes Network");
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(250, 0);
		this.setLayout(null);
		this.setVisible(true);

		this.setContentPane(this.createPanel(net));

		this.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				int height = getHeight();
				int width = getWidth();
				scrollPane.setBounds(0, 0, width - 15, height - 50);
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
		this.pack();

		this.setVisible(true);
	}

	/**
	 * tạo panel chứa mạng bayes
	 * 
	 * @param net:
	 *            Mạng bayes truy�?n vào
	 * @return
	 */
	private JPanel createPanel(Network net) {
		JPanel contentPane = new JPanel(null);
		// tạo panel vẽ mạng bayes
		RiskPanel panel = new RiskPanel();
		panel.createButtons(net.getNodes(), net.getLinks());

		// add panel rủi ro vào scroll để có thể kéo
		scrollPane = new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(0, 0, 900, 900);

		contentPane.setPreferredSize(new Dimension(900, 900));
		contentPane.add(scrollPane);
		return contentPane;
	}

}
