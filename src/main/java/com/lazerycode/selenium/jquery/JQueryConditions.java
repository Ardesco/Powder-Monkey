package com.lazerycode.selenium.jquery;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Map;

public class JQueryConditions {

    public static ExpectedCondition<Boolean> jQueryAJAXCallsHaveCompleted() {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };
    }

    public static ExpectedCondition<Boolean> listenerIsRegisteredOnElement(final String listenerType, final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                Map<String, Object> registeredListeners = (Map<String, Object>) ((JavascriptExecutor) driver).executeScript("return jQuery._data(jQuery(arguments[0]).get(0), 'events')", element);
                for (Map.Entry<String, Object> listener : registeredListeners.entrySet()) {
                    if (listener.getKey().equals(listenerType)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
