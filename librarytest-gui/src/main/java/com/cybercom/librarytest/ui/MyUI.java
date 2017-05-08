package com.cybercom.librarytest.ui;

import javax.servlet.annotation.WebServlet;

import com.cybercom.librarytest.ui.layouts.BrowseAuthorsLayout;
import com.cybercom.librarytest.ui.layouts.BrowseBooksLayout;
import com.cybercom.librarytest.ui.layouts.CreateAuthorLayout;
import com.cybercom.librarytest.ui.layouts.CreateBookLayout;
import com.cybercom.librarytest.ui.layouts.CreateUserLayout;
import com.cybercom.librarytest.ui.layouts.EditAuthorLayout;
import com.cybercom.librarytest.ui.layouts.EditBookLayout;
import com.cybercom.librarytest.ui.layouts.EditUserLayout;
import com.cybercom.librarytest.ui.layouts.HelpLayout;
import com.cybercom.librarytest.ui.layouts.LeftMenuLayout;
import com.cybercom.librarytest.ui.layouts.LoggedOutLayout;
import com.cybercom.librarytest.ui.layouts.LoginLayout;
import com.cybercom.librarytest.ui.layouts.LogoutLayout;
import com.cybercom.librarytest.ui.layouts.SingleAuthorLayout;
import com.cybercom.librarytest.ui.layouts.SingleBookLayout;
import com.cybercom.librarytest.ui.layouts.StartPageLayout;
import com.cybercom.librarytest.ui.layouts.TopBannerLayout;
import com.cybercom.librarytest.ui.layouts.UserProfileLayout;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("my.vaadin.app.MyAppWidgetset")
public class MyUI extends UI {

	private static final long serialVersionUID = 1L;
	public static String HOME_URI;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
		
		HOME_URI = vaadinRequest.getContextPath();
		
		// Set the general order of the layouts.
		// The left-hand side menu and the main content share a horizontal
		// layout below the top banner.
		VerticalLayout leftMenuLayout = new VerticalLayout();
		VerticalLayout mainContentLayout = new VerticalLayout();
		HorizontalLayout leftMenuAndMainContentLayout = new HorizontalLayout();
		leftMenuAndMainContentLayout
				.addComponents(leftMenuLayout, mainContentLayout);
		HorizontalLayout topBannerLayout = new HorizontalLayout();
		VerticalLayout fullPageLayout = new VerticalLayout();
		fullPageLayout
				.addComponents(topBannerLayout, leftMenuAndMainContentLayout);
		VerticalLayout fullScreenLayout = new VerticalLayout();
		Panel fullPageContainer = new Panel();
		fullPageContainer.setContent(fullPageLayout);
		fullScreenLayout.addComponent(fullPageContainer);

		// Set the ids of the layouts.
		fullScreenLayout.setId("full-screen");
		fullPageContainer.setId("full-page");
		leftMenuLayout.setId("left-menu");
		mainContentLayout.setId("main-content");
		leftMenuAndMainContentLayout.setId("left-menu-and-main-content");
		topBannerLayout.setId("top-banner");
		
		// Set the sizes of the layouts.
		leftMenuLayout.setWidth("200px");
		leftMenuLayout.setHeight("100%");
		topBannerLayout.setWidth("100%");
		topBannerLayout.setHeight("150px");
		leftMenuAndMainContentLayout.setExpandRatio(mainContentLayout, 1);
		leftMenuAndMainContentLayout.setWidth("100%");
		leftMenuAndMainContentLayout.setHeightUndefined();
		//fullPageLayout.setExpandRatio(leftMenuAndMainContentLayout, 1);
		fullPageLayout.setHeightUndefined();
		fullScreenLayout.setComponentAlignment(fullPageContainer, Alignment.TOP_CENTER);
		
