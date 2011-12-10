package models.Consulta;

 import models.Resultado;
 import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 5/12/11
 * Time: 11:19 PM
 */
@Entity
public class Consulta extends Model {
    public String consulta;

    @OneToMany(mappedBy = "consulta")
    public List<Resultado> resultados;

    public Map<String, Long> getFrecuencias() {
        ProcesadorConsulta procesadorConsulta = new ProcesadorConsulta();
        procesadorConsulta.setConsulta(consulta);
        return procesadorConsulta.contarFrecuencias();
    }
}
