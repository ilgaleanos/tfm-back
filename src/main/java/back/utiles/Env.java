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

import ch.qos.logback.classic.Level;
import org.springframework.stereotype.Component;

/**
 * VARIABLES GLOBALES PARA LOS DIFERENTES ENTORNOS
 * <p>
 * Poner las variables de entorno y centralizarlas en una sola clase solo cambia el origen.
 * Aquí puedes tomarlas directamente ya sea del entorno o definidas aquí.
 */
@Component
public class Env {

    // ENV
    public static final String AUTH_ENVIRONMENT = System.getenv("AUTH_ENVIRONMENT") == null ? "" : System.getenv("AUTH_ENVIRONMENT");

    // RECAPTCHA
    public static final String AUTH_RECAPTCHA_CLAVE_SECRETA = System.getenv("AUTH_RECAPTCHA_CLAVE_SECRETA");
    public static final String AUTH_RECAPTCHA_FAKE = System.getenv("AUTH_RECAPTCHA_FAKE");

    // JWT
    public static final String AUTH_ISSUER = System.getenv("AUTH_ISSUER");
    public static final String AUTH_PUBLIC_KEY_PATH = System.getenv("AUTH_PUBLIC_KEY_PATH");
    public static final String AUTH_PRIVATE_KEY_PATH = System.getenv("AUTH_PRIVATE_KEY_PATH");

    // COOKIE
    public static final Boolean AUTH_COOKIE_HTTP_ONLY = Boolean.parseBoolean(System.getenv("AUTH_COOKIE_HTTP_ONLY"));
    public static final Boolean AUTH_COOKIE_SECURE = Boolean.parseBoolean(System.getenv("AUTH_COOKIE_SECURE"));

    // SMTP
    public static final String AUTH_SMTP_USUARIO = System.getenv("AUTH_SMTP_USUARIO");
    public static final String AUTH_SMTP_CLAVE = System.getenv("AUTH_SMTP_CLAVE");

    // POSTGRESQL
    public static final int AUTH_MYSQL_CONNECTIONS = 3;
    static final String AUTH_MYSQL_CLOUD_SQL_CONNECTION_NAME = System.getenv("AUTH_MYSQL_CLOUD_SQL_CONNECTION_NAME");
    static final String AUTH_MYSQL_JDBCURL = System.getenv("AUTH_MYSQL_JDBCURL");
    static final String AUTH_MYSQL_USERNAME = System.getenv("AUTH_MYSQL_USERNAME");
    static final String AUTH_MYSQL_PASSWORD = System.getenv("AUTH_MYSQL_PASSWORD");

    // LOGS
    public static Level levelLog = AUTH_ENVIRONMENT.equals("DEBUG") ? Level.INFO : Level.WARN;
}
