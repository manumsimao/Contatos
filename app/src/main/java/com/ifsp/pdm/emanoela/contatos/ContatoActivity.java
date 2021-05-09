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

import com.ifsp.pdm.emanoela.contatos.databinding.ActivityContatoBinding;

public class ContatoActivity extends AppCompatActivity {
    private ActivityContatoBinding activityContatoBinding;
    private final int CALL_PHONE_PREMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityContatoBinding = ActivityContatoBinding.inflate(getLayoutInflater());
        setContentView(activityContatoBinding.getRoot());

        activityContatoBinding.celularCB.setOnClickListener(v -> {
            if (((CompoundButton) v).isChecked()) {
                activityContatoBinding.celularET.setVisibility(View.VISIBLE);
            } else {
                activityContatoBinding.celularET.setVisibility(View.GONE);
            }
        });
    }

    public void onClickButton(View view) {
        Contato contato = criarContato();
        switch (view.getId()) {
            case R.id.salvarBTN:
                Intent retornoIntent = new Intent();
                retornoIntent.putExtra(Intent.EXTRA_USER, contato);
                setResult(RESULT_OK, retornoIntent);
                finish();
                break;
            case R.id.emailBTN:
                Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO);
                enviarEmailIntent.setData(Uri.parse("mailto:"));
                enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {contato.getEmail()});
                enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contato");
                enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, contato.toString());
                startActivity(Intent.createChooser(enviarEmailIntent, "Ops"));
                break;
            case R.id.ligarBTN:
                verifyCallPhonePermission();
                break;
            case R.id.siteBTN:
                Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
                abrirNavegadorIntent.setData(Uri.parse(contato.getSite()));
                startActivity(Intent.createChooser(abrirNavegadorIntent, "Ops"));
                break;
            case R.id.pdfBTN:
                break;
        }
    }

    private void verifyCallPhonePermission() {
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + activityContatoBinding.telefoneET.getText().toString()));
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

    private Contato criarContato () {
        Contato contato = new Contato();
        contato.setNome(activityContatoBinding.nomeCompletoET.getText().toString());
        contato.setEmail(activityContatoBinding.emailET.getText().toString());
        contato.setTipoTelefone(activityContatoBinding.tipoTelefoneSP.getSelectedItem().toString());
        contato.setTelefone(activityContatoBinding.telefoneET.getText().toString());
        contato.setAddCelular(activityContatoBinding.celularCB.isChecked());
        if(activityContatoBinding.celularCB.isChecked()){
            contato.setCelular(activityContatoBinding.celularET.getText().toString());
        }
        String link = activityContatoBinding.siteET.getText().toString();
        if(!link.contains("http://") || !link.contains("https://")){
            link = "http://" + link;
        }
        contato.setSite(link);

        return contato;
    }
}