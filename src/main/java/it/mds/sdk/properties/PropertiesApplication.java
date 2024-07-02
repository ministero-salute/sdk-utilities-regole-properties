/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@SpringBootApplication
public class PropertiesApplication implements CommandLineRunner {

    private static final String DOCKER_PROPERTIES_FOLDER = "/app/properties";
    private static final String PROPERTIES_FOLDER = "/sdk/properties";
    private static final String DOCKER_REGOLE_FOLDER = "/app/regole";
    private static final String REGOLE_FOLDER = "/sdk/regole";

    public static void main(String[] args) {
        SpringApplication.run(PropertiesApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        try(DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(DOCKER_REGOLE_FOLDER))) {
            ds.forEach( p -> copy(p, REGOLE_FOLDER));
        }

//        try(DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(DOCKER_PROPERTIES_FOLDER))) {
//            ds.forEach( p -> {
//                copy(p, PROPERTIES_FOLDER);
//            });
//        }

        System.exit(0);
    }

    public void copy(Path source, String destDir) {
        String name = source.getFileName().toString();
        Path dest = Paths.get(destDir, name);
        log.info("Copying {} to {} ", source, dest);
        try{
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage(), ioe);
        }
    }
}
