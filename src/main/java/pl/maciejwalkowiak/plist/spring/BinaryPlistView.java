package pl.maciejwalkowiak.plist.spring;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import pl.maciejwalkowiak.plist.PlistSerializerImpl;

import com.dd.plist.BinaryPropertyListWriter;
import com.dd.plist.NSObject;

/**
 * 
 * @author uddhav kambli
 *
 */
public class BinaryPlistView extends AbstractView {

	private PlistSerializerImpl plistSerializer;

	private String contentType = "application/x-plist";

	private boolean disableCaching = true;
	
	private Set<String> renderedAttributes;

	public BinaryPlistView() {
		setContentType(contentType);
		plistSerializer = new PlistSerializerImpl();
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		
		NSObject result = plistSerializer.objectify(filterModel(model));
		BinaryPropertyListWriter.write(out, result);
		
		out.flush();
		out.close();
	}

	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(getContentType());
		
		String pathInfo = request.getPathInfo();
		if (pathInfo == null) {
			pathInfo = "response.plist";
		} else {
			pathInfo.replace('/', '_');
		}
		
		response.setHeader("Content-Disposition","attachment; filename=\"" + pathInfo +"\"");
		
		if (this.disableCaching) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
	}

	protected Object filterModel(Map<String, Object> model) {
		int size = this.renderedAttributes.size();
		Map<String, Object> result = new HashMap<String, Object>(model.size());
		
		if (size != 1) {
			Set<String> renderedAttributes = !CollectionUtils.isEmpty(this.renderedAttributes) ? this.renderedAttributes : model.keySet();
			
			for (Map.Entry<String, Object> entry : model.entrySet()) {
				if (!(entry.getValue() instanceof BindingResult) && renderedAttributes.contains(entry.getKey())) {
					result.put(entry.getKey(), entry.getValue());
				}
			}
			
			return result;
			
		} else {
			String attribute = this.renderedAttributes.iterator().next();
			Object value = model.get(attribute);
			
			return value == null ? result : value;
		}
	}

	public void setDisableCaching(boolean disableCaching) {
		this.disableCaching = disableCaching;
	}

	public void setPlistSerializer(PlistSerializerImpl plistSerializer) {
		this.plistSerializer = plistSerializer;
	}

	/**
	 * Sets the attributes in the model that should be rendered by this view. When set, all other model attributes will be
	 * ignored.
	 */
	public void setRenderedAttributes(Set<String> renderedAttributes) {
		this.renderedAttributes = renderedAttributes;
	}

}
