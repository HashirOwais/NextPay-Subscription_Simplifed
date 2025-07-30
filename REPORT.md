
![University of Regina Logo](https://www.uregina.ca/communications-marketing/assets/visual-identity/tagline-urlogo-white-background/ur_logo-w-1-line-tagline_horiz_full-colour_rgb.png)
# NextPay: Your Subscriptions, Simplified

Hashir Owais ( 200483044)  
Muhammad Tariq (200464392)<br>
Simran Gahra (200484408)  

<div align="center">
  <img src="Assets/companylogo.png" height="450">
</div>


## Table of Contents

1. [Introduction](#1-introduction)  
2. [Design Problem](#2-design-problem)  
   2.1 [Problem Definition](#21-problem-definition)  
   2.2 [Design Requirements](#22-design-requirements)  
   - 2.2.1 [Functions](#221-functions)  
   - 2.2.2 [Objectives](#222-objectives)  
   - 2.2.3 [Constraints](#223-constraints)  
3. [Solution](#3-solution)  
   - 3.1 [Solution 1](#31-solution-1)  
   - 3.2 [Solution 2](#32-solution-2)  
   - 3.3 [Final Solution](#33-final-solution)  
     - [Components](#components)  
     - [Features](#features)  
     - [Environmental, Societal, Safety, and Economic Considerations](#environmental-societal-safety-and-economic-considerations)  
     - [Limitations](#limitations)  
4. [Team Work](#4-team-work)  
   - [Meeting 1](#meeting-1)  
   - [Meeting 2](#meeting-2)  
   - [Meeting 3](#meeting-3)  
   - [Meeting 4](#meeting-4)  
5. [Project Management](#5-project-management)  
6. [Conclusion and Future Work](#6-conclusion-and-future-work)  
7. [References](#7-references)  
8. [Appendix](#8-appendix)  

---

## 1. Introduction

In today's digital age, managing multiple subscription services - such as streaming platforms, cloud storage, productivity tools, and fitness memberships - can be challenging and often leads to overlooked renewals, unexpected charges, and limited financial oversight. Many users lack a simple and effective way to track these recurring expenses, resulting in financial inefficiencies and missed opportunities to optimize spending.

To address this need, our project, **NextPay**, introduces a Java-based Command-Line Interface (CLI) application that centralizes the tracking and management of user subscriptions. Developed as part of the ENSE 375 Software Testing and Validation course, NextPay emphasizes rigorous test-driven development (TDD) using Java and JUnit, with all data securely stored locally via SQLite.

The application's design supports key features such as adding and removing subscriptions, filtering and sorting, cost calculations, renewal reminders, and CSV export. These features not only streamline subscription management but also serve as practical domains for applying systematic software testing methodologies required by the course, including boundary value, equivalence class, state transition, and use case testing.

The sections that follow in this report detail the design problem and requirements, document alternative and final solutions, explain team collaboration and management practices, and summarize testing approaches and project outcomes. This structure provides a comprehensive overview of our engineering and testing process, ensuring both practical effectiveness and alignment with ENSE 375 objectives.

---

## 2. Design Problem

### 2.1 Problem Definition

With the growing reliance on digital services, individuals increasingly subscribe to multiple platforms including streaming services, cloud storage, productivity tools, and fitness memberships. Managing these subscriptions manually often leads to challenges such as overlooked renewal dates, forgotten free trials, redundant services, and unexpected charges. These issues not only result in financial inefficiencies but also reduce user control over personal finances.

The goal of this project is to design and implement a Subscription Tracking Application that centralizes the management of recurring subscriptions. The application will allow users to input and monitor their active subscriptions, receive notifications for upcoming payments or trial expirations, and gain insights into their recurring spending patterns. By streamlining subscription oversight, the application aims to enhance financial awareness, reduce unnecessary expenditures, and provide a user centric solution to a common modern problem.


### 2.2 Design Requirements

#### 2.2.1 Functions

The Subscription Tracking Application will include a set of core features aimed at simplifying subscription management for users:

- **Add and remove subscriptions**  
  Users are be able to easily add new subscriptions by entering details such as name, cost, billing, and renewal date. Subscriptions that are no longer needed are removable with a simple command.



- **View previous subscriptions**  
  Users can access a history of previously tracked subscriptions, in turn helping in analyzing long-term expenditure.

- **Sorting feature**  
  Subscriptions can be sorted based on the defined criteria to improve organization and readability.

- **Filtering feature**  
  Users will be able to filter subscriptions by dates and costs.

- **Calculate total/monthly/yearly cost**  
  The system will automatically calculate and display the total cost of all active subscriptions, broken down by monthly and yearly values.

- **Exporting to a CSV**  
  Users can export their subscription data Excel formats, enabling easy backup.

#### 2.2.2 Objectives

- **User-friendly**  
  The system should be intuitive and simple to use, even for users with limited technical background.

- **Efficient**  
  Features should execute quickly, with minimal input required to perform essential actions like adding or updating a subscription.

- **Reliable**  
  The application must function consistently and correctly, maintaining data accuracy and stability throughout use.

- **Functioning**  
  All features described in the design must be implemented fully and operate as expected in real-world scenarios.

- **Passes software tests**  
  The system should be validated through unit testing to ensure that all components work as intended and handle edge cases properly.

#### 2.2.3 Constraints

- **Limited development and testing experience**  
  The team has limited prior experience with Java and formal software testing, which may affect implementation depth and testing coverage.

- **Application must be developed in a Java-based environment**  
  The entire application must be developed using Java, and all testing must be conducted with the JUnit framework to meet project requirements.

- **Must follow systematic testing principles**  
  All testing must follow the systematic testing principles and be integrated throughout the development lifecycle. The following testing methodoligies must be applied where applicable:
  - Boundary Value Testing
  - Equivalence Class Testing
  - Decision Tables Testing
  - State Transition Testing
  - Use Case Testing 
  - Integration Testing<br>
  
  In addition to these, **Path Testing** and **Data Flow Testing** must also be employed for certain individual functions.

- **All data must be stored locally**  
  The application will store all user and subscription data on the local machine - no external or cloud storage is permitted.  

---

## 3. Solution

This section details various brainstormed solutions, their testing potential, and the rationale for the final choice.
Testing

### 3.1 Solution 1 (Web Application)

Our first proposed solution involved building a full-stack web application with the following architecture:
  -    Frontend: React.js
  -    Backend: Java Spring Boot
  -    Database: PostgreSQL
  -    ORM: Hibernate
  -    Deployment: AWS Infrastructure

While this solution provided a scalable, modern, and user-friendly platform, it introduced substantial complexity in terms of testing. To properly validate this architecture, we would have required multiple testing tools and strategies:
  -    UI Testing using Jest, Selenium, or React Testing Library
  -    Backend API Testing with Spring Boot Test, Postman, and Mockito
  -    Database Integration Testing using containers (e.g., TestContainers)
  -    End-to-End Testing to validate full workflows

Due to this layered complexity, conducting focused JUnit-based unit testing on the core logic became more difficult and would detract from meeting the course’s emphasis on systematic, isolated testing techniques such as:
  -    **Boundary Value Testing**
  -    **Equivalence Class Testing**
  -    **Use Case Testing**

Furthermore, setting up and validating test environments across frontend, backend, and deployment layers would significantly slow down development. Given the limited timeframe and scope of ENSE 375, we decided not to proceed with this solution.

### 3.2 Solution 2 (Mobile Application)
The second idea was to build a mobile application using either React Native or Android Studio (Java/Kotlin). This would provide an intuitive interface and better portability for users.

However, similar to solution 1 this design came with its own testing challenges:
- Testing across multiple devices and operating systems reduces repeatability.
- UI workflows would rely on tools like Espresso or Detox, which are out of scope for this project.
- Core logic would still require JUnit testing, but isolating logic from UI in mobile development adds extra overhead.
- Integration and state validation are harder to automate due to simulator/emulator reliance.

This architecture limited our ability to apply structured testing techniques, particularly:
- Performing boundary and equivalence class testing without direct access to raw data inputs
- Executing repeatable use case tests in a headless/automated way

Since ENSE 375 places strong emphasis on test-driven development using JUnit and clear, structured testing strategies, this solution did not align well with our goals. We therefore chose not to implement the mobile architecture.

 
### 3.3 Final Solution - Java CLI App

After evaluating the web and mobile application designs, we selected a Command-Line Interface (CLI) Java Application with an SQLite local database as our final solution. This decision was driven primarily by the project’s focus on test-driven development (TDD) using Maven and structured testing techniques such as:
  - Boundary value testing
  - Equivalence class testing
  - Use case testing

Compared to the other solutions, the CLI approach provides the simplest and most controllable environment for systematic testing while meeting all functional requirements.

##### Comparison Table

| Criteria                  | Solution 1 (Web App)      | Solution 2 (Mobile)     | Final Solution (CLI App) |
|---------------------------|---------------------------|--------------------------|---------------------------|
| Ease of JUnit Testing     | Low                       | Medium                   | High                      |
| Testing Complexity        | High                      | High                     | Low                       |
| Setup/Deployment Complexity | High (AWS, Docker)       | Medium                   | Low (local only)          |
| Data Storage              | PostgreSQL                | SQLite/Local             | SQLite (Local)            |
| User Interface            | GUI (Web)                 | GUI (Mobile)             | CLI (Text-based)          |
| Meets Course Constraints  | Partially                 | Partially                | Fully                     |
| Learning Curve for Team   | High                      | Medium                   | Low                       |
| Development Time          | High                      | Medium                   | Low                       |

---

#### 3.3.1 Components

- **Java Classes**: App logic (CRUD, login, filters)  
  → JUnit: unit, path, data flow, boundary, equivalence, state

- **SQLite DB**: Local data storage  
  → Integration testing (DAO)

- **CLI Interface**: User interaction  
  → Use case + manual testing

- **File Exporting (CSV)**: Export subscriptions
  → Boundary + use case testing

Block Diagram (Fig. 1)

                +------------------+
                |   CLI Interface   |
                +---------+--------+
                          |
              +-----------v-----------+
              |   Application Logic    | (CRUD, Filters, Login)
              +-----------+-----------+
                          |
              +-----------v-----------+
              |  Database Manager      | (SQLite)
              +------------------------+


---

#### 3.3.2 Features

- User Account Creation & Login
- CRUD for Subscriptions (Add, Edit, Delete, View)
- Filter and Sort Subscriptions by Date, Cost, or Name
- Countdown to Next Payment
- Monthly and Yearly Cost Summaries
- Export to CSV (Excel-compatible)
- Local Data Storage using SQLite

---

#### 3.3.3 Environmental, Societal, Safety, and Economic Considerations

- **Environmental**: Local application—no server deployment reduces energy use compared to cloud-hosted solutions.
- **Societal**: Enhances user financial literacy and control over digital spending; accessible due to its lightweight CLI format.
- **Safety**: Local data storage mitigates data breach risks associated with cloud-based services.
- **Economic**: Free, open-source tools (Java, SQLite) with zero deployment costs; designed for personal finance management.
- **Reliability**: Fully tested with systematic unit and integration tests to ensure correctness and stability.

---

#### 3.3.4 Limitations

- No Graphical User Interface (GUI) – Might be less intuitive for non-technical users.
- No Cloud Backup – All data is local; risk of data loss without manual backups.
- Single-User Local Storage – No multi-user online support.
- Limited Error Handling – Basic error handling for CLI; advanced validation could be improved.
- Manual Export for Backup – Automation for backups is not implemented.

---

#### 3.3.5 Solution Overview:

Our final solution is a Java-based Command Line Interface (CLI) application backed by an SQLite database. This approach was selected because it allows us to apply rigorous JUnit-based testing techniques without the complexity introduced by front-end frameworks, mobile environments, or web deployment layers. It aligns perfectly with ENSE 375’s focus on systematic testing, including boundary value analysis, equivalence class testing, use case testing, decision tables, state transition testing, path testing, and data flow testing.

The CLI design minimizes setup overhead, improves development speed, and makes the core logic highly testable in isolation. Our solution is efficient, reliable, cost-effective, environmentally sustainable (due to no server resources), and safe with localized data storage.

While the lack of a graphical user interface is a limitation, the focus on correctness, functionality, and systematic testing fully satisfies the course requirements.

#### Final Considerations Comparison Table

| Criterion                        | Solution 1 (Web App)                                                  | Solution 2 (Mobile App)                                              | Final Solution (CLI App)                                                  |
|----------------------------------|------------------------------------------------------------------------|------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| Environmental Impact         | High – Cloud hosting and deployment use more energy                   | Medium – Device-dependent, moderate energy use                         | Low – Local app, no server deployment                                 |
| Societal Benefit             | Moderate – GUI accessible, but high complexity                        | High – User-friendly interface, good accessibility                     | High – Improves financial literacy via lightweight CLI                 |
| Safety (Data Security)       | Moderate – Hosted DB with potential breach risks                      | Moderate – App data stored locally, but less secure                    | High – Local storage reduces breach risks                              |
| Economic Cost                | High – Requires cloud infra, devops, third-party tools                | Medium – Platform-dependent, some free tools used                      | Low – Open-source tools, no deployment costs                           |
| Reliability (Testing)        | Low – Complex integration makes systematic testing harder             | Medium – Some automated testing, but emulator/device issues            | High – Fully tested with JUnit and structured techniques               |
| GUI Availability             | Yes – Web GUI                                                         | Yes – Mobile GUI                                                       | No – CLI only                                                          |
| Cloud Backup                 | Yes – Built-in through hosting                                        | Partial – Depends on implementation                                    | No – Local only; manual backups required                               |
| Multi-User Support           | Yes – Account-based via backend                                       | Partial – Some mobile apps support this                                | No – Single-user local only                                            |
| Error Handling               | Moderate – Backend can handle exceptions                              | Moderate – Platform-dependent                                          | Basic – CLI-based, limited validation                                  |
| Automation for Backups       | Possible – Through backend scripts                                    | Limited – Could be automated with OS tools                             | Not Implemented – Manual export required                        |

---

## 4. Team Work

### Meeting 1

**Time**: May 12, 2025, 12:15pm – 12:55pm  
**Agenda**: Project Idea Discusssion

| Team Member     | Previous Task | Completion State | Next Task |
|------------------|----------------|------------------|------------|
| Hashir Owais | N/A            | N/A              | Create repository and share with Dr Sharma     |
| Muhammad Tariq | N/A            | N/A              | Add REPORT.md into repository    |
| Simran Gahra | N/A            | N/A              | Ensure timelines are being met/ make note of deadlines     |

**Meeting Outcome**:The outcome of this meeting was us finalzing the idea for the project. We concluded that our application will be a subscription tracking app.

### Meeting 2

**Time**: May 22, 2025, 04:35pm – 05:pm <br>
**Agenda**: Completion of Deliverables

| Team Member     | Previous Task                                      | Completion State | Next Task                                                                                      |
|------------------|----------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------|
| Hashir Owais     | Created the repository and shared it with Dr. Sharma | Completed        | Schedule a meeting with Yogesh Sharma to discuss the project design and assist in finalizing the design section of the report. |
| Muhammad Tariq   | Added `REPORT.md` to the repository                | Completed        | Collaborate on completing the project design section of the report.                            |
| Simran Gahra     | Tracked deadlines and ensured timeline adherence   | Completed        | Review and finalize the project design section, proofread it, and ensure it is pushed to GitHub before the submission deadline. |


### Meeting 3

**Time**: June 4th 2025, 12:15pm – 1:15:pm <br>
**Agenda**: Solutions Delegation Session

| Team Member     | Previous Task                                      | Completion State | Next Task                                                                                      |
|------------------|----------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------|
| Hashir Owais     | Schedule a meeting with Yogesh Sharma to discuss the project design and assist in finalizing the design section of the report. | Completed        | Assist team to work on section 3 and determine what solutions can be approached. |
| Muhammad Tariq   | Collaborate on completing the project design section of the report.                | Completed        | Collaborate with the team to work on section 3 and push the updated report to the repository by the deadline.                            |
| Simran Gahra     | Review and finalize the project design section, proofread it, and ensure it is pushed to GitHub before the submission deadline.   | Completed        | Work with team to brainstorm new ideas for section 3. |

### Meeting 4

**Time**: June 20th 2025, 3:15pm – 4:15pm <br>
**Agenda**: Finalize solution for project and do comparisons with other solutions.

| Team Member     | Previous Task                                      | Completion State | Next Task                                                                                      |
|------------------|----------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------|
| Hashir Owais     | Assist team to work on section 3 and determine what solutions can be approached. | In Progress - 75%        | Complete the pending tasks and start the MVP for the project to better visualize user flow. |
| Muhammad Tariq   | Collaborate with the team to work on section 3 and push the updated report to the repository by the deadline.                | In Progress - 75%        |   Complete current tasks, assist team with any pending tasks and start making the ERD for the relational database.                         |
| Simran Gahra     | Work with team to brainstorm new ideas for section 3.   | In Progress - 80%    |Wrap up current task and assist with MVPs and ERD's. |

---

### Meeting 5

**Time**: June 30th, 2025, 6:15pm - 8:00pm <br>
**Agenda**: Initial Project Config

| Team Member     | Previous Task                                      | Completion State | Next Task                                                                                      |
|------------------|----------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------|
| Hashir Owais | Complete the pending tasks and start the MVP for the project to better visualize user flow. | In Progress - 60% | Complete current and previous assigned tasks. |
| Muhammad Tariq   | Complete current tasks, assist team with any pending tasks and start making the ERD for the relational database. |   In Progress - 70% | Complete current and previous assigned tasks. |
| Simran Gahra     | Wrap up current task and assist with MVPs and ERD's. | In Progress - 70% | Complete current and previous assigned tasks. |

---

### Meeting 6

**Time**: July 02th, 2025, 12:15pm - 3:10pm <br>
**Agenda**: Junit Project Config and Module Brainstorm

| Team Member     | Previous Task                                      | Completion State | Next Task                                                                                      |
|------------------|----------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------|
| Hashir Owais     | Complete current and previous assigned tasks. | Completed | Lead the discussions for project structure and mangement. |
| Muhammad Tariq   | Complete current and previous assigned tasks. | Completed | Initialize Maven project and setup modules. |
| Simran Gahra     | Complete current and previous assigned tasks. | Completed | Brainsorm methods/modules for Nextpay and assist with Maven folder structure. |

---

### Meeting 7

**Time**: July 05th, 2025, 2:30pm - 7:25pm <br>
**Agenda**: Impliment DB_Module

| Team Member     | Previous Task                                      | Completion State | Next Task                                                                                      |
|------------------|----------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------|
| Hashir Owais     | Lead the discussions for project structure and mangement. | Completed | Implement and test functions for updating, filtering subscriptions by name and date. |
| Muhammad Tariq   | Initialized Maven project and setup modules. | Completed | Implement and test functions to add, delete, and view subscriptions. |
| Simran Gahra     | Brainsorm methods/modules for Nextpay and assist with Maven folder structure. | Completed | Implement and test functions for DB creation and initialization, user authentication, and exporting subscriptions to CSV. |

---

### Meeting 8

**Time**: July 9th, 2025, 5:10pm – 7:05pm  
**Agenda**: Initialize and brainstorm functions for `UI_module` and `Subscriptions_module`

| Team Member     | Previous Task                                                                 | Completion State | Next Task                                                                                      |
|------------------|------------------------------------------------------------------------------|------------------|------------------------------------------------------------------------------------------------|
| Hashir Owais     | Implement and test functions for updating, filtering subscriptions by name and date.            | Completed        | Draft CLI layout and define interaction flow for filtering options in `UI_module`.            |
| Muhammad Tariq   | Implement and test functions to add, delete, and view subscriptions.      | Completed        | Define function responsibilities and parameters for adding/deleting subscriptions.            |
| Simran Gahra     | Implement and test functions for DB creation and initialization, user authentication, and exporting subscriptions to CSV.                     | Completed        | Determine data requirements and interface signatures between DB and `Subscriptions_module`.   |

---

### Meeting 9

**Time**: July 16th, 2025, 10:25am – 1:55pm  
**Agenda**: Implement functions for `UI_module` and `Subscriptions_module`

| Team Member     | Previous Task                                                                 | Completion State | Next Task                                                                                      |
|------------------|------------------------------------------------------------------------------|------------------|------------------------------------------------------------------------------------------------|
| Hashir Owais     | Draft CLI layout and define interaction flow for filtering options in `UI_module`.                              | Completed        | Merge and test filtering commands in UI; validate against edge cases.                         |
| Muhammad Tariq   | Define function responsibilities and parameters for adding/deleting subscriptions.                              | Completed        | Merge add/delete logic into main branch and resolve integration conflicts.                    |
| Simran Gahra     | Determine data requirements and interface signatures between DB and `Subscriptions_module`.                             | Completed        | Test DB interactions with live CLI; ensure robust error handling and logging.                 |

---

### Meeting 10

**Time**: July 22nd, 2025, 5:25pm – 7:30pm  
**Agenda**: Merge code for `UI_module` and `Subscriptions_module`

| Team Member     | Previous Task                                                                 | Completion State | Next Task                                                                                      |
|------------------|------------------------------------------------------------------------------|------------------|------------------------------------------------------------------------------------------------|
| Hashir Owais     | Merge and test filtering commands in UI; validate against edge cases.                        | Completed        | Begin test case drafting for UI routing and input validation.                                 |
| Muhammad Tariq   | Merge add/delete logic into main branch and resolve integration conflicts.                                  | Completed        | Begin unit test creation for core subscription operations.                                    |
| Simran Gahra     | Test DB interactions with live CLI; ensure robust error handling and logging.                               | Completed        | Ensure testable state of DB components and complete meeting minutes.                          |

---

### Meeting 11

**Time**: July 24th, 2025, 2:00pm – 3:15pm  
**Agenda**: Improve code coverage for the application

| Team Member     | Previous Task                                                                 | Completion State | Next Task                                                                                      |
|------------------|------------------------------------------------------------------------------|------------------|------------------------------------------------------------------------------------------------|
| Hashir Owais     | Begin test case drafting for UI routing and input validation.                          | In Progress      | Expand CLI testing to cover invalid inputs and command boundaries.                            |
| Muhammad Tariq   | Begin unit test creation for core subscription operations.                              | In Progress      | Strengthen test cases for logic and edge cases; target >75% coverage.                         |
| Simran Gahra     | Ensure testable state of DB components and complete meeting minutes.                          | In Progress      | Define `TESTING.md` layout and gather equivalence/boundary test inputs.                       |

---

### Meeting 12

**Time**: July 29th, 2025, 3:35pm – 7:15pm  
**Agenda**: Begin formal testing and finalize `TESTING.md`

| Team Member     | Previous Task                                                                 | Completion State | Next Task                                                                                      |
|------------------|------------------------------------------------------------------------------|------------------|------------------------------------------------------------------------------------------------|
| Hashir Owais     | Expand CLI testing to cover invalid inputs and command boundaries.                     | In Progress      | Complete path/data flow testing and state transition scenarios.                               |
| Muhammad Tariq   | Strengthen test cases for logic and edge cases; target >75% coverage.                  | In Progress      | Conduct decision table and integration tests. Format test sections in `TESTING.md`.           |
| Simran Gahra     | Define `TESTING.md` layout and gather equivalence/boundary test inputs.                  | In Progress      | Execute use case and equivalence class tests. Finalize formatting and QA review.              |

---

## 5. Project Management

The following Gantt chart outlines all major tasks, their dependencies, slack time, and the critical path for the NextPay project.

<div align="center">
  <img src="Docs/GANTT Chart-2025-07-28-234126.png" alt="Gantt Chart" width="850"/>
</div>


### 5.1 Task Breakdown & Predecessors
| ID | Task Name                          | Predecessors | Duration | Slack | Notes                                 |
|----|------------------------------------| ------------ |----------|-------|---------------------------------------|
| 1  | Planning & Team Formation          | —            | 2 weeks  | 0     | Critical path begins here             |
| 2  | Design Requirements & Constraints  | 1            | 2 weeks  | 0     | Informs DB and CLI requirements       |
| 3  | DB Schema & System Design          | 2            | 2 weeks  | 1     | Can begin slightly after design phase |
| 4  | CLI Development & Core Features    | 3            | 4 weeks  | 0     | Core logic must be completed early    |
| 5  | DB Integration & Connecting Filtering/Sorting | 4    | 2 weeks  | 0     | Built on finalized CLI + DB           |
| 6  | Export, Auth, Refactor      | 5            | 2 weeks  | 1     | Enhancements after core CLI done      |
| 7  | All Testing & Bug Fixes            | 4, 5, 6      | 3 weeks  | 0     | Tests must cover all finalized logic  |
| 8  | Final Docs, Slides, Polish         | 7            | 1 week   | 0     | Must follow completion of dev & test  |

### 5.2 Slack Time
- Task 3 (DB Schema) and Task 6 (Enhancements) each have about 1 week of slack, giving flexibility if earlier tasks are delayed.
- All other tasks have zero slack.
- **Critical Path:** Task 1 → Task 2 → Task 4 → Task 5 → Task 7 → Task 8

---

## 6. Conclusion and Future Work

### Conclusion
The NextPay subscription tracking application successfully achieved all primary design functions and objectives while satisifying the comprehensive testing requirements of ENSE 375. Our team brought there minds together to develope a fully functional Java Based CLI Application that addresses the core problem of subscription management thorough systematic software development and rigorous testing practices such as Test Driven Development (TDD). The key achievements include:
- Efficently adding, updating, deleting user subscriptions with completed CRUD fucntionality.
- Managing user accounts.
- Sorting subscriptions by billing dates in ascending or descending order.
- Calculating and displaying subscription costs that occur monthly or yearly with comprehensive summaries.
- Allowing users to export their subscription data to CSV format, for any external purposes.
- Storing all data securely in a local SQLite database system
- Providing an intuitive CLI for streamlined subscription management.

By achieving thes objectives, we satisfied the goals of Java based development. systematic JUnit testing, local data storage, and user friendliness while meeting all ENSE 375 testing requirements.

### Future Work
While the final solution meets the current needs of individual subscription management, there are opportunies for future improvements:
1) **Web Application Development**: Develop a full-stack web application using Spring Boot backend with REST APIs and React frontend for cross platform accessibility and modern user experience.
2) **Multi User Support**: Enhancing the architecture to support multiple accounts with shared subscription management and collaborative budgeting features.
3) **Advanced Analytics**: Extend the analytics to include more details of subscription habits. Adding notification support to email and SMS for subscription renewals can also be implemented.
4) **Subscription Category Management**: Build upon the current subscription data model to include categorization features, allowing users to group subscriptions and view category-based spending summaries similar to the existing monthly summary feature.
5) **AI Integration**: Implement machine learning algorithms to analyze user subscription patterns and provide personalized financial education recommendations.

---

## 7. References

Use **IEEE style** citations. Only include items that are cited in the report.

---

## 8. Appendix

### UI Module Diagram
<div align="center">
  <img src="Docs/UI_Module_NextPay.png" alt="UI Module Diagram" width="650"/>
</div>

### Subscriptions Module Diagram
<div align="center">
  <img src="Docs/Subscriptions_Module_NextPay.png" alt="Subscriptions Module Diagram" width="650"/>
</div>

### Database Module Diagram
<div align="center">
  <img src="Docs/DB_Module_NextPay.png" alt="DB Module Diagram" width="650"/>
</div>

### Storyboard
<div align="center">
  <img src="Docs/Storyboard_NextPay.png" alt="Storyboard" width="650"/>
</div>

### Database ERD
<div align="center">
  <img src="Docs/db_ERD_NextPay.png" alt="Database ERD" width="650"/>
</div>


---
