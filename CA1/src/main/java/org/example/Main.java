package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        readDataFromFile(hotel);

        // Perform operations on hotel object if needed

        writeDataInFile(hotel);
    }

    private static void writeDataInFile(Hotel hotel) {
        try {
            String stateJson = hotel.logState();
            java.nio.file.Files.write(new File("src/main/resources/state.json").toPath(), stateJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readDataFromFile(Hotel hotel) {
        try {
            String jsonData = new String(java.nio.file.Files.readAllBytes(new File("src/main/resources/data.json").toPath()));
            hotel.parseJson(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}