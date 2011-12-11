package models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 10/12/11
 * Time: 05:53 PM
 */
public class FrecuenciaTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");
    }

    @Test
    public void testFindOrCreate() throws Exception {
        Fuente fuente   = Fuente.all().first();
        Termino termino = Termino.findOrCreate("PruebaDeFrecuencia");

        Frecuencia frec = Frecuencia.findOrCreate(fuente, termino);
        Assert.assertTrue(frec != null);
        Assert.assertTrue(frec.frecuencia == 0l);
        frec.frecuencia = 10l;
        frec.save();

        Frecuencia frec2 = Frecuencia.findOrCreate(fuente, termino);
        Assert.assertTrue(frec2 != null);
        Assert.assertTrue(frec2.frecuencia == 10l);

        List frecuencias = Frecuencia.find("byFuenteAndTermino", fuente, termino).fetch();
        Assert.assertTrue(frecuencias.size() == 1);
    }
}
