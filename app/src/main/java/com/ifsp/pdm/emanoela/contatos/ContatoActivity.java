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
            case R.id.pdfBTN:
                break;
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