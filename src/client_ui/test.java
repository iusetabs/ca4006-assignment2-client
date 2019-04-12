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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private JButton clearButton;
    public MessengerService ms;
    private Calendar cal;
    private SimpleDateFormat sdf;
    private boolean timetable;
    private boolean new_room;
    private boolean new_booking;
    private boolean availability;
    private boolean testing;
    private Map<String, Integer> o_Sizes = new HashMap<>();

    public test() {
        setAllFalse();
        paramPanel.setVisible(false);
        ms  = new MessengerService(1, "http://10.216.35.189:8080");

        ActionListener listener = e -> {
            if (!e.getSource().equals(clearButton) && !(e.getSource().equals(ENTERButton)))
                reset_param_layout();
            if (e.getSource().equals(TIMETABLEButton)){
                setAllFalse();
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
                setAllFalse();
                t_dF1.setText("Room name");
                t_dF2.setText("Day");
                t_dF3.setText("Time");
                dF4.setVisible(false);
                t_dF4.setVisible(false);
                availability = true;
                afterBtn();            }
            else if (e.getSource().equals(NEWROOMButton)){
                setAllFalse();
                t_dF1.setText("New room name");
                t_dF2.setText("Capacity");
                dF3.setVisible(false);
                t_dF3.setVisible(false);
                dF4.setVisible(false);
                t_dF4.setVisible(false);
                afterBtn();
                new_room = true;
            }
            else if (e.getSource().equals(VIEWROOMSButton)) {
                setAllFalse();
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
                setAllFalse();
                t_dF1.setText("Room name");
                t_dF2.setText("Day");
                t_dF3.setText("Time");
                t_dF4.setText("Number of attendees");
                afterBtn();
                new_booking = true;
            }
            else if (e.getSource().equals(ENTERButton)){
                if(timetable){
                        String rN = dF1.getText().strip();
                        int sD = Integer.parseInt(dF2.getText().strip());
                        int eD = Integer.parseInt(dF3.getText().strip());
                        ArrayList<String> a = ms.get_timetable(rN, sD, eD);
                    if (a.isEmpty()){
                        outputArea.append(t() + "No timetable found!");
                    }
                    else{
                        outputArea.append(t() + "Timetable for room " + rN + "\n");
                        Map<Integer, List<Map<String, Object>>> bookings_ByDay = new HashMap<>();
                        for (String s : a) {
                            Map<String, Object> m = ms.jsonToMap(s);
                            Integer d = (Integer) m.get("day");
                            if (bookings_ByDay.get(d) == null) {
                                List<Map<String, Object>> l = new ArrayList<>();
                                bookings_ByDay.put(d, l);
                            }
                            List<Map<String, Object>> l = bookings_ByDay.get(d);
                            l.add(m);
                            bookings_ByDay.put((int) m.get("day"), l);
                        }
                        List<Integer> keys = new ArrayList<>(bookings_ByDay.keySet());
                        Collections.sort(keys);
                        System.out.println(keys);
                        for (Integer s : keys){
                            for (Map<String, Object> m :  bookings_ByDay.get(s))
                             outputArea.append(t() + "Day: " + m.get("day") + ". Booked at " + m.get("time") + " hours.\n");
                        }
                        outputArea.append("\n");
                    }
                    timetable = false;
                    clearText();
                }
                else if (new_room){
                    String rN = dF1.getText().strip();
                    int c = Integer.parseInt(dF2.getText().strip());
                    clearText();
                    Boolean res = ms.new_room(rN, c);
                    if (res)
                        outputArea.append(t() + "Successfully created room " + rN + " with capacity " + c + ".\n");
                    else
                        outputArea.append(t() + "Failed to create room " + rN + " with capacity " + c + ".\n");
                    outputArea.append("\n");
                    new_room = false;
                }
                else if(new_booking) {
                    String rN = dF1.getText().strip();
                    int d = Integer.parseInt(dF2.getText().strip());
                    String t = dF3.getText().strip();
                    int a = Integer.parseInt(dF4.getText().strip());
                    clearText();
                    Boolean res = ms.make_booking(rN, d, t, a);
                    if (res)
                        outputArea.append(t() + "Successful booking. \n");
                    else
                        outputArea.append(t() + "Failed to book room " + rN + " at " + t + " on day " + d + ".\n");
                    outputArea.append("\n");
                    new_booking = false;
                }
                else if (availability){
                    String rN = dF1.getText().strip();
                    int d = Integer.parseInt(dF2.getText().strip());
                    String t = dF3.getText().strip();
                    clearText();
                    ArrayList<String> a = ms.get_timetable(rN, d, d);
                    boolean res = true;
                    if(!a.isEmpty()) {
                        for (String s : a) {
                            Map<String, Object> m = ms.jsonToMap(s);
                            if (m.get("time").equals(t)) {
                                res = false;
                                break;
                            }
                        }
                    }
                    if (res)
                        outputArea.append(t() + rN + " is available at " +  t  + " hours.\n");
                    else
                        outputArea.append(t() + rN + " is not available at " +  t  + " hours.\n");
                    outputArea.append("\n");
                    availability = false;
                }
                else if(testing){
                    int num = Integer.parseInt(dF1.getText().strip());
                    run_program(num, outputArea);
                    outputArea.append("\n");
                    testing = false;
                }
            }
            else if (e.getSource().equals(TESTButton)) {
                setAllFalse();
                t_dF1.setText("How many threads?");
                dF2.setVisible(false);
                t_dF2.setVisible(false);
                dF3.setVisible(false);
                t_dF3.setVisible(false);
                dF4.setVisible(false);
                t_dF4.setVisible(false);
                testing = true;
                afterBtn();
            }
            else if(e.getSource().equals(clearButton)){
                outputArea.selectAll();
                outputArea.replaceSelection("");
            }
            paramPanel.revalidate();
            paramPanel.repaint();

        };
        TIMETABLEButton.addActionListener(listener);
        ROOMAVAILABILITYButton.addActionListener(listener);
        NEWROOMButton.addActionListener(listener);
        VIEWROOMSButton.addActionListener(listener);
        NEWBOOKINGButton.addActionListener(listener);
        ENTERButton.addActionListener(listener);
        TESTButton.addActionListener(listener);
        clearButton.addActionListener(listener);

    }

    public void run_program(int num, JTextArea outputArea){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Random ran = new Random();
        String ip = "http://10.216.35.189:8080";
        for (int i=0; i < 50; i++ ){
            MessengerService m = new MessengerService(i, ip, outputArea);
            executor.schedule(m, Math.abs(ran.nextInt(5)+1) , TimeUnit.SECONDS);
        }
        executor.shutdown();
    }

    private void reset_param_layout() {
        dF1.setVisible(true);
        t_dF1.setVisible(true);
        dF2.setVisible(true);
        t_dF2.setVisible(true);
        dF3.setVisible(true);
        t_dF3.setVisible(true);
        dF4.setVisible(true);
        t_dF4.setVisible(true);
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

    private void setAllFalse(){
       timetable = false;
       new_room = false;
       new_booking = false;
       availability = false;
       testing = false;
    }

    private String t(){
        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime()) +":\t";
    }

}
