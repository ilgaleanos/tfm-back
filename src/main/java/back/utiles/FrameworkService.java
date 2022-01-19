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

package back.utiles;

import io.undertow.util.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Esta clase es una clase de utilidades para manejar todas las peticiones, esto me brinda flexibilidad y rendimiento
 * ya que empleo serializadores minimizando la necesidad de hacer copias del contenido del request y parseos a String
 * innecesarios.
 */
@Service
public class FrameworkService {

    // si se necesita activar cors para web este es el parámetro se puede pasar al env pero aqui no suma
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final byte[] RESPONSE_ERROR_403 = "FORBIDDEN".getBytes(StandardCharsets.UTF_8);
    private static final byte[] RESPONSE_ERROR_500 = "{\"code\":500,\"error\":\"internal server error\"}".getBytes(StandardCharsets.UTF_8);

    private final SerializadorService serializadorService;


    @Autowired
    FrameworkService(
            SerializadorService serializadorService
    ) {
        this.serializadorService = serializadorService;
    }


    /*==================================================================================================================
     * MANEJADORES
     *=================================================================================================================*/


    /**
     * @param req:      la petición enviada por el usuario, necesaria por seguridad
     * @param resp:     el actual puntero de intercambio http
     * @param response: bytes con los datos de entrega en el intercambio http
     * @param tipo:     Tipo de elemento retornado al cliente
     */
    public void sendBYTES(HttpServletRequest req, HttpServletResponse resp, byte[] response, int length, String tipo) throws IOException {
        resp.setContentType(tipo);
        try (ServletOutputStream out = resp.getOutputStream()) {
            out.write(response, 0, length);
        }
    }


    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp:     el actual puntero de intercambio http
     * @param response: estructura final con los datos de entrega en el intercambio http
     */
    public void sendJSON(HttpServletResponse resp, HashMap<String, Object> response) throws IOException {
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            serializadorService.serialize(response, outputStream);
        }
    }


    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp:    el actual puntero de intercambio http
     * @param entrada: parámetros requeridos por la petición
     */
    public void sendBadRequestJSON(HttpServletResponse resp, Object entrada) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HashMap<String, Object> response = new HashMap<>();
        response.put("code", 400);
        if (entrada != null) {
            Field[] parametros = entrada.getClass().getDeclaredFields();
            HashMap<String, String> campos = new HashMap<>(parametros.length);
            for (Field param : parametros) {
                if (param.getAnnotatedType().getType().equals(Pattern.class)) continue;
                campos.put(param.getName(), param.getType().getSimpleName());
            }
            response.put("required", campos);
        } else {
            response.put("message", "invalid json");
        }

        try (ServletOutputStream out = resp.getOutputStream()) {
            serializadorService.serialize(response, out);
        }
    }


    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp:  el actual puntero de intercambio http
     * @param error: excecion para manejar
     */
    public void sendErrorJSON(HttpServletResponse resp, Exception error) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (Env.AUTH_ENVIRONMENT != null && Env.AUTH_ENVIRONMENT.equals("DEBUG")) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("code", 0);
            hm.put("error", error.getMessage());
            hm.put("trace", Arrays.toString(error.getStackTrace()));
            try (ServletOutputStream out = resp.getOutputStream()) {
                serializadorService.serialize(hm, out);
            }
            return;
        }

        try (ServletOutputStream out = resp.getOutputStream()) {
            out.write(RESPONSE_ERROR_500);
        }
    }


    /**
     * Finaliza el intercambio HTTP con el string de no autorizado
     *
     * @param resp: el actual puntero de intercambio http
     */
    public void sendForbiddenTEXT(HttpServletResponse resp) throws IOException {
        resp.setStatus(StatusCodes.FORBIDDEN);
        resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
        try (ServletOutputStream out = resp.getOutputStream()) {
            out.write(RESPONSE_ERROR_403);
        }
    }


    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp: el actual puntero de intercambio http
     * @param resp: cadena final de entrega en el intercambio http
     */
    public void sendTEXT(HttpServletResponse resp, String response) throws IOException {
        resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
        try (ServletOutputStream out = resp.getOutputStream()) {
            out.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    /*==================================================================================================================
     *  obtener body raw
     *=================================================================================================================*/

    /**
     * Transforma el cuerpo de la petición a una clase establecida, elaborado a nivel medio de rendimiento
     * podriamos crear los precompilados de las clases pero es innecesario ese nivel de optimizacion para esta tarea,
     * con este nivel es suficiente parta esta tarea
     *
     * @param req           HttpServletRequest
     * @param acumularClass
     * @return HashMap
     * @throws IOException cuando no pueda crear el reader
     */
    public <T> T getBody(HttpServletRequest req, Class<T> acumularClass) throws IOException {
        req.setCharacterEncoding(UTF8);
        try {
            return serializadorService.deserialize(acumularClass, req.getInputStream());
        } catch (Exception err) {
            return null;
        }
    }
}
