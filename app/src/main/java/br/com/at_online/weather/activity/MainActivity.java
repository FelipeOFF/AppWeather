package br.com.at_online.weather.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import br.com.at_online.weather.R;
import br.com.at_online.weather.adapter.CidadesAdapter;
import br.com.at_online.weather.dialog.ConnErrorDialog;
import br.com.at_online.weather.fragment.TaskFragment;
import br.com.at_online.weather.model.Cidade;
import br.com.at_online.weather.model.Temperatura;
import br.com.at_online.weather.utils.ConnectivityUtils;
import br.com.at_online.weather.utils.Constants;
import br.com.at_online.weather.webservice.WebServiceProxy;

public class MainActivity extends AppCompatActivity implements TaskFragment.TaskListener<List<Cidade>> {

    // Adapter para exibção das cidades na lista
    private CidadesAdapter adapter;

    // Task para exibição das cidades
    private ListCidadesTask task;

    // Flag que indica se a lista de cidades deve ser atualizada
    private boolean refresh;

    // Flag que indica se a lista de cidades esta em progresso de atualização
    private boolean inProgress;

    private ListView listView;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        // Definir o adapter para lista
        adapter = new CidadesAdapter();
        listView.setAdapter(adapter);

        // Criar a task
        task = TaskFragment.getInstance(this, ListCidadesTask.class);

        if (savedInstanceState == null) {
            // Se a activity esta sendo iniciada pela primeira vez marque a flag de refresh como true
            refresh = true;
        } else {
            // Se a activity esta sendo iniciada após uma mudançã ada apresentação
            // obtem a lista de ciades que foram salvada previamente e carrega o adapter
            CidadeHolder holder = (CidadeHolder) savedInstanceState.getSerializable("cidades");
            if (holder != null) {
                adapter.setCidades(holder.getCidades());
            }

            // Se a lista esta em progresso de atualização quando a configuração mudou, exibe o
            // indicador de progresso
            inProgress = savedInstanceState.getBoolean("inProgress");
            if (inProgress) {
                setInProgress(true);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Se o Refresh deve ser feito
        if (refresh) {
            refresh();
            refresh = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                // Faz o refresh da lista
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        // Colocar a lista de ciades no bundle para recuperação posterior. Encapsular a lista dentro
        // de um CidadeHolder para que seja posivel a incerção do Bundle
        outState.putSerializable("cidades", new CidadeHolder(adapter.getCidades()));

        outState.putBoolean("inProgress", inProgress);
    }

    private void refresh() {
        if (!ConnectivityUtils.isConnected(this)) {
            // Se não existir uma coneção mostra o dialog de error e termina
            ConnErrorDialog.show(getFragmentManager());
        } else {
            task.execute(0);
        }
    }

    /**
     * Este método serve para fazer a logica de inverção de visualização na activity
     *
     * @param inProgress booleano que representa a visualização de progresso
     */
    private void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;

        if (inProgress) {
            listView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Este método representa o processo antes da execução
     *
     * @param taskId Id da task
     */
    @Override
    public void beforeTaskExecute(int taskId) {
        setInProgress(true);
        adapter.clear();
    }

    /**
     * Este método representa o processo depois da execução
     *
     * @param taskId  Id da Task
     * @param cidades Cidades retornadas depois da execução
     */
    @Override
    public void afterTaskExecute(int taskId, List<Cidade> cidades) {
        adapter.setCidades(cidades);
        setInProgress(false);
    }

    /**
     * Esta classe serve para manipular uma execução em segundo plano
     */
    public static class ListCidadesTask extends TaskFragment<Void, List<Cidade>> {

        /**
         * Método executa o processo em background. De fato o coração do aplicativo esta
         * Aqui.
         *
         * @param taskId Id da Task
         * @return Lista de cidades
         */
        @Override
        protected List<Cidade> executeInBackground(int taskId) {
            try {
                // Cria o proxi de acesso ao WebService
                WebServiceProxy proxy = new WebServiceProxy();

                // Obtem a lista de clientes
                List<Cidade> cidades = proxy.listCidade();

                // Para cada cidade obtem a temperatura com base no ID da Cidade
                for (Cidade cidade : cidades) {
                    Temperatura temperatura = proxy.obterTemperatura(cidade.getId());
                    cidade.setTemperatura(temperatura);
                }

                // Se este aplicati estiver em produção esta linha abaixo devera ser excluida
                SystemClock.sleep(3000);

                return cidades;
            } catch (Exception e) {
                Log.e(Constants.LOG_TAG, "Error ao executar task ", e);
                return Collections.emptyList();
            }
        }
    }

    /**
     * Esta classe serve para esta armazenando a lista emquanto ela esta em atualização
     * Ela é necessaria para salvar as cidades em um bundle
     */
    public static class CidadeHolder implements Serializable {
        private List<Cidade> cidades;

        public CidadeHolder(List<Cidade> cidades) {
            this.cidades = cidades;
        }

        public List<Cidade> getCidades() {
            return cidades;
        }
    }
}
