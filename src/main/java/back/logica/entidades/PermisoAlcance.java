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

package back.logica.entidades;


/**
 * Permisos en la base de datos, mantenemos una copia por rendimiento
 */
public enum PermisoAlcance {
    ABIERTO(0),
    VISUALIZAR(2),
    CREAR(3),
    ACTUALIZAR(5),
    ELIMINAR(7),
    ADMINISTRATIVO(11),
    INALCANZABLE(17);

    private final int id;

    PermisoAlcance(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
