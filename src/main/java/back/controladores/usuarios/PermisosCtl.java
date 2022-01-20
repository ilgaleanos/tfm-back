package back.controladores.usuarios;

import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.flujos.seguridad.SeguridadService;
import back.logica.flujos.usuarios.PermisosFlujo;
import back.logica.io.usuarios.PermisosIn;
import back.utiles.Env;
import back.utiles.FrameworkService;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.undertow.util.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
public class PermisosCtl {
    private final FrameworkService fw;
    private final SeguridadService seguridadService;
    private final PermisosFlujo permisosFlujo;

    @Autowired
    public PermisosCtl(
            FrameworkService fw,
            SeguridadService seguridadService,
            PermisosFlujo permisosFlujo
    ) {
        this.fw = fw;
        this.seguridadService = seguridadService;
        this.permisosFlujo = permisosFlujo;
    }

    @RequestMapping(
            value = "/v1/permisos",
            method = RequestMethod.GET
    )
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_PERMISOS, PermisoAlcance.VISUALIZAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            ArrayList<HashMap<String, Object>> permisos = permisosFlujo.obtenerPermisos();
            response.put("permisos", permisos);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }

    @RequestMapping(
            value = "/v1/permisos_usuario",
            method = RequestMethod.GET
    )
    public void doGetPU(HttpServletRequest req, HttpServletResponse resp, @RequestParam int id) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_PERMISOS, PermisoAlcance.VISUALIZAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            ArrayList<HashMap<String, Object>> permisos = permisosFlujo.obtenerPermisosUsuario(id);
            response.put("permisos", permisos);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }


    @CrossOrigin
    @RequestMapping(
            value = "/v1/permiso",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_PERMISOS, PermisoAlcance.CREAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        PermisosIn.POST body = fw.getBody(req, PermisosIn.POST.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            int success = permisosFlujo.asignar(
                    body.getUsuario_id(),
                    body.getPermiso_id(),
                    body.getAlcance(),
                    decodedJWT.getClaim("uuid").asString()
            );
            response.put("success", success);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }


    @CrossOrigin
    @RequestMapping(
            value = "/v1/permiso",
            method = RequestMethod.DELETE
    )
    public void doDelete(
            HttpServletRequest req, HttpServletResponse resp,
            @RequestParam int usuario_id, @RequestParam int permiso_id
    ) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_PERMISOS, PermisoAlcance.ELIMINAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            int success = permisosFlujo.eliminar(usuario_id, permiso_id);
            response.put("success", success);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }

}
