package edu.daae419skku.teamprojectboard;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyIID";

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefersh() 호출");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refershed Token: "+refreshedToken);

    }

}
