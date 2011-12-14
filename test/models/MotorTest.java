package models;

import models.Consulta.Consulta;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 8/12/11
 * Time: 05:57 PM
 */
public class MotorTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");
    }

    @Test
    public void testSeleccionarMotores() throws Exception {
        Consulta consulta = Consulta.all().first();
        consulta.calcularFrecuencias();

        List<Motor> motores = Motor.seleccionarMotores(consulta);
        Assert.assertNotNull(motores);
        Assert.assertTrue(motores.size() > 0);
    }

    @Test
    public void testSelectorDocumentos() throws Exception {
        Consulta consulta = Consulta.all().first();
        consulta.calcularFrecuencias();
        List<Motor> motores = Motor.all().fetch();

        motores = Motor.selectorDocumentos(consulta, 10l, motores);
        Assert.assertNotNull(motores);
    }
}
