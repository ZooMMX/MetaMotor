package models;

import models.Consulta.Consulta;
import org.junit.Assert;
import org.junit.Test;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.JPASupport;
import play.test.UnitTest;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 8/12/11
 * Time: 08:36 PM
 */
public class ConsultaTest extends UnitTest {

    @Test
    public void testFrecuencias() throws Exception {
        Consulta consulta = new Consulta();
        consulta.consulta = "Esto es una prueba de esta consulta de prueba pura prueba Tomarla Tomarlas " +
                "Tomarlos Tomarlo Tomarle Tomarles Tomado Tomó";

        HashMap<String, Long> mapaEsperado = new HashMap<String, Long>();
        mapaEsperado.put("consult", 1l);
        mapaEsperado.put("prueb"  , 3l);
        mapaEsperado.put("pur"    , 1l);
        mapaEsperado.put("tom"    , 8l);

        Map<String, Long> mapaEntregado = consulta.contarFrecuencias();

        Assert.assertTrue( mapaEsperado.equals( mapaEntregado ) );

        Consulta consultaPersistida = consulta.save();
        consulta.calcularFrecuencias();
        consulta.calcularFrecuencias();

        Termino t = Termino.find("byTermino", "tom").first();
        GenericModel.JPAQuery query = Frecuencia.find("fuente = ? AND termino = ?", consulta, t);
        Assert.assertTrue(query.fetch().size()==1);
        Frecuencia frec = Frecuencia.find("fuente = ? AND termino = ?", consulta, t).first();
        Assert.assertTrue(frec.frecuencia == 8l);
    }
}
