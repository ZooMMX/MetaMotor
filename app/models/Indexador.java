package models;

import models.motor.ClienteXmlRpc;
import models.motor.Motor;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 14/12/11
 * Time: 04:54 AM
 */
public class Indexador {

    public void indexar() throws MalformedURLException, XmlRpcException {
        List<Motor> motores = Motor.all().fetch();
        for (Motor motor : motores) {
            ClienteXmlRpc clienteXmlRpc = new ClienteXmlRpc(motor.urlXmlRpc);
            motor.setNDocs(clienteXmlRpc.getNDocs());
            motor.save();
            HashMap<String, Integer> frecuencias = clienteXmlRpc.getFrecuencias();
            for (String terminoTxt : frecuencias.keySet()) {
                Termino termino = Termino.findOrCreate(terminoTxt);
                Frecuencia frecuencia = Frecuencia.findOrCreate(motor, termino);
                frecuencia.frecuencia = Long.valueOf( frecuencias.get(terminoTxt) );
                frecuencia.save();
            }
        }
    }
}
