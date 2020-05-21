package org;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


class UserGUI {

    UserGUI(){
        JFrame f=new JFrame("Client GUI");
        JButton b=new JButton("Start");
        b.setBounds(110,150,130, 40);
        JLabel label = new JLabel();
        label.setText("Enter address :");
        label.setBounds(10, 15, 100, 100);
        JLabel labelport = new JLabel();
        labelport.setText("Enter port :");
        labelport.setBounds(10, 55, 100, 100);
        final JLabel label1 = new JLabel();
        label1.setBounds(10, 200, 200, 100);
        final JTextField textfield= new JTextField();
        textfield.setBounds(110, 50, 130, 30);
        final JTextField textfield1= new JTextField();
        textfield1.setBounds(110, 90, 130, 30);
        f.add(label1);
        f.add(textfield);
        f.add(label);
        f.add(labelport);
        f.add(b);
        f.add(textfield1);
        f.setSize(300,300);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String site = textfield.getText();
                String portnumber = textfield1.getText();
                int portnumberInt = Integer.parseInt(portnumber);
                label1.setText("The Desktop sharing is on");
                Client re = new Client(site,portnumberInt);
                re.sending.start();
            }
        });
    }


    public static void main(String[] args) {
        new UserGUI();
    }
}
