package back.controladores.seguridad;

import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.flujos.seguridad.LogoutFlujo;
import back.logica.flujos.seguridad.SeguridadService;
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
import java.util.HashMap;

@RestController
public class LogoutController {
    private final FrameworkService fw;
    private final SeguridadService seguridadService;
    private final LogoutFlujo logoutFlujo;

    @Autowired
    public LogoutController(
            FrameworkService fw,
            SeguridadService seguridadService,
            LogoutFlujo logoutFlujo
    ) {
        this.fw = fw;
        this.seguridadService = seguridadService;
        this.logoutFlujo = logoutFlujo;
    }

    @RequestMapping(
            value = "/v1/logout",
            method = RequestMethod.DELETE
    )
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ABIERTO, PermisoAlcance.ABIERTO);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        HashMap<String, Object> response = new HashMap<>();
        try {
            logoutFlujo.eliminar(
                    decodedJWT.getClaim("uuid").asString()
            );
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }
}
