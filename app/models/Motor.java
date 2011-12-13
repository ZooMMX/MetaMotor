package models;

import models.Consulta.Consulta;
import org.apache.commons.lang.NotImplementedException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Before;
import play.Logger;
import play.db.jpa.JPA;
import play.test.Fixtures;

import javax.persistence.Entity;
import javax.persistence.Query;
import javax.swing.event.ListSelectionEvent;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.lang.Math.*;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 5/12/11
 * Time: 11:18 PM
 */
@Entity
public class Motor extends Fuente {

    public String nombre;
    public Long documentos;
    BigDecimal rm;

    public static List<Motor> seleccionarMotores(Consulta consultaDelUsuario) {
        try {
            String consultaJPQL =
                    "SELECT new list( sqrt(sum((q.frecuencia - m.frecuencia) * (q.frecuencia - m.frecuencia))) as valor," +
                    " m.fuente)" +
                    " FROM Frecuencia q, Frecuencia m" +
                    " WHERE q.fuente.class = Consulta" +
                    " AND m.fuente.class = Motor" +
                    " AND m.termino = q.termino" +
                    " GROUP BY m.fuente.id" +
                    " ORDER BY valor";

            ArrayList<Motor> motores = new ArrayList<Motor>();

            Query q = em().createQuery(consultaJPQL);
            List resultados = q.getResultList();
            for (Object object : resultados) {
                List       resultList = (List) object;
                BigDecimal similitud  = new BigDecimal ((Double) resultList.get(0));
                Motor      motor      = (Motor) resultList.get(1);
                Logger.info("Similitud: "+similitud+", en motor: "+motor.nombre);
                motores.add(motor);
            }
            return motores;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BigDecimal selectorDocumentos() {
        return null;
    }

    public void setWeight(BigDecimal weight) {
        throw new NotImplementedException("En construcción");
    }

    public List<Resultado> consultar(Consulta consultaDelUsuario) {
        try {
           XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
           XmlRpcClient           client = new XmlRpcClient();
           config.setServerURL(new URL("http://localhost/xmlrpc"));
           client.setConfig(config);

           Object[]        params     = new Object[]{ consultaDelUsuario.consulta };
           List            rawList    = Arrays.asList( (Object[]) client.execute("Motor.consultar", params) );
           List<Resultado> resultados = rawList2Resultados(rawList, consultaDelUsuario);

           Logger.info("+Respuesta del servidor XMLRPC: " + resultados);
           return resultados;

        } catch (Exception exception) {
           Logger.error(exception, "JavaClient: " + exception);
        }
        return null;
    }

    private List<Resultado> rawList2Resultados(List rawList, Consulta consulta) {
        List<Resultado> resultados = new ArrayList<Resultado>();
        for (Object o : rawList) {
            Resultado r = new Resultado();
            r.consulta  = consulta;
            r.motor     = this;
            r.documento = String.valueOf(o);
            resultados.add(r);
        }
        return resultados;
    }
}
