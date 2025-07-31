# TESTING.md

**Note:** All diagrams in this document use Mermaid syntax for flowcharts, ERDs, state machines, and graphs.

## 1. Overview

This document describes the systematic testing plan for NextPay, covering unit tests, integration tests, and validation techniques as per ENSE 375 requirements. All JUnit tests have been implemented; this report outlines the test design, control and data-flow analyses, and key test cases.

---

## 2. Specification Based Testing






### 2.1 Path Testing

- **Target**: `db_module.addSubscription(Subscription s)`
- **Paths Covered**:
  - Valid input → saved successfully → returns true
  - Empty name or negative cost → validation fails → returns false
  - Exception in DB layer → returns false (though not directly unit-tested)

---



```mermaid
flowchart TD
  N1([N1: Start])
  N2[N2: Validate name, cost, billingCycleType]
  N3{N3: Validation OK?}
  N4[N4: Try DB Connection]
  N5[N5: Prepare & execute INSERT]
  N6{N6: Insert successful?}
  N7[N7: Fetch last inserted ID]
  N8[N8: Set ID on object]
  N9[N9: Return true]
  N10[N10: Return false]
  N11[N11: Caught SQL Exception → printStackTrace]
  N12([N12: End])

  N1 --> N2 --> N3
  N3 -- No --> N10 --> N12
  N3 -- Yes --> N4 --> N5 --> N6
  N6 -- Yes --> N7 --> N8 --> N9 --> N12
  N6 -- No --> N9 --> N12
  N4 --> N11 --> N10 --> N12
  N5 --> N11
  N7 --> N11
```



#### 2.1.1 Prime Paths (PPC)

| ID  | Prime Path                                                            | Description                        |
|-----|----------------------------------------------------------------------|------------------------------------|
| P1  | N1 → N2 → N3(No) → N10 → N12                                         | Validation fails                   |
| P2  | N1 → N2 → N3(Yes) → N4 → N5 → N6(No) → N9 → N12                      | Insert returns 0 rows (edge case) |
| P3  | N1 → N2 → N3(Yes) → N4 → N5 → N6(Yes) → N7 → N8 → N9 → N12           | Full happy path                    |
| P4  | N4 → N11 → N10 → N12                                                 | DB exception                       |

---

#### 2.1.2 Path Testing Summary Table

| Path ID | Test Requirement (Path Description)                                                     | JUnit Test Case Name                                       | Expected Outcome            | Actual Result        |
|---------|------------------------------------------------------------------------------------------|------------------------------------------------------------|-----------------------------|----------------------|
| P1      | N1 → N2 → N3(No) → N10 → N12  → Validation fails (invalid name or cost)                 | `addSubscription_EmptyName_ReturnsFalse()`                 | returns `false`, no insert  | ✅ As Expected       |
| P1      | N1 → N2 → N3(No) → N10 → N12  → Validation fails (invalid name or cost)                 | `addSubscription_NegativeCost_ReturnsFalse()`              | returns `false`, no insert  | ✅ As Expected       |
| P2      | N1 → N2 → N3(Yes) → N4 → N5 → N6(No) → N9 → N12 → Insert failed (rows = 0)              | *(No direct test implemented)*                             | returns `false`             | ❌ Not Covered       |
| P3      | N1 → N2 → N3(Yes) → N4 → N5 → N6(Yes) → N7 → N8 → N9 → N12 → Full success               | `addSubscription_ValidSubscription_True()`                 | returns `true`, DB insert   | ✅ As Expected       |
| P3      | N1 → N2 → N3(Yes) → N4 → N5 → N6(Yes) → N7 → N8 → N9 → N12 → Full success               | `addSubscription_ValidNonRecurringSubscription_True()`     | returns `true`, DB insert   | ✅ As Expected       |
| P4      | N4 → N11 → N10 → N12 → DB exception triggers → catch & return false                    | *(No direct test implemented)*                             | returns `false`, print err  | ❌ Not Covered       |

🔸 Note: No existing test explicitly triggers P2 or P4 




### 2.2 Data‑Flow Testing

**Target**: `db_module.updateSubscription(Subscription s)`

- **Definitions & Uses**:  
  - **DU1**: `def name = s.getSubscriptionsName` → `use name in null/empty check`  
  - **DU2**: `def cost = s.getCost` → `use cost in cost < 0 check`  
  - **DU3**: `def cycleType = s.getBillingCycleType` → `use cycleType in SQL binding`  
  - **DU4**: `def date = s.getBillingCycleDate` → `use date in SQL binding`

---

#### Definitions and Uses for `updateSubscription(Subscription s)`

```mermaid
flowchart TD
  N1([N1: Start])
  N2[N2: def name = s.getSubscriptionsName]
  N3[N3: use name in null/empty check]
  N4{N4: Name valid?}
  N5[N5: def cost = s.getCost]
  N6[N6: use cost in <0 check]
  N7{N7: Cost valid?}
  N8[N8: def cycleType = s.getBillingCycleType]
  N9[N9: use cycleType in SQL bind]
  N10[N10: def date = s.getBillingCycleDate]
  N11[N11: use date in SQL bind]
  N12[N12: Try DB Connection]
  N13[N13: Prepare & bind values]
  N14[N14: Execute update]
  N15{N15: rows > 0?}
  N16[N16: Return true]
  N17[N17: Return false]
  N18[N18: SQL Exception → printStackTrace]
  N19([N19: End])

  N1 --> N2 --> N3 --> N4
  N4 -- No --> N17 --> N19
  N4 -- Yes --> N5 --> N6 --> N7
  N7 -- No --> N17 --> N19
  N7 -- Yes --> N8 --> N9 --> N10 --> N11 --> N12 --> N13 --> N14 --> N15
  N15 -- Yes --> N16 --> N19
  N15 -- No --> N17 --> N19
  N12 --> N18 --> N17 --> N19
  N13 --> N18
  N14 --> N18
```

