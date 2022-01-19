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

package back.logica.flujos.seguridad;

import back.logica.basededatos.seguridad.SeguridadDB;
import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.entidades.Usuario;
import back.utiles.Env;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.SQLException;
import java.util.*;

@Component
public class SeguridadService {

    private final static Logger logger = LoggerFactory.getLogger(SeguridadService.class);
    private final static String AUTH = "Authorization";


    private static Algorithm algorithm;
    private static JWTVerifier verifier;
    private static boolean leidas = false;

    private final SeguridadDB sdb;

    @Autowired
    SeguridadService(SeguridadDB sdb) {
        this.sdb = sdb;
    }


    /**
     * Lector de las claves pública y privada evitando acceso a disco
     */
    private synchronized static void readKeys() {
        if (leidas) return;
        try {
            RSAPublicKey publicKey = (RSAPublicKey) Reader.getPublicKey(Env.AUTH_PUBLIC_KEY_PATH);
            RSAPrivateKey privateKey = (RSAPrivateKey) Reader.getPrivateKey(Env.AUTH_PRIVATE_KEY_PATH);
            algorithm = Algorithm.RSA512(publicKey, privateKey);
            verifier = JWT.require(algorithm).withIssuer(Env.AUTH_ISSUER).acceptLeeway(1).build();
            leidas = true;
        } catch (Exception err) {
            logger.error("readKeys = " + Arrays.toString(err.getStackTrace()));
        }
    }


    /**
     * Generador de Token de Session
     *
     * @param uuid     : identificador único de cada usuario
     * @param cantidad : cantidad del tiempo para la vida del token
     * @param unidad   : unidad de tiempo para fijar la vida del token
     * @return String Token generado
     */
    public static String contruirToken(String uuid, int cantidad, int unidad) {
        if (!leidas) readKeys();
        try {
            final Date today = new Date();
            final Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(unidad, cantidad);
            final Date newDate = c.getTime();

            return JWT.create()
                    .withIssuer(Env.AUTH_ISSUER)
                    .withClaim("uuid", uuid)
                    .withIssuedAt(today)
                    .withExpiresAt(newDate)
                    .sign(algorithm);

        } catch (Exception err) {
            logger.error("readKeys = " + Arrays.toString(err.getStackTrace()));
        }

        return "";
    }


    /**
     * Generador de Token de Session basado en cookie
     *
     * @param usuario : identificador único de cada usuario
     * @param unidad  :  estructura con los datos para la generacion del token
     * @return String Token generado y cookie fijada
     */
    public String generarToken(Usuario usuario, int unidad) throws SQLException {
        if (!leidas) readKeys();

        String token = contruirToken(
                usuario.getUuid(),
                usuario.getVida_horas(),
                unidad
        );

        sdb.fijarToken(usuario);
        return token;
    }


    /**
     * @param req: petición realizada
     * @return cabecera de autenticación extraído de la petición
     */
    private String obtenerTokenDeCabeceras(HttpServletRequest req) {
        String token = req.getHeader(AUTH);
        if (token != null && !token.isEmpty()) {
            return token;
        }

        final Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AUTH)) {
                token = cookie.getValue();
                break;
            }
        }

        return token;
    }


    /**
     * Validador del token de session
     *
     * @param token   : String que contiene el token
     * @param permiso : Permiso para buscar si el acceso esta disponible
     * @param alcance
     * @return JWT decodificado para su manipulación en los controladores
     */
    public DecodedJWT esValido(String token, Permiso permiso, PermisoAlcance alcance) {
        if (!leidas) readKeys();
        try {
            DecodedJWT tokenDecoded = verifier.verify(token);

            if (tokenDecoded != null) {

                Map<String, Object> permisos = tokenDecoded.getClaim("P").asMap();
                String permisoStr = String.valueOf(permiso.getValue());

                if (
                        permiso.equals(Permiso.ABIERTO) || (
                                permisos.containsKey(permisoStr) &&
                                        (((Integer) permisos.get(permisoStr)) % alcance.getValue()) == 0
                        )
                ) {
                    if (sdb.existeToken(tokenDecoded.getClaim("uuid").asString(), permiso, alcance) == 1) {
                        return tokenDecoded;
                    }
                }
            }

            return null;
        } catch (Exception err) {
            return null;
        }
    }


    /**
     * Verificador de autorización del usuario ya sea por header o por cookie
     *
     * @param req:     puntero http de intercambio de la petición actual
     * @param permiso: Permiso para buscar si el acceso esta disponible
     * @param alcance: nivel del alcance necesario para dar autorización
     * @return JWT decodificado para su manipulación en los controladores
     */
    public DecodedJWT verificarAutorizacion(HttpServletRequest req, Permiso permiso, PermisoAlcance alcance) {
        final String token = this.obtenerTokenDeCabeceras(req);
        return esValido(token, permiso, alcance);
    }


    /**
     * Eliminamos el token tanto de la base como de la cookie, el token queda inválido
     *
     * @param req:  petición del usuario
     * @param resp: respuesta al usuario
     */
    public void eliminarSesion(HttpServletRequest req, HttpServletResponse resp) {
        final String token = this.obtenerTokenDeCabeceras(req);
        if (token == null || token.isEmpty()) {
            return;
        }

        try {
            DecodedJWT decodedJWT = new JWT().decodeJwt(token);
            String uuid = decodedJWT.getClaim("uuid").asString();

            if (uuid != null && !uuid.isEmpty()) {
                sdb.borrarToken(uuid);
            }
        } catch (Exception ignored) {
        }
    }


    /**
     * Utilidad para generar tokens de forma segura, el UUID puede llegar a tener vulnerabilidad de predicción
     *
     * @return cadena segura
     */
    public String generateTokenSeguro(int longitud) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[longitud];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes).substring(0, longitud);
    }


    /**
     * Utilidad para generar otps de forma segura
     *
     * @return cadena segura
     */
    public String generateOTPSeguro(int longitud) {
        StringBuilder generatedOTP = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        try {
            secureRandom = SecureRandom.getInstance(secureRandom.getAlgorithm());
            for (int i = 0; i < longitud; i++) {
                generatedOTP.append(secureRandom.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedOTP.toString();
    }
}
