package models;

import models.Consulta.Consulta;
import models.Resultado;
import models.motor.Motor;
import org.junit.Assert;
import play.Logger;

import java.util.Hashtable;
import java.util.List;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 14/12/11
 * Time: 02:07
 */
public class MetaMotor {
    public List<Resultado> buscar(String consultaTxt) throws Error {
        try {
            // 1
            Consulta consultaDelUsuario = new Consulta();
            consultaDelUsuario.consulta = consultaTxt;
            consultaDelUsuario.save();
            consultaDelUsuario.calcularFrecuencias();
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

            return resultadosMezclados;
        } catch (Error err) {
            throw err;
        }
    }
}
