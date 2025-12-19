# AI Web Researcher with Selenium & TestNG

This project automatically searches Bing for a user-provided keyword, visits the top 5 results, extracts content, and uses Google Gemini AI to analyze and generate summary reports. It's a **web automation and AI integration** application.

Additionally, the project includes Selenium-based automation examples such as **cross-browser testing** with TestNG (Chrome & Firefox) and a **price comparison** tool.
Testing is handled via JUnit for unit tests and Selenium + TestNG for end-to-end (E2E) functional testing.


## 🛠 Tech Stack

| Technology | Description |
|-----------|----------|
| **Java 21** | Core programming language |
| **Spring Boot 3.2.3** | Web application framework |
| **Selenium WebDriver 4.24.0** | Browser automation |
| **TestNG 7.9.0** | Testing framework (parameterized and factory tests) |
| **WebDriverManager 5.8.0** | Automatic driver setup |
| **Jsoup 1.17.2** | HTML parsing and web scraping |
| **Thymeleaf** | Server-side rendering (HTML templates) |
| **Google Gemini 2.5 Flash** | AI summarization and analysis |
| **Jackson** | JSON processing |
| **Maven** | Dependency management |

---

## Features and Development Details

### 1️⃣ **AI-Powered Web Research Engine**
**Key Files:**
- `SearchService.java`: Performs Bing search using Selenium, collects URLs, and decodes Bing's encrypted URL structure
- `ContentParser.java`: Extracts and cleans HTML content using Jsoup
- `OpenAIService.java`: Creates summaries and comparative reports using Google Gemini API
- `WebController.java`: Spring Boot controller managing `/` and `/search` endpoints
- `keyword_index.html` & `processing.html`: Dynamic page rendering with Thymeleaf

**How It Works:**
1. User enters a keyword → 2. Selenium searches Bing and finds the top 5 results → 3. Jsoup extracts content from each URL → 4. Gemini AI summarizes each content → 5. All summaries are combined into a final comparative report → 6. Results are displayed on the web interface

---

### 2️⃣ **Cross-Browser Testing (TestNG)**
**Key Files:**
- `LoginTest.java`: Tests login scenarios (valid/invalid username & password)
- `testng.xml`: Parallel test configuration for Chrome and Firefox

---

### 3️⃣ **Price Comparison Bot**
**Key Files:**
- `PriceCompare.java`: Scrapes Samsung tablet prices from 3 different e-commerce sites and compares them

**How It Works:**
- WebDriverManager automatically sets up Chrome driver
- Selenium visits 3 different sites:
  - NaveenLabs OpenCart
  - DemoBlaze
  - WebScraper.io
- XPath and CSS Selectors locate price elements
- Calculates cheapest, most expensive, and average prices

---

## 🚀 Installation and Running

### Requirements
- **Java 21** or higher
- **Maven 3.8+**
- **Google Chrome** or **Firefox** (WebDriverManager handles automatic setup)
- **Gemini API Key** (must be defined in OpenAIService.java)

---

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/SeleniumWebDriver_TestNG-master.git
cd SeleniumWebDriver_TestNG-master
```

---

### 2️⃣ Set Up Gemini API Key
Open `src/main/java/org/Keyword/chatGPT/OpenAIService.java` and add your API key:

```java
private static final String API_KEY = "YOUR_API_KEY_HERE";
```

**How to Get an API Key:**
1. Visit [Google AI Studio](https://aistudio.google.com/app/apikey)
2. Click "Create API Key"
3. Copy the generated key

---

### 3️⃣ Install Dependencies
```bash
mvn clean install
```

---

### 4️⃣ Run the Application

#### **A) Spring Boot Web Application**
```bash
mvn spring-boot:run
```

Open your browser at: **http://localhost:8080**

Enter a keyword (e.g., "Artificial Intelligence") and click "Start Research".

---

#### **B) Run TestNG Tests**
```bash
mvn test
```

This command runs all tests in `testng.xml` (Chrome & Firefox) in parallel.

---

#### **C) Run Price Comparison Bot**
```bash
mvn exec:java -Dexec.mainClass="pricehecker.PriceCompare"
```

The console will display Samsung tablet prices from 3 different sites and a comparison report.

---


## 📄 License

This project is licensed under the **MIT** license. See the `LICENSE` file for details.

---

## 🙏 Acknowledgments

This project was developed to learn Selenium WebDriver, TestNG, and AI integration. Open to improvements!

**For questions:** Use the GitHub Issues page.

---

## 📌 Contact
- **Email:** aybuke.yaman@tedu.edu.tr
