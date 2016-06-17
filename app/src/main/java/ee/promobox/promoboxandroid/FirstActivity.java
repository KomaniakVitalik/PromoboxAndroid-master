package ee.promobox.promoboxandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.logging.Logger;

public class FirstActivity extends Activity {

    private EditText et;

    private void hideSystemUI() {

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        hideSystemUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first);
        et = (EditText) findViewById(R.id.first_start_device_id);

        Button b = (Button) findViewById(R.id.first_start_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsAndStart();
            }
        });

        Button settings = (Button) findViewById(R.id.first_activity_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });

    }

    private void checkPermissionsAndStart() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    start();
                }
            }
            return;
        }
        //if Version int < M skip permission checks and start next activity
        start();

    }

    private void start() {

        String uuid = et.getText().toString();

        if (TextUtils.isEmpty(uuid)) {
            Toast.makeText(FirstActivity.this, "Wrong uuid", Toast.LENGTH_LONG).show();
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", MainActivity.RESULT_FINISH_FIRST_START);
        returnIntent.putExtra("deviceUuid", uuid);
        setResult(RESULT_OK, returnIntent);
        FirstActivity.this.finish();

    }

    private boolean isPermissionGranted(String permission) {

        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndStart();
                }
                break;
        }

    }


}
