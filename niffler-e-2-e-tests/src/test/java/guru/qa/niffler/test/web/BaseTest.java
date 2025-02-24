package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import lombok.Getter;

@Getter
public class BaseTest  {
  public static final Config CFG = Config.getInstance();
  protected final Header header;
  protected final Calendar calendar;
  protected final SearchField searchField;

  public BaseTest() {
    this.header = new Header();
    this.calendar = new Calendar();
    this.searchField = new SearchField();
  }
}
