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
        Assert.assertNotNull(consultaDelUsuario.contarFrecuencias());
        consultaDelUsuario.calcularFrecuencias();
        //2
        List<Motor> motores;
        motores = Motor.seleccionarMotores(consultaDelUsuario);
        //3
        motores = Motor.selectorDocumentos(consultaDelUsuario, 10l, motores);
        //4 Éste es todo el dispatcher
        Hashtable<Motor, List<Resultado>> resultados = new Hashtable<Motor, List<Resultado>>();
        for (Motor motor : motores) {
            //5
            List<Resultado> resultadosMotor = null;
            try {
                resultadosMotor = motor.consultar(consultaDelUsuario);
                resultados.put(motor, resultadosMotor);
            } catch (Exception e) { Assert.fail("Error de conexión a motores"); }
        }
        //6
        List<Resultado> resultadosMezclados = Resultado.mezclar(resultados);
    }
}
