/**
 * Copyright 2022 Leito. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package back.controladores.seguridad;

import back.logica.entidades.Usuario;
import back.logica.flujos.seguridad.CredencialesFlujo;
import back.logica.flujos.seguridad.SeguridadService;
import back.logica.io.seguridad.CredencialesIn;
import back.utiles.Env;
import back.utiles.FrameworkService;
import io.undertow.util.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


/**
 *
 */
@RestController
public class CredencialesCtrl extends HttpServlet {


    private final FrameworkService fw;
    private final SeguridadService seguridadService;
    private final CredencialesFlujo credencialesFlujo;


    @Autowired
    public CredencialesCtrl(
            FrameworkService fw,
            SeguridadService seguridadService,
            CredencialesFlujo credencialesFlujo
    ) {
        this.fw = fw;
        this.seguridadService = seguridadService;
        this.credencialesFlujo = credencialesFlujo;
    }


    @CrossOrigin
    @RequestMapping(
            value = "/v1/generar_otp",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void generarOTP(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CredencialesIn.GenerarOTP body = fw.getBody(req, CredencialesIn.GenerarOTP.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(4);
        try {
            String uuid = credencialesFlujo.generarOTP(
                    body.getTelefono(),
                    body.getCorreo(),
                    body.getCaptcha()
            );

            response.put("uuid", uuid);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }


    @CrossOrigin
    @RequestMapping(
            value = "/v1/generar_token",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CredencialesIn.GenerarToken body = fw.getBody(req, CredencialesIn.GenerarToken.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(resp, body);
            return;
        }

        HashMap<String, Object> response = new HashMap<>(4);
        try {
            Usuario usuario = credencialesFlujo.ingresar(
                    body.getUuid(),
                    body.getOtp(),
                    body.getCaptcha()
            );

            if (usuario != null) {
                response.put("token", usuario.getToken());
                response.put("correo", usuario.getCorreo());
                response.put("telefono", usuario.getTelefono());
                response.put("nombre", usuario.getNombre());
                response.put("apellido", usuario.getApellido());
            } else {
                response.put("token", null);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }
}
