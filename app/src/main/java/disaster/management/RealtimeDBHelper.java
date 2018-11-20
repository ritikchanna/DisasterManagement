package disaster.management;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RealtimeDBHelper {


    Context mContext;
    DatabaseReference databaseReference;
    public RealtimeDBHelper(Context context){
        mContext=context;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("Ritik", "RealtimeDBHelper()");
    }



public void writeRequest(Listener listener, int RequestCode,String ID,String Name, String Location, String Message, String Contact, @Nullable String Status, @Nullable String Time) {
if(Status==null) Status="Sent";
if(Time==null) Time=String.valueOf(System.currentTimeMillis());
    Request request = new Request();
    request.setContact(Contact);
    request.setLocation(Location);
    request.setMessage(Message);
    request.setName(Name);
    request.setStatus(Status);
    request.setTime(Time);
    request.setUID(ID);
    databaseReference.child("Requests").child(request.getUID()).setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            listener.OnDownloadResult(RequestCode,request.getUID());
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            listener.OnErrorDownloadResult(RequestCode);
        }
    });
}


public void readRequest(Listener listener,int RequestCode){
        databaseReference.child("Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Request> requests= new ArrayList<>();
                Log.d("Ritik " ,"Count"+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Request request = postSnapshot.getValue(Request.class);
                    requests.add(request);
                }
                listener.OnDownloadResult(RequestCode,requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.OnErrorDownloadResult(RequestCode);
            }
        });
}





}

