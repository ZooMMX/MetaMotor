package models;

import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 5/12/11
 * Time: 11:18 PM
 */
@Entity
public class Termino extends Model {

    @Unique
    public String termino;

    public static Termino findOrCreate(String termino) {
        Termino t = Termino.find("byTermino", termino).first();
        if(t == null) {
            t = new Termino();
            t.termino = termino;
            t.save();
        }
        return t;
    }
    //@OneToMany(mappedBy = "frecuencia")
    //List<Frecuencia> frecuencias;
}
