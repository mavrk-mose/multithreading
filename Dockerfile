FROM eclipse-temurin

# Install ImageMagick and other dependencies
RUN apt-get update && apt-get install -y imagemagick

# Set the working directory
WORKDIR /app

# Copy the source code to the container
COPY src /app/src

# Compile the Java source files
RUN javac /app/src/Main.java

# Set the PATH environment variable to include ImageMagick binaries
ENV PATH="/usr/local/bin:${PATH}"

# Run your Java class
CMD ["java", "-cp", "src", "Main"]
