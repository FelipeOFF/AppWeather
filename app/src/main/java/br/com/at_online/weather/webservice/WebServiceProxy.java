package br.com.at_online.weather.webservice;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.com.at_online.weather.model.Cidade;
import br.com.at_online.weather.model.Temperatura;
import br.com.at_online.weather.utils.Constants;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Criado por Felipe de Oliveira em 23/05/16.
 * <p>
 * <p> Esta classe tem um papel fundamental na aplicação
 * <p> Ela é que retorna valores vindos do webservice
 * <p>
 * Qualquer duvida contate.
 * Tel: (011)95245-7295
 * Email: felipe-not@hotmail.com
 */
public class WebServiceProxy {

    // Este valor representa a URL que contem o web service
    private static final HttpUrl WEB_SERVICE_CONTENT = HttpUrl.parse("http://code.softblue.com.br:8080/web/rest/weather");

    // Este é o objeto que permitira que eu fassa uma coneção com o WebService
    private OkHttpClient client = new OkHttpClient();

    /**
     * Este método retorna um lista de cidades vinda do webservice
     *
     * @return Uma lista de ciades
     * @throws IOException         - Ocorre quando feito uma conexão
     * @throws WebServiceException - Ocorre quando obtiver error de JSON
     */
    public List<Cidade> listCidade() throws IOException, WebServiceException {
        try {
            Request request = new Request.Builder()
                    .url(WEB_SERVICE_CONTENT)
                    .build();

            Response response = client.newCall(request).execute();

            String body = getResposeAsString(response);

            JSONArray array = new JSONArray(body);

            List<Cidade> cidades = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                cidades.add(Cidade.createFromJson(object));
            }

            return cidades;
        } catch (JSONException e) {
            throw new WebServiceException("Error ao processar JSON", e);
        }
    }

    /**
     * Este método converte o id de uma cidade em uma Temperatura
     * @param cidadeId - O Id da cidade
     * @return A Temperatura que coresponde a cidade
     * @throws IOException
     * @throws WebServiceException
     */
    public Temperatura obterTemperatura(int cidadeId) throws IOException, WebServiceException{
        try {
            HttpUrl url = WEB_SERVICE_CONTENT.newBuilder()
                    .addPathSegment(String.valueOf(cidadeId))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            String body = getResposeAsString(response);

            JSONObject object = new JSONObject(body);
            return Temperatura.createFromJSON(object);
        } catch (JSONException e) {
            throw new WebServiceException("Erro ao processar JSON", e);
        }
    }

    /**
     * Este método permite verificar se ouve algum error na conexão
     *
     * @param response - A resposta do servidor
     * @return Um Json que representa a resposta
     * @throws WebServiceException Só ocorerar se ouver um error
     * @throws IOException Só ocorerar se ouver um error
     */
    private String getResposeAsString(Response response) throws WebServiceException, IOException{
        String body = response.body().string();

        int code = response.code();
        if(code != HttpsURLConnection.HTTP_OK){
            // Se o codigo não for um sucesso (200). Então sera lansado uma exceção
            throw new WebServiceException("O HTTP Retornou um Codigo de error: " + code);
        }

        Log.i(Constants.LOG_TAG, "JSON: " + body);
        return body;
    }

}
