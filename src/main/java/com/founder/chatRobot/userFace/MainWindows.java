package com.founder.chatRobot.userFace;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.controler.InternalControler;
import com.founder.chatRobot.controler.common.Controler;

import java.awt.Color;

import javax.swing.ScrollPaneConstants;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Toolkit;

public class MainWindows {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindows window = new MainWindows();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public MainWindows() {
		initialize();
	}

	private void tread(JTextPane display, JTextArea input, Controler controler,
			String userId) {
		String inputInfo, outputInfo = null;
		int length, displayLength;

		length = input.getAccessibleContext().getAccessibleEditableText()
				.getCharCount();
		inputInfo = input.getAccessibleContext().getAccessibleEditableText()
				.getTextRange(0, length);

		List<ResultBean> beanList = controler.tread(inputInfo, userId, userId,
				"上海");

		Color color = Color.BLACK;

		Document document = display.getDocument();

		try {
			StyleContext sc = StyleContext.getDefaultStyleContext();
			AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
					StyleConstants.Foreground, color);

			Font font = new Font("华文仿宋", Font.PLAIN, 20);
			aset = sc.addAttribute(aset, StyleConstants.Family,
					font.getFamily());
			aset = sc.addAttribute(aset, StyleConstants.FontSize, 20);

			input.getAccessibleContext().getAccessibleEditableText()
					.delete(0, length);
			input.getAccessibleContext().getAccessibleEditableText()
					.delete(0, length);
			displayLength = display.getAccessibleContext()
					.getAccessibleEditableText().getCharCount();
			inputInfo = "用户：	" + inputInfo + "\n";

			document.insertString(displayLength, inputInfo.trim(), aset);
		} catch (BadLocationException e1) {

			// TODO Auto-generated catch block

			e1.printStackTrace();

		}

