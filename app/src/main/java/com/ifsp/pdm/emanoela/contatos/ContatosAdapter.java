package com.ifsp.pdm.emanoela.contatos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ifsp.pdm.emanoela.contatos.databinding.ViewContatoBinding;

import java.util.ArrayList;

public class ContatosAdapter extends ArrayAdapter<Contato> {
    public ContatosAdapter(Context context, int layout, ArrayList<Contato> contatosList){
        super(context, layout, contatosList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewContatoBinding viewContatoBinding;
        ContatoViewHolder contatoViewHolder;

        if(convertView == null){
            viewContatoBinding = ViewContatoBinding.inflate(LayoutInflater.from(getContext()));

            convertView = viewContatoBinding.getRoot();

            contatoViewHolder = new ContatoViewHolder();
            contatoViewHolder.nomeContatoTV = viewContatoBinding.nomeContatoTv;
            contatoViewHolder.emailContatoTV =viewContatoBinding.emailContatoTV;

            convertView.setTag(contatoViewHolder);
        }
        contatoViewHolder = (ContatoViewHolder) convertView.getTag();

        Contato contato = getItem(position);
        contatoViewHolder.nomeContatoTV.setText(contato.getNome());
        contatoViewHolder.emailContatoTV.setText(contato.getEmail());

        return convertView;
    }

    private class ContatoViewHolder{
        public TextView nomeContatoTV;
        public TextView emailContatoTV;
    }
}
