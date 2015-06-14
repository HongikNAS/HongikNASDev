package kr.ac.hongik.nas.ftpserver.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class LoginTest {
	
	@Test
	public void 권한테스트() {
		//given
		Login test = new Login("root", "1234");
		
		//when
		boolean result = test.tryToAuthorize();
		
		//then
		assertThat(result, is(true));
	}
}
