package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Calendar {
    private final SelenideElement calendar = $(".MuiDateCalendar-root");

    @Step("Выбрать дату в календаре")
    public Calendar selectDateInCalendar(String date) {
        String[] dateParts = date.split("\\.");
        String day = dateParts[0];
        String month = dateParts[1];
        String year = dateParts[2];
        $(".MuiInputBase-root [type='button']").click();

        calendar.$("[data-testid='ArrowDropDownIcon']").click();
        calendar.$$(".MuiPickersYear-yearButton").find(text(year)).click();

        while (!isCorrectMonth(month)) {
            calendar.$("[title='Next month']").click();
        }

        calendar.$$("[role='gridcell']").find(text(day)).click();
        return this;
    }

    private boolean isCorrectMonth(String month) {
        String displayedMonth = calendar.$(".MuiPickersFadeTransitionGroup-root").getText();
        return displayedMonth.contains(month);
    }
}
