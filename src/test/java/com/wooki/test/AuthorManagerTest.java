package com.wooki.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.domain.exception.AuthorAlreadyException;
import com.wooki.domain.model.Author;
import com.wooki.services.AuthorManager;

/**
 * Verify authoring management.
 * 
 * @author ccordenier
 * 
 */
@ContextConfiguration(locations = { "/daoContext.xml",
		"/applicationContext.xml" })
public class AuthorManagerTest extends AbstractTestNGSpringContextTests {

	private AuthorManager authorManager;

	@BeforeClass
	public void initDb() throws AuthorAlreadyException {

		authorManager = (AuthorManager) applicationContext
				.getBean("authorManager");

		// Add author to the book
		Author john = new Author();
		john.setEmail("john.doe@gmail.com");
		john.setUsername("johndoe");
		john.setPassword("password");
		john = authorManager.addAuthor(john);

	}

	/**
	 * Verify that we cannot create a user that already exists.
	 * 
	 */
	@Test
	public void checkUserExists() {
		Author author = authorManager.findByUsername("JohNDOE");
		Assert.assertNotNull(author, "John doe exist.");
	}

	/**
	 * Verifies that an exception is thrown when adding an author that already
	 * exists.
	 * 
	 */
	@Test
	public void verifyAuthorAlreadyExists() {
		Author john = new Author();
		john.setEmail("john.doe@hotmail.com");
		john.setUsername("johndoe");
		john.setPassword("passpass");
		try {
			john = authorManager.addAuthor(john);
			Assert
					.fail("User john already exists, call add must raise an exception.");
		} catch (AuthorAlreadyException uaEx) {
			uaEx.printStackTrace();
		}
	}

	/**
	 * Verify that findByUsername is case insensitive.
	 * 
	 */
	@Test
	public void checkFindByUserName() {
		Author author = authorManager.findByUsername("JoHnDoe");
		Assert.assertNotNull(author);
	}

	/**
	 * Verify password handling.
	 *
	 */
	@Test
	public void checkPasswordVerification() {
		Assert.assertTrue(authorManager.checkPassword("JoHnDoe", "password"),
				"John password is 'password. This must match.");
		Assert.assertFalse(
				authorManager.checkPassword("JoHnDoe", "falsePassword"),
				"John password is 'password. This must not match.");
	}
}
