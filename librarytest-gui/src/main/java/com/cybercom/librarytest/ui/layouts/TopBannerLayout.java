package com.cybercom.librarytest.ui.layouts;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class TopBannerLayout extends VerticalLayout {
	private static final long serialVersionUID = -8019180230951386957L;
	
	public TopBannerLayout() {
		
		Panel panel = new Panel();
		Link bannerLink = new Link("Library Test Application", 
				new ExternalResource(HOME_URI));
		bannerLink.setId("banner-link");
		panel.setContent(bannerLink);
		setSizeFull();
		panel.setSizeFull();
		addComponent(panel);
	    //setMargin(true);
	    setSpacing(true);
	}
}
