package br.com.at_online.weather.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.at_online.weather.R;
import br.com.at_online.weather.model.Cidade;

/**
 * Criado por Felipe de Oliveira em 23/05/16.
 * <p>
 * <p>
 * <p>
 * <p>
 * Qualquer duvida contate.
 * Tel: (011)95245-7295
 * Email: felipe-not@hotmail.com
 */
public class CidadesAdapter extends BaseAdapter {

    // Lista que armazenara as cidades armazenadas
    private List<Cidade> cidades = new ArrayList<>();

    /**
     * Este método retorna a quantidade de elementos que compoem a lista
     *
     * @return o numero de elementos
     */
    @Override
    public int getCount() {
        return cidades.size();
    }

    /**
     * retorna o item que esta na posição indicada
     * @param position Posição do elemento no adapter
     * @return retorna a ciade do elemento
     */
    @Override
    public Cidade getItem(int position) {
        return cidades.get(position);
    }

    /**
     * Retorna o id de uma cidade com base no item
     * @param position posição da cidade
     * @return o id corespondente a ciade
     */
    @Override
    public long getItemId(int position) {
        return cidades.get(position).getId();
    }

    /**
     * Retorna a view para ser colocada no adapter.
     * Este método é o responsavel por fazer cada elelento da lista.
     * @param position posição do elemento indicado
     * @param convertView A view de retorno de posição
     * @param parent o brupo que compoem a view
     * @return view da lista
     */
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.adapter_cidades, parent, false);
            holder = new ViewHolder();
            holder.txtCidade = (TextView) view.findViewById(R.id.txt_cidade);
            holder.txtPais = (TextView) view.findViewById(R.id.txt_pais);
            holder.txtMax = (TextView) view.findViewById(R.id.txt_max);
            holder.txtMin = (TextView) view.findViewById(R.id.txt_min);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Cidade cidade = cidades.get(position);
        holder.txtCidade.setText(cidade.getNome());
        holder.txtPais.setText(cidade.getPais());
        holder.txtMax.setText("Max: " + cidade.getTemperatura().getMax());
        holder.txtMin.setText("Min: " + cidade.getTemperatura().getMin());

        return view;
    }

    /**
     * Este método permite Adicionar novas ciadades a lista
     * @param cidades Lista de cidades a ser adicionadas;
     */
    public void setCidades(List<Cidade> cidades) {
        this.cidades.clear();
        this.cidades.addAll(cidades);
        Collections.sort(this.cidades);
        notifyDataSetChanged();
    }

    /**
     * Este mpetodo retorna a lista que contem todas as
     * ciadades
     * @return ciadades
     */
    public List<Cidade> getCidades() {
        return cidades;
    }

    /**
     * Este método limpa a lista.s
     */
    public void clear(){
        this.cidades.clear();
        notifyDataSetChanged();
    }

    /**
     * Sera usado um ViewHolder para melhorar o desenpenho da lista
     * e deixar ela ser colocado de um modo mais suave
     */
    private class ViewHolder{
        private TextView txtCidade;
        private TextView txtPais;
        private TextView txtMin;
        private TextView txtMax;
    }
}
