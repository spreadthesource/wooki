package com.wooki.test;

import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class AuthorManagerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private AuthorManager authorManager;

	@BeforeClass
	public void initDb() throws AuthorAlreadyException {

		// Add author to the book
		Author john = new Author();
		john.setEmail("john.doe@gmail.com");
		john.setUsername("johndoe");
		john.setPassword("password");
		authorManager.addAuthor(john);

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
			authorManager.addAuthor(john);
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

}
