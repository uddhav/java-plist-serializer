package pl.maciejwalkowiak.plist.handler;

import com.dd.plist.NSObject;

public interface Handler {
	boolean supports(Object object);
	String handle(Object object);
	NSObject objectify(Object object);
}
