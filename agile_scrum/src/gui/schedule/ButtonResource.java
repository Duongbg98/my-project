package gui.schedule;

import java.io.IOException;

import javax.swing.JButton;

import com.bayesserver.Network;

import controller.ActionButtonResource;
import entity.Resource;
import gui.risk.RiskFrame;

public class ButtonResource extends JButton {
	private static final long serialVersionUID = 1L;
	private RiskFrame riskFrame;
	private Network network;
	private Resource resource;

	public ButtonResource(String label, Network net) throws IOException {
		this.network = net;
		this.setText(label);
		this.setBorder(null);

		this.addActionListener(new ActionButtonResource(this));
	}

	/**
	 * @return the riskFrame
	 */
	public RiskFrame getRiskFrame() {
		return riskFrame;
	}

	/**
	 * @param riskFrame
	 *            the riskFrame to set
	 */
	public void setRiskFrame(RiskFrame riskFrame) {
		this.riskFrame = riskFrame;
	}

	/**
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}

	/**
	 * @param network
	 *            the network to set
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
