package br.com.at_online.weather.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Criado por Felipe de Oliveira em 23/05/16.
 * <p>
 * <p> Esta classe representa uma ciadade
 * <p>
 * Qualquer duvida contate.
 * Tel: (011)95245-7295
 * Email: felipe-not@hotmail.com
 */
public class Cidade implements Comparable<Cidade>, Serializable {

    private int id;
    private String nome;
    private String pais;
    private Temperatura temperatura;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Temperatura getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Temperatura temperatura) {
        this.temperatura = temperatura;
    }

    /**
     * Este método permite converter um JSONObject
     * em uma classe Cidade com apenas os parametros padrões.
     * (Sem a classe Temperatura)
     *
     * @param jsonObj - É necessario um JSONObject que contenhão os valores id, nome e pais
     * @return Ciadade - Retorna a classe cidade
     */
    public static Cidade createFromJson(JSONObject jsonObj) {
        Cidade cidade = new Cidade();
        cidade.id = jsonObj.optInt("id", -1);
        cidade.nome = jsonObj.optString("nome", null);
        cidade.pais = jsonObj.optString("pais", null);
        return cidade;
    }

    /**
     * Compara o nome de duas cidades
     *
     * @param another - A ciadade a ser comparada
     * @return int - Representa a comparação
     */
    @Override
    public int compareTo(Cidade another) {
        return this.nome.compareTo(another.nome);
    }

}
