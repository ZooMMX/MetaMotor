package models.motor;

import models.Consulta.Consulta;
import models.Frecuencia;
import models.Termino;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathOps implements Serializable {
    static List<Motor> calcularRm(Consulta consulta, Long resultadosEsperados, List<Motor> motores) {
        List<Termino> terminos = new ArrayList<Termino>();
        for (Frecuencia frecuencia : consulta.frecuencias) {
            terminos.add(frecuencia.termino);
        }
        BigDecimal cv2Sum = new BigDecimal(0d);
        BigDecimal cvv = new BigDecimal(0d);
        HashMap<String, HashMap<Motor, Long>> completa = new HashMap<String, HashMap<Motor, Long>>();
        for (Motor motor : motores) {

            for (Termino termino : terminos) {
                Long totalFrecuencias = (Long) Frecuencia.find("SELECT SUM(f.frecuencia) FROM Frecuencia as f WHERE termino = ? AND fuente.class = Motor", termino).query.getSingleResult();
                if (totalFrecuencias == null) {
                    continue;
                }
                if (completa.get(termino.termino) == null) {
                    completa.put(termino.termino, new HashMap<Motor, Long>());
                }
                Frecuencia frecDFmj = Frecuencia.findOrCreate(motor, termino); // Inicialización perezosa
                completa.get(termino.termino).put(motor, frecDFmj.frecuencia);
            }
        }
        for (Motor motor : motores) {
            for (String termino : completa.keySet()) {
                Long frec = completa.get(termino).get(motor);
                BigDecimal a = new BigDecimal(frec.doubleValue() / motor.nDocs.doubleValue());
                BigDecimal b = new BigDecimal(0d);
                BigDecimal c = new BigDecimal(0d);
                for (Map.Entry<Motor, Long> motorFrec : completa.get(termino).entrySet()) {
                    if (motorFrec.getKey() == motor) {
                        continue;
                    }
                    b = b.add(new BigDecimal(motorFrec.getValue().doubleValue()));
                    c = c.add(new BigDecimal(motor.nDocs.doubleValue()));
                }
                BigDecimal d = b.divide(c);
                BigDecimal cv = a.divide(a.add(d), BigDecimal.ROUND_HALF_UP);
                BigDecimal cv2 = (cv.subtract(promedio(completa.get(termino)))).pow(2);
                cv2Sum = cv2Sum.add(cv2);
            }
        }
        cvv = cvv.add(cv2Sum);
        cvv = cvv.divide(new BigDecimal(motores.size()));

        for (Motor motor : motores) {
            BigDecimal rm = new BigDecimal(0d);
            for (String termino : completa.keySet()) {
                rm = rm.add(new BigDecimal(completa.get(termino).get(motor).doubleValue()).multiply(cvv));
            }
            motor.setRm(rm);
        }

        return motores;
    }

    static BigDecimal promedio(HashMap<Motor, Long> motorLongHashMap) {
        BigDecimal resultado = new BigDecimal(0d);
        BigDecimal numMotores = new BigDecimal(motorLongHashMap.size());
        for (Long aLong : motorLongHashMap.values()) {
            BigDecimal frecuencia = new BigDecimal(aLong.doubleValue());
            resultado = resultado.add(frecuencia);
        }
        return resultado.divide(numMotores);
    }
}