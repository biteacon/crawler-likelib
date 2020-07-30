package com.hackathon.crawler.processor.cache;

import com.hackathon.crawler.processor.exceptions.ErrorCode;
import com.hackathon.crawler.processor.exceptions.ProcessException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class CacheTxFilesHandler {

    @Value("${crawler.path-to-cache}")
    private String path;

    public void writeCacheInFile(byte[] cache) {

        Path originalPath = Paths.get(path);
        try {
            if (Files.exists(originalPath))
                Files.delete(originalPath);
            Files.write(originalPath, cache, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new ProcessException(ErrorCode.PROCESS_EXCEPTION, "Can`t rewrite tx_cache.");
        }
    }

    public byte[] readCacheFromFile() {
        InputStream initialStream = getClass().getClassLoader().getResourceAsStream("cache/tx_cache");
        byte[] copiedData = new byte[0];
        Path originalPath = Paths.get(path);

        try {
            if (!Files.exists(originalPath)) {
                copiedData = IOUtils.toByteArray(initialStream);
                Files.write(originalPath, copiedData, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            throw new ProcessException(ErrorCode.PROCESS_EXCEPTION, "Can`t copy tx_cache." + e.getMessage());
        }

        byte[] cacheInBytes;
        try {
            cacheInBytes = Files.readAllBytes(originalPath);
        } catch (IOException e) {
            throw new ProcessException(ErrorCode.PROCESS_EXCEPTION, "Can`t read tx_cache from: " + path);
        }

        return cacheInBytes;
    }
}
