package prv.irony.java_api.data.session;

import lombok.Data;

@Data
public class SessionCookie {
  private boolean httpOnly;
  private String path;
  private long expires;
  private long originalMaxAge;
}