		for (ResultBean bean : beanList) {
			outputInfo = bean.getAnswer();

			color = Color.RED;

			document = display.getDocument();

			try {
				StyleContext sc = StyleContext.getDefaultStyleContext();
				AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
						StyleConstants.Foreground, color);

				Font font = new Font("华文仿宋", Font.PLAIN, 20);
				aset = sc.addAttribute(aset, StyleConstants.Family,
						font.getFamily());
				aset = sc.addAttribute(aset, StyleConstants.FontSize, 20);

				String splitInfo = "";

				String[] splitArray = outputInfo.split("\n");

				for (String tmp : splitArray) {
					if (tmp.length() > 37) {
						splitInfo += tmp.substring(0, 37) + "\n";
						tmp = tmp.substring(37);
						while (tmp.length() > 38) {
							splitInfo += tmp.substring(0, 40) + "\n";
							tmp = tmp.substring(40);
						}
						splitInfo += tmp + "\n";
					} else {
						splitInfo += tmp + "\n";
					}

				}

				outputInfo = "\n客服：" + splitInfo + "\n";
				displayLength = display.getAccessibleContext()
						.getAccessibleEditableText().getCharCount();

				document.insertString(displayLength, outputInfo, aset);
			} catch (BadLocationException e1) {

				// TODO Auto-generated catch block

				e1.printStackTrace();

			}
		}

		/*
		 * JScrollBar sBar = scrollPane.getVerticalScrollBar();
		 * sBar.setValue(sBar.getMaximum());
		 */
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setFont(new Font("方正舒体", Font.PLAIN, 20));
		try {
			URL url = MainWindows.class.getResource("/title.png");
			if (url == null) {
				File f = new File("title.png");

				url = f.toURI().toURL();

			}

			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		// frame.setUndecorated(true);
		frame.getContentPane().setFont(new Font("华文仿宋", Font.PLAIN, 20));
		frame.setBounds(100, 100, 862, 611);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Controler controler = new InternalControler();
		controler.init();

		// 设置背景图片
		((JPanel) this.frame.getContentPane()).setOpaque(false);

		try {
			URL url = MainWindows.class.getResource("/backup.png");
			if (url == null) {
				File f = new File("backup.png");

				url = f.toURI().toURL();

			}

			ImageIcon img = new ImageIcon(url);
			JLabel background = new JLabel(img);
			this.frame.getLayeredPane().add(background,
					new Integer(Integer.MIN_VALUE));
			background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Blue hills.jpg这个图片的位置要跟当前这个类是同一个包下
		// URL url = MainWindows.class.getResource("\\backup.jpg");

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel panel = new JPanel(new BorderLayout());
		scrollPane.setViewportView(panel);

		final JTextPane display = new JTextPane();
		display.setEditable(false);
		panel.add(display, BorderLayout.CENTER);

		int height = 10;
		Point p = new Point();
		p.setLocation(0, display.getHeight() * height);
		scrollPane.getViewport().setViewPosition(p);

		final JTextArea userId = new JTextArea();
		userId.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		userId.setFont(new Font("华文仿宋", Font.PLAIN, 20));
		userId.setText("111");

		final JTextArea input = new JTextArea();
		input.setFont(new Font("华文仿宋", Font.PLAIN, 20));
		input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					int length = userId.getAccessibleContext()
							.getAccessibleEditableText().getCharCount();
					String inputInfo = userId.getAccessibleContext()
							.getAccessibleEditableText()
							.getTextRange(0, length);
					tread(display, input, controler, inputInfo);
				}
			}
		});

		JButton button = new JButton("");
		try {
			URL url = MainWindows.class.getResource("/input.png");
			if (url == null) {
				File f = new File("input.png");

				url = f.toURI().toURL();

			}

			button.setIcon(new ImageIcon(url));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int length = userId.getAccessibleContext()
						.getAccessibleEditableText().getCharCount();
				String inputInfo = userId.getAccessibleContext()
						.getAccessibleEditableText().getTextRange(0, length);

				tread(display, input, controler, inputInfo);
			}
		});

		JButton button_2 = new JButton("");
		try {
			URL url = MainWindows.class.getResource("/quit.png");
			if (url == null) {
				File f = new File("quit.png");

				url = f.toURI().toURL();

			}

			button_2.setIcon(new ImageIcon(url));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		 * button_2.setIcon(new ImageIcon(InternalControler.FINAL_DATA_PATH +
		 * "quit.png"));
		 */
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});

		JLabel lblid = new JLabel("\u7528\u6237ID");
		lblid.setFont(new Font("华文仿宋", Font.PLAIN, 20));

		JLabel label = new JLabel("\u8F93\u5165");
		label.setFont(new Font("华文仿宋", Font.PLAIN, 20));

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																scrollPane,
																GroupLayout.PREFERRED_SIZE,
																829,
																GroupLayout.PREFERRED_SIZE)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				lblid,
																				GroupLayout.PREFERRED_SIZE,
																				70,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				userId,
																				GroupLayout.PREFERRED_SIZE,
																				45,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				label,
																				GroupLayout.PREFERRED_SIZE,
																				54,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				input,
																				GroupLayout.DEFAULT_SIZE,
																				532,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				button,
																				GroupLayout.PREFERRED_SIZE,
																				59,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				button_2,
																				GroupLayout.PREFERRED_SIZE,
																				56,
																				GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap(47, Short.MAX_VALUE)
										.addComponent(scrollPane,
												GroupLayout.PREFERRED_SIZE,
												477, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createParallelGroup(
																				Alignment.BASELINE)
																		.addComponent(
																				lblid)
																		.addComponent(
																				userId,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				label)
																		.addComponent(
																				input,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																button,
																GroupLayout.PREFERRED_SIZE,
																29,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																button_2,
																GroupLayout.PREFERRED_SIZE,
																29,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		frame.getContentPane().setLayout(groupLayout);
		frame.getContentPane().setFocusTraversalPolicy(
				new FocusTraversalOnArray(new Component[] { input, button }));

	}
}
