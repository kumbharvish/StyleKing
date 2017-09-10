import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class TestJDialog extends JFrame implements ActionListener
{
private JLabel l;

public TestJDialog(String title)
{
    super(title);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.setLayout(new GridLayout(0,1));
    JButton  b = new JButton("Input Dialog");
    b.addActionListener(this);
    this.add(b);

    l = new JLabel();
    this.add(l);

    setSize(300, 100);
    setVisible(true);
}

public void actionPerformed(ActionEvent evt)
{
    String s = evt.getActionCommand();
    String input = JOptionPane.showInputDialog(this,
                                               "Saisissez votre mot de passé:",
                                               s,
                                               JOptionPane.QUESTION_MESSAGE);
    l.setText("Mot passé: " + input);
}

public static void main(String[] args)
{
    new TestJDialog("Example");
}
}