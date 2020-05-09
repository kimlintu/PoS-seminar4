package integration.dbhandler.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import integration.dbhandler.InvalidItemIDException;
import integration.dbhandler.SystemCreator;
import integration.dbhandler.data.ItemDescription;
import model.util.Amount;
import model.util.IdentificationNumber;

class ItemDescriptionTest {
	private ItemDescription description;
	private SystemCreator creator;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		creator = new SystemCreator();
		description = creator.getInventorySystem().retrieveItemDescription(new IdentificationNumber(123));
	}

	@AfterEach
	void tearDown() throws Exception {
		creator = null;
		description = null;
	}

	@Test
	void testEqual() {
		ItemDescription identicalDescription = new ItemDescription("apple", new Amount(5), new Amount(0.16),
				new IdentificationNumber(123));

		assertTrue(description.equals(identicalDescription),
				"Item descriptions with identical IDs " + "does not equal.");
	}

	@Test
	void testNotEqual() throws InvalidItemIDException {
		ItemDescription differentDescription = creator.getInventorySystem()
				.retrieveItemDescription(new IdentificationNumber(666));

		assertFalse(description.equals(differentDescription), "Item descriptions with different IDs " + "are equal.");
	}

}
