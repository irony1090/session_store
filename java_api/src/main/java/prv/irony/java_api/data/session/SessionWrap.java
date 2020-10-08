package prv.irony.java_api.data.session;

import java.util.HashMap;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SessionWrap {
  private String id;
  private SessionCookie cookie;
  private HashMap<String, Object> session;
  private long lastAccess;

}
