package models.motor;

import models.Consulta.Consulta;
import models.Fuente;
import models.Resultado;
import play.Logger;

import javax.persistence.Entity;
import javax.persistence.Query;
import java.math.BigDecimal;
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
    public String urlXmlRpc;

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
        ClienteXmlRpc clienteXmlRpc = new ClienteXmlRpc(this);
        try {
           List<Resultado> resultados = clienteXmlRpc.consultar(consultaDelUsuario);

           Logger.info("+Respuesta del servidor XMLRPC: " + resultados);
           return resultados;

        } catch (Exception exception) {
           Logger.error(exception, "JavaClient: " + exception);
           throw exception;
        }
    }

    public static List<Motor> selectorDocumentos(Consulta consulta, Long resultadosEsperados, List<Motor> motores) {
        motores = MathOps.calcularRm(consulta, resultadosEsperados, motores);
        BigDecimal rmTotal = new BigDecimal(0d);
        for (Motor motor : motores) {
            rmTotal = rmTotal.add(motor.rm);
        }

        for (Motor motor : motores) {
            BigDecimal extraerNDocs = new BigDecimal(0d);
            BigDecimal k = new BigDecimal(resultadosEsperados.doubleValue());
            extraerNDocs = extraerNDocs.add(k.multiply(motor.rm));
            extraerNDocs = extraerNDocs.divide(rmTotal, BigDecimal.ROUND_HALF_UP);
            motor.setWeight(extraerNDocs);
        }

        return motores;
    }

    public void setRm(BigDecimal rm) {
        this.rm = rm;
    }

    public void setNDocs(Long nDocs) {
        this.nDocs = nDocs;
    }
}
