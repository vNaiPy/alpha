package com.naipy.alpha.config;

import com.naipy.alpha.modules.utils.ConstantVariables;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigurationLoader {

    private ConfigurationLoader() {
        throw new UnsupportedOperationException("This class cannot be instanced");
    }

    public static String getMapsKey () {
        String key = ConstantVariables.EMPTY_STRING;
        String keyPath = ConstantVariables.USER_PATH.concat("/Documentos/key.txt");
        Path path = Paths.get(keyPath);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            key = reader.readLine();
        } catch (IOException e) {
            throw new UnsupportedOperationException("Erro ao ler o arquivo: " + e.getMessage());
        }
        return key;
    }
}
