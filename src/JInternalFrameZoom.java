import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;


public class JInternalFrameZoom extends JInternalFrame {
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JInternalFrameZoom frame = new JInternalFrameZoom();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JInternalFrameZoom() {
		setBounds(200, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(93, 29, 221, 199);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(103, 31, 86, 20);
		panel.add(textField);
		
		JLabel label = new JLabel("Last Name : ");
		label.setBounds(10, 62, 83, 14);
		panel.add(label);
		
		JLabel label_1 = new JLabel("First Name : ");
		label_1.setBounds(10, 34, 83, 14);
		panel.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(103, 59, 86, 20);
		panel.add(textField_1);
		
		JButton button = new JButton("Submit");
		button.setBounds(56, 116, 89, 23);
		panel.add(button);
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
		//ScalableLayoutUtils.installScalableLayoutRecursive(new DefaultScalableLayoutRegistry(), getContentPane());

	}
}
