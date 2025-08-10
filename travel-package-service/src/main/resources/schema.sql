-- Travel Package Service Database Schema (PostgreSQL with R2DBC)
-- Database: travel_package_service_db

CREATE DATABASE IF NOT EXISTS travel_package_service_db;

\c travel_package_service_db;

-- Drop existing tables if they exist (in correct order due to foreign keys)
DROP TABLE IF EXISTS flight CASCADE;
DROP TABLE IF EXISTS hotel CASCADE;
DROP TABLE IF EXISTS travel_packages CASCADE;

-- Travel Packages table
CREATE TABLE travel_packages (
    id VARCHAR(100) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_price_positive CHECK (price > 0),
    CONSTRAINT chk_name_not_empty CHECK (LENGTH(name) > 0)
);

-- Hotels table
CREATE TABLE hotel (
    id VARCHAR(100) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    room_type VARCHAR(100),
    check_in INTERVAL,
    check_out INTERVAL,
    total_rooms INTEGER NOT NULL,
    available_rooms INTEGER NOT NULL,
    id_travel_package VARCHAR(100),
    
    -- Constraints
    CONSTRAINT chk_total_rooms_positive CHECK (total_rooms > 0),
    CONSTRAINT chk_available_rooms_non_negative CHECK (available_rooms >= 0),
    CONSTRAINT chk_available_rooms_not_exceed_total CHECK (available_rooms <= total_rooms),
    CONSTRAINT chk_hotel_name_not_empty CHECK (LENGTH(name) > 0),
    
    -- Foreign key
    CONSTRAINT fk_hotel_travel_package 
        FOREIGN KEY (id_travel_package) 
        REFERENCES travel_packages(id) 
        ON DELETE CASCADE
);

-- Flights table
CREATE TABLE flight (
    id VARCHAR(100) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    flight_number VARCHAR(50) NOT NULL,
    departure_time VARCHAR(100),
    arrival_time VARCHAR(100),
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    airline VARCHAR(100),
    id_travel_package VARCHAR(100),
    
    -- Constraints
    CONSTRAINT chk_total_seats_positive CHECK (total_seats > 0),
    CONSTRAINT chk_available_seats_non_negative CHECK (available_seats >= 0),
    CONSTRAINT chk_available_seats_not_exceed_total CHECK (available_seats <= total_seats),
    CONSTRAINT chk_flight_number_not_empty CHECK (LENGTH(flight_number) > 0),
    CONSTRAINT chk_flight_number_unique UNIQUE (flight_number),
    
    -- Foreign key
    CONSTRAINT fk_flight_travel_package 
        FOREIGN KEY (id_travel_package) 
        REFERENCES travel_packages(id) 
        ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX idx_travel_packages_name ON travel_packages (name);
CREATE INDEX idx_travel_packages_price ON travel_packages (price);
CREATE INDEX idx_travel_packages_created_at ON travel_packages (created_at);

CREATE INDEX idx_hotel_travel_package ON hotel (id_travel_package);
CREATE INDEX idx_hotel_name ON hotel (name);
CREATE INDEX idx_hotel_available_rooms ON hotel (available_rooms);

CREATE INDEX idx_flight_travel_package ON flight (id_travel_package);
CREATE INDEX idx_flight_number ON flight (flight_number);
CREATE INDEX idx_flight_airline ON flight (airline);
CREATE INDEX idx_flight_available_seats ON flight (available_seats);

-- Create a function to update available rooms/seats safely
CREATE OR REPLACE FUNCTION reserve_hotel_rooms(
    room_quantity INTEGER,
    travel_package_id VARCHAR(100)
) RETURNS INTEGER AS $$
DECLARE
    updated_rows INTEGER;
BEGIN
    UPDATE hotel 
    SET available_rooms = available_rooms - room_quantity
    WHERE id_travel_package = travel_package_id 
      AND available_rooms >= room_quantity;
    
    GET DIAGNOSTICS updated_rows = ROW_COUNT;
    RETURN updated_rows;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION reserve_flight_seats(
    seat_quantity INTEGER,
    travel_package_id VARCHAR(100)
) RETURNS INTEGER AS $$
DECLARE
    updated_rows INTEGER;
BEGIN
    UPDATE flight 
    SET available_seats = available_seats - seat_quantity
    WHERE id_travel_package = travel_package_id 
      AND available_seats >= seat_quantity;
    
    GET DIAGNOSTICS updated_rows = ROW_COUNT;
    RETURN updated_rows;
END;
$$ LANGUAGE plpgsql;

-- Create view for package availability summary
CREATE VIEW package_availability AS
SELECT 
    tp.id,
    tp.name,
    tp.price,
    COALESCE(h.available_rooms, 0) as available_rooms,
    COALESCE(h.total_rooms, 0) as total_rooms,
    COALESCE(f.available_seats, 0) as available_seats,
    COALESCE(f.total_seats, 0) as total_seats,
    CASE 
        WHEN h.available_rooms > 0 AND f.available_seats > 0 THEN 'AVAILABLE'
        WHEN h.available_rooms = 0 OR f.available_seats = 0 THEN 'SOLD_OUT'
        ELSE 'UNKNOWN'
    END as availability_status
FROM travel_packages tp
LEFT JOIN hotel h ON tp.id = h.id_travel_package
LEFT JOIN flight f ON tp.id = f.id_travel_package;

-- Sample data insertion
INSERT INTO travel_packages (name, description, price) VALUES
('Paris Weekend Getaway', 'A wonderful 3-day weekend in the City of Light', 899.99),
('Tokyo Adventure', 'Experience the bustling metropolis of Tokyo', 1299.99),
('Caribbean Cruise', 'Relax on a 7-day Caribbean cruise', 1599.99)
ON CONFLICT DO NOTHING;

-- Comments for documentation
COMMENT ON TABLE travel_packages IS 'Main travel package offerings';
COMMENT ON TABLE hotel IS 'Hotel information and availability for packages';
COMMENT ON TABLE flight IS 'Flight information and availability for packages';
COMMENT ON VIEW package_availability IS 'Combined view of package availability status';