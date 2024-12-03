package com.naipy.alpha.config;

import com.naipy.alpha.modules.utils.ConstantVariables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationLoader {

    private ConfigurationLoader() {
        throw new UnsupportedOperationException("This class cannot be instanced");
    }

    public static String getMapsKey () {
        String key = ConstantVariables.EMPTY_STRING;
        String keyPath = ConstantVariables.USER_PATH.concat("/Documentos/key.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(keyPath));
            key = br.readLine();
        } catch (IOException e) {
            throw new UnsupportedOperationException("Erro ao ler o arquivo: " + e.getMessage());
        }
        return key;
    }
}
