package guru.qa.niffler.page;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import lombok.Getter;

@Getter
public class BasePage {
  public static final Config CFG = Config.getInstance();
  protected final Header header;
  protected final Calendar calendar;
  protected final SearchField searchField;

  public BasePage() {
    this.header = new Header();
    this.calendar = new Calendar();
    this.searchField = new SearchField();
  }
}
