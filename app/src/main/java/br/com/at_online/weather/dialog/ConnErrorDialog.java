package br.com.at_online.weather.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;

import br.com.at_online.weather.R;

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
public class ConnErrorDialog extends DialogFragment {

    public static final String TAG = "connErrorDialog";

    public static void show(FragmentManager fm){
        ConnErrorDialog dialog = new ConnErrorDialog();
        dialog.show(fm, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_sem_conexao)
                .setMessage(R.string.dialog_conexao_de_dados_nao_disponivel)
                .setPositiveButton(R.string.dialog_fechar, null)
                .create();
    }
}
