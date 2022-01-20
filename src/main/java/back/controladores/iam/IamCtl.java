package back.controladores.iam;

import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.flujos.iam.IamFlujo;
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
public class IamCtl {

    private final FrameworkService fw;
    private final SeguridadService seguridadService;
    private final IamFlujo iamFlujo;

    @Autowired
    public IamCtl(
            FrameworkService fw,
            SeguridadService seguridadService,
            IamFlujo iamFlujo
    ) {
        this.fw = fw;
        this.seguridadService = seguridadService;
        this.iamFlujo = iamFlujo;
    }

    @RequestMapping(
            value = "/v1/iam",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_PERMISOS, PermisoAlcance.VISUALIZAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        IamIn.POST body = fw.getBody(req, IamIn.POST.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(4);
        try {
            ArrayList<HashMap<String, Object>> permisos = iamFlujo.obtenerPermisos(
                    body.getUsuario_id(),
                    body.getPlataforma_id()
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
