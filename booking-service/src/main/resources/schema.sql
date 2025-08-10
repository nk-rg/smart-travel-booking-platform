-- Booking Service Database Schema (MySQL)
-- Database: booking_service_db

CREATE DATABASE IF NOT EXISTS booking_service_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE booking_service_db;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS book;

-- Bookings table (entity is named "Book" in Java)
CREATE TABLE book (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    package_id VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    package_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    hotel_id VARCHAR(100),
    flight_id VARCHAR(100),
    
    -- Constraints
    CONSTRAINT chk_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_package_price_positive CHECK (package_price > 0),
    CONSTRAINT chk_total_price_positive CHECK (total_price > 0),
    CONSTRAINT chk_total_price_calculation CHECK (total_price = package_price * quantity),
    
    -- Indexes for performance
    INDEX idx_book_user_id (user_id),
    INDEX idx_book_package_id (package_id),
    INDEX idx_book_created_at (created_at),
    INDEX idx_book_hotel_id (hotel_id),
    INDEX idx_book_flight_id (flight_id)
) ENGINE=InnoDB 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci
  COMMENT='Booking records for travel packages';

-- Create a view for booking analytics
CREATE VIEW booking_summary AS
SELECT 
    DATE(created_at) as booking_date,
    COUNT(*) as total_bookings,
    SUM(quantity) as total_quantity,
    SUM(total_price) as total_revenue,
    AVG(total_price) as avg_booking_value
FROM book 
GROUP BY DATE(created_at)
ORDER BY booking_date DESC;

-- Comments for documentation
ALTER TABLE book COMMENT = 'Main booking records table';
ALTER TABLE book MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key, auto-incrementing booking identifier';
ALTER TABLE book MODIFY COLUMN user_id VARCHAR(100) NOT NULL COMMENT 'Reference to user from user-service';
ALTER TABLE book MODIFY COLUMN package_id VARCHAR(100) NOT NULL COMMENT 'Reference to travel package from travel-package-service';
ALTER TABLE book MODIFY COLUMN quantity INT NOT NULL COMMENT 'Number of bookings for the package';
ALTER TABLE book MODIFY COLUMN package_price DECIMAL(10, 2) NOT NULL COMMENT 'Price per unit of the package';
ALTER TABLE book MODIFY COLUMN total_price DECIMAL(10, 2) NOT NULL COMMENT 'Total price (package_price * quantity)';
ALTER TABLE book MODIFY COLUMN hotel_id VARCHAR(100) COMMENT 'Reference to hotel if part of the package';
ALTER TABLE book MODIFY COLUMN flight_id VARCHAR(100) COMMENT 'Reference to flight if part of the package';