Real-Time System Monitoring & GitHub Analytics Dashboard
DevOps Pulse is a comprehensive Spring Boot dashboard that provides real-time system monitoring alongside GitHub user analytics, giving developers a unified view of their local machine health and GitHub profile metrics.



**Features**
System Monitoring
CPU, Memory, and Disk Usage with percentage formatting for easy readability

Process count tracking and system uptime monitoring

ISO 8601 timestamps for standardized time representation

GitHub Integration
User profile analytics including repositories and followers information

Robust error handling for GitHub API responses (404, rate limits)

Custom User-Agent header for proper API etiquette

**Technology Stack**
Backend:

Spring Boot 4.0.1 with Java 25

OSHI 6.6.3 for accurate system metrics collection

Project Lombok for cleaner code

Spring Boot Actuator for application insights

Bean Validation for input sanitization

**Testing:**

JUnit 5 for comprehensive testing

MockRestServiceServer for API mocking

AssertJ for fluent assertions

**Quick Start**
Clone the repository:
git clone https://github.com/nazarkapusta228/devops-pulse.git

Navigate to the project directory:
cd devops-pulse/devops-pulse

Build the project:
mvn clean package

Run the application:
mvn spring-boot:run

Access the dashboard:
Open your browser and navigate to: http://localhost:8080/

**Dashboard Components**
Once running, the dashboard displays:

Real-time system resource utilization (CPU, Memory, Disk)

Active process count and system uptime

GitHub user statistics (when configured with appropriate credentials)

Clean, responsive interface with visual indicators

**Configuration**
To use GitHub integration features, you'll need to:

Create a GitHub Personal Access Token

Configure the application with your token

Set the target GitHub username for analytics

**Testing**
Run the complete test suite with:
mvn test

The project includes unit tests for:

System monitoring components
GitHub API integration
Error handling scenarios
Service layer logic



Status: Complete backend with real API integrations


**Author**
Nazar Kapustynskyi 

Monitor your system. Track your GitHub. All in one dashboard.
