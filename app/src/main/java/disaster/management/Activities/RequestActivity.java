package disaster.management.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import disaster.management.Constants;
import disaster.management.Listener;
import disaster.management.R;
import disaster.management.RealtimeDBHelper;

public class RequestActivity extends AppCompatActivity implements OnMapReadyCallback,Listener {
    String locationRaw,name,status,message,time,uid,contact,category;
    TextView name_tv,time_tv,status_tv,message_tv,category_tv;
    Button btn_accept,btn_deny,btn_call;
    private RealtimeDBHelper realtimeDBHelper;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        realtimeDBHelper= new RealtimeDBHelper(RequestActivity.this);
        mapFragment.getMapAsync(this);
        name_tv= findViewById(R.id.name_tv);
        time_tv= findViewById(R.id.time_tv);
        status_tv=findViewById(R.id.status_tv);
        message_tv=findViewById(R.id.message_tv);
        btn_accept= findViewById(R.id.btn_accept);
        category_tv=findViewById(R.id.category_tv);
        btn_deny=findViewById(R.id.btn_reject);
        btn_call= findViewById(R.id.btn_call);
        locationRaw=getIntent().getStringExtra("location");
        name=getIntent().getStringExtra("name");
        time=getIntent().getStringExtra("time");
        status=getIntent().getStringExtra("status");
        message=getIntent().getStringExtra("message");
        uid=getIntent().getStringExtra("uid");
        contact=getIntent().getStringExtra("contact");
        category=getIntent().getStringExtra("category");
        name_tv.setText(name);
        time_tv.setText("Generated on: "+convertTime(time));
        status_tv.setText("Request Status: "+status);
        message_tv.setText("Message: "+message);
        btn_call.setText("Call Victim: "+contact);
        category_tv.setText("Category: "+category);
        progressBar=findViewById(R.id.progressbar_request);
        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realtimeDBHelper.writeRequest(RequestActivity.this,Constants.REQUEST_SUBMIT,uid,name,locationRaw,message,contact,category,"denied",time);
                progressBar.setVisibility(View.VISIBLE);
                btn_accept.setEnabled(false);
                btn_deny.setEnabled(false);
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realtimeDBHelper.writeRequest(RequestActivity.this,Constants.REQUEST_SUBMIT,uid,name,locationRaw,message,contact,category,"accepted",time);
                progressBar.setVisibility(View.VISIBLE);
                btn_accept.setEnabled(false);
                btn_deny.setEnabled(false);
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + contact));
                RequestActivity.this.startActivity(intent);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng location = new LatLng(Double.valueOf(locationRaw.split(",")[0]), Double.valueOf(locationRaw.split(",")[1]));
        googleMap.addMarker(new MarkerOptions().position(location)
                .title("Victim's Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));

    }



    @Override
    public void OnDownloadResult(int ResponseCode, Object Response) {
        progressBar.setVisibility(View.INVISIBLE);
        btn_accept.setEnabled(true);
        btn_deny.setEnabled(true);
        Toast.makeText(RequestActivity.this,"Status for the request has been modified",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void OnErrorDownloadResult(int ResponseCode) {
        progressBar.setVisibility(View.INVISIBLE);
        btn_accept.setEnabled(true);
        btn_deny.setEnabled(true);
        Toast.makeText(RequestActivity.this,"Something went wrong, Please try again",Toast.LENGTH_SHORT).show();

    }
    public String convertTime(String millis){
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        Date result = new Date(Long.valueOf(millis));
        return simple.format(result);
    }
}
