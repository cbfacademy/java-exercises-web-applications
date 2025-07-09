# Web Applications

[![Language](https://img.shields.io/badge/language-HTML-E54B20.svg?style=for-the-badge)][1]

The goal of these programming exercises is to practise:
- working with a web server
- working with a web client
- creating simple HTML pages

## :globe_with_meridians: HTTP

**<ins>Exercise 1</ins>**

Analyse the following HTTP request:

```
    GET https://www.google.com HTTP/1.1
    Host: cs.unibg.it
    User Agent: Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124
    (KHTML, like Gecko) Safari/125
    Accept: ext/xml, application/xml, application/xhtml+xml, text/html;q=0.9,
    text/plain;q=0.8, image/png,*,*;q=0.5
    Accept-Language: it
    Keep-Alive: 300
    Connection: keep-alive
```

1. What is the requested URL?
2. Which version of HTTP is used?
3. Does the browser ask for a persistent or a non-persistent connection?
4. What is, in your opinion, the utility in indicating the type (and version) of browser used by
the client in the HTTP Request?

**<ins>Exercise 2</ins>**

An HTTP client sends the following message:

```
    GET http://cs.unibg.it /index.html HTTP/1.1
    User-agent: Mozilla/4.0
    Accept: text/html, image/gif, image/jpeg
    If-modified-since: 27 Feb 2017 08:10:00
```

Write down two feasible responses of the HTTP server (only the status line).

## :spider_web: HTML

An [HTML](https://developer.mozilla.org/en-US/docs/Web/HTML) form is a section of the page that [collects input](https://developer.mozilla.org/en-US/docs/Learn_web_development/Extensions/Forms/Basic_native_form_controls) from the user. The input from the user is generally sent to a server (web servers, mail clients, etc). We use the `<form>` element to create forms in HTML.

### Form Structure

Create a HTML form in [login.html](src/main/resources/login.html) with the following structure:

#### Required Elements:

Create a `<form>` element containing:
- Three `<div>` elements containing:
   1. An email-type `<input>` element with its id set to "email" and an external label
   2. A password-type `<input>` with its id set to "password" and an external label
   3. A checkbox-type input with its id set to "remember", inside a label and a "Forgot password" `<a>` element (you can use `#` for the href attribute)
- A submit-type `<button>`

#### Accessibility Requirements:
- All inputs should have a `name` attribute defined
- All inputs should have associated labels using `for` attributes or being wrapped by the associated label

### Visual Reference
![HTML Form](./html-form.png)

**Note:** Styling for the form is provided, but you won't be graded on its appearance. Focus on the correct HTML structure and accessibility features.

## :white_check_mark: Verify Your Implementation

To verify that your form is structured as expected, run the tests:

```shell
./mvnw clean test
```

**Learn more:** [Anatomy of an HTML Document](https://developer.mozilla.org/en-US/docs/Learn/Getting_started_with_the_web/HTML_basics#anatomy_of_an_html_document)
