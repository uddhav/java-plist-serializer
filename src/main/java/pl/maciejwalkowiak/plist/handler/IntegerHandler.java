package pl.maciejwalkowiak.plist.handler;

import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;

public class IntegerHandler extends SimpleHandler {
	public boolean supports(Object object) {
		return object instanceof Integer
				|| object instanceof Long
				|| object instanceof Short;
	}

	@Override
	protected String getWrap() {
		return "integer";
	}
	
	public NSObject objectify(Object object) {
		if (object instanceof Long) {
			return new NSNumber(((Long)object).intValue());
		} else if (object instanceof Short) {
			return new NSNumber(((Short)object).intValue());
		}
		
		return new NSNumber(((Integer)object).intValue());
	}
}
