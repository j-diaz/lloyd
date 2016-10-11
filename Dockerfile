FROM josediaz30/oracle-jdk8-thin

# File Author
MAINTAINER Jose Diaz

# Bundle app source
COPY . /src

# Install app dependencies
RUN cd /src; ./gradlew lloyd-core:shadow

# Run app
CMD ["java", "jar", "lloyd-core/build/libs/lloyd-core-1.0-all.jar"]
