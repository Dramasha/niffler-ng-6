package guru.qa.niffler.page.component;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SetValueOptions;
import io.qameta.allure.Step;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Calendar extends BaseComponent<Calendar> {

    private final ElementsCollection
            chooseYear = $$(".MuiPickersYear-root");

    private final SelenideElement
            calendar = $(".MuiDateCalendar-root"),
            clickImgCalendar = $("img[alt='Calendar']"),
            arrowDropDownIcon = $(".MuiPickersCalendarHeader-label"),
            chooseMonth = $(".MuiPickersCalendarHeader-labelContainer"),
            clickLeft = $("svg[data-testid='ArrowLeftIcon']"),
            chooseDay = $(".MuiDayCalendar-monthContainer"),
            setDate = $("[name='date']");

    public Calendar() {
        super($(".MuiPickersLayout-root"));
    }

    @Step("Выбрать дату в календаре")
    public void selectDateInCalendar(String year, String month, String day) {
        clickImgCalendar.click();
        arrowDropDownIcon.click();
        chooseYear.find(text(year)).click();

        do {
            clickLeft.click();
        } while (chooseMonth.shouldHave(text("%s %s".formatted(month, year))).isDisplayed());

        chooseDay.shouldHave(text(day)).click();

    }
}
