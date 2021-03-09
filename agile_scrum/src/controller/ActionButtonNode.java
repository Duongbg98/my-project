
package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gui.risk.ButtonNode;
import gui.risk.ProbabilityTable;


/**
 * @author tuanl
 *
 */
public class ActionButtonNode implements MouseListener {

	private ButtonNode buttonNode;

	public ActionButtonNode(ButtonNode buttonNode) {
		this.buttonNode = buttonNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (buttonNode.getProbabilityTable() == null) {
			buttonNode.setProbabilityTable(new ProbabilityTable(buttonNode.getNode()));
			buttonNode.getProbabilityTable().setVisible(false);
		}

		if (buttonNode.getProbabilityTable().isVisible()) {
			buttonNode.getProbabilityTable().setVisible(false);
		} else {
			buttonNode.getProbabilityTable().setVisible(true);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		buttonNode.setScreenX(e.getXOnScreen());
		buttonNode.setScreenY(e.getYOnScreen());

		buttonNode.setMyX(buttonNode.getX());
		buttonNode.setMyY(buttonNode.getY());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}