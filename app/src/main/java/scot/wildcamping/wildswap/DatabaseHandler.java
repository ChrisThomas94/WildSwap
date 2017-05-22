package scot.wildcamping.wildswap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import scot.wildcamping.wildswap.Objects.Site;

/**
 * Created by Chris on 22-May-17.
 *
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "wildSwap";

    // Contacts table name
    private static final String TABLE_SITES = "sites";

    // Contacts Table Columns names
    private static final String KEY_ID = "cid";
    private static final String KEY_SITE_ADMIN = "cid";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LON = "longitude";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "description";
    private static final String KEY_CLASS = "classification";
    private static final String KEY_RATING = "rating";
    private static final String KEY_DISPLAY_PIC = "display_pic";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SITE_ADMIN + " TEXT,"
                + KEY_LAT + " TEXT"
                + KEY_LON + " TEXT"
                + KEY_TITLE + " TEXT"
                + KEY_DESC + " TEXT"
                + KEY_CLASS + " TEXT"
                + KEY_RATING + " TEXT"
                + KEY_DISPLAY_PIC + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    // Adding new site
    public void addSite(Site site) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SITE_ADMIN, site.getSiteAdmin()); // Contact Name
        values.put(KEY_LAT, site.getPosition().latitude); // Contact Phone Number
        values.put(KEY_LON, site.getPosition().longitude); // Contact Phone Number
        values.put(KEY_TITLE, site.getTitle()); // Contact Phone Number
        values.put(KEY_DESC, site.getDescription()); // Contact Phone Number
        values.put(KEY_CLASS, site.getClassification()); // Contact Phone Number
        values.put(KEY_RATING, site.getRating()); // Contact Phone Number
        values.put(KEY_DISPLAY_PIC, site.getDisplay_pic()); // Contact Phone Number


        // Inserting Row
        db.insert(TABLE_SITES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single site
    public Site getSite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SITES, new String[] { KEY_ID,
                        KEY_SITE_ADMIN, KEY_LAT, KEY_LON, KEY_TITLE, KEY_DESC, KEY_CLASS, KEY_RATING, KEY_DISPLAY_PIC}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Site site = new Site();
        /*Site site = new Site(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));*/
        // return contact
        return site;
    }

    // Getting All Sites
    //public List<Site> getAllSites() {}

    // Getting contacts Sites
    //public int getSiteCount() {}

    // Updating single site
    //public int updateSite(Site site) {}

    // Deleting single site
    //public void deleteSite(Site site) {}

}
