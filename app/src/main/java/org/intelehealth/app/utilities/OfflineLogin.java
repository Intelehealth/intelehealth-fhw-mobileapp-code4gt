package org.intelehealth.app.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import org.intelehealth.app.utilities.CustomLog;
import android.widget.Toast;


import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


import org.intelehealth.app.R;
import org.intelehealth.app.activities.homeActivity.HomeScreenActivity_New;
import org.intelehealth.app.app.AppConstants;
import org.intelehealth.app.app.IntelehealthApplication;


/**
 * Provides necessary login alternative if offline.
 * <p>
 * Created by Dexter Barretto on 5/24/17.
 * Github : @dbarretto
 */

public class OfflineLogin {

    private static final String TAG = OfflineLogin.class.getSimpleName();
    private static OfflineLogin mOfflineLogin;
    private Context mContext;
    private SharedPreferences mSharedPreference;
    String user,pass, provider_uuid, creator_uuid, chw_name;
    //SessionManager sessionManager;
    SessionManager sessionManager = null;


    private OfflineLogin(Context context) {
        mContext = context;
        mSharedPreference = mContext.getSharedPreferences(
                context.getString(R.string.offline_login_shared_preference_key), Context.MODE_PRIVATE);
        sessionManager = new SessionManager(mContext);
    }

    /**
     * Provides application context.
     *
     * @return {@link OfflineLogin}
     */
    public static OfflineLogin getOfflineLogin() {
        if (mOfflineLogin == null)
            mOfflineLogin = new OfflineLogin(IntelehealthApplication.getAppContext());
        return mOfflineLogin;
    }