---

#### 2.2.1 DU Paths: Definition–Use Chains

| ID   | DU Path          | Description                            |
|------|------------------|----------------------------------------|
| DU1  | N2 → N3 → N4      | Name defined & used in name check      |
| DU2  | N5 → N6 → N7      | Cost defined & used in cost check      |
| DU3  | N8 → N9           | CycleType defined & used in SQL bind   |
| DU4  | N10 → N11         | BillingDate defined & used in SQL bind |

---

#### 2.2.2 Actual JUnit Test Cases for Data‑Flow

| ID   | DU Path(s)      | Description              | Test Method Name                                          | Expected Result             |
|------|------------------|--------------------------|-----------------------------------------------------------|-----------------------------|
| TC4  | DU1, DU2–DU4     | Valid update             | `updateSubscription_ValidUpdate_ReturnsTrue()`           | returns `true`, DB updated  |
| TC5  | DU2              | Negative cost            | `updateSubscription_NegativeCost_ReturnsFalse()`          | returns `false`, no update  |
| TC6  | DU1              | Empty name               | `updateSubscription_EmptyName_ReturnsFalse()`             | returns `false`, no update  |

---
#### 2.2.3 Data flow Summary Table

| DU ID | Test Requirement (Definition–Use Chain Description)             | JUnit Test Case Name                                      | Expected Result            | Actual Result      |
|-------|------------------------------------------------------------------|-----------------------------------------------------------|-----------------------------|--------------------|
| DU1   | N2 → N3 → N4 → `name` used in null/empty check                  | `updateSubscription_EmptyName_ReturnsFalse()`             | returns `false`, no update | ✅ As Expected     |
| DU2   | N5 → N6 → N7 → `cost` used in <0 validation                     | `updateSubscription_NegativeCost_ReturnsFalse()`          | returns `false`, no update | ✅ As Expected     |
| DU3   | N8 → N9 → `cycleType` used in SQL bind                          | `updateSubscription_ValidUpdate_ReturnsTrue()`            | returns `true`, DB updated | ✅ As Expected     |
| DU4   | N10 → N11 → `billingDate` used in SQL bind                      | `updateSubscription_ValidUpdate_ReturnsTrue()`            | returns `true`, DB updated | ✅ As Expected     |

---



## 3. Integration Testing
Integration testing validates the interaction between our three core modules (UI, Subscriptions, and Database) through end-to-end CLI workflows. We test complete user scenarios spanning multiple modules, ensuring data flows correctly from user input through business logic to database persistence. Our approach uses sequential operations (add→view→delete) to validate that changes in one module are correctly reflected in dependent modules, ensuring the application functions as a cohesive system.

* **Modules**: UI Module ↔ Subscriptions Module ↔ Database Module
* **Scenario**: add → view → delete subscription via CLI commands

---

### 3.1 Test Cases

| ID   | Action           | Steps                                   | Expected Outcome                |
| ---- | ---------------- | --------------------------------------- | ------------------------------- |
| INT1 | Add then view    | 1. ui.add("Netflix",...)<br>2. ui.list  | Entry appears in DB and console |
| INT2 | Delete after add | 1. Add subscription<br>2. ui.delete(id) | Removed from DB; confirmation   |

#### 3.1.1 Test Case Diagrams

```mermaid
flowchart TD
  Start --> Add[Add Subscription]
  Add --> View[List Subscriptions]
  View --> Verify1[Console & DB Check]
  Verify1 --> End
```

```mermaid
flowchart TD
  Start --> Add[Add Subscription]
  Add --> Delete[Delete Subscription]
  Delete --> Verify2[Console & DB Check]
  Verify2 --> End
```

| ID   | Action           | Steps                                         | Expected Outcome                |
|------|------------------|-----------------------------------------------|---------------------------------|
| INT1 | Add then view    | 1. `ui.add("Netflix",...)`<br>2. `ui.list`    | Entry appears in DB and console |
| INT2 | Delete after add | 1. Add subscription<br>2. `ui.delete(id)`     | Removed from DB; confirmation   |

---

## 4. Validation Testing
Validation testing ensures NextPay meets user requirements through five systematic techniques: Boundary Value Analysis for edge cases, Equivalence Class Testing for input partitioning, Decision Table Testing for complex logic combinations, State-Transition Testing for UI flow validation, and Use-Case Testing for end-to-end scenarios. This approach validates proper handling of both valid inputs and graceful rejection of invalid data across our subscription management system.

**Files and Methods Under Test**  
- **subscriptions_module.java**  
  - `addSubscription(Subscription s)`  
  - `updateSubscription(Subscription updated)`  
  - `handleDeleteSubscription(int userId, int subscriptionId)`  
