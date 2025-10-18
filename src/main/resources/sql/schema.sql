-- =======================
-- BẢNG USERS
-- =======================
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('teacher', 'student')),
    average_score REAL DEFAULT 0
);

-- =======================
-- BẢNG QUIZZES
-- =======================
CREATE TABLE IF NOT EXISTS quizzes (
    quiz_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    created_by INTEGER NOT NULL,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =======================
-- BẢNG QUESTIONS
-- =======================
CREATE TABLE IF NOT EXISTS questions (
    question_id INTEGER PRIMARY KEY AUTOINCREMENT,
    quiz_id INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_answer TEXT NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

-- =======================
-- BẢNG RESULTS
-- =======================
CREATE TABLE IF NOT EXISTS results (
    result_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    quiz_id INTEGER NOT NULL,
    score REAL NOT NULL,
    taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);
