package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.AbstractMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 10/12/11
 * Time: 02:34 PM
 */
@Entity
public class Fuente extends Model {
    @OneToMany(mappedBy = "fuente")
    public List<Frecuencia> frecuencias;

    public AbstractMap<String, Long> getFrecuenciasIn(List<String> terminos) {
        //Query q = JPA.em().createNamedQuery("getFrecuenciasIn");
        //q.setParameter("motor"       , this);
        //q.setParameter("terminosList", terminos);
        return null;
    }
}
