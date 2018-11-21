package disaster.management.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import disaster.management.Constants;
import disaster.management.Listener;
import disaster.management.Myadapter;
import disaster.management.R;
import disaster.management.RealtimeDBHelper;
import disaster.management.Request;

public class VolActivity extends Activity implements Listener {
    private RealtimeDBHelper realtimeDBHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Request> requests;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol);
        realtimeDBHelper= new RealtimeDBHelper(VolActivity.this);
        realtimeDBHelper.readRequest(VolActivity.this,Constants.REQUEST_READ);
        mRecyclerView = (RecyclerView) findViewById(R.id.requests_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        progressBar=findViewById(R.id.progress_request_list);
        requests=new ArrayList<>();
        mAdapter = new Myadapter(requests,this);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void OnDownloadResult(int ResponseCode, Object Response) {
        requests.clear();
        requests.addAll((ArrayList<Request>)Response);
        mAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void OnErrorDownloadResult(int ResponseCode) {
        Toast.makeText(VolActivity.this,"Something went again,Retrying",Toast.LENGTH_SHORT).show();
        realtimeDBHelper.readRequest(VolActivity.this,Constants.REQUEST_READ);
        progressBar.setVisibility(View.INVISIBLE);
    }



}
