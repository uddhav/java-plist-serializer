package pl.maciejwalkowiak.plist.handler;

import com.dd.plist.NSObject;

import pl.maciejwalkowiak.plist.PlistSerializerImpl;

/**
 * Used for handlers that need to use {@link pl.maciejwalkowiak.plist.PlistSerializerImpl} internally.
 * {@link CollectionHandler}, {@link MapHandler}
 */
public abstract class AbstractHandler implements Handler {
	protected PlistSerializerImpl plistSerializer;

	protected AbstractHandler(PlistSerializerImpl plistSerializer) {
		this.plistSerializer = plistSerializer;
	}

	public String handle(Object object) {
		if (plistSerializer == null) {
			throw new IllegalStateException("plist is not initialized");
		}

		return doHandle(object);
	}
	
	public NSObject objectify(Object object) {
		if (plistSerializer == null) {
			throw new IllegalStateException("plist is not initialized");
		}

		return doObjectify(object);
	}

	abstract String doHandle(Object object);
	abstract NSObject doObjectify(Object object);
}
