package com.jumbo.torture;

import com.jumbo.torture.provider.Torture.TortureMsg;
import com.jumbo.torture.utils.LogUtils;
import com.jumbo.torture.utils.MsgOperationUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeMsgActivity extends Activity implements OnClickListener{
    private static String TAG = ComposeMsgActivity.class.getName();

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.compose_msg_activity);

        findViewById(R.id.submit_btn).setOnClickListener(this);
        findViewById(R.id.cancel_btn).setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.content_editor);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.submit_btn:
                String content = mEditText.getText().toString();
                if(insert(content)){
                    finish();
                }
                break;
            case R.id.cancel_btn:
                finish();
                break;
            default:
                break;

        }
    }

    private boolean insert(String content){
        if(TextUtils.isEmpty(content)){
            Toast.makeText(ComposeMsgActivity.this, "Empty Insert @_@",Toast.LENGTH_SHORT).show();
            return false;
        }

        ContentValues values = new ContentValues();

        values.put(TortureMsg.CREATED_TIME, System.currentTimeMillis());
        values.put(TortureMsg.MODIFY_TIME, System.currentTimeMillis());
        values.put(TortureMsg.TITLE, MsgOperationUtils.handleTitle(this, "", content));
        values.put(TortureMsg.CONTENT,content);

        try {
            Uri id = getContentResolver().insert(TortureMsg.CONTENT_URI, values);
        } catch (NullPointerException e) {
            LogUtils.e(TAG, e.getMessage());
        }

        return true;
    }
}
