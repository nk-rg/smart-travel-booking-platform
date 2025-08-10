-- User Service Database Schema (PostgreSQL)
-- Database: user_service_db

CREATE DATABASE IF NOT EXISTS user_service_db;

\c user_service_db;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS users CASCADE;

-- Create ENUM type for user roles
CREATE TYPE user_role_enum AS ENUM ('USER', 'ADMIN');

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth TIMESTAMP,
    role user_role_enum NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT valid_names CHECK (
        LENGTH(first_name) > 0 AND 
        LENGTH(last_name) > 0
    )
);

-- Create indexes for performance
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_active ON users (active);
CREATE INDEX idx_users_role ON users (role);
CREATE INDEX idx_users_created_at ON users (created_at);

-- Create function to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update updated_at
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Insert default admin user (password should be hashed in real application)
INSERT INTO users (email, password, first_name, last_name, role) 
VALUES ('admin@stbp.com', '$2a$10$dummyHashedPassword', 'System', 'Administrator', 'ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Comments for documentation
COMMENT ON TABLE users IS 'User accounts for the Smart Travel Booking Platform';
COMMENT ON COLUMN users.id IS 'Primary key, auto-incrementing user identifier';
COMMENT ON COLUMN users.email IS 'Unique email address used for authentication';
COMMENT ON COLUMN users.password IS 'BCrypt hashed password';
COMMENT ON COLUMN users.role IS 'User role: USER or ADMIN';
COMMENT ON COLUMN users.active IS 'Account status, false for deactivated accounts';