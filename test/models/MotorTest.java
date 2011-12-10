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
        Assert.assertNotNull(consulta.getFrecuencias());

        List<Motor> motores = Motor.seleccionarMotores(consulta);
        Assert.assertNotNull(motores);
    }

    public void testSelectorDocumentos() throws Exception {

    }

    public void testSetWeight() throws Exception {

    }

    public void testConsultar() throws Exception {

    }
}