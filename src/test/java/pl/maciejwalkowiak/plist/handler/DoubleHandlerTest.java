package pl.maciejwalkowiak.plist.handler;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import java.math.BigDecimal;

import org.junit.Test;

import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;

public class DoubleHandlerTest {
	private DoubleHandler doubleHandler = new DoubleHandler();
	@Test
	public void testHandlingFloats() {
		//given
		Float object = 3.445f;
		//when
		String xml = doubleHandler.handle(object);

		//then
		assertThat(xml).isEqualTo("<real>3.445</real>");
	}

	@Test
	public void testHandlingDouble() {
		//given
		Double object = 3.99d;
		//when
		String xml = doubleHandler.handle(object);

		//then
		assertThat(xml).isEqualTo("<real>3.99</real>");
	}
	
	@Test
	public void testHandlingBigDecimal() {
		//given
		BigDecimal object = new BigDecimal(-71.5d);
		
		//when
		String xml = doubleHandler.handle(object);

		//then
		assertThat(xml).isEqualTo("<real>-71.5</real>");
		
		//when
		NSObject number = doubleHandler.objectify(object);

		//then
		assertThat(number).isEqualTo(new NSNumber(object.doubleValue()));
	}

	@Test
	public void testHandlingInteger() {
		//given
		Integer object = 4;
		try {
			//when
			String xml = doubleHandler.handle(object);
			fail();
		} catch (ObjectNotSupportedException e) {
			//then

		}


	}
}
