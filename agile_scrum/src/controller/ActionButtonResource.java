
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.risk.RiskFrame;
import gui.schedule.ButtonResource;

/**
 * @author tuanl
 *
 */
public class ActionButtonResource implements ActionListener {
	private ButtonResource buttonResource;

	public ActionButtonResource(ButtonResource buttonResource) {
		this.buttonResource = buttonResource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (buttonResource.getRiskFrame() == null) {
			buttonResource.setRiskFrame(new RiskFrame(buttonResource.getNetwork()));
			buttonResource.getRiskFrame().setVisible(false);
		}
		if (buttonResource.getRiskFrame().isVisible()) {
			buttonResource.getRiskFrame().setVisible(false);
		} else {
			buttonResource.getRiskFrame().setVisible(true);
		}

	}

}
