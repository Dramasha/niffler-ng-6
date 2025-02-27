package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class Calendar extends BaseComponent<Calendar> {

    private final ElementsCollection
            chooseYear = $$(".MuiPickersYear-root");

    private final SelenideElement
            calendar = $(".MuiDateCalendar-root"),
            clickImgCalendar = $("img[alt='Calendar']"),
            arrowDropDownIcon = $(".MuiPickersCalendarHeader-label"),
            chooseMonth = $(".MuiPickersCalendarHeader-labelContainer"),
            clickRight = $("svg[data-testid='ArrowRightIcon']"),
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

        boolean isDecember = false;

        do {
            clickRight.click();
            if (chooseMonth.getText().equals("December %s".formatted(year))) {
                isDecember = true;
                break;
            }
        } while (!chooseMonth.getText().equals("%s %s".formatted(month, year)));

        if (isDecember) {
            do {
                clickLeft.click();
            } while (!chooseMonth.getText().equals("%s %s".formatted(month, year)) || !chooseMonth.getText().equals("January %s".formatted(year)));
        }
        chooseDay.$(byText(day)).click();
    }
}
