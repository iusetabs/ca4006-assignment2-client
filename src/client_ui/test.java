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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    private JTextField textField1;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField2;
    private JTextArea outputArea;
    public MessengerService ms;
    private Calendar cal;
    private SimpleDateFormat sdf;

    public test() {
        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm:ss");
        paramPanel.setVisible(false);
        ms  = new MessengerService(1, "http://10.216.35.189:8080");

        ActionListener listener = e -> {
            if (e.getSource().equals(ENTERButton)){
                paramPanel.setVisible(false);
            }
            else if (e.getSource().equals(TIMETABLEButton)){
                afterBtn();
            }
            else if (e.getSource().equals(ROOMAVAILABILITYButton)) {
                afterBtn();            }
            else if (e.getSource().equals(NEWROOMButton)){
                afterBtn();            }
            else if (e.getSource().equals(ENTERButton)){
                afterBtn();            }
            else if (e.getSource().equals(VIEWROOMSButton)) {
                outputArea.append(t() + "Getting listing of current rooms\n");
                paramPanel.setVisible(false);
                ArrayList<String> a = ms.get_request("/rooms");
                if (a.isEmpty()){
                    outputArea.append("No rooms found!");
                }
                else{
                    for (String s : a) {
                        Map<String, Object> m = jsonToMap(s);
                        outputArea.append(t() + "Room " + m.get("name") + " has a capacity of " + m.get("capacity")  + ".\n");
                    }
                    outputArea.append("\n");
                }
            }
            else if (e.getSource().equals(NEWBOOKINGButton)){
                afterBtn();            }
            else if (e.getSource().equals(ENTERButton)) {
                afterBtn();
            }
            else if (e.getSource().equals(TESTButton)) {
                afterBtn();
            }
        };
        ENTERButton.addActionListener(listener);
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
        clearText();
    }
    private void clearText(){

    }

    private Map<String, Object> jsonToMap(String s) {
        Map<String, Object> m;
        try {
            m = new ObjectMapper().readValue(s, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            m = new HashMap<>();
        }
        return m;
    }

    private String t(){
        return sdf.format(cal.getTime()) +":\t";
    }
}
