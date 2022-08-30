package com.example.fuelmanegementapp.interfaces;

import java.util.Optional;

public interface httpDataManager {
    void retrieveData(String type,Optional<String> retrievedData);
}
