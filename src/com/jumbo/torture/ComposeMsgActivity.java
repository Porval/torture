package com.jumbo.torture;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jumbo.torture.model.TortureMsgModel;
import com.jumbo.torture.provider.Torture.TortureMsg;
import com.jumbo.torture.utils.LogUtils;
import com.jumbo.torture.utils.MsgOperationUtils;

public class ComposeMsgActivity extends Activity implements OnClickListener{
    private static String TAG = ComposeMsgActivity.class.getName();

    private static final String[] PROJECTION = new String[]{
        TortureMsg._ID,
        TortureMsg.CONTENT,
        TortureMsg.TITLE,
        TortureMsg.COMMENTS,
        TortureMsg.CREATED_TIME,
        TortureMsg.TAGS_ID,
    };

    private static final int T_MSG_INDEX_ID 				= 0;
    private static final int T_MSG_INDEX_CONTENT 			= 1;
    private static final int T_MSG_INDEX_TITLE				= 2;
    private static final int T_MSG_INDEX_COMMENTS   		= 3;
    private static final int T_MSG_INDEX_CREATED_TIME 		= 4;
    private static final int T_MSG_INDEX_TAGS_ID			= 5;

    //the diffeernt distinct mode that compose t_msg
    private static final int MODE_EDIT 		= 0;
    private static final int MODE_INSERT 	= 1;

    private TortureMsgModel mTortureMsg;

    private EditText mContentEditor;
    private TextView mTitleView;

    private int mMode = MODE_INSERT;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.compose_msg_activity);

        queryMsgData();

        initView();

    }

    private void queryMsgData(){
        Intent intent = getIntent();
        if(intent == null){
            finish();
            LogUtils.e(TAG," ComposeMsgActivity Null intent ");
            return;
        }

        mUri = intent.getData();

        if(mUri == null){
            mUri = TortureMsg.CONTENT_URI;
        }

        if(Intent.ACTION_EDIT.equals(intent.getAction())){
            mMode = MODE_EDIT;
        } else if(Intent.ACTION_INSERT.equals(intent.getAction())){
            mMode = MODE_INSERT;
            mUri = getContentResolver().insert(mUri, null);
        }

    }

    private void initView(){
        mContentEditor = (EditText) findViewById(R.id.content_editor);
        mTitleView = (TextView) findViewById(R.id.title);

        findViewById(R.id.submit_btn).setOnClickListener(this);
        findViewById(R.id.cancel_btn).setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        fillViewData();
    }

    private void fillViewData(){
        Cursor cursor = null;
        try{
            cursor = getContentResolver().query(mUri, PROJECTION, null, null,null);
            if(cursor != null){
                cursor.moveToFirst();
                mContentEditor.setText(cursor.getString(T_MSG_INDEX_CONTENT));
                mTitleView.setText(cursor.getString(T_MSG_INDEX_TITLE));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null){
                cursor.close();
            }
            cursor = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.submit_btn:
                String content = mContentEditor.getText().toString();
                if(MsgOperationUtils.insert(this,content) != null){
                    finish();
                } else {
                    Toast.makeText(ComposeMsgActivity.this, "Insert ERROR @_@ ",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_btn:
                finish();
                break;
            default:
                break;

        }
    }
}
