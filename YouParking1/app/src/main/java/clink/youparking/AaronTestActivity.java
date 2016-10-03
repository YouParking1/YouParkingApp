package clink.youparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class AaronTestActivity extends AppCompatActivity {

    private EditText mInputMessageView;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://108.167.99.14:88");
        } catch (URISyntaxException e) {}
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... arg) {
            final Object args = arg[0];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //JSONObject data = (JSONObject) args;
                    String lat;
                    String mLong;
//                    try {
//                        lat = data.getString("Latitude");
//                        mLong = data.getString("Longitude");
//                    } catch (JSONException e) {
//                        return;
//                    }


                    //String combined = lat + " " + mLong;
                    System.out.println(args.toString() + " *&*&*&*&*&*&*&*");
                    TextView myTextView = (TextView) findViewById(R.id.from_server);
                    myTextView.setText(args.toString());
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aaon_test);

        mSocket.on("new message", onNewMessage);
        mSocket.connect();
    }

    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        mInputMessageView.setText("");
        mSocket.emit("new message", message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }
}
