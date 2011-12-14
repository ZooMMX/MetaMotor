package models.Consulta;

 import models.Frecuencia;
 import models.Fuente;
 import models.Resultado;
 import models.Termino;
 import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
 import javax.persistence.PostPersist;
 import javax.persistence.PrePersist;
 import java.util.AbstractMap;
 import java.util.ArrayList;
 import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 5/12/11
 * Time: 11:19 PM
 */
@Entity
public class Consulta extends Fuente {
    public String consulta;

    @OneToMany(mappedBy = "consulta")
    public List<Resultado> resultados;

    public static Consulta crearConsulta(String consultaTxt) {
        Consulta consultaDelUsuario = new Consulta();
        consultaDelUsuario.consulta = consultaTxt;
        consultaDelUsuario.save();
        consultaDelUsuario.calcularFrecuencias();
        consultaDelUsuario.frecuencias = Frecuencia.find("byFuente", consultaDelUsuario).fetch();
        return consultaDelUsuario;
    }

    public void calcularFrecuencias() {
        AbstractMap<String, Long> frecuencias = contarFrecuencias();

        for (String termino : frecuencias.keySet()) {
            Termino t = Termino.findOrCreate(termino);

            Frecuencia frec = Frecuencia.findOrCreate(this, t);
            frec.frecuencia = frecuencias.get(termino);
            frec.save();
        }
    }

    public AbstractMap<String, Long> contarFrecuencias() {
        ProcesadorConsulta procesadorConsulta = new ProcesadorConsulta();
        procesadorConsulta.setConsulta(consulta);

        return procesadorConsulta.contarFrecuencias();
    }
}