- **db_module.java**  
  - `addSubscription(Subscription s)`  
  - `updateSubscription(Subscription s)`  
  - `exportSubscriptions(int userId)`  
- **UIModule.java**  
  - `handleAddSubscription(int userId)`  
  - `handleUpdateSubscription(int userId, int subId)`
  - `handleMainMenuSelection(int choice)`
  - `handleStartSelection(int choice)` 

---

### 4.1 Boundary Value Analysis

**Target Methods:** `db_module.addSubscription()`, `UIModule.handleAddSubscription()`, `UIModule.handleMainMenuSelection()`, `UIModule.handleStartSelection()`


We pick values at, just below, and just above each boundary to exercise edge cases.

#### Cost Field Testing:

|Test Case|Input Value|Expected Result|Actual Result|
|---|---|---|---|
|1|-0.01|Rejected|Rejected|
|2|0|Accepted|Accepted|
|3|0.01|Accepted|Accepted|
|4|9999.99|Accepted|Accepted|
|5|10000|Accepted|Accepted|
|6|10000.01|Rejected|Rejected|

**Boundaries:** Min = 0, Min+ = 0.01, Max– = 9999.99, Max = 10000


#### Name Length Testing:

|Test Case|Name Length|Expected Result|Actual Result|
|---|---|---|---|
|1|0 (empty)|Rejected|Rejected|
|2|1|Accepted|Accepted|
|3|2|Accepted|Accepted|
|4|50|Accepted|Accepted|
|5|99|Accepted|Accepted|
|6|100|Accepted|Accepted|
|7|101|Rejected|Rejected|

**Boundaries:** Min = 1, Min+ = 2, Max– = 99, Max = 100

> **Example:**  
> Calling `db_module.updateSubscription(s)` with `s.getCost() = -5.00` returns `false`.



#### Menu Choice Testing:

|Test Case|Menu Choice|Expected Result|Actual Result|
|---|---|---|---|
|1|0|Rejected (-1)|Rejected (-1)|
|2|1|Accepted (1)|Accepted (1)|
|3|2|Accepted (2)|Accepted (2)|
|4|5|Accepted (5)|Accepted (5)|
|5|6|Accepted (0)|Accepted (0)|
|6|7|Rejected (-1)|Rejected (-1)|
|7|99|Rejected (-1)|Rejected (-1)|

**Boundaries:** Min = 1, Min+ = 2, Max– = 5, Max = 6





---

### 4.2 Equivalence Class Testing

We partition each input into valid/invalid classes and select one representative test per class.

### Add Subscription Testing

**Target Methods:** `db_module.addSubscription()`, `db_module.updateSubscription()`, `subscriptions_module.addSubscription()`, `subscriptions_module.updateSubscription()`, `UIModule.handleAddSubscription()`, `UIModule.handleUpdateSubscription()`

#### Cost Field Testing:

|Test Case|Input Value|Equivalence Class|Expected Result|Actual Result|
|---|---|---|---|---|
|1|-5.00|Invalid (< 0)|Rejected|Rejected|
|2|0.00|Valid (= 0)|Accepted|Accepted|
|3|10.99|Valid (> 0)|Accepted|Accepted|
|4|9999.99|Valid (high)|Accepted|Accepted|

**Classes:** Valid ≥ 0 (including free) | Invalid < 0


#### Name Field Testing:


|Test Case|Input Value|Equivalence Class|Expected Result|Actual Result|
|---|---|---|---|---|
|1|""|Invalid (empty)|Rejected|Rejected|
|2|"A"|Valid (1 char)|Accepted|Accepted|
|3|"Netflix"|Valid (normal)|Accepted|Accepted|
|4|101-char|Invalid (> 100)|Rejected|Rejected|

**Classes:** Valid 1–100 characters | Invalid empty or > 100 characters

#### Cycle Type Testing:


|Test Case|Input Value|Equivalence Class|Expected Result|Actual Result|
|---|---|---|---|---|
|1|"monthly"|Valid (standard)|Accepted|Accepted|
|2|"yearly"|Valid (standard)|Accepted|Accepted|
|3|"one-time"|Valid (standard)|Accepted|Accepted|
|4|"weekly"|Invalid (other)|Rejected|Rejected|
|5|""|Invalid (empty)|Rejected|Rejected|

**Classes:** Valid `monthly`, `yearly`, `one-time` | Invalid any other string
Target Methods: db_module.isUserValid(String username, String password), subscriptions_module.validateUser(String username, String password), subscriptions_module.isUserValid(User user)

### User Authentication Testing:

**Target Methods**: `db_module.isUserValid(String username, String password)`, `subscriptions_module.isUserValid(User user)`,`UIModule.handleLogin(String username, String password)`

#### Username field testing
|Test Case|Input Value|Equivalence Class|Expected Result|Actual Result|
|---|---|---|---|---|
|1|""|Invalid (empty)|Rejected (false)|Rejected (false)|
|2|"testuser"|Valid (existing user)|Accepted (true)|Accepted (true)|
|3|"nonexistent"|Valid format (non-existing)|Rejected (false)|Rejected (false)|
|4|null|Invalid (null)|Rejected (false)|Rejected (false)|
|5|"user with spaces"|Valid format|Rejected (false)|Rejected (false)|