		// Add the content of the layouts.
		topBannerLayout.addComponent(new TopBannerLayout());
		mainContentLayout
				.addComponent(getMainContentForUrl(vaadinRequest.getPathInfo()));
		Component leftMenuContent = new LeftMenuLayout();
		leftMenuLayout.addComponent(leftMenuContent);
		setContent(fullScreenLayout);
		final Styles styles = Page.getCurrent().getStyles();
		styles.add(
				  "#full-page { max-width: 1000px; width: 100%; } "
				+ "#main-content { min-height: 720px; } "
				+ "#left-menu .v-panel, #top-banner .v-panel { background: #EFF1F9 !important; } "
				+ "#left-menu .v-slot .v-panel .v-link { "
				+     "font-weight: bold; "
				+     "font-size: large; "
				+     "font-variant: small-caps; "
				+ "} "
				+ "#top-banner #banner-link { "
				+     "font-weight: bold; "
				+     "font-size: x-large; "
				+     "font-family: Verdana, Geneva, sans-serif; "
				+     "text-decoration: none; "
				+ "} "
				+ "#main-content #main-content-header { "
				+     "font-weight: bold; "
				+     "font-size: large; "
				+ "} "
		);
    }
	
	/**
	 * Generates the main content based on the provided url. 
	 */
	private Component getMainContentForUrl(String url) {
		
		if (url.startsWith(HOME_URI)) {
			url = url.substring(HOME_URI.length());
		}
		if (url.equals(BrowseAuthorsLayout.BASE_URI)) {
			return new BrowseAuthorsLayout();
		} else if (url.equals(BrowseBooksLayout.BASE_URI)) {
			return new BrowseBooksLayout();
		} else if (url.startsWith(SingleBookLayout.BASE_URI)) {
			String[] segments = url.split("/");
			return new SingleBookLayout(Long.parseLong(
					segments.length == 3 ? segments[2] : "-1"
			)); 
		} else if (url.startsWith(SingleAuthorLayout.BASE_URI)) {
			String[] segments = url.split("/");
			return new SingleAuthorLayout(Long.parseLong(
					segments.length == 3 ? segments[2] : "-1"
			));
		} else if (url.equals(CreateBookLayout.BASE_URI)) {
			return new CreateBookLayout();
		} else if (url.equals(CreateAuthorLayout.BASE_URI)) {
			return new CreateAuthorLayout();
		} else if (url.equals(CreateUserLayout.BASE_URI)) {
			return new CreateUserLayout();
		} else if (url.startsWith(EditBookLayout.BASE_URI)) {
			String[] segments = url.split("/");
			return new EditBookLayout(Long.parseLong(
					segments.length == 4 ? segments[3] : "-1"
			));
		} else if (url.startsWith(EditAuthorLayout.BASE_URI)) {
			String[] segments = url.split("/");
			return new EditAuthorLayout(Long.parseLong(
					segments.length == 4 ? segments[3] : "-1"
			));
		} else if (url.startsWith(EditUserLayout.BASE_URI)) {
			String[] segments = url.split("/");
			return new EditUserLayout(Long.parseLong(
					segments.length == 4 ? segments[3] : "-1"
			));
		} else if (url.equals(LoginLayout.BASE_URI)) {
			return new LoginLayout();
		} else if (url.equals(LogoutLayout.BASE_URI)) {
			return new LogoutLayout();
		} else if (url.startsWith(LoggedOutLayout.BASE_URI)) {
			String[] segments = url.split("/");
			return new LoggedOutLayout(Long.parseLong(
					segments.length == 4 ? segments[3] : "-1"
			));
		} else if (url.equals(UserProfileLayout.BASE_URI)) {
			return new UserProfileLayout();
		} else if (url.equals(HelpLayout.BASE_URI)) {
			return new HelpLayout();
		}
		return new StartPageLayout();
	}

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

		private static final long serialVersionUID = 1L;
    }
    
    /**
     * Goes through the UIs of the current VaadinSession and returns the last one in the list.
     * @return
     */
    public static UI getCurrentUI() {
    	UI currentUi = null;
    	for (UI ui : VaadinSession.getCurrent().getUIs()) {
    		currentUi = ui;
    	}
    	return currentUi;
    }
}
