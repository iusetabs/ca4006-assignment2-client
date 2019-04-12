package client_code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static client_code.HttpRest.executePost;

public class MessengerService implements Runnable {

    private int name;
    private String IP;
    private JTextArea outputArea;
    private boolean print_to_ui;

    public MessengerService(int name, String IP){
        this.name = name;
        this.IP = IP;
    }

    public MessengerService(int name, String IP, JTextArea outputArea){
        this.name = name;
        this.IP = IP;
        this.outputArea = outputArea;
        this.print_to_ui = true;
    }

    public ArrayList get_request(String req){

        return HttpRest.executeGet(this.IP + req);
    }

    public ArrayList post_request(String req, String payload){
        ArrayList lst;
        String error = "Failed : HTTP error code";
        String server_down = "Server down!";
        while(true) {
            try {
                lst = HttpRest.executePost(this.IP + req, payload);
                break;
            } catch (RuntimeException e) {
                if(e.equals(error)){
                    try {
                        Thread.currentThread().sleep(200);
                    } catch (InterruptedException ex) {}
                }
                if (e.equals(server_down)) {
                    System.out.println(server_down);
                    throw new RuntimeException(server_down);
                }
            }
        }
        return lst;
    }

    public boolean make_booking(String room, int day, String time, int att){
        String booking_tmp = ("{\"roomName\": \""+ room +"\", \"day\": "+ day +", \"time\":\""+ time +"\", \"num_attendees\": "+ att +"}");
        String error = "Error, booking already exists";
        return (!post_request("/bookings", booking_tmp).get(0).toString().equals(error));
    }

    public ArrayList get_rooms(){
        return get_request("/rooms");
    }

    public ArrayList get_checkRoom(String name, int day, String time){
        return get_request("/checkRoom/rooms/"+ name +"/day/"+ day +"/time/"+ time);
    }

    public boolean new_room(String room, int capacity){
        String booking_tmp = ("{\"name\": \""+ room +"\", \"capacity\": "+ capacity +"}");
        String error = "Error, cannot post to room";
        return (!post_request("/rooms", booking_tmp).get(0).toString().equals(error));
    }

    public ArrayList get_timetable(String room, int s_day, int e_day){
       return get_request("/timetableWeek/rooms/"+ room +"/startDay/"+ s_day +"/endDay/"+ e_day);
    }

    public void build_request() {
        //System.out.println(get_rooms());
        //System.out.println(get_checkRoom("CG04", 2, "16:00"));
        //System.out.println(get_timetable("CG04", 2, 7));
        ArrayList lst = new ArrayList();
        try {
            lst = read_json_get_rooms(get_rooms());
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        Random ran = new Random();
        for (int i = 0; i < 1; i++) {
            try {
                if (print_to_ui)
                    outputArea.append(this.name + " :" + make_booking((String) lst.get(Math.abs(ran.nextInt(lst.size()))), Math.abs(ran.nextInt(6)), get_time(ran) + ":00", Math.abs(ran.nextInt(80))) + "\n");
                else
                    System.out.println(this.name + " :" + make_booking((String) lst.get(Math.abs(ran.nextInt(lst.size()))), Math.abs(ran.nextInt(6)), get_time(ran) + ":00", Math.abs(ran.nextInt(80))));
            }
            catch(RuntimeException e){
                String server_down = "Server down!";
                if(e.equals(server_down) && print_to_ui)
                    outputArea.append(this.name +": "+server_down + "\n");
            }
        }
    }

    public String get_time(Random ran){
        int x = ran.nextInt(24);
        int tme = Math.abs(x);
        if (tme< 10)
            return "0"+x;
        return String.valueOf(tme);
    }


    public ArrayList read_json_get_rooms (ArrayList lst) throws org.json.simple.parser.ParseException {
        ArrayList tmp = new ArrayList();
        for (Object str : lst) {
            Object obj = new JSONParser().parse((String) str);
            JSONObject jo = new JSONObject((Map) obj);
            tmp.add(jo.get("name"));
        }
        return tmp;
    }

        public Map<String, Object> jsonToMap (String s){
            Map<String, Object> m;
            try {
                m = new ObjectMapper().readValue(s, HashMap.class);
            } catch (IOException e) {
                e.printStackTrace();
                m = new HashMap<>();
            }
            return m;
        }

        @Override
        public void run () {
            //noinspection InfiniteLoopStatement
            build_request();
        }
    }