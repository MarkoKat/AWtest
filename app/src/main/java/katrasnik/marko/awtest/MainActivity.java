package katrasnik.marko.awtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Accelerometer_Provider;
import com.aware.providers.Applications_Provider;
import com.aware.providers.Battery_Provider;
import com.aware.providers.Gyroscope_Provider;
import com.aware.providers.Light_Provider;
import com.aware.providers.Screen_Provider;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    public int stevec = 0;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise AWARE
        Intent aware = new Intent(this, Aware.class);
        this.startService(aware);
        //Activate Accelerometer
        Aware.setSetting(this, Aware_Preferences.STATUS_ACCELEROMETER, true);
        //Set sampling frequency
        Aware.setSetting(this, Aware_Preferences.FREQUENCY_ACCELEROMETER, 50000);
        //Apply settings
        Aware.startSensor(this, Aware_Preferences.STATUS_ACCELEROMETER);

        //Light sensor
        Aware.setSetting(this, Aware_Preferences.STATUS_LIGHT, true);
        Aware.setSetting(this, Aware_Preferences.FREQUENCY_LIGHT, 50000);
        Aware.startSensor(this, Aware_Preferences.STATUS_LIGHT);

        //Applications
        Aware.setSetting(this, Aware_Preferences.STATUS_GYROSCOPE, true);
        Aware.setSetting(this, Aware_Preferences.FREQUENCY_GYROSCOPE, 50000);
        Aware.startSensor(this, Aware_Preferences.STATUS_GYROSCOPE);

        Aware.setSetting(this, Aware_Preferences.STATUS_BATTERY, true);
        Aware.startSensor(this, Aware_Preferences.STATUS_BATTERY);

        Log.i(TAG, "test");

        //BeriBazo();
    }

    private static ContextReceiver contextReceiver = new ContextReceiver();
    public static class ContextReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Action for context here.
            Log.i(TAG, "received");
            //MainActivity test1 = new MainActivity();
            //test1.povecajStevec();
        }
    }

    public void povecajStevec() {
        stevec += 1;
        TextView textTest = findViewById(R.id.textStevec);
        textTest.setText(stevec);
    }

    public void beriBazoAcc(View v) {
        Cursor accelerometer_data = getContentResolver().query(
                Accelerometer_Provider.Accelerometer_Data.CONTENT_URI, null,
                "accuracy > 1", null, "timestamp ASC");


        if(accelerometer_data != null) {
            try {
                int n = accelerometer_data.getCount();
                accelerometer_data.moveToLast();
                double x = accelerometer_data.getDouble(accelerometer_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_0));
                double y = accelerometer_data.getDouble(accelerometer_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_1));
                double z = accelerometer_data.getDouble(accelerometer_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_2));

                Log.i(TAG, "Koordinate : " + x + " : " + y + " : " + z);
                TextView textTest = findViewById(R.id.textStevec);
                textTest.setText("Accelerometer : " + (double)Math.round(x * 10000d) / 10000d + " : " + (double)Math.round(y * 10000d) / 10000d + " : " + (double)Math.round(z * 10000d) / 10000d + " : "+ n);
            }
            catch (Exception e) {
                e.printStackTrace();
                TextView textTest = findViewById(R.id.textStevec);
                textTest.setText("No accelerometer data!");
            }

        /*
        do {
            double x = accelerometer_data.getDouble(accelerometer_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_0));
            Log.i(TAG, "Koordinata x: " + x);
            double y = accelerometer_data.getDouble(accelerometer_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_1));
            double z = accelerometer_data.getDouble(accelerometer_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_2));
            //do what you need with x,y,z variables
        } while (accelerometer_data.moveToNext());
        */
        }
        else {
            TextView textTest = findViewById(R.id.textStevec);
            textTest.setText("Ni še podatkov!");
        }
    }

    public void beriBazoLight(View v) {
        Cursor light_data = getContentResolver().query(
                Light_Provider.Light_Data.CONTENT_URI, null,
                null, null, "timestamp ASC");

        int n = light_data.getCount();
        Log.i(TAG, "Svetlost stevilo : " + n);

        try {
            //int n = light_data.getCount();
            light_data.moveToLast();
            double l = light_data.getDouble(light_data.getColumnIndex(Light_Provider.Light_Data.LIGHT_LUX));

            Log.i(TAG, "Svetlost : " + l);
            TextView textTest = findViewById(R.id.textLight);
            textTest.setText("Light : " + (double)Math.round(l * 10000d) / 10000d + " : "+ n);
        }
        catch (Exception e) {
            e.printStackTrace();
            TextView textTest = findViewById(R.id.textLight);
            textTest.setText("No light data!");
        }
    }

    public void beriBazoGyro(View v) {
        Cursor gyro_data = getContentResolver().query(
                Gyroscope_Provider.Gyroscope_Data.CONTENT_URI, null,
                null, null, "timestamp ASC");

        int n = gyro_data.getCount();
        Log.i(TAG, "Gyro stevilo : " + n + " --- " + gyro_data);

        try {
            //int n = light_data.getCount();
            gyro_data.moveToLast();
            double sc = gyro_data.getDouble(gyro_data.getColumnIndex(Gyroscope_Provider.Gyroscope_Data.VALUES_0));

            Log.i(TAG, "Gyro x : " + sc);
            TextView textTest = findViewById(R.id.textGyro);
            textTest.setText("Gyro : " + sc + " : " + n);
        }
        catch (Exception e) {
            e.printStackTrace();
            TextView textTest = findViewById(R.id.textGyro);
            textTest.setText("No gyro data!");
        }
    }

    public void beriBazoBatt(View v) {
        Cursor batt_data = getContentResolver().query(
                Battery_Provider.Battery_Data.CONTENT_URI, null,
                null, null, "timestamp ASC");

        try {
            int n = batt_data.getCount();
            Log.i(TAG, "Battery stevilo : " + n + " --- " + batt_data);
            batt_data.moveToLast();
            int sc = batt_data.getInt(batt_data.getColumnIndex(Battery_Provider.Battery_Data.LEVEL));

            Log.i(TAG, "Battery : " + sc);
            TextView textTest = findViewById(R.id.textBattery);
            textTest.setText("Battery : " + sc + " : " + n);
        }
        catch (Exception e) {
            e.printStackTrace();
            TextView textTest = findViewById(R.id.textBattery);
            textTest.setText("No battery data!");
        }
    }

    public void brisiBazo (View v) {
        deleteDatabaseMaster(Accelerometer_Provider.Accelerometer_Data.CONTENT_URI);
        deleteDatabaseMaster(Gyroscope_Provider.Gyroscope_Data.CONTENT_URI);
    }

    public void deleteDatabaseMaster (Uri databaseUri) {
        int mRowsDeleted = 0;
        mRowsDeleted = getContentResolver().delete(
                databaseUri,   // the user dictionary content URI
                null,                                  // the column to select on
                null                              // the value to compare to
        );

        Toast.makeText(this, "Database cleared", LENGTH_SHORT).show();
    }

}


