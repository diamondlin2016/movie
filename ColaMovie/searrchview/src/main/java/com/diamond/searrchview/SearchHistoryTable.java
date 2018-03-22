package com.diamond.searrchview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
public class SearchHistoryTable {

    private static int mHistorySize = 2;
    private static Integer mCurrentDatabaseKey = null;
    private final SearchHistoryDatabase dbHelper;
    private SQLiteDatabase db;

    public SearchHistoryTable(Context mContext) {
        dbHelper = new SearchHistoryDatabase(mContext);
        open();
    }

//     FOR onResume AND onPause
    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addItem(SearchItem item) {
        addItem(item, mCurrentDatabaseKey);
    }

    public void addItem(SearchItem item, Integer databaseKey) {
        ContentValues values = new ContentValues();
        if (!checkText(item.getText().toString())) {
            values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT, item.getText().toString());
            if (databaseKey != null) {
                values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY, databaseKey);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            db.insert(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null, values);
        } else {
            values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID, getLastItemId(databaseKey) + 1);
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            db.update(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, values, SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " = ? ", new String[]{Integer.toString(getItemId(item))});
        }
    }

    private int getItemId(SearchItem item) {
        String query = "SELECT " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID +
                " FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE +
                " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT + " = ?";
        Cursor res = db.rawQuery(query, new String[]{item.getText().toString()});
        res.moveToFirst();
        int id = res.getInt(0);
        res.close();
        return id;
    }

    private int getLastItemId(Integer databaseKey) {
        String sql = "SELECT " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        if (databaseKey != null)
            sql += " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY + " = " + databaseKey;
        Cursor res = db.rawQuery(sql, null);
        res.moveToLast();
        int count = res.getInt(0);
        res.close();
        return count;
    }

    private boolean checkText(String text) {

        String query = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE + " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{text});

        boolean hasObject = false;

        if (cursor.moveToFirst()) {
            hasObject = true;
        }

        cursor.close();
        return hasObject;
    }

    public List<SearchItem> getAllItems(Integer databaseKey) {
        mCurrentDatabaseKey = databaseKey;
        List<SearchItem> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        if (databaseKey != null) {
            selectQuery += " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY + " = " + databaseKey;
        }
        selectQuery += " ORDER BY " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " DESC LIMIT " + mHistorySize;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SearchItem item = new SearchItem();
                item.setIconResource(R.drawable.ic_history_black_24dp);
                item.setText(cursor.getString(1));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void setHistorySize(int historySize) {
        mHistorySize = historySize;
    }

    public void clearDatabase() {
        clearDatabase(null);
    }

    public void clearDatabase(Integer key) {
        if (key == null) {
            db.delete(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null, null);
        } else {
            db.delete(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY + " = ?", new String[]{String.valueOf(key)});
        }
    }

    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
