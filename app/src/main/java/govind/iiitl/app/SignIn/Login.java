package govind.iiitl.app.SignIn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import govind.iiitl.app.MainActivity;
import govind.iiitl.app.R;

public class Login extends AppCompatActivity {
    SharedPreferences sp;

    private static final int MY_REQUEST_CODE = 1;
    List<AuthUI.IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean("logged", false)) {
            goToMainActivity();
        } else {
            //Init Providers
            providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    //  new AuthUI.IdpConfig.PhoneBuilder().build(),
                    //  new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );
            showSignInOptions();
        }
    }

    private void goToMainActivity() {
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                //Get Current User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Show Email
                Toast.makeText(this, "Welcome " + user.getDisplayName() + "!", Toast.LENGTH_LONG).show();
                sp.edit().putBoolean("logged", true).apply();
                goToMainActivity();
            } else {
                Toast.makeText(this, "Some error occured", Toast.LENGTH_LONG).show();
            }
        }
    }
}
