package pl.maciejwalkowiak.plist;

import org.junit.Test;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;

import pl.maciejwalkowiak.plist.handler.Handler;
import pl.maciejwalkowiak.plist.strategy.UppercaseNamingStrategy;

import java.math.BigDecimal;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;

public class PlistSerializerImplTest {
	private PlistSerializerImpl plistSerializer = new PlistSerializerImpl();

	@Test
	public void testStringArraySerialization() {
		//given
		List<String> strings = Arrays.asList("string1", "string2", "string3");

		//when
		String xml = plistSerializer.serialize(strings);

		//then
		assertThat(xml).isEqualTo("<array><string>string1</string><string>string2</string><string>string3</string></array>");
		
		// when
		NSObject object = plistSerializer.objectify(strings);
		
		// then
		NSArray array = new NSArray(new NSObject[] { new NSString("string1"), new NSString("string2"), new NSString("string3") });
		assertThat(object.toXMLPropertyList()).isEqualTo(array.toXMLPropertyList());
	}

	@Test
	public void testIntegerListSerialization() {
		//given
		List<Integer> strings = Arrays.asList(4,6,8);

		//when
		String xml = plistSerializer.serialize(strings);

		//then
		assertThat(xml).isEqualTo("<array><integer>4</integer><integer>6</integer><integer>8</integer></array>");
		
		// when
		NSObject object = plistSerializer.objectify(strings);
		
		// then
		NSArray array = new NSArray(new NSObject[] { new NSNumber(4), new NSNumber(6), new NSNumber(8) });
		assertThat(object.toXMLPropertyList()).isEqualTo(array.toXMLPropertyList());
	}

	@Test
	public void testObjectSerialization() {
		//given
		Post post = new Post(new Author("jason bourne"), "java-plist-serializer introduction", 9);
		post.addComment(new Comment("maciejwalkowiak", "first comment"));
		post.addComment(new Comment("john doe", "second comment"));

		//when
		String xml = plistSerializer.serialize(post);

		//then
		assertThat(xml).isEqualTo("<dict><key>author</key><dict><key>name</key><string>jason bourne</string></dict>" +
				"<key>comments</key><array>" +
				"<dict><key>author</key><string>maciejwalkowiak</string><key>content</key><string>first comment</string></dict>" +
				"<dict><key>author</key><string>john doe</string><key>content</key><string>second comment</string></dict></array>" +
				"<key>title</key><string>" + post.getTitle() + "</string>" +
				"<key>views</key><integer>" + post.getViews() + "</integer>" +
				"</dict>");
		
		// when
		NSObject object = plistSerializer.objectify(post);
		
		// then
		NSDictionary postD = new NSDictionary();
		NSDictionary authorD = new NSDictionary();
		NSDictionary commentD1 = new NSDictionary();
		NSDictionary commentD2 = new NSDictionary();
		authorD.put("name", new NSString("jason bourne"));
		postD.put("author", authorD);
		NSArray commentA = new NSArray(2);
		commentD1.put("author", new NSString("maciejwalkowiak"));
		commentD1.put("content", new NSString("first comment"));
		commentD2.put("author", new NSString("john doe"));
		commentD2.put("content", new NSString("second comment"));
		commentA.setValue(0, commentD1);
		commentA.setValue(1, commentD2);
		postD.put("comments", commentA);
		postD.put("title", new NSString("java-plist-serializer introduction"));
		postD.put("views", new NSNumber(9));
		assertThat(object.toXMLPropertyList()).isEqualTo(postD.toXMLPropertyList());
	}

	@Test
	public void testNullSerialization() {
		assertThat(plistSerializer.serialize(null)).isEqualTo("");
		assertThat(plistSerializer.objectify(null)).isEqualTo(null);
	}

	@Test
	public void testStaticFieldSerialization() {
		//given
		ClassWithStaticFields classWithStaticFields = new ClassWithStaticFields();

		//when
		String result = plistSerializer.serialize(classWithStaticFields);

		//then
		assertThat(result).isEqualTo("<dict><key>serializableField</key><integer>1</integer></dict>");
		
		// when
		NSObject object = plistSerializer.objectify(classWithStaticFields);
		
		// then
		NSDictionary cD = new NSDictionary();
		cD.put("serializableField", new NSNumber(1));
		assertThat(object.toXMLPropertyList()).isEqualTo(cD.toXMLPropertyList());
	}

