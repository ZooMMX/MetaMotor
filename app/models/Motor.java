package models;

import models.Consulta.Consulta;
import org.apache.commons.lang.NotImplementedException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import play.Logger;
import play.utils.Utils;

import javax.persistence.Entity;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.ConnectException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 5/12/11
 * Time: 11:18 PM
 */
@Entity
public class Motor extends Fuente {

    public String nombre;
    public BigDecimal rm;
    public BigDecimal weight;
    public Long nDocs;

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

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public List<Resultado> consultar(Consulta consultaDelUsuario) throws Exception {
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
           throw exception;
        }
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

    public static List<Motor> selectorDocumentos(Consulta consulta, Long resultadosEsperados, List<Motor> motores) {
        motores = calcularRm (consulta, resultadosEsperados, motores);
        BigDecimal rmTotal = new BigDecimal(0d);
        for (Motor motor : motores) {
            rmTotal = rmTotal.add(motor.rm);
        }

        for (Motor motor : motores) {
            BigDecimal extraerNDocs = new BigDecimal(0d);
            BigDecimal k = new BigDecimal(resultadosEsperados.doubleValue());
            extraerNDocs = extraerNDocs.add(k.multiply(motor.rm));
            extraerNDocs = extraerNDocs.divide(rmTotal);
            motor.setWeight(extraerNDocs);
        }

        return motores;
    }

    private static List<Motor> calcularRm(Consulta consulta, Long resultadosEsperados, List<Motor> motores) {
        List<Termino> terminos = new ArrayList<Termino>();
        for (Frecuencia frecuencia : consulta.frecuencias) {
            terminos.add(frecuencia.termino);
        }
        BigDecimal cv2Sum = new BigDecimal(0d);
        BigDecimal cvv    = new BigDecimal(0d);
        HashMap<String, HashMap<Motor, Long>> completa = new HashMap<String, HashMap<Motor, Long>>();
        for (Motor motor : motores) {

            for (Termino termino : terminos) {
                Long totalFrecuencias = (Long) Frecuencia.find("SELECT SUM(f.frecuencia) FROM Frecuencia as f WHERE termino = ? AND fuente.class = Motor", termino).query.getSingleResult();
                if(totalFrecuencias == null) { continue; }
                if(completa.get(termino.termino) == null) { completa.put(termino.termino, new HashMap<Motor, Long>()); }
                Frecuencia frecDFmj = Frecuencia.findOrCreate(motor, termino); // Inicialización perezosa
                completa.get(termino.termino).put(motor, frecDFmj.frecuencia);
            }
        }
        for (Motor motor : motores) {
            for (String termino : completa.keySet()) {
                Long frec = completa.get(termino).get(motor);
                BigDecimal a = new BigDecimal( frec.doubleValue() / motor.nDocs.doubleValue() );
                BigDecimal b = new BigDecimal(0d);
                BigDecimal c = new BigDecimal(0d);
                for (Map.Entry<Motor, Long> motorFrec : completa.get(termino).entrySet()) {
                    if(motorFrec.getKey() == motor) { continue; }
                    b = b.add( new BigDecimal( motorFrec.getValue().doubleValue() ));
                    c = c.add( new BigDecimal( motor.nDocs.doubleValue() ) );
                }
                BigDecimal d   = b.divide(c);
                BigDecimal cv  = a.divide(a.add(d), BigDecimal.ROUND_HALF_UP);
                BigDecimal cv2 = (cv.subtract(promedio(completa.get(termino)))).pow(2);
                cv2Sum = cv2Sum.add(cv2);
            }
        }
        cvv = cvv.add(cv2Sum);
        cvv = cvv.divide( new BigDecimal ( motores.size() ) );

        for (Motor motor : motores) {
            BigDecimal rm = new BigDecimal(0d);
            for (String termino : completa.keySet()) {
                rm = rm.add( new BigDecimal( completa.get(termino).get(motor).doubleValue() ).multiply( cvv ) );
            }
            motor.setRm(rm);
        }

        return motores;
    }

    private static BigDecimal promedio(HashMap<Motor, Long> motorLongHashMap) {
        BigDecimal resultado  = new BigDecimal(0d);
        BigDecimal numMotores = new BigDecimal( motorLongHashMap.size() );
        for (Long aLong : motorLongHashMap.values()) {
            BigDecimal frecuencia = new BigDecimal( aLong.doubleValue() );
            resultado = resultado.add( frecuencia );
        }
        return resultado.divide( numMotores );
    }

    private void setRm(BigDecimal rm) {
        this.rm = rm;
    }

}
