package br.com.at_online.weather.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado por Felipe de Oliveira em 23/05/16.
 * <p>
 * Para solucionar o problema de destruição da view quando o dispositivo
 * muda de orientação é feito uma tratativa bem simples.
 * <p>
 * Foi feito um fragment que é justamente este aqui para
 * carregar o processo e este processo de segundo plano ira permanecer
 * no fragment que estara desatachado da activity. Portanto quando
 * for destruido a activity o fragment permanecerar intacto.
 * <p>
 * Qualquer duvida contate.
 * Tel: (011)95245-7295
 * Email: felipe-not@hotmail.com
 */
public abstract class TaskFragment<Params, Result> extends Fragment {

    private static final String TAG = "TaskFragment";

    // Variavel que indiga se a activity esta pronta para ser chamada.
    private boolean activityReady;

    // Lista de execuções pendentes para serem enviadas para a activity
    private List<Runnable> pendingCallbacks = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Este parametro define que o fragment não sera destruido junto com a activity.
        // Isso permite que o fragment continue executando a task em background.
        setRetainInstance(true);
    }

    /**
     * O android chama este método quando o onCreate da activity para de executar
     * a activity pode ser executada apartir de um fragment
     *
     * @param savedInstanceState Instancia vinda da activity
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (pendingCallbacks.size() > 0) {
            // Se existe execuções pendentes enviar para a activity
            for (Runnable callback : pendingCallbacks) {
                getActivity().runOnUiThread(callback);
            }
            // Limpar a lista de execuxões pendentes
            pendingCallbacks.clear();
        }

        // Definir a flag da activity pronta como true
        activityReady = true;
    }


    /**
     * O android chama este método quando fragment é desatachado
     * da activity
     */
    @Override
    public void onDetach() {
        super.onDetach();

        // define a flag da activity pronta como false
        activityReady = false;
    }

    /**
     * Este método retorna para a Activity um Listener
     *
     * @return este método retorna um Listener que deve ser implementado pela Activity
     */
    private TaskListener<Result> getTaskActivity() {
        Activity activity = getActivity();

        if (activity instanceof TaskListener) {
            return (TaskListener<Result>) activity;
        } else {
            return null;
        }
    }

    /**
     * Executa a Task.
     *
     * @param taskId Id da Task
     * @param params Varargs de parametros a ser adicionado
     */
    public void execute(int taskId, Params... params) {
        BackgroundTask task = new BackgroundTask(taskId);
        task.execute(params);
    }

    /**
     * Executa a asyncTask só com o ID
     *
     * @param taskId id da Task
     */
    public void execute(int taskId) {
        execute(taskId, (Params[]) null);
    }

    /**
     * Método de execução da Task, que sera executada em Background.
     * Deve Ser implementado pelas subclasses
     *
     * @param taskId Id da Task
     * @return Resultado da Execução
     */
    protected abstract Result executeInBackground(int taskId);

    /**
     * Classe responsavel pela execução das tasks que serão executadas em background.
     */
    private class BackgroundTask extends AsyncTask<Params, Void, Result> {

        // ID da Task
        private int taskId;

        public BackgroundTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        protected void onPreExecute() {
            TaskListener<Result> activity = getTaskActivity();
            if(activity != null){
                activity.beforeTaskExecute(taskId);
            }
        }

        @SafeVarargs
        @Override
        protected final Result doInBackground(Params... params) {
            return executeInBackground(taskId);
        }

        @Override
        protected void onPostExecute(final Result result) {
            final TaskListener<Result> activity = getTaskActivity();

            if(activity != null){
                if(activityReady){
                    activity.afterTaskExecute(taskId, result);
                }else{
                    pendingCallbacks.add(() -> activity.afterTaskExecute(taskId, result));
                }
            }
        }
    }

    /**
     * Listener que chama a execução e a pós-execução.
     * @param <Result>
     */
    public interface TaskListener<Result>{
        void beforeTaskExecute(int taskId);
        void afterTaskExecute(int taskId, Result result);
    }

    /**
     * Este método retorna uma instancia da classe TaskFragment
     *
     * @param activity Activity que esta instanciando o fragment
     * @param taskFragmentClass A task propiamente dita
     * @param <Params> Parametros inseridos na task
     * @param <Result> Resultados esperados da task
     * @param <F> objeto generico a ser devolvido
     * @return uma instancia de TaskFragment
     */
    public static <Params, Result, F extends TaskFragment<Params, Result>> F getInstance(Activity activity, Class<? extends TaskFragment<Params, Result>> taskFragmentClass){

        // Verifica se o fragment ja esta atrelado a activity
        TaskFragment<Params, Result> taskFragment = (TaskFragment<Params, Result>) activity.getFragmentManager().findFragmentByTag(TAG);

        if(taskFragment == null){
            try {
                taskFragment = taskFragmentClass.newInstance();
                activity.getFragmentManager().beginTransaction().add(taskFragment, TAG).commit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return (F) taskFragment;
    }

}
