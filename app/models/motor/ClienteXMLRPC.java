package models.motor;

import models.Consulta.Consulta;
import models.Resultado;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ClienteXmlRpc implements Serializable {
    public String       urlXmlRpc;
    public XmlRpcClient client;
    private Motor motor;

    public ClienteXmlRpc(Motor motor) throws MalformedURLException {
        this(motor.urlXmlRpc);
        this.motor = motor;
    }

    public ClienteXmlRpc(String urlXmlRpc) throws MalformedURLException {
        this.urlXmlRpc = urlXmlRpc;

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        client = new XmlRpcClient();
        config.setServerURL(new URL(urlXmlRpc));
        client.setConfig(config);
    }

    List<Resultado> consultar(Consulta consultaDelUsuario) throws MalformedURLException, XmlRpcException {

        Object[] params = new Object[]{consultaDelUsuario.consulta, motor.weight.doubleValue()};
        List rawList = Arrays.asList((Object[]) client.execute("Motor.consultar", params));
        motor = motor;
        return rawList2Resultados(rawList, consultaDelUsuario, motor);
    }

    List<Resultado> rawList2Resultados(List rawList, Consulta consulta, Motor motor) {
        List<Resultado> resultados = new ArrayList<Resultado>();
        for (Object o : rawList) {
            HashMap<String, Object> map = (HashMap<String, Object>) o;
            Resultado r = new Resultado();
            r.consulta = consulta;
            r.motor = motor;
            r.documento = (String) map.get("documento");
            r.relevancia = new BigDecimal((Double) map.get("relevancia"));
            resultados.add(r);
        }
        return resultados;
    }

    public String getUrlXmlRpc() {
        return urlXmlRpc;
    }

    public Long getNDocs() throws XmlRpcException {
        Object[] params = new Object[]{};
        return Long.valueOf( (Integer) client.execute("Motor.getNDocs", params) );
    }

    public HashMap<String,Integer> getFrecuencias() throws XmlRpcException {
        return (HashMap<String, Integer>) client.execute("Motor.getFrecuencias", new ArrayList());
    }
}