package models;

import models.Consulta.Consulta;
import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 5/12/11
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Resultado extends Model {

    String documento;

    Motor motor;

    Consulta consulta;

    public static List<Resultado> mezclar(Hashtable<Motor, List<Resultado>> resultados) {
        throw new UnsupportedOperationException();

    }
}
