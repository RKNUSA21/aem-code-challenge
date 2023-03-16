package com.anf.core.models;

import static org.mockito.Mockito.when;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/***Begin Code - Rajesh Koti***/

class NewsFeedSlingModelTest {

	@Mock
	private ResourceResolver resourceResolver;

	@Mock
	private Resource resource;

	@Mock
	private Node node;

	@Mock
	private NodeIterator it;

	@Mock
	private Property property;

	@InjectMocks
	private NewsFeedSlingModel newsfeed;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void test() throws RepositoryException {

		when(resourceResolver.getResource("/var/commerce/products/anf-code-challenge/newsData")).thenReturn(resource);
		when(resource.adaptTo(Node.class)).thenReturn(node);
		when(node.getNodes()).thenReturn(it);
		when(it.hasNext()).thenReturn(true).thenReturn(false);
		when(it.nextNode()).thenReturn(node);
		when(node.getProperty(Mockito.anyString())).thenReturn(property);
		when(property.getString()).thenReturn("Author");

		newsfeed.init();
	}

}

/***End Code - Rajesh Koti***/

