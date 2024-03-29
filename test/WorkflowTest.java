import models.Consulta.Consulta;
import models.motor.Motor;
import models.Resultado;
import models.XmlRpcServerMock;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 5/12/11
 * Time: 11:49 PM
 */
public class WorkflowTest extends UnitTest {

    WebServer server;

    @Before
    public void setUp() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");
        try {
         System.out.println("Attempting to start XML-RPC Server...");
         server = new WebServer(9001);
         XmlRpcServer xmlRpcServer = server.getXmlRpcServer();
         PropertyHandlerMapping phm = new PropertyHandlerMapping();

         phm.addHandler("Motor", XmlRpcServerMock.class);
         xmlRpcServer.setHandlerMapping(phm);

         server.start();
         System.out.println("Started successfully.");
         System.out.println("Accepting requests. (Halt program to stop.)");
       } catch (Exception exception) {
         System.err.println("JavaServer: " + exception);
       }
    }

    @After
    public void tearDown() {
        server.shutdown();
    }

    @Test
    public void WorkflowTest() {
        // 1
        Consulta consultaDelUsuario = Consulta.crearConsulta("Esto es una prueba de esta consulta de prueba pura prueba Tomarla Tomarlas");
        Assert.assertNotNull("Consulta: "+consultaDelUsuario.consulta+", sin palabras clave", consultaDelUsuario.frecuencias);
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
