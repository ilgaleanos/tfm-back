package back.controladores.modelos;

import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.flujos.modelos.EvaluarFlujo;
import back.logica.io.modelos.EvaluarIn;
import back.utiles.Env;
import back.utiles.FrameworkService;
import back.utiles.SeguridadService;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.undertow.util.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class EvaluarCtl {


    private final FrameworkService fw;
    private final SeguridadService seguridadService;
    private final EvaluarFlujo evaluarFlujo;

    @Autowired
    public EvaluarCtl(
            FrameworkService fw,
            SeguridadService seguridadService,
            EvaluarFlujo evaluarFlujo
    ) {
        this.fw = fw;
        this.seguridadService = seguridadService;
        this.evaluarFlujo = evaluarFlujo;
    }


    @CrossOrigin
    @RequestMapping(
            value = "/v1/evaluar/regresion",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void evaluarRegresion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.PREDICTORES, PermisoAlcance.VISUALIZAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        EvaluarIn body = fw.getBody(req, EvaluarIn.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            String valor = evaluarFlujo.evaluarRegresion(body);
            response.put("valor", valor);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }


    @CrossOrigin
    @RequestMapping(
            value = "/v1/evaluar/red",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void evaluarRed(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.PREDICTORES, PermisoAlcance.VISUALIZAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        EvaluarIn body = fw.getBody(req, EvaluarIn.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            String valor = evaluarFlujo.evaluarRed(body);
            response.put("valor", valor);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }
}