**Classes:** Valid existing username | Invalid (empty/null/non-existing)


#### Password Field Testing:
| Test Case | Input Value     | Equivalence Class        | Expected Result  | Actual Result    |
| --------- | --------------- | ------------------------ | ---------------- | ---------------- |
| 1         | ""              | Invalid (empty)          | Rejected (false) | Rejected (false) |
| 2         | "password123"   | Valid (correct password) | Accepted (true)  | Accepted (true)  |
| 3         | "wrongpassword" | Valid format (incorrect) | Rejected (false) | Rejected (false) |
| 4         | null            | Invalid (null)           | Rejected (false) | Rejected (false) |
| 5         | "short"         | Valid format (too short) | Rejected (false) | Rejected (false) |

**Classes:** Valid correct password | Invalid (empty/null/incorrect)

#### Combined Authentication testing (Username and Password)

|Test Case|Username|Password|Equivalence Class|Expected Result|Actual Result|
|---|---|---|---|---|---|
|1|"testuser"|"password123"|Valid (both correct)|Accepted (true)|Accepted (true)|
|2|"testuser"|"wrongpass"|Invalid (wrong password)|Rejected (false)|Rejected (false)|
|3|"wronguser"|"password123"|Invalid (wrong username)|Rejected (false)|Rejected (false)|
|4|""|"password123"|Invalid (empty username)|Rejected (false)|Rejected (false)|
|5|"testuser"|""|Invalid (empty password)|Rejected (false)|Rejected (false)|
|6|""|""|Invalid (both empty)|Rejected (false)|Rejected (false)|
|7|null|null|Invalid (both null)|Rejected (false)|Rejected (false)|

**Classes**
- Valid: Both username and password match predefined record.
- Invalid: Any combination where username doesnt exist, password is wrong or either field is empty




---

### 4.3 Decision Table Testing 
Our objective is to verify db_module.addSubscription(Subscription s) and subscriptions_module.handleDeleteSubscription(int userId,int subId)  under every meaningful combination of its validation checks. <br>

We use the lecture’s algorithmic steps: identify → enumerate → map → prune → derive tests.

- Identify: List all condition and action stubs.
- Enumerate: Expand the full set of rules (truth table).
- Prune: Collapse duplicates or irrelevant cases.
- Derive: Turn each unique rule into a concrete JUnit/UITest.

#### 4.3.1 Decision Tables - addSubscription 

#### 4.3.1.1 Condition & Action Stubs

| ID     | Condition stub                              |
| ------ | ------------------------------------------- |
| **C₁** | `isNameValid(s.getSubscriptionsName())`     |
| **C₂** | `isCostValid(s.getCost())`                  |
| **C₃** | `isCycleTypeValid(s.getBillingCycleType())` |


| ID     | System action                                           |
| ------ | ------------------------------------------------------- |
| **A₁** | *Reject* → return `false` due to **invalid name**       |
| **A₂** | *Reject* → return `false` due to **invalid cost**       |
| **A₃** | *Reject* → return `false` due to **invalid cycle type** |
| **A₄** | *Accept* → insert row and return `true`                 |



#### 4.3.1.2 Full Decision Table (8 Rules)

| Rule | C₁ | C₂ | C₃ | A₁ | A₂ | A₃ | A₄ |
| ---- | -- | -- | -- | -- | -- | -- | -- |
| R1   | F  | –  | –  | X  |    |    |    |
| R2   | T  | F  | –  |    | X  |    |    |
| R3   | T  | T  | F  |    |    | X  |    |
| R4   | T  | T  | T  |    |    |    | X  |
| R5   | F  | F  | –  | X  |    |    |    |
| R6   | F  | T  | F  | X  |    |    |    |
| R7   | T  | F  | F  |    | X  |    |    |
| R8   | F  | F  | F  | X  |    |    |    |


“–” means “don’t-care” (condition value irrelevant for that rule).

#### 4.3.1.3 Rule Reduction
Because rules R5–R8 trigger the same action as R1–R3, they are merged, leaving the minimal decision table below:

| Rule   | C₁ | C₂ | C₃ | Result                       |
| ------ | -- | -- | -- | ---------------------------- |
| **R1** | F  | –  | –  | `false` — invalid name       |
| **R2** | T  | F  | –  | `false` — invalid cost       |
| **R3** | T  | T  | F  | `false` — invalid cycle type |
| **R4** | T  | T  | T  | `true` — success             |


#### 4.3.1.4 Derived Test Cases

| TC  | Rule | Representative input (`name`, `cost`, `cycle`) | JUnit method                                                | Expected           |
| --- | ---- | ---------------------------------------------- | ----------------------------------------------------------- | ------------------ |
| DT1 | R1   | `("", 9.99, "monthly")`                        | `addSubscription_EmptyName_ReturnsFalse`                    | `false`            |
| DT2 | R2   | `("Netflix", -1.00, "monthly")`                | `addSubscription_NegativeCost_ReturnsFalse`                 | `false`            |
| DT3 | R3   | `("Netflix", 9.99, "weekly")`                  | `addSubscription_InvalidCycle_ReturnsFalse` | `false`            |
| DT4 | R4   | `("Spotify", 8.99, "monthly")`                 | `addSubscription_ValidSubscription_True`                    | `true` & row in DB |


