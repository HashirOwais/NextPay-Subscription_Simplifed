
![University of Regina Logo](https://www.uregina.ca/communications-marketing/assets/visual-identity/tagline-urlogo-white-background/ur_logo-w-1-line-tagline_horiz_full-colour_rgb.png)
# NextPay: Your Subscriptions, Simplified

Hashir Owais (200483044)  
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

Give a brief description of the design and a summary of the relevant background information related to the topic. Provide rationale about what is needed and why.

Give the reader an overview of what is in the next sections.

**Do not include detailed results here.**

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

- **Account creation**  
  The system will allow users to create individual accounts so that personal subscription data is stored separately.

- **View previous subscriptions**  
  Users can access a history of previously tracked subscriptions, in turn helping in analyzing long-term expenditure.

- **Sorting feature**  
  Subscriptions can be sorted based on the defined criteria to improve organization and readability.

- **Filtering feature**  
  Users will be able to filter subscriptions by dates and costs.

- **Countdown till next payment**  
  The application will be capable display the number of days remaining until the next scheduled payment.

- **Calculate total/monthly/yearly cost**  
  The system will automatically calculate and display the total cost of all active subscriptions, broken down by monthly and yearly values.

- **Exporting to a Excel sheet**  
  Users can export their subscription data Excel formats, enabling easy backup.

- **Importing from a Excel sheet**  
  The application will also allow importing data from Excel structured files, saving time during setup.

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
  Testing methodologies such as path testing, boundary value testing, equivalence class testing, and use case testing must be employed as part of the development lifecycle.

- **All data must be stored locally**  
  The application will store all user and subscription data on the local machine - no external or cloud storage is permitted.  

---

## 3. Solution

This section details various brainstormed solutions, their testing potential, and the rationale for the final choice.
Testing

### 3.1 Solution 1 (Website)

Our initial solution was to develop a full featured web application that provided a graphical user interface (GUI) for users to interact with the system. This would include user authentication with hashed credentials, allowing for secure access to features such as viewing, adding, and modifying subscription data.

This stack would have consisted of Java Spring Boot for the backend, React.js for the frontend, PostgreSQL as the database, and Hibernate as the ORM. The application would have been deployed using AWS infrastructure.

However, this solution was not selected due to the time constraints of our project timeline. Implementing a web-based interface along with secure authentication and front-end functionality would require extensive development and testing efforts. Given the scope of the course and the limited timeframe, we prioritized a simpler implementation that allowed us to focus more on core logic and testability, rather than UI complexity and integration testing.

### 3.2 Solution 2 (Mobile Application)

Our second proposed solution was to develop a mobile application using frameworks like React Native or Android Studio, providing users with a clean and accessible interface. It would have communicated with a backend service built in Java Spring Boot and stored data locally or remotely.

However, this approach was not chosen due to the added complexity in testing and deployment. Mobile apps require extensive UI testing across devices, emulators, and operating systems, making it harder to focus on core logic. Given our project timeline, we prioritized a simpler implementation that allowed for more effective unit testing and faster development.
 
### 3.3 Final Solution

Explain why this solution was selected over others. You may include a comparison table.

#### Components

List the components used, their purposes, and the testing methods applied. Include a block diagram labeled as **Fig. 1**.

#### Features

_(Placeholder section; add content as needed.)_

#### Environmental, Societal, Safety, and Economic Considerations

Discuss how the design addresses these aspects. Mention any decisions made to enhance reliability, safety, and cost-effectiveness.

#### Limitations

State the known limitations of the solution.

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

**Time**: May 22, 2025, 04:35pm – 05:pm 
**Agenda**: Completion of Deliverables

| Team Member     | Previous Task                                      | Completion State | Next Task                                                                                      |
|------------------|----------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------|
| Hashir Owais     | Created the repository and shared it with Dr. Sharma | Completed        | Schedule a meeting with Yogesh Sharma to discuss the project design and assist in finalizing the design section of the report. |
| Muhammad Tariq   | Added `REPORT.md` to the repository                | Completed        | Collaborate on completing the project design section of the report.                            |
| Simran Gahra     | Tracked deadlines and ensured timeline adherence   | Completed        | Review and finalize the project design section, proofread it, and ensure it is pushed to GitHub before the submission deadline. |


### Meeting 3

_Provide similar table and info as above._

### Meeting 4

_Provide similar table and info as above._

---

## 5. Project Management

Include a Gantt chart outlining all tasks, their predecessors, slack time, and critical path.

---

## 6. Conclusion and Future Work

Summarize achievements, focusing on fulfilled functions and objectives.  
Discuss any limitations and suggest improvements for future iterations.

---

## 7. References

Use **IEEE style** citations. Only include items that are cited in the report.

---

## 8. Appendix

Add any supplementary material or documentation here.

---
