package br.com.at_online.weather.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Criado por Felipe de Oliveira em 23/05/16.
 * <p>
 * <p> Este método representa a temperatura das ciadades
 * <p>
 * Qualquer duvida contate.
 * Tel: (011)95245-7295
 * Email: felipe-not@hotmail.com
 */
public class Temperatura implements Serializable {

    private int min;
    private int max;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Cria um objeto temperatura apartir de um JSONObject
     *
     * @param jsonObj - O objeto Json que contenha os valores min, e max
     * @return Temperatura - O objeto que representa a temperatura
     */
    public static Temperatura createFromJSON(JSONObject jsonObj){
        Temperatura temperatura = new Temperatura();
        temperatura.max = jsonObj.optInt("max", -1);
        temperatura.max = jsonObj.optInt("min", -1);
        return temperatura;
    }
}
