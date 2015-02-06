package co.mobilemakers.contactstudio2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by ariel.cattaneo on 05/02/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private final static String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private final static String DATABASE_NAME = "contacts.db";
    private final static int DATABASE_VERSION = 1;

    private Dao<ContactModel, Integer> mContactDao = null;

    public Dao<ContactModel, Integer> getDocumentDao() throws SQLException {
        if (mContactDao == null) {
            mContactDao = getDao(ContactModel.class);
        }
        return mContactDao;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.d(LOG_TAG, "Creating database");
            TableUtils.createTable(connectionSource, ContactModel.class);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Error creating database.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
