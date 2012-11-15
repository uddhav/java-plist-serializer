package pl.maciejwalkowiak.plist.handler;

import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;

public class DoubleHandler extends SimpleHandler {
	@Override
	protected String getWrap() {
		return "real";
	}

	public boolean supports(Object object) {
		return object instanceof Float || object instanceof Double;
	}
	
	public NSObject objectify(Object object) {
		if (object instanceof Float) {
			return new NSNumber(((Float)object).doubleValue());
		}
		
		return new NSNumber(((Double)object).doubleValue());
	}
}
