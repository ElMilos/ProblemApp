-- USERS
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  login VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL CHECK (role IN ('REPORTER','DEVELOPER','ADMIN'))
);

-- ISSUES
CREATE TABLE issues (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  description TEXT NOT NULL,
  status VARCHAR(20) NOT NULL CHECK (status IN ('OPEN','IN_PROGRESS','RESOLVED','CLOSED')),
  priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW','MEDIUM','HIGH','CRITICAL')),
  created_by BIGINT NOT NULL REFERENCES users(id),
  assignee_id BIGINT REFERENCES users(id)
);

-- COMMENTS
CREATE TABLE comments (
  id BIGSERIAL PRIMARY KEY,
  issue_id BIGINT NOT NULL REFERENCES issues(id) ON DELETE CASCADE,
  user_id BIGINT NOT NULL REFERENCES users(id),
  description TEXT NOT NULL
);

CREATE INDEX idx_issues_status_priority_assignee ON issues(status, priority, assignee_id);
