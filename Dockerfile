FROM eclipse-temurin

# Install ImageMagick and other dependencies
RUN apt-get update && apt-get install -y imagemagick

# Run apt-get update again before installing Maven
RUN apt-get update

# Install Maven
RUN apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the source code to the container
COPY . /app

# Build the Java project using Maven
RUN mvn clean compile

# Set the PATH environment variable to include ImageMagick binaries
ENV PATH="/usr/local/bin:${PATH}"

# Create a directory to store the converted images
RUN mkdir /converted_images

# Run your Java class and save the converted images to /converted_images directory
CMD ["java", "-cp", "/app/target/classes", "com.mkippe.App"]
