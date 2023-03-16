package com.anf.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***Begin Code - Rajesh Koti***/

@Component(service = EventListener.class, immediate = true)
public class CustomEventListner implements EventListener {

	private static final Logger log = LoggerFactory.getLogger(CustomEventListner.class);

	/**
	 * Resource Resolver Factory
	 */
	@Reference
	private ResourceResolverFactory resolverFactory;

	/**
	 * Resource Resolver
	 */
	private ResourceResolver resolver;

	@Reference
	private SlingRepository repository;

	/**
	 * Session object
	 */
	private Session session;

	/**
	 * Activate method to initialize stuff
	 * 
	 * @throws LoginException
	 * @throws RepositoryException
	 * @throws UnsupportedRepositoryOperationException
	 */
	@Activate
	protected void activate(ComponentContext componentContext) throws LoginException, UnsupportedRepositoryOperationException, RepositoryException {

		// private static final Logger log =
		// LoggerFactory.getLogger(CustomEventListner.class);

		log.info("Activating the observation");

		Map<String, Object> params = new HashMap<>();
		params.put(ResourceResolverFactory.SUBSERVICE, "eventingService");

		/**
		 * Getting resource resolver from the service factory
		 */
		resolver = resolverFactory.getServiceResourceResolver(params);

		session.getWorkspace().getObservationManager().addEventListener(this, Event.NODE_ADDED, "/content/anf-code-challenge/us/en", true, null, null, false);
	}

	@Override
	public void onEvent(EventIterator events) {
		// TODO Auto-generated method stub

		try {

			while (events.hasNext()) {

				log.info("Something has been added: {} ", events.nextEvent().getPath());
				Session jcrSession = resolver.adaptTo(Session.class);

				Node resource = resolver.getResource(events.nextEvent().getPath()).adaptTo(Node.class);
				resource.setProperty("pageCreated", (Boolean) true);
				jcrSession.save();
			}
		} catch (Exception e) {

			log.error("Exception occurred", e);
		}

	}

	@Deactivate
	protected void deactivate() {

		if (session != null) {

			session.logout();
		}
	}

}
/***End Code - Rajesh Koti***/
