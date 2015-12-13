# selenium-webdriver

A Selenium Webdriver for running DukeScript Integration tests. Test methods are integrated with BrwsrCtx to be executed on the
correct thread. This isn't a full implementation of the WebDriver API yet, but it already supports some useful features. Use like this:

```
public class SimpleTest {

    private static WebDriverFX driver;

    @BeforeClass
    public static void test() throws InterruptedException, Exception {
        driver = new WebDriverFX(SimpleTest.class.getResource("testWithModel.html"));
        driver.executeAndWait(new Runnable() {
            @Override
            public void run() {
                TestModel testModel = new TestModel("Hello", "World");
                testModel.applyBindings();
            }
        });
    }

    @Test
    public void withModel() {
        WebElement element = driver.findElement(By.id("target"));
        Assert.assertEquals("Hello", element.getText());
        WebElement button = driver.findElement(By.id("button"));
        button.click();
        Assert.assertEquals("World", element.getText());
        WebElement input = driver.findElement(By.id("input"));
        input.clear();
        input.sendKeys("DukeScript");
        button.click();
        Assert.assertEquals("DukeScript", element.getText());
        try {
            Thread.sleep(1000); // not needed, just to give you enough time to see the updates in the browser 
        } catch (InterruptedException ex) {
            Logger.getLogger(SimpleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }
  ```
