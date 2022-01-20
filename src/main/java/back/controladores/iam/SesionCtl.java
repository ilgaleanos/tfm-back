package back.controladores.iam;

import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.flujos.iam.SesionFlujo;
import back.logica.flujos.seguridad.SeguridadService;
import back.logica.io.iam.IamIn;
import back.utiles.Env;
import back.utiles.FrameworkService;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.undertow.util.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase de presentación del api
 */
@RestController
public class SesionCtl {

    private final FrameworkService fw;
    private final SeguridadService seguridadService;
    private final SesionFlujo sesionFlujo;

    @Autowired
    public SesionCtl(
            FrameworkService fw,
            SeguridadService seguridadService,
            SesionFlujo sesionFlujo
    ) {
        this.fw = fw;
        this.seguridadService = seguridadService;
        this.sesionFlujo = sesionFlujo;
    }

    @RequestMapping(
            value = "/v1/sesion",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ABIERTO, PermisoAlcance.ABIERTO);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        IamIn.Sesion body = fw.getBody(req, IamIn.Sesion.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(4);
        try {
            ArrayList<HashMap<String, Object>> permisos = sesionFlujo.obtenerPermisos(
                    body.getPlataforma_id(),
                    decodedJWT.getClaim("uuid").asString()
            );
            response.put("permisos", permisos);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }
}
