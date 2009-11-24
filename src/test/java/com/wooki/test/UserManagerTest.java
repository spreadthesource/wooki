package com.wooki.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;
import com.wooki.services.UserManager;

/**
 * Verify authoring management.
 * 
 * @author ccordenier
 * 
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UserManagerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private UserManager userManager;

	@BeforeClass
	public void initDb() throws UserAlreadyException {

		// Add author to the book
		User john = new User();
		john.setEmail("john.doe@gmail.com");
		john.setUsername("johndoe");
		john.setFullname("John Doe");
		john.setPassword("password");
		userManager.addUser(john);

	}

	/**
	 * Verify that we cannot create a user that already exists.
	 * 
	 */
	@Test
	public void checkUserExists() {
		User user = userManager.findByUsername("JohNDOE");
		Assert.assertNotNull(user, "John doe exist.");
	}

	/**
	 * Verifies that an exception is thrown when adding an author that already
	 * exists.
	 * 
	 */
	@Test
	public void verifyAuthorAlreadyExists() {
		User john = new User();
		john.setEmail("john.doe@hotmail.com");
		john.setUsername("johndoe");
		john.setFullname("John Doe");
		john.setPassword("passpass");
		try {
			userManager.addUser(john);
			Assert
					.fail("User john already exists, call add must raise an exception.");
		} catch (UserAlreadyException uaEx) {
			uaEx.printStackTrace();
		}
	}

	/**
	 * Verify that findByUsername is case insensitive.
	 * 
	 */
	@Test
	public void checkFindByUserName() {
		User user = userManager.findByUsername("JoHnDoe");
		Assert.assertNotNull(user);
	}

}
