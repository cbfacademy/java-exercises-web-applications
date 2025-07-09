package com.cbfacademy;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HTML Structure Tests")
public class HtmlTest {

    private org.jsoup.nodes.Document document;

    @BeforeEach
    void setUp() throws IOException {
        // Load the HTML file from src/main/resources
        File htmlFile = new File("src/main/resources/login.html");
        document = org.jsoup.Jsoup.parse(htmlFile, StandardCharsets.UTF_8.name());
    }

    @Test
    @DisplayName("Should contain exactly one form element")
    void shouldContainExactlyOneForm() {
        org.jsoup.select.Elements forms = document.select("form");
        assertEquals(1, forms.size(), "Page should contain exactly one form element");
    }

    @Test
    @DisplayName("Should contain exactly three input elements with expected ids")
    void shouldContainThreeInputsWithExpectedIds() {
        org.jsoup.select.Elements inputs = document.select("input");
        assertEquals(3, inputs.size(), "Form should contain exactly three input elements");

        // Verify specific input ids exist
        assertNotNull(document.getElementById("email"), "Should have input with id 'email'");
        assertNotNull(document.getElementById("password"), "Should have input with id 'password'");
        assertNotNull(document.getElementById("remember"), "Should have input with id 'remember'");

        // Verify input types
        org.jsoup.nodes.Element emailInput = document.getElementById("email");
        org.jsoup.nodes.Element passwordInput = document.getElementById("password");
        org.jsoup.nodes.Element rememberInput = document.getElementById("remember");

        assertEquals("email", emailInput.attr("type"), "Email input should have type 'email'");
        assertEquals("password", passwordInput.attr("type"), "Password input should have type 'password'");
        assertEquals("checkbox", rememberInput.attr("type"), "Remember input should have type 'checkbox'");
    }

    @Test
    @DisplayName("Should contain a link with 'forgot password' text (case-insensitive)")
    void shouldContainForgotPasswordLink() {
        org.jsoup.select.Elements links = document.select("a");
        boolean forgotPasswordLinkFound = links.stream()
                .anyMatch(link -> link.text().toLowerCase().contains("forgot password"));

        assertTrue(forgotPasswordLinkFound,
                "Should contain a link with 'forgot password' text (case-insensitive)");
    }

    @Test
    @DisplayName("Should contain exactly one submit button")
    void shouldContainSubmitButton() {
        org.jsoup.select.Elements submitButtons = document.select("button[type=submit], input[type=submit]");
        assertEquals(1, submitButtons.size(), "Should contain exactly one submit button");

        org.jsoup.nodes.Element submitButton = submitButtons.first();
        assertEquals("submit", submitButton.attr("type"), "Button should have type 'submit'");
    }

    @Test
    @DisplayName("All labels should be correctly associated with inputs")
    void shouldHaveCorrectLabelAssociations() {
        // Test email label association
        org.jsoup.nodes.Element emailLabel = document.select("label[for=email]").first();
        assertNotNull(emailLabel, "Should have a label with for='email'");
        assertEquals("email", emailLabel.attr("for"), "Email label should reference email input");

        // Test password label association
        org.jsoup.nodes.Element passwordLabel = document.select("label[for=password]").first();
        assertNotNull(passwordLabel, "Should have a label with for='password'");
        assertEquals("password", passwordLabel.attr("for"), "Password label should reference password input");

        // Test checkbox label association (checkbox should be inside the label)
        org.jsoup.nodes.Element rememberLabel = document.select("label[for=remember]").first();
        assertNotNull(rememberLabel, "Should have a label with for='remember'");
        assertEquals("remember", rememberLabel.attr("for"), "Remember label should reference remember input");

        // Verify checkbox is inside its label (internal labeling)
        org.jsoup.nodes.Element checkboxInLabel = rememberLabel.select("input[type=checkbox]").first();
        assertNotNull(checkboxInLabel, "Checkbox should be contained within its label element");
        assertEquals("remember", checkboxInLabel.attr("id"), "Checkbox inside label should have id 'remember'");
    }

    @Test
    @DisplayName("Should have proper form structure with three div containers")
    void shouldHaveProperFormStructure() {
        org.jsoup.nodes.Element form = document.select("form").first();
        org.jsoup.select.Elements formDivs = form != null ? form.select("> div") : new org.jsoup.select.Elements();
        assertEquals(3, formDivs.size(), "Form should contain exactly three direct div children");

        // Verify each div contains the expected elements
        org.jsoup.nodes.Element firstDiv = formDivs.get(0);
        assertTrue(firstDiv.select("input[type=email]").size() > 0,
                "First div should contain email input");

        org.jsoup.nodes.Element secondDiv = formDivs.get(1);
        assertTrue(secondDiv.select("input[type=password]").size() > 0,
                "Second div should contain password input");

        org.jsoup.nodes.Element thirdDiv = formDivs.get(2);
        assertTrue(thirdDiv.select("input[type=checkbox]").size() > 0,
                "Third div should contain checkbox input");
        assertTrue(thirdDiv.select("a").size() > 0,
                "Third div should contain the forgot password link");
    }

    @Test
    @DisplayName("Should have all required form attributes and accessibility features")
    void shouldHaveRequiredFormAttributes() {
        // Verify all inputs have name attributes
        org.jsoup.select.Elements inputs = document.select("input");
        for (org.jsoup.nodes.Element input : inputs) {
            assertFalse(input.attr("name").isEmpty(),
                    "Input with id '" + input.attr("id") + "' should have a name attribute");
        }

        // Verify email and password inputs have placeholder text
        org.jsoup.nodes.Element emailInput = document.getElementById("email");
        org.jsoup.nodes.Element passwordInput = document.getElementById("password");

        assertNotNull(emailInput, "Email input should be present");
        assertFalse(emailInput.attr("placeholder").isEmpty(),
                "Email input should have placeholder text");
        assertNotNull(passwordInput, "Password input should be present");
        assertFalse(passwordInput.attr("placeholder").isEmpty(),
                "Password input should have placeholder text");
    }
}