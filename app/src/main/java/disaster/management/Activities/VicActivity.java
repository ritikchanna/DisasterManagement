package disaster.management.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import disaster.management.Constants;
import disaster.management.Listener;
import disaster.management.R;
import disaster.management.RealtimeDBHelper;

public class VicActivity extends Activity implements Listener {
    EditText et_message,et_phone;
    Button btn_submit;
    TextView tv_status;
    LocationManager locationManager;
    private RealtimeDBHelper realtimeDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //todo check request status
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vic);
        et_message= findViewById(R.id.et_message);
        et_phone = findViewById(R.id.et_phone);
        btn_submit = findViewById(R.id.btn_send);
        tv_status = findViewById(R.id.tv_status);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        realtimeDBHelper = new RealtimeDBHelper(VicActivity.this);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
                double longitude=77.52136;
                double latitude=28.42368;
                try {
                    Log.d("Ritik", "fetching location ");
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location!=null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    }
                    Log.d("Ritik", "onClick: "+latitude+"   "+longitude);
                }catch (SecurityException e){
                    if (ContextCompat.checkSelfPermission(VicActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {



                            ActivityCompat.requestPermissions(VicActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    99);
                        }

                    e.printStackTrace();
                }
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                realtimeDBHelper.writeRequest(VicActivity.this,Constants.REQUEST_SUBMIT,firebaseAuth.getCurrentUser().getEmail().replace('.','&')+firebaseAuth.getCurrentUser().getPhoneNumber(),firebaseAuth.getCurrentUser().getDisplayName(),String.valueOf(latitude)+","+String.valueOf(longitude),et_message.getText().toString(),et_phone.getText().toString(),null,null);
            }
        });

    }


    @Override
    public void OnDownloadResult(int ResponseCode, Object Response) {
    tv_status.setText("Request submitted successfully");
    tv_status.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnErrorDownloadResult(int ResponseCode) {
        Toast.makeText(VicActivity.this,"Something Went Wrong, PLease try again",Toast.LENGTH_SHORT).show();

    }
}
