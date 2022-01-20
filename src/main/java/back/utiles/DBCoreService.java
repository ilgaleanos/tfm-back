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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class DBCoreService {

    protected final static Gson gson = new Gson();
    private final static Logger logger = LoggerFactory.getLogger(DBCoreService.class);

    /**
     * @param query:  String con la petición a ejecutar
     * @param params: Array con los parámetros para parametrizar la petición
     * @return JsonArray
     */
    protected ArrayList<HashMap<String, Object>> obtenerElementos(String query, Object[] params) throws SQLException {

        try (
                Connection connection = MySqlCP.getDB().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                )
        ) {
            int index;
            for (index = 0; index < params.length; index++) {
                statement.setObject(index + 1, params[index]);
            }

            ResultSet resultset = statement.executeQuery();
            ResultSetMetaData rsmd = resultset.getMetaData();

            resultset.last();
            int capacidad = resultset.getRow();
            if (capacidad == 0) {
                return new ArrayList<>(0);
            }
            resultset.beforeFirst();

            int cantidadColumnas = rsmd.getColumnCount();
            String[] props = new String[cantidadColumnas];
            for (index = 1; index <= cantidadColumnas; index++) {
                props[index - 1] = rsmd.getColumnName(index);
            }

            HashMap<String, Object> elemento = new HashMap<>(cantidadColumnas);
            ArrayList<HashMap<String, Object>> lista = new ArrayList<>(capacidad);
            while (resultset.next()) {
                elemento.clear();
                for (index = 0; index < cantidadColumnas; index++) {
                    elemento.put(props[index], resultset.getObject(props[index]));
                }
                lista.add(new HashMap<>(elemento));
            }
            resultset.close();

            return lista;
        } catch (Exception err) {
            logger.warn(err.getMessage() + " -> " + query);
            throw err;
        }
    }

    /**
     * @param query:  String con la petición a ejecutar
     * @param params: Array con los parámetros para parametrizar la petición
     * @return JsonArray
     */
    protected ArrayList<HashMap<String, Object>> obtenerElementos(
            String query, Object[] params, String[] toHashMap
    ) throws SQLException {
        try (
                Connection connection = MySqlCP.getDB().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                )
        ) {
            int index;
            for (index = 0; index < params.length; index++) {
                statement.setObject(index + 1, params[index]);
            }

            ResultSet resultset = statement.executeQuery();
            ResultSetMetaData rsmd = resultset.getMetaData();

            resultset.last();
            int capacidad = resultset.getRow();
            if (capacidad == 0) {
                return new ArrayList<>(0);
            }
            resultset.beforeFirst();

            int cantidadColumnas = rsmd.getColumnCount();
            String[] props = new String[cantidadColumnas];
            for (index = 1; index <= cantidadColumnas; index++) {
                props[index - 1] = rsmd.getColumnName(index);
            }

            HashMap<String, Object> elemento = new HashMap<>(cantidadColumnas);
            ArrayList<HashMap<String, Object>> lista = new ArrayList<>(capacidad);
            while (resultset.next()) {
                elemento.clear();
                for (index = 0; index < cantidadColumnas; index++) {
                    elemento.put(props[index], resultset.getObject(props[index]));
                }

                for (String parseable : toHashMap) {
                    Object datos = elemento.get(parseable);
                    if (datos == null) {
                        continue;
                    }
                    elemento.put(parseable, gson.fromJson(datos.toString(), HashMap.class));
                }

                lista.add(new HashMap<>(elemento));
            }
            resultset.close();

            return lista;
        } catch (Exception err) {
            logger.warn(err.getMessage() + " -> " + query);
            throw err;
        }
    }

    /**
     * @param query:     String con la petición a ejecutar
     * @param params:    Array con los parámetros para parametrizar la petición
     * @param classType: Clase para transformar la fila en un POJO
     * @param <T>:       Template para poder realizar peticiones con diferentes estructuras
     * @return JsonArray
     */
    protected <T> ArrayList<T> obtenerElementos(String query, Object[] params, Type classType) throws SQLException {
        try (
                Connection connection = MySqlCP.getDB().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY
                )

        ) {
            for (int index = 0; index < params.length; index++) {
                statement.setObject(index + 1, params[index]);
            }

            ResultSet resultset = statement.executeQuery();
            ResultSetMetaData rsmd = resultset.getMetaData();

            resultset.last();
            int capacidad = resultset.getRow();
            if (capacidad == 0) {
                return new ArrayList<>(0);
            }
            resultset.beforeFirst();

            int columnCount = rsmd.getColumnCount();
            String[] props = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                props[i - 1] = rsmd.getColumnName(i);
            }

            ArrayList<T> lista = new ArrayList<>(capacidad);
            while (resultset.next()) {
                JsonObject elemento = new JsonObject();
                for (String prop : props) {
                    elemento.addProperty(prop, resultset.getString(prop));
                }
                lista.add(gson.fromJson(elemento, classType));
            }
            resultset.close();

            return lista;
        } catch (Exception err) {
            logger.warn(err.getMessage() + " -> " + query);
            throw err;
        }
    }


    /**
     * @param query:  String con la petición a ejecutar
     * @param params: Array con los parámetros para parametrizar la petición
     * @return JsonArray
     */
    protected HashMap<String, Object> obtenerElemento(String query, Object[] params) throws SQLException {
        try (
                Connection connection = MySqlCP.getDB().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY
                )
        ) {
            int index;
            for (index = 0; index < params.length; index++) {
                statement.setObject(index + 1, params[index]);
            }

            ResultSet resultset = statement.executeQuery();
            ResultSetMetaData rsmd = resultset.getMetaData();

            int cantidadColumnas = rsmd.getColumnCount();
            String[] props = new String[cantidadColumnas];

            for (index = 1; index <= cantidadColumnas; index++) {
                props[index - 1] = rsmd.getColumnName(index);
            }

            HashMap<String, Object> elemento = new HashMap<>(cantidadColumnas);
            while (resultset.next()) {
                for (index = 0; index < props.length; index++) {
                    elemento.put(props[index], resultset.getObject(props[index]));
                }
            }
            resultset.close();

            return elemento;
        } catch (Exception err) {
            logger.warn(err.getMessage() + " -> " + query);
            throw err;
        }
    }


    /**
     * @param query:  String con la petición a ejecutar
     * @param params: Array con los parámetros para parametrizar la petición
     * @return JsonArray
     */

    protected int ejecutarQuery(String query, Object[] params) throws SQLException {
        int success = 0;
        try (
                Connection connection = MySqlCP.getDB().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY
                )
        ) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            ResultSet resultset = statement.executeQuery();

            if (resultset.next()) {
                success = resultset.getInt("result");
            }
            resultset.close();

            return success;
        } catch (Exception err) {
            logger.warn(err.getMessage() + " -> " + query);
            throw err;
        }
    }


    /**
     * @param query:  String con la petición a ejecutar
     * @param params: Array con los parámetros para parametrizar la petición
     * @return JsonArray
     */

    protected String ejecutarQueryStr(String query, Object[] params) throws SQLException {
        String success = "";
        try (
                Connection connection = MySqlCP.getDB().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY
                )
        ) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            ResultSet resultset = statement.executeQuery();

            if (resultset.next()) {
                success = resultset.getString("result");
            }
            resultset.close();

            return success;
        } catch (Exception err) {
            logger.warn(err.getMessage() + " -> " + query);
            throw err;
        }
    }


    /**
     * @param query:  String con la petición a ejecutar
     * @param params: Array con los parámetros para parametrizar la petición
     * @param data:   datos para parametrizar la petición
     * @return JsonArray
     */
    protected int insertarMultiple(String query, String[] params, ArrayList<HashMap<String, String>> data) throws SQLException {
        int success;
        try (
                Connection connection = MySqlCP.getDB().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY
                )
        ) {
            if (data.size() == 0) {
                return 1;
            }

            connection.setAutoCommit(false);

            int indice;
            int counter = 0;
            for (HashMap<String, String> info : data) {
                for (indice = 0; indice < params.length; indice++) {
                    statement.setString(indice + 1, info.get(params[indice]));
                }
                statement.addBatch();
                counter++;

                if (counter % 1000 == 0 || counter == data.size()) {
                    statement.executeBatch();
                }
            }

            connection.commit();
            connection.setAutoCommit(true);
            success = 1;
        } catch (Exception err) {
            logger.warn(err.getMessage() + " -> " + query);
            throw err;
        }

        return success;
    }
}
