package models;

import models.Consulta.Consulta;
import org.apache.commons.lang.NotImplementedException;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 5/12/11
 * Time: 11:18 PM
 */
@Entity
public class Motor extends Model {

    String nombre;

    @OneToMany(mappedBy = "motor")
    public List<Frecuencia> frecuencias;

    public static List<Motor> seleccionarMotores(Consulta consultaDelUsuario) {
        return null;
    }

    public BigDecimal selectorDocumentos() {
        return null;
    }

    public void setWeight(BigDecimal weight) {
        throw new NotImplementedException("En construcción");
    }

    public List<Resultado> consultar(Consulta consultaDelUsuario) {
        return null;
    }
}
