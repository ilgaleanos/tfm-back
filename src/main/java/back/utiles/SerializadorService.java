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

import com.dslplatform.json.DslJson;
import com.dslplatform.json.runtime.Settings;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * En esta función implementamos el serializador de nuestras peticiones
 */
@Service
public class SerializadorService {

    private final static DslJson.Settings<Object> settings = Settings.withRuntime().includeServiceLoader();
    public final static DslJson<Object> dslJson = new DslJson<>(settings);


    public void serialize(HashMap<String, Object> response, ServletOutputStream outputStream) {
        try {
            dslJson.serialize(response, outputStream);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }


    public void serialize(Exception err, ServletOutputStream outputStream) {
        try {
            dslJson.serialize(err, outputStream);
        } catch (IOException err2) {
            err2.printStackTrace();
        }
    }


    public byte[] serialize(Object serializable) {
        try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream()) {
            dslJson.serialize(serializable, outputStream);
            return outputStream.toByteArray();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }


    public <T> T deserialize(Class<T> acumularClass, ServletInputStream inputStream) {
        try {
            return dslJson.deserialize(acumularClass, inputStream);
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }


    public <T> T deserialize(Class<T> acumularClass, byte[] datos) {
        try {
            return dslJson.deserialize(acumularClass, datos, datos.length);
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }
}
