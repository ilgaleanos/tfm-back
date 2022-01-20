package back.controladores.usuarios;

import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.flujos.seguridad.SeguridadService;
import back.logica.flujos.usuarios.UsuariosFlujo;
import back.logica.io.usuarios.UsuariosIn;
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
import java.util.Optional;


@RestController
public class UsuariosCtl {

    private final FrameworkService fw;
    private final SeguridadService seguridadService;
    private final UsuariosFlujo usuariosFlujo;

    @Autowired
    public UsuariosCtl(
            FrameworkService fw,
            SeguridadService seguridadService,
            UsuariosFlujo usuariosFlujo
    ) {
        this.fw = fw;
        this.seguridadService = seguridadService;
        this.usuariosFlujo = usuariosFlujo;
    }

    @RequestMapping(
            value = "/v1/usuarios",
            method = RequestMethod.GET
    )
    public void doGet(HttpServletRequest req, HttpServletResponse resp, @RequestParam Optional<Integer> id) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_USUARIOS, PermisoAlcance.VISUALIZAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            if (id.isPresent()) {
                HashMap<String, Object> usuario = usuariosFlujo.obtenerUsuario(
                        id.get(),
                        decodedJWT.getClaim("uuid").asString()
                );
                response.put("usuario", usuario);
            } else {
                ArrayList<HashMap<String, Object>> usuarios = usuariosFlujo.obtenerUsuarios(
                        decodedJWT.getClaim("uuid").asString()
                );
                response.put("usuarios", usuarios);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }


    @CrossOrigin
    @RequestMapping(
            value = "/v1/usuarios",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_USUARIOS, PermisoAlcance.CREAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }


        UsuariosIn.POST body = fw.getBody(req, UsuariosIn.POST.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            int id = usuariosFlujo.crear(
                    body.getCorreo(),
                    body.getNombre(),
                    body.getApellido(),
                    decodedJWT.getClaim("uuid").asString()
            );
            response.put("id", id);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }

    @CrossOrigin
    @RequestMapping(
            value = "/v1/usuarios",
            method = RequestMethod.PUT,
            consumes = "application/json"
    )
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_USUARIOS, PermisoAlcance.ACTUALIZAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }


        UsuariosIn.PUT body = fw.getBody(req, UsuariosIn.PUT.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            int id = usuariosFlujo.actualizar(
                    body.getCorreo(),
                    body.getNombre(),
                    body.getApellido(),
                    decodedJWT.getClaim("uuid").asString(),
                    body.getId()
            );
            response.put("id", id);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }


    @CrossOrigin
    @RequestMapping(
            value = "/v1/usuarios",
            method = RequestMethod.DELETE
    )
    public void doDelete(HttpServletRequest req, HttpServletResponse resp, @RequestParam int id) throws IOException {
        DecodedJWT decodedJWT = seguridadService.verificarAutorizacion(req, Permiso.ADMIN_USUARIOS, PermisoAlcance.ELIMINAR);
        if (decodedJWT == null) {
            fw.sendForbiddenTEXT(resp);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(1);
        try {
            int usuario_id = usuariosFlujo.eliminar(
                    id, decodedJWT.getClaim("uuid").asString()
            );
            response.put("id", usuario_id);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }
}
