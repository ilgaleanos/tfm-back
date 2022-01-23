package back.controladores.iam;

import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.flujos.iam.SesionFlujo;
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

    @CrossOrigin
    @RequestMapping(
            value = "/v1/sesion",
            method = RequestMethod.GET
    )
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ABIERTO, PermisoAlcance.ABIERTO);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            ArrayList<HashMap<String, Object>> permisos = sesionFlujo.obtenerPermisos(
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
