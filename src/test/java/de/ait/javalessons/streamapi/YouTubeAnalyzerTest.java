package de.ait.javalessons.streamapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YouTubeAnalyzerTest {

    @Test
    void  testIsMore10MSuccess() {

        //Average
        YouTubeAnalyzer youTubeAnalyzer = new YouTubeAnalyzer();

        //Action
        boolean result = youTubeAnalyzer.isMore10M();

        //Assert
        assertTrue(result);
    }
}
