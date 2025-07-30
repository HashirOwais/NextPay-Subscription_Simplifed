# TESTING.md

**Note:** All diagrams in this document use Mermaid syntax for flowcharts, ERDs, state machines, and graphs.

## 1. Overview

This document describes the systematic testing plan for NextPay, covering unit tests, integration tests, and validation techniques as per ENSE 375 requirements. All JUnit tests have been implemented; this report outlines the test design, control and data-flow analyses, and key test cases.

---

## 2. Unit Testing

---

### MVP 1: `db_module.addSubscription(Subscription s)`

```mermaid
flowchart TD
  Start --> Validate[Check name and cost validity]
  Validate -- OK --> Insert[Execute INSERT via JDBC]
  Insert --> SetID[Fetch and set generated ID]
  SetID --> ReturnTrue[Return true]
  Validate -- Fail --> ReturnFalse[Return false]
  ReturnFalse --> End
  ReturnTrue --> End
```

**Prime Paths**

- **P1**: Start → Validate(OK) → Insert → SetID → ReturnTrue → End
- **P2**: Start → Validate(Fail) → ReturnFalse → End

**Test Cases**

| ID  | Path | Description                                 | Source Tests                                              | Expected                 |
|-----|------|---------------------------------------------|----------------------------------------------------------|--------------------------|
| TC1 | P1   | Valid subscription (non‑empty name, cost≥0) | `db_moduleTest.addSubscription_ValidSubscription_True`    | returns true; row in DB  |
| TC2 | P2   | Empty name                                  | `db_moduleTest.addSubscription_EmptyName_ReturnsFalse`    | returns false; no insert |
| TC3 | P2   | Negative cost                               | `db_moduleTest.addSubscription_NegativeCost_ReturnsFalse` | returns false; no insert |

---

### MVP 2: `db_module.exportSubscriptions(int userId)`

```mermaid
flowchart TD
  Start --> Query["SELECT * FROM Subscriptions WHERE UserID = ?"]
  Query --> WriteHeader["writer.writeNext(header)"]
  WriteHeader --> Loop{"rs.next()?"}
  Loop -- No --> ReturnFalse["return false"]
  ReturnFalse --> End
  Loop -- Yes --> WriteRow["writer.writeNext(row)"]
  WriteRow --> Loop
  Loop -- EndOfRows --> ReturnTrue["return true"]
  ReturnTrue --> End
```

**Prime Paths**

- **P1** (no rows): Start → Query → WriteHeader → Loop(No) → ReturnFalse → End
- **P2** (some rows): Start → Query → WriteHeader → Loop(Yes…) → WriteRow→…→ ReturnTrue → End

**Test Cases**

| ID  | Path | Description                     | Source Tests                                                                                                     | Expected                                |
|-----|------|---------------------------------|------------------------------------------------------------------------------------------------------------------|-----------------------------------------|
| TC4 | P1   | No subscriptions for user       | `UITest.testExportToCSV_NoSubscriptions_ReturnsFalse`                                                            | returns false; only header              |
| TC5 | P2   | One or more subscriptions exist | `UITest.testExportToCSV_WithSubscriptions_ReturnsTrue`<br>`db_moduleTest.exportSubscriptions_WithValidUser_True` | returns true; CSV file with header+rows |

---

### MVP 3: `db_module.deleteSubscription(int subId)`

```mermaid
flowchart TD
  Start --> Delete[Execute DELETE WHERE SubscriptionID=subId]
  Delete --> ReturnTrue[return true]
  ReturnTrue --> End
```


**Prime Paths**
- **P1**: Start → Delete → ReturnTrue → End

**Test Cases**

| ID  | Path | Description                                      | Source Tests                                                                                                                                                                | Expected                                              |
|-----|------|--------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| TC6 | P1   | Direct delete on existing ID                     | `db_moduleTest.deleteSubscription_ValidId_True`                                                                                                                            | returns true; row removed                             |
| TC7 | P1   | Direct delete on non‑existent ID                 | `db_moduleTest.deleteSubscription_NonExistentId_ReturnsFalse` *(note: test method name)*                                                                                   | returns true/false per implementation; row unaffected |
| TC8 | P1   | UI‑level delete with ownership and non‑ownership | `UITest.testDeleteSubscription_ValidDeletion_True`<br>`UITest.testDeleteSubscription_NonExistentSubscription_False`<br>`UITest.testDeleteSubscription_NotOwnedByUser_False` | UI returns correct boolean and DB state               |

---

### 2.1 Path Testing

* **Target**: `SubscriptionModule.addSubscription(Subscription s)`
  * Paths:
    * Valid input → subscription saved (happy path)
    * Null/empty name → `IllegalArgumentException`
    * Negative cost → validation error

