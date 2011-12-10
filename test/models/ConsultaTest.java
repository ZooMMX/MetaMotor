package models;

import models.Consulta.Consulta;
import org.junit.Assert;
import org.junit.Test;
import play.test.UnitTest;

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
    public void testGetFrecuencias() throws Exception {
        Consulta consulta = new Consulta();
        consulta.consulta = "Esto es una prueba de esta consulta de prueba pura prueba Tomarla Tomarlas " +
                "Tomarlos Tomarlo Tomarle Tomarles Tomado Tomó";

        HashMap<String, Long> mapaEsperado = new HashMap<String, Long>();
        mapaEsperado.put("consult", 1l);
        mapaEsperado.put("prueb"  , 3l);
        mapaEsperado.put("pur"    , 1l);
        mapaEsperado.put("tom"    , 8l);

        Map<String, Long> mapaEntregado = consulta.getFrecuencias();

        Assert.assertTrue( mapaEsperado.equals( mapaEntregado ) );
    }
}
