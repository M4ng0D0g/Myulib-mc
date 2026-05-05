package com.myudog.myulib.api.control;

import com.myudog.myulib.api.control.network.ControlInputPayload;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class ControlInputPayloadTest {

    @Test
    void controlInputPayloadUsesExpectedIdentifierAndFields() {
        assertEquals(Identifier.fromNamespaceAndPath(Myulib.MOD_ID, "control_input"), ControlInputPayload.ID,
                "ControlInputPayload ID must stay stable for network compatibility");

        ControlInputPayload payload = new ControlInputPayload(true, false, true, false, true, false, 30.0f, -15.0f);
        assertTrue(payload.up(), "Payload should keep up flag");
        assertFalse(payload.down(), "Payload should keep down flag");
        assertTrue(payload.left(), "Payload should keep left flag");
        assertFalse(payload.right(), "Payload should keep right flag");
        assertTrue(payload.jumping(), "Payload should keep jumping flag");
        assertFalse(payload.sneaking(), "Payload should keep sneaking flag");
        assertEquals(30.0f, payload.yaw(), "Payload should keep yaw value");
        assertEquals(-15.0f, payload.pitch(), "Payload should keep pitch value");
    }
}

