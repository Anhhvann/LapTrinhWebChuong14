# Stage 1: Build WAR với Maven + JDK
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom và source
COPY pom.xml .
COPY src ./src

# Build project tạo file WAR trong target/
RUN mvn clean package -DskipTests

# Stage 2: Runtime với Tomcat
FROM tomcat:10.1.15-jdk17

# Xoá webapps mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR từ stage build
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Set environment variables for Gmail
ENV Gmail_User=anhyeuem1phutthoi@gmail.com
ENV Gmail_Pass=nztvllgzkebxyofv

# Mở port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
