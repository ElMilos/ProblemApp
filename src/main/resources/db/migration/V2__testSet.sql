INSERT INTO users(email, password_hash, role) VALUES
  ('alice@example.com', 'hash-alice', 'REPORTER'),
  ('bob@example.com',   'hash-bob',   'DEVELOPER'),
  ('admin@example.com', 'hash-admin', 'ADMIN');

INSERT INTO issues(title, description, status, priority, assignee_id, created_by)
VALUES
  ('Login fails on invalid token', '500 on /auth when token expired', 'OPEN', 'HIGH', NULL, 1),
  ('UI alignment on dashboard', 'Cards overlap on smaller screens', 'IN_PROGRESS', 'MEDIUM', 2, 1),
  ('Add password reset', 'Feature request: reset via email', 'RESOLVED', 'LOW', 2, 1);

INSERT INTO comments(issue_id, author_id, text) VALUES
  (1, 1, 'Happens intermittently on stage'),
  (2, 2, 'Working on CSS grid adjustments'),
  (3, 2, 'Implemented basic flow; needs review');
