package models.Consulta;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 8/12/11
 * Time: 10:18 PM
 */
import snowball.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author octavioruizcastillo
 */
public class ProcesadorConsulta {

    private   String                    doc;
    protected ArrayList<String>         terminos;
    protected HashMap<String, Long>     frecuencias;

    private void lematizar() {

        MySpaStemmer stem = new MySpaStemmer();
        terminos  = new ArrayList<String>();

        String docLimpio = doc;
        docLimpio = docLimpio.replaceAll("[^A-Za-z0-9áéíóúÁÉÍÓÚñÑ ]", " "); //Transforma todos los símbolos en espacios
        docLimpio = docLimpio.replace("[ \t]{2,}", " "); //Remueve espacios excedentes
        docLimpio = docLimpio.toLowerCase();

        for (String palabra : docLimpio.split(" ")) {
            stem.setCurrent(palabra);
            stem.stem();
            final String lexema = stem.getCurrent();
            if(lexema.length() > 2) {
                terminos.add(lexema);
            }
        }
    }

    public AbstractMap<String, Long> contarFrecuencias() {
        lematizar();

        frecuencias = new HashMap<String, Long>();

        for (String termino : terminos) {
            Long count = frecuencias.get(termino);
            count = count == null ? 1 : count + 1;
            frecuencias.put(termino, count);
        }

        return frecuencias;

    }

    public void setConsulta(String doc) {
        this.doc = doc;
    }
}


