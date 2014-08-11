package colorviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

public class ColorViewer extends JFrame {

	/**
	 * Serial version ID;
	 */
	private static final long serialVersionUID = -6773008384764509775L;

	private static final byte RGB = 1;
	private static final byte HEX = 2;

	private static final String title = "Color Viewer by @fuadsaud";

	private static final Object helpText = "Type a HEX code or RGB values to see the respective color.\nHEX accepted formats are:\n> ffffff\n> fff\n> #ffffff\n> #fff\nRGB accepted formats are:\n> 255,255,255\nStatus bar show the HEX and RGB code to the current color\nIf you enter RGB values, window's title shows he HEX code and vice-versa";

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new ColorViewer().setVisible(true);
	}

	private final JLabel hex = new JLabel("HEX:");
	private final JLabel rgb = new JLabel("RGB:");

	private JTextField textField;

	private JButton help;
	private JButton go;

	private JPanel controls;
	private JPanel color;
	private JPanel main;

	private JLabel hexValue;
	private JLabel rgbValue;

	private byte mode;

	public ColorViewer() {
		super(title);

		color = new JPanel();

		initControls();

		initMain();

		add(main);
		setSize(555, 555);
		setMinimumSize(new Dimension(300, 300));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/resources/colorviewer-icon256.png")));
	}

	@SuppressWarnings("unused")
	private boolean containsLettersHEXDigits(String text) {
		text = text.toLowerCase();
		if (text.contains("a") || text.contains("b") || text.contains("c") || text.contains("d")
				|| text.contains("e") || text.contains("f")) {
			return true;
		} else {
			return false;
		}
	}

	private void decode() {
		try {
			String text = textField.getText();
			String rgbCode, hexCode;

			if (!text.isEmpty()) {
				defineCodeType(text);
				Color c = null;
				switch (mode) {
				case HEX:
					c = decodeHEX(text);
					break;
				case RGB:
					c = decodeRGB(text);
					break;
				}

				hexCode = getHEXCode(c);
				rgbCode = getRGBCode(c);

				color.setBackground(c);
				color.repaint();

				setTitle((mode == RGB ? hexCode : rgbCode) + " - " + title);
				hexValue.setText(hexCode);
				rgbValue.setText(rgbCode);

				textField.setSelectionStart(0);
				textField.setSelectionEnd(text.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private Color decodeHEX(String text) {
		if (!text.startsWith("#")) {
			text = "#" + text;
		}

		if (text.length() == 4) {
			StringBuilder sb = new StringBuilder("#");
			for (int i = 1; i < text.length(); i++) {
				sb.append(text.charAt(i));
				sb.append(text.charAt(i));
			}
			text = sb.toString();
		}

		return Color.decode(text);
	}

	private Color decodeRGB(String text) {
		String[] rgb = text.split(",");

		if (rgb.length < 3) {
			throw new IllegalArgumentException("String must be on format: r,g,b");
		}

		for (int i = 0; i < 3; i++) {
			rgb[i] = rgb[i].replaceAll(" ", "");
		}

		int r = Integer.parseInt(rgb[0]);
		int g = Integer.parseInt(rgb[1]);
		int b = Integer.parseInt(rgb[2]);

		Color c = new Color(r, g, b);
		return c;
	}

	private void defineCodeType(String text) {
		if (text.contains(","))
			mode = RGB;
		else
			mode = HEX;
	}

	private String getHEXCode(Color c) {
		String[] rgb = { Integer.toHexString(c.getRed()), Integer.toHexString(c.getGreen()),
				Integer.toHexString(c.getBlue()) };
		for (int i = 0; i < rgb.length; i++) {
			System.out.println(rgb[i].length());
			if (rgb[i].length() == 1) {
				rgb[i] = '0' + rgb[i];
			}
		}

		return "#" + rgb[0] + rgb[1] + rgb[2];
	}

	private String getRGBCode(Color c) {
		return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
	}

	private void initControls() {
		controls = new JPanel(new MigLayout(new LC().fillX().noGrid()));

		textField = new JTextField();
		help = new JButton("Help");
		go = new JButton("Go!");
		rgbValue = new JLabel("");
		hexValue = new JLabel("");

		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				decode();
			}
		});

		help.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(main, helpText, "Color Viewer Help",
						JOptionPane.PLAIN_MESSAGE);
			}
		});

		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					decode();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}
		});

		controls.add(textField, "grow");
		controls.add(help, "wrap");
		// controls.add(hex);
		// controls.add(rgb, "wrap");
		controls.add(go, "grow, wrap");
		controls.add(hex);
		controls.add(hexValue);
		controls.add(new JLabel("            "), "align center");
		controls.add(rgb);
		controls.add(rgbValue);
	}

	private void initMain() {
		main = new JPanel(new BorderLayout());

		main.add(color);
		main.add(controls, BorderLayout.SOUTH);
	}
}
