package com.jumbo.torture.provider;

import java.util.HashMap;

import com.jumbo.torture.R;
import com.jumbo.torture.provider.Torture.TortureMsg;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class TortureProvider extends ContentProvider {
    private static final String TAG = TortureProvider.class.getName();

    private SQLiteOpenHelper mOpenHelper;

    private static final int T_MSGES 	= 1;
    private static final int T_MSG_ID 	= 2;

    private static final UriMatcher mUriMatcher;
    private static HashMap<String,String> mTortureMsgProjectionMap;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(Torture.AUTHORITY, "tortureMsges", T_MSGES);
        mUriMatcher.addURI(Torture.AUTHORITY, "tortureMsges/#", T_MSG_ID);

        mTortureMsgProjectionMap = new HashMap<String, String>();
        mTortureMsgProjectionMap.put(TortureMsg._ID,TortureMsg._ID);
        mTortureMsgProjectionMap.put(TortureMsg.TITLE, TortureMsg.TITLE);
        mTortureMsgProjectionMap.put(TortureMsg.CONTENT, TortureMsg.CONTENT);
        mTortureMsgProjectionMap.put(TortureMsg.COMMENTS, TortureMsg.COMMENTS);
        mTortureMsgProjectionMap.put(TortureMsg.CREATED_TIME,TortureMsg.CREATED_TIME);
        mTortureMsgProjectionMap.put(TortureMsg.MODIFY_TIME,TortureMsg.MODIFY_TIME);
        mTortureMsgProjectionMap.put(TortureMsg.PRIORITY, TortureMsg.PRIORITY);
        mTortureMsgProjectionMap.put(TortureMsg.STATUS,TortureMsg.STATUS);
        mTortureMsgProjectionMap.put(TortureMsg.TAGS_ID,TortureMsg.TAGS_ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        switch(mUriMatcher.match(uri)){
            case T_MSGES:
                count = db.delete(DataBaseHelper.MSG_TABLE_NAME, selection,selectionArgs);
                break;
            case T_MSG_ID:
                String msgId = uri.getPathSegments().get(1);
                count = db.delete(DataBaseHelper.MSG_TABLE_NAME, TortureMsg._ID + "=" + msgId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)){
            case T_MSG_ID:
                return TortureMsg.CONTENT_ITEM_TYPE;
            case T_MSGES:
                return TortureMsg.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
         // Validate the requested uri
        if (mUriMatcher.match(uri) != T_MSGES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        Long now = Long.valueOf(System.currentTimeMillis());

        // Make sure that the fields are all set
        if (values.containsKey(TortureMsg.CREATED_TIME) == false) {
            values.put(TortureMsg.CREATED_TIME,now);
        }

        if (values.containsKey(TortureMsg.MODIFY_TIME) == false) {
            values.put(TortureMsg.MODIFY_TIME, now);
        }

        if (values.containsKey(TortureMsg.TITLE) == false) {
            Resources r = Resources.getSystem();
            values.put(TortureMsg.TITLE, r.getString(android.R.string.untitled));
        }

        if (values.containsKey(TortureMsg.CONTENT) == false) {
            Resources r = Resources.getSystem();
            values.put(TortureMsg.CONTENT,r.getString(R.string.empty_content_hint));
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(DataBaseHelper.MSG_TABLE_NAME,TortureMsg.CONTENT,values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(TortureMsg.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
         SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
         qb.setTables(DataBaseHelper.MSG_TABLE_NAME);

         switch (mUriMatcher.match(uri)) {
             case T_MSGES:
                 qb.setProjectionMap(mTortureMsgProjectionMap);
                 break;

             case T_MSG_ID:
                 qb.setProjectionMap(mTortureMsgProjectionMap);
                 qb.appendWhere(TortureMsg._ID + "=" + uri.getPathSegments().get(1));
                 break;

             default:
                 throw new IllegalArgumentException("Unknown URI " + uri);
         }

         // If no sort order is specified use the default
         String orderBy;
         if (TextUtils.isEmpty(sortOrder)) {
             orderBy = TortureMsg.DEFAULT_SORT_ORDER;
         } else {
             orderBy = sortOrder;
         }

         // Get the database and run the query
         SQLiteDatabase db = mOpenHelper.getReadableDatabase();
         Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

         // Tell the cursor what uri to watch, so it knows when its source data changes
         c.setNotificationUri(getContext().getContentResolver(), uri);
         return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (mUriMatcher.match(uri)) {
            case T_MSGES:
                count = db.update(DataBaseHelper.MSG_TABLE_NAME, values, selection, selectionArgs);
                break;
            case T_MSG_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.update(DataBaseHelper.MSG_TABLE_NAME, values, TortureMsg._ID + "=" + noteId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}