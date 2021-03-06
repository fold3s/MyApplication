package com.example.hao.smarthome.fragments.H1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hao.smarthome.BackGround;
import com.example.hao.smarthome.R;
import com.example.hao.smarthome.Sign_In;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;


public class Room2_H1 extends Fragment{

    ImageView fan_2,led_2;
    boolean fan_2_flag,led_2_flag;
    private Sign_In sign_in;
    BackGround backGround;
    private Emitter.Listener onSetNode = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message;
                    String msg =  args[0].toString();
                    try {
                        JSONObject data=new JSONObject((String)args[0]);
                        message=data.getString("rcode");
                        Log.d("receive",data.toString());
                        if(message.equals("200")){
                            Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    };
    private Emitter.Listener onSync = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj =  new JSONObject((String)args[0]);
                        String message=obj.toString();
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                        Log.d("sync","asd");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private Emitter.Listener onCheck= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message,device,status;
                    try{
                        JSONObject data=new JSONObject((String)args[0]);
                        message=data.getString("rcode");
                        device=data.getString("nodeCode");
                        status=data.getString("status");
                        if(message.equals("200")){
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                            if(device.equals("2H2")){
                                if(status.equals("1")){
                                    fan_2.setImageResource(R.mipmap.fan_on);
                                    fan_2_flag=true;
                                }
                                else if(status.equals("0")){
                                    fan_2.setImageResource(R.mipmap.fan_off);
                                    fan_2_flag=false;
                                }
                            }
                            else if(device.equals("2H1")){
                                if(status.equals("1")){
                                    led_2.setImageResource(R.mipmap.led_on);
                                    led_2_flag=true;
                                } else if (status.equals("0")) {
                                    led_2.setImageResource(R.mipmap.led_off);
                                    led_2_flag=false;
                                }
                            }
                        }
                        else {
                            Toast.makeText(getActivity(),"Error Occured",Toast.LENGTH_LONG).show();
                        }
                        Log.d("check",data.toString());
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    public Room2_H1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sign_in = new Sign_In();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.room2_h1, container, false);
        backGround = new BackGround();
        fan_2=(ImageView)rootView.findViewById(R.id.fan_2);
        led_2=(ImageView)rootView.findViewById(R.id.led_2);
        backGround.mSocket.connect();
        backGround.mSocket.on("RMNODE",onCheck);
        backGround.mSocket.on("RMSETNODE",onSetNode);
        checknode("H2","2H2");
        checknode("H2","2H1");

        fan_2.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fan_2_flag==true){
                    fan_2.setImageResource(R.mipmap.fan_off);
                    fan_2_flag=false;
                    setnode("H2","2H2",0);


                }
                else if(fan_2_flag==false){
                    fan_2.setImageResource(R.mipmap.fan_on);
                    fan_2_flag=true;
                    setnode("H2","2H2",1);
                }
            }
        });
        led_2.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(led_2_flag==true){
                    led_2.setImageResource(R.mipmap.led_off);
                    led_2_flag=false;
                    setnode("H2","2H1",0);
                }
                else if(led_2_flag==false){
                    led_2.setImageResource(R.mipmap.led_on);
                    led_2_flag=true;
                    setnode("H2","2H1",1);
                }
            }
        });
        return rootView;

    }
    private void setnode(String homeCode,String nodeCode,int status){
        JSONObject obj = new JSONObject();
        try {
            obj.put("title","@MSETNODE");
            obj.put("userID","tuyen");
            obj.put("homeCode",homeCode);
            obj.put("nodeCode",nodeCode);
            obj.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("error","here");
        Log.d("error",obj.toString());

        backGround.mSocket.emit("MSETNODE",obj.toString());
    }
    private void checknode(String homeCode,String nodeCode){
        JSONObject obj=new JSONObject();
        try{
            obj.put("title","@MNODE");
            obj.put("homeCode",homeCode);
            obj.put("nodeCode",nodeCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        backGround.mSocket.emit("MNODE",obj.toString());
    }

    }


