package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerFragmentItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
import com.myrescribe.R;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.notification.AppointmentAlarmTask;
import com.myrescribe.notification.DosesAlarmTask;
import com.myrescribe.notification.InvestigationAlarmTask;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.customesViews.ScrollableImageView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by jeetal on 28/6/17.
 */

public class HomePageActivity extends DrawerActivity {

    private Context mContext;
    private String mGetMealTime;
    String breakFastTime = "";
    String lunchTime = "";
    String dinnerTime = "";
    String snacksTime = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer_new);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = HomePageActivity.this;
        if (getIntent().getBooleanExtra(MyRescribeConstants.ALERT, true))
            notificationForMedicine();
        drawerConfiguration();
    }

    private void setImageBitmap(Bitmap bmp) {
        ImageView imageView = new ScrollableImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(bmp.getWidth(), bmp.getHeight() - toolbar.getHeight()));
        imageView.setImageBitmap(bmp);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        container.addView(imageView);
    }

    /* private class BitmapLoaderTask extends AsyncTask<String, Void, Bitmap> {

         private ProgressBar progress;

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progress = (ProgressBar) findViewById(android.R.id.progress);
         }

         @Override
         protected Bitmap doInBackground(String... params) {
             AssetManager assets = getAssets();
             Bitmap bmp = null;
             try {
                 bmp = BitmapFactory.decodeStream(assets.open(params[0]));
             } catch (IOException e) {
                 // Log.e(DEBUG_TAG, e.getMessage(), e);
             }
             return bmp;
         }

         @Override
         protected void onPostExecute(Bitmap result) {
             super.onPostExecute(result);
             progress.setVisibility(View.INVISIBLE);
             setImageBitmap(result);
         }
     }
 */
    private void notificationForMedicine() {

        AppDBHelper appDBHelper = new AppDBHelper(mContext);
        Cursor cursor = appDBHelper.getPreferences("1");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                breakFastTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.BREAKFAST_TIME));
                lunchTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.LUNCH_TIME));
                dinnerTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.DINNER_TIME));
                snacksTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.SNACKS_TIME));
                cursor.moveToNext();
            }
        }
        cursor.close();

        String times[] = {breakFastTime, lunchTime, dinnerTime, snacksTime};
        String date = CommonMethods.getCurrentTimeStamp(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY);

        new DosesAlarmTask(mContext, times, date).run();
        new InvestigationAlarmTask(mContext, "9:00 AM", getResources().getString(R.string.investigation_msg)).run();
        new AppointmentAlarmTask(mContext, "9:00 AM", getResources().getString(R.string.appointment_msg)).run();
    }

    @Override
    public void onBackPressed() {

        closeDrawer();

        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {
            Calendar c = Calendar.getInstance();
            int hour24 = c.get(Calendar.HOUR_OF_DAY);
            int Min = c.get(Calendar.MINUTE);
            mGetMealTime = CommonMethods.getMealTime(hour24, Min, this);
            if (mGetMealTime.equals(getString(R.string.break_fast))) {
                Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                intentNotification.putExtra(MyRescribeConstants.MEDICINE_SLOT, getString(R.string.breakfast_medication));
                intentNotification.putExtra(MyRescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                intentNotification.putExtra(MyRescribeConstants.TIME, breakFastTime);
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNotification);

            } else if (mGetMealTime.equals(getString(R.string.mlunch))) {
                Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                intentNotification.putExtra(MyRescribeConstants.MEDICINE_SLOT, getString(R.string.lunch_medication));
                intentNotification.putExtra(MyRescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                intentNotification.putExtra(MyRescribeConstants.TIME, lunchTime);
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNotification);

            } else if (mGetMealTime.equals(getString(R.string.msnacks))) {
                Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                intentNotification.putExtra(MyRescribeConstants.MEDICINE_SLOT, getString(R.string.snacks_medication));
                intentNotification.putExtra(MyRescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                intentNotification.putExtra(MyRescribeConstants.TIME, snacksTime);
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNotification);

            } else if (mGetMealTime.equals(getString(R.string.mdinner))) {
                Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                intentNotification.putExtra(MyRescribeConstants.MEDICINE_SLOT, getString(R.string.dinner_medication));
                intentNotification.putExtra(MyRescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                intentNotification.putExtra(MyRescribeConstants.TIME, dinnerTime);
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNotification);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.doctor_details) {
            Intent intent = new Intent(mContext, DoctorListActivity.class);
            startActivity(intent);
        } else if (id == R.id.investigations) {
            Intent intent = new Intent(mContext, InvestigationActivity.class);

            startActivity(intent);
        } else if (id == R.id.onGoingMedication) {
            Intent intent = new Intent(HomePageActivity.this, PrescriptionActivity.class);
            startActivity(intent);
        } else if (id == R.id.appointments) {
            Intent intent = new Intent(mContext, AppointmentActivity.class);
            startActivity(intent);
        } else if (id == R.id.records) {
            Intent intent = new Intent(mContext, MyRecordsActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    private void logout() {
        String baseUrl = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.SERVER_PATH, mContext);
        MyRescribePreferencesManager.clearSharedPref(mContext);
        MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.SERVER_PATH, baseUrl, mContext);
        MyRescribePreferencesManager.putString(getString(R.string.logout), "" + 1, mContext);

        AppDBHelper appDBHelper = new AppDBHelper(mContext);
        appDBHelper.deleteDatabase();

        new DosesAlarmTask(mContext, null, null).run();
        new AppointmentAlarmTask(mContext, null, null).run();
        new InvestigationAlarmTask(mContext, null, null).run();

        Intent intent = new Intent(mContext, LoginMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private void drawerConfiguration() {
        setDrawerTheme(
                new DrawerTheme(this)
                        .setBackgroundColorRes(R.color.recentblue)
                        .setTextColorPrimaryRes(R.color.white)
                        .setTextColorSecondaryRes(R.color.white)
        );
       /* addItems(new DrawerItem()
                        .setTextPrimary(getString(R.string.app_name))
                        .setTextSecondary(getString(R.string.enter_email_id)),
                new DrawerFragmentItem()
                        .setFragment(new ListFragment())
                        .setTextPrimary(getString(R.string.enter_mobile_no)),
                new DrawerFragmentItem()
                        .setFragment(new Fragment())
                        .setImage(ContextCompat.getDrawable(this, R.drawable.investigation))
                        .setTextPrimary(getString(R.string.investigation))
                        .setTextSecondary(getString(R.string.investigations))
        );*/

        addItems(new DrawerItem()
                        .setTextPrimary(getString(R.string.going_medication))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_prescription)),
                new DrawerItem()
                        .setTextPrimary(getString(R.string.doctor_details))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_doctor_visit)),
                new DrawerItem()
                        .setTextPrimary(getString(R.string.investigation))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_investigations)),
                new DrawerItem()
                        .setTextPrimary(getString(R.string.appointments))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_appointments)),
                new DrawerItem()
                        .setTextPrimary(getString(R.string.my_records))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_my_records)),
                new DrawerItem()
                        .setTextPrimary(getString(R.string.logout))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_logout))
        );
        setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long itemID, int position) {
                //  selectItem(position);
                String id = item.getTextPrimary();
                if (id.equalsIgnoreCase(getString(R.string.doctor_details))) {
                    Intent intent = new Intent(mContext, DoctorListActivity.class);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.investigation))) {
                    Intent intent = new Intent(mContext, InvestigationActivity.class);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.going_medication))) {
                    Intent intent = new Intent(mContext, PrescriptionActivity.class);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.appointments))) {
                    Intent intent = new Intent(mContext, AppointmentActivity.class);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.my_records))) {
                    Intent intent = new Intent(mContext, MyRecordsActivity.class);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.logout))) {
                    logout();
                }
                closeDrawer();
            }
        });

        // TODO : HARDEDCODED will get remove once done with APIs.
        addProfile(new DrawerProfile()
                .setId(1)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.big))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.drawer_header_bg))
                .setName("Mr.Avinash Deshpande")
                .setDescription("avinash_deshpande@gmail.com")
        );
        addProfile(new DrawerProfile()
                .setId(2)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.small_copy))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.drawer_header_bg))
                .setName("Mr.Sandeep Deshmukh ")
                .setDescription("sandeep_deshmukh@gmail.com")
        );

    }
}
