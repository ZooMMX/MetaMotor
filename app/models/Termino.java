package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 5/12/11
 * Time: 11:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Termino extends Model {

    String termino;

    //@OneToMany(mappedBy = "frecuencia")
    //List<Frecuencia> frecuencias;
}
