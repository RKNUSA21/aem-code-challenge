package com.anf.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.dam.api.Asset;

/***Begin Code - Rajesh Koti***/


@Component(service = { Servlet.class })
@SlingServletPaths(value = "/bin/countyDropdown")
public class CountyDatasourceServlet extends SlingSafeMethodsServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountyDatasourceServlet.class);

	transient ResourceResolver resourceResolver;
	transient Resource pathResource;
	transient ValueMap valueMap;
	transient List<Resource> resourceList;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		resourceResolver = request.getResourceResolver();
		pathResource = request.getResource();
		resourceList = new ArrayList<>();

		try {
			/* Getting AEM Tags Path given on datasource Node */
			String jsonDataPath = Objects.requireNonNull(pathResource.getChild("datasource")).getValueMap().get("jsonDataPath", String.class);
			assert jsonDataPath != null;
			// Getting Tag Resource using JsonData
			Resource jsonResource = request.getResourceResolver().getResource(jsonDataPath);
			assert jsonResource != null;
			//Getting Asset from jsonResource
			Asset jsonAsset = jsonResource.adaptTo(Asset.class);
			assert jsonAsset != null;
			//Converting Asset into input stream for JSON Object
			InputStream inputStream = jsonAsset.getOriginal().getStream();

			StringBuilder stringBuilder = new StringBuilder();
			String eachLine;
			assert inputStream != null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

			while ((eachLine = bufferedReader.readLine()) != null) {
				stringBuilder.append(eachLine);
			}

			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
			Iterator<String> jsonKeys = jsonObject.keys();
			// Iterating JSON Objects over key
			while (jsonKeys.hasNext()) {
				String jsonKey = jsonKeys.next();
				String jsonValue = jsonObject.getString(jsonKey);

				valueMap = new ValueMapDecorator(new HashMap<>());
				valueMap.put("value", jsonValue);
				valueMap.put("text", jsonKey);
				resourceList.add(new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", valueMap));
			}

			/* Create a DataSource that is used to populate the drop-down control */
			DataSource dataSource = new SimpleDataSource(resourceList.iterator());
			request.setAttribute(DataSource.class.getName(), dataSource);

		} catch (JSONException | IOException e) {
			LOGGER.error("Error in Json Data Exporting : {}", e.getMessage());
		}
	}
}
/***End Code - Rajesh Koti***/

