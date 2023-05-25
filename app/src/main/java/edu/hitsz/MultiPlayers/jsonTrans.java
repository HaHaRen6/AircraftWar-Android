//package edu.hitsz.MultiPlayers;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//
//public class jsonTrans extends AppCompatActivity implements View.OnClickListener  {
//
//    private Socket socket;
//    private PrintWriter writer;
//
//    private static  final String TAG = "MainActivity";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button btnSend = findViewById(R.id.btnSend);
//
//        btnSend.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.btnSend:
//                 new Thread(new NetConn()).start();
//                break;
//        }
//    }
//
//
//    protected class NetConn extends Thread {
//        private BufferedReader in;
//
//        public NetConn() {
//        }
//
//        @Override
//        public void run() {
//            try {
//                socket = new Socket();
//                //运行时修改成服务器的IP
//                socket.connect(new InetSocketAddress
//                        ("10.250.102.144", 9999), 5000);
//                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
//                writer = new PrintWriter(new BufferedWriter(
//                        new OutputStreamWriter(
//                                socket.getOutputStream(), "utf-8")), true);
//
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("name", "123");
//                jsonObject.put("score", "100");
//                writer.println(jsonObject.toString());
//                Log.i(TAG,"json = " + jsonObject.toString());
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//
//}
//
