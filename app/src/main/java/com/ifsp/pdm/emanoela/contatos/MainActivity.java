package com.ifsp.pdm.emanoela.contatos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.ifsp.pdm.emanoela.contatos.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private final int BROWSER_REQUEST_CODE = 0;
    private final int CALL_PHONE_PREMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.celularCB.setOnClickListener(v -> {
            if (((CompoundButton) v).isChecked()) {
                activityMainBinding.celularET.setVisibility(View.VISIBLE);
            } else {
                activityMainBinding.celularET.setVisibility(View.GONE);
            }
        });
    }

    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.salvarBTN:
                break;
            case R.id.emailBTN:
                Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO);
                enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL, activityMainBinding.emailET.getText().toString());
                startActivity(enviarEmailIntent);
                break;
            case R.id.ligarBTN:
                verifyCallPhonePermission();
                break;
            case R.id.siteBTN:
                Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
                abrirNavegadorIntent.setData(Uri.parse(activityMainBinding.siteET.getText().toString()));
                startActivityForResult(abrirNavegadorIntent, BROWSER_REQUEST_CODE);
                break;
            case R.id.pdfBTN:
                break;
        }
    }

    private void verifyCallPhonePermission() {
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + activityMainBinding.telefoneET.getText().toString()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(ligarIntent);
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PREMISSION_REQUEST_CODE);
            }
        } else {
            startActivity(ligarIntent);
        }
    }
}