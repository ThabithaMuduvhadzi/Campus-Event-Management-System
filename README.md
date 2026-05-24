# Campus-Event-Management-System
A Java-based event coordination system implementing role-based access control, background thread processing, and data persistence.

## Core Functionalities

* **Role-Based Access Control:** Distinct interfaces and operational permissions for Staff and Student users.
    * **Staff:** Create, update, cancel, and view comprehensive event states, including active registrations and waitlists.
    * **Students:** Browse available events, register using an Event ID, cancel active bookings, and check statuses.
* **Capacity & State Control:** Automated tracking of maximum capacities. The system handles live registration registers and overflows dynamically.
* **Search & Sort Engine:** Built-in algorithms allowing users to query scheduled events using full or partial name matches, or exact calendar dates, with sorting capabilities.

## Technical Architecture

* **Multithreading Concurrency:** Background thread processing is engineered to manage immediate, automatic waitlist promotions the exact millisecond an active student cancels their spot. This ensures background task simulation without freezing the primary UI execution thread.
* **Data Structures & Collections:** Employs optimal collections to organize memory. `ArrayLists` manage dynamic event indices and quick linear searches, while `Queues` enforce a strict first-come, first-served (FIFO) strategy for event waitlists.
* **Data Persistence (File I/O):** Built with custom file serialization streams to ensure that all generated events, registration states, and queue sequences are permanently saved to disk and fully reloaded into memory automatically upon application boot.
* **Defensive Programming:** Implements rigorous input validation loops and custom exception handling wrappers to cleanly reject negative capacities, malformed dates/times, invalid data types, and duplicate candidate sign-ups.

## Getting Started

### Prerequisites
* Java Development Kit installed on your system.

### Compilation and Execution
1. Clone the repository to your local machine:
   ```bash
2. Open your Java Development Kit
3. Select Open Folder and choose the folder you cloned.
4. Run the main program which is the CampusEventSystem.java 
   git clone
