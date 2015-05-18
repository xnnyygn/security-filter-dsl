package in.xnnyygn.securityfilterdsl;

import in.xnnyygn.securityfilterdsl.context.ActionReferenceContext;
import in.xnnyygn.securityfilterdsl.context.RefreshableActionReferenceContext;
import in.xnnyygn.securityfilterdsl.parser.ActionConfigParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * Test
 * 
 * @author xnnyygn
 */
public class RefreshableActionReferenceContextTest {

  @Test
  public void testGetRoot() throws InterruptedException {
    RefreshableActionReferenceContext context =
        new RefreshableActionReferenceContext(new ActionConfigParser(), 5,
            "src/test/resources/action-config/rule-pass.txt");
    List<Thread> threads = new ArrayList<Thread>();
    int n = 10;
    for (int i = 0; i < n; i++) {
      GetRootThread thread = new GetRootThread(context);
      threads.add(thread);
      thread.start();
    }
    for (Thread thread : threads) {
      thread.join();
    }
    // there must be only one time initialing in 5s
  }

  class GetRootThread extends Thread {

    private final ActionReferenceContext context;

    public GetRootThread(ActionReferenceContext context) {
      super();
      this.context = context;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(new Random().nextInt(1000));
      } catch (InterruptedException e) {
      }
      context.getRoot();
    }

  }

  @Test
  public void testDuplicatedInstance() throws InterruptedException {
    RefreshableActionReferenceContext context1 =
        new RefreshableActionReferenceContext(new ActionConfigParser(), 1,
            "src/test/resources/action-config/rule-pass.txt");
    context1.start();
    RefreshableActionReferenceContext context2 =
        new RefreshableActionReferenceContext(new ActionConfigParser(), 1,
            "src/test/resources/action-config/rule-pass.txt");
    context2.start();
    Thread.sleep(5000);
  }
}