**Coverage Claim** – The four test cases satisfy **Each-Rule** and **Each-Action** coverage; they also achieve **pair-wise (2-way) combination** because any two conditions toggle across the set.

#### 4.3.2 Decision Tables - handleDeleteSubscription
This routine first checks that the requested subscription exists and is owned by the caller; only then does it delegate to db_module.deleteSubscription().


#### 4.3.2.1 Condition & Action Stubs

| ID     | Condition stub (predicate)  | Description                       |
| ------ | --------------------------- | --------------------------------- |
| **C₁** | `subscriptionExists(subId)` | row with `subId` is present in DB |
| **C₂** | `isOwner(userId, subId)`    | row belongs to calling user       |

| ID     | Action stub (system response)                                      |
| ------ | ------------------------------------------------------------------ |
| **A₁** | **Reject** – return `false`                                        |
| **A₂** | **Accept** – call `db.deleteSubscription()` and return its Boolean |


#### 4.3.2.2 Full Decision Table (4 raw rules)

| Rule | C₁    | C₂    | A₁    | A₂    |
| ---- | ----- | ----- | ----- | ----- |
| R1   | **F** | –     | **X** |       |
| R2   | **T** | **F** | **X** |       |
| R3   | **T** | **T** |       | **X** |
| R4   | **F** | **F** | **X** |       |

“–” means “don’t-care” (condition value irrelevant for that rule).


#### 4.3.2.3 Rule Reduction
Because rule R4 trigger the same action as R1, they are merged, leaving the minimal decision table below:

| Rule   | C₁ | C₂ | Result                                                   |
| ------ | -- | -- | -------------------------------------------------------- |
| **R1** | F  | –  | `false` — subscription not found                         |
| **R2** | T  | F  | `false` — caller not owner                               |
| **R3** | T  | T  | Boolean from `db.deleteSubscription()` (normally `true`) |

#### 4.3.2.4 Derived Test Cases

| TC           | Covers Rule | Scenario                                  | JUnit / UI test                                               | Expected             |
| ------------ | ----------- | ----------------------------------------- | ------------------------------------------------------------- | -------------------- |
| **DT-DEL-1** | R1          | `subId` not in DB                         | `UITest.testDeleteSubscription_NonExistentSubscription_False` | `false`              |
| **DT-DEL-2** | R2          | `subId` exists but owned by another user  | `UITest.testDeleteSubscription_NotOwnedByUser_False`          | `false`              |
| **DT-DEL-3** | R3          | `subId` exists **and** is owned by caller | `UITest.testDeleteSubscription_ValidDeletion_True`            | `true` & row removed |


**Coverage Claim** – The three test cases satisfy **Each-Rule** and **Each-Action** coverage; they also achieve **pair-wise (2-way) combination** because any two conditions toggle across the set.

#### 4.3.3 Decision Table Summary Tables

#### addSubscription

| Path ID | Test Requirement (Rule / Scenario)             | JUnit Test Case Name                          | Expected Outcome                  | Actual Result\* |
| ------- | ---------------------------------------------- | --------------------------------------------- | --------------------------------- | --------------- |
| **R1**  | Reject invalid **name** (`C₁ = F`)             | `addSubscription_EmptyName_ReturnsFalse()`    | returns `false`; no DB insert     | ✅ As Expected   |
| **R2**  | Reject invalid **cost** (`C₂ = F`)             | `addSubscription_NegativeCost_ReturnsFalse()` | returns `false`; no DB insert     | ✅ As Expected   |
| **R3**  | Reject invalid **cycle type** (`C₃ = F`)       | `addSubscription_InvalidCycle_ReturnsFalse()` | returns `false`; no DB insert     | ✅ As Expected   |
| **R4**  | Accept when all predicates true → row inserted | `addSubscription_ValidSubscription_True()`    | returns `true`; row present in DB | ✅ As Expected   |

#### handleDeleteSubscription

| Path ID | Test Requirement (Rule / Scenario)                       | JUnit / UI Test Case Name                                     | Expected Outcome              | Actual Result\* |
| ------- | -------------------------------------------------------- | ------------------------------------------------------------- | ----------------------------- | --------------- |
| **R1**  | Row **does not exist** (`C₁ = F`)                        | `UITest.testDeleteSubscription_NonExistentSubscription_False` | returns `false`; DB unchanged | ✅ As Expected   |
| **R2**  | Row exists but **caller not owner** (`C₁ = T`, `C₂ = F`) | `UITest.testDeleteSubscription_NotOwnedByUser_False`          | returns `false`; row remains  | ✅ As Expected   |
| **R3**  | Row exists **and** caller is owner (`C₁ = T`, `C₂ = T`)  | `UITest.testDeleteSubscription_ValidDeletion_True`            | returns `true`; row removed   | ✅ As Expected   |


---

### 4.4 State-Transition Testing & Node Coverage

We model the UI flows as a finite-state machine to ensure each transition is exercised. Our node coverage met a 100% requirement. 

