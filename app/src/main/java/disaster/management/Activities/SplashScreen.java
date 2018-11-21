package disaster.management.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import disaster.management.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
            launchMain();
        else
        launchAuthUI();
    }

    public void launchMain(){
        startActivity(new Intent(SplashScreen.this,MainActivity.class));
        finish();
    }

    public void launchAuthUI(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
        //        new AuthUI.IdpConfig.TwitterBuilder().build()
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppThemeFirebaseAuth)
                       // .setLogo(R.drawable.ic_launcher_foreground)
                        .build(),
                98);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 98) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                launchMain();
                finish();
            } else {
                //Toast.makeText(SplashScreen.this,"Please Login to use the app",Toast.LENGTH_SHORT).show();
                //launchAuthUI();
                finish();
            }
        }
    }

}
