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

import com.google.common.io.ByteStreams;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class Reader {

    public static PublicKey getPublicKey(String filename) throws Exception {
        try (InputStream is = Reader.class.getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                return null;
            }
            byte[] keyBytes = ByteStreams.toByteArray(is);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        }
    }

    public static PrivateKey getPrivateKey(String filename) throws Exception {
        try (InputStream is = Reader.class.getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                return null;
            }
            byte[] keyBytes = ByteStreams.toByteArray(is);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }
    }
}