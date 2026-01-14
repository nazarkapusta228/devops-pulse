DevOps Pulse 
A Spring Boot dashboard for real-time system monitoring + GitHub analytics.

Features
System Monitoring:
CPU/Memory/Disk usage with % formatting
Process count & uptime tracking
ISO 8601 timestamps

GitHub Integration:
User profile analytics (repos, followers)
Error handling (404, rate limits)
Custom User-Agent header

Tech Stack
Spring Boot 4.0.1 + Java 25
OSHI 6.6.3 for system metrics
Lombok + Actuator + Validation
Testing: JUnit 5, MockRestServiceServer, AssertJ

Quick Start
bash
git clone https://github.com/nazarkapusta228/devops-pulse.git
cd devops-pulse
mvn clean package
mvn spring-boot:run