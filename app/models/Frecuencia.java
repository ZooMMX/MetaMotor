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
    public Motor motor;

    @ManyToOne
    public Termino termino;
}
