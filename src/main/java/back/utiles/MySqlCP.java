/**
 * Copyright 2022 Leito. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>s
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package back.utiles;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;


/**
 * IMPORTANTE: La columna de password de los usuarios es latin1
 * <p>
 * Para determinar el taño del pool se ha toma connections = ((core_count * 2) + effective_spindle_count)
 */
public class MySqlCP {


    private final static Logger logger = LoggerFactory.getLogger(MySqlCP.class);

    public static boolean iniciado = false;
    private static DataSource db;

    private static synchronized void createPool() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Env.levelLog);

        HikariConfig config = new HikariConfig();

        config.addDataSourceProperty("autoReconnect", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("truecachePrepStmts", "true");
        config.addDataSourceProperty("trustServerCertificate", "true");
        config.addDataSourceProperty("useOldAliasMetadataBehavior", "yes");

//        config.addDataSourceProperty("useSSL", "true");
//        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
//        config.addDataSourceProperty("cloudSqlInstance", Env.AUTH_MYSQL_CLOUD_SQL_CONNECTION_NAME);
//        config.addDataSourceProperty("enableIamAuth", "true");
//        config.addDataSourceProperty("characterEncoding", "UTF-8");
//        config.addDataSourceProperty("connectionCollation", "utf8mb4_bin");

        config.setJdbcUrl(Env.AUTH_MYSQL_JDBCURL);
        config.setUsername(Env.AUTH_MYSQL_USERNAME);
        config.setPassword(Env.AUTH_MYSQL_PASSWORD);
        config.setMaximumPoolSize(Env.AUTH_MYSQL_CONNECTIONS);

        MySqlCP.db = new HikariDataSource(config);
        iniciado = true;

        logger.info("+-------------------------------+");
        logger.info("|       Connected to MySQL      |");
        logger.info("+-------------------------------+");
    }

    public static DataSource getDB() {
        if (!iniciado) createPool();
        return MySqlCP.db;
    }
}
