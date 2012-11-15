package pl.maciejwalkowiak.plist.handler;

import java.math.BigDecimal;

import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;

public class DoubleHandler extends SimpleHandler {
	@Override
	protected String getWrap() {
		return "real";
	}

	public boolean supports(Object object) {
		return object instanceof Float || object instanceof Double || object instanceof BigDecimal;
	}
	
	public NSObject objectify(Object object) {
		if (object instanceof Float) {
			return new NSNumber(((Float)object).doubleValue());
		} else if (object instanceof BigDecimal) {
			return new NSNumber(((BigDecimal)object).doubleValue());
		}
		
		return new NSNumber(((Double)object).doubleValue());
	}
}
