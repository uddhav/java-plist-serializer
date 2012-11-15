package pl.maciejwalkowiak.plist.handler;

import com.dd.plist.NSObject;
import com.dd.plist.NSNumber;

public class BooleanHandler implements Handler {

	public String handle(Object object) {
		Boolean o = (Boolean) object;

		return "<" + String.valueOf(o) + "/>";
	}

	public boolean supports(Object object) {
		return object instanceof Boolean;
	}
	
	public NSObject objectify(Object object) {
		return new NSNumber((Boolean)object);
	}
}
