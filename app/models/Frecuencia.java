package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 5/12/11
 * Time: 11:18 PM
 */
@Entity
public class Frecuencia extends Model {
    public Long frecuencia;

    @ManyToOne
    public Fuente fuente;

    @ManyToOne
    public Termino termino;

    public static Frecuencia findOrCreate(Fuente fuente, Termino termino) {
        Frecuencia frec = Frecuencia.find("fuente = ? AND termino = ?", fuente, termino).first();
        if(frec == null) {
            frec = new Frecuencia();
            frec.frecuencia = 0l;
            frec.fuente     = fuente;
            frec.termino    = termino;
            frec.save();
        }
        return frec;
    }
}
