package pl.maciejwalkowiak.plist;

import java.util.UUID;

import pl.maciejwalkowiak.plist.handler.Handler;

import com.dd.plist.NSObject;
import com.dd.plist.NSString;

public class UUIDHandler implements Handler {
	public boolean supports(Object object) {
		return object instanceof UUID;
	}

	public String handle(Object object) {
		UUID uuid = (UUID)object;

		return XMLHelper.wrap(uuid.toString()).with("string");
	}

	public NSObject objectify(Object object) {
		return new NSString(((UUID)object).toString());
	}
}
