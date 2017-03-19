package prakhar.com.edforamusicsample.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import prakhar.com.edforamusicsample.Model.SongDetailsModel;

/**
 * Created by lendingkart on 3/12/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SongDB";

    // Contacts table name
    private static final String TABLE_SONGS = "Songs";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL = "image";
    private static final String KEY_ARTIST = "total_score";
    private static final String KEY_COVER = "matches_played";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_PLAYER = "CREATE TABLE IF NOT EXISTS " + TABLE_SONGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_URL + " TEXT," + KEY_ARTIST + " TEXT," + KEY_COVER + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_PLAYER);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addContact(SongDetailsModel songDetailsModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, songDetailsModel.getSong()); // Contact Name
        values.put(KEY_URL, songDetailsModel.getUrl()); // Contact Phone
        values.put(KEY_ARTIST, songDetailsModel.getArtists()); // Contact Name
        values.put(KEY_COVER, songDetailsModel.getCoverImage()); // Contact Phone


        // Inserting Row
        db.insert(TABLE_SONGS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public ArrayList<SongDetailsModel> getAllSongs() {
        ArrayList<SongDetailsModel> songsList = new ArrayList<SongDetailsModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SongDetailsModel songDetailsModel = new SongDetailsModel(null);

                songDetailsModel.setSong(cursor.getString(1));
                songDetailsModel.setUrl(cursor.getString(2));
                songDetailsModel.setArtists(cursor.getString(3));
                songDetailsModel.setCoverImage(cursor.getString(4));
                // Adding contact to list
                songsList.add(songDetailsModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return songsList;
    }


    // Getting All Contacts
    public ArrayList<SongDetailsModel> getAllResultWithQuery(String query) {
        ArrayList<SongDetailsModel> songQueriedList = new ArrayList<SongDetailsModel>();

        // Select All Query
        String songSelectQuery = "SELECT  * FROM " + TABLE_SONGS + " WHERE " + KEY_NAME + " LIKE " + "'" + "%"
                + query + "%" + "'" + " OR " + KEY_ARTIST + " LIKE " + "'" + "%" + query + "%" + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor songCursor = db.rawQuery(songSelectQuery, null);

        // looping through all rows and adding to list
        if (songCursor.moveToFirst()) {
            do {
                SongDetailsModel songDetailsModel = new SongDetailsModel(null);

                songDetailsModel.setSong(songCursor.getString(1));
                songDetailsModel.setUrl(songCursor.getString(2));
                songDetailsModel.setArtists(songCursor.getString(3));
                songDetailsModel.setCoverImage(songCursor.getString(4));
                // Adding contact to list
                songQueriedList.add(songDetailsModel);
            } while (songCursor.moveToNext());
        }
        // return songs result hashmap
        return songQueriedList;
    }


    // Deleting All
    public void deleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONGS, null, null);
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }


        // return count
        return count;
    }
}
