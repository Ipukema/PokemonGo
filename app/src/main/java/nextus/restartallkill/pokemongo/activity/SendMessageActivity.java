package nextus.restartallkill.pokemongo.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import java.util.HashMap;
import java.util.Map;

import nextus.restartallkill.pokemongo.BoardItem;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.CustomRequest;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class SendMessageActivity extends CycleControllerActivity implements View.OnClickListener {

    @DeclareView(id=R.id.send_button, click="this")FloatingActionButton send_button;
    @DeclareView(id=R.id.user_mail) EditText user_mail;
    @DeclareView(id=R.id.msg_info) EditText msg_info;
    @DeclareView(id=R.id.msg_title) EditText msg_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message, true);

    }

    public void sendData()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_mail", user_mail.getText().toString());
        param.put("msg_title", msg_title.getText().toString());
        param.put("msg_info", msg_info.getText().toString());


        String url = "http://125.209.193.163/pokemongo/sendMsg.jsp";

        final CustomRequest<BoardItem> jsonObjReq = new CustomRequest<BoardItem>(Request.Method.POST, url, param,
                BoardItem.class, //Not null.
                new Response.Listener<BoardItem>() {
                    @Override
                    public void onResponse(BoardItem response) {
                        try {
                           // MyApplication.getInstance().boardItem = response;
                            //Log.e("Test:",response.getBoardData().get(0).getBoard_title());
                            //MySingletonOld.dinosaursBasicData.getData().addAll(response.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
        MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void reset()
    {
        user_mail.setText(null);
        msg_info.setText(null);
        msg_title.setText(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.send_button:
                sendData();
                reset();
                Toast.makeText(this,"전송되었습니다.",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
