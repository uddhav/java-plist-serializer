## Additions
Apple recommends using the binary plist format for sending data to the iOS apps from the server. I needed a Java / Spring framework solution ASAP. Maciej Walkowiak had a text plist solution and Daniel Dreibrodt of http://code.google.com/p/plist/ had a binary plist solution. This is a quick mashup of both. It works for me. Feel free to use or improve. Thanks!

The BinaryPlistView can be used in the ContentNegotiatingViewResolverBean to quickly support binary plist output. The original PlistView takes care of the text plist output.

In your Spring config xmls... renderedAttributes set is used for filtering the model
```
<!-- Rendered Attributes -->
<util:set id="renderedAttributes">
	<value>response</value> <!-- eg. only syndicate the response attribute of the model -->
</util:set>

<!-- Content Negotiation -->
<bean class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver" p:order="0">
  	<property name="mediaTypes">
      	<map>
      		.
      		.
      		.
          	<entry key="plist" value="application/x-plist" />
      	</map>
  	</property>

	.
	.
	.
  	
  	<property name="defaultViews">
      	<list>
      		.
      		.
      		.
          	<!-- Binary plist -->
          	<bean class="pl.maciejwalkowiak.plist.spring.BinaryPlistView" p:contentType="application/x-plist" p:renderedAttributes-ref="renderedAttributes" />
        </list>
  	</property>
</bean>  
```
## Original README
## Java Objects to Property List Serializer

java-plist-serializer is a Java library that can be used to convert Java Objects into their Property List representation.

Usage is very simple:


    Comment comment = new Comment("maciej walkowiak", "hello plist");
    PlistSerializer serializer = new PlistSerializerImpl();

    String xml = serializer.serialize(comment); // creates xml just for serialized object
    String validXml = serializer.toXML(comment); // creates valid xml with header, dtd include

### Supported data types

Out of the box serializer supports fields with types:

* Boolean, Short, Integer, Long, Float, Double and their primitive equivalents
* String
* java.util.Date
* java.util.Collection
* java.util.Map

When object passed to serialization is not of one fields included in that list - serializer iterates through its fields using reflection, serializes each field and wraps result into <dict />.

#### Adding custom serializer

If there is a need to add serializer for another data type it can be done by implementing <code>pl.maciejwalkowiak.plist.handler.Handler</code> interface and then added to PlistSerializer by calling
<code>pl.maciejwalkowiak.plist.PlistSerializerImpl#setAdditionalHandlers</code>.

An example how to implement and add custom data type serializer can be found in test method: <code>PlistSerializerImplTest#testAdditionalHandler</code>

### Customization

There are few ways to customize XML output:

* object fields can be ignored by annotating them with <code>@PlistIgnore</code>
* object field by default is serialized to <code><key /></code> with field's name content. To provide custom key name use <code>@PlistAlias</code> annotation
* you can choose or define your own key naming strategy. Currently keys can be created with exactly the same name as field by using <code>DefaultNamingStrategy</code>
or fields can be changed to uppercase using <code>UppercaseNamingStrategy</code>. You can implement your own naming policy by implementing <code>pl.maciejwalkowiak.plist.NamingStrategy</code> and registering it by <code>PlistSerializerImpl</code> constructor argument

### Example

We have classes:

```
public class Post {
	private String title;
	private Integer views = 0;
	private List<Comment> comments = new ArrayList<Comment>();
	private Author author;

	public Post(Author author, String title, Integer views) {
		this.title = title;
		this.views = views;
		this.author = author;
	}
}

public class Comment {
	private String content;
	private String author;

	public Comment(String author, String content) {
		this.content = content;
		this.author = author;
	}
}

public class Author {
	private String name;
}
```

And execute code:

```
Post post = new Post(new Author("jason bourne"), "java-plist-serializer introduction", 9);
post.addComment(new Comment("maciejwalkowiak", "first comment"));
post.addComment(new Comment("john doe", "second comment"));

String xml = plistSerializer.toXmlPlist(post);
```

<code>xml</code> will contain unformatted version of:

```
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
	<dict>
		<key>author</key>
		<dict>
			<key>name</key>
			<string>jason bourne</string>
		</dict>
		<key>comments</key>
		<array>
			<dict>
				<key>author</key>
				<string>maciejwalkowiak</string>
				<key>content</key>
				<string>first comment</string>
			</dict>
			<dict>
				<key>author</key>
				<string>john doe</string>
				<key>content</key>
				<string>second comment</string>
			</dict>
		</array>
		<key>title</key>
		<string>java-plist-serializer introduction</string>
		<key>views</key>
		<integer>9</integer>
	</dict>
</plist>
```

### Spring Integration

It is possible to use PlistSerializer together with Spring MVC. To return property lists in a response of calling some Spring Controller you can use <code>pl.maciejwalkowiak.plist.spring.PlistView</code>.

There are several ways to configure Spring MVC. The easiest to understand example of usage of <code>PlistView</code>:

```
@Controller
public class BlogController {
	@RequestMapping(value = "/loadBlogPost", method = RequestMethod.GET)
	public ModelAndView loadBlogPost() {
		Post post = new Post(new Author("jason bourne"), "java-plist-serializer introduction", 9);
		post.addComment(new Comment("maciejwalkowiak", "first comment"));
		post.addComment(new Comment("john doe", "second comment"));

		ModelMap model = new ModelMap();
		model.addAttribute("RESULT", notification);

		return new ModelAndView(new PlistView(), model);
	}
}
```

### Known issues

If you have cycle dependency in serialized object tree you will get StackOverFlowError unless you ignore reference by annotating with <code>@PlistIgnore</code>

