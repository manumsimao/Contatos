package com.ifsp.pdm.emanoela.contatos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ifsp.pdm.emanoela.contatos.databinding.ActivityContatosBinding;

import java.util.ArrayList;

public class ContatosActivity extends AppCompatActivity {
    private ActivityContatosBinding activityContatosBinding;
    private ArrayList<Contato> contatosList;
    private ContatosAdapter contatosAdapter;
    private final int NOVO_CONTATO_REQUEST_CODE = 0;
    private final int CALL_PHONE_PREMISSION_REQUEST_CODE = 1;
    private final int EDITAR_CONTATO_REQUEST_CODE = 2;
    private  Contato contato;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContatosBinding = ActivityContatosBinding.inflate(getLayoutInflater());
        setContentView(activityContatosBinding.getRoot());

        contatosList = new ArrayList<>();

        contatosAdapter = new ContatosAdapter(this, R.layout.view_contato, contatosList);

        activityContatosBinding.contatosLV.setAdapter(contatosAdapter);

        registerForContextMenu(activityContatosBinding.contatosLV);

        activityContatosBinding.contatosLV.setOnItemClickListener((parent, view, position, id) ->{
            contato = contatosList.get(position);
            Intent detalhesIntent = new Intent(this, ContatoActivity.class);
            detalhesIntent.putExtra(Intent.EXTRA_USER, contato);
            startActivity(detalhesIntent);
        } );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(activityContatosBinding.contatosLV);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contatos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.novoContatoMi){
            Intent novoContatoIntent = new Intent(this, ContatoActivity.class);
            startActivityForResult(novoContatoIntent, NOVO_CONTATO_REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NOVO_CONTATO_REQUEST_CODE && resultCode == RESULT_OK){
            Contato contato = (Contato) data.getSerializableExtra(Intent.EXTRA_USER);
            if (contato != null){
                contatosList.add(contato);
                contatosAdapter.notifyDataSetChanged();
            }
        }else {
            if (requestCode == EDITAR_CONTATO_REQUEST_CODE && resultCode ==RESULT_OK){
                Contato contato = (Contato) data.getSerializableExtra(Intent.EXTRA_USER);
                int posicao = data.getIntExtra(Intent.EXTRA_INDEX, -1);
                if (contato != null && posicao != -1){
                    contatosList.remove(posicao);
                    contatosList.add(posicao, contato);
                    contatosAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_contato, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        contato = contatosAdapter.getItem(menuInfo.position);

        switch (item.getItemId()) {
            case R.id.enviarEmailMI:
                Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(("mailto:")));
                enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contato.getEmail()});
                enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, contato.getNome());
                enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, contato.toString());
                startActivity(enviarEmailIntent);
                return true;
            case R.id.ligarMI:
                verifyCallPhonePermission();
                return true;
            case R.id.acessarSitelMI:
                Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
                abrirNavegadorIntent.setData(Uri.parse(contato.getSite()));
                startActivity(Intent.createChooser(abrirNavegadorIntent, "Ops"));
                return true;
            case R.id.editarContatoMI:
                Intent editarContatoIntent = new Intent(this, ContatoActivity.class);
                editarContatoIntent.putExtra(Intent.EXTRA_USER, contato);
                editarContatoIntent.putExtra(Intent.EXTRA_INDEX, menuInfo.position);
                startActivityForResult(editarContatoIntent, EDITAR_CONTATO_REQUEST_CODE);
                return true;
            case R.id.removerContatoMI:
                contatosList.remove(contato);
                contatosAdapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }

    private void verifyCallPhonePermission() {
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + contato.getTelefone()));
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