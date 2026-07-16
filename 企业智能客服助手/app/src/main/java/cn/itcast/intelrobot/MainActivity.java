package cn.itcast.intelrobot;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMsg;
    private Button btnSend;
    private List<ChatBean> chatList = new ArrayList<>();
    private ChatAdapter adapter;
    private final Handler handler = new Handler(Looper.getMainLooper());

    // ====================== 【唯一改这里：你的本地服务器地址】 ======================
    private static final String URL = "http://10.115.11.59:8090";
    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定控件
        recyclerView = findViewById(R.id.recyclerView);
        etMsg = findViewById(R.id.et_msg);
        btnSend = findViewById(R.id.btn_send);

        // 初始化聊天列表
        adapter = new ChatAdapter(chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 机器人初始欢迎语
        chatList.add(new ChatBean("你好，我是智能聊天机器人！", ChatBean.TYPE_ROBOT));
        adapter.notifyDataSetChanged();

        // 发送按钮点击事件
        btnSend.setOnClickListener(v -> {
            String msg = etMsg.getText().toString().trim();
            if(!msg.isEmpty()){
                // 添加用户消息
                chatList.add(new ChatBean(msg, ChatBean.TYPE_USER));
                adapter.notifyItemInserted(chatList.size()-1);
                recyclerView.scrollToPosition(chatList.size()-1);
                etMsg.setText("");

                // 发送请求给服务器
                sendChatRequest(msg);
            }
        });
    }

    private void sendChatRequest(String msg){
        new Thread(() -> {
            try {
                // ====================== 【修复1：请求格式必须和server.js一致】 ======================
                JSONObject json = new JSONObject();
                JSONObject perception = new JSONObject();
                JSONObject inputText = new JSONObject();
                inputText.put("text", msg);
                perception.put("inputText", inputText);
                json.put("perception", perception);

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(json.toString(), JSON_TYPE);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String resStr = response.body().string();

                // ====================== 【修复2：解析格式匹配server.js返回】 ======================
                JSONObject resJson = new JSONObject(resStr);
                String reply = resJson.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("values")
                        .getString("text");

                // 切回主线程更新UI
                handler.post(() -> {
                    chatList.add(new ChatBean(reply, ChatBean.TYPE_ROBOT));
                    adapter.notifyItemInserted(chatList.size()-1);
                    recyclerView.scrollToPosition(chatList.size()-1);
                });



            } catch (IOException | JSONException e) {
                e.printStackTrace();
                handler.post(() -> {
                    chatList.add(new ChatBean("网络请求失败，请重试", ChatBean.TYPE_ROBOT));
                    adapter.notifyItemInserted(chatList.size()-1);
                });
            }
        }).start();
    }
}


