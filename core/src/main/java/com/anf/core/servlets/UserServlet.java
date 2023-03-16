/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anf.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.services.ContentService;

@Component(service = { Servlet.class })
@SlingServletPaths(
        value = "/bin/saveUserDetails"
)
public class UserServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    
	private static final Logger LOGGER = LoggerFactory.getLogger(CountyDatasourceServlet.class);


    @Reference
    private ContentService contentService;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
			final SlingHttpServletResponse resp) throws ServletException, IOException {
    	
    	/***Begin Code - Rajesh Koti***/
 	
		ResourceResolver resourceResolver = req.getResourceResolver();
		Session session = resourceResolver.adaptTo(Session.class);

		try {
			Node sourceNode = session.getNode("/etc/age");
			Property minAge = sourceNode.getProperty("minAge");
			Property maxAge = sourceNode.getProperty("maxAge");

			String minAgeStr = minAge.getString();
			String maxAgeStr = maxAge.getString();
			int age = Integer.parseInt(req.getParameter("age"));

			if (age > Integer.parseInt(minAgeStr) && age < Integer.parseInt(maxAgeStr)) {

				String firstName = req.getParameter("firstName");
				String lastName = req.getParameter("lastName");
				String country = req.getParameter("country");

				contentService.commitUserDetails(firstName, lastName, age, country, session);
			}else {
				resp.setStatus(500);
			}
		} catch (Exception e) {

			LOGGER.error("Exception Occured");
		}
		/***End Code - Rajesh Koti***/

	}
  
}

