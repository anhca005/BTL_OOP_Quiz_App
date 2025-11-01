-- ===========================
--  USERS TABLE
-- ===========================
CREATE TABLE IF NOT EXISTS users (
    user_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    user_name     TEXT NOT NULL,
    email         TEXT UNIQUE NOT NULL,
    password      TEXT NOT NULL,
    role          TEXT NOT NULL CHECK(role IN ('teacher', 'student')),
    average_score REAL DEFAULT 0
);

-- ===========================
--  QUIZZES TABLE
-- ===========================
CREATE TABLE IF NOT EXISTS quizzes (
    quiz_id     INTEGER PRIMARY KEY,
    user_id     INTEGER NOT NULL, -- giáo viên tạo quiz
    title       TEXT NOT NULL,
    description TEXT,
    created_at  TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ===========================
--  QUESTIONS TABLE
-- ===========================
CREATE TABLE IF NOT EXISTS questions (
    question_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    quiz_id         INTEGER NOT NULL,
    question_text   TEXT NOT NULL,
    correct_answer  TEXT NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

-- ===========================
--  OPTIONS TABLE
-- ===========================
CREATE TABLE IF NOT EXISTS options (
    option_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    question_id   INTEGER NOT NULL,
    text          TEXT NOT NULL,
    display_order INTEGER,
    FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);

-- ===========================
--  RESULTS TABLE
-- ===========================
CREATE TABLE IF NOT EXISTS results (
    result_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     INTEGER NOT NULL,
    quiz_id     INTEGER NOT NULL,
    score       REAL NOT NULL,
    submitted_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);
