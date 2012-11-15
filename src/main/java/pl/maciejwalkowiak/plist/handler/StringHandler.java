package pl.maciejwalkowiak.plist.handler;

import com.dd.plist.NSObject;
import com.dd.plist.NSString;

public class StringHandler extends SimpleHandler {
	public boolean supports(Object object) {
		return object instanceof String;
	}

	@Override
	protected String getWrap() {
		return "string";
	}
	
	public NSObject objectify(Object object) {
		return new NSString((String)object);
	}
}
