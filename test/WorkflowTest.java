import models.Consulta.Consulta;
import models.Motor;
import models.Resultado;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.Logger;

import play.test.Fixtures;
import play.test.UnitTest;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 5/12/11
 * Time: 11:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");
    }

    @Test
    public void WorkflowTest() {
        // 1
        Consulta consultaDelUsuario = Consulta.all().first();
        Logger.info("prueba"+consultaDelUsuario.consulta);
        Assert.assertNotNull(consultaDelUsuario.getFrecuencias());
        //2
        List<Motor> motores;
        motores = Motor.seleccionarMotores(consultaDelUsuario);
        //3
        for (Motor motor : motores) {
            motor.setWeight(motor.selectorDocumentos());
        }
        //4
        Hashtable<Motor, List<Resultado>> resultados = new Hashtable<Motor, List<Resultado>>();
        for (Motor motor : motores) {
            //5
            List<Resultado> resultadosMotor = motor.consultar(consultaDelUsuario);
            resultados.put(motor, resultadosMotor);
        }
        //6
        List<Resultado> resultadosMezclados = Resultado.mezclar(resultados);
    }
}
