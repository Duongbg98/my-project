package gui.risk;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.Border;

public class RoundedBorder implements Border {

	private int radius;
	private String text;
	private Color color;
	
	RoundedBorder(int radius, String text, Color color) {
		this.radius = radius;
		this.text = text;
		this.color = color;
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(0, 0, 0, 0);
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(3F));
		g2D.setColor(Color.BLACK);
		g2D.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
		g2D.setColor(color);
		g2D.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
		g2D.setColor(Color.BLUE);
		Font myFont = new Font("Arial", Font.PLAIN, 12);
		g2D.setFont(myFont);
		int widthString = g.getFontMetrics().stringWidth(text);
		g2D.drawString(text, x + width / 2 - widthString / 2, y + height / 2);
	}
}
