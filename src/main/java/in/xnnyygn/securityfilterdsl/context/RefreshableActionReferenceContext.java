package in.xnnyygn.securityfilterdsl.context;

import in.xnnyygn.securityfilterdsl.parser.ActionConfigParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refreshable action reference context.
 * 
 * @author xnnyygn
 */
public class RefreshableActionReferenceContext extends ForwardingActionReferenceContext implements
    Runnable {

  private static final Log log = LogFactory.getLog(RefreshableActionReferenceContext.class);
  private String actionConfigDigest;
  private final ScheduledExecutorService threadExecutor = Executors.newScheduledThreadPool(1);
  private ScheduledFuture<?> future;

  private final ActionConfigParser actionConfigParser;
  private final String actionConfigPath;
  private final long duration;

  /**
   * Constructor.
   * 
   * @param actionConfigParser parser
   * @param duration duration, in second
   * @param actionConfigPath action configuration path
   */
  public RefreshableActionReferenceContext(ActionConfigParser actionConfigParser, long duration,
      String actionConfigPath) {
    super();
    this.actionConfigParser = actionConfigParser;
    this.actionConfigPath = actionConfigPath;
    this.duration = duration;

    ensureLatestActionConfig();
    if (super.underlying == null) {
      throw new IllegalStateException("action config not prepard");
    }
  }

  public void start() {
    future = threadExecutor.scheduleAtFixedRate(this, duration, duration, TimeUnit.SECONDS);
  }

  public void stop() {
    future.cancel(false);
  }

  public void run() {
    ensureLatestActionConfig();
  }

  private void ensureLatestActionConfig() {
    // calculate file digest
    String digest = null;
    try {
      digest = DigestUtils.md5Hex(IOUtils.toByteArray(new FileInputStream(actionConfigPath)));
    } catch (IOException e) {
      // do not log exception since typically file not found
      log.warn("failed to calculate digest of file [" + actionConfigPath + "], cause is "
          + e.getMessage());
      return;
    }

    if (StringUtils.equals(digest, actionConfigDigest)) {
      // file not change
      return;
    }

    updateActionConfig(digest);
  }

  private void updateActionConfig(String digest) {
    if (log.isDebugEnabled()) {
      log.debug("set config digest " + digest);
    }
    actionConfigDigest = digest;
    parseActionConfig();
  }

  private void parseActionConfig() {
    if (log.isInfoEnabled()) {
      log.info("parse config [" + actionConfigPath + "]");
    }

    try {
      super.underlying = actionConfigParser.parse(new FileInputStream(actionConfigPath));
    } catch (IOException e) {
      log.warn("failed to parse action config", e);
    }

    if (log.isDebugEnabled()) {
      log.debug("current config " + super.underlying);
    }
  }

  @Override
  public String toString() {
    return "RefreshableActionReferenceContext [actionConfigPath=" + actionConfigPath
        + ", duration=" + duration + "]";
  }

}
