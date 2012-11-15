package pl.maciejwalkowiak.plist.handler;

import org.junit.Test;

import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;

import static org.fest.assertions.Assertions.assertThat;

public class BooleanHandlerTest {
	private BooleanHandler handler = new BooleanHandler();

	@Test
	public void shouldReturnTrue() {
		//given
		Boolean toHandle = true;

		//when
		String result = handler.handle(toHandle);

		//then
		assertThat(result).isEqualTo("<true/>");
		
		//when
		NSObject object = handler.objectify(toHandle);

		//then
		assertThat(object).isEqualTo(new NSNumber(toHandle));
	}

	@Test
	public void shouldReturnFalse() {
		//given
		Boolean toHandle = false;

		//when
		String result = handler.handle(toHandle);

		//then
		assertThat(result).isEqualTo("<false/>");
		
		//when
		NSObject object = handler.objectify(toHandle);

		//then
		assertThat(object).isEqualTo(new NSNumber(toHandle));
	}
}
