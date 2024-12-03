package com.naipy.alpha.config;

import com.naipy.alpha.modules.utils.ConstantVariables;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationLoaderTest {

    @Test
    void getMapsKey() {
        String key = ConstantVariables.EMPTY_STRING;
        String keyPath = ConstantVariables.USER_PATH.concat("/Documentos/key.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(keyPath));
            key = br.readLine();
        } catch (IOException e) {
            throw new UnsupportedOperationException("Erro ao ler o arquivo: " + e.getMessage());
        }
        assertNotEquals(ConstantVariables.EMPTY_STRING, key);
        assertTrue(key.startsWith("&key="));
    }
}