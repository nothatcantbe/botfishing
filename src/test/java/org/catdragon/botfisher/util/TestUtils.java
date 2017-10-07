package org.catdragon.botfisher.util;

import org.catdragon.botfisher.hibernate.pojo.User;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;

import javax.persistence.Column;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TestUtils {
    public static File getFileInTargetDir(String fileName) {
        String targetDirLoc = System.getProperty("BOTFISHER_BUILD_DIR");

        File targetDir = new File(targetDirLoc);
        File file = new File(targetDir, fileName);

        return file;
    }

    public static void assertArrayEquals(String text, double expected[], double actual[], double delta) {
        assertThat(expected.length, is(equalTo(actual.length)));
        for (int i = 0; i < expected.length; i++) {
            if (Double.isNaN(expected[i]) && Double.isNaN(actual[i])) {
                continue;
            }
            assertThat(text + ": i = " + i, expected[i], is(equalTo(actual[i])));
        }
    }

    public static void writeStringToFile(String text, File file) throws FileNotFoundException {
        try(  PrintWriter out = new PrintWriter( file )  ){
            out.println( text );
        }
    }

    public static String readStringFromFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    public static void serialize(File write, Serializable object)
            throws IOException {
        // Serialization
        //Saving of object in a file
        FileOutputStream file = new FileOutputStream(write);
        ObjectOutputStream out = new ObjectOutputStream(file);

        // Method for serialization of object
        out.writeObject(object);

        out.close();
        file.close();
    }

    public static Serializable deserialize(File read)
            throws IOException, ClassNotFoundException {

        // Deserialization
        // Reading the object from a file
        FileInputStream file = new FileInputStream(read);
        ObjectInputStream in = new ObjectInputStream(file);

        // Method for deserialization of object
        Serializable serializable = (Serializable)in.readObject();

        in.close();
        file.close();

        return serializable;
    }
}
