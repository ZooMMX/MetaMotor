package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index(String consulta) {

        validation.required(consulta);

        List<Resultado> resultados = new ArrayList<Resultado>();
        if(consulta != null && !consulta.equals("")) {
            MetaMotor metaMotor = new MetaMotor();
            resultados = metaMotor.buscar(consulta);
        }
        render(resultados);
    }

}