* **Target**: `SubscriptionModule.removeSubscription(id)`
  * Paths:
    * Existing ID → removed successfully
    * Nonexistent ID → returns `false`

### 2.2 Data-Flow Testing

* **Target**: `SubscriptionCSVExporter.export(List<Subscription>)`
  * Definitions:
    * DU1: Header row definition → use
    * DU2: Subscription field definition → use
  * Tests:
    * Single subscription → header + one data row
    * Multiple subscriptions → header + multiple rows
    * Empty list → header only

---

## 3. Integration Testing

* **Modules**: UI Module ↔ Subscriptions Module ↔ Database Module
* **Scenario**: add → view → delete subscription via CLI commands

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

### 4.1 Boundary Value Analysis

| Field        | Boundary Values       | Test Inputs                           | Expected      |
| ------------ | --------------------- | ------------------------------------- | ------------- |
| cost         | 0, 0.01, -0.01        | 0, 0.01, -0.01                        | ok, ok, error |
| name length  | 1, 100, 101 chars     | "A", 100-char string, 101-char string | ok, ok, error |
| renewal date | today, distant future | today, 2099‑12‑31                     | ok, ok        |

### 4.2 Equivalence Class Testing

* **Cost**: Valid > 0; Invalid ≤ 0
* **Name**: Valid length 1–100; Invalid empty or > 100

### 4.3 Decision Table

| Cost Valid | Name Valid | Action |
| ---------- | ---------- | ------ |
| T          | T          | Accept |
| T          | F          | Reject |
| F          | T          | Reject |
| F          | F          | Reject |

### 4.4 State-Transition Testing

Diagrams below ensure transitions between:

* `{NoSubscriptions}` ↔ `{HasSubscriptions}`
* `{LoggedOut}` ↔ `{LoggedIn}`

### 4.5 Use-Case Testing

1. **Use Case 1**: Login → Add → List → Logout
2. **Use Case 2**: Import CSV → Verify count → Export CSV

---

## 5. Module & Data Diagrams

<details>
<summary>Mermaid ERD & Flow</summary>

```mermaid
erDiagram
  USER ||--o{ SUBSCRIPTION : owns
  SUBSCRIPTION {
    int id PK
    string name
    double cost
    boolean recurring
    string cycleType
    date nextBilling
  }
```

```mermaid
flowchart LR
  UIModule --> SubMod[subscriptions_module]
  SubMod --> DBMod[db_module]
  DBMod --> SQLite[(nextpay.db)]
```

</details>

---

## 6. Control-Flow Graphs

#### 6.1 addSubscription

```mermaid
flowchart TD
  Start --> Input[Enter details]
  Input --> Check{Valid?}
  Check -- Yes --> Save[Save to DB] --> End
  Check -- No --> Error[Throw exception] --> End
```

#### 6.2 export CSV

```mermaid
flowchart TD
  Start --> Header[Write header]
  Header --> Loop{has rows?}
  Loop -- Yes --> WriteRow --> Loop
  Loop -- No --> End
```

---

---

## 7.1 DU Paths & Test Cases

Below are the key definition–use paths for our NextPay core flows, along with corresponding test cases referencing the actual JUnit methods and source files.

### 7.1.1 addSubscription

**DU Path 1 (happy path):**  
```mermaid
flowchart TD
  A[Initialize Subscription s]
  A --> B[def name = s.getSubscriptionsName]
  B --> C[use name in validation db_module.addSubscription]
  C --> D[def cost = s.getCost]
  D --> E[use cost in validation]
  E --> F[def isRecurring = s.isRecurring]
  F --> G[use isRecurring in pstmt.setBoolean]
  G --> H[def cycleType = s.getBillingCycleType]
  H --> I[use cycleType in pstmt.setString]
  I --> J[def date = s.getBillingCycleDate]
  J --> K[use date.toString in pstmt.setString]
  K --> L[def userId = s.getUserID]
  L --> M[use userId in pstmt.setInt]
  M --> N[INSERT executes]
  N --> O[return true]
```

**DU Path 2 (validation fail):**

```mermaid
flowchart TD
  A[Initialize Subscription s]
  A --> B[def name = s.getSubscriptionsName]
  B --> C[use name in validation]
  C --> D[validation fails]
  D --> E[return false]
```

| TC  | Path | Description              | Source Tests                                                               | Expected Result            |
| --- | ---- | ------------------------ | -------------------------------------------------------------------------- | -------------------------- |
| TC1 | P1   | Valid sub → inserted     | `db_moduleTest.addSubscription_ValidSubscription_True()` (db\_module.java) | returns `true` & row in DB |
| TC2 | P2   | Empty name → rejected    | `db_moduleTest.addSubscription_EmptyName_ReturnsFalse()`                   | returns `false`, no insert |
| TC3 | P2   | Negative cost → rejected | `db_moduleTest.addSubscription_NegativeCost_ReturnsFalse()`                | returns `false`, no insert |

