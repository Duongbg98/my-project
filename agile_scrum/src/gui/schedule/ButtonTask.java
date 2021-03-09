package gui.schedule;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ButtonTask extends JButton {

	private static final long serialVersionUID = 1L;
	private String message;
	private String name;
	private int time;

	public ButtonTask(String name, int time, double prob) {
		this.name = name;
		this.time = time;
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setText(name);
		
		reloadData(prob);

		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msgbox(message);
			}
		});
	}

	/**
	 * Hiển thị thông báo khi click vào button
	 * 
	 * @param s:
	 *            chuỗi hiển thị
	 */
	private void msgbox(String s) {
		JOptionPane.showMessageDialog(null, s);
	}

	/**
	 * thay đổi message thông báo với xác suất truyền vào
	 * @param prob
	 */
	public void reloadData(double prob) {
		message = "Task ID: " + name + "\n";
		message += "Thời gian thực hiện: " + "" + this.time + "dvtg" + "\n";
		message += "Xác suất hoàn thành: " + "" + (prob * 100) + "%" + "\n";
	}
}
