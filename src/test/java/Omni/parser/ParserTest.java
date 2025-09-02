package Omni.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import Omni.exceptions.InvalidArgumentException;
import Omni.exceptions.OmniException;
import Omni.storage.Storage;
import Omni.tasklist.TaskList;
import Omni.ui.Ui;

public class ParserTest {

    @Test
    public void checkValidDateString_success() throws Exception {
        Path currPath = Paths.get(".");
        assertTrue(new Parser(new Ui(), new TaskList(), new Storage(currPath))
                        .checkValidDateString("01-01-2025")
        );

        assertTrue(new Parser(new Ui(), new TaskList(), new Storage(currPath))
                .checkValidDateString("01-01-2025 1400")
        );
    }

    public void checkInvalidDateString_success() throws OmniException {
        Path currPath = Paths.get(".");
        InvalidArgumentException e1 = assertThrows(InvalidArgumentException.class,
                () -> new Parser(new Ui(), new TaskList(), new Storage(currPath))
                .checkValidDateString("01/01/2025")
        );

        InvalidArgumentException e2 = assertThrows(InvalidArgumentException.class,
                () -> new Parser(new Ui(), new TaskList(), new Storage(currPath))
                        .checkValidDateString("01-01-2025 14:00")
        );

        assertTrue(e1.getMessage().contains("Invalid date format!"));
        assertTrue(e2.getMessage().contains("Invalid date format!"));
    }
}