```mermaid
stateDiagram-v2
    [*] --> [1]LoggedOut
    [1]LoggedOut --> [2]LoginPrompt: handleLogin()
    [2]LoginPrompt --> [3]LoggedIn: validCreds
    [2]LoginPrompt --> [1]LoggedOut: cancel/invalid
    [3]LoggedIn --> [4]MainMenu: displayMenu()
    [4]MainMenu --> [5]AddFlow: handleAddSubscription()
    [5]AddFlow --> [4]MainMenu: success/cancel
    [4]MainMenu --> [6]ViewFlow: handleViewSubscriptions()
    [6]ViewFlow --> [4]MainMenu: return
    [4]MainMenu --> [7]UpdateFlow: handleUpdateSubscription()
    [7]UpdateFlow --> [4]MainMenu: success/cancel
    [4]MainMenu --> [8]DeleteFlow: handleDeleteSubscription()
    [8]DeleteFlow --> [4]MainMenu: success/reject
    [4]MainMenu --> [9]ExportFlow: exportToCSV()
    [9]ExportFlow --> [4]MainMenu: return
    [4]MainMenu --> [1]LoggedOut: Quit
```

#### 4.4.1 Node-to-Test Mapping

| Node | State       | Test Method(s)                                                    |
| ---- | ----------- | ----------------------------------------------------------------- |
| 1    | LoggedOut   | `UITest.testStartUp_ShowsLogin()`                                 |
| 2    | LoginPrompt | `UITest.testInvalidLogin_ReturnsToPrompt()`                       |
| 3    | LoggedIn    | `UITest.testValidLogin_LeadsToMenu()`                             |
| 4    | MainMenu    | `UITest.testDisplayMenu_AfterLogin()`                             |
| 5    | AddFlow     | `UITest.testHandleAddSubscription_Valid_ReturnsTrue()`            |
| 6    | ViewFlow    | `UITest.testViewAllSubscriptions_WithSubscriptions_ReturnsTrue()` |
| 7    | UpdateFlow  | `UITest.testHandleUpdateSubscription_ValidUpdate_ReturnsTrue()`   |
| 8    | DeleteFlow  | `UITest.testDeleteSubscription_ValidDeletion_True()`              |
| 9    | ExportFlow  | `UITest.testExportToCSV_WithSubscriptions_ReturnsTrue()`          |

* **Coverage:** 9/9 nodes exercised → **100% node coverage**.
---

#### 4.4.2 State-Transision Testing Table

| Node | Test Requirement (State/Transition)                         | JUnit Test Method Name                                            | Expected Result                     | Actual Result   |
|------|-------------------------------------------------------------|-------------------------------------------------------------------|--------------------------------------|-----------------|
| N1   | Entering initial LoggedOut state                            | `UITest.testStartUp_ShowsLogin()`                                 | Login prompt shown                   | ✅ As Expected  |
| N2   | Handle login prompt and invalid login (stay in N2/N1 loop)  | `UITest.testInvalidLogin_ReturnsToPrompt()`                       | Returns to login                     | ✅ As Expected  |
| N3   | Valid login transition to LoggedIn state                    | `UITest.testValidLogin_LeadsToMenu()`                             | Proceeds to menu                     | ✅ As Expected  |
| N4   | Main menu state presented                                   | `UITest.testDisplayMenu_AfterLogin()`                             | Menu options visible                 | ✅ As Expected  |
| N5   | Transition to AddFlow, then return to MainMenu              | `UITest.testHandleAddSubscription_Valid_ReturnsTrue()`            | Subscription added, return to menu   | ✅ As Expected  |
| N6   | ViewFlow state → view subscriptions                         | `UITest.testViewAllSubscriptions_WithSubscriptions_ReturnsTrue()` | Subscription list shown              | ✅ As Expected  |
| N7   | UpdateFlow → valid update and return                        | `UITest.testHandleUpdateSubscription_ValidUpdate_ReturnsTrue()`   | Update confirmed                     | ✅ As Expected  |
| N8   | DeleteFlow → valid delete and return                        | `UITest.testDeleteSubscription_ValidDeletion_True()`              | Row removed                          | ✅ As Expected  |
| N9   | ExportFlow → export CSV and return                          | `UITest.testExportToCSV_WithSubscriptions_ReturnsTrue()`          | File created, returns to menu        | ✅ As Expected  |

### 4.5 Use-Case Testing

Use-case testing validates complete user journeys rather than isolated functions. For each core story we document - in the same style used elsewhere in this document - the happy path, every extension (alternate/exception flow), and the concrete JUnit or integration test that drives the path.

#### 4.5.1 UC-01 Log in / Log out

| Item                      | Details                                                                                                                             |
| ------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| **Goal**                  | User gains authenticated access and eventually logs out.                                                                            |
| **Primary actor**         | Registered user                                                                                                                     |
| **Pre-conditions**        | Application running; user account exists                                                                                            |
| **Main success scenario** | 1. Display login prompt → 2. User enters valid credentials → 3. System shows main menu → 4. User selects **Quit** → 5. Session ends |
| **Extensions**            | **E1**: Invalid credentials ⇒ show error and re-prompt (loop to step 1).                                                            |
| **Post-condition**        | Session closed, no lingering state                                                                                                  |

| TC ID    | Path          | Driver                                    | Expected        |
| -------- | ------------- | ----------------------------------------- | --------------- |
| UC-01-H  | happy         | `UITest.testValidLogin_LeadsToMenu`       | Menu shown      |
| UC-01-E1 | invalid creds | `UITest.testInvalidLogin_ReturnsToPrompt` | Re-prompt login |


#### 4.5.2 UC-02 Add Subscription

| Item                      | Details                                                                                                                  |
| ------------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| **Goal**                  | User records a new subscription                                                                                          |
| **Pre-conditions**        | UC-01 completed (user at main menu)                                                                                      |
| **Main success scenario** | 1. Choose “Add” → 2. Enter valid name/cost/cycle/date → 3. System stores subscription → 4. Confirmation shown            |
| **Extensions**            | **E1**: Any field invalid ⇒ show error, stay in Add menu.<br>**E2**: User cancels ⇒ return to main menu without changes. |
| **Post-condition**        | New row in `Subscriptions` table (unless E1/E2)                                                                          |

| TC ID    | Path          | Driver                                                    | Expected            |
| -------- | ------------- | --------------------------------------------------------- | ------------------- |
| UC-02-H  | happy         | `UITest.testHandleAddSubscription_Valid_ReturnsTrue`      | Row created, `true` |
| UC-02-E1 | invalid input | `db_moduleTest.addSubscription_NegativeCost_ReturnsFalse` | `false`, no row     |
| UC-02-E2 | cancelled     | `UITest.testHandleAddSubscription_Cancel_ReturnsFalse`    | `false`, no row     |

#### 4.5.3 UC-03 View Subscriptions

| Item                      | Details                                                           |
| ------------------------- | ----------------------------------------------------------------- |
| **Goal**                  | User lists existing subscriptions (optionally sorted)             |
| **Main success scenario** | 1. Choose “View” → 2. Select sort order → 3. System displays list |
| **Extensions**            | **E1**: No rows ⇒ show “none found”, return `false`.              |
| **Post-condition**        | Screen refreshed; no DB change                                    |

| TC ID    | Path       | Driver                                                          | Expected               |
| -------- | ---------- | --------------------------------------------------------------- | ---------------------- |
| UC-03-H  | happy      | `UITest.testViewAllSubscriptions_WithSubscriptions_ReturnsTrue` | `true`, list printed   |
| UC-03-E1 | empty list | `UITest.testViewAllSubscriptions_NoSubscriptions_ReturnsFalse`  | `false`, message shown |

#### 4.5.4 UC-04 Update Subscription

| Item                      | Details                                                                                                                               |
| ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| **Goal**                  | User edits an existing subscription                                                                                                   |
| **Main success scenario** | 1. Choose “Update” → 2. Enter ID → 3. Provide valid edits → 4. System updates DB → 5. Confirmation shown                              |
| **Extensions**            | **E1**: ID not found ⇒ error, stay in Update.<br>**E2**: Invalid edits ⇒ error, remain in Update.<br>**E3**: Cancel ⇒ abandon update. |
| **Post-condition**        | Row updated (happy only)                                                                                                              |

| TC ID    | Path         | Driver                                                        | Expected            |
| -------- | ------------ | ------------------------------------------------------------- | ------------------- |
| UC-04-H  | happy        | `UITest.testHandleUpdateSubscription_ValidUpdate_ReturnsTrue` | `true`, row changed |
| UC-04-E1 | ID not found | `UITest.testHandleUpdateSubscription_InvalidId_ReturnsFalse`  | `false`, no change  |
| UC-04-E2 | bad data     | `db_moduleTest.updateSubscription_NegativeCost_ReturnsFalse`  | `false`, no change  |


#### 4.5.5 UC-05 Delete Subscription

| Item                      | Details                                                                                            |
| ------------------------- | -------------------------------------------------------------------------------------------------- |
| **Goal**                  | User removes a subscription they own                                                               |
| **Main success scenario** | 1. Choose “Delete” → 2. Enter ID → 3. System verifies ownership → 4. Row deleted → 5. Confirmation |
| **Extensions**            | **E1**: ID does not exist ⇒ error.<br>**E2**: Not owner ⇒ error.<br>**E3**: User cancels.          |
| **Post-condition**        | Row removed (happy only)                                                                           |

| TC ID    | Path        | Driver                                                        | Expected         |
| -------- | ----------- | ------------------------------------------------------------- | ---------------- |
| UC-05-H  | happy       | `UITest.testDeleteSubscription_ValidDeletion_True`            | `true`, row gone |
| UC-05-E1 | ID missing  | `UITest.testDeleteSubscription_NonExistentSubscription_False` | `false`          |
| UC-05-E2 | wrong owner | `UITest.testDeleteSubscription_NotOwnedByUser_False`          | `false`          |


#### 4.5.6 UC-06 Export to CSV

| Item                      | Details                                                                        |
| ------------------------- | ------------------------------------------------------------------------------ |
| **Goal**                  | User exports their subscriptions to a CSV file                                 |
| **Main success scenario** | 1. Choose “Export” → 2. System queries DB → 3. CSV generated → 4. Confirmation |
| **Extensions**            | **E1**: No subscriptions ⇒ CSV has only header; return `false`.                |
| **Post-condition**        | File created on disk (happy or E1)                                             |

| TC ID    | Path       | Driver                                                 | Expected               |
| -------- | ---------- | ------------------------------------------------------ | ---------------------- |
| UC-06-H  | happy      | `UITest.testExportToCSV_WithSubscriptions_ReturnsTrue` | `true`, file with rows |
| UC-06-E1 | empty list | `UITest.testExportToCSV_NoSubscriptions_ReturnsFalse`  | `false`, header only   |


