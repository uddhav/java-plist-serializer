package pl.maciejwalkowiak.plist.handler;

import pl.maciejwalkowiak.plist.PlistSerializerImpl;
import pl.maciejwalkowiak.plist.XMLHelper;

import java.util.Map;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;

public class MapHandler extends AbstractHandler {
	public MapHandler(PlistSerializerImpl plistSerializer) {
		super(plistSerializer);
	}

	@Override
	String doHandle(Object object) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) object;

		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			result.append(XMLHelper.wrap(plistSerializer.getNamingStrategy().fieldNameToKey(entry.getKey())).with("key"));
			result.append(plistSerializer.serialize(entry.getValue()));
		}

		return XMLHelper.wrap(result).with("dict");
	}
	
	@Override
	NSObject doObjectify(Object object) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)object;

		NSDictionary result = new NSDictionary();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = plistSerializer.getNamingStrategy().fieldNameToKey(entry.getKey());
			NSObject value = plistSerializer.objectify(entry.getValue());
			
			if (value != null) {
				result.put(key, value);
			}
		}

		return result;
	}

	public boolean supports(Object object) {
		return object instanceof Map;
	}
}