---

### 7.1.2 updateSubscription

**DU Path 1 (happy path):**

```mermaid
flowchart TD
  A[Fetch Subscription s from DB]
  A --> B[def name = s.getSubscriptionsName]
  B --> C[use name in non-empty check]
  C --> D[def cost = s.getCost]
  D --> E[use cost in non-negative check]
  E --> F[def cycleType = s.getBillingCycleType]
  F --> G[use cycleType in UPDATE SET BillingCycleType]
  G --> H[def date = s.getBillingCycleDate]
  H --> I[use date.toString in UPDATE SET BillingCycleDate]
  I --> J[execute UPDATE]
  J --> K[return true]
```

**DU Path 2 (validation fail):**

```mermaid
flowchart TD
  A[Fetch s]
  A --> B[def cost = s.getCost]
  B --> C[use cost in non-negative check]
  C --> D[cost less than 0]
  D --> E[return false]
```

| TC  | Path | Description              | Source Tests                                                                                                                    | Expected Result                |
| --- | ---- | ------------------------ | ------------------------------------------------------------------------------------------------------------------------------- | ------------------------------ |
| TC4 | P1   | Valid update → persisted | `UITest.testHandleUpdateSubscription_ValidUpdate_ReturnsTrue()`<br>`db_moduleTest.updateSubscription_ValidUpdate_ReturnsTrue()` | returns `true`, DB row updated |
| TC5 | P2   | Negative cost → rejected | `db_moduleTest.updateSubscription_NegativeCost_ReturnsFalse()`                                                                  | returns `false`, DB unchanged  |
| TC6 | P2   | Empty name → rejected    | `db_moduleTest.updateSubscription_EmptyName_ReturnsFalse()`                                                                     | returns `false`, DB unchanged  |

---

### 7.1.3 deleteSubscription

**DU Path 1:**

```mermaid
flowchart TD
  A[Receive subscriptionId]
  A --> B[def id = subscriptionId]
  B --> C[use id in DELETE FROM Subscriptions WHERE SubscriptionID]
  C --> D[execute DELETE]
  D --> E[return true]
```

| TC  | Path | Description                                          | Source Tests                                                                                              | Expected Result            |
| --- | ---- | ---------------------------------------------------- | --------------------------------------------------------------------------------------------------------- | -------------------------- |
| TC7 | P1   | Existing ID → removed                                | `UITest.testDeleteSubscription_ValidDeletion_True()`<br>`db_moduleTest.deleteSubscription_ValidId_True()` | returns `true`, row gone   |
| TC8 | P1   | Non‑existent ID → no error (impl detail)             | `UITest.testDeleteSubscription_NonExistentSubscription_False()`                                           | returns `false` (UI layer) |
| TC9 | P1   | Wrong‑user delete → blocked in subscriptions\_module | `UITest.testDeleteSubscription_NotOwnedByUser_False()`                                                    | returns `false`            |

---

### 7.1.4 exportSubscriptions

**DU Path 1 (no rows):**

```mermaid
flowchart TD
  A[Prepare SELECT statement]
  A --> B[use ResultSet rs]
  B --> C[writer.writeNext header]
  C --> D[rs.next returns false]
  D --> E[return false]
```

**DU Path 2 (with rows):**

```mermaid
flowchart TD
  A[Prepare SELECT statement]
  A --> B[use ResultSet rs]
  B --> C[writer.writeNext header]
  C --> D[rs.next returns true]
  D --> E[writer.writeNext row]
  E --> D
  D --> F[return true] 
```

| TC   | Path | Description        | Source Tests                                                                                                         | Expected Result                       |
| ---- | ---- | ------------------ | -------------------------------------------------------------------------------------------------------------------- | ------------------------------------- |
| TC10 | P1   | No subs for user   | `UITest.testExportToCSV_NoSubscriptions_ReturnsFalse()`                                                              | returns `false`, only header in CSV   |
| TC11 | P2   | One + subs present | `UITest.testExportToCSV_WithSubscriptions_ReturnsTrue()`<br>`db_moduleTest.exportSubscriptions_WithValidUser_True()` | returns `true`, CSV has header + rows |

---

### 7.1.5 viewSubscriptions & sortSubscriptions

**DU Path (view all):**

```mermaid
flowchart TD
  A[Call getAllSubscriptionsForUser userId]
  A --> B[use returned List in console-print loop]
  B --> C[return true or false]
```

| TC   | Description                       | Source Tests                                                      | Expected Result |
| ---- | --------------------------------- | ----------------------------------------------------------------- | --------------- |
| TC12 | view no subs → prints none        | `UITest.testViewAllSubscriptions_NoSubscriptions_ReturnsFalse()`  | returns `false` |
| TC13 | view with subs → prints & returns | `UITest.testViewAllSubscriptions_WithSubscriptions_ReturnsTrue()` | returns `true`  |

