# IssueBoard — Requirements

**Document version:** 1.0  
**Owner:** Product (PM)  
**Last updated:** 2025‑08‑12

## 1. Purpose
IssueBoard is a lightweight issue/bug tracking system that enables teams to register, discuss, and resolve work items. It focuses on clarity and speed: create → discuss → move through statuses → report.

## 2. In‑Scope
- Creating, viewing, filtering, and updating issues (work items).
- Commenting on issues.
- Simple workflow with limited, explicit transitions.
- Basic reporting (counts/lists per status and time range).
- Role‑based permissions (Reporter, Developer, Admin).

## 3. Out of Scope
- Complex project configuration, custom fields, or advanced automations.
- Scrum/Kanban boards, sprint planning.
- Integrations with external tools (CI/CD, chat, email) beyond simple event hooks.
- File storage beyond lightweight attachments (optional/future).

## 4. Roles & Permissions
- **Reporter**
    - Create issues.
    - View issues.
    - Comment on own or shared issues.
- **Developer**
    - All of the above.
    - Assign issues (self or others if allowed by policy).
    - Change status according to workflow.
    - Edit title/description/priority of assigned issues.
- **Admin**
    - Full access (manage users, override transitions, reassign ownership).
    - Configure global settings and policies.

> Note: Exact permission checks are defined in the workflow policy (see workflow.md).

## 5. Core Entities (Business View)
- **User**: identifies a person (email, role).
- **Issue**: title, description, priority, status, assignee, creator, timestamps.
- **Comment**: free‑text discussion item linked to an issue (author, created_at).

## 6. Statuses (high‑level)
`OPEN → IN_PROGRESS → RESOLVED → CLOSED`  
(With optional reopen: `RESOLVED → IN_PROGRESS`).  
Detailed rules are in `workflow.md`.

## 7. Priorities
`LOW | MEDIUM | HIGH | CRITICAL`  
Used for filtering and reporting; no automatic SLA enforcement in v1 (KPIs only).

## 8. User Stories & Acceptance Criteria
1) **Create Issue**  
   As a Reporter, I want to create an issue with title, description, priority, and optional assignee so that the problem is registered.  
   **AC:**
- Title (≥ 3 chars) and description (≥ 10 chars) are required.
- Default status is `OPEN`.
- Creator is stored; timestamps set.

2) **Change Status**  
   As a Developer, I want to move issues through the workflow so that progress is reflected.  
   **AC:**
- Only allowed transitions are accepted (see workflow).
- `RESOLVED` and `CLOSED` require a reason/comment.
- Unauthorized users receive a permission error.

3) **Comment**  
   As a User, I want to add comments to discuss details.  
   **AC:**
- Comment is stored with author and timestamp.
- Comments appear in chronological order per issue.

4) **Filter & Search**  
   As a User, I want to filter issues by status, priority, assignee, and creation date range.  
   **AC:**
- Combined filters work (AND semantics).
- Results are paginated and ordered by newest first by default.

5) **Report (Listing/Counts)**  
   As a Manager, I want a report of issues by status and/or within a date range for reviews.  
   **AC:**
- Can generate counts per status and a list export for a given range.
- Time range defaults to last 30 days if not provided.

## 9. Validation Rules (Business)
- Title: 3–200 characters.
- Description: minimum 10 characters.
- Priority: one of defined values.
- Status: one of defined values and only via allowed transitions.
- Assignee (if present) must be an existing user.

## 10. Error Handling (Contract)
- Provide structured error responses with:
    - `timestamp`, `path`, `error` (type), `message`, optional `details[]`.
- Typical errors: ValidationError, PermissionDenied, NotFound, Conflict (illegal transition).

## 11. Auditing
- For Issues and Comments: store `created_at`, `updated_at` (if applicable), and `created_by` where relevant.
- For status changes: store `changed_by`, `changed_at`, `from_status`, `to_status` (as event record or log).

## 12. Reporting Requirements (v1)
- **Counts per status** for a date range (based on creation date or last update—policy defined in workflow).
- **Export list** of issues with filters applied (status, priority, assignee, date range).

## 13. Non‑Functional Requirements
- Usability: simple, clean flows; essential fields only.
- Performance: typical list/filter operations should respond under 500 ms under moderate load.
- Security: role checks on all mutating actions; sensible defaults (least privilege).
- Reliability: no data loss on common operations; transactional integrity for state transitions.
- Observability: basic logs for create/update/status‑change actions.

## 14. KPIs (Internal)
- Median time in `OPEN` before first transition.
- Median time from `IN_PROGRESS` to `RESOLVED`.
- Reopen rate (ratio of transitions `RESOLVED → IN_PROGRESS`).

## 15. Glossary
- **Issue**: a tracked unit of work (bug/feature/task).
- **Status**: lifecycle state of an issue.
- **Assignee**: the person responsible for progressing the issue.
- **Reporter**: the person who created the issue.

## 16. Open Questions (to be decided)
- Should attachment uploads be supported in v1? (default: no)
- Maximum comment length? (proposal: 2000 chars)
- Default list sort: `created_at DESC` (proposed).

