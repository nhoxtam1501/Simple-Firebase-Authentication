package com.ducku.firebaseauthentication;

import static com.firebase.ui.auth.ErrorCodes.NO_NETWORK;
import static com.firebase.ui.auth.ErrorCodes.UNKNOWN_ERROR;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.AuthUI;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            authenticate();
        }
    }

    private void authenticate() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(getAuthProviderList())
                        .setIsSmartLockEnabled(false)
                        .build(),
                REQUEST_CODE);
    }

    private List<AuthUI.IdpConfig> getAuthProviderList() {
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        providers.add(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        return providers;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == ResultCodes.OK) {
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            } else {
                if (response == null) {
                    return;
                }
                if (response.getErrorCode() == NO_NETWORK)
                    return;
                if (response.getErrorCode() == UNKNOWN_ERROR)
                    return;
            }
        }
    }
}