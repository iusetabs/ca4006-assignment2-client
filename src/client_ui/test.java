package client_ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test {
    private JPanel mainPanel;
    private JButton TESTButton;
    private JButton ROOMAVAILABILITYButton;
    private JButton NEWROOMButton;
    private JButton VIEWROOMSButton;
    private JButton TIMETABLEButton;
    private JButton NEWBOOKINGButton;
    private JButton ENTERButton;
    private JPanel butttonPanel;
    private JPanel outputPanel;
    private JPanel paramPanel;

    public test() {
    }

    public static void main(String [] args){
        JFrame frame = new JFrame("app");
        frame.setContentPane(new test().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
