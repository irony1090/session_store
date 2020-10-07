package prv.irony.java_api.data.session;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.springframework.session.Session;

public class MySession implements Session{
	private String id;
	// private String beforeModifidId;
	private Instant creation;
	private Instant lastAccessedTime;
	private Duration interval;
	private HashMap<String, Object> attrs;

	public MySession(String id){
		this.id = id;
		this.attrs = new HashMap<String, Object>();
		this.creation = Instant.now();
	}

	@Override
	public String getId() {
		System.out.println("getId");
		return id;
	}

	@Override
	public String changeSessionId() {
		System.out.println("change");
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAttribute(String attributeName) {
		System.out.println("getAttribute : " + attributeName);
		return this.attrs.get(attributeName);
	}

	@Override
	public Set<String> getAttributeNames() {
		System.out.println("getAttributeNames");
		return attrs.keySet();
	}

	@Override
	public void setAttribute(String attributeName, Object attributeValue) {
		System.out.println("setAttributeNames");
		this.attrs.put(attributeName, attributeValue);
		System.out.println(this.attrs);
	}

	@Override
	public void removeAttribute(String attributeName) {
		System.out.println("removeAttributeNames");
		this.attrs.remove(attributeName);
	}

	@Override
	public Instant getCreationTime() {
		System.out.println("getCreationTime");
		return creation;
	}

	@Override
	public void setLastAccessedTime(Instant lastAccessedTime) {
		long cur = lastAccessedTime.toEpochMilli();
		System.out.println("setLastAccessedTime : " + cur);
		System.out.println("setLastAccessedTime : " + new Date(cur) );
		this.lastAccessedTime = lastAccessedTime;
	}

	@Override
	public Instant getLastAccessedTime() {
		System.out.println("getLastAccessedTime");
		return this.lastAccessedTime;
	}

	@Override
	public void setMaxInactiveInterval(Duration interval) {
		System.out.println("setMaxInactiveInterval : " + interval.toMillis());
		this.interval = interval;
		System.out.println("after setMaxInactiveInterval : " + this.isExpired() + " : " + interval.toMillis());

	}

	@Override
	public Duration getMaxInactiveInterval() {
		System.out.println("getMaxInactiveInterval");
		return interval;
	}

	@Override
	public boolean isExpired() {
		System.out.println("isExpired");
		return (this.lastAccessedTime.toEpochMilli() + this.interval.toMillis()) < System.currentTimeMillis() ? true : false;
	}
  
}
