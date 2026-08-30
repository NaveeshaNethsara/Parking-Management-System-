# 🚗 Smart Parking Management System (Java OOP)

A desktop-based Parking Management System built with **Java (Swing GUI)** demonstrating core **Object-Oriented Programming (OOP)** principles, design patterns, and native file-based persistence.

---

## 🌟 Key Features

- **Vehicle Management**: Register, search, update, and manage vehicles (Cars, Motorcycles, Vans).
- **Parking Slot Allocation**: Smart slot assignment with vehicle-slot type compatibility checks.
- **Entry & Exit Automation**: Automatic ticket generation upon entry and duration/fee calculation upon exit.
- **Dynamic Fee Calculation**: Polymorphic pricing based on vehicle type and parked hours:
  - 🚗 **Car**: LKR 100.00 / hour
  - 🏍️ **Motorcycle**: LKR 50.00 / hour
  - 🚐 **Van**: LKR 150.00 / hour
- **Payment & Receipts**: Process payments via Cash, Card, or Online Transfer.
- **Live Dashboard**: Real-time slot availability, occupancy rates, and revenue metrics.
- **Flat-File Persistence**: Persistent storage without external database dependencies using Java File I/O streams.

---

## 🧱 OOP Architecture & Principles

- **Encapsulation**: Private fields, validation checks, and public getters/setters in models.
- **Inheritance**: Subclasses (`Car`, `Motorcycle`, `Van`) extending the abstract base class (`Vehicle`).
- **Polymorphism**: 
  - Dynamic Method Overriding for `calculateParkingFee(hours)`.
  - Compile-time Method Overloading for helper and search utilities.
- **Abstraction**:
  - `Vehicle` abstract base class.
  - `DataStore<T>` generic interface decoupling storage mechanisms from business logic.
- **Software Architecture**: 3-Tier Layered Architecture (Presentation Layer / Service Layer / Data Access Object Layer).

---

## 📁 Project Structure

```
├── data/                  # Persistent storage text files (.txt)
│   ├── vehicles.txt
│   ├── slots.txt
│   ├── tickets.txt
│   └── payments.txt
├── src/
│   ├── data/              # Data access layer (DataStore interface & implementations)
│   ├── gui/               # Swing GUI panels and components
│   ├── models/            # Domain entities (Vehicle, ParkingSlot, ParkingTicket, etc.)
│   ├── services/          # Business logic managers
│   ├── utils/             # Constants, Validators, ID Generators
│   ├── ParkingApp.java    # Application entry point
│   └── SystemTest.java    # Automated test suite
├── run.bat                # Build and launch script
├── test.bat               # Automated test execution script
└── README.md
```

---

## 🚀 How to Run

### Prerequisites
- Java Development Kit (JDK 8 or higher)

### Using Batch Script (Windows)
Double click or run from terminal:
```bash
run.bat
```

### Manual Compilation & Execution
```bash
# Compile
javac -d bin -sourcepath src src/ParkingApp.java

# Run
java -cp bin ParkingApp
```

### Running Tests
```bash
test.bat
```
