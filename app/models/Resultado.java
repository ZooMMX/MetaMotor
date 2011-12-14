package models;

import models.Consulta.Consulta;
import models.motor.Motor;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 5/12/11
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Resultado extends Model {

    public String documento;
    public BigDecimal relevancia;

    public Motor motor;

    @ManyToOne
    public Consulta consulta;

    public static List<Resultado> mezclar(Hashtable<Motor, List<Resultado>> resultados) {
        ArrayList<Resultado> resultadosList = new ArrayList<Resultado>();
        for (List<Resultado> resultadoList : resultados.values()) {
            resultadosList.addAll(resultadoList);
        }

        Collections.sort(resultadosList, new Comparator<Resultado>() {
            public int compare(Resultado resultado, Resultado resultado1) {
                return resultado1.relevancia.compareTo(resultado.relevancia);
            }
        });
        return resultadosList;
    }
}