**DU Path (sort):**

```mermaid
flowchart TD
  A[Prompt sortOrder]
  A --> B[def order = scanner.nextLine]
  B --> C[use order in db.getAllSubscriptionsSortedByDate order]
  C --> D[loop-print sorted list]
  D --> E[return true or false]
```

| TC   | Description                         | Source Tests                                                                                                                                                       | Expected Result |
| ---- | ----------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ | --------------- |
| TC14 | valid asc/desc → prints & returns   | `UITest.testHandleViewSubscriptions_SortByAsc_Covered()`<br>`UITest.testHandleViewSubscriptions_Case2_DescSortOrder_ReturnsTrue()`                                 | returns `true`  |
| TC15 | invalid or empty list/order → false | `UITest.testHandleViewSubscriptions_Case2_EmptySubscriptions_ReturnsFalse()`<br>`UITest.testHandleViewSubscriptions_Case2_NullReturnFromController_ReturnsFalse()` | returns `false` |

---

> All DU paths above map directly into the JUnit methods in your
> `db_moduleTest.java`, `subscriptions_moduleTest.java` and `UITest.java` files.
> This ensures that every definition–use pair in your code is exercised by at least one test.

---

## 8. System Testing & Node Coverage

* **Finite State Machine** for login & subscription lifecycle
* **Node Coverage**: each state visited

```mermaid
stateDiagram-v2
  [*] --> Initialize: start application
  Initialize --> LoginPrompt: displayStartScreen
  LoginPrompt --> LoggedIn: handleLogin success
  LoginPrompt --> [*]: handleStartSelection Quit
  LoggedIn --> MainMenu: displayMainMenu

  MainMenu --> AddFlow: handleMainMenuSelection 1
  AddFlow --> EnterAddDetails: displayAddSubscriptionMenu
  EnterAddDetails --> ValidateAdd: handleAddSubscription userId
  ValidateAdd --> MainMenu: return to menu

  MainMenu --> ViewFlow: handleMainMenuSelection 3
  ViewFlow --> ValidateView: handleViewSubscriptions userId choice
  ValidateView --> MainMenu: return to menu

  MainMenu --> UpdateFlow: handleMainMenuSelection 4
  UpdateFlow --> ValidateUpdate: handleUpdateSubscription userId subId
  ValidateUpdate --> MainMenu: return to menu

  MainMenu --> DeleteFlow: handleMainMenuSelection 2
  DeleteFlow --> ValidateDelete: handleDeleteSubscription userId subId
  ValidateDelete --> MainMenu: return to menu

  MainMenu --> ExportFlow: handleMainMenuSelection 5
  ExportFlow --> ExecuteExport: exportToCSV userId
  ExecuteExport --> MainMenu: return to menu

  MainMenu --> LoggedOut: handleMainMenuSelection 6
  LoggedOut --> [*]: end session
```

```mermaid
stateDiagram-v2
  LoggedOut --> LoggedIn: handleLogin success
  LoggedIn --> MainMenu: displayMainMenu
  MainMenu --> AddFlow: handleAddSubscription
  AddFlow --> MainMenu: return
  MainMenu --> ViewFlow: handleViewSubscriptions
  ViewFlow --> MainMenu: return
  MainMenu --> UpdateFlow: handleUpdateSubscription
  UpdateFlow --> MainMenu: return
  MainMenu --> DeleteFlow: handleDeleteSubscription
  DeleteFlow --> MainMenu: return
  MainMenu --> ExportFlow: exportToCSV
  ExportFlow --> MainMenu: return
  MainMenu --> LoggedOut: handleMainMenuSelection Quit
```

Every state and transition was exercised by at least one test, ensuring complete node coverage.

### 11.2 Test & Coverage Summary
- **Total tests**: 97 JUnit tests across `UITest`, `db_moduleTest`, `subscriptions_moduleTest`, and `AppTest`.
- **Coverage (via JaCoCo)**:
  - `db_module.java`: **85.98%**
  - `subscriptions_module.java`: **91.67%**
  - `UIModule.java`: **87.80%**
  - `Subscription.java`: **58.93%**
  - `User.java`: **0.00%** (only simple getters/setters)

Most core logic methods exceed 85% coverage; model classes have lower coverage due to trivial getters/setters and untested `toString()`.

### 11.3 Limitations
- **Model classes** (`Subscription`, `User`) have minimal testing (getters/setters, `toString()`)—low risk but lowers overall coverage.
- **UI menus** and CLI prompts are difficult to fully automate; while we test navigation handlers, the `display*` methods are not directly asserted.
- **Main entry point** (`App.java`): not covered by unit tests, as it simply wires modules and would require heavier integration tooling.