    /**
     * Stores login credentials in shared preferences after hashing.
     *
     * @param username The username
     * @param password The password
     */
    public void setUpOfflineLogin(String username, String password) {
        StringEncryption stringEncryption = new StringEncryption();
        String random_salt = stringEncryption.getRandomSaltString();
        String hash = null;
        try {
            hash = StringEncryption.convertToSHA256(random_salt + password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        SharedPreferences.Editor editor = mSharedPreference.edit();

        if (username != null && !TextUtils.isEmpty(username))
            editor.putString(mContext.getString(R.string.offline_login_username_key), username);

        if (random_salt != null && !TextUtils.isEmpty(random_salt)) editor.putString(
                mContext.getString(R.string.offline_login_salt_key), random_salt);

        if (hash != null && !TextUtils.isEmpty(hash)) editor.putString(
                mContext.getString(R.string.offline_login_password_key), hash);

        editor.putBoolean(
                mContext.getString(R.string.offline_login_status), true);

        editor.commit();

        CustomLog.i(TAG, "Created Offline Login!");
        CustomLog.i(TAG, "Username: " + username);
        CustomLog.i(TAG, "Salt: " + random_salt);
        CustomLog.i(TAG, "Password Hash: " + hash);
    }

    // this was already not being used anywhere so commented it as this class is also used in new ui so not deleting it.
/*
    public void login(String username, String password) {
        CustomLog.i(TAG, "Checking Offline Login!");
        if (mSharedPreference.contains(mContext.getString(R.string.offline_login_username_key)) &&
                mSharedPreference.contains(mContext.getString(R.string.offline_login_salt_key)) &&
                mSharedPreference.contains(mContext.getString(R.string.offline_login_password_key))) {

            String stored_username = mSharedPreference.getString(mContext.getString(R.string.offline_login_username_key), null);

            if (stored_username != null) {
                CustomLog.i(TAG, "Username: " + username);
                CustomLog.i(TAG, "Stored Username: " + stored_username);
                String stored_password = mSharedPreference.getString(mContext.getString(R.string.offline_login_password_key), null);
                String stored_salt = mSharedPreference.getString(mContext.getString(R.string.offline_login_salt_key), null);

                if (stored_password != null && stored_salt != null) {
                    CustomLog.i(TAG, "Stored Salt: " + stored_salt);
                    CustomLog.i(TAG, "Stored Password : " + stored_password);
                    StringEncryption stringEncryption = new StringEncryption();
                    String hash = null;
                    try {
                        hash = StringEncryption.convertToSHA256(stored_salt + password);
                    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                    CustomLog.i(TAG, "Hashed Password : " + hash);
                    if (hash != null && stored_password.equals(hash) && stored_username.equals(username)) {
                        Toast.makeText(mContext, mContext.getString(R.string.success_offline_login), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        setOfflineLoginStatus(true);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.error_offline_login), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.offline_authentication_not_possible), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.offline_credentials_unavailable), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.offline_credentials_unavailable), Toast.LENGTH_LONG).show();
        }
    }
*/

    public void invalidateLoginCredentials() {
        mSharedPreference.edit().clear().apply();
        CustomLog.i(TAG, mContext.getString(R.string.invalidate_offline_login));
    }

    public Boolean getOfflineLoginStatus() {
        if (mSharedPreference.contains(mContext.getString(R.string.offline_login_status))) {
            return mSharedPreference.getBoolean(mContext.getString(R.string.offline_login_status), false);
        }
        return false;
    }

    public void setOfflineLoginStatus(Boolean status) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(
                mContext.getString(R.string.offline_login_status), status);
        editor.apply();
    }

    public void offline_login(String username, String password)
    {
        SQLiteDatabase db_1 = IntelehealthApplication.inteleHealthDatabaseHelper.getReadableDatabase();
        Cursor c = db_1.rawQuery("SELECT * FROM tbl_user_credentials",null);

        String hash_de_password = null;

        if(c.moveToFirst() && c != null)
        {
            //String user_decode = c.getString(c.getColumnIndexOrThrow("username"));
            String pass_decode = c.getString(c.getColumnIndexOrThrow("password"));
            CustomLog.d("pass_read", "pass_read"+pass_decode);

            try {
                CustomLog.d("MICE", "MICE: "+getSalt_DATA());
                //hash_de_email = StringEncryption.convertToSHA256(salt_getter_setter.getSalt() + user_decode);
                hash_de_password = StringEncryption.convertToSHA256(getSalt_DATA() + password);


            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                FirebaseCrashlytics.getInstance().recordException(e);
            }
            CustomLog.d("HASH","HASH: "+hash_de_password);
        }
        else
        {
            CustomLog.d("OFFLINE_C_EMPTY", "OFFLINE_C_EMPTY : "+c);
        }
        c.close();

        String[] cols = {username,hash_de_password};
        CustomLog.d("Column","Column: "+username+" "+hash_de_password);

        SQLiteDatabase db = IntelehealthApplication.inteleHealthDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "tbl_user_credentials",
                null,
                "username=? AND password=?",
                cols, null,null,null
                );

        if(cursor.moveToFirst())
        {
            user = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            pass = cursor.getString(cursor.getColumnIndexOrThrow("password"));
           chw_name = cursor.getString(cursor.getColumnIndexOrThrow("chwname"));
            provider_uuid = cursor.getString(cursor.getColumnIndexOrThrow("provider_uuid_cred"));
            creator_uuid = cursor.getString(cursor.getColumnIndexOrThrow("creator_uuid_cred"));
           // CustomLog.d("OFF_USER","DB_DATA"+user+" "+pass);
            CustomLog.d("OFF_USER","DB_DATA"+user+" "+pass+" " +chw_name+" "+provider_uuid+" "+creator_uuid);


           sessionManager.setProviderID(provider_uuid);
           sessionManager.setCreatorID(creator_uuid);
           sessionManager.setChwname(chw_name);
                Intent intent = new Intent(mContext, HomeScreenActivity_New.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                setOfflineLoginStatus(true);
                mContext.startActivity(intent);
            Toast.makeText(mContext, mContext.getString(R.string.success_offline_login), Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(mContext, mContext.getString
                    (R.string.error_incorrect_password), Toast.LENGTH_SHORT).show();
        }
        
        cursor.close();

    }

    public String getSalt_DATA() {
        BufferedReader reader = null;
        String salt = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open("salt.env")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                salt = mLine;
                CustomLog.d("SA", "SA " + salt);
            }
        } catch (Exception e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    //log the exception
                }
            }
        }
        return salt;

    }
}
