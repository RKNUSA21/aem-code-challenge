package com.anf.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

/***Begin Code - Rajesh Koti***/

@Component(service = { Servlet.class })
@SlingServletPaths(
        value = "/bin/QueryBuilderDetails"
)
public class QueryBuilderServlet extends SlingSafeMethodsServlet{
	
	private static final long serialVersionUID = 1L;

  private static final String PATH="/content/anf-code-challenge/us/en";
  
  private static final String PROPERTY_SEARCH="anfCodeChallenge";
  
  private static final String PROPERTY_SEARCH_VALUE="true";
  
  private static final String PROPERTY_SEARCH_OPERATION="exists";
    
    /**
	 * Injecting the QueryBuilder dependency
	 */
	@Reference
	private QueryBuilder builder;
	
	/**
	 * Session object
	 */
	private Session session;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException, IOException {
        // Make use of ContentService to write the business logic
    	final Logger log = LoggerFactory.getLogger(QueryBuilderServlet.class);
    	try {
    		ResourceResolver resourceResolver = request.getResourceResolver();
    		session = resourceResolver.adaptTo(Session.class);
			
			/**
			 * Map for the predicates
			 */
			Map<String, String> predicate = new HashMap<>();

			/**
			 * Configuring the Map for the predicate
			 */
			predicate.put("path", PATH);
			predicate.put("1_property", PROPERTY_SEARCH);
			predicate.put("1_property.value", PROPERTY_SEARCH_VALUE);
			predicate.put("1_property.operation", PROPERTY_SEARCH_OPERATION);
			
			/**
			 * Creating the Query instance
			 */
			Query query = builder.createQuery(PredicateGroup.create(predicate), session);
			
			query.setStart(0);
			query.setHitsPerPage(10);
			
			/**
			 * Getting the search results
			 */
			SearchResult searchResult = query.getResult();
			searchResult.getHits().forEach(result-> {
				try {
					response.getWriter().println(result.getPath());
				} catch (IOException | RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
    	
    }catch (Exception e) {

		log.error(e.getMessage(), e);
	} finally {
		
		if(session != null) {
			
			session.logout();
		}
	}

}
}
/***End Code - Rajesh Koti***/
