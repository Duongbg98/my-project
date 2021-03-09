
package gui.schedule;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import entity.Resource;
import model.BayesNet;

/**
 * @author tuanl
 *
 */
public class InforResource extends JFrame {

	private static final long serialVersionUID = 1L;
	private Resource resource = null;
	private static Resource resourceTemp = new Resource();
	private List<Resource> listResource = null;
	private JComboBox<String> cmbDaily = null;
	private JComboBox<String> cmbWorkSkill = null;
	private JComboBox<String> cmbAgileSkill = null;
	private JComboBox<String> cmbAgileExp = null;
	private JComboBox<String> cmbResource = null;
	private boolean isDisplay = true;

	public InforResource(BayesNet net) {

		this.listResource = net.getResources();
		this.resource = listResource.get(0);
		this.setSize(600, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.initComponent(listResource.size());

		this.setVisible(true);
	}

	public void initComponent(int numResource) {
		JLabel label = new JLabel("Thông tin nguồn lực");
		label.setFont(new Font("Tahoma", Font.BOLD, 20));
		label.setBounds(220, 20, 250, 25);
		this.getContentPane().add(label);

		JLabel lblResource = new JLabel("Resource");
		lblResource.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblResource.setBounds(120, 60, 200, 20);
		this.getContentPane().add(lblResource);

		cmbResource = new JComboBox<String>();
		for (int i = 1; i <= numResource; i++) {
			cmbResource.addItem("Resource " + i);
		}
		cmbResource.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cmbResource.setBounds(350, 60, 95, 20);
		this.getContentPane().add(cmbResource);

		cmbResource.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int indexSelect = cmbResource.getSelectedIndex();
				resource = listResource.get(indexSelect);
				cmbAgileExp.setSelectedItem(resource.getAgileExperience());
				cmbAgileSkill.setSelectedIndex(resource.getAgileSkill() - 1);
				cmbWorkSkill.setSelectedIndex(resource.getSkillLevel() - 1);
				cmbDaily.setSelectedItem(resource.getDailyMeeting());
			}
		});

		JLabel lblAgileExp = new JLabel("Kinh nghiệm quản lý agile");
		lblAgileExp.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAgileExp.setBounds(120, 90, 200, 20);
		this.getContentPane().add(lblAgileExp);

		cmbAgileExp = new JComboBox<String>();
		cmbAgileExp.addItem("yes");
		cmbAgileExp.addItem("no");
		cmbAgileExp.setSelectedItem(resource.getAgileExperience());
		cmbAgileExp.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cmbAgileExp.setBounds(350, 90, 95, 20);
		this.getContentPane().add(cmbAgileExp);

		JLabel lblAgileSkill = new JLabel("Kỹ năng agile");
		lblAgileSkill.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAgileSkill.setBounds(120, 120, 95, 20);
		this.getContentPane().add(lblAgileSkill);

		cmbAgileSkill = new JComboBox<String>();
		for (int i = 1; i <= 5; i++) {
			cmbAgileSkill.addItem("Level " + i);
		}
		cmbAgileSkill.setSelectedItem("Level " + resource.getAgileSkill());
		cmbAgileSkill.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cmbAgileSkill.setBounds(350, 120, 95, 20);
		this.getContentPane().add(cmbAgileSkill);

		JLabel lblWorkSkill = new JLabel("Kỹ năng công việc");
		lblWorkSkill.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblWorkSkill.setBounds(120, 150, 200, 20);
		this.getContentPane().add(lblWorkSkill);

		cmbWorkSkill = new JComboBox<String>();
		for (int i = 1; i <= 5; i++) {
			cmbWorkSkill.addItem("Level " + i);
		}
		cmbWorkSkill.setSelectedItem("Level " + resource.getSkillLevel());
		cmbWorkSkill.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cmbWorkSkill.setBounds(350, 150, 95, 20);
		this.getContentPane().add(cmbWorkSkill);

		JLabel lblDaily = new JLabel("Tham gia họp daily");
		lblDaily.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDaily.setBounds(120, 180, 200, 20);
		this.getContentPane().add(lblDaily);

		cmbDaily = new JComboBox<String>();
		cmbDaily.addItem("yes");
		cmbDaily.addItem("no");
		cmbDaily.setSelectedItem(resource.getDailyMeeting());
		cmbDaily.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cmbDaily.setBounds(350, 180, 95, 20);
		this.getContentPane().add(cmbDaily);

		JButton btnChange = new JButton("Cập nhật");
		btnChange.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnChange.setBounds(160, 230, 100, 30);
		this.getContentPane().add(btnChange);

		btnChange.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				resourceTemp.setAgileExperience(resource.getAgileExperience());
				resourceTemp.setAgileSkill(resource.getAgileSkill());
				resourceTemp.setSkillLevel(resource.getSkillLevel());
				resourceTemp.setDailyMeeting(resource.getDailyMeeting());
				resourceTemp.setID(resource.getID());
				String agileExp = (String) cmbAgileExp.getSelectedItem();
				String agileSkill = (String) cmbAgileSkill.getSelectedItem();
				String workSkill = (String) cmbWorkSkill.getSelectedItem();
				String daily = (String) cmbDaily.getSelectedItem();
				resource.setAgileExperience(agileExp);
				resource.setAgileSkill(Integer.parseInt(agileSkill.substring(6)));
				resource.setSkillLevel(Integer.parseInt(workSkill.substring(6)));
				resource.setDailyMeeting(daily);
				JOptionPane.showMessageDialog(null,
						"Bạn vừa thay đổi thông tin về nguồn lực " + (cmbResource.getSelectedIndex() + 1)
								+ ". Hãy quay lại màn hình schedule để theo dõi kết quả thực hiện các "
								+ "công việc của nguồn lực này.");
				isDisplay = false;
				display();

			}
		});

		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnReset.setBounds(300, 230, 100, 30);
		this.getContentPane().add(btnReset);

		btnReset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				resource.setAgileExperience(resourceTemp.getAgileExperience());
				resource.setAgileSkill(resourceTemp.getAgileSkill());
				resource.setSkillLevel(resourceTemp.getSkillLevel());
				resource.setDailyMeeting(resourceTemp.getDailyMeeting());
				if (cmbResource.getSelectedIndex() == resourceTemp.getID() - 1) {
					cmbDaily.setSelectedItem(resourceTemp.getDailyMeeting());
					cmbWorkSkill.setSelectedItem("Level " + resource.getSkillLevel());
					cmbAgileSkill.setSelectedItem("Level " + resource.getAgileSkill());
					cmbAgileExp.setSelectedItem(resource.getAgileExperience());
				}
				FrameAgile.reLoad();

			}
		});

	}

	private void display() {
		if (!this.isDisplay) {
			this.dispose();
		}
	}
}
