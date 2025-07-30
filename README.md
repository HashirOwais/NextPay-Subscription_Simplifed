# NextPay – Quick‑Start Guide

Welcome!  This README walks you through cloning, building, testing, and running **NextPay** from the command line with Maven.

---
## Project Report & Test Documentation

Full design rationale, testing artefacts (path, data‑flow, BVA, etc.), and team documentation live in **REPORT.md**.

**REPORT.md** ➡️ [Open here](./REPORT.md)<br> 
**TESTING.md** ➡️ [Open here](./TESTING.md)

---
## Prerequisites

| Tool             | Minimum Version | Notes                                               |
| ---------------- | --------------- | --------------------------------------------------- |
| **JDK**          | 21 (LTS)        | Set `JAVA_HOME` and add `java` / `javac` to `PATH`. |
| **Apache Maven** | 3.9.x           | Verify with `mvn -v`.                               |
| **Git**          | any recent      | For cloning the repository.                         |



*Tested on MacOS, Windows, Linux.*

---

## Clone the repository

```bash
$ git clone https://github.com/HashirOwais/NextPay-Subscription_Simplifed.git
$ cd nextpay
```

##  Build & Compile

```bash
# Cleans any previous build and compiles the project
$ mvn clean compile
```

This step resolves dependencies (JUnit, SQLite driver, etc.) and produces `.class` files in `target/classes`.

---

##  Run the Unit & Integration Tests

```bash
$ mvn test
```

The Maven **Surefire** plugin executes all JUnit 5 test classes in `src/test/java`. A summary with the number of tests run, failures, and coverage will be displayed at the end.

---

## Execute the CLI Application

We use the Maven **exec** plugin to run the main class directly from the build tree — no need to package a jar first.

```bash
$ mvn exec:java
```


---

## Contributors

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/muhammadt1">
        <img src="https://github.com/muhammadt1.png" width="100px;" alt="Muhammad Tariq"/>
        <br />
        <sub><b>Muhammad Tariq</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/HashirOwais">
        <img src="https://github.com/HashirOwais.png" width="100px;" alt="Hashir Owais"/>
        <br />
        <sub><b>Hashir Owais</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/SimranGahra">
        <img src="https://github.com/SimranGahra.png" width="100px;" alt="Simran Gahra"/>
        <br />
        <sub><b>Simran Gahra</b></sub>
      </a>
    </td>
  </tr>
</table>