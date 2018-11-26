package disaster.management.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import disaster.management.Constants;
import disaster.management.Listener;
import disaster.management.R;
import disaster.management.RealtimeDBHelper;

public class VicActivity extends Activity implements Listener {
    EditText et_message,et_phone,et_name;
    Button btn_submit,btn_call,btn_sms;
    TextView tv_status;
    LocationManager locationManager;
    private RealtimeDBHelper realtimeDBHelper;
    FirebaseAuth firebaseAuth;
    Spinner category_spinner;
    String category = "Uncategorized";
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //todo check request status
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vic);

        et_message= findViewById(R.id.et_message);
        et_phone = findViewById(R.id.et_phone);
        et_name=findViewById(R.id.et_name);
        btn_submit = findViewById(R.id.btn_send);
        btn_call = findViewById(R.id.btn_call);
        btn_sms= findViewById(R.id.btn_sms);
        category_spinner=findViewById(R.id.spinner_category);
        progressBar=findViewById(R.id.progressBar_vic);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category="Uncategorized";
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + "+919582286528"));
               startActivity(intent);
            }
        });
        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"+"+919582286528"));
                intent.putExtra("sms_body","Name: "+et_name.getText().toString()+"\nPosition: "+getLatLon()+"\nMessage: "+et_message.getText().toString());
                startActivity(intent);
            }
        });
        tv_status = findViewById(R.id.tv_status);
        firebaseAuth = FirebaseAuth.getInstance();
        et_name.setText(firebaseAuth.getCurrentUser().getDisplayName()+"");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        realtimeDBHelper = new RealtimeDBHelper(VicActivity.this);
        realtimeDBHelper.readRequest(this,Constants.REQUEST_READ,firebaseAuth.getCurrentUser().getEmail().replace('.', '&') + firebaseAuth.getCurrentUser().getPhoneNumber());
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(firebaseAuth.getCurrentUser().getDisplayName()!=null||et_name.getText().toString().length()>2) {
                   realtimeDBHelper.writeRequest(VicActivity.this, Constants.REQUEST_SUBMIT, firebaseAuth.getCurrentUser().getEmail().replace('.', '&') + firebaseAuth.getCurrentUser().getPhoneNumber(), et_name.getText().toString(), getLatLon(), et_message.getText().toString(), et_phone.getText().toString(), category, null, null);
                    progressBar.setVisibility(View.VISIBLE);
                    btn_submit.setEnabled(false);if(firebaseAuth.getCurrentUser().getDisplayName()==null)
            et_name.setVisibility(View.VISIBLE);
               } else
                    et_name.setError("Please input your name");
            }
        });

    }


    @Override
    public void OnDownloadResult(int ResponseCode, Object Response) {
        if(ResponseCode==Constants.REQUEST_READ)
        {
            Log.d("Ritik", "OnDownloadResult: readout"+Response);
            progressBar.setVisibility(View.INVISIBLE);
            btn_submit.setEnabled(true);
            int status=(int)Response;
            if(status==0)
            tv_status.setText("Request submitted successfully");
            else if(status==1)
                tv_status.setText("Request Accepted");
            else if(status==2)
                tv_status.setText("Request Denied");
            tv_status.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            btn_submit.setEnabled(true);
            tv_status.setText("Request submitted successfully");
            tv_status.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void OnErrorDownloadResult(int ResponseCode) {
        if(ResponseCode==Constants.REQUEST_SUBMIT) {
            Toast.makeText(VicActivity.this, "Something Went Wrong, PLease try again", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            btn_submit.setEnabled(true);
        }

    }

    public String getLatLon(){
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        double longitude=77.523653;
        double latitude=28.417412;
        if (ContextCompat.checkSelfPermission(VicActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {



            ActivityCompat.requestPermissions(VicActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    99);
        }
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
        return String.valueOf(latitude)+","+String.valueOf(longitude);
    }
}
