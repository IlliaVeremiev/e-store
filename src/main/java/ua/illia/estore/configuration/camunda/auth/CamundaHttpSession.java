package ua.illia.estore.configuration.camunda.auth;

import org.camunda.bpm.webapp.impl.security.auth.Authentications;
import org.camunda.bpm.webapp.impl.security.auth.UserAuthentication;
import org.camunda.bpm.webapp.impl.security.filter.util.CsrfConstants;
import org.camunda.bpm.webapp.impl.security.filter.util.HttpSessionMutexListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.illia.estore.configuration.security.EmployeeUserDetails;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.Serializable;
import java.util.Enumeration;

public class CamundaHttpSession implements HttpSession {

    public static final String CAM_AUTH_SESSION_KEY = "authenticatedUser";
    public static final String DEFAULT_PROCESS_ENGINE_NAME = "default";

    private final HttpSession session;
    private final HttpServletRequest request;

    private final SessionMutex mutex = new SessionMutex();

    public CamundaHttpSession(HttpSession session, HttpServletRequest request) {
        this.session = session;
        this.request = request;
    }

    @Override
    public long getCreationTime() {
        return session.getCreationTime();
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return session.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        session.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return session.getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        if (CAM_AUTH_SESSION_KEY.equals(name)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                EmployeeUserDetails details = (EmployeeUserDetails) authentication.getDetails();
                Authentications authentications = new Authentications();
                authentications.addOrReplace(new UserAuthentication(details.getUser().getEmail(), DEFAULT_PROCESS_ENGINE_NAME));
                return authentications;
            }
        }
        if (CsrfConstants.CSRF_TOKEN_SESSION_ATTR_NAME.equals(name)) {
            return request.getHeader(CsrfConstants.CSRF_TOKEN_HEADER_NAME);
        }
        if (HttpSessionMutexListener.AUTH_TIME_SESSION_MUTEX.equals(name)) {
            return mutex;
        }
        return session.getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        return session.getValue(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return session.getAttributeNames();
    }

    @Override
    public String[] getValueNames() {
        return session.getValueNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        session.setAttribute(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        session.putValue(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        session.removeAttribute(name);
    }

    @Override
    public void removeValue(String name) {
        session.removeValue(name);
    }

    @Override
    public void invalidate() {
        session.invalidate();
    }

    @Override
    public boolean isNew() {
        return session.isNew();
    }

    private static class SessionMutex implements Serializable {
    }
}
