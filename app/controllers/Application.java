package controllers;

import org.apache.xmlrpc.XmlRpcException;
import play.*;
import play.mvc.*;

import java.net.MalformedURLException;
import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index(String consulta, Integer nDocs) {

        validation.required(consulta);

        List<Resultado> resultados = new ArrayList<Resultado>();
        if(consulta != null && !consulta.equals("")) {
            MetaMotor metaMotor = new MetaMotor();
            resultados = metaMotor.buscar(consulta, nDocs);
        }
        render(resultados);
    }

    public static void indexarMotores() throws MalformedURLException, XmlRpcException {
        Indexador indexador = new Indexador();
        indexador.indexar();
        renderText("Motores indexados!");
    }

}