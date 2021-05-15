package com.ifsp.pdm.emanoela.contatos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ifsp.pdm.emanoela.contatos.databinding.ActivityContatoBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContatoActivity extends AppCompatActivity {
    private ActivityContatoBinding activityContatoBinding;
    private Contato contato;
    private int posicao = -1;

    private final int PERMISSAO_ESCRITA_ARMAZENAMENTO_EXTERNO = 0;

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

        contato = (Contato) getIntent().getSerializableExtra(Intent.EXTRA_USER);
        if(contato != null){
            posicao = getIntent().getIntExtra(Intent.EXTRA_INDEX, -1);

            boolean ativo = posicao!=-1;
            alterarAtivacaoViews(ativo);

            activityContatoBinding.nomeCompletoET.setText(contato.getNome());
            activityContatoBinding.emailET.setText(contato.getEmail());
            if(contato.tipoTelefone.equals("Residencial")) {
                activityContatoBinding.residencialRB.setChecked(true);
            }else{
                activityContatoBinding.comercialRB.setChecked(true);
            }
            activityContatoBinding.telefoneET.setText(contato.getTelefone());
            activityContatoBinding.celularCB.setChecked(contato.isAddCelular());
            activityContatoBinding.celularET.setText(contato.getCelular());
            activityContatoBinding.siteET.setText(contato.getSite());

        }else {
            getSupportActionBar().setSubtitle("Novo contato");
        }
    }

    private void alterarAtivacaoViews(boolean ativo) {
        activityContatoBinding.nomeCompletoET.setEnabled(ativo);
        activityContatoBinding.emailET.setEnabled(ativo);
        activityContatoBinding.telefoneET.setEnabled(ativo);
        activityContatoBinding.tipoTelefoneRG.setEnabled(ativo);
        activityContatoBinding.celularCB.setEnabled(ativo);
        activityContatoBinding.celularET.setEnabled(ativo);
        activityContatoBinding.siteET.setEnabled(ativo);
    }

    public void onClickButton(View view) {
         contato = criarContato();
        switch (view.getId()) {
            case R.id.salvarBTN:
                Intent retornoIntent = new Intent();
                retornoIntent.putExtra(Intent.EXTRA_USER, contato);
                retornoIntent.putExtra(Intent.EXTRA_INDEX, posicao);
                setResult(RESULT_OK, retornoIntent);
                finish();
                break;
            case R.id.pdfBTN:
                verificarPermissaoEscritaArmazenamentoExterno();
                break;
        }
    }

    private void verificarPermissaoEscritaArmazenamentoExterno() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_ESCRITA_ARMAZENAMENTO_EXTERNO);
            }else {
                gerarDocumentoPDF();
            }
        }else {
            gerarDocumentoPDF();
        }
    }

    private void gerarDocumentoPDF() {
        View conteudo = activityContatoBinding.getRoot();
        int largura = conteudo.getWidth();
        int altura = conteudo.getHeight();

        PdfDocument documentoPdf = new PdfDocument();

        PdfDocument.PageInfo configuracaoPagina = new PdfDocument.PageInfo.Builder(largura, altura, 1).create();
        PdfDocument.Page pagina = documentoPdf.startPage(configuracaoPagina);

        conteudo.draw(pagina.getCanvas());
        documentoPdf.finishPage(pagina);

        File diretorioDocumentos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        try {
            File documento = new File(diretorioDocumentos, contato.getNome().replace(" ", "_") + ".pdf");
            documento.createNewFile();
            documentoPdf.writeTo(new FileOutputStream(documento));
            documentoPdf.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSAO_ESCRITA_ARMAZENAMENTO_EXTERNO){
            verificarPermissaoEscritaArmazenamentoExterno();
        }
    }

    private Contato criarContato () {
        Contato contato = new Contato();
        contato.setNome(activityContatoBinding.nomeCompletoET.getText().toString());
        contato.setEmail(activityContatoBinding.emailET.getText().toString());
        RadioButton radioButton = (RadioButton) findViewById(activityContatoBinding.tipoTelefoneRG.getCheckedRadioButtonId());
        contato.setTipoTelefone((String)radioButton.getText());
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