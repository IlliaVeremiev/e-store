package ua.illia.estore.configuration.camunda.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class CamundaServletRequestWrapper extends HttpServletRequestWrapper {
    public CamundaServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public HttpSession getSession(boolean create) {
        return new CamundaHttpSession(((HttpServletRequest) getRequest()).getSession(create), (HttpServletRequest) getRequest());
    }

    @Override
    public HttpSession getSession() {
        return new CamundaHttpSession(((HttpServletRequest) getRequest()).getSession(), (HttpServletRequest) getRequest());
    }
}
