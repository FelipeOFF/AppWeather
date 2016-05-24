package br.com.at_online.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Criado por Felipe de Oliveira em 23/05/16.
 * <p>
 * <p> Utilitarios relacionados a conectividade
 * <p>
 * Qualquer duvida contate.
 * Tel: (011)95245-7295
 * Email: felipe-not@hotmail.com
 */
public class ConnectivityUtils {

    /**
     * Este método permite monitorar e ve se a uma connecção disponivel
     * Exige a permição android.permission.ACCESS_NETWORK_STATE
     *
     * @param context - É necessario o contexto para verificar a conexão
     * @return boolean representando a conexão ativa.
     */
    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
