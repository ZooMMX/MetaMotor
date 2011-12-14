package models;

import models.motor.Motor;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 14/12/11
 * Time: 05:36 AM
 */
public class IndexadorTest extends UnitTest {

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
    public void testIndexar() throws Exception {
        Indexador indexador = new Indexador();
        indexador.indexar();

        Motor motor1 = Motor.all().first();
        Termino t1   = Termino.findOrCreate("hol");
        Termino t2   = Termino.findOrCreate("consult");
        Termino t3   = Termino.findOrCreate("tom");
        Termino t4   = Termino.findOrCreate("es");
        Termino t5   = Termino.findOrCreate("estar");

        Assert.assertTrue( Frecuencia.findOrCreate(motor1, t1).frecuencia == 4l );
        Assert.assertTrue( Frecuencia.findOrCreate(motor1, t2).frecuencia == 7l );
        Assert.assertTrue( Frecuencia.findOrCreate(motor1, t3).frecuencia == 1l );
        Assert.assertTrue( Frecuencia.findOrCreate(motor1, t4).frecuencia == 5l );
        Assert.assertTrue( Frecuencia.findOrCreate(motor1, t5).frecuencia == 2l );
    }
}
