package models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 10/12/11
 * Time: 04:07 PM

 */
public class TerminoTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");
    }

    @Test
    public void testFindOrCreate() throws Exception {
        Termino.findOrCreate("prueba");
        Assert.assertTrue(Termino.find("byTermino", "prueba").<Termino>first() != null);
        Assert.assertTrue(Termino.find("byTermino", "nada").<Termino>first()   == null);
        Assert.assertTrue(Termino.find("byTermino", "prueba").<Termino>first().termino.equals("prueba"));

        Termino.findOrCreate("prueba2");
        Assert.assertTrue(Termino.find("byTermino", "prueba2").<Termino>first() != null);

        Termino.findOrCreate("prueba");
        Assert.assertTrue(Termino.find("byTermino", "prueba").<Termino>first() != null);
    }
}
