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

public class ClienteXMLRPC implements Serializable {
    public ClienteXMLRPC() {
    }

    List<Resultado> consultar(Consulta consultaDelUsuario) throws MalformedURLException, XmlRpcException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        XmlRpcClient client = new XmlRpcClient();
        config.setServerURL(new URL("http://localhost:9001/xmlrpc"));
        client.setConfig(config);

        Object[] params = new Object[]{consultaDelUsuario.consulta};
        List rawList = Arrays.asList((Object[]) client.execute("Motor.consultar", params));
        return rawList2Resultados(rawList, consultaDelUsuario);
    }

    List<Resultado> rawList2Resultados(List rawList, Consulta consulta) {
        List<Resultado> resultados = new ArrayList<Resultado>();
        for (Object o : rawList) {
            HashMap<String, Object> map = (HashMap<String, Object>) o;
            Resultado r = new Resultado();
            r.consulta = consulta;
            r.motor = null;
            r.documento = (String) map.get("documento");
            r.relevancia = new BigDecimal((Double) map.get("relevancia"));
            resultados.add(r);
        }
        return resultados;
    }
}