	@Test
	public void testAnnotations() {
		//given
		ClassWithAnnotations classWithAnnotations = new ClassWithAnnotations();

		//when
		String xml = plistSerializer.serialize(classWithAnnotations);

		//then
		assertThat(xml).isEqualTo("<dict><key>trick</key><string>i am renamed</string></dict>");
		
		// when
		NSObject object = plistSerializer.objectify(classWithAnnotations);
		
		// then
		NSDictionary cD = new NSDictionary();
		cD.put("trick", new NSString("i am renamed"));
		assertThat(object.toXMLPropertyList()).isEqualTo(cD.toXMLPropertyList());
	}

	@Test
	public void testPlistRenameWithFollowingStrategy() {
		//given
		PlistSerializerImpl plistSerializer = new PlistSerializerImpl(new UppercaseNamingStrategy());
		AnnotatedClass annotatedClass = new AnnotatedClass();

		//when
		String xml = plistSerializer.serialize(annotatedClass);

		//then
		assertThat(xml).isEqualTo("<dict><key>FIRST_FIELD</key><string>i am following</string><key>secondField</key><string>i am not following</string></dict>");
		
		// when
		NSObject object = plistSerializer.objectify(annotatedClass);
		
		// then
		NSDictionary cD = new NSDictionary();
		cD.put("FIRST_FIELD", new NSString("i am following"));
		cD.put("secondField", new NSString("i am not following"));
		assertThat(object.toXMLPropertyList()).isEqualTo(cD.toXMLPropertyList());
	}

	@Test
	public void testFieldSerializationWhenNull() {
		//given
		Comment comment = new Comment(null, "content");

		//when
		String xml = plistSerializer.serialize(comment);

		//then
		assertThat(xml).isEqualTo("<dict><key>content</key><string>content</string></dict>");
		
		// when
		NSObject object = plistSerializer.objectify(comment);
		
		// then
		NSDictionary cD = new NSDictionary();
		cD.put("content", new NSString("content"));
		assertThat(object.toXMLPropertyList()).isEqualTo(cD.toXMLPropertyList());
	}

	@Test
	public void testToXML() {
		//given
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\"><plist version=\"1.0\">";
		String footer = "</plist>";

		String testObject= "testObject";

		//when
		String xml = plistSerializer.toXmlPlist(testObject);

		//then
		assertThat(xml).startsWith(header).endsWith(footer);
	}

	@Test
	public void testAdditionalHandler() {
		//given
		Handler uuidHandler = new UUIDHandler();
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();

		//when
		plistSerializer.setAdditionalHandlers(Arrays.asList(uuidHandler));
		String xml = plistSerializer.serialize(uuid);

		//then
		assertThat(xml).isEqualTo("<string>" + uuidString + "</string>");
		
		// when
		NSObject object = plistSerializer.objectify(uuid);
		
		// then
		NSString string = new NSString(uuidString);
		assertThat(object.toXMLPropertyList()).isEqualTo(string.toXMLPropertyList());
	}

	@Test
	public void testDoubleSerializationHandler() {
		//given
		Double object = 4.55d;

		//when
		String xml = plistSerializer.serialize(object);

		//then
		assertThat(xml).isEqualTo("<real>4.55</real>");
		
		// when
		NSObject nsObject = plistSerializer.objectify(object);
		
		// then
		NSNumber number = new NSNumber(object.doubleValue());
		assertThat(nsObject).isEqualTo(number);
	}

	@Test
	public void testSupportedDataTypes() throws IllegalAccessException, InstantiationException {
		//given
		List supportedDataTypes = Arrays.asList(
				Integer.valueOf(1),
				Double.valueOf(1d),
				Float.valueOf(1f),
				Short.valueOf("1"),
				Long.valueOf(1l),
				Double.valueOf(1d),
				new Boolean(true),
				new Date(),
				new HashSet<String>(),
				new HashMap<String, String>());

		for (Object o: supportedDataTypes) {
			//when
			boolean isSupported = plistSerializer.getHandlerWrapper().isSupported(o);

			//then
			assertThat(isSupported).isTrue();
		}
	}
}
