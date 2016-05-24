package br.com.at_online.weather.webservice;

/**
 * Criado por Felipe de Oliveira em 23/05/16.
 *
 * Esta classe foi criado com o intuito de se aver
 * uma exceção pode tratala de forma correta
 *
 * Qualquer duvida contate:
 * Tel: (011)95245-7295
 * Email: felipe-not@hotmail.com
 */
public class WebServiceException extends Exception {

    public WebServiceException(){

    }

    public WebServiceException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }

    public WebServiceException(String detailMessage){
        super(detailMessage);
    }

    public WebServiceException(Throwable throwable){
        super(throwable);
    }

}
