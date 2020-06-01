package view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.Controller;
import integration.dbhandler.InvalidItemIDException;
import integration.dbhandler.SystemCreator;
import model.util.IdentificationNumber;

class ViewTest {
	private SystemCreator creator;
	private Controller controller;

	private View view;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		creator = new SystemCreator();
		controller = new Controller(creator);
		view = new View(controller);
	}

	@AfterEach
	void tearDown() throws Exception {
		creator = null;
		controller = null;
		view = null;
	}

	@Test
	void testInvalidIDException() {
		IdentificationNumber invalidID = new IdentificationNumber(9889893247L);

		try {
			controller.processItem(invalidID, 3);
			fail("No exception was thrown when invalid ID was entered");
		} catch (InvalidItemIDException e) {
			assertTrue(e.getMessage().contains(invalidID.toString()),
					"Exception error message does not contain the invalid ID.");
		}
	}

	@Test
	void testCorrectInvalidIDErrorMsg() throws IOException {
		IdentificationNumber invalidID = new IdentificationNumber(9889893247L);

		PrintStream originalStream = System.out;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));

		view.testRunWithInvalidID(invalidID);
		String userErrorMsg = outStream.toString();
		assertTrue(userErrorMsg.contains(invalidID.toString()), "User error message does not specify the invalid ID.");
		
		System.setOut(originalStream);
	}
}
