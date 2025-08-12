# IssueBoard — Workflow & Policy

**Document version:** 1.0  
**Owner:** Product (PM) + Team Lead  
**Last updated:** 2025‑08‑12

## 1. Overview
This document defines the lifecycle of an Issue, allowed transitions, required permissions, and business rules for state changes. It also describes event emission for notifications and reporting.

## 2. States
- `OPEN`: Newly created; no work started.
- `IN_PROGRESS`: Work has begun.
- `RESOLVED`: A fix or solution has been implemented and awaits verification.
- `CLOSED`: Verified and finalized; no further work expected.

## 3. Allowed Transitions
- `OPEN → IN_PROGRESS`
- `IN_PROGRESS → RESOLVED`
- `RESOLVED → CLOSED`
- `RESOLVED → IN_PROGRESS` (Reopen)

> All other transitions are **forbidden** by default. Admin may override (policy section 7).

## 4. Transition Rules & Preconditions
### 4.1 General
- The issue must exist and not already be in the target state.
- The user must have permission for the specific transition (see §5).
- Transitions are atomic; partial updates are not allowed.

### 4.2 Required Inputs
- **To `IN_PROGRESS`**: assignee must be set (either existing or set during transition).
- **To `RESOLVED`**: a **resolution note** (comment/message) is required.
- **To `CLOSED`**: a **closure note** and verification (checkbox/flag) are required.
- **Reopen (`RESOLVED → IN_PROGRESS`)**: a **reopen reason** is required.

### 4.3 Validation & Conflict Handling
- Illegal transitions return a Conflict error (e.g., `OPEN → CLOSED` directly).
- If another user changed the issue meanwhile (stale update), return a Conflict with a hint to refresh.

## 5. Permissions Matrix (Who may perform which transition?)
| Transition                     | Reporter | Developer (assignee) | Admin |
|--------------------------------|---------:|----------------------:|------:|
| `OPEN → IN_PROGRESS`           |    ✖     | ✔ (self or assigned)  |  ✔   |
| `IN_PROGRESS → RESOLVED`       |    ✖     | ✔ (assignee)          |  ✔   |
| `RESOLVED → CLOSED`            |    ✔ (*) | ✔ (not required)      |  ✔   |
| `RESOLVED → IN_PROGRESS`       |    ✔     | ✔                      |  ✔   |

(*) Reporter can close after verification if policy allows; otherwise only Admin can close.

Additional policy:
- Editing title/description/priority is allowed for: **assignee** and **admin**.
- Changing assignee: **admin** or **assignee** (self‑assign) depending on team policy.

## 6. Event Model (Observer)
On every successful transition, emit an event:
