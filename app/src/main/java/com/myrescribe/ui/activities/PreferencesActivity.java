package com.myrescribe.ui.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.myrescribe.R;
import com.myrescribe.helpers.database.AppDBHelper;
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;
import com.philliphsu.bottomsheetpickers.time.grid.GridTimePickerDialog;
import java.util.Calendar;
import java.util.GregorianCalendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "Preferences";
    @BindView(R.id.breakFastSelect)
    TextView mBreakFastSelect;
    @BindView(R.id.lunchTimeSelect)
    TextView mLunchTimeSelect;
    @BindView(R.id.dinnerTimeSelect)
    TextView mDinnerTimeSelect;
    @BindView(R.id.snackstSelect)
    TextView mSnackstSelect;
    @BindView(R.id.saveButton)
    Button mSaveButton;

    AppDBHelper appDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appDBHelper = AppDBHelper.getInstance(PreferencesActivity.this);
    }

    @OnClick({R.id.breakFastSelect, R.id.lunchTimeSelect,R.id.snackstSelect, R.id.dinnerTimeSelect, R.id.saveButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.breakFastSelect:
                setTime(mBreakFastSelect);
                break;
            case R.id.lunchTimeSelect:
                setTime(mLunchTimeSelect);
                break;
            case R.id.snackstSelect:
                setTime(mSnackstSelect);
                break;
            case R.id.dinnerTimeSelect:
                setTime(mDinnerTimeSelect);
                break;
            case R.id.saveButton:

                String breakFastSelect = "";
                String lunchTimeSelect = "";
                String dinnerTimeSelect = "";
                String snacksTimeSelect = "";

                if (!mBreakFastSelect.getText().toString().equals(""))
                    breakFastSelect = mBreakFastSelect.getText().toString();

                if (!mLunchTimeSelect.getText().toString().equals(""))
                    lunchTimeSelect = mLunchTimeSelect.getText().toString();

                if (!mSnackstSelect.getText().toString().equals(""))
                    snacksTimeSelect = mSnackstSelect.getText().toString();

                if (!mDinnerTimeSelect.getText().toString().equals(""))
                    dinnerTimeSelect = mDinnerTimeSelect.getText().toString();

                appDBHelper.insertPreferences("1", breakFastSelect, lunchTimeSelect,snacksTimeSelect, dinnerTimeSelect);

                break;
        }
    }

    private void setTime(final TextView textView) {
        Calendar now = Calendar.getInstance();
        GridTimePickerDialog dialog = GridTimePickerDialog.newInstance(
                new BottomSheetTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {
                        Calendar cal = new GregorianCalendar();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        textView.setText(DateFormat.getTimeFormat(PreferencesActivity.this).format(cal.getTime()));
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(PreferencesActivity.this));
        dialog.setThemeDark(false);
        dialog.show(getSupportFragmentManager(), TAG);
    }
}
