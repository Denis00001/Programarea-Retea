package org;
import javax.swing.*;

public class MainFrame extends JFrame{
    private JPanel mainPanel = new JPanel();
    private JLabel screenShot = new JLabel();

    public MainFrame(){
        setVisible(true);
        setSize(1366,768);
        add(mainPanel);
    }


    public void setPanel(byte[] image){
        try{
            mainPanel.add(screenShot);
            screenShot.setIcon(new ImageIcon(image));
        }
        catch(Exception ioe){
            System.out.println("Excpetion:"+ioe);
        }
    }

  /*  public static void main(String[] args) {
        MainFrame obj = new MainFrame();
    }
   */
}
