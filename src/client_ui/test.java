package client_ui;

import client_code.MessengerService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private JTextField dF1;
    private JTextField dF2;
    private JTextField dF3;
    private JTextField dF4;
    private JTextArea outputArea;
    private JLabel t_dF1;
    private JLabel t_dF2;
    private JLabel t_dF3;
    private JLabel t_dF4;
    public MessengerService ms;
    private Calendar cal;
    private SimpleDateFormat sdf;
    private boolean timetable = false;

    public test() {
        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm:ss");
        paramPanel.setVisible(false);
        ms  = new MessengerService(1, "http://10.216.35.189:8080");

        ActionListener listener = e -> {
            if (e.getSource().equals(TIMETABLEButton)){
                t_dF1.setText("Room name");
                t_dF2.setText("Start day");
                t_dF3.setText("End day");
                dF4.setVisible(false);
                t_dF4.setVisible(false);
                afterBtn();
                timetable = true;
                outputArea.append(t() + "....Please enter room, start day and end day.\n");
            }
            else if (e.getSource().equals(ROOMAVAILABILITYButton)) {
                afterBtn();            }
            else if (e.getSource().equals(NEWROOMButton)){
                afterBtn();            }
            else if (e.getSource().equals(VIEWROOMSButton)) {
                outputArea.append(t() + "Getting listing of current rooms\n");
                paramPanel.setVisible(false);
                ArrayList<String> a = ms.get_request("/rooms");
                if (a.isEmpty()){
                    outputArea.append(t() + "No rooms found!\n");
                }
                else{
                    for (String s : a) {
                        Map<String, Object> m = ms.jsonToMap(s);
                        outputArea.append(t() + "Room " + m.get("name") + " has a capacity of " + m.get("capacity")  + ".\n");
                    }
                    outputArea.append("\n");
                }
            }
            else if (e.getSource().equals(NEWBOOKINGButton)){
                afterBtn();            }
            else if (e.getSource().equals(ENTERButton)){
                if(timetable){
                        String rN = dF1.getText().strip();
                        int sD = Integer.parseInt(dF2.getText().strip());
                        int eD = Integer.parseInt(dF3.getText().strip());
                        ArrayList<String> a = ms.get_timetable(rN, sD, eD);
                    if (a.isEmpty()){
                        outputArea.append("No timetable found!");
                    }
                    else{
                        for (String s : a) {
                            System.out.println(s);
                            Map<String, Object> m = ms.jsonToMap(s);
                            ArrayList<String> arranged_keySet = arrangeKeySet(m);
                            //outputArea.append(t() + "Room " + m.get("name") + " has a capacity of " + m.get("capacity")  + ".\n");
                        }
                        outputArea.append("\n");
                    }
                    timetable = false;
                    dF4.setVisible(true);
                    t_dF4.setVisible(true);
                    clearText();
                }
            }
            else if (e.getSource().equals(TESTButton)) {
                afterBtn();
            }
        };
        TIMETABLEButton.addActionListener(listener);
        ROOMAVAILABILITYButton.addActionListener(listener);
        NEWROOMButton.addActionListener(listener);
        VIEWROOMSButton.addActionListener(listener);
        NEWBOOKINGButton.addActionListener(listener);
        ENTERButton.addActionListener(listener);
        TESTButton.addActionListener(listener);
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

    private void afterBtn(){
        paramPanel.setVisible(true);
    }
    private void clearText(){
        dF1.setText("");
        dF2.setText("");
        dF3.setText("");
        dF4.setText("");
        paramPanel.setVisible(false);
    }

    private String t(){
        return sdf.format(cal.getTime()) +":\t";
    }

    private ArrayList<String> arrangeKeySet( Map<String, Object> m){
        ArrayList<String> a = new ArrayList<>();

        for (String k : m.keySet()){
            a.add(m.get(k).toString());
        }
        Collections.sort(a);
        return a;
    }
}