#### 4.5.7 End-to-End Scenario Map

The figure below consolidates the main success paths (UC-02 → UC-03 → UC-04 → UC-05 → UC-06) with login/logout wrapping, matching the CLI flow.

```mermaid
flowchart TD
  A[Start: Login] --> B{Valid?}
  B -->|Yes| C[Main Menu]
  B -->|No| A
  C --> D[Add Subscription]
  D --> E{Valid Input?}
  E -->|Yes| F[Show Confirmation]
  E -->|No| D
  F --> G[List Subscriptions]
  G --> H[Update Subscription]
  H --> I{Valid Update?}
  I -->|Yes| J[Confirm Update]
  I -->|No| H
  J --> K[Delete Subscription]
  K --> L{Valid Delete?}
  L -->|Yes| M[Confirm Delete]
  L -->|No| K
  M --> N[Logout]
```
Each labelled node is reached by at least one of the test cases in UC-01 – UC-06, ensuring full traversal of the primary system workflow along with all significant alternate branches.


* **Main Path:** `handleAddSubscription` → `getAllSubscriptionsForUser` → `updateSubscription` → `deleteSubscription` → `logout`
* **Alternates:** invalid login, input errors on add/update/delete, user-canceled operations.

---

## 5. System Testing & Coverage
System testing validates the complete NextPay application through end-to-end CLI scenarios and finite state machine modeling. We achieved 93 JUnit tests with zero failures, covering full user workflows (Login → Add → List → Update → Delete → Export), CLI navigation paths, and data persistence verification. Node coverage ensures all application states and transitions are tested through comprehensive FSM analysis.

We performed **system testing** across the full CLI application, driving end-to-end scenarios via the UI module and verifying persistence in SQLite. 93 JUnit tests ran with zero failures, covering:

* **Login** → Add → List → Update → Delete → Export flows
* CLI menu navigation and error paths
* Data persistence and CSV output

---

### 5.1 Finite State Machine & Node Coverage

We verified **node coverage** of the key application states via a finite-state machine (FSM). Each numbered transition maps to a UI action:

```mermaid
stateDiagram-v2
  [*] --> Initialize: start application
  Initialize --> LoginPrompt: displayStartScreen()
  LoginPrompt --> LoggedIn: handleLogin(success)
  LoginPrompt --> [*]: handleStartSelection(Quit)
  LoggedIn --> MainMenu: displayMainMenu()

  MainMenu --> AddFlow: handleMainMenuSelection(1)
  AddFlow --> EnterAddDetails: displayAddSubscriptionMenu()
  EnterAddDetails --> ValidateAdd: handleAddSubscription(userId)
  ValidateAdd --> MainMenu: return to menu

  MainMenu --> ViewFlow: handleMainMenuSelection(3)
  ViewFlow --> ValidateView: handleViewSubscriptions(userId, choice)
  ValidateView --> MainMenu: return to menu

  MainMenu --> UpdateFlow: handleMainMenuSelection(4)
  UpdateFlow --> ValidateUpdate: handleUpdateSubscription(userId, subId)
  ValidateUpdate --> MainMenu: return to menu

  MainMenu --> DeleteFlow: handleMainMenuSelection(2)
  DeleteFlow --> ValidateDelete: handleDeleteSubscription(userId, subId)
  ValidateDelete --> MainMenu: return to menu

  MainMenu --> ExportFlow: handleMainMenuSelection(5)
  ExportFlow --> ExecuteExport: exportToCSV(userId)
  ExecuteExport --> MainMenu: return to menu

  MainMenu --> LoggedOut: handleMainMenuSelection(6)
  LoggedOut --> [*]: end session
```

Every state and transition was exercised by at least one test, ensuring complete node coverage.

---

### 5.2 Test & Coverage Summary

**Total Tests:** 94 JUnit tests across all modules with zero failures.

| Test Class | Target Module | Coverage | Key Testing Areas |
|------------|---------------|----------|-------------------|
| `UITest` | `UIModule.java` | **87.80%** | CLI navigation, menu handling, user input validation, login flows |
| `db_moduleTest` | `db_module.java` | **85.35%** | Database operations, JDBC connections, CRUD operations, SQL queries |
| `subscriptions_moduleTest` | `subscriptions_module.java` | **91.67%** | Business logic, subscription validation, user ownership checks |
| `AppTest` | `App.java` | **0.00%** | Application entry point, module wiring (limited coverage by design) |
| N/A | `Subscription.java` | **58.93%** | Model class getters/setters, toString() methods |
| N/A | `User.java` | **0.00%** | Simple getters/setters only (trivial methods) |

![alt text](Docs/image.png)

**Coverage Notes:**
- Core logic methods exceed 85% coverage across all main modules
- Model classes have lower coverage due to trivial getters/setters and untested `toString()` methods
- UI display methods are difficult to automate but navigation handlers are fully tested
- App.java intentionally has lower coverage as it primarily wires modules together
- **Model classes** (`Subscription`, `User`) have minimal testing (getters/setters, `toString()`)—low risk but lowers overall coverage.
- **UI menus** and CLI prompts are difficult to fully automate; while we test navigation handlers, the `display*` methods are not directly asserted.
- **Main entry point** (`App.java`): not covered by unit tests, as it simply wires modules and would require heavier integration tooling.


---
