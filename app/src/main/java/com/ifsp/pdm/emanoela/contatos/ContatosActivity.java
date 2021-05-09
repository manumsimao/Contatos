package com.ifsp.pdm.emanoela.contatos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.ifsp.pdm.emanoela.contatos.databinding.ActivityContatosBinding;

import java.util.ArrayList;

public class ContatosActivity extends AppCompatActivity {
    private ActivityContatosBinding activityContatosBinding;
    private ArrayList<Contato> contatosList;
    private ArrayAdapter<String> contatosAdapter;
    private final int NOVO_CONTATO_REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContatosBinding = ActivityContatosBinding.inflate(getLayoutInflater());
        setContentView(activityContatosBinding.getRoot());

        contatosList = new ArrayList<>();
        popularContatosList();

        contatosAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, contatosList);

        activityContatosBinding.contatosLV.setAdapter(contatosAdapter);
    }

    private void popularContatosList(){
        for (int i=0; i<20; i++){
            contatosList.add(
                    new Contato(
                            "Nome" +i,
                            "email"+i+"@gmail.com",
                            (i%2==0)?"Comercial":"Residencial",
                            "telefone"+i,
                            (i%2==0)? true:false,
                            (i%2==0)? "celular"+i:"",
                            "www.site"+i+".com.br")
            );
        }
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
        }
    }
}