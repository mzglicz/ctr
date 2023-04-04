package org.pl.maciej.ctr.tracking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(OutputCaptureExtension.class)
class LogEventConsumerTest {

    @Test
    void shouldNotThrowWhenFailedToConsume(CapturedOutput capturedOutput) throws JsonProcessingException {
        var objMapper = Mockito.mock(ObjectMapper.class);
        var exception = Mockito.mock(JsonProcessingException.class);
        var underTest = new LogEventConsumer(objMapper);
        var event = new Event("eid", "elid", "aid", Map.of());
        when(objMapper.writeValueAsString(any())).thenThrow(exception);
        assertDoesNotThrow(() -> underTest.consumer(event));
        assertTrue(capturedOutput.getOut().contains("Not able to serialize event"));
    }

    @Test
    void shouldConsumeNormally(CapturedOutput capturedOutput) throws JsonProcessingException {
        var objMapper = Mockito.mock(ObjectMapper.class);

        var underTest = new LogEventConsumer(objMapper);
        var serializationResult = "{\"id\": \"some-value\"}";
        when(objMapper.writeValueAsString(any())).thenReturn(serializationResult);
        var event = new Event("eid", "elid", "aid", Map.of());
        underTest.consumer(event);
        assertTrue(capturedOutput.getOut().contains(serializationResult));
    